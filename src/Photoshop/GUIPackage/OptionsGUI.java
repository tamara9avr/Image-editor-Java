package Photoshop.GUIPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.ImagePackage.Image;

import javax.swing.*;
import java.awt.*;

public class OptionsGUI extends JPanel {
      LayerGUI lg;
      SelectionsGUI sg;
      OperationsGUI og;
      MainGUI owner;
    public DisplayGUI currentImage;

    public OptionsGUI(MainGUI owner){
        this.owner = owner;
        currentImage = new DisplayGUI(this);
        setSize(new Dimension(1200,800));
        lg = new LayerGUI(this);
        sg = new SelectionsGUI(this);
        og = new OperationsGUI(this);
        setLayout(new BorderLayout());
        addComponents();
        setVisible(false);
    }

    private void addComponents() {


        ButtonGroup group = new ButtonGroup();
        JRadioButton menu = new JRadioButton();
        menu.setLayout(new GridLayout(1,4));

        JRadioButtonMenuItem layer = new JRadioButtonMenuItem("Layers", true);
        JRadioButtonMenuItem selection = new JRadioButtonMenuItem("Selections", false);
        JRadioButtonMenuItem operation = new JRadioButtonMenuItem("Operations", false);
        JButton reset = new JButton("Reset");

        group.add(layer);
        group.add(selection);
        group.add(operation);

        menu.add(layer);
        menu.add(selection);
        menu.add(operation);
        menu.add(reset);

        add(menu, BorderLayout.NORTH);

        Panel options = new Panel();
        options.setLayout(new CardLayout());

        options.add(lg);
        options.add(sg);
        options.add(og);

        add(options, BorderLayout.EAST);
        add(currentImage,BorderLayout.CENTER);

        reset.addActionListener(e->{
            Image img = Image.getInstance();
            try {
                img.reset();
                currentImage.currentState();
                currentImage.repaint();
            } catch (FileTypeException | FileIOExceptions fileTypeException) {
                ErrorDialog ed = new ErrorDialog(owner,fileTypeException.getMessage());
                ed.setVisible(true);
            }

        });


            layer.addActionListener(e->{
                og.setVisible(false);
                sg.setVisible(false);
                lg.setVisible(true);
            });
            selection.addActionListener(e->{
              og.setVisible(false);
              sg.setVisible(true);
              lg.setVisible(false);
            });

           operation.addActionListener(e->{
                og.setVisible(true);
                sg.setVisible(false);
                lg.setVisible(false);
            });

    }

    public void newProject() {
        Image img = Image.getInstance();
        if(!img.isEmpty()){
            img.newProject();
        }
        lg.newProject();
        sg.newProject();
        og.newProject();
    }
}
