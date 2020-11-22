package Photoshop.ErrorPackage;

public class OperationsError extends Exception {
    public OperationsError() {
        super("Please choose only one operation to execute.\n If you wolud like to combine more operations, please create composite operation.\n");
    }
}
