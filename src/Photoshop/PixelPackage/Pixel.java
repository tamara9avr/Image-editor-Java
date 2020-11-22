package Photoshop.PixelPackage;

public class Pixel {
	
	int red, green, blue;
	double alpha = 1;															//Default opacity is 100% - image is fully visible

	public Pixel(int red, int green, int blue, double alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = (alpha/255);											//Value of alpha is used in percentage
	}
	
	public Pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	
	

}
