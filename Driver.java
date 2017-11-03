package MirrorMaps;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import MirrorMaps.MirroredHashMap;
import MirrorMaps.Timer;

public class Driver {
	public static void main(String[] args){
		test1();
		test2(1000000, 1);
	}
	
	public static void test1(){
		MirroredHashMap<String, String> m = new MirroredHashMap<String, String>();
		m.put("Alex", "1.1.1.1");
		m.put("Alex", "2.2.2.2");
		m.put("Alex", "2.3.3.3");
		m.put("Alex", "2.2.2.2");
		m.put("Bob", "3.3.3.3");
		m.put("Charlie", "4.4.4.4");
		m.put("Deborah", "4.4.4.4");
		m.put("Erica", "4.4.4.4");
		m.put("Bob", "3.3.3.3");
		System.out.println(m.getPrimaryMap().toString());
		System.out.println(m.getSecondaryMap().toString());
		System.out.println(m.getCountersPrimaryMap().toString());
		System.out.println(m.getCountersSecondaryMap().toString());
		System.out.println(m.getTopNByKeys(5).toString());
		System.out.println(m.getTopNByValues(5).toString());
		System.out.println(m.getTopNByKeys(99).toString());
		System.out.println(m.getTopNByValues(99).toString());
		/* Output: 
		{Alex=[1.1.1.1, 2.2.2.2, 2.3.3.3, 2.2.2.2], Deborah=[4.4.4.4], Erica=[4.4.4.4], Bob=[3.3.3.3, 3.3.3.3], Charlie=[4.4.4.4]}
		{1.1.1.1=[Alex], 2.2.2.2=[Alex, Alex], 3.3.3.3=[Bob, Bob], 4.4.4.4=[Charlie, Deborah, Erica], 2.3.3.3=[Alex]}
		{Alex=4, Deborah=1, Erica=1, Bob=2, Charlie=1}
		{1.1.1.1=1, 2.2.2.2=2, 3.3.3.3=2, 4.4.4.4=3, 2.3.3.3=1}
		[Alex=4, Bob=2, Erica=1, Deborah=1, Charlie=1]
		[4.4.4.4=3, 3.3.3.3=2, 2.2.2.2=2, 2.3.3.3=1, 1.1.1.1=1]
		[Alex=4, Bob=2, Erica=1, Deborah=1, Charlie=1]
		[4.4.4.4=3, 3.3.3.3=2, 2.2.2.2=2, 2.3.3.3=1, 1.1.1.1=1]
		 */
	}
	
	public static void test2(int numEntries, int usernameLength){
		// Generate 
		MirroredHashMap<String, InetAddress> m = new MirroredHashMap<String, InetAddress>();
		Timer timer = new Timer();
		List<List> entries = new ArrayList<List>();
		
		for(int i = 0; i < numEntries; i++){
			List tuple = new ArrayList();
			tuple.add(generateUser(usernameLength));
			tuple.add(generateIP());
			entries.add(tuple);
		}
		
		Timer.tic();
		for(List tuple : entries){
			m.put((String)tuple.get(0), (InetAddress)tuple.get(1));
		}
		System.out.println(m.getTopNByKeys(10).toString());
		System.out.println(m.getTopNByValues(10).toString());
		System.out.println("Finished in :" + Timer.toc() + " seconds.");
		
		/* Example Output for test2(1000000, 1)
		[s=38857, b=38705, p=38662, z=38656, w=38607, e=38602, x=38584, c=38571, j=38558, u=38552]
		[/95.219.69.244=2, /93.223.229.180=2, /9.3.201.224=2, /88.114.192.146=2, /86.85.217.79=2, /86.48.40.120=2, /85.129.209.76=2, /83.66.177.1=2, /82.242.115.252=2, /80.178.153.226=2]
		Finished in :11.598976531 nanoseconds.
		*/
	}
	
	public static String generateUser(int len){
		String chars = "abcdefghijklmnopqrstuvwxyz";
		Random random = new Random();
		char[] user = new char[len];
		for(int i = 0; i < len; i++){
			user[i] = chars.charAt(random.nextInt(26));
		}
		return new String(user);
	}
	
	public static InetAddress generateIP(){
		Iterator<Integer> randOctIter = new Random().ints(1,255).iterator();
		try {
			return InetAddress.getByName(String.format("%d.%d.%d.%d", randOctIter.next(),randOctIter.next(),randOctIter.next(),randOctIter.next()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return InetAddress.getLoopbackAddress();
	}
}
