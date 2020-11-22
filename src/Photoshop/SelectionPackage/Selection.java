package Photoshop.SelectionPackage;

import java.awt.*;
import java.io.*;
import java.util.Vector;

import Photoshop.ErrorPackage.FileIOExceptions;

public class Selection {
	String name;
	Vector<Rectangle> rects;
	boolean active;
	Vector<Point> startPositions = new Vector<>();
	Vector<Point> endPositions = new Vector<>();
	
	public Selection(String name, Vector<Rectangle> rects) {
		super();
		this.name = name;
		this.rects = rects;
	}

	public Vector<Point> getStartPositions() {
		return startPositions;
	}

	public Vector<Point> getEndPositions() {
		return endPositions;
	}

	public void setStartPositions(Vector<Point> startPositions) {
		this.startPositions = new Vector<>();
		for(Point sp : startPositions){
			this.startPositions.add(sp);
		}
	}

	public void setEndPositions(Vector<Point> endPositions) {
		this.endPositions = new Vector<>();
		for(Point ep : endPositions){
			this.endPositions.add(ep);
		}
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Vector<Rectangle> getRects() {
		return rects;
	}


	public void setRects(Vector<Rectangle> rects) {
		this.rects = rects;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	public String saveAs() throws FileIOExceptions {
		String fname = "Functions\\"+name + ".fun";
			try {
				BufferedWriter sel = new BufferedWriter(new FileWriter(fname));
				sel.write(name + "\n");
				for(Rectangle r : rects) {
					sel.write(r.toString());
				}
				sel.close();
				return  fname;
			} catch (IOException e) {
				throw new FileIOExceptions();
			}
	}
}
