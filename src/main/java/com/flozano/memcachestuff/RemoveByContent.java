package com.flozano.memcachestuff;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.KeyIterator;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RemoveByContent {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RemoveByContent.class);

	public static void main(String[] argz) throws IOException,
			MemcachedException, InterruptedException, TimeoutException {
		MemcachedClient iteratorClient = null;
		MemcachedClient client = null;
		try {
			String host = argz[0];
			String searchString = argz[1];
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
					LOGGER.info("Empty key {}", key);
					continue;
				}
				if (needsToBeDeleted(key, keyValue, searchString)) {
					client.delete(key);
				}
			}

		} finally {
			iteratorClient.shutdown();
			client.shutdown();
		}
	}

	private static boolean needsToBeDeleted(String key, byte[] keyValue,
			String searchString) {
		byte[] searchValue = searchString.getBytes(Charset
				.forName("ISO-8859-1"));

		if (KPM.indexOf(keyValue, searchValue) > 0) {
			LOGGER.info("Found {} in key {} - removing", searchString, key);
			return true;
		} else {
			return false;
		}
	}
}
