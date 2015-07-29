package ch.unibe.scg.doodle.magnolia.jcr;

import info.magnolia.context.MgnlContext;
import info.magnolia.context.SimpleContext;
import info.magnolia.context.SystemContext;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.objectfactory.Components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;

import org.apache.jackrabbit.commons.JcrUtils;

import ch.unibe.scg.doodle.database.DoodleDatabaseMap;

import com.thoughtworks.xstream.XStream;

/**
 * Database abstraction for JCR. Groups data using nodes, uses a property for
 * each map entry. Objects are serialized to and deserialized from String for
 * storage, using XStream.
 * 
 * @author Cedric Reichenbach
 *
 * @param <T>
 */
public class JcrDatabase<T> extends DoodleDatabaseMap<T> {

	private static final String PATH_PREFIX = "/doodledebug/";
	private static final String WORKSPACE = "data";
	private static final String NODE_TYPE = "mgnl:content"; // XXX: Ok?

	private final Node node;
	private final XStream xstream = new XStream();

	public JcrDatabase(String mapName) throws LoginException,
			RepositoryException {
		super(mapName);

		assureMgnlContext();

		Session session = MgnlContext.getJCRSession(WORKSPACE);
		node = JcrUtils.getOrCreateByPath(PATH_PREFIX + mapName, NODE_TYPE,
				session);
	}

	private void saveSession() throws AccessDeniedException,
			ItemExistsException, ReferentialIntegrityException,
			ConstraintViolationException, InvalidItemStateException,
			VersionException, LockException, NoSuchNodeTypeException,
			RepositoryException {
		// TODO: Make this async, and "cache" (for performance)
		// Save is expensive especially for nodes with many/big properties
		node.getSession().save(); // XXX: Expensive!
	}

	@SuppressWarnings("unchecked")
	private void assureMgnlContext() {
		// XXX: Correct? Release at some point?
		if (!MgnlContext.hasInstance())
			MgnlContext.setInstance(new SimpleContext(Components
					.getComponent(SystemContext.class)));
	}

	@Override
	public void clear() {
		try {
			Session session = node.getSession();
			node.remove();
			session.save();
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Object key) {
		if (!(key instanceof String))
			return null;
		try {
			if (!node.hasNode((String) key))
				return null;
			Node subNode = node.getNode((String) key);
			if (!subNode.hasProperty((String) key))
				return null;
			Property property = PropertyUtil.getPropertyOrNull(subNode,
					(String) key);
			return (T) xstream.fromXML(property.getValue().getString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<String> keySet() {
		NodeIterator iterator;
		try {
			iterator = node.getNodes();
		} catch (RepositoryException e1) {
			throw new RuntimeException();
		}

		Set<String> result = new HashSet<String>();
		while (iterator.hasNext()) {
			try {
				result.add(iterator.nextNode().getName());
			} catch (RepositoryException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	@Override
	public T put(String key, T value) {
		T previous = this.get(key);

		try {
			Node subNode = NodeUtil.createPath(node, key, NODE_TYPE);
			subNode.setProperty(key, xstream.toXML(value));

			saveSession();
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}

		return previous;
	}

	@Override
	public T remove(Object key) {
		T previous = this.get(key);

		try {
			if (key instanceof String && !node.hasNode((String) key))
				return null;

			node.getNode((String) key).remove();

			saveSession();
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}

		return previous;
	}

	@Override
	public int size() {
		return this.keySet().size();
	}

	@Override
	public Collection<T> values() {
		Collection<T> result = new ArrayList<T>();
		for (String key : this.keySet()) {
			result.add(this.get(key));
		}
		return result;
	}

}
