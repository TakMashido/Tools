package com.github.TakMashido.Tools.plotLib;

public class AvaragerFactory {
	public static Avarager getMaxAvarager() {
		return new MaxAvarager();
	}
	
	public static class MaxAvarager extends Avarager{
		private float max=0;
		@Override
		public float getAvarage() {
			return max;
		}
		@Override
		public void add(float val) {
			if(val>max)max=val;
		}
		public void add(short[] vals, int offset, int length) {
			
		}
	}
}