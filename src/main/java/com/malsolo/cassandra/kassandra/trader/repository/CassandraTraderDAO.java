package com.malsolo.cassandra.kassandra.trader.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.malsolo.cassandra.kassandra.trader.model.Quote;

public class CassandraTraderDAO {

	// Cassandra connection 
	private final CassandraManager client = new CassandraManager();
	
	// CQL to insert quote data
	private final String INSERT_QUOTE_CQL = "INSERT INTO quote (symbol, price_time, "
			+ "open_price, high_price, low_price, close_price, "
			+ "volume) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	// CQL to select quote of a stock over a date period
	private final String SELECT_QUOTE_CQL = "SELECT * FROM quote "
			+ "WHERE symbol = ? AND price_time >= ? AND price_time <= ?";
	
	// CQL to get the latest date of the stock quote data of a stock
	private final String LAST_QUOTE_CQL = "SELECT price_time FROM quote "
			+ "WHERE symbol = ? ORDER BY price_time DESC LIMIT 1";

	// prepared statements for higher performance
	private PreparedStatement psInsertQuoteCQL;
	private PreparedStatement psSelectQuoteCQL;
	private PreparedStatement psLastQuoteCQL;

	public CassandraTraderDAO(final String newHost, final int newPort) {
		// get Cassandra connection
		client.connect(newHost, newPort, "malsolocdma");
		
		// prepare the statements
		prepareInsertQuoteCQL();
		prepareSelectQuoteCQL();
		prepareLastQuoteCQL();
	}

	// disconnect from Cassandra
	public void close() {
		client.close();
	}

	private void prepareInsertQuoteCQL() {
		psInsertQuoteCQL = client.getSession().prepare(INSERT_QUOTE_CQL);
	}

	private void prepareSelectQuoteCQL() {
		psSelectQuoteCQL = client.getSession().prepare(SELECT_QUOTE_CQL);
	}

	private void prepareLastQuoteCQL() {
		psLastQuoteCQL = client.getSession().prepare(LAST_QUOTE_CQL);
	}

	// persist a Quote POJO
	public void saveQuote(Quote q) {
		BoundStatement bs = new BoundStatement(psInsertQuoteCQL);

		client.getSession().execute(
				bs.bind(q.getSymbol(), q.getPrice_time(), q.getOpen_price(),
						q.getHigh_price(), q.getLow_price(),
						q.getClose_price(), q.getVolume()));
	}

	// return a list of Quote
	public List<Quote> selectQuoteBySymbolAndDateRange(final String symbol,
			final Date fromDate, final Date toDate) {
		BoundStatement bs = new BoundStatement(psSelectQuoteCQL);

		final ResultSet quoteResults = client.getSession().execute(
				bs.bind(symbol.toUpperCase(), fromDate, toDate));
		List<Quote> quotes = new ArrayList<Quote>();

		// convert each row into Quote
		for (Row r : quoteResults) {
			quotes.add(new Quote(r.getString("symbol"),
					r.getDate("price_time"),
					r.getFloat("open_price"),
					r.getFloat("high_price"),
					r.getFloat("low_price"),
					r.getFloat("close_price"),
					r.getDouble("volume")));
		}

		return quotes;
	}

	// return the latest date of a stock
	public Date lastQuoteDateBySymbol(final String symbol) {
		BoundStatement bs = new BoundStatement(psLastQuoteCQL);

		final ResultSet quoteResults = client.getSession().execute(
				bs.bind(symbol.toUpperCase()));
		final Row r = quoteResults.one();

		return r != null ? r.getDate("price_time") : null;
	}
	
}
