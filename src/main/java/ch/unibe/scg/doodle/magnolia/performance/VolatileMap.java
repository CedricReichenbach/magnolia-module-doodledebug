package ch.unibe.scg.doodle.magnolia.performance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.unibe.scg.doodle.database.DoodleDatabaseMap;

public class VolatileMap<T> extends DoodleDatabaseMap<T> {
	
	private static final Map<String, Object> backMap = new HashMap<>();

	public VolatileMap(String mapName) {
		super(mapName);
	}

	@Override
	public void clear() {
		backMap.clear();
	}

	@Override
	public T get(Object key) {
		return (T) backMap.get(key);
	}

	@Override
	public Set<String> keySet() {
		return backMap.keySet();
	}

	@Override
	public T put(String key, T value) {
		return (T) backMap.put(key, value);
	}

	@Override
	public T remove(Object key) {
		return (T) backMap.remove(key);
	}

	@Override
	public int size() {
		return backMap.size();
	}

	@Override
	public Collection<T> values() {
		return (Collection<T>) backMap.values();
	}
}
