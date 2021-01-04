package com.github.TakMashido.Tools.stopwatch;

public class LongCatcherGroup {
	private static final float movingFactor=.99f;
	private static final float noiseMovingFactor=.999f;
	
	private static final long noiseThresholdUp=1500;							//Boundaries for normal moving factor usage 
	private static final long noiseThresholdDown=666;							//In for ratioOfValue*1000
	
	
	class LongCatcher{
		long min=Long.MAX_VALUE;
		long max=Long.MIN_VALUE;
		
		long avaraged=0;
		
		public LongCatcher(long value) {
			min=value;
			max=value;
			avaraged=value;
		}
		
		void put(long value) {
			long ratio=avaraged*1000/value;
			
			if(value>max)
				max=value;
			if(value<min)
				min=value;
			
			if(ratio>noiseThresholdDown&&ratio<noiseThresholdUp) {
				value*=1-movingFactor;
				avaraged*=movingFactor;
				avaraged+=value;
			} else {
				value*=1-noiseMovingFactor;
				avaraged*=noiseMovingFactor;
				avaraged+=value;
			}
		}
		
		@Override
		public String toString() {
			return String.format("%.2f : %.2f : %.2f ms", avaraged/1000000f,min/1000000f,max/1000000f);
		}
	}
}