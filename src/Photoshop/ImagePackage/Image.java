package Photoshop.ImagePackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.ErrorPackage.XMLFileException;
import Photoshop.FormaterPackage.BMP;
import Photoshop.FormaterPackage.Formater;
import Photoshop.FormaterPackage.XML;
import Photoshop.LayerPackage.LayerVector;
import Photoshop.PixelPackage.Pixel;
import Photoshop.SelectionPackage.Selection;

import java.io.IOException;
import java.util.Vector;

public class Image {

	Formater myFormater = null;
	public static Image instance = null;
	LayerVector layers = new LayerVector();
	private boolean empty = true;
	private String original;
	Vector<Selection> selections = new Vector<>();
	Selection activeSelection = null;
	String operationsPath;

	public String getOriginal() {
		return original;
	}

	public void setOperationsPath(String operationsPath) {
		this.operationsPath = operationsPath;
	}

	public boolean isEmpty() {
		return empty;
	}

	private Image() {}

	public static Image getInstance() {
		if(instance==null) instance =  new Image();
		return instance;
	}

	public void newProject(){
		empty = true;
		layers.clear();
		original = null;
		selections.clear();
		operationsPath = null;
	}


	public void addLayer(String fname) throws FileIOExceptions, FileTypeException {
		if(original==null) original = fname;
		myFormater = new BMP();
		String newName = myFormater.getPath(fname);
		Vector<Pixel> pix = myFormater.open(newName);
		if(layers.getLayers().size()==0) original = fname;
		layers.add(pix, myFormater.getWidth(), myFormater.getHeight());
		empty = false;
	}

	public void addLayer() {
		layers.add();
		empty = false;
	}
	
	public void activateLayer(int index, boolean flag) {
		layers.activateLayer(index, flag);
	}
	
	public void setLayerOpacity(int index, int op) {
		layers.setOpacity(index, op);
	}

	public Vector<Selection> getSelections() {
		return selections;
	}

	public int getLayerOpacity(int index){
		return layers.getLayerOpacity(index);
	}

	public boolean getIsActiveLayer(int index){
		return layers.getIsActiveLayer(index);
	}
	
	public void deleteLayer(int index) {
		layers.deleteLayer(index);
	}
	
	public void reset() throws FileTypeException, FileIOExceptions {
		layers.reset();
		addLayer(original);
	}

	public void addSelection(Selection s){
		selections.add(s);
	}

	public void deleteSelection(String s){
		if(activeSelection.getName().equals(s)) activeSelection = null;
		for(Selection sel : selections){
			if(sel.getName().equals(s)){
				selections.remove(sel);
				return;
			}
		}
	}

	public Selection getActiveSelection() {
		return activeSelection;
	}

	public void activateSelection(String name, boolean flag){
		if(activeSelection!= null && activeSelection.getName().equals(name)){
			if(flag) return;
			else activeSelection = null;
		}
		for(Selection s : selections){
			s.setActive(false);
			if(s.getName().equals(name)){
				s.setActive(flag);
				if(flag) {
					activeSelection = s;
					activeSelection.setStartPositions(new Vector<>(s.getStartPositions()));
					activeSelection.setEndPositions(new Vector<>(s.getEndPositions()));
				}
				return;
			}
		}
	}

	public void loadLayer(int index) throws FileTypeException, FileIOExceptions {
		layers.getLayers().get(index).save();
	}

	public void saveImage(String fname, int bpp) throws FileIOExceptions, FileTypeException {
		myFormater = new BMP(layers.getWidth(), layers.getHeight(),layers.calculateImage());
		myFormater.save(myFormater.getPath(fname), bpp);
	}

	public void saveToXML() throws FileIOExceptions, XMLFileException {
		Vector<String> lys = layers.prepareForXML();
		String sels ="";
		if(activeSelection!=null) {
			sels = activeSelection.saveAs();
		}
		String ops=operationsPath;
		XML fileForCPP = new XML(lys, sels, ops);
		fileForCPP.write("XML\\project.xml");
	}

	public void callOperation() throws FileIOExceptions, FileTypeException, XMLFileException {
		saveToXML();

		String file = "Project1\\Debug\\Project1.exe" +" XML\\project.xml " + 1;				//Third argument is used as command;

		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(file);
			process.waitFor();

			layers.loadLayers();

		} catch (IOException | InterruptedException | FileIOExceptions e) {
			throw new FileIOExceptions();
		}
	}

	public void load() throws FileTypeException, FileIOExceptions {
		layers.loadLayers();
	}

}


