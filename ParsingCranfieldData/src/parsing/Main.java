package parsing;

//javac -d bin -classpath bin:libs/jsoup-1.8.1.jar src/parsing/*.java
//java -classpath bin:libs/jsoup-1.8.1.jar parsing.Main
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	static Map<Integer, Data> allData = new HashMap<Integer, Data>();
	static Map<String, Integer> tokenCount = new HashMap<String, Integer>();
	static int wordsThatOccurOnlyOnce = 0;
	private static int numOfFiles = 700;
	static int[] distinctPerDoc = new int[numOfFiles];
	private static int totalWordCount = 0;
	static long timeTakenToTokenize = 0;
	static long timeTakenToStem = 0;
	private static int[] tokensPerDocument = new int[numOfFiles];
	private static int fileCount = 0;
	private static Map<String, Integer> finalCount = new HashMap<String, Integer>();;
	private static int[] stemsPerDocument = new int[numOfFiles];
	private static int fileCountStem = 0;

	public static void main(String[] args) {
		if(args.length==0){
			System.out.println("Please specify the path to the Cranfield text collection");
			return;
		}
		String path = "/home/shruthi/AllFiles/Sem2/InfoRetrieval/Projecct/Ours/supremeGod/texts";
		partOne(path);
		/*if(args[0].endsWith("/")) 
			 partOne(args[0]); 
		 else
			 partOne(args[0]+"/"); */
		 
		 /*System.out.println();
		 System.out.println("Using stemmer!"); 
		 partTwo();*/
	}

	private static void partOne(String path) {
		readCranfieldTextCollection(path);
		tokenCount = Utilities.sortByValue(tokenCount);

		System.out.println("Time taken to tokenize: " + (timeTakenToTokenize)
				/ 1000.0 + " seconds");
		System.out
				.println("The number of tokens in the Cranfield text collection: "
						+ totalWordCount);
		System.out
				.println("The number of unique words in the Cranfield text collection: "
						+ tokenCount.size());
		System.out
				.println("The number of words that occur only once in the Cranfield text collection: "
						+ wordsThatOccurOnlyOnce);

		System.out
				.println("30 most frequent words in the Cranfield text collection: ");
		Utilities.printThirtyFrequentWords(tokenCount);

		System.out.print("The average number of word tokens per document: "+Utilities.averageTokensPerDocument(tokensPerDocument));
		System.out.println();
	}

	private static void partTwo() {
		finalCount = Utilities.stemmer(tokenCount);
		finalCount = Utilities.sortByValue(finalCount);
		System.out.println("Time taken to stem the tokens: "
				+ (timeTakenToStem) / 1000.0 + " seconds");
		System.out
				.println("The number of distinct stems in the Cranfield text collection: "
						+ finalCount.size());
		System.out
				.println("The number of stems that occur only once in the Cranfield text collection: "
						+ Utilities.occurOnlyOnce(finalCount));
		System.out
				.println("The 30 most frequent stems in the Cranfield text collection: ");
		Utilities.printThirtyFrequentWords(finalCount);
		System.out
				.println("The average number of word stem tokens per document: "
						+ Utilities.averageTokensPerDocument(stemsPerDocument));
		System.out.println();
	}

	private static void readCranfieldTextCollection(String baseUrl) {
		File folder = new File(baseUrl);
		timeTakenToTokenize = 0;
		for (File f : folder.listFiles()) {
			Data temp = new Data();
			temp = Utilities.readFileAndPopulateObject(baseUrl + f.getName());
			tokenizeText(temp.getText());
		}
	}

	public static void tokenizeText(String text) {
		long start = System.currentTimeMillis();
		List<String> uniqueWordsPerDocument = new ArrayList<String>();
		//text="Hey there! \"Its me!\". It's U-S.A";
		text = text.replaceAll("[^a-zA-Z ]", "");
		String a[] = text.split("\\s+");
		for (String word : a) {
			if (!Utilities.isANumber(word)) {
				totalWordCount += 1;
				word = word.toLowerCase().trim();
				if (tokenCount.containsKey(word)) {
					int count = tokenCount.get(word);
					if (count == 1) {
						// if the word occurs more than once, remove that word.
						wordsThatOccurOnlyOnce--;
					}
					tokenCount.put(word, count + 1);
				} else {
					if (!word.isEmpty()) {
						tokenCount.put(word, 1);
						wordsThatOccurOnlyOnce++;

					}
				}
				if (!uniqueWordsPerDocument.contains(word)) {
					uniqueWordsPerDocument.add(word);
				}
			}
		}

		timeTakenToTokenize += System.currentTimeMillis() - start;
		tokensPerDocument[fileCount++] = uniqueWordsPerDocument.size();

		start = System.currentTimeMillis();
		//stemmingPerDocument(uniqueWordsPerDocument);
		timeTakenToStem += System.currentTimeMillis() - start;

	}

	public static void stemmingPerDocument(List<String> tokensPerDoc) {
		Stemmer stm = new Stemmer();
		List<String> stemsPerDoc = new ArrayList<String>();
		for (String token : tokensPerDoc) {
			stm.add(token.toCharArray(), token.length());
			stm.stem();
			{
				String stemmedValue = stm.toString();
				if (!stemsPerDoc.contains(stemmedValue))
					stemsPerDoc.add(stemmedValue);
			}
		}
		stemsPerDocument[fileCountStem++] = stemsPerDoc.size();
	}

}
