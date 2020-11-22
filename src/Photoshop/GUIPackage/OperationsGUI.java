package Photoshop.GUIPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ImagePackage.Image;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.ErrorPackage.XMLFileException;

public class OperationsGUI extends JPanel {

    JButton comp = new JButton("Create");
    JLabel lbl = new JLabel("Create new composite operation");
    JLabel lbl1 = new JLabel("Composite operation is empty");

    private final int n = 14;
    OptionsGUI owner;
    Vector<String> names = new Vector<>();
    Vector<Integer> values = new Vector<>();
    JTextField name = new JTextField("Enter name here");

    OperationsGUI(OptionsGUI owner){
        this.owner = owner;
        addComponents();
        setLayout(new GridLayout(3,1));
    }

    public void addComponents(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1));
        name.setEnabled(false);
        panel.add(lbl);
        panel.add(comp);
        panel.add(name);
        JPanel panel0 = new JPanel();
        panel0.setLayout(new GridLayout(10,1));
        panel0.add(lbl1);
        this.add(panel0);
        this.add(panel);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(n+1,2));
        JCheckBoxMenuItem[] array = new JCheckBoxMenuItem[n];
        array[0] = new JCheckBoxMenuItem("Add");
        array[1] = new JCheckBoxMenuItem("Substitute");
        array[2] = new JCheckBoxMenuItem("Multiply");
        array[3] = new JCheckBoxMenuItem("Divide");
        array[4] = new JCheckBoxMenuItem("InverseDivide");
        array[5] = new JCheckBoxMenuItem("InverseSubstitute");
        array[6] = new JCheckBoxMenuItem("Inverse");
        array[7] = new JCheckBoxMenuItem("BlackAndWhite");
        array[8] = new JCheckBoxMenuItem("Gray");
        array[9] = new JCheckBoxMenuItem("Power");
        array[10] = new JCheckBoxMenuItem("Minimum");
        array[11] = new JCheckBoxMenuItem("Maximum");
        array[12] = new JCheckBoxMenuItem("Logarithm");
        array[13] = new JCheckBoxMenuItem("Composite");
        array[13].setEnabled(false);

        JTextField[] vs = new JTextField[n];

        JButton done = new JButton("Done");


        for(int i = 0; i<n; i++){
            vs[i] = new JTextField("10");
           panel2.add(array[i]);
           panel2.add(vs[i]);
        }


        panel2.add(done);
        this.add(panel2);


        comp.addActionListener(e->{
            name.setEnabled(true);
            lbl1.setText("Current composite operation is:");
            for(int i = 0; i<n; i++){
                if(array[i].isSelected()){
                    names.add(array[i].getText());
                    values.add(Integer.valueOf(vs[i].getText()));
                   JLabel lblpom = new JLabel(array[i].getText()+":"+ vs[i].getText()+System.lineSeparator());
                   panel0.add(lblpom);
                }
            }
            array[13].setEnabled(true);
        });

        done.addActionListener(e->{
            boolean flag = false;
            String ops="";
            int val=10;
            for(int i = 0 ; i<n; i++){
                if(array[i].isSelected() && !flag){
                    flag = true;
                    ops = array[i].getText();
                    val = Integer.parseInt(vs[i].getText());
                }
              //  if(!flag) throw new OperationsException();
            }
            if(flag) {
                try {
                    saveAndCalculate(ops, val);
                    owner.currentImage.currentState();
                    owner.currentImage.repaint();
                } catch (FileIOExceptions | FileTypeException | XMLFileException fileIOExceptions) {
                    ErrorDialog ed = new ErrorDialog(owner.owner,fileIOExceptions.getMessage());
                    ed.setVisible(true);
                }
            }
        });

    }

   private void saveAndCalculate(String ops, int val) throws FileIOExceptions, FileTypeException, XMLFileException {
        if(ops.equals("Composite")){
            saveComposite(name.getText());
        }
        else{
            saveOthers(ops,val);
        }
        Image im = Image.getInstance();
       im.callOperation();
    }

    private void saveOthers(String ops, int val) throws FileIOExceptions {
        String fname = "Functions\\Operation.fun";
        Image im = Image.getInstance();
        im.setOperationsPath(fname);
        try {
            BufferedWriter op = new BufferedWriter(new FileWriter(fname));
            op.write(ops+" ");
            op.write(String.valueOf(val));
            op.close();
        } catch (IOException e) {
            throw new FileIOExceptions();
        }
    }

    private void saveComposite(String name) throws FileIOExceptions {
        String fname = "Functions\\" + name + ".fun";
        Image im = Image.getInstance();
        im.setOperationsPath(fname);
        try {
            BufferedWriter op = new BufferedWriter(new FileWriter(fname));
            for(int i =0; i<names.size();i++){
                op.write(names.get(i)+" "+values.get(i)+"\n");
            }
            op.close();
        } catch (IOException e) {
            throw new FileIOExceptions();
        }
    }

    public void newProject() {
        names.clear();
        values.clear();
        lbl1.setText("Composite operation is empty");
    }
}
