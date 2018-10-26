import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class GraphMaker{
	public static void main(String[]args) {		
		System.setErr(System.out);
		
		ArrayList<String> fileNames=new ArrayList<String>();
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
				fileNames.add(args[++i]);
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
				System.out.println("Version: 1.1");
				System.out.println("Compilation date: 25.10.2018");
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
				System.out.println("-in: \tFile name. If specyfied more then one time plots multiple graphs in one file. Default: input command before special firs special one.");
				System.out.println("-i: \tInverts data verticaly");
				System.out.println("-show:");
				System.out.println("-s: \tOpen output image in default image viewer");
				System.out.println("-info: \tPrints readme");
				System.out.println("-help:");
				System.out.println("-h: \tPrints this message");
				return;
			default:
				if(fileNames.size()==i)fileNames.add(args[i]);
				else {
					System.out.print(args[i]+" is not correct option.");
				}
			}
		}
		
		if(fileNames.size()==0) {
			System.out.print("Please set input data");
			return;
		}
		if(outFileName==null)
			outFileName=fileNames.get(0);
		
		Double[][] data=new Double[fileNames.size()][];
		double max=-Double.MAX_VALUE;
		double min=Double.MAX_VALUE;
		
		String file;
		for(int i=0;i<fileNames.size();i++) {
			file=fileNames.get(i);
			Scanner scanner=null;
			try {
				scanner=new Scanner(new FileInputStream(file));
			} catch (FileNotFoundException ex) {
				System.out.println("File \""+file+".\" doesn't exist");
				return;
			}
			
			ArrayList<Double> dataBuffer=new ArrayList<Double>();
			while(scanner.hasNext())
				dataBuffer.add(Double.parseDouble(scanner.next()));
			
			data[i]=dataBuffer.toArray(new Double[0]);
			for(Double value:data[i]) {
				if(value>max)max=value;
				if(value<min)min=value;
			}
			
			scanner.close();
		}
		
		int maxX=0;
		for(Double[] elem:data)
			maxX=elem.length>x?elem.length:x;
		if(x==0)
			x=maxX;
		
		double yScale=(y-1)/(max-min);
		double xScale=maxX/(float)x;
		
		BufferedImage graph=new BufferedImage(x,y,BufferedImage.TYPE_BYTE_GRAY);
		Graphics g=graph.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, x, y);
		g.setColor(Color.BLACK);
		
		g.drawString(Double.toString(invert?-min:max), 2, 11);
		g.drawString(Double.toString(invert?-max:min), 2, y-2);
		
		for(int i=0;i<data.length;i++) {
			g.setColor(getColor());
			if(data.length>1)
				g.drawString(fileNames.get(i), 2, 30+i*13);
			
			int length;
			int end=0;
			float endFloat=0;
			int lastY=invert?0:y;
			int newY=0;
			for(int j=0;j<data[i].length;j++) {
				endFloat+=xScale;
				length=(int)(endFloat-end);
				double value=avarage(data[i],end,end+length)-min;
				newY=(int)(value*yScale)-1;
				if(!invert)newY=y-newY;
				if((invert?lastY<newY:lastY>newY)) {
					g.drawLine(j, lastY, j, newY);
				}
				g.fillRect(j, newY, 1, 1);
				lastY=newY;
				end+=length;
			}
		}
		
		if(show) {
			String imageName=outFileName.substring(0,outFileName.indexOf('.'))+"."+outFormat;
			try {
				ImageIO.write(graph, outFormat, new File(imageName));
				Runtime.getRuntime().exec("cmd /c "+outFileName.substring(0,outFileName.indexOf('.'))+"."+outFormat);
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
	
	private static Random random=new Random(0);
	private static Color getColor() {
		return new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255));
	}
}