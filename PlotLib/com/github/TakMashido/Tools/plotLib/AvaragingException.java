package com.github.TakMashido.Tools.plotLib;

public class AvaragingException extends RuntimeException{
	public AvaragingException(String message) {
		super(message);
	}
	public AvaragingException() {
		this("Unsupported avaraging type");
	}
}