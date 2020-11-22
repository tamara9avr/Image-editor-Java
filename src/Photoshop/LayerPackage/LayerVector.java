package Photoshop.LayerPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.PixelPackage.Pixel;

import java.util.Vector;

public class LayerVector {

	Vector<Layer> layers;
	int width, height;


	public LayerVector() {
		layers = new Vector<>();

	}

	public void add(Vector<Pixel> pix, int width, int height) {

		layers.add(new Layer(pix,width,height));
		resize(width, height);
	}

	public void add() {
		layers.add(new Layer(width, height));
	}

	public void resize(int width, int height) {
		if(width!=this.width || height != this.height) {
			this.width = Math.max(width, this.width);
			this.height = Math.max(height, this.height);
			for(Layer l : layers) {
				l.resize(this.width, this.height);
			}
				layers.lastElement().resize(this.width, this.height);
		}
	}

	public Vector<Layer> getLayers() {
		return layers;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		resize(this.width, this.height);
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		resize(this.width, this.height);
	}

	public Vector<Pixel> calculateImage(){
		Layer pom = new Layer(width, height,255,0);
		Vector<Pixel> pix = pom.getPixels();

		for(Layer l : layers) {
			if(l.isActive() && l.getOpacity()>0) {
				int i = 0;
				for(Pixel p : l.getPixels()) {

					double A0 = pix.get(i).getAlpha() * pom.getOpacity()/100;
					double A1 = p.getAlpha()*l.getOpacity()/100;
					double As = A0 + (1 - A0) * A1;
					Pixel hlp;
					if(As>0) {
						int R = (int) ((pix.get(i).getRed() * A0/As) + p.getRed()*(1 - A0)*(A1/As));
						int G =(int) ((pix.get(i).getGreen() * A0/As) + p.getGreen()*(1 - A0)*(A1/As));
						int B = (int) ((pix.get(i).getBlue() * A0/As) + p.getBlue()*(1 - A0)*(A1/As));

						double A = As*255;

						hlp = new Pixel(R,G,B,A);

						//pom.setOpacity(l.getOpacity());
					}
					else{
						hlp = new Pixel(255,255,255,0);
					}
					pix.set(i++, hlp);
				}
			}
		}
		return pix;
	}

	public int getLayerOpacity(int index){
		return layers.get(index).getOpacity();
	}

	public boolean getIsActiveLayer(int index){
		return layers.get(index).isActive();
	}

	public void activateLayer(int index, boolean flag) {
		layers.get(index).setActive(flag);
	}

	public void setOpacity(int index, int op) {
		layers.get(index).setOpacity(op);
	}


	public void deleteLayer(int index) {
		layers.remove(index);
	}

	public void reset() {
		layers.clear();
	}

	public void loadLayers() throws FileTypeException, FileIOExceptions {
		for(Layer l : layers){
			if(l.isActive()){
				l.load();
			}
		}
	}


	public Vector<String> prepareForXML() throws FileIOExceptions {
		Vector<String> ret = new Vector<>();
		for(Layer l : layers){
			if(l.isActive()) {
				String path = l.save();
				ret.add(path);
			}
		}
		return  ret;
	}

    public void clear() {
		layers.clear();
    }
}
