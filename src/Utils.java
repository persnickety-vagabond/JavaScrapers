import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Utils {

	public static String readTextFile(String fileName) {

		String returnValue = "";
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader(fileName);
			reader = new BufferedReader(file);
			String line = "";
			while ((line = reader.readLine()) != null) {
				returnValue += line + "\n";
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (file != null) {
				try {
					file.close();
					reader.close();
				} catch (IOException e) {
					// Ignore issues during closing
				}
			}
		}
		return returnValue;
	}

	public static void writeTextFile(String fileName, String s) {
		try {
			FileUtils.writeStringToFile(new File(fileName), s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String toTitleCase(String input) {
		StringBuilder titleCase = new StringBuilder();
		boolean nextTitleCase = true;
		input = input.toLowerCase();

		for (char c : input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			}

			titleCase.append(c);
		}

		return titleCase.toString();
	}
}
