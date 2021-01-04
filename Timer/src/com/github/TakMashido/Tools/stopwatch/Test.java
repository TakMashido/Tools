package com.github.TakMashido.Tools.stopwatch;

public class Test {
	public static void main(String[]args) throws InterruptedException {
		
		Stopwatch stopwatch=new Stopwatch();
		
		for(int i=0;i<1000;i++) {
			stopwatch.startPeriod("a");
				stopwatch.startPeriod("aa");
					stopwatch.moment("aaa");
					stopwatch.moment("aab");
					Thread.sleep(10);
					stopwatch.moment("aac");
				stopwatch.endPeriod();
				stopwatch.startPeriod("ab");
					stopwatch.startPeriod("aba");
					stopwatch.endPeriod();
				stopwatch.endPeriod();
				Thread.sleep(2);
			stopwatch.endPeriod();
			stopwatch.tick();
		}
		
		
		System.out.println(stopwatch.toFullString());
	}
}
