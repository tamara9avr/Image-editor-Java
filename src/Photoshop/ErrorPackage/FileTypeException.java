package Photoshop.ErrorPackage;

public class FileTypeException extends Exception {

	public FileTypeException() {
		super("Error. This is not valid file or its format is not currently supported. Try different file");
	}

}
