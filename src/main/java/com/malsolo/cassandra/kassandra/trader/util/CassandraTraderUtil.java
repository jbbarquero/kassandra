package com.malsolo.cassandra.kassandra.trader.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CassandraTraderUtil {

	static Logger log = Logger.getLogger(CassandraTraderUtil.class.getName());

	// convert string to date
	static public Date convertDate(final String dateString) {

		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;

		try {
			d = df.parse(dateString);
		} catch (ParseException e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		return d;
	}
	
	// convert double to 2 decimal places
	static public String convertDecimal(final double value) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(value);
	}

}
