package Photoshop.GUIPackage;

import javax.swing.*;
import java.awt.*;

public class ErrorDialog  extends Dialog {

    String info=" ";

    public ErrorDialog(Frame owner, String info) {
        super(owner,"Error",true);
        setBounds(200,200,400,200);
        this.info = info;
        setLayout(new GridLayout(2,1));
        addComponents(info);
    }

    private void addComponents(String info) {
        JLabel information = new JLabel(info);
        JButton btn = new JButton("OK");
        add(information);
        add(btn);
        btn.addActionListener(e->{
            dispose();
        });
    }
}
