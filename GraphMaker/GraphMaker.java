import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GraphMaker{
	public static void main(String[]args) {
		System.setErr(System.out);
		
		String fileName=null;
		String outFileName=null;
		String outFormat="png";
		int y=500;
		int x=0;
		boolean invert=false;
		boolean show=false;
		
		for(int i=0;i<args.length;i++) {
			switch(args[i]) {
			case "-y":
				y=Integer.parseInt(args[++i]);
				break;
			case "-x":
				x=Integer.parseInt(args[++i]);
				break;
			case "-out":
				outFileName=args[++i];
				break;
			case "-format":
			case "-f":
				outFormat=args[++i];
				break;
			case "-in":
				fileName=args[++i];
				break;
			case "-i":
				invert=true;
				break;
			case "-show":
			case "-s":
				show=true;
				break;
			case "-info":
				System.out.println("Author:  Tak Mashido");
				System.out.println("Version: 1.0");
				System.out.println("Compilation date: 29.10.2018");
				return;
			case "-help":
			case "-h":
				System.out.println("Graph creation tool.");
				System.out.println("GraphMaker [dataFileName] [options]");
				System.out.println("Options:");
				System.out.println("-y: \tSets height of graph");
				System.out.println("-x: \tSets width of graph");
				System.out.println("-out: \tName of output file. Default fileName");
				System.out.println("-format:");
				System.out.println("-f \tFormat of saved graph image. Default .png");
				System.out.println("-in: \tFile name. Default: first input command if not special one");
				System.out.println("-i: \tInverts data verticaly");
				System.out.println("-show:");
				System.out.println("-s: \tOpen output image in default image viewer");
				System.out.println("-info: \tPrints readme");
				System.out.println("-help:");
				System.out.println("-h: \tPrints this message");
				return;
			default:
				if(i==0)fileName=args[0];
				else {
					System.out.print(args[i]+" is not correct option.");
				}
			}
		}
		
		if(fileName==null) {
			System.out.print("Please set input data");
			return;
		}
		if(outFileName==null)
			outFileName=fileName;
		
		
		Scanner scanner=null;
		try {
			scanner=new Scanner(new FileInputStream(fileName));
		} catch (FileNotFoundException ex) {
			System.out.println("File \""+fileName+".\" doesn't exist");
			return;
		}
		
		ArrayList<Double> dataBuffer=new ArrayList<Double>();
		while(scanner.hasNext())
			dataBuffer.add(Double.parseDouble(scanner.next()));
		
		double max=-Double.MAX_VALUE;
		double min=Double.MAX_VALUE;
		
		Double[] data=dataBuffer.toArray(new Double[0]);
		for(Double value:dataBuffer) {
			if(value>max)max=value;
			if(value<min)min=value;
		}
		
		if(x==0)
			x=data.length;
		
		double yscale=(y-1)/(max-min);
		
		BufferedImage graph=new BufferedImage(x,y,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g=graph.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, x, y);
		g.setColor(Color.BLACK);
		
		g.drawString(Double.toString(invert?-min:max), 2, 11);
		g.drawString(Double.toString(invert?-max:min), 2, y-2);
		
		int length;
		int end=-1;
		int lastY=invert?0:y;
		int newY=0;
		for(int i=0;i<x;i++) {
			length=(data.length-end-1)/(x-i);
			double value=avarage(data,end+1,end+length+1)-min;
			newY=(int)(value*yscale)-1;
			if(!invert)newY=y-newY;
			if((invert?lastY<newY:lastY>newY)) {
				g.setColor(Color.GRAY);
				g.drawLine(i, lastY, i, newY);
				g.setColor(Color.BLACK);
			}
			g.fillRect(i, newY, 1, 1);
			lastY=newY;
			end+=length;
		}
		
		if(show) {
			String imageName=fileName.substring(0,fileName.indexOf('.'))+"."+outFormat;
			try {
				ImageIO.write(graph, outFormat, new File(imageName));
				Runtime.getRuntime().exec("cmd /c "+fileName.substring(0,fileName.indexOf('.'))+"."+outFormat);
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Graph saved");
	}
	
	private static double avarage(Double[] data, int start, int end) {
		double Return=0;
		for(int i=start;i<end;i++)
			Return+=data[i];
		return Return/(end-start);
	}
}