package com.es.tungnv.Aiball;

public interface IVideoSink {
	void onFrame(byte[] frame, byte[] pcm);
	void onInitError(String errorMessage);
	void onVideoEnd();
}