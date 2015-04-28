package ch.unibe.scg.doodle.magnolia.jcr;

import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

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

		Session session = MgnlContext.getJCRSession(WORKSPACE);
		node = JcrUtils.getOrCreateByPath(PATH_PREFIX + mapName, NODE_TYPE,
				session);
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
		Property property = PropertyUtil.getPropertyOrNull(node, (String) key);
		try {
			return (T) xstream.fromXML(property.getValue().getString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Set<String> keySet() {
		PropertyIterator iterator;
		try {
			iterator = node.getProperties();
		} catch (RepositoryException e1) {
			throw new RuntimeException();
		}

		Set<String> result = new HashSet<String>();
		while (iterator.hasNext()) {
			try {
				result.add(iterator.nextProperty().getName());
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
			node.setProperty(key, xstream.toXML(value));
			node.getSession().save();
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}

		return previous;
	}

	@Override
	public T remove(Object key) {
		T previous = this.get(key);

		try {
			if (key instanceof String && !node.hasProperty((String) key))
				return null;
			node.getProperty((String) key).remove();
			node.getSession().save();
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
