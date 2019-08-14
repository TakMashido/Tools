Basic liblary for creating, editing plots.

Usage example:

short[] data=new short[]{1,2,3,4,5};

JPanel plotPanel=new PlotPanel();

Plot plot=new ShortArrayPlot(data);
plot.color=Color.RED;
plot.setName("foo");

plotPanel.add(plot);

And add plotPanel into wanted position in your app JFrame.

Or just:
Plot anotherPlot=...;

PlotPanel.showWindow("title",new ShortArrayPlot(data), anotherPlot);

To create new window containing given plots.