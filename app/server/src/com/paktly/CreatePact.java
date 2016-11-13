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

@WebServlet("/addpact/")
public class CreatePact extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = Logger.getLogger(CreatePact.class.getName());

	private static int pactId = 1;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String[] params = HTTPUtility.readMultipleLinesRespone(request);
			String param = String.join("", params);
			JSONObject paramJSON = new JSONObject(param);

			String phone1 = (String) paramJSON.get("phone1");
			String phone2 = (String) paramJSON.get("phone2");
			String startTime = (String) paramJSON.get("start_time");
			DBHandler dbHandler = new DBHandler();
			int resCode = dbHandler.update("insert into pacts values(?, ?, ?, ?)", Integer.toString(++pactId) , phone1, phone2, startTime), pid=-1;
			if(resCode == 1){
				pid = pactId;
			}
			response.getWriter().println(pid);
			dbHandler.close();
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, "Exception occured while registering a user!");
			e.printStackTrace();
		}
	}

}
