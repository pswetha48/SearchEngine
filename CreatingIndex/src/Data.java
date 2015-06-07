import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
	private String docno;
	private String title;
	private int relevanceFactorNum;
	private String author;
	private String biblio;
	private String text;
	public HashMap<String, Integer> termFrequency = new HashMap<String, Integer>();
	public HashMap<String, Integer> termStemFrequency = new HashMap<String, Integer>();

	private List<String> stemsPerDoc = new ArrayList<String>();
	private int max_tf = -1;
	private int doclen;
	private int max_tf_stem = -1;
	private String mostFrequentWord;
	private String mostFrequentWordStem;

	public void tokenize() {
		String a[] = text.split("\\|");
		for (String w : a) {
			for(String word: w.trim().split(" ") ){
				doclen += 1;
				word = word.replaceAll("-", " ");
				word = word.toLowerCase().trim();
				if (word!=null && !word.isEmpty() && !word.equals("") && !Utilities.isAStopWord(word) && !Utilities.isANumber(word)) {
					lemmatize(word);
					//stemize(word);
					}
			}
			
		}
		for (String key : termFrequency.keySet()) {
			
			int i=termFrequency.get(key);
			if(key!=null && !key.isEmpty()){
				if (i > max_tf){
					max_tf = i;
					mostFrequentWord=key;
				}
			}
		}
		Utilities.docMaxLen.append(docno + ":" +mostFrequentWord+":"+ max_tf + ":" + doclen + "\n");

		for (String key : termStemFrequency.keySet()) {
			int i = termStemFrequency.get(key);
			if(key!=null && !key.isEmpty()){
				if (i > max_tf_stem){
					max_tf_stem = i;
					mostFrequentWordStem = key;
				}
			}
			
		}
		if(mostFrequentWord==null)
			System.out.println(docno+" "+mostFrequentWord);
		Utilities.docMaxLenStem.append(docno + "-" + mostFrequentWordStem+":"+max_tf_stem + ":" + doclen
				+ "\n");
	}

	private void stemize(String word) {
		Stemmer stemObj = new Stemmer();
		stemObj.add(word.toCharArray(), word.length());
		stemObj.stem();
		{
			word = stemObj.toString();
		}
		word = word.trim();
		if (termStemFrequency.containsKey(word)) {
			int count = termStemFrequency.get(word);
			termStemFrequency.put(word, count + 1);
		} else {
			word = word.trim();
			if (word!=null && !word.isEmpty()) {
				word = word.replace(".", "");
				word = word.replace(",", "");
				termStemFrequency.put(word, 1);
			}
		}
		word = validateWord(word);
		if(!word.isEmpty())
			Utilities.indexInformationStem(docno, word.trim());
	}

	private String validateWord(String word) {
		word = word.replaceAll("[^A-Za-z]", "");
		word = word.trim();
		if(word.startsWith("`") && word.startsWith("-lrb-") && word.startsWith("/ ") && word.isEmpty() && word.length()<2){
			word="";
		}
		return word;
	}

	private void lemmatize(String word) {
		word = Lemmatizer.getInstance().getLemma(word);
			word = word.trim();
		if (termFrequency.containsKey(word)) {
			int count = termFrequency.get(word);
			termFrequency.put(word, count + 1);
		} else {
			word = word.trim();
			if (word!=null && !word.isEmpty()) {
				word = word.replace(".", "");
				word = word.replace(",", "");
				termFrequency.put(word, 1);
			}
		}
		word = validateWord(word);
		if(!word.isEmpty())
		Utilities.indexInformation(docno, word.trim().replace("lrb", " ").replace("rrb", " "), relevanceFactorNum);
	}

	public int getTermFrequency(String word) {
		if (termFrequency.containsKey(word)) {
			return termFrequency.get(word);
		}
		return 0;
	}

	public String getDocno() {
		return docno;
	}

	public void setDocno(String docno) {
		this.docno = docno;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBiblio() {
		return biblio;
	}

	public void setBiblio(String biblio) {
		this.biblio = biblio;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void print() {
		System.out.println("Docnumber: " + this.getDocno());
		System.out.println("Title: " + this.getTitle());
		System.out.println("Author: " + this.getAuthor());
		System.out.println("Biblio: " + this.getBiblio());
		System.out.println("Text: " + this.getText());
	}

	public int getRelevanceFactorNum() {
		return relevanceFactorNum;
	}

	public void setRelevanceFactorNum(int relevanceFactorNum) {
		this.relevanceFactorNum = relevanceFactorNum;
	}

}
