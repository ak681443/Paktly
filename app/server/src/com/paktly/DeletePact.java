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

@WebServlet("/delpact/")
public class DeletePact extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final Logger LOGGER = Logger.getLogger(DeletePact.class.getName());

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String[] params = HTTPUtility.readMultipleLinesRespone(request);
			String param = String.join("", params);
			JSONObject paramJSON = new JSONObject(param);

			String phone1 = (String) paramJSON.get("phone1");
			String phone2 = (String) paramJSON.get("phone2");
			String pactid = (String) paramJSON.get("pactid");
			DBHandler dbHandler = new DBHandler();
			int resCode = dbHandler.update("delete from pacts where pactid=? and ((phone1=? and phone2=?) or (phone2=? and phone1=?))", pactid , phone1, phone2, phone2, phone1);

			response.getWriter().println(resCode);
			dbHandler.close();
		}catch(Exception e) {
			LOGGER.log(Level.WARNING, "Exception occured while registering a user!");
			e.printStackTrace();
		}
	}

}
