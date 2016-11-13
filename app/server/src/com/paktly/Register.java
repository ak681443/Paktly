package com.paktly;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.paktly.db.DBHandler;
import com.paktly.util.HTTPUtility;

@WebServlet("register/")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = Logger.getLogger(Register.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String[] params = HTTPUtility.readMultipleLinesRespone(request);
			String param = String.join("", params);
			JSONObject paramJSON = new JSONObject(param);
			String firstName = (String) paramJSON.get("first_name");
			String lastName = (String) paramJSON.get("last_name");
			String phone = (String) paramJSON.get("phone");
			JSONObject nessieReq = new JSONObject();
			JSONObject address = new JSONObject();	
			int resCode = 0;


			// Assigning some dummy address for all users, just for the POC
			address.put("street_number", "34");
			address.put("street_name", "SW");
			address.put("city", "GNV");
			address.put("state","FL");
			address.put("zip", "32777");
			nessieReq.put("address", address);
			nessieReq.put("first_name", firstName);
			nessieReq.put("last_name", lastName);

			HTTPUtility.sendPostRequestWithJSON("http://api.reimaginebanking.com/customers?key="+HTTPUtility.apiKey, nessieReq);

			if (HTTPUtility.httpConn.getResponseCode() == 201) {
				String[] nessieResponse = HTTPUtility.readMultipleLinesRespone();
				String res = String.join("", nessieResponse);
				JSONObject resJSON = new JSONObject(res);
				String id = (String) ((JSONObject) resJSON.get("objectCreated")).get("_id");
				nessieReq = new JSONObject();
				// Create a new account for managing our paktly transactions, just for our convenience. TODO: Should get a capital one account if it already exists with the user.
				nessieReq.put("type", "Credit Card");
				nessieReq.put("nickname", firstName);
				nessieReq.put("rewards", 0);
				nessieReq.put("balance", 100); // Setting an initial balance of $100

				HTTPUtility.sendPostRequestWithJSON("http://api.reimaginebanking.com/customers/"+id+"/accounts?key="+HTTPUtility.apiKey, nessieReq);
				if (HTTPUtility.httpConn.getResponseCode() == 201) {
					nessieResponse = HTTPUtility.readMultipleLinesRespone();
					res = String.join("", nessieResponse);
					resJSON = new JSONObject(res);
					String accNo = (String) ((JSONObject) resJSON.get("objectCreated")).get("_id");

					DBHandler dbHandler = new DBHandler();
					resCode = dbHandler.update("insert into users values(?, ?, ?, ?)", firstName, phone, id, accNo);
					dbHandler.close();
				}
			}

		}
		catch (Exception e) {
			LOGGER.log(Level.WARNING, "Exception occured while registering a user!");
			e.printStackTrace();
		}
		finally {
			response.getWriter().println(resCode);
		}
	}

}
