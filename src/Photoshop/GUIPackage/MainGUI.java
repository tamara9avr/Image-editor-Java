package Photoshop.GUIPackage;

import Photoshop.ErrorPackage.FileIOExceptions;
import Photoshop.ErrorPackage.FileTypeException;
import Photoshop.ImagePackage.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainGUI extends JFrame {

    OptionsGUI opt;
    boolean savedFlag = false;
    CloseDialog cd = new CloseDialog(this);

    public MainGUI() {
        super("Photoshop");
        addComponents();
        setVisible(true);
        setLayout(new BorderLayout());
        setBounds(100,0,1230,1000);

       addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void addComponents() {


        opt = new OptionsGUI(this);
        add(opt,BorderLayout.NORTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenuItem newProject = new JMenuItem("New project");
        JMenuItem saveAs = new JMenuItem("Save as");
        menu1.add(newProject);
        menu1.add(saveAs);
        menuBar.add(menu1);
        this.setJMenuBar(menuBar);

        newProject.addActionListener(e -> {
            opt.newProject();
            opt.setVisible(true);
        });

        saveAs.addActionListener(e -> {
            SaveDialogGUI save = new SaveDialogGUI(this, "Do you want to save project?", true);
            save.setVisible(true);
            if(save.isSave()) {//User can choose to save project or to continue working.
                Image img = Image.getInstance();
                try {

                    img.saveImage(save.getPath(),save.getBpp());                                   //If user chose to save project, it proceeds with saving process.
                    savedFlag = true;

                } catch (FileIOExceptions | FileTypeException fileIOExceptions) {
                    ErrorDialog ed = new ErrorDialog(this,fileIOExceptions.getMessage());
                    ed.setVisible(true);
                }
            }
        });
    }
    public static void main(String[] args){
        new MainGUI();
    }


}
