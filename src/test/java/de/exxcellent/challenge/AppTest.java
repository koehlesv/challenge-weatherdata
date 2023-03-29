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
		byte dayResult = App.csvParsing(App.FILE_PATH_WEATHER_CSV, ",", CSVFileType.WEATHER);
		assertEquals(14, dayResult);
		dayResult = App.csvParsing(PATH_TO_NONEXISTING_FILE, ",", CSVFileType.WEATHER);
		assertEquals(-1, dayResult);
	}

	@Test
	@DisplayName("CSV Parsing: Football")
	void checkCSVParsingFootballFile() {
		byte dayResult = App.csvParsing(App.FILE_PATH_WEATHER_CSV, ",", CSVFileType.FOOTBALL);
		assertEquals(14, dayResult); // TODO: Change to actual expected value.
		dayResult = App.csvParsing(PATH_TO_NONEXISTING_FILE, ",", CSVFileType.FOOTBALL);
		assertEquals(-1, dayResult);
	}

	@Test
	@DisplayName("Float Parsing: Correct Data")
	void floatParsingCorrectData() {
		String[] workingText = { "2", "1", "0" };
		float returnValue = App.extractFloatFromStringArr(workingText);
		assertEquals(1, returnValue);
	}

	@Test
	@DisplayName("Float Parsing: Too Few Array Fields")
	void floatParsingSmallArray() {
		String[] textWithTooFewArrayFields = { "2" };
		float returnValue = App.extractFloatFromStringArr(textWithTooFewArrayFields);
		assertEquals(-2, returnValue);
	}

	@Test
	@DisplayName("Float Parsing: Actual Strings instead of Floats")
	void floatParsingWrongStrings() {
		String[] textWithoutFloats = { "Alpha", "Beta", "Gamma" };
		float returnValue = App.extractFloatFromStringArr(textWithoutFloats);
		assertEquals(-2, returnValue);
	}

}