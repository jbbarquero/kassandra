package com.malsolo.cassandra.kassandra.trader.repository;

import java.text.SimpleDateFormat;
import java.util.List;

import com.malsolo.cassandra.kassandra.trader.model.Quote;
import com.malsolo.cassandra.kassandra.trader.util.CassandraTraderUtil;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;

public class TradingSignal {
	
	private Core taLibCore;
	
	public TradingSignal() {
		// instantiate TA-Lib
		taLibCore = new Core();
	}

	// scan list of Quote for close price crossing over 10-Day SMA
	public void sma10BreakUp(final List<Quote> quotes) {

		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		// set 10 days for simple moving average
		final int PERIODS_AVERAGE = 10;
		
		// the close price series
		double[] closePrice = new double[quotes.size()];
		
		// the 10-Day SMA series
		double[] out = new double[quotes.size()];
		
		// index of first 10-Day SMA
		MInteger begin = new MInteger();
		// number of 10-Day SMA
		MInteger length = new MInteger();

		// prepare the close price series
		for (int i = 0; i < quotes.size(); i++)
			closePrice[i] = quotes.get(i).getClose_price();

		// call TA-Lib sma function to prepare the 10-Day SMA series
		RetCode retCode = taLibCore.sma(0, closePrice.length - 1, closePrice,
				PERIODS_AVERAGE, begin, length, out);

		// if sma success
		if (retCode == RetCode.Success) {
			// scan for close price crosses over 10-Day SMA 
			for (int i = begin.value; i < quotes.size(); i++) {
				if ((i > begin.value)
						&& (closePrice[i - 1] < out[i - 1 - begin.value])
						&& (closePrice[i] > out[i - begin.value])) {
					// print out the signal
					StringBuilder line = new StringBuilder();
					line.append("Period #");
					line.append(i + 1);
					line.append(" Signal=SMA10BreakUp");
					line.append(" Date=");
					line.append(df.format(quotes.get(i).getPrice_time()));
					line.append(" prevClose=");
					line.append(CassandraTraderUtil
							.convertDecimal(closePrice[i - 1]));
					line.append(" prevSMA(10)=");
					line.append(CassandraTraderUtil.convertDecimal(out[i - 1
							- begin.value]));
					line.append(" close=");
					line.append(CassandraTraderUtil
							.convertDecimal(closePrice[i]));
					line.append(" SMA(10)=");
					line.append(CassandraTraderUtil.convertDecimal(out[i
							- begin.value]));
					System.out.println(line.toString());
				}
			}
		}
	}

}
