import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Manipulator {

	public static void main(String[] args) throws IOException {

		FileReader csvData = new FileReader("data/email headers.csv");
		CSVParser parser = new CSVParser(csvData, CSVFormat.EXCEL);
		List<CSVRecord> csvRecords = parser.getRecords();

		Addresses addresses = new Addresses();
		Emails emails = new Emails();

		/**
		 * read records generate email addresses, email content, from-to-email
		 * relationships and stored them in three separated files.
		 */
		for (int i = 1; i < csvRecords.size(); i++) {
			CSVRecord record = (CSVRecord) csvRecords.get(i);
			Email e = new Email();

			addresses.add(record.get(0));
			e.fromId = addresses.getId(record.get(0));

			String[] ss = record.get(1).split(",");
			for (int j = 0; j < ss.length; j++) {
				addresses.add(ss[j]);
				e.toIds.add(addresses.getId(ss[j]));
			}

			e.time = record.get(2);
			e.content = record.get(3);

			emails.add(e);

			/*
			 * test System.out.println("original record: " + record.toString());
			 * System.out.println("from email id: " + e.fromId);
			 * System.out.print("to email ids: ["); for (int j = 0; j <
			 * e.toIds.size(); j++) { System.out.print(e.toIds.get(j) + " "); }
			 * System.out.println("]"); System.out.println("send time: " +
			 * e.time); System.out.println("email content: " + e.content);
			 * break;
			 */

		}

		addresses.generateCSV();

		emails.generateCSV();

	}
}
