/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.GUI;

import javax.swing.JTextField;

/**
 *
 * @author dave
 */
public class TextField extends JTextField {

    public TextField() {
        super.setFont(theme.font);
    }

    public TextField(int i) {
        super(i);
        super.setFont(theme.font);
    }
    
}
