package de.exxcellent.challenge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public final class App {

	enum CSVFileType {
		WEATHER, FOOTBALL
	}

	public static final String FILE_PATH_WEATHER_CSV = "src/main/resources/de/exxcellent/challenge/weather.csv";
	public static final String FILE_PATH_FOOTBALL_CSV = "src/main/resources/de/exxcellent/challenge/football.csv";
	private static final int NUMBER_OF_OFFSET_LINES_WEATHER_CSV_FILE = 1;
	private static final int NUMBER_OF_OFFSET_LINES_FOOTBALL_CSV_FILE = 1;

	/**
	 * This is the main entry method.
	 * 
	 * @param args The CLI arguments passed
	 */
	public static void main(String... args) {
		String dayWithSmallestTempSpread = csvParsing(FILE_PATH_WEATHER_CSV, ",", CSVFileType.WEATHER);
		outputCSVData(dayWithSmallestTempSpread, CSVFileType.WEATHER);

		String teamWithSmallestGoalSpread = csvParsing(FILE_PATH_FOOTBALL_CSV, ",", CSVFileType.FOOTBALL);
		outputCSVData(teamWithSmallestGoalSpread, CSVFileType.FOOTBALL);
	}

	/**
	 * Prints the day with the smallest temperature spread opr the team with the
	 * smallest goal difference. If the file does not exist or is not correctly
	 * formatted, an error message is printed instead.
	 * 
	 * @param result        The team with the smallest goal difference or the day
	 *                      with the smallest temperature difference.
	 * @param typeOfCSVFile The type of CSV file.
	 */
	public static void outputCSVData(String result, CSVFileType typeOfCSVFile) {
		switch (result) {
		case "-1":
			System.err.println(errormessageFileDoesntExist(typeOfCSVFile));
			break;
		case "-2":
			System.err.println(errormessageFileHasIncorrectFormat(typeOfCSVFile));
			break;
		default:
			System.out.println(successfullOutput(result, typeOfCSVFile));
			break;
		}

	}

	/**
	 * To be called if the calculation was succesfull.
	 * 
	 * @param result        The calculated result (Day or Team).
	 * @param typeOfCSVFile The type of the csv file.
	 * @return Success message.
	 */
	public static String successfullOutput(String result, CSVFileType typeOfCSVFile) {
		switch (typeOfCSVFile) {
		case FOOTBALL:
			return "Team with smallest goal spread       : " + result;
		case WEATHER:
			return "Day with smallest temperature spread : " + result;
		}
		return null;
	}

	/**
	 * Output if the file has the wrong format.
	 * 
	 * @param typeOfCSVFile The type of the csv file.
	 * @return Error message.
	 */
	public static String errormessageFileHasIncorrectFormat(CSVFileType typeOfCSVFile) {
		switch (typeOfCSVFile) {
		case FOOTBALL:
			return "The given file under " + FILE_PATH_FOOTBALL_CSV + " is not correctly formatted.";
		case WEATHER:
			return "The given file under " + FILE_PATH_WEATHER_CSV + " is not correctly formatted.";
		}
		return null;
	}

	/**
	 * Output if the file does not exist.
	 * 
	 * @param typeOfCSVFile The type of the csv file.
	 * @return Error message.
	 */
	public static String errormessageFileDoesntExist(CSVFileType typeOfCSVFile) {
		switch (typeOfCSVFile) {
		case FOOTBALL:
			return "The file under the given file path " + FILE_PATH_FOOTBALL_CSV + " does not exist.";
		case WEATHER:
			return "The file under the given file path " + FILE_PATH_WEATHER_CSV + " does not exist.";
		}
		return null;
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
	 * @param numberOfColumns The number of columns in the given file.
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
		for (int i = NUMBER_OF_OFFSET_LINES_WEATHER_CSV_FILE; i < fileContent.size(); i++) {
			currentDay = fileContent.get(i).split(separator);
			currentDifference = extractTwoFloatsFromStringArrReturnDifference(currentDay, 1, 2);
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
	 * Finds and returns the team with the minimal goal difference.
	 * 
	 * @param fileContent     The content of the given file, linewise.
	 * @param numberOfColumns The number of columns in the given file.
	 * @param separator       The used separator which seperates the columns.
	 * @return The team with the minimal difference between goals shot and goals
	 *         taken. If the file is not correctly formatted, it returns -2 instead.
	 */
	public static String calculateTeamsWithMinGoalDiff(ArrayList<String> fileContent, int numberOfColumns,
			String separator) {
		String[] currentTeam = new String[numberOfColumns];
		float differenceInGoals = Integer.MAX_VALUE;
		float currentDifference;
		String teamWithMinimalDifference = null;
		for (int i = NUMBER_OF_OFFSET_LINES_FOOTBALL_CSV_FILE; i < fileContent.size(); i++) {
			currentTeam = fileContent.get(i).split(separator);
			currentDifference = extractTwoFloatsFromStringArrReturnDifference(currentTeam, 5, 6);
			if (currentDifference == -2) {
				return "-2";
			}
			if (currentDifference < differenceInGoals) {
				try {
					teamWithMinimalDifference = currentTeam[0];
				} catch (NumberFormatException e) {
					return "-2";
				}
				differenceInGoals = currentDifference;
			}
		}
		return teamWithMinimalDifference;
	}

	/**
	 * Extracts the data of the csv-file.
	 * 
	 * @param filePath      The path where the CSV-File is located.
	 * @param separator     The used separator which seperates the different columns
	 *                      in the base file.
	 * @param typeOfCSVFile The type of CSV-File (either weather or football).
	 * @return The day with the minimal temperature spread or the team with minimal
	 *         goal difference, depending on the value of the variable
	 *         typeOfCSVFile. If the file content is null or the given csv type is
	 *         not yet implemented, it returns -1 instead.
	 */
	public static String csvParsing(String filePath, String separator, CSVFileType typeOfCSVFile) {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = readFile(filePath);
		if (fileContent != null) {
			int numberOfColumns = arrListGetColumnSize(fileContent, separator);
			String result = "-1";
			switch (typeOfCSVFile) {
			case FOOTBALL:
				result = calculateTeamsWithMinGoalDiff(fileContent, numberOfColumns, separator);
				break;
			case WEATHER:
				result = calculateDayWithMinTempDiff(fileContent, numberOfColumns, separator) + "";
				break;
			}
			return result;
		} else {
			return "-1";
		}
	}

	/**
	 * For a given day this method calculates the difference between two float
	 * values at given positions in a String array.
	 * 
	 * @param textArrayContainingFloats The text-array where the two floats should
	 *                                  be extracted from.
	 * @param indexOfFirstValue         Index of the first float value.
	 * @param indexOfSecondValue        Index of the second float value.
	 * @return The difference between the two float values. If the file is not
	 *         correctly formatted, it returns -2 instead.
	 */
	public static Float extractTwoFloatsFromStringArrReturnDifference(String[] textArrayContainingFloats,
			int indexOfFirstValue, int indexOfSecondValue) {
		try {
			float firstValue = Float.parseFloat(textArrayContainingFloats[indexOfFirstValue]);
			float secondValue = Float.parseFloat(textArrayContainingFloats[indexOfSecondValue]);
			float currentDifference = firstValue - secondValue;
			return currentDifference;
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
			return (float) -2;
		}
	}
}
