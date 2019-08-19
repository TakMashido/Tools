package com.github.TakMashido.Tools.cssTools;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.scene.Parent;
import javafx.scene.control.Labeled;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

/**Set of tools to help creating and debuging javafx css.
 * @author TakMashido
*/
public class CssTools {
	/**Prints tree of style classes and pseudoclasses of given javafx gui element(Node or MenuItem) and child elements of it.
	 * @param styleable Element to examine.
	 */
	public static void inspectCssTree(Styleable styleable) {
		inspectCssTree(styleable, 0);
	}
	/**Prints tree of style classes and pseudoclasses of given javafx gui element(Node or MenuItem) and childen elements of it.
	 * @param styleable Element to examine.
	 * @param level Number of /t inserted before {@code styleable} elements classes.
	 */
	private static void inspectCssTree(Styleable styleable, int level) {
		printClasses(styleable,level);
		
		boolean haveChilds=inspectChilds(styleable, level+1);
		
		if(haveChilds)for(int i=0;i<level;i++)System.out.print("\t");
		System.out.println("}");
	}
	/**Invoces {@link #inspectChilds(Styleable, int)} on each child of given element.
	 * @param styleable Element to examine
	 * @param level Amount of /t before each elements classes.
	 * @return If element have any children.
	 */
	private static boolean inspectChilds(Styleable styleable, int level) {
		List<Styleable> childs=new ArrayList<Styleable>();
		
		if(styleable instanceof Labeled) { 
			Styleable graphic=((Labeled) styleable).getGraphic();
			if(graphic!=null)childs.add(graphic);
		}
		if(styleable instanceof MenuItem) {
			Styleable graphic=((MenuItem) styleable).getGraphic();
			if(graphic!=null)childs.add(graphic);
		}
		if(styleable instanceof Parent) childs.addAll(((Parent) styleable).getChildrenUnmodifiable());
		if(styleable instanceof Menu) childs.addAll(((Menu) styleable).getItems());
		if(styleable instanceof MenuButton) childs.addAll(((MenuButton) styleable).getItems());
		if(styleable instanceof MenuBar) childs.addAll(((MenuBar) styleable).getMenus());
		
		System.out.print("{");
		if(childs.size()>0)System.out.println();
		for(Styleable child:childs) {
			inspectCssTree(child,level);
		}
		
		return childs.size()!=0;
	}
	
	/**Prints all classes and pseudoclasses of given element with given amount of alignment.
	 * @param styleable Element to examine.
	 * @param level Amount of /t befor classes.
	 */
	private static void printClasses(Styleable styleable, int level) {
		List<String> styles=styleable.getStyleClass();
		Set<PseudoClass> pseudoClasses=styleable.getPseudoClassStates();
		boolean println=false;
		for(String style:styles) {
			if(println) System.out.println(',');
			else println=true;
			for(int i=0;i<level;i++)System.out.print("\t");
			System.out.print(style);
		}
		for(PseudoClass ps:pseudoClasses) {
			if(println) System.out.println(',');
			else println=true;
			for(int i=0;i<level;i++)System.out.print("\t");
			System.out.print(":"+ps.getPseudoClassName());
		}
		if(styles.size()+pseudoClasses.size()==0) {
			for(int i=0;i<level;i++)System.out.print("\t");
			System.out.print("NONE");
		}
	}
	
	
	/**Apply css to given parent and refresh it every time orginal file changed.
	 * @param target Parent to apply css.
	 * @param css Css to apply
	 * @return Thread responsible for refreshing.
	 */
	public static Thread addCssRefresher(Parent target, File css) {
		Thread cssRefreasher=new Thread() {
			public void run() {
				long lastModified=css.lastModified();
				String cssPath=null;
				try {
					cssPath = css.toURI().toURL().toExternalForm();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int index=target.getStylesheets().size();
				target.getStylesheets().add(cssPath);
				
				while(true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						break;
					}
					
					long newLastModified=css.lastModified();
					if(newLastModified!=lastModified) {
						lastModified=newLastModified;
						target.getStylesheets().set(index,cssPath);
					}
				}
			}
		};
		cssRefreasher.setDaemon(true);
		cssRefreasher.start();
		return cssRefreasher;
	}
}