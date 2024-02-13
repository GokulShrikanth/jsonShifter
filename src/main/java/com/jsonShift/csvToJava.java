package com.jsonShift;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import io.vertx.ext.web.RoutingContext;

public class csvToJava {
    private InputStream stream;

	public csvToJava() {

	}

	public csvToJava(InputStream stream) throws FileNotFoundException {
		this.stream = stream;
	}

	public csvToJava(File file) throws FileNotFoundException {
		stream = new FileInputStream(file);
	}

	@SuppressWarnings("deprecation")
	public List<Map<String, Object>> read(InputStream stream) throws JsonProcessingException, IOException {
		List<Map<String, Object>> response = new LinkedList<Map<String, Object>>();
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		MappingIterator<Map<String, String>> iterator = mapper.reader(Map.class).with(schema).readValues(stream);
		while (iterator.hasNext()) {
			response.add(Collections.<String, Object>unmodifiableMap(iterator.next()));
		}
		return response;
	}

	@SuppressWarnings("deprecation")
	public List<Map<String, Object>> read() throws JsonProcessingException, IOException {
		if (this.stream == null) {
			throw new RuntimeException("Exception occured. No data to convert.");
		}
		List<Map<String, Object>> response = new LinkedList<Map<String, Object>>();
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = CsvSchema.emptySchema().withHeader();
		MappingIterator<Map<String, String>> iterator = mapper.reader(Map.class).with(schema).readValues(stream);
		while (iterator.hasNext()) {
			response.add(Collections.<String, Object>unmodifiableMap(iterator.next()));
		}

		return response;
	}

   public static void shift(RoutingContext routingContext) throws IOException {
        String body = routingContext.getBodyAsString();
        try {
            csvToJava csv = new csvToJava();
            List<Map<String, Object>> response = csv.read();
            routingContext.response().putHeader("content-type", "text").end(response.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
