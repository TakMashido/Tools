package com.github.TakMashido.Tools.plotLib;

import java.awt.Color;
import java.util.Random;

/**Represents function to plot.
 * Supports only integer based indexs.
 * @author TakMashido
 */
public abstract class Plot {
	private static Random random=new Random(1337);
	
	/**Color which will be used to draw this plot*/
	public Color color=new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
	
	/**Value used to auto generate plot name*/
	private static int lastPlotIndex=0;
	/**Name of this plot*/
	protected String name;
	
	public Plot() {
		name="plot"+lastPlotIndex++;
	}
	public Plot(String name) {
		this.name=name;
	}
	public Plot(Color color) {
		super();
		this.color=color;
	}
	public Plot(String name, Color color) {
		this.name=name;
		this.color=color;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
	
	/**Returns avaraged value from given range.
	 * @param startIndex Point of avaraging start.
	 * @param endIndex Point of avaraging end.
	 * @return Value of appling avaraging method to values in given range.
	 */
	public abstract float get(int startIndex,int endIndex);
	
	/**Return maximum value of plot or NaN if unknow.
	 * @return Read above.
	 */
	public abstract float getMax();
	/**Return minimum value of plot or NaN if unknow.
	 * @return Read above.
	 */
	public abstract float getMin();
	
	/**Returns preffered size of graph or -1 if not know.
	 * @return Read above.
	 */
	public abstract int getSize();
}