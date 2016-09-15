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
        JSONObject json = new JSONObject();

        JSONArray colHeaders = new JSONArray();
        colHeaders.add("Total");
        colHeaders.add("Assignment 1");
        colHeaders.add("Assignment 2");
        colHeaders.add("Exam 1");
        json.put("colHeaders", colHeaders);

        JSONArray rowHeaders = new JSONArray();
        json.put("rowHeaders", rowHeaders);

        JSONArray data = new JSONArray();
        json.put("data", data);

        CSVParser parser = new CSVParser();
        BufferedReader bufReader = new BufferedReader(new StringReader(csvString));

        try {
            String line = bufReader.readLine(); //skip header

            while ((line = bufReader.readLine()) != null ) {
                String[] record = parser.parseLine(line);
                rowHeaders.add(record[0]);
                JSONArray row = new JSONArray();
                row.add(new Long(record[1]));
                row.add(new Long(record[2]));
                row.add(new Long(record[3]));
                row.add(new Long(record[4]));
                data.add(row);
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        return json.toString();
    }

    public static String jsonToCsv(String jsonString) {
        JSONObject json = null;

        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String csv = "\"ID\"," + Converter.<String>joinArray((JSONArray) json.get("colHeaders")) + "\n";

        JSONArray headers = (JSONArray) json.get("rowHeaders");
        JSONArray data = (JSONArray) json.get("data");

        for (int i = 0, il = headers.size(); i < il; i++) {
            csv += (
                "\""+ (String) headers.get(i) + "\"," +
                Converter.<Long>joinArray((JSONArray) data.get(i)) + "\n"
            );
        }

        return csv;
    }

    @SuppressWarnings("unchecked")
    private static <T> String joinArray(JSONArray array) {
        String out = "";
        for (int i = 0, il = array.size(); i < il; i++) {
            out += "\"" + ((T) array.get(i)) + "\"";
            if (i < il - 1) {
                out += ",";
            }
        }
        return out;
    }

    public static boolean jsonStringsAreEqual(String a, String b) {
        try {
            return jsonEqaul(new JSONParser().parse(a), new JSONParser().parse(b));
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static boolean jsonEqaul(Object a, Object b) {
        if (a instanceof JSONObject && b instanceof JSONObject) {
            return jsonObjectEqaul((JSONObject) a, (JSONObject) b);
        } else if (a instanceof JSONArray && b instanceof JSONArray) {
            return jsonArrayEqaul((JSONArray) a, (JSONArray) b);
        } else {
            return a.equals(b);
        }
    }
    private static boolean jsonObjectEqaul(JSONObject a, JSONObject b) {
        for (Object k : a.keySet()) {
            String key = (String) k;

            if (!b.containsKey(key) || !jsonEqaul(a.get(key), b.get(key))) {
                return false;
            }
        }
        return true;
    }
    private static boolean jsonArrayEqaul(JSONArray a, JSONArray b) {
        int aSize = a.size();

        if (aSize != b.size()) {
            return false;
        } else {
            for (int i = 0, il = aSize; i < il; i++) {
                if (!jsonEqaul(a.get(i), b.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }
}
