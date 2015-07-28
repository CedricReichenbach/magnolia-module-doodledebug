package ch.unibe.scg.doodle.magnolia.performance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.unibe.scg.doodle.database.DoodleDatabaseMap;

import com.thoughtworks.xstream.XStream;

public class SimFastHBaseMap<T> extends DoodleDatabaseMap<T> {
	private final XStream xstream = new XStream();

	private static final Map<String, String> backMap = new HashMap<>();

	public SimFastHBaseMap(String tableFullName) {
		super(tableFullName);
	}

	@Override
	public void clear() {
		backMap.clear();
	}

	@Override
	public T get(Object key) {
		if (backMap.get(key) == null)
			return null;

		return (T) xstream.fromXML(backMap.get(key));
	}

	@Override
	public Set<String> keySet() {
		return backMap.keySet();
	}

	@Override
	public T put(String key, T value) {
		return (T) backMap.put(key, xstream.toXML(value));
	}

	@Override
	public T remove(Object key) {
		T oldValue = this.get(key);
		backMap.remove(key);
		return oldValue;
	}

	@Override
	public int size() {
		return backMap.size();
	}

	@Override
	public Collection<T> values() {
		Collection<T> values = new ArrayList<>();
		Collection<String> valuesXML = backMap.values();

		for (String valueXML : valuesXML) {
			@SuppressWarnings("unchecked")
			T value = (T) xstream.fromXML(valueXML); // XXX: Better solution?
			values.add(value);
		}

		return values;
	}

}
