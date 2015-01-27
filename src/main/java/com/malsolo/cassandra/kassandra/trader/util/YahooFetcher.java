package com.malsolo.cassandra.kassandra.trader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.malsolo.cassandra.kassandra.trader.model.Quote;

public class YahooFetcher {

	static Logger log = Logger.getLogger(YahooFetcher.class.getName());

	// collect stock quote data from Yahoo! Finance
	static public BufferedReader getStock(String symbol, int fromMonth,
			int fromDay, int fromYear, int toMonth, int toDay, int toYear) {

		try {
			// Retrieve CSV stream
			URL yahoo = new URL("http://ichart.yahoo.com/table.csv?s="
					+ symbol.toUpperCase() + "&a="
					+ Integer.toString(fromMonth) + "&b="
					+ Integer.toString(fromDay) + "&c="
					+ Integer.toString(fromYear) + "&d="
					+ Integer.toString(toMonth) + "&e="
					+ Integer.toString(toDay) + "&f="
					+ Integer.toString(toYear) + "&g=d&ignore=.csv");
			log.info(yahoo.toString());
			URLConnection connection = yahoo.openConnection();
			InputStreamReader is = new InputStreamReader(
					connection.getInputStream());
			// return the BufferedReader
			return new BufferedReader(is);

		} catch (IOException e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		return null;
	}

	// convert each stock quote data into a Quote POJO
	static public Quote parseQuote(String symbol, String[] feed) {

		Date price_time = null;
		float daylow = 0;
		float dayhigh = 0;
		float dayopen = 0;
		float dayclose = 0;
		double volume = 0;

		try {
			price_time = new SimpleDateFormat("yyyy-MM-dd").parse(feed[0]);
			dayopen = Float.parseFloat(feed[1]);
			dayhigh = Float.parseFloat(feed[2]);
			daylow = Float.parseFloat(feed[3]);
			dayclose = Float.parseFloat(feed[4]);
			volume = Double.parseDouble(feed[5]);
			
		} catch (ParseException e) {
			log.log(Level.SEVERE, e.toString(), e);
		}
		// create a Quote POJO
		return new Quote(symbol.toUpperCase(), price_time, dayopen, dayhigh,
				daylow, dayclose, volume);
	}

}
