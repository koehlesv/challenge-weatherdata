package de.exxcellent.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

/**
 * 
 * @author Sven
 *
 */
class AppTest {
	private static final String PATH_TO_NONEXISTING_FILE = "src/main/resources/de/exxcellent/challenge/nonExistingFile.csv";

//    private String successLabel = "not successful";
//
//    @BeforeEach
//    void setUp() {
//        successLabel = "successful";
//    }
//
//    @Test
//    void aPointlessTest() {
//        assertEquals("successful", successLabel, "My expectations were not met");
//    }
//
//    @Test
//    void runFootball() {
//        App.main("--football", "football.csv");
//    }

	@Test
	@DisplayName("Filereading of existing file")
	void checkReadFile() {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = App.readFile(App.FILE_PATH_WEATHER_CSV);
		assertNotNull(fileContent);
		assertEquals("Day,MxT,MnT,AvT,AvDP,1HrP TPcpn,PDir,AvSp,Dir,MxS,SkyC,MxR,Mn,R AvSLP", fileContent.get(0));
	}

	@Test
	@DisplayName("Filereading of non-existing file")
	void checkReadFileWrongName() {
		ArrayList<String> fileContent = new ArrayList<String>();
		fileContent = App.readFile(PATH_TO_NONEXISTING_FILE);
		assertNull(fileContent);
	}

	@Test
	@DisplayName("Lentgh of columns")
	void checkArrListGetColumnSize() {
		ArrayList<String> textUsingComma = new ArrayList<String>();
		textUsingComma.add("Alpha,Beta,Gamma,Delta,Epsilon");
		int columnSize = App.arrListGetColumnSize(textUsingComma, ",");
		assertEquals(5, columnSize);

		ArrayList<String> textUsingSemicolon = new ArrayList<String>();
		textUsingSemicolon.add("Alpha;Beta;Gamma;Delta");
		textUsingSemicolon.add("Epsilon;Zeta;Eta;Theta");
		columnSize = App.arrListGetColumnSize(textUsingSemicolon, ";");
		assertEquals(4, columnSize);
	}

	@Test
	@DisplayName("Check CSV Parsing")
	void checkCSVParsing() {
		byte dayResult = App.csvParsing(App.FILE_PATH_WEATHER_CSV, ",");
		assertEquals(14, dayResult);

		String wrongPath = "src/main/resources/de/exxcellent/challenge/weather.csv";
		dayResult = App.csvParsing(PATH_TO_NONEXISTING_FILE, ",");
		assertEquals(-1, dayResult);
	}

	@Test
	@DisplayName("Float Parsing")
	void floatParsing() {
		String[] workingText = { "2", "1", "0" };
		float returnValue = App.extractFloatFromStringArr(workingText);
		assertEquals(1, returnValue);

		String[] textWithTooFewArrayFields = { "2" };
		returnValue = App.extractFloatFromStringArr(textWithTooFewArrayFields);
		assertEquals(-2, returnValue);

		String[] textWithoutFloats = { "Alpha", "Beta", "Gamma" };
		returnValue = App.extractFloatFromStringArr(textWithoutFloats);
		assertEquals(-2, returnValue);
	}

}