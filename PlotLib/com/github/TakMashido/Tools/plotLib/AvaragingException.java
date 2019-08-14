package com.github.TakMashido.Tools.plotLib;

public class AvaragingException extends RuntimeException{
	private static final long serialVersionUID = 8292289776214024516L;
	
	public AvaragingException(String message) {
		super(message);
	}
	public AvaragingException() {
		this("Unsupported avaraging type");
	}
}