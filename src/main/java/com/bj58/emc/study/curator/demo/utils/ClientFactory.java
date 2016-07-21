package com.bj58.emc.study.curator.demo.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ClientFactory {

	public static CuratorFramework newClient() {
		String connectionString = "127.0.0.1:2180,127.0.0.1:2181,127.0.0.1:2182";
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client =  CuratorFrameworkFactory.newClient(connectionString, retryPolicy);
		/*CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectionString)
				.connectionTimeoutMs(30000)
				.canBeReadOnly(false)
				.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
				.namespace("superconfig")
				.defaultData(null)
				.build();*/
		client.getCuratorListenable().addListener(new CuratorListener() {
			public void eventReceived(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
				System.out.println("------------事件-----------"+curatorEvent.getType()+"------------");
			}
		});

		client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
			public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
				System.out.println("------------事件connectionState-----------"+connectionState+"------------");
			}
		});

		return client;
	}

	public static void main(String[] args) {
		ClientFactory.newClient();

	}
}
