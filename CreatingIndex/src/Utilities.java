import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.nodes.Document;

public class Utilities {
	private static List<String> stopWords = new ArrayList<String>(
			Arrays.asList("a", "all", "an", "and", "any", "are", "as", "be",
					"been", "but", "by", "few", "for", "have", "he", "her",
					"here", "him", "his", "how", "i", "in", "is", "it", "its",
					"many", "me", "my", "none", "of", "on", "or", "our", "she",
					"some", "the", "their", "them", "there", "they", "that",
					"this", "us", "was", "what", "when", "where", "which",
					"to", "who", "why", "will", "with", "you", "your"));
	private static List<String> stopWordsRelevance = new ArrayList<String>(
			Arrays.asList("a", "all", "an", "and", "any", "are", "as", "be",
					"been", "but", "by", "few", "for", "have", "he", "her",
					"here", "him", "his", "how", "i", "is", "it", "its",
					"many", "me", "my", "none", "of", "or", "our", "she",
					"some", "the", "their", "them", "there", "they", "that",
					"this", "us", "was", "what", "which",
					"to", "who", "why", "will", "with", "you", "your"));
	public static TreeMap<String, Integer> documentFrequency = new TreeMap<String, Integer>();
	// <Word, <docNum, numberOftimesInDoc>>
	public static TreeMap<String, TreeMap<Double, Integer>> listOfDocNos = new TreeMap<String, TreeMap<Double, Integer>>();
	private static TreeMap<String, Integer> documentFrequencyStem = new TreeMap<String, Integer>();
	public static TreeMap<String, TreeMap<Integer, Integer>> listOfDocNosStem = new TreeMap<String, TreeMap<Integer, Integer>>();
	public static StringBuilder docMaxLen = new StringBuilder();
	public static StringBuilder docMaxLenStem = new StringBuilder();
	//<String, (3,8), <Document number:count>>
	
	public static void printDocFreq() {
		for (String i : documentFrequency.keySet())
			System.out.println(i);
	}

	public static void frontEncoding() {
		StringBuilder b = new StringBuilder();
		String prevWord = documentFrequency.firstKey();
		int count = 0;
		for (String i : documentFrequency.keySet()) {
			if (count != 0) {
				if (i.startsWith(prevWord)) {
					String sub = i.replace(prevWord, "");
					b.append(sub.length() + sub);
				} else {
					StringBuilder s = new StringBuilder();
					for (int d = 0; d < i.length(); d++) {
						if (i.charAt(d) != prevWord.charAt(d)) {
							break;
						}
						s.append(i.charAt(d));
					}
					if (s.toString().length() < prevWord.length()) {
						prevWord = i;
					} else
						prevWord = s.toString();
					if (prevWord.length() < 1)
						prevWord = i;
					b.append("\n");
					b.append(prevWord);
				}
			}
			count++;
		}
		System.out.println(b.toString());
	}

	public static void indexInformation(String docNum, String word, int rel) {
		if (!isAStopWord(word))
			try {

				if (!documentFrequency.containsKey(word)) {
					documentFrequency.put(word, rel);
					TreeMap<Double, Integer> myMap = new TreeMap<Double, Integer>();
					myMap.put(Double.parseDouble(docNum), 1);
					listOfDocNos.put(word, myMap);

				} else {
					double num = Double.parseDouble(docNum);
					Set<Double> completedDocs = listOfDocNos.get(word)
							.keySet();
					if (!completedDocs.contains(Double.parseDouble(docNum))) {
						documentFrequency.put(word,rel);
					}
					if (!listOfDocNos.get(word).containsKey(num)) {
						listOfDocNos.get(word).put(num, 0);
					}
					listOfDocNos.get(word).put(num,
							listOfDocNos.get(word).get(num) + 1);
				}
			} catch (Exception e) {
				System.out.println(word + ":" + docNum);
			}

	}

	public static void indexInformationStem(String docNum, String word) {
		try {

			if (!documentFrequencyStem.containsKey(word)) {
				documentFrequencyStem.put(word, 1);
				TreeMap<Integer, Integer> myMap = new TreeMap<Integer, Integer>();
				myMap.put(Integer.parseInt(docNum), 1);
				listOfDocNosStem.put(word, myMap);

			} else {
				int num = Integer.parseInt(docNum);
				Set<Integer> completedDocs = listOfDocNosStem.get(word)
						.keySet();
				if (!completedDocs.contains(Integer.parseInt(docNum))) {
					documentFrequencyStem.put(word,
							documentFrequencyStem.get(word) + 1);
				}
				if (!listOfDocNosStem.get(word).containsKey(num)) {
					listOfDocNosStem.get(word).put(num, 0);
				}
				listOfDocNosStem.get(word).put(num,
						listOfDocNosStem.get(word).get(num) + 1);
			}
		} catch (Exception e) {
			System.out.println(word + ":" + docNum);
		}

	}

