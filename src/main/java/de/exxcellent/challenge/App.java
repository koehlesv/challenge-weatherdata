package de.exxcellent.challenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public final class App {

	public static final String FILE_PATH_WEATHER_CSV = "src/main/resources/de/exxcellent/challenge/weather.csv";
	public static final String FILE_PATH_FOOTBALL_CSV = "src/main/resources/de/exxcellent/challenge/football.csv";
	private static final int NUMBER_OF_OFFSET_LINES_CSV_FILE = 1;

	/**
	 * This is the main entry method.
	 * 
	 * @param args The CLI arguments passed
	 */
	public static void main(String... args) {
		byte dayWithSmallestTempSpread = csvParsing(FILE_PATH_WEATHER_CSV, ",");
		outputWeatherData(dayWithSmallestTempSpread);

		String teamWithSmallestGoalSpread = "A good team"; // Your goal analysis function call …
		System.out.printf("Team with smallest goal spread       : %s%n", teamWithSmallestGoalSpread);
	}

	/**
	 * Prints the day with the smallest temperature spread. If the file does not
	 * exist or is not correctly formatted, an error message is printed instead.
	 * 
	 * @param dayWithSmallestTempSpread The identifier of the day with the smallest
	 *                                  temperature difference.
	 */
	public static void outputWeatherData(byte dayWithSmallestTempSpread) {
		if (dayWithSmallestTempSpread != -1) {
			if (dayWithSmallestTempSpread != -2) {
				System.out.printf("Day with smallest temperature spread : %s%n", dayWithSmallestTempSpread);
			} else {
				System.err.println("The given file under " + FILE_PATH_WEATHER_CSV + " is not correctly formatted.");
			}
		} else {
			System.err.println("The file under the given file path " + FILE_PATH_WEATHER_CSV + " does not exist.");
		}
	}

	/**
	 * Reads a file linewise.
	 * 
	 * @param filePath The path where the CSV-File is located.
	 * @return ArrayList<String>, where every entry contains one line of the file.
	 *         If the file is not found, the method returns null.
	 */
	public static ArrayList<String> readFile(String filePath) {
		ArrayList<String> fileContent = new ArrayList<String>();
		try (Scanner scan = new Scanner(new File(filePath))) {
			while (scan.hasNext() == true) {
				fileContent.add(scan.nextLine());
			}
		} catch (FileNotFoundException e) {
			return null;
		}
		return fileContent;
	}

	/**
	 * Calculates the number of columns in an ArrayList<String>. All "columns" need
	 * to be seperated using the same separator which is given as a parameter.
	 * 
	 * @param fileContent The content of a file given linewise as ArrayList<String>.
	 * @param separator   The used separator which seperates the columns.
	 * @return The number of columns.
	 */
	public static int arrListGetColumnSize(ArrayList<String> fileContent, String separator) {
		String firstLine = fileContent.get(0);
		String[] content = firstLine.split(separator);
		return content.length;
	}

	/**
	 * Finds and returns the day with the minimal temperature spread.
	 * 
	 * @param fileContent     The content of the given file, linewise.
	 * @param numberOfColumns he number of columns in the given file.
	 * @param separator       The used separator which seperates the columns.
	 * @return The day with the minimal difference between maximum and minimum
	 *         temperature. If the file is not correctly formatted, it returns -2
	 *         instead.
	 */
	public static byte calculateDayWithMinTempDiff(ArrayList<String> fileContent, int numberOfColumns,
			String separator) {
		String[] currentDay = new String[numberOfColumns];
		float differenceMaxMinTemperature = Integer.MAX_VALUE;
		float currentDifference;
		byte dayWithMinimalDifference = -1;
		for (int i = NUMBER_OF_OFFSET_LINES_CSV_FILE; i < fileContent.size(); i++) {
			currentDay = fileContent.get(i).split(separator);
			currentDifference = extractFloatFromStringArr(currentDay);
			if (currentDifference == -2) {
				return -2;
			}
			if (currentDifference < differenceMaxMinTemperature) {
				try {
					dayWithMinimalDifference = Byte.parseByte(currentDay[0]);
				} catch (NumberFormatException e) {
					return -2;
				}
				differenceMaxMinTemperature = currentDifference;
			}
		}
		return dayWithMinimalDifference;
	}

	/**
	 * Extracts the data of the csv-file.
	 * 
	 * @param filePath  The path where the CSV-File is located.
	 * @param separator The used separator which seperates the different columns in
	 *                  the base file.
	 * @return The day with the minimal temperature spread. If the file content is
	 *         null, it returns -1 instead.
	 */
	public static byte csvParsing(String filePath, String separator) {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = readFile(filePath);
		if (fileContent != null) {
			int numberOfColumns = arrListGetColumnSize(fileContent, separator);
			byte dayWithMinimalTempDifference = calculateDayWithMinTempDiff(fileContent, numberOfColumns, separator);
			return dayWithMinimalTempDifference;
		} else {
			return -1;
		}
	}

	/**
	 * For a given day this method calculates the difference between maximum and
	 * minimum temperature.
	 * 
	 * @param currentDay One line of the table.
	 * @return The temperature-difference on the current day. If the file is not
	 *         correctly formatted, it returns -2 instead.
	 */
	public static Float extractFloatFromStringArr(String[] currentDay) {
		try {
			float currentDayMxT = Float.parseFloat(currentDay[1]);
			float currentDayMnT = Float.parseFloat(currentDay[2]);
			float currentDifference = currentDayMxT - currentDayMnT;
			return currentDifference;
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
			return (float) -2;
		}
	}
}
