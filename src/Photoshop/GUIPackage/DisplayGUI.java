package Photoshop.GUIPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.SelectionPackage.Selection;
import Photoshop.ImagePackage.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class DisplayGUI  extends JPanel {

    BufferedImage buffImg;

    Vector<Point> startPositions = new Vector<>();
    Vector<Point> endPositions = new Vector<>();
    boolean flag;
    OptionsGUI owner;
    Photoshop.SelectionPackage.Selection activeSelection;

    public void setActiveSelection(Selection activeSelection) {
        this.activeSelection = activeSelection;
        this.startPositions = activeSelection.getStartPositions();
        this.endPositions = activeSelection.getEndPositions();
    }

    public Selection getActiveSelection() {
        return activeSelection;
    }

    public DisplayGUI(OptionsGUI owner){
        setSize(1200,1000);
        setVisible(true);
        this.owner = owner;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(buffImg!=null && flag) {
                    int x = e.getX();
                    int y = e.getY();
                    startPositions.add(new Point(x, y));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(buffImg!=null && flag) {
                    int x = e.getX();
                    int y = e.getY();
                    endPositions.add(new Point(x, y));
                    repaint();
                }
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g.clearRect(0,0,1000,1000);
        g.drawImage(buffImg,0,0,null);
        g.setColor(Color.RED);
        if(!startPositions.isEmpty() && !endPositions.isEmpty()) {
            for(int i = 0; i < endPositions.size(); i++) {
                Point startPoint = startPositions.elementAt(i);
                Point endPoint = endPositions.elementAt(i);
                if(startPoint.getX() > buffImg.getWidth() || startPoint.getY()>buffImg.getHeight()){
                    startPositions.remove(i);
                    endPositions.remove(i);
                    return;
                }
                endPoint.x = endPoint.x>0 ? (int)endPoint.getX() : 0;
                endPoint.y = endPoint.y>0 ? (int)endPoint.getY() : 0;
                endPoint.x = endPoint.getX()<buffImg.getWidth() ? (int)(endPoint.getX()):(buffImg.getWidth()-1);
                endPoint.y = endPoint.getY()<buffImg.getHeight() ? (int)(endPoint.getY()):(buffImg.getHeight()-1);
                startPoint.x = startPoint.x>0 ? (int)startPoint.getX() : 0;
                startPoint.y = startPoint.y>0 ? (int)startPoint.getY() : 0;
                double px = Math.min(startPoint.getX(), endPoint.getX());
                double py = Math.min(startPoint.getY(), endPoint.getY());
                double pw = Math.abs(startPoint.getX() - endPoint.getX());
                double ph = Math.abs(startPoint.getY() - endPoint.getY());
                g.drawRect((int) px, (int) py, (int) pw, (int) ph);
            }
        }
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

    public void resetSelections(){
        startPositions.clear();
        endPositions.clear();
        repaint();
    }

    public void currentState() {
        try {
            Image img = Image.getInstance();
            img.saveImage("Images\\image.bmp", 32);
            buffImg = ImageIO.read(new File("Images\\image.bmp"));
        } catch (FileIOExceptions | IOException | FileTypeException fileIOExceptions) {
            ErrorDialog ed = new ErrorDialog(owner.owner,fileIOExceptions.getMessage());
            ed.setVisible(true);
        }

    }

    public void newProject() {
        Graphics g = getGraphics();
        g.setColor(Color.white);
        g.drawRect(0,0,1200,1000);
        startPositions.clear();
        endPositions.clear();
        activeSelection = null;
    }
}
