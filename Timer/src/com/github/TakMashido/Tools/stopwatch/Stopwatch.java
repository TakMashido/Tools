package com.github.TakMashido.Tools.stopwatch;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import com.github.TakMashido.Tools.stopwatch.LongCatcherGroup.LongCatcher;

/**Simple high precision stopwatch for code profiling.
 * Supports measuring multiple nested periods.
 * Period is time period with allowed nesting.
 * Moment is non nestable period. When new one starts previous one ends.
 * @author TakMashio */
public class Stopwatch{
	private LongCatcherGroup masterChatcher=new LongCatcherGroup();
	
	private long lastTick;
	
	private LongCatcher fullPeriod;
	
	private StringTree treeRoot=new StringTree();
	private HashMap<String,StringTree> tree=new HashMap<>();						//Map of tree elements to be able to print it
	private HashMap<String,LongCatcher> periods=new HashMap<>();					//Contain all registered periods and moments
	
	private Stack<String> periodNames=new Stack<>();
	private Stack<Long> periodsTimes=new Stack<>();
	
	private boolean isPreviousMoment=false;											//Is previously registered period a moment?
	
	public Stopwatch() {
		start();
	}
	
	/**Start/restart stopwatch.
	 * Set lastTick to current time and drains periods stack without updating their time. 
	 */
	public void start() {
		lastTick=System.nanoTime();
		
		periodNames.clear();
		periodsTimes.clear();
	}
	
	/**Start new nested period.*/
	public void startPeriod(String name) {
		if(isPreviousMoment) {
			endPeriod0();
			isPreviousMoment=false;
		}
		periodNames.push(name);
		periodsTimes.push(System.nanoTime());
	}
	/**End previously started period and moment and update their times.
	 * @return Current period time in ms.
	 */
	public double endPeriod() {
		if(isPreviousMoment) {
			endPeriod0();
			isPreviousMoment=false;
		}
		
		return endPeriod0()/1e6;
	}
	private long endPeriod0() {
		long time=System.nanoTime()-periodsTimes.pop();
		String name=periodNames.pop();
		
		LongCatcher catcher=periods.get(name);
		if(catcher==null) {
			periods.put(name, masterChatcher.new LongCatcher(time));
			
			//Push it to the tree
			
			String parent=periodNames.empty()?null:periodNames.peek();
			
			StringTree selfTree=tree.get(name);
			if(selfTree==null) {
				selfTree=new StringTree();
				selfTree.parent=parent;
				selfTree.name=name;
				tree.put(name, selfTree);
			} else
				selfTree.parent=parent;
			
			if(parent==null) {
				treeRoot.childs.add(name);
				return time;
			}
			
			StringTree parentTree=tree.get(parent);
			if(parentTree==null) {
				parentTree=new StringTree();
				parentTree.name=parent;
				tree.put(parent, parentTree);
			}
			parentTree.childs.add(name);
		} else {
			catcher.put(time);
		}
		
		return time;
	}
	
	/**Start new moment and ends previous one if started.
	 * @param name Name of moment to start.
	 */
	public void moment(String name) {
		if(isPreviousMoment) {
			endPeriod0();
			isPreviousMoment=false;
		}
		
		startPeriod(name);
		isPreviousMoment=true;
	}
	
	/**End all periods and moments, update fullPeriod time and start new period.*/
	public void tick() {
		while(!periodNames.empty())
			endPeriod();
		
		long time=System.nanoTime()-lastTick;
		
		if(fullPeriod==null) {
			fullPeriod=masterChatcher.new LongCatcher(time);
		} else {
			fullPeriod.put(time);
		}
		
		lastTick=System.nanoTime();
	}
	
	@Override
	public String toString() {
		return "Counted "+fullPeriod.toString();
	}
	
	/**Get String containing all nested times, not only outer one.*/
	public String toFullString() {
		StringBuilder builder=new StringBuilder();
		builder.append("Time: ").append(fullPeriod).append('\n');
		
		for(String str:treeRoot.childs) {
			toFullString(str, builder, 1);
		}
		
		return builder.toString();
	}
	private void toFullString(String name, StringBuilder builder, int indentationLevel) {
		indent(builder, indentationLevel);
		builder.append(name).append(": \t").append(periods.get(name)).append('\n');
		
		for(String str:tree.get(name).childs)
			toFullString(str, builder, indentationLevel+1);
	}
	private void indent(StringBuilder builder, int level) {
		for(int i=0;i<level;i++)
			builder.append("  ");
	}
	
	/**Call {@link #tick()} and print data to stdout.*/
	public void printoutTick() {
		printTick(System.out);
	}
	/**Call {@link #tick()} and print data to given stream.*/
	public void printTick(PrintStream stream) {
		tick();
		stream.println(toString());
	}
	
	/**Call {@link #tick()} and print full string to stdout.*/
	public void fullPrintoutTick() {
		fullPrintTick(System.out);
	}
	/**Call {@link #tick()} and print full string to given stream.*/
	public void fullPrintTick(PrintStream stream) {
		tick();
		stream.println(toFullString());
	}
	
	static class StringTree{
		String name;
		String parent;
		ArrayList<String> childs=new ArrayList<>();
	}
}