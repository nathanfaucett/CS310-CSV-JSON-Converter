package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    /*
        Consider a CSV file like the following:

        ID,Total,Assignment 1,Assignment 2,Exam 1
        111278,611,146,128,337
        111352,867,227,228,412
        111373,461,96,90,275
        111305,835,220,217,398
        111399,898,226,229,443
        111160,454,77,125,252
        111276,579,130,111,338
        111241,973,236,237,500

        The corresponding JSON file would be as follows (note the curly braces):

        {
            "colHeaders":["Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    */

    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        try {
            JSONObject json = new JSONObject();

            JSONArray colHeaders = JSONArray();
            colHeaders.add("Total","Assignment 1","Assignment 2","Exam 1");
            json.set("colHeaders", colHeaders);

            JSONArray rowHeaders = JSONArray();
            json.set("rowHeaders", rowHeaders);

            JSONArray data = JSONArray();
            json.set("data", data);

            CSVParser parser = CSVParser.parse(csvString);
            for (CSVRecord record : parser) {
                rowHeaders.add(record.get(0));
                data.add(record.get(1));
                data.add(record.get(2));
                data.add(record.get(3));
                data.add(record.get(4));
            }

            return json.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String jsonToCsv(String jsonString) {
        JSONObject json = null;

        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String csv = "\"ID\"," + joinStringArray((JSONArray) json.get("colHeaders")) + "\n";

        JSONArray headers = (JSONArray) json.get("rowHeaders");
        JSONArray data = (JSONArray) json.get("data");

        for (int i = 0, il = headers.size(); i < il; i++) {
            csv += (
                "\""+ (String) headers.get(i) + "\"," +
                joinLongArray((JSONArray) data.get(i)) + "\n"
            );
        }

        return csv;
    }

    public static boolean jsonStringsAreEqual(String a, String b) {
        try {
            JSONObject jsonA = (JSONObject) new JSONParser().parse(a);
            JSONObject jsonB = (JSONObject) new JSONParser().parse(b);
            return jsonA.toString().equals(jsonB);
        } catch (Exception ex) {
            return false;
        }
    }

    private static <T> String joinLongArray(JSONArray array) {
        String out = "";
        for (int i = 0, il = array.size(); i < il; i++) {
            out += "\"" + ((Long) array.get(i)) + "\"";
            if (i < il - 1) {
                out += ",";
            }
        }
        return out;
    }
    private static <T> String joinStringArray(JSONArray array) {
        String out = "";
        for (int i = 0, il = array.size(); i < il; i++) {
            out += "\"" + ((String) array.get(i)) + "\"";
            if (i < il - 1) {
                out += ",";
            }
        }
        return out;
    }
}
