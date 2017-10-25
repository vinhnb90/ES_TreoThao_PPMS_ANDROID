package com.es.tungnv.utils.aiball;

public interface IVideoSink {
	void onFrame(byte[] frame, byte[] pcm);
	void onInitError(String errorMessage);
	void onVideoEnd();
}