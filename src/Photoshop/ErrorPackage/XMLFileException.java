package Photoshop.ErrorPackage;

public class XMLFileException extends Exception {
    public XMLFileException() {
        super("An error has occurred while processing image");
    }
}
