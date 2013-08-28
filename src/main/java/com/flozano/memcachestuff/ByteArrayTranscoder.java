package com.flozano.memcachestuff;

import net.rubyeye.xmemcached.transcoders.CachedData;
import net.rubyeye.xmemcached.transcoders.CompressionMode;
import net.rubyeye.xmemcached.transcoders.Transcoder;
import net.rubyeye.xmemcached.transcoders.TranscoderUtils;

public class ByteArrayTranscoder implements Transcoder<byte[]> {

	private final TranscoderUtils u = new TranscoderUtils(false);

	public CachedData encode(byte[] o) {
		return new CachedData(0, o);
	}

	public byte[] decode(CachedData d) {
		return d.getData();
	}

	public void setPrimitiveAsString(boolean primitiveAsString) {
		// TODO Auto-generated method stub

	}

	public void setPackZeros(boolean packZeros) {
		// TODO Auto-generated method stub

	}

	public void setCompressionThreshold(int to) {
		// TODO Auto-generated method stub

	}

	public boolean isPrimitiveAsString() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPackZeros() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCompressionMode(CompressionMode compressMode) {
		// TODO Auto-generated method stub

	}

}
