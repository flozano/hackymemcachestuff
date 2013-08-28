package com.flozano.memcachestuff;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class CleanSerializedByClass {
	public static void main(String[] argz) throws IOException,
			MemcachedException, InterruptedException, TimeoutException {
		MemcachedClient iteratorClient = null;
		MemcachedClient client = null;
		try {
			String host = argz[0];
			String value = argz[1];
			byte[] searchValue = value.getBytes(Charset.forName("ISO-8859-1"));
			XMemcachedClientBuilder clientBuilder = new XMemcachedClientBuilder(
					host);
			clientBuilder.setTranscoder(new ByteArrayTranscoder());
			iteratorClient = clientBuilder.build();
			client = clientBuilder.build();

			InetSocketAddress address = iteratorClient.getAvailableServers()
					.iterator().next();
			KeyIterator it = iteratorClient.getKeyIterator(address);
			while (it.hasNext()) {
				String key = it.next();
				byte[] keyValue = client.get(key);

				if (keyValue == null) {
					System.err.println(">>>>>>> Empty key " + key);
					continue;
				}
				if (needsToBeDeleted(key, keyValue)) {
					client.delete(key);
				}
			}

		} finally {
			iteratorClient.shutdown();
			client.shutdown();
		}
	}

	private static boolean needsToBeDeleted(String key, byte[] keyValue) {
		if (KPM.indexOf(keyValue, keyValue) != -1) {
			System.err.println(">>>>>>> OK " + key + " - removing");
			return true;
		} else {
			System.err.println("ko " + key + " - " + new String(keyValue));
			return false;
		}
	}

	private static boolean needsToBeDeleted_data(String key, byte[] keyValue) {
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(keyValue));
			Object o = ois.readObject();
			// TODO continue
			// if o instanceof blabla and fields match blabla...
			return false;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);

		}

	}
}
