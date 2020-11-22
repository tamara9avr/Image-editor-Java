package Photoshop.GUIPackage;

import javax.swing.*;
import java.awt.*;

public class CloseDialog extends Dialog {

    boolean flag;

    public CloseDialog(JFrame owner) {
        super(owner, true);
        setBounds(100,100,400,300);
        setVisible(false);
        setLayout(new GridLayout(3,1));
        addComponents();
    }

    private void addComponents() {
        JLabel lbl = new JLabel("Project is not saved.\nAre you sure you want to exit Photoshop?");
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        add(lbl);
        add(yes);
        add(no);

        yes.addActionListener(e-> {
            flag = true;
            setVisible(false);
        });

        no.addActionListener(e->{
            flag=false;
            setVisible(false);
        });
    }
}
