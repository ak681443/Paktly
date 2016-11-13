package com.paktly.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class HTTPUtility {

	public static HttpURLConnection httpConn;
	public static String apiKey = "a7bee24dea13a17e6a19fc277698f30b";

	public static HttpURLConnection sendGetRequest(String requestURL) throws IOException {
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);

		httpConn.setDoInput(true); // true if we want to read server's response
		httpConn.setDoOutput(false); // false indicates this is a GET request

		return httpConn;
	}

	public static HttpURLConnection sendPostRequest(String requestURL, Map<String, String> params) throws IOException {
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);

		httpConn.setDoInput(true); // true indicates the server returns response

		StringBuffer requestParams = new StringBuffer();

		if (params != null && params.size() > 0) {

			httpConn.setDoOutput(true); // true indicates POST request

			// creates the params string, encode them using URLEncoder
			Iterator<String> paramIterator = params.keySet().iterator();
			while (paramIterator.hasNext()) {
				String key = paramIterator.next();
				String value = params.get(key);
				requestParams.append(URLEncoder.encode(key, "UTF-8"));
				requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
				requestParams.append("&");
			}

			// sends POST data
			OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
			writer.write(requestParams.toString());
			writer.flush();
		}

		return httpConn;
	}

	public static HttpURLConnection sendPostRequestWithJSON(String requestURL, JSONObject params) throws IOException {
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setUseCaches(false);

		httpConn.setDoInput(true); // true indicates the server returns response

		httpConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		
		httpConn.setDoOutput(true); // true indicates POST request
		// sends POST data
		OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
		writer.write(params.toString());
		writer.flush();
		return httpConn;
	}


	public static String readSingleLineRespone() throws IOException {
		InputStream inputStream = null;
		if (httpConn != null) {
			inputStream = httpConn.getInputStream();
		} else {
			throw new IOException("Connection is not established.");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String response = reader.readLine();
		reader.close();

		return response;
	}

	public static String[] readMultipleLinesRespone() throws IOException {
		InputStream inputStream = null;
		if (httpConn != null) {
			inputStream = httpConn.getInputStream();
		} else {
			throw new IOException("Connection is not established.");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> response = new ArrayList<String>();

		String line = "";
		while ((line = reader.readLine()) != null) {
			response.add(line);
		}
		reader.close();

		return (String[]) response.toArray(new String[0]);
	}

	public static String[] readMultipleLinesRespone(HttpServletRequest req) throws IOException {
		InputStream inputStream = req.getInputStream();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		List<String> response = new ArrayList<String>();

		String line = "";
		while ((line = reader.readLine()) != null) {
			response.add(line);
		}
		reader.close();

		return (String[]) response.toArray(new String[0]);
	}

	
	public static void disconnect() {
		if (httpConn != null) {
			httpConn.disconnect();
		}
	}

}
