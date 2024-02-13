package com.jsonShift;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.XML;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * The serializeObj class provides methods to convert a list of maps to different string formats such as JSON, CSV, and XML.
 */
public class serializeObj {

    /**
     * Default constructor for the serializeObj class.
     */
    public serializeObj() {
    }

    /**
     * Converts the given list of maps to a JSON string.
     *
     * @param array The list of maps to be converted.
     * @return The JSON string representation of the list of maps.
     */
    public String toJson(List<Map<String, Object>> array) {
        JSONArray jarr = new JSONArray(array);
        return jarr.toString();
    }

    /**
     * Converts the given list of maps to a CSV string.
     *
     * @param array The list of maps to be converted.
     * @return The CSV string representation of the list of maps.
     * @throws IOException If an I/O error occurs.
     */
    public String toCsv(List<Map<String, Object>> array) throws IOException {
        Writer writer = new StringWriter();
        csvWriter(array, writer);
        return writer.toString();
    }

    /**
     * Writes the given list of maps to a CSV using the provided writer.
     *
     * @param listOfMap The list of maps to be written.
     * @param writer    The writer to be used for writing the CSV.
     * @throws IOException If an I/O error occurs.
     */
    public static void csvWriter(List<Map<String, Object>> listOfMap, Writer writer) throws IOException {
        CsvSchema schema = null;
        CsvSchema.Builder schemaBuilder = CsvSchema.builder();
        if (listOfMap != null && !listOfMap.isEmpty()) {
            for (String col : listOfMap.get(0).keySet()) {
                schemaBuilder.addColumn(col);
            }
            schema = schemaBuilder.build().withLineSeparator("\r").withHeader();
        }
        CsvMapper mapper = new CsvMapper();
        mapper.writer(schema).writeValues(writer).writeAll(listOfMap);
        writer.flush();
    }

    /**
     * Converts the given list of maps to an XML string.
     *
     * @param array The list of maps to be converted.
     * @return The XML string representation of the list of maps.
     * @throws IOException If an I/O error occurs.
     */
    public String toXml(List<Map<String, Object>> array) throws IOException {
        serializeObj t = new serializeObj();
        String json = t.toJson(array);
        System.out.println("json is " + json);
        return json2xml(json);
    }

    /**
     * Converts the given JSON string to an XML string.
     *
     * @param json The JSON string to be converted.
     * @return The XML string representation of the JSON string.
     * @throws IOException If an I/O error occurs.
     */
    public static String json2xml(String json) throws IOException {
        JSONArray jsonObject = new JSONArray(json);
        String xml = XML.toString(jsonObject, "root");
        return xml;
    }
}