	public static Data readFileAndPopulateObject(String filePath) {
		File input = new File(filePath);
		Document doc;
		Data data = new Data();
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
		{
			data.setDocno(input.getName());
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
			data.setText(sb.toString());
			data.tokenize();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return data;
	}
	public static List<Data> readFileAndPopulateObjectAnnotations(String filePath) {
		List<Data> allData = new ArrayList<Data>();
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
		{
			String line;
			while ((line = br.readLine()) != null) {
				Data data = new Data();
				data.setDocno(line.split(",")[0].trim()+"."+line.split(",")[1].trim());
				data.setRelevanceFactorNum(Integer.parseInt(line.split(",")[2].trim()));
				data.setText(line.split(",")[3]);
				data.tokenize();
				allData.add(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return allData;
	}

	public static boolean isANumber(String word) {
		try {
			Double.parseDouble(word);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static TreeMap<String, Integer> stemmer(
			Map<String, Integer> tokenCount) {
		TreeMap<String, Integer> finalCount = new TreeMap<String, Integer>();
		for (String token : tokenCount.keySet()) {
			Stemmer stemObj = new Stemmer();
			int len = token.length();
			stemObj.add(token.toCharArray(), len);
			stemObj.stem();
			{
				String stemmedValue = stemObj.toString();
				if (finalCount.containsKey(stemmedValue)) {
					finalCount.put(stemmedValue, finalCount.get(stemmedValue)
							+ tokenCount.get(token));
				} else {
					finalCount.put(stemmedValue, tokenCount.get(token));
				}
			}

		}
		return finalCount;
	}

	public static int occurOnlyOnce(Map<String, Integer> tokens) {
		int count = 0;
		for (String key : tokens.keySet()) {
			if (tokens.get(key) == 1) {
				count++;
			}
		}
		return count;
	}

	public static double averageTokensPerDocument(int[] perDocument) {
		int sum = 0;
		for (int i : perDocument) {
			sum += i;
		}
		return sum / perDocument.length;
	}

	public static void printThirtyFrequentWords(Map<String, Integer> tokens) {
		int frequentWordCount = 0;
		for (String token : tokens.keySet()) {
			if (frequentWordCount != 30) {
				System.out.println(token + ": " + tokens.get(token));
				frequentWordCount++;
			} else
				break;
		}
		System.out.println();
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {

		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (-(o1.getValue()).compareTo(o2.getValue()));
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static boolean isAStopWord(String word) {
		if (stopWordsRelevance.contains(word.trim())) {
			return true;
		}
		return false;
	}

	public static void printAllDetails() {
		try {
			PrintWriter p = new PrintWriter(new File(
					"annotations_index.uncompressed"));
			// p.println("Key: Number of docs that the term occurs in: List of docs it occurs in");
			//System.out.println(documentFrequency.lastKey());
			//System.out.println(listOfDocNos.lastKey());
			//System.out.println(listOfDocNos.size());
			for (String key : documentFrequency.keySet()) {
				if (!key.isEmpty() && listOfDocNos.containsKey(key)){
					
					p.println(key + ": " + documentFrequency.get(key) + " : "
							+ listOfDocNos.get(key).toString());
				}
					
			}
			// p.println("Key: Number of docs that the term occurs in: List of docs it occurs in");
			// p.println(docMaxLen.toString());
			p.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			PrintWriter p = new PrintWriter(new File("max_tf_doclen.txt"));
			p.println(docMaxLen.toString());
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void printAllDetailsStem() {
		try {
			PrintWriter p = new PrintWriter(new File(
					"index_version2.uncompressed"));
			// p.println("Key: Number of docs that the term occurs in: List of docs it occurs in");
			for (String key : documentFrequencyStem.keySet()) {
				if (!key.isEmpty())
					p.println(key + ": " + documentFrequencyStem.get(key)
							+ " : " + listOfDocNosStem.get(key).toString());
			}
			// p.println("Key: Number of docs that the term occurs in: List of docs it occurs in");
			// p.println(docMaxLenStem.toString());
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			PrintWriter p = new PrintWriter(new File("max_tf_doclen_stem.txt"));
			p.println(docMaxLenStem.toString());
			p.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String getGammaCode(int frequency) {
		String binary = Integer.toBinaryString(frequency);
		String gammaCode = "";
		binary = binary.substring(1); // cut off first leading bit
		for (int i = 0; i < binary.length(); i++) {
			gammaCode = gammaCode + "1";
		}
		gammaCode = gammaCode + "0";
		gammaCode = gammaCode + binary; // length+offset
		return gammaCode;
	}

	public static String getDeltaCode(int documentID) {
		String deltaCode = "";
		String binary = Integer.toBinaryString(documentID);
		String gammaCode = getGammaCode(binary.length());
		binary = binary.substring(1);
		deltaCode = gammaCode + binary;
		return deltaCode;
	}

	public static void getDictionaryAndPostings() {
		try {
			PrintWriter p = new PrintWriter(new File("dictionary_postings.txt"));
			for (String key : documentFrequency.keySet()) {
				p.println(key + " - "
						+ listOfDocNos.get(key).keySet().toString());
			}
			p.close();
		} catch (Exception e) {

		}
	}

	public static void printParticularDetails() {
		String[] names = { "Reynolds", "NASA", "Prandtl", "flow", "pressure",
				"boundary", "shock" };
		try {
			PrintWriter p = new PrintWriter(new File("final_answer.txt"));
			for (String name : names) {
				p.println("Term: " + name);
				String lemma = Lemmatizer.getInstance()
						.getLemma(name.toLowerCase()).trim();
				TreeMap<Double, Integer> docTerm = listOfDocNos.get(lemma);
				p.println("Inverted list length: "
						+ docTerm.toString().getBytes().length);
				p.println("Document frequency: " + documentFrequency.get(lemma));
				p.println("Document Id-Term Frequency");
				for (double id : docTerm.keySet())
					p.print(id + "-" + docTerm.get(id) + ", ");
			}
			p.println();
			p.close();
		} catch (Exception e) {

		}
	}

	public static String stemmize(String word) {
		Stemmer stemObj = new Stemmer();
		stemObj.add(word.toCharArray(), word.length());
		stemObj.stem();
		{
			word = stemObj.toString();
		}
		word = word.trim();
		return word;
	}

	public static void printParticularDetailsStem() {
		String[] names = { "Reynolds", "NASA", "Prandtl", "flow", "pressure",
				"boundary", "shock" };
		try {
			PrintWriter p = new PrintWriter(new File("final_answer_stem.txt"));
			for (String name : names) {
				p.println("Term: " + name);
				String stem = stemmize(name.toLowerCase());
				TreeMap<Integer, Integer> docTerm = listOfDocNosStem.get(stem
						.trim());
				p.println("Inverted list length: "
						+ docTerm.toString().getBytes().length);
				p.println("Document frequency: "
						+ documentFrequencyStem.get(stem));
				p.println("Document Id-Term Frequency");
				for (int id : docTerm.keySet())
					p.print(id + "-" + docTerm.get(id) + ", ");
			}
			p.println();
			p.close();
		} catch (Exception e) {

		}
	}

	public static void compressIndex() {
		try {
			PrintWriter p = new PrintWriter(new File(
					"index_version1.compressed"));
			int count = 0;
			for (String lemma : documentFrequency.keySet()) {
				if (count < 8000) {
					count++;
					Integer[] posting = new Integer[listOfDocNos.get(lemma)
							.keySet().size()];
					posting = listOfDocNos.get(lemma).keySet().toArray(posting);
					int len = posting.length;
					for (int i = 1; i < len; i++) {
						String bytes = getGammaCode(posting[i] - posting[i - 1]);
						p.print(bytes + " ");
					}
					p.println();
				}
			}
			p.close();
		} catch (Exception e) {

		}
	}

	public static void compressIndexStem() {
		try {
			PrintWriter p = new PrintWriter(new File(
					"index_version2.compressed"));
			int count = 0;
			for (String lemma : documentFrequencyStem.keySet()) {
				if (count < 3000) {
					count++;
					Integer[] posting = new Integer[listOfDocNosStem.get(lemma)
							.keySet().size()];
					posting = listOfDocNosStem.get(lemma).keySet()
							.toArray(posting);
					for (int docId : posting) {
						String bytes = getDeltaCode(docId);
						p.print(bytes + " ");
					}
					p.println();
				}
			}
			p.close();
		} catch (Exception e) {

		}
	}
}
