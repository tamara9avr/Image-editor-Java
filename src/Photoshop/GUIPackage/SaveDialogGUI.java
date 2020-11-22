package Photoshop.GUIPackage;

import javax.swing.*;
import java.awt.*;

public class SaveDialogGUI extends Dialog {

    private boolean save = false;
    String path;
    int bpp = 32;

    public SaveDialogGUI(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        setBounds(100,100,400,300);
        setLayout(new GridLayout(6,1));
        addComponents();
    }

    public String getPath() {
        return path;
    }

    public int getBpp(){return bpp;}

    private void addComponents() {
        JButton yes = new JButton("Yes");
        JButton back = new JButton("Back");
        JLabel label = new JLabel("Save as:");
        JLabel text = new JLabel(path);
        JRadioButtonMenuItem tf = new JRadioButtonMenuItem("24");
        JRadioButtonMenuItem tt = new JRadioButtonMenuItem("32");
        JLabel bits = new JLabel("Bits per pixel:");
        ButtonGroup gr = new ButtonGroup();
        gr.add(tf);
        gr.add(tt);
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(1,3));
        p2.add(bits);
        p2.add(tf);
        p2.add(tt);
        JButton browse = new JButton("Browse...");
        add(label);
        add(text);
        add(browse);
        add(p2);
        add(yes);
        add(back);

        browse.addActionListener(e->{
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    path = chooser.getSelectedFile().getPath();
                    text.setText(path);
                }
        });



        yes.addActionListener(a -> {
            save = true;
            setVisible(false);
        });

        back.addActionListener(a -> setVisible(false));

        tf.addActionListener(e->{
            if(tf.isSelected()){
                bpp =  Integer.parseInt(tf.getText());
            }
        });

        tt.addActionListener(e->{
            if(tt.isSelected()){
                bpp = Integer.parseInt(tt.getText());
            }
        });
    }

    public boolean isSave() {
        return save;
    }
}
