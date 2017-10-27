package com.cts.application.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cts.application.request.DateRequest;

public class DateUtils {
	public static Date convertDateRequestToDate(DateRequest dateRequest) throws ParseException {
		Date date;
		String dateString = dateRequest.getDate().getYear()
				+"-"+dateRequest.getDate().getMonth()
				+"-"+dateRequest.getDate().getDay();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			System.out.println("In DateUtils: "+ e.getMessage());
			throw e;
		}
		return date;
	}
	
	/*public static void main(String args[]) {
		DateRequest dr = new DateRequest();
		dr.setYear("1988");
		dr.setMonth("01");
		dr.setDay("24");
		
		try {
			Date dd = new DateUtils().convertDateRequestToDate(dr);
			System.out.println(dd);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}
