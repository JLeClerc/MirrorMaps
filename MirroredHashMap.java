package MirrorMaps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

class CountersMapEntryComparator implements Comparator<Map.Entry<? , Integer>>{
	public int compare(Map.Entry<?, Integer> e1, Map.Entry<?, Integer> e2){
		//Fallback to a String comparison on value tie to prevent b,b,c,c,c from leading to [c:3] rather than [c:3, b:2]
		if(e1.getValue() == e2.getValue()){
			return e1.getKey().toString().compareTo(e2.getKey().toString());
		}
		return (e1.getValue() ) - (e2.getValue() );
	}
	public boolean equals(Map.Entry<?, Integer> e1, Map.Entry<?, Integer> e2){
		return e1.equals(e2);
	}
}

@SuppressWarnings("serial")
public class MirroredHashMap<K,V> extends HashMap<K,V> implements MirrorableMap<K,V> {
	private Map<K, Vector<V>> primaryMap;
	private Map<V, Vector<K>> secondaryMap;
	private Map<K, Integer> countersPrimaryMap;
	private Map<V, Integer> countersSecondaryMap;
	private TreeSet<Map.Entry<K, Integer>> countersPrimarySorted;
	private TreeSet<Map.Entry<V, Integer>> countersSecondarySorted;
	
	public MirroredHashMap(){
		this.primaryMap = new HashMap<K, Vector<V>>();
		this.secondaryMap = new HashMap<V, Vector<K>>();
		this.countersPrimaryMap = new HashMap<K, Integer>();
		this.countersSecondaryMap = new HashMap<V, Integer>();
		this.countersPrimarySorted = new TreeSet<Map.Entry<K, Integer>>(new CountersMapEntryComparator()); 
		this.countersSecondarySorted = new TreeSet<Map.Entry<V, Integer>>(new CountersMapEntryComparator()); 
	}
	
	// These only return a copy of the maps, in order to avoid a consistency issue between them (similar to database corruption.)
	public Map<K, Vector<V>> getPrimaryMap(){
		return new HashMap<K, Vector<V>>(this.primaryMap);
	}
	public Map<V, Vector<K>> getSecondaryMap(){
		return new HashMap<V, Vector<K>>(this.secondaryMap);
	}
	public Map<K, Integer> getCountersPrimaryMap(){
		return new HashMap<K, Integer>(this.countersPrimaryMap);
	}
	public Map<V, Integer> getCountersSecondaryMap(){
		return new HashMap<V, Integer>(this.countersSecondaryMap);
	}
	
	public ArrayList<Map.Entry<K,Integer>> getTopNByKeys(int n){
		if(n > this.countersPrimarySorted.size()){
			//throw new RuntimeException(String.format("Cannot get top N elements: N (%d) is greater than the total number of keys (%d).", n, this.countersPrimarySorted.size()));
			n = this.countersPrimarySorted.size();
		}
		Iterator<Map.Entry<K, Integer>> iter = this.countersPrimarySorted.descendingIterator();
		ArrayList<Map.Entry<K, Integer>> returnList = new ArrayList<Map.Entry<K, Integer>>();
		for(int i = 0; i < n; i++){
			returnList.add(iter.next());
		}
		return returnList;
	}

	public ArrayList<Map.Entry<V,Integer>> getTopNByValues(int n){
		if(n > this.countersSecondarySorted.size()){
			//throw new RuntimeException(String.format("Cannot get top N elements: N (%d) is greater than the total number of keys (%d).", n, this.countersSecondarySorted.size()));
			n = this.countersSecondarySorted.size();
		}
		Iterator<Map.Entry<V, Integer>> iter = this.countersSecondarySorted.descendingIterator();
		ArrayList<Map.Entry<V, Integer>> returnList = new ArrayList<Map.Entry<V, Integer>>();
		for(int i = 0; i < n; i++){
			returnList.add(iter.next());
		}
		return returnList;
	}
	
	@Override
	public V put(K key, V value){
		V standardReturnValue = null; //standard Map.put() behavior is to return null if K previously had no mappings for V.
		
		//increment the counters before the content. Guard against the entry not yet existing.
		if(!countersPrimaryMap.containsKey(key)){
			countersPrimaryMap.put(key, 0);
		}
		if(!countersSecondaryMap.containsKey(value)){
			countersSecondaryMap.put(value, 0);
		}
		Map.Entry<K,Integer> oldPrimaryEntry = new SimpleImmutableEntry<K,Integer>(key, countersPrimaryMap.get(key));
		Map.Entry<V,Integer> oldSecondaryEntry = new SimpleImmutableEntry<V,Integer>(value, countersSecondaryMap.get(value));
		Map.Entry<K,Integer> newPrimaryEntry = new SimpleImmutableEntry<K,Integer>(oldPrimaryEntry.getKey(), oldPrimaryEntry.getValue() + 1);
		Map.Entry<V,Integer> newSecondaryEntry = new SimpleImmutableEntry<V,Integer>(oldSecondaryEntry.getKey(), oldSecondaryEntry.getValue() + 1);
		countersPrimaryMap.put(newPrimaryEntry.getKey(), newPrimaryEntry.getValue());
		countersSecondaryMap.put(newSecondaryEntry.getKey(), newSecondaryEntry.getValue());
		//update the sorted sets
		countersPrimarySorted.remove(oldPrimaryEntry);
		countersPrimarySorted.add(newPrimaryEntry);
		countersSecondarySorted.remove(oldSecondaryEntry);
		countersSecondarySorted.add(newSecondaryEntry);
		
		//update primaryMap and secondaryMap
		if(primaryMap.containsKey(key)){
			standardReturnValue = primaryMap.get(key).lastElement();
			primaryMap.get(key).add(value);
		} else {
			primaryMap.put(key, new Vector<V>());
			primaryMap.get(key).add(value);
		}
		if(secondaryMap.containsKey(value)){
			secondaryMap.get(value).add(key);
		} else {
			secondaryMap.put(value, new Vector<K>());
			secondaryMap.get(value).add(key);
		}
		
		return standardReturnValue;
	}
	
}
