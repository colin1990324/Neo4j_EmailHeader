import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
