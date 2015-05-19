import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Emails {

	List<Email> list;

	public Emails() {
		list = new ArrayList<Email>();
	}

	public void add(Email e) {
		list.add(e);
	}

	// Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";
	// CSV file header
	private static final Object[] FILE_HEADER_EMAIL = { "id", "time", "content" };
	private static final Object[] FILE_HEADER_RELATION = { "emailId", "fromId",
			"toId" };

	public void generateCSV() throws IOException {

		CSVFormat csvFileFormat = CSVFormat.DEFAULT
				.withRecordSeparator(NEW_LINE_SEPARATOR);

		FileWriter fileWriter = new FileWriter("data/emails.csv");
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		csvFilePrinter.printRecord(FILE_HEADER_EMAIL);

		FileWriter fileWriterRelation = new FileWriter("data/relations.csv");
		CSVPrinter csvFilePrinterRelation = new CSVPrinter(fileWriterRelation,
				csvFileFormat);
		csvFilePrinterRelation.printRecord(FILE_HEADER_RELATION);

		for (int i = 0; i < list.size(); i++) {
			List<String> emailRecord = new ArrayList<String>();
			emailRecord.add(String.valueOf(i));
			emailRecord.add(list.get(i).time);
			emailRecord.add(list.get(i).content);
			csvFilePrinter.printRecord(emailRecord);

			for (int j : list.get(i).toIds) {
				List<String> relationRecord = new ArrayList<String>();
				relationRecord.add(String.valueOf(i));
				relationRecord.add(String.valueOf(list.get(i).fromId));
				relationRecord.add(String.valueOf(j));
				csvFilePrinterRelation.printRecord(relationRecord);
			}
		}

		try {
			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
			System.out.println("data/emails.csv generated successfully.");

			fileWriterRelation.flush();
			fileWriterRelation.close();
			csvFilePrinterRelation.close();
			System.out.println("data/relations.csv generated successfully.");
		} catch (IOException e) {
			System.out
					.println("Error while flushing/closing fileWriter/csvPrinter !!!");
			e.printStackTrace();
		}

	}

	HashMap<String, Integer> count = new HashMap<String, Integer>();

	/**
	 * count words with top frequency. 
	 * remove stop words to find meaningful content.
	 * @param top
	 */
	public void countTopFrequentWords(int top) {
		Map stopWords = StopWords.getStopWords();
		for (Email e : list) {
			String[] ss = e.content.split(" ");
			for (String s : ss) {
				if (stopWords.containsKey(s))
					continue;
				if (count.containsKey(s))
					count.put(s, count.get(s) + 1);
				else
					count.put(s, 1);
			}
		}

		Map<String, Integer> sortedMapDesc = sortByComparator(count, DESC);
		printMap(sortedMapDesc);

	}

	public static boolean ASC = true;
	public static boolean DESC = false;

	/**
	 * can sort Map<String, Integer> by value
	 * 
	 * @param unsortMap
	 * @param order
	 * @return
	 */
	public static Map<String, Integer> sortByComparator(
			Map<String, Integer> unsortMap, final boolean order) {

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/**
	 * print the String-Integer pair
	 * 
	 * @param map
	 * @param top
	 */
	public static void printMap(Map<String, Integer> map, int top) {
		int i = 0;
		for (Entry<String, Integer> entry : map.entrySet()) {
			if (i >= top)
				break;
			i++;
			System.out.println("Key : " + entry.getKey() + ",     Value : "
					+ entry.getValue());
		}
	}

	public static void printMap(Map<String, Integer> map) {
		printMap(map, 20);
	}
}
