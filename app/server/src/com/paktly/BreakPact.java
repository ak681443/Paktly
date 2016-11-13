package com.paktly;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.paktly.db.DBHandler;
import com.paktly.util.HTTPUtility;

@WebServlet("/breakpact/")
public class BreakPact extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final double PENALTYAMOUNT = 0.5; // 50 cents for every violation of pact

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			String[] params = HTTPUtility.readMultipleLinesRespone(request);
			String param = String.join("", params);
			JSONObject paramJSON = new JSONObject(param);

			String initiatorPhone = (String) paramJSON.get("phone"), receiverPhone = "";
			String pactid = (String) paramJSON.get("pactid");
			DBHandler dbHandler = new DBHandler();
			int resCode = -1;
			ResultSet rs = dbHandler.execute("select * from pacts where pactid=?", pactid);
			while (rs.next()) {
				String phone1 = rs.getString("phone1");
				String phone2 = rs.getString("phone2");
				if(phone1.equals(initiatorPhone)){
					receiverPhone = phone2;
				}
				else{
					receiverPhone = phone1;
				}
			}
			resCode = dbHandler.update("delete from pacts where pactid=? and (phone1=? or phone2=?)", pactid , initiatorPhone, initiatorPhone);

			//TODO: Penalise the pact breaker by deduction money from his account

			if(resCode == 1) {
				rs = dbHandler.execute("select accno, name from users where phone=?", receiverPhone);
				String payerNo = "", payeeNo = "", payerName = "", payeeName = "";
				while (rs.next()) {
					payerNo = rs.getString(1);
					payerName = rs.getString(2);
				}
				rs = dbHandler.execute("select accno, name from users where phone=?", initiatorPhone);
				while (rs.next()) {
					payeeNo = rs.getString(1);
					payeeName = rs.getString(2);
				}

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String transDate = dateFormat.format(date).toString().split("\\\\s+")[0];

				JSONObject nessieReq = new JSONObject();
				nessieReq.put("medium", "balance");
				nessieReq.put("payee_id", payeeNo);
				nessieReq.put("transaction_date", transDate);
				nessieReq.put("amount", PENALTYAMOUNT);
				nessieReq.put("description", "This is yours, Mr. "+payeeName+"!. Your friend "+payerName+" gave up on the pact.");

				HTTPUtility.sendPostRequestWithJSON("http://api.reimaginebanking.com/accounts/"+payerNo+"/transfers?key="+HTTPUtility.apiKey, nessieReq);
				System.out.println("$$$ Response code: "+HTTPUtility.httpConn.getResponseCode());

			} 
		}
		catch (SQLException | JSONException e) {
			e.printStackTrace();
		}
		response.getWriter().println(resCode);
		dbHandler.close();
	}

}
