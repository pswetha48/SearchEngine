package parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

public class Utilities {

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
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return data;
	}

	public static boolean isANumber(String word) {
		try {
			Double.parseDouble(word);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static HashMap<String, Integer> stemmer(
			Map<String, Integer> tokenCount) {
		HashMap<String, Integer> finalCount = new HashMap<String, Integer>();
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

}
