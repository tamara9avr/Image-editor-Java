package Photoshop.LayerPackage;

import java.util.Vector;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.FormaterPackage.BMP;
import Photoshop.FormaterPackage.Formater;
import Photoshop.PixelPackage.Pixel;

public class Layer {

	static int num = 0;
	int id ;
	int width, height;
	int opacity = 100;
	Vector<Pixel> pixels = new Vector<>();
	boolean active = true;


	public Layer(Vector<Pixel> pixels, int width, int height) {				//Reading new layer from file
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		active = true;
		id = num++;
	}

	public Layer(int width, int height) {										//Adding empty layer with preferred dimensions
		this.width = width;
		this.height = height;
		Pixel p = new Pixel(255,255,255,0);
		for(int i = 0; i<width*height; i++) {
			pixels.add(p);
		}
	}

	public Layer(int width, int height, int color, int alpha) {
		this.width = width;
		this.height = height;
		Pixel p = new Pixel(color,color,color,alpha);
		for(int i = 0; i<width*height; i++) {
			pixels.add(p);
		}
	}

	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}


	public int getOpacity() {
		return opacity;
	}


	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}


	public Vector<Pixel> getPixels() {
		return pixels;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void resize(int width, int height) {                                //Adding transparent pixels to fill to preferred dimensions
		Pixel pom = new Pixel(255, 255, 255, 0);
		int dif = width - this.width;
		if (dif > 0) {
			int i = 0, k = 0;
			int size = this.width * this.height;
			do {
				i++;
				int pos = i * this.width + k;
				for (int j = 0; j < dif; j++) {
					if (pos <= size)
						pixels.add(pos, pom);
					else {
						pixels.add(pom);
					}
					++size;
				}
				k += dif;

			} while (i != this.height);
			this.width = width;
		}

		dif = height - this.height;
		if (dif > 0) {
			for (int j = 0; j < dif * this.width; j++) {
				pixels.add(pom);
			}
			this.height = height;
		}

	}

    public String save() throws FileIOExceptions {
		String fname = "Images\\Layer" + id + ".bmp";
		Formater formater = new BMP(width,height,pixels);
		formater.save(fname,32);
		return fname;
    }

	public void load() throws FileTypeException, FileIOExceptions {							//Loading new pixles that have been changed in cpp program
		String fname = "Images\\Layer" + id + ".bmp";
		Formater formater = new BMP();
		pixels = formater.open(fname);
	}
}
