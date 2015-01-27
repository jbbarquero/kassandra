package com.malsolo.cassandra.kassandra.trader.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity(name = "quote")
public class Quote implements Serializable {

	private static final long serialVersionUID = 658600857728808439L;

	@Column(name = "symbol")
	private String symbol;

	public Quote(String symbol, Date price_time, Float open_price,
			Float high_price, Float low_price, Float close_price, Double volume) {
		super();
		this.symbol = symbol;
		this.price_time = price_time;
		this.close_price = close_price;
		this.high_price = high_price;
		this.low_price = low_price;
		this.open_price = open_price;
		this.volume = volume;
	}

	@Column(name = "price_time")
	private Date price_time;

	@Column(name = "close_price")
	private Float close_price;

	@Column(name = "high_price")
	private Float high_price;

	@Column(name = "low_price")
	private Float low_price;

	@Column(name = "open_price")
	private Float open_price;

	@Column(name = "volume")
	private Double volume;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Date getPrice_time() {
		return price_time;
	}

	public void setPrice_time(Date price_time) {
		this.price_time = price_time;
	}

	public Float getClose_price() {
		return close_price;
	}

	public void setClose_price(Float close_price) {
		this.close_price = close_price;
	}

	public Float getHigh_price() {
		return high_price;
	}

	public void setHigh_price(Float high_price) {
		this.high_price = high_price;
	}

	public Float getLow_price() {
		return low_price;
	}

	public void setLow_price(Float low_price) {
		this.low_price = low_price;
	}

	public Float getOpen_price() {
		return open_price;
	}

	public void setOpen_price(Float open_price) {
		this.open_price = open_price;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Quote) {
			Quote q = Quote.class.cast(obj);
			return new EqualsBuilder().append(symbol, q.symbol)
					.append(price_time, q.price_time).isEquals();
		}
		return false;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

}

