import java.util.*;

public class rh2{

	public static void main(String[] args){
		Map<String,Integer> wordValue = new HashMap<String,Integer>();
		String reviews[] = {"The price here was reasonable. They had clean bathrooms.",
							"Their bathrooms were really nice. I loved the sandwiches",
							"sandwiches"};

		Scanner in = new Scanner(System.in);

		Dictionary<String, Integer> d = new Hashtable<String, Integer>();
		d.put("price", 5);
		d.put("service", 5);
		d.put("quality", 5);
		d.put("clean", 5);
		d.put("cleanliness", 5);

		for(String review : reviews){
			System.out.println(review);
			int points = 0;
			//for(String word : review.split("[\\s@&.?$+-]+")){
			for(String word : review.split(" ")){
				if(word.endsWith(".")){
					word = word.substring(0, word.length() - 1);	
				} 
				if(d.get(word) != null){
					points += d.get(word);
				}
				int value = wordValue.containsKey(word) ? wordValue.get(word) : 0;
				wordValue.put(word, value + 1);
			}
		}

		for(String word : wordValue.keySet()){
			System.out.printf("key: %s value: %d\n", word, wordValue.get(word));
		}


	}
}
