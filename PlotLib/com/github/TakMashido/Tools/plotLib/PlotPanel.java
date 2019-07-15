package com.github.TakMashido.Tools.plotLib;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.TakMashido.Tools.plotLib.arrayPlot.ShortArrayPlot;

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
		
		int nameY=10;
		for(Plot plot:plots) {
			g.setColor(plot.color);
			
			g.drawString(plot.getName(), 2, nameY);
			nameY+=10;
			
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
			normalize(data);
			
			
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
	
	private static void normalize(float[] data) {
		float min=0;
		float max=0;
		for(float val:data) {
			if(val<min) min=val;
			if(val>max) max=val;
		}
		if(max==0) return;
		min*=-1.02f;
		max*=1.02f;
		max+=min;
		for(int i=0;i<data.length;i++)data[i]=1-(data[i]+min)/max;
	}
}