Some timers to ease time meansurments to ease development optimazation.
Use during developing time sensitive parts, for existing code base profiler should be way better.

Usage example:

StopWatch sw=new StopWatch();

sw.start()
while(true){
	sw.moment("a");
	a();					//Time consuming method

	sw.period("bar");		//No need to end moment, ends automaticly
	if(bar()){
		sw.moment("foo");
		foo();
		sw.moment("next");
		next();
	}
	sw.endperiod();			//Pop periods stack

	sw.printTick();			//Call tick(end all periods, including root one) an println whole loop time to stdout
}

sw.fullPrintoutTick();		//Println whole tree and it's times to stdout.

all times are printed in form "avarage:min:max ms" with name of period preceding
all names should be unique, otherwise given period/moment could be started in few places 