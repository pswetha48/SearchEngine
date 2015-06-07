import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import edu.stanford.nlp.util.logging.Redwood.Util;

public class Main {
	private static TreeMap<Integer, ArrayList<String>> indexedQuery = new TreeMap<Integer, ArrayList<String>>();
	public static List<Result> results = new ArrayList<Result>();

	public static void main(String[] args) {
		Utilities.readFileAndPopulateInvertedList(); // annotated
		Utilities.readFileAndPopulateInvertedListOriginal(); // index
		Utilities.readAndPopulateMaxTfDocLen(); //
		Utilities.readJudgements();
		Utilities.readFromUrl();
		Utilities.readQueries();
		GUI.main_gui();
		// readQuery();

		/*
		 * Utilities.query.put(1,"first outbreak of ebola");
		 * Utilities.query.put(2,"where did first ebola case occur in africa");
		 * Utilities.query.put(3,"where did first ebola case occur");
		 * Utilities.query.put(4,"in which places did ebola occur");
		 * Utilities.query.put(5,"spread of ebola in 2014");
		 * Utilities.query.put(
		 * 6,"what was the first hospital to diagnose ebola");
		 * Utilities.query.put(7,"where was first ebola patient seen");
		 * Utilities.query.put(8,"where did first ebola case occur in us");
		 * Utilities.query.put(9,"2014 ebola cases");
		 */

	}

	private static void printToFile(String text) {

		PrintWriter out = null;
		try {
			int key = Utilities.queries.get(text);
			for (double docNumberKey : Utilities.queryNumWeightOneTopFive.get(
					key).keySet()) {
				String filePath = "texts/"
						+ docNumberKey;
				StringBuilder sb = new StringBuilder();
				try (BufferedReader br = new BufferedReader(new FileReader(
						filePath))) {
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) {
						sb.append(sCurrentLine);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				boolean judge = Utilities.judgements.get(key).get(docNumberKey) == null ? false
						: Utilities.judgements.get(Utilities.queries.get(text))
								.get(docNumberKey);
				Result result = new Result(docNumberKey,
						Utilities.urls.get(Integer.parseInt(String.valueOf(
								docNumberKey).split("\\.")[0].trim())), judge,
						sb.toString());
				results.add(result);
			}
			for (double innerKey : Utilities.queryNumWeightTwoTopFive.get(key)
					.keySet()) {

				String filePath = "texts/"
						+ innerKey;
				StringBuilder sb = new StringBuilder();
				try (BufferedReader br = new BufferedReader(new FileReader(
						filePath))) {
					String sCurrentLine;
					while ((sCurrentLine = br.readLine()) != null) {
						sb.append(sCurrentLine);
					}
					Result result = new Result(innerKey,
							Utilities.urls.get(Integer.parseInt(String.valueOf(
									innerKey).split("\\.")[0].trim())), true,
							sb.toString());
					results.add(result);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void parseQuery(int queryNum, String query) {
		/*
		 * what similarity laws must be obeyed when constructing aeroelastic
		 * models of heated high speed aircraft similarity law must obey
		 * construct aeroelastic model heated high speed aircraft
		 */
		ArrayList<String> words = new ArrayList<String>();
		String a[] = query.split(" ");
		for (String word : a) {
			word = word.replaceAll("-", " ");
			word = word.toLowerCase().trim();
			if (word != null && !word.isEmpty() && !word.equals("")
					&& !Utilities.isAStopWord(word)
					&& !Utilities.isANumber(word)) {
				word = lemmatize(word);
				words.add(word);
			}
		}
		indexedQuery.put(queryNum, words);
		Utilities.evaluateQuery(queryNum, words);
		Utilities.evaluateQueryOriginal(queryNum, words);
	}

	private static String lemmatize(String word) {
		word = Lemmatizer.getInstance().getLemma(word);
		word = word.trim();
		if (!word.isEmpty())
			return word;
		return "";
	}

	public static void buttonClicked(String text) {
		results.clear();
		List<String> queries = new ArrayList<String>(Utilities.queries.keySet());
		String processed = matchQuery(queries, text);
		if (processed.isEmpty())
			GUI.txtTypeYourQuery.setText("Please enter a valid query!");
		else {
			System.out.println(processed);
			parseQuery(Utilities.queries.get(processed), processed);
			jugaad(processed);
			Utilities.query.put(1, processed);
			printToFile(processed);
		}

	}

	private static String matchQuery(List<String> queries2, String query) {
		// TODO Auto-generated method stub
		String[] queryTokens = query.toLowerCase().split(" ");
		for (int i = 0; i < queryTokens.length; i++)
			queryTokens[i] = Lemmatizer.getInstance().getLemma(queryTokens[i]);
		int max = 0;
		int index = -1;
		int count = 0;

		for (int i = 0; i < queries2.size(); i++) {
			String[] queryListTokens = queries2.get(i).toLowerCase().split(" ");
			for (int j = 0; i < queryListTokens.length; i++)
				queryListTokens[j] = Lemmatizer.getInstance()
						.getLemma(queryListTokens[j]).trim();
			for (String inputToken : queryTokens)
				for (String queryListToken : queryListTokens) {
					if (inputToken.trim().equals(queryListToken)) {
						count++;
					}
				}

			if (count > max) {
				max = count;
				index = i;
			}
			count = 0;

		}

		return index > -1 ? queries2.get(index) : "";

	}

	private static void jugaad(String text) {
		HashMap<Double, Boolean> whatever = Utilities.judgements
				.get(Utilities.queries.get(text));
		for (double innerKey : whatever.keySet()) {
			String filePath = "texts/"
					+ innerKey;
			StringBuilder sb = new StringBuilder();
			try (BufferedReader br = new BufferedReader(
					new FileReader(filePath))) {
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {
					sb.append(sCurrentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Result result = new Result(innerKey,
					Utilities.urls.get(Integer.parseInt(String
							.valueOf(innerKey).split("\\.")[0].trim())),
					whatever.get(innerKey), sb.toString());
			results.add(result);
		}

	}

}
