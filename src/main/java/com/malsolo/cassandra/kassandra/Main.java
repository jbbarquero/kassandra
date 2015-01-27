package com.malsolo.cassandra.kassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class Main {
	
	private Cluster cluster;
	private Session session;
	
	public void connect(String node) {
		this.cluster = Cluster.builder().addContactPoint(node).build();
		
		Metadata metadata = cluster.getMetadata();
		
		System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
		
		for (Host host : metadata.getAllHosts()) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n"
					, host.getDatacenter(), host.getAddress(), host.getRack());
		};
		
		session = cluster.connect();
	}
	
	public void close() {
		cluster.close();
	}

	public static void main(String[] args) {
		Main client = new Main();
		client.connect("127.0.0.1");
		client.close();
	}

}
