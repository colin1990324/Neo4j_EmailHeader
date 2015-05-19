import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Addresses {

	private Map<String, Integer> map;
	private List<String> list;

	public Addresses() {
		map = new HashMap<String, Integer>();
		list = new ArrayList<String>();
	}

	public void add(String address) {
		if (!map.containsKey(address)) {
			list.add(address);
			map.put(address, list.size() - 1);
		}
	}

	public int getId(String address) {
		return map.get(address);
	}

	public String getAddress(int id) {
		return list.get(id);
	}

	// Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";
	// CSV file header
	private static final Object[] FILE_HEADER = { "id", "address" };

	public void generateCSV() throws IOException {

		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		// Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormat = CSVFormat.DEFAULT
				.withRecordSeparator(NEW_LINE_SEPARATOR);
		// initialize FileWriter object
		fileWriter = new FileWriter("data/addresses.csv");
		// initialize CSVPrinter object
		csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		// Create CSV file headercsvFilePrinter.printRecord(FILE_HEADER);
		csvFilePrinter.printRecord(FILE_HEADER);

		for (int i = 0; i < list.size(); i++) {
			List<String> addressRecord = new ArrayList<String>();
			addressRecord.add(String.valueOf(i));
			addressRecord.add(list.get(i));
			csvFilePrinter.printRecord(addressRecord);
		}

		try {
			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
			System.out.println("data/addresses.csv generated successfully.");
		} catch (IOException e) {
			System.out
					.println("Error while flushing/closing fileWriter/csvPrinter !!!");
			e.printStackTrace();
		}

	}
}
