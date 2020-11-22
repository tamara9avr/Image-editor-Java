package Photoshop.GUIPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.ImagePackage.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class LayerGUI extends JPanel {

    Image img;
    OptionsGUI owner;
    Vector<JRadioButton> layers = new Vector<>();
    ButtonGroup group = new ButtonGroup();
    int counter = 0;

    JButton deleteLayerButton, activateLayerButton,deactivateLayerButton, showLayerButton, showAll, showOriginal, changeOpacity;


    JSlider slider;
    JLabel label;
    JTextField opacText;

    public LayerGUI(OptionsGUI owner){
        this.owner = owner;
        this.setSize(200,800);
        setLayout(new GridLayout(15,1));
        img = Image.getInstance();
        addComponents();

        setVisible(false);
    }

    private void addComponents() {

        JButton addLayerButton = new JButton( "Add layer");

        add(addLayerButton);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(1,2));

        deleteLayerButton = new JButton("Delete layer");
        activateLayerButton = new JButton("Activate layer");
        deactivateLayerButton = new JButton("Deactivate layer");
        showLayerButton = new JButton("Show layer");
        showAll = new JButton("Show all");
        showOriginal = new JButton("Show original image");
        changeOpacity = new JButton("Change opacity");

        deleteLayerButton.setEnabled(false);
        activateLayerButton.setEnabled(false);
        deactivateLayerButton.setEnabled(false);
        showLayerButton.setEnabled(false);
        changeOpacity.setEnabled(false);
        showOriginal.setEnabled(false);
        showAll.setEnabled(false);

        add(deleteLayerButton);
        btnPanel.add(activateLayerButton);
        btnPanel.add(deactivateLayerButton);
        add(btnPanel);
        add(showLayerButton);
        add(showAll);
        add(showOriginal);

        JPanel panel33 = new JPanel();
        panel33.setLayout(new FlowLayout());

        slider = new JSlider();
        slider.setValue(100);
        label = new JLabel(String.valueOf(slider.getValue()));
        slider.setMinimum(0);
        slider.setMaximum(100);
        slider.setPreferredSize(new Dimension(150,30));
        opacText = new JTextField("100");
        opacText.setPreferredSize(new Dimension(50,50));
        panel33.add(opacText);
        panel33.add(slider);
        panel33.add(label);
        panel33.add(changeOpacity);

        add(panel33);

        slider.addChangeListener(e->{
            label.setText(String.valueOf(slider.getValue()));
            opacText.setText(label.getText());
        });

        opacText.addActionListener(e->{
            label.setText(opacText.getText());
            slider.setValue(Integer.parseInt(label.getText()));
        });

        changeOpacity.addActionListener(e->{
            int val = Integer.parseInt(label.getText());
            int index = getSelected();
            img.setLayerOpacity(index,val);

            owner.currentImage.currentState();
            owner.currentImage.repaint();
        });

        opacText.addActionListener(e->{
            String op = opacText.getText();
            int val = Integer.parseInt(op);
            label.setText(op);
            slider.setValue(val);
        });

        showOriginal.addActionListener(e->{
            try {
               String path = img.getOriginal();
                owner.currentImage.buffImg = ImageIO.read(new File(path));
                owner.currentImage.repaint();
            } catch (IOException fileTypeException) {
                ErrorDialog ed = new ErrorDialog(owner.owner, fileTypeException.getMessage());
                ed.setVisible(true);
            }
        });

        showAll.addActionListener(e->{
            owner.currentImage.currentState();
            owner.currentImage.repaint();
        });

        addLayerButton.addActionListener(e -> {
            try {

                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(this);

                if(returnVal==JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getPath();
                    img.addLayer(path);
                    owner.currentImage.currentState();
                    owner.currentImage.repaint();
                    JRadioButton button = new JRadioButton("Layer"+counter++);
                    layers.add(button);
                    group.add(button);
                    add(button);
                    addRadioButtonsActionListener();
                    repaint();
                }
            } catch (FileIOExceptions | FileTypeException fileIOExceptions) {
                ErrorDialog ed = new ErrorDialog(owner.owner,fileIOExceptions.getMessage());
                ed.setVisible(true);
            }
        });

        deleteLayerButton.addActionListener( e-> {
                    int num = getSelected();
                    img.deleteLayer(num);

                    owner.currentImage.currentState();
                    owner.currentImage.repaint();
                    layers.remove(num);
        });

        activateLayerButton.addActionListener(e-> {

            int num = getSelected();
            img.activateLayer(num,true);

            owner.currentImage.currentState();
            owner.currentImage.repaint();

        });

        deactivateLayerButton.addActionListener(e-> {

            int num = getSelected();
            img.activateLayer(num,false);

            owner.currentImage.currentState();
            owner.currentImage.repaint();

        });

        showLayerButton.addActionListener(e-> {
            int num = getSelected();
            try{
                img.loadLayer(num);
                owner.currentImage.buffImg = ImageIO.read(new File("Images\\Layer"+num+".bmp"));
                owner.currentImage.repaint();
            } catch (FileIOExceptions | FileTypeException | IOException exc) {
                ErrorDialog ed = new ErrorDialog(owner.owner, exc.getMessage());
                ed.setVisible(true);
            }
        });

        addRadioButtonsActionListener();

    }

    private int getSelected(){
        for(JRadioButton btn : layers){
            if(btn.isSelected()){

                return layers.indexOf(btn);
            }
        }
        return -1;
    }

    private void addRadioButtonsActionListener(){

        for(JRadioButton btn : layers){
            btn.addActionListener(e->{
                    deleteLayerButton.setEnabled(true);
                    activateLayerButton.setEnabled(true);
                    deactivateLayerButton.setEnabled(true);
                    showLayerButton.setEnabled(true);
                    changeOpacity.setEnabled(true);
                    showOriginal.setEnabled(true);
                    showAll.setEnabled(true);
                    int opc = img.getLayerOpacity(layers.indexOf(btn));
                    boolean act = img.getIsActiveLayer(layers.indexOf(btn));
                    slider.setValue(opc);
                    label.setText(String.valueOf(opc));
                    opacText.setText(String.valueOf(opc));
            });
        }
    }

    public void newProject() {
        deleteLayerButton.setEnabled(false);
        activateLayerButton.setEnabled(false);
        deactivateLayerButton.setEnabled(false);
        showLayerButton.setEnabled(false);
        changeOpacity.setEnabled(false);
        showOriginal.setEnabled(false);
        showAll.setEnabled(false);

        group = new ButtonGroup();
        layers.clear();
        counter = 0;

        repaint();
    }
}
