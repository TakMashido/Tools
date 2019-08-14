package com.github.TakMashido.Tools.plotLib.arrayPlot;

import java.awt.Color;

import com.github.TakMashido.Tools.plotLib.Avaraging;
import com.github.TakMashido.Tools.plotLib.AvaragingException;
import com.github.TakMashido.Tools.plotLib.Plot;

/**Generates plot from short array;
 * @author TakMashido
 */
public class FloatArrayPlot extends Plot{
	private float[] data;
	private Avaraging avaraging=Avaraging.AVARAGE;
	
	private float max=Float.NaN;
	private float min=Float.NaN;
	
	public FloatArrayPlot(float[] array) {
		data=array;
	}
	public FloatArrayPlot(float[] array, Avaraging avar) {
		data=array;
		setAvaraging(avar);
	}
	public FloatArrayPlot(String name, float[] array) {
		super(name);
		data=array;
	}
	public FloatArrayPlot(String name,float[] array, Avaraging avar) {
		super(name);
		data=array;
		setAvaraging(avar);
	}
	public FloatArrayPlot(String name, Color color, float[] array) {
		super(name,color);
		data=array;
	}
	public FloatArrayPlot(String name, Color color, float[] array, Avaraging avar) {
		super(name,color);
		data=array;
		setAvaraging(avar);
	}
	
	@Override
	public float get(int startIndex, int endIndex) {
		if(endIndex<startIndex) {
			int temp=startIndex;
			startIndex=endIndex;
			endIndex=temp;
		}
		
		if(startIndex<0)startIndex=0;
		if(endIndex>data.length)endIndex=data.length;
		
		float Return=0;
		switch(avaraging) {
		case MAX:
			Return=Integer.MIN_VALUE;
			for(int i=startIndex;i<endIndex;i++) {
				Return=Math.max(data[i], Return);
			}
			return Return;
		case MIN:
			Return=Integer.MAX_VALUE;
			for(int i=startIndex;i<endIndex;i++) {
				Return=Math.min(data[i], Return);
			}
			return Return;
		case AVARAGE:
			double ret=0;
			for(int i=startIndex;i<endIndex;i++) {
				ret+=data[i];
			}
			return (float) (ret/(endIndex-startIndex));
		default: throw new AvaragingException();
		}
	}
	@Override
	public int getSize() {
		return data.length;
	}
	
	public Avaraging getAvaraging() {
		return avaraging;
	}
	public void setAvaraging(Avaraging avaraging) {
		this.avaraging = avaraging;
	}
	
	@Override
	public float getMin() {
		if(min!=min) {
			min=Float.MAX_VALUE;
			for(Float dat:data)if(dat<min)min=dat;
		}
		return min;
	}
	@Override
	public float getMax() {
		if(max!=max) {
			max=-Float.MAX_VALUE;
			for(Float dat:data)if(dat>max)max=dat;
		}
		return max;
	}
}