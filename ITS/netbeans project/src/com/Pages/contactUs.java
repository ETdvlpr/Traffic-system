/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.Label;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 *
 * @author dave
 */
public class contactUs extends JInternalFrame {

    public contactUs() {
        super("contact information", true, true, true, true);
        super.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        super.setLayout(new GridLayout(3, 1));
        super.add(two(new Label("Dawit Samuel       :  "), new Label("acedawitt@gmail.com")));
        super.add(two(new Label("Biruk Tesfaw        :  "), new Label("biruk3870@gmail.com")));
        super.add(two(new Label("Belayneh Melka     :  "), new Label("bemeodar@gmail.com")));
        super.pack();
    }

    private JPanel two(Component a, Component b) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(a);
        p.add(b);
        return p;
    }
}
