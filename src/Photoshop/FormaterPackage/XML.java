package Photoshop.FormaterPackage;

import java.io.File;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Photoshop.ErrorPackage.XMLFileException;
import org.w3c.dom.*;

public class XML  {

    Vector<String> layersPaths;
    String operationsPath;
    String selectionsPath;

   public XML(Vector<String> lys, String sel, String ops){
        layersPaths = lys;
        operationsPath = ops;
        selectionsPath = sel;
    }

    public void write(String fname) throws XMLFileException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("Data");
            doc.appendChild(rootElement);


            String path = System.getProperty("user.dir")+"\\";

            for(String s : layersPaths){
                s = path+s;
                rootElement.appendChild(createDataElement(doc,"Layer",s));
            }

            if(!selectionsPath.equals("")) {
                rootElement.appendChild(createDataElement(doc, "Selection", path+selectionsPath));
            }
            rootElement.appendChild(createDataElement(doc,"CompositeOperation",path+operationsPath));

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult file = new StreamResult(new File(fname));
            transformer.transform(source, file);


        } catch (ParserConfigurationException | TransformerException e) {
            throw new XMLFileException();
        }

    }

    private Node createDataElement(Document doc,String type, String path) {
        Element data = doc.createElement(type);
        data.appendChild(doc.createTextNode(path));
        return data;
    }


}
