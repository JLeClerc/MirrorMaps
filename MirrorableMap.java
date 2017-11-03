package MirrorMaps;

import java.util.List;
import java.util.Map;
import java.util.Vector;

// Primary and Secondary Maps use the ListMultimap structure from com.google.common.collect.Multimap
public interface MirrorableMap<K, V>{
	public Map<K, Vector<V>> getPrimaryMap();
	public Map<V, Vector<K>> getSecondaryMap();
	public Map<K, Integer> getCountersPrimaryMap();
	public Map<V, Integer> getCountersSecondaryMap();
	public List<Map.Entry<K,Integer>> getTopNByKeys(int n);
	public List<Map.Entry<V,Integer>> getTopNByValues(int n);
}
