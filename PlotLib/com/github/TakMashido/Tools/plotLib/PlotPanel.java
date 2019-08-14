package com.github.TakMashido.Tools.plotLib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PlotPanel extends JPanel{
	private static final long serialVersionUID = -2369931890928429839L;
	
	private List<Plot> plots;
	
	public PlotPanel(List<Plot> plots) {
		this.plots=plots;
	}
	public PlotPanel(Plot... plots) {
		this.plots=new ArrayList<Plot>();
		for(int i=0;i<plots.length;i++) {
			this.plots.add(plots[i]);
		}
	}
	
	/**Creates new window containing PlotPanel with given plots.
	 * @param plots Plots to draw.
	 * @return Created window.
	 */
	public static JFrame showWindow(Plot... plots) {
		return showWindow("Plot",plots);
	}
	/**Creates new window containing PlotPanel with given plots.
	 * @param title Name of window.
	 * @param plots Plots to draw.
	 * @return Created window.
	 */
	public static JFrame showWindow(String title,Plot... plots) {
		JFrame frame=new JFrame();
		frame.add(new PlotPanel(plots));
		frame.setTitle(title);
		frame.setSize(400,300);
		frame.setVisible(true);
		
		return frame;
	}
	
	/**Adds new plot to panel and repaints it.
	 * @param plot
	 */
	public void addPlot(Plot plot) {
		plots.add(plot);
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		int maxX=getWidth();
		int maxY=getHeight();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, maxX, maxY);
		
		int plotIndex=-1;
		for(Plot plot:plots) {
			plotIndex++;
			g.setColor(plot.color);
			
			g.drawString(plot.getName(), maxX-g.getFontMetrics().stringWidth(plot.getName())-4, plotIndex*11+10);
			
			float jump=plot.getSize();
			if(jump==-1) throw new RuntimeException("Plots with unknown size are not supported yet.");
			jump/=maxX;
			
			float index=0;
			float[] data=new float[maxX];
			for(int i=0;i<maxX;i++) {
				float endIndex=index+jump;
				int intIndex=(int)index;
				int intEndIndex=(int)endIndex;
				if(intIndex==intEndIndex)
					data[i]=Float.NaN;
				else
					data[i]=plot.get(intIndex, intEndIndex);
				index=endIndex;
			}
			
			float min=plot.getMin();
			if(min!=min)min=getMin(data);
			float max=plot.getMax();
			if(max!=max)max=getMax(data);
			normalize(data,min,max);
			
			g.drawString(String.format("%.2f", max), 2, plotIndex*11+10);
			g.drawString(String.format("%.2f", min), 2, maxY-plotIndex*11-4);
			
			int ind=0;
			int lastX=0;
			int lastY=0;
			for(;ind<data.length;ind++) {
				if(data[ind]!=data[ind])continue;
				lastX=ind;
				lastY=(int)(data[ind]*maxY);
				break;
			}
			for(;ind<data.length;ind++) {
				if(data[ind]!=data[ind])continue;
				int y=(int)(data[ind]*maxY);
				g.drawLine(ind, y, lastX, lastY);
				lastX=ind;
				lastY=y;
			}
		}
	}
	
	private static float getMin(float[] data) {
		float min=Float.MAX_VALUE;
		for(Float dat:data)if(dat<min)min=dat;
		return min;
	}
	private static float getMax(float[] data) {
		float max=-Float.MAX_VALUE;
		for(Float dat:data)if(dat<max)max=dat;
		return max;
	}
	private static void normalize(float[] data, float min, float max) {
		if(min==max) {
			Arrays.fill(data, .5f);
			return;
		}
		min*=-1.02f;
		max*=1.02f;
		max+=min;
		for(int i=0;i<data.length;i++)data[i]=1-(data[i]+min)/max;
	}
}