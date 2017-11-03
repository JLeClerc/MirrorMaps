## MirrorMaps
### A solution to your most demanding indexing needs

Let's say that you have 100MB of text. 
* How many times does the word "marlin" appear? 
* Which pages does it appear on? 
* Which page contains the most marlins? 
* Which page has the greatest number of words? 
* What are the 10 most common words in the text? How about the 17 most common?
* How many words are on page 231?

Let's say that you have a non-stop stream of IP addresses and URLs from your webserver log.
* Which URL's did IP 66.249.73.60 visit?
* How many IP's visited "/buyNow.html"? 
* Can you list them? 	
* Which pages had the most visitors?
* Some time has passed. How about now? 

All of these questions and more can be answered in logarithmic time with the usage of a MirrorMap.

### How it Works

When you insert a Key,Value pair into a multimap, it maintains six different data structures.

Four maps based on hash tables:
* Key, [Values]
* Value, [Keys]
* Key, # of values
* Value, # of keys

Two sets based on trees:
* Key, # of values
* Value, # of keys

The first four provide log(n) access to keys from values and values from keys. The last two provide topNKeys(), topNValues(), etc. functionality. 

In short, each question you can ask about the data uses an efficient data structure that is suited to the task. And all data structures are kept consistent with one another through encapsulation of Map.add() by the MirrorMap class.

Input:
```java
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
```
Output:
``` java
		{Alex=[1.1.1.1, 2.2.2.2, 2.3.3.3, 2.2.2.2], Deborah=[4.4.4.4], Erica=[4.4.4.4], Bob=[3.3.3.3, 3.3.3.3], Charlie=[4.4.4.4]}
		{1.1.1.1=[Alex], 2.2.2.2=[Alex, Alex], 3.3.3.3=[Bob, Bob], 4.4.4.4=[Charlie, Deborah, Erica], 2.3.3.3=[Alex]}
		{Alex=4, Deborah=1, Erica=1, Bob=2, Charlie=1}
		{1.1.1.1=1, 2.2.2.2=2, 3.3.3.3=2, 4.4.4.4=3, 2.3.3.3=1}
		[Alex=4, Bob=2, Erica=1, Deborah=1, Charlie=1]
		[4.4.4.4=3, 3.3.3.3=2, 2.2.2.2=2, 2.3.3.3=1, 1.1.1.1=1]
		[Alex=4, Bob=2, Erica=1, Deborah=1, Charlie=1]
		[4.4.4.4=3, 3.3.3.3=2, 2.2.2.2=2, 2.3.3.3=1, 1.1.1.1=1]
   ```