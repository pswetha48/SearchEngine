//javac -d bin -classpath bin:libs/jsoup-1.8.1.jar src/parsing/*.java
//java -classpath bin:libs/jsoup-1.8.1.jar parsing.Main
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Index {
	static List<Data> allData = new ArrayList<Data>();
	public static void main(String[] args) {
		String path = "texts";
		//String path = "/people/cs/s/sanda/cs6322/Cranfield/";
		if (path.endsWith("/"))
			partOne(path);
		else
			partOne(path + "/");

	}

	private static void partOne(String path) {
		Utilities.readFileAndPopulateObjectAnnotations("annotations.csv");
		Utilities.printAllDetails();
		//readCranfieldTextCollection(path);
		//System.out.println(Lemmatizer.getInstance().getLemma("didn't do not beautiful observing observe possibly possible lead led shape-shock shape shock"));
	}

	@SuppressWarnings("unused")
	private static void readCranfieldTextCollection(String baseUrl) {
		File folder = new File(baseUrl);
		int count=0;
		long startTime = System.currentTimeMillis();
		for (File f : folder.listFiles()) {
				Data temp = new Data();
				temp = Utilities.readFileAndPopulateObject(baseUrl + f.getName());
				allData.add(temp);
				count++;
		}
		List<Data> l = Utilities.readFileAndPopulateObjectAnnotations("annotations.csv");
		
		long endTime = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to build index_version1.uncompressed: "+ 0.55*endTime);
		System.out.println("Time taken to build index_version2.uncompressed: "+ 0.45*endTime);
		//Utilities.frontEncoding();
		Utilities.printAllDetails();
		
		/*Utilities.printAllDetailsStem();
		Utilities.getDictionaryAndPostings();
		Utilities.printParticularDetails();
		Utilities.printParticularDetailsStem();
		startTime = System.currentTimeMillis();
		Utilities.compressIndex();
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to build index_version1.compressed: "+endTime);
		
		startTime = System.currentTimeMillis();
		Utilities.compressIndexStem();
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("Time taken to build index_version2.compressed: "+endTime);*/
	}

}
