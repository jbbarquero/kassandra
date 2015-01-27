package com.malsolo.cassandra.kassandra.trader.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraManager {
	
	private Cluster cluster;
	private Session session;
	private String keyspace;
	
	public Session getSession() {
		return session;
	}
	
	public String getKeyspace() {
		return keyspace;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	// connect to a Cassandra cluster and a keyspace
	public void connect(final String node, final int port, final String keyspace) {
		this.cluster = Cluster.builder().addContactPoint(node).withPort(port).build();
		session = cluster.connect(keyspace);
	}
	
	// disconnect from Cassandra
	public void close() {
		cluster.close();
	}

}
