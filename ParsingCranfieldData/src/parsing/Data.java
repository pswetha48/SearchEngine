package parsing;

import java.util.HashMap;

public class Data {
	private static String docno;
	private static String title;
	private static String author;
	private static String biblio;
	private static String text;
	private static HashMap<String, Integer> listOfWords = new HashMap<String, Integer>();
	
	public String getDocno() {
		return docno;
	}
	public void setDocno(String docno) {
		Data.docno = docno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		Data.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		Data.author = author;
	}
	public String getBiblio() {
		return biblio;
	}
	public void setBiblio(String biblio) {
		Data.biblio = biblio;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		Data.text = text;
	}
	public void print() {
		System.out.println("Docnumber: "+this.getDocno());
		System.out.println("Title: "+this.getTitle());
		System.out.println("Author: "+this.getAuthor());
		System.out.println("Biblio: "+this.getBiblio());
		System.out.println("Text: "+this.getText()); 
	}
	public void createWordList(){
		
	}
	
	
}
