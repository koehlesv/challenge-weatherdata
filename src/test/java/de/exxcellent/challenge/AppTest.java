package de.exxcellent.challenge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.exxcellent.challenge.App.CSVFileType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

/**
 * JUnit-Tests
 * 
 * @author Sven
 *
 */
class AppTest {
	private static final String PATH_TO_NONEXISTING_FILE = "src/main/resources/de/exxcellent/challenge/nonExistingFile.csv";

	@Test
	@DisplayName("Filereading of weather-data-file")
	void checkReadWeatherFile() {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = App.readFile(App.FILE_PATH_WEATHER_CSV);
		assertNotNull(fileContent);
		assertEquals("Day,MxT,MnT,AvT,AvDP,1HrP TPcpn,PDir,AvSp,Dir,MxS,SkyC,MxR,Mn,R AvSLP", fileContent.get(0));
	}

	@Test
	@DisplayName("Filereading of football-data-file")
	void checkReadFootballFile() {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = App.readFile(App.FILE_PATH_FOOTBALL_CSV);
		assertNotNull(fileContent);
		assertEquals("Team,Games,Wins,Losses,Draws,Goals,Goals Allowed,Points", fileContent.get(0));
	}

	@Test
	@DisplayName("Filereading of non-existing file")
	void checkReadFileWrongName() {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = App.readFile(PATH_TO_NONEXISTING_FILE);
		assertNull(fileContent);
	}

	@Test
	@DisplayName("Lentgh of columns using comma")
	void checkArrListGetColumnSizeUsingComma() {
		ArrayList<String> textUsingComma = new ArrayList<String>();
		textUsingComma.add("Alpha,Beta,Gamma,Delta,Epsilon");
		int columnSize = App.arrListGetColumnSize(textUsingComma, ",");
		assertEquals(5, columnSize);
	}

	@Test
	@DisplayName("Lentgh of columns using semicolon")
	void checkArrListGetColumnSizeUsingSemicolon() {
		ArrayList<String> textUsingSemicolon = new ArrayList<String>();
		textUsingSemicolon.add("Alpha;Beta;Gamma;Delta");
		textUsingSemicolon.add("Epsilon;Zeta;Eta;Theta");
		int columnSize = App.arrListGetColumnSize(textUsingSemicolon, ";");
		assertEquals(4, columnSize);
	}

	@Test
	@DisplayName("CSV Parsing: Weather")
	void checkCSVParsingWeatherFile() {
		String dayResult = App.csvParsing(App.FILE_PATH_WEATHER_CSV, ",", CSVFileType.WEATHER);
		assertEquals("14", dayResult);
		dayResult = App.csvParsing(PATH_TO_NONEXISTING_FILE, ",", CSVFileType.WEATHER);
		assertEquals("-1", dayResult);
	}

	@Test
	@DisplayName("CSV Parsing: Football")
	void checkCSVParsingFootballFile() {
		String teamResult = App.csvParsing(App.FILE_PATH_FOOTBALL_CSV, ",", CSVFileType.FOOTBALL);
		assertEquals("Aston_Villa", teamResult);
		teamResult = App.csvParsing(PATH_TO_NONEXISTING_FILE, ",", CSVFileType.FOOTBALL);
		assertEquals("-1", teamResult);
	}

	@Test
	@DisplayName("Float Parsing: Correct Data")
	void floatParsingCorrectData() {
		String[] workingText = { "2", "1", "0" };
		float returnValue = App.extractTwoFloatsFromStringArrReturnDifference(workingText, 1, 2);
		assertEquals(1, returnValue);
	}

	@Test
	@DisplayName("Float Parsing: Too Few Array Fields")
	void floatParsingSmallArray() {
		String[] textWithTooFewArrayFields = { "2" };
		float returnValue = App.extractTwoFloatsFromStringArrReturnDifference(textWithTooFewArrayFields, 1, 2);
		assertEquals(-2, returnValue);
	}

	@Test
	@DisplayName("Float Parsing: Actual Strings instead of Floats")
	void floatParsingWrongStrings() {
		String[] textWithoutFloats = { "Alpha", "Beta", "Gamma" };
		float returnValue = App.extractTwoFloatsFromStringArrReturnDifference(textWithoutFloats, 1, 2);
		assertEquals(-2, returnValue);
	}

	@Test
	@DisplayName("Football Team Determination")
	void footballTeam() {
		ArrayList<String> fileContent = App.readFile(App.FILE_PATH_FOOTBALL_CSV);
		int numberOfColumns = App.arrListGetColumnSize(fileContent, ",");
		String result = App.calculateTeamsWithMinGoalDiff(fileContent, numberOfColumns, ",");
		assertEquals("Aston_Villa", result);
	}

	@Test
	@DisplayName("Result-Message: File does not exist")
	void errormessageFileDoesNotExist() {
		String returnMessage = App.errormessageFileDoesntExist(CSVFileType.FOOTBALL);
		assertEquals("The file under the given file path " + App.FILE_PATH_FOOTBALL_CSV + " does not exist.",
				returnMessage);

		returnMessage = App.errormessageFileDoesntExist(CSVFileType.WEATHER);
		assertEquals("The file under the given file path " + App.FILE_PATH_WEATHER_CSV + " does not exist.",
				returnMessage);
	}

	@Test
	@DisplayName("Result-Message: File is wrongly formatted")
	void errormessageIsWronglyFormatted() {
		String returnMessage = App.errormessageFileHasIncorrectFormat(CSVFileType.FOOTBALL);
		assertEquals("The given file under " + App.FILE_PATH_FOOTBALL_CSV + " is not correctly formatted.",
				returnMessage);

		returnMessage = App.errormessageFileHasIncorrectFormat(CSVFileType.WEATHER);
		assertEquals("The given file under " + App.FILE_PATH_WEATHER_CSV + " is not correctly formatted.",
				returnMessage);
	}

	@Test
	@DisplayName("Result-Message: Correct output given")
	void correctOutput() {
		String returnMessage = App.successfullOutput("4", CSVFileType.FOOTBALL);
		assertEquals("Team with smallest goal spread       : 4", returnMessage);

		returnMessage = App.successfullOutput("17", CSVFileType.WEATHER);
		assertEquals("Day with smallest temperature spread : 17", returnMessage);
	}

}