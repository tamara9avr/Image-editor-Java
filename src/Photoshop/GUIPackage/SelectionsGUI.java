package Photoshop.GUIPackage;

import Photoshop.SelectionPackage.*;
import Photoshop.ImagePackage.Image;
import Photoshop.SelectionPackage.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SelectionsGUI extends JPanel {

    OptionsGUI owner;
    Selection sel;
    Vector<Rectangle> rectangles = new Vector<>();
    Vector<JRadioButton> selButtons = new Vector<>();
    ButtonGroup group = new ButtonGroup();


    JTextField name;
    JButton saveButton, deleteSelectionButton, discardSelectionButton, activateSelectionButton, deactivateSelectionButton,addSelectionButton;
    public SelectionsGUI(OptionsGUI owner){

        this.owner = owner;
        this.setSize(200,800);
        setLayout(new GridLayout(2,1));
        addComponents();

        setVisible(false);
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7,1));

        JPanel selections = new JPanel();

        selections.setLayout(new GridLayout(5,1));

        addSelectionButton = new JButton( "Select");

        panel.add(addSelectionButton);

        saveButton = new JButton("Save");
        name = new JTextField("");
        deleteSelectionButton = new JButton("Delete selection");
        discardSelectionButton = new JButton("Discard selection");
        activateSelectionButton = new JButton("Activate selection");
        deactivateSelectionButton = new JButton("Deactivate selection");

        deleteSelectionButton.setEnabled(false);
        activateSelectionButton.setEnabled(false);
        deactivateSelectionButton.setEnabled(false);


        JPanel actPanel = new JPanel();
        actPanel.setLayout(new GridLayout(1,2));

        panel.add(saveButton);
        panel.add(name);
        panel.add(discardSelectionButton);
        panel.add(deleteSelectionButton);
        actPanel.add(activateSelectionButton);
        actPanel.add(deactivateSelectionButton);
        panel.add(actPanel);
        add(panel);
        add(selections);

        addSelectionButton.addActionListener(e -> {
            owner.currentImage.flag = true;
            owner.currentImage.resetSelections();
            rectangles.clear();
        });

        saveButton.addActionListener(e->{
            owner.currentImage.flag = false;
            for(int i = 0; i<owner.currentImage.getStartPositions().size(); i++){
                Point startPoint = owner.currentImage.getStartPositions().elementAt(i);
                Point endPoint = owner.currentImage.getEndPositions().elementAt(i);
                int x = (int)Math.min(startPoint.getX(), endPoint.getX());
                int y = (int)Math.min(startPoint.getY(), endPoint.getY());
                int width = (int)Math.abs(startPoint.getX() - endPoint.getX());
                int height = (int)Math.abs(startPoint.getY() - endPoint.getY());
                Rectangle rect = new Rectangle(x,y,width,height);
                rectangles.add(rect);
            }
            sel = new Selection(name.getText(),rectangles);
            sel.setStartPositions(owner.currentImage.getStartPositions());
            sel.setEndPositions(owner.currentImage.getEndPositions());
            Image img = Image.getInstance();
            img.addSelection(sel);
            img.activateSelection(name.getText(), true);
            JRadioButton btn = new JRadioButton(name.getText());
            group.add(btn);
            selButtons.add(btn);
            selections.add(btn);
            addRadioButtonsActionListener();
        });

        discardSelectionButton.addActionListener(e->{
            owner.currentImage.flag = false;
            owner.currentImage.resetSelections();

            rectangles.clear();
        });

        deleteSelectionButton.addActionListener( e-> {
            owner.currentImage.flag = false;
            String str = getSelected();
                    Image img = Image.getInstance();
                    img.deleteSelection(str);
                    if(owner.currentImage.activeSelection!=null && owner.currentImage.getActiveSelection().getName().equals(str)){
                        owner.currentImage.resetSelections();
                    }
                    for(JRadioButton it : selButtons) {
                        if (it.getText().equals(str))
                            selButtons.remove(it);
                        break;
                    }

        });

        activateSelectionButton.addActionListener(e-> {
            owner.currentImage.flag = false;

            Image img = Image.getInstance();

            String str = getSelected();
            img.activateSelection(str,true);
            owner.currentImage.resetSelections();
            owner.currentImage.setStartPositions(img.getActiveSelection().getStartPositions());
            owner.currentImage.setEndPositions(img.getActiveSelection().getEndPositions());
            owner.currentImage.repaint();
            owner.currentImage.setActiveSelection(img.getActiveSelection());
        });

        deactivateSelectionButton.addActionListener(e-> {
            owner.currentImage.flag = false;

            Image img = Image.getInstance();

            String str = getSelected();
            img.activateSelection(str,false);
            if(owner.currentImage.activeSelection!=null && owner.currentImage.getActiveSelection().getName().equals(str)) {
                owner.currentImage.resetSelections();
                owner.currentImage.repaint();
            }
        });

    }

    private String getSelected(){
        for(JRadioButton btn : selButtons){
            if(btn.isSelected()){
                return btn.getText();
            }
        }
        return null;
    }

    private void addRadioButtonsActionListener(){

        for(JRadioButton btn : selButtons){
            btn.addActionListener(e->{
                deleteSelectionButton.setEnabled(true);
                activateSelectionButton.setEnabled(true);
                deactivateSelectionButton.setEnabled(true);
            });
        }
    }

    public void newProject() {
        deleteSelectionButton.setEnabled(false);
        activateSelectionButton.setEnabled(false);
        deactivateSelectionButton.setEnabled(false);

        selButtons.clear();
        group = new ButtonGroup();
        rectangles.clear();
    }
}
