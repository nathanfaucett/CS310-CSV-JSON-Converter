package keywords;

import edu.jsu.mcis.*;
import org.json.simple.parser.*;

public class CSVJSONKeywords {

    public String convertToJson(String csv) {
        return Converter.csvToJson(csv);
    }

    public String convertToCsv(String json) {
        return Converter.jsonToCsv(json);
    }

    public boolean jsonStringsAreEqual(String s, String t) {
        return Converter.jsonStringsAreEqual(s, t);
    }
}
