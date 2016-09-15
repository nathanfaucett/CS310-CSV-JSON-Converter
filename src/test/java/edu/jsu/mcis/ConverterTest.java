package edu.jsu.mcis;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConverterTest {
    private String csvString;
    private String jsonString;


    private static String readFile(String path) throws IOException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        String newline = System.getProperty("line.separator");
        String out = "";

        try {
            while(scanner.hasNextLine()) {
                out += scanner.nextLine() + newline;
            }
            return out;
        } finally {
            scanner.close();
        }
    }

    @Before
    public void setUp() {
        try {
            jsonString = readFile("src/test/resources/grades.json");
            csvString = readFile("src/test/resources/grades.csv");
        } catch(IOException e) {}
    }

    @Test
    public void testConvertCSVtoJSON() {
        assertTrue(Converter.jsonStringsAreEqual(Converter.csvToJson(csvString), jsonString));
    }

    @Test
    public void testConvertJSONtoCSV() {
        assertEquals(Converter.jsonToCsv(jsonString), csvString);
    }
}
