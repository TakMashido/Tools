package com.github.TakMashido.Tools.plotLib.arrayPlot;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.TakMashido.Tools.plotLib.Avaraging;
import com.github.TakMashido.Tools.plotLib.AvaragingException;
import com.github.TakMashido.Tools.plotLib.Plot;

/**Generates plot from short array;
 * @author TakMashido
 */
public class ShortArrayPlot extends Plot{
	private short[] data;
	private Avaraging avaraging=Avaraging.AVARAGE;
	
	public ShortArrayPlot(short[] array) {
		data=array;
	}
	public ShortArrayPlot(short[] array, Avaraging avar) {
		data=array;
		setAvaraging(avar);
	}
	public ShortArrayPlot(String name, short[] array) {
		super(name);
		data=array;
	}
	public ShortArrayPlot(String name,short[] array, Avaraging avar) {
		super(name);
		data=array;
		setAvaraging(avar);
	}
	public ShortArrayPlot(String name, Color color, short[] array) {
		super(name,color);
		data=array;
	}
	public ShortArrayPlot(String name, Color color, short[] array, Avaraging avar) {
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
		
		int Return=0;
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
		case MOST_FREQUENT:
			HashMap<Short,AtomicInteger> occurences=new HashMap<Short,AtomicInteger>();
			for(int i=startIndex;i<endIndex;i++) {
				AtomicInteger val=occurences.get(data[i]);
				if(val==null) {
					occurences.put(data[i], new AtomicInteger(1));
				} else {
					val.incrementAndGet();
				}
			}
			
			Iterator<Short> keys=occurences.keySet().iterator();
			int max=0;
			float val=0;
			while(keys.hasNext()) {
				short tempVal=keys.next();
				int occ=occurences.get(tempVal).get();
				if(occ>max) {
					max=occ;
					val=tempVal;
				}
			}
			
			return val;
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
}