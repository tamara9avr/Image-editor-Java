package Photoshop.ErrorPackage;

public class FileIOExceptions extends Exception {

	public FileIOExceptions() {
		super("File not found. Check the path you entered, or try different file");
	}

}
