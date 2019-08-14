package com.github.TakMashido.Tools.plotLib;

public abstract class Avarager {
	public abstract float getAvarage();
	public abstract void add(float val);
	public void add(float[] vals) {
		add(vals,0,vals.length);
	}
	public void add(float[] vals, int offset, int length) {
		length+=offset;
		for(int i=offset;i<length;i++)add(vals[i]);
	}
}