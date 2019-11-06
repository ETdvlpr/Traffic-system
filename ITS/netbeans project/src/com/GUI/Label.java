/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.GUI;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author dave
 */
public class Label extends JLabel{

    public Label() {
        super.setFont(theme.font);
    }
    public Label(String text){
        super(text);
        super.setFont(theme.font);
    }
    public Label(Icon icon) {
        super(icon);
        super.setFont(theme.font);
    }
}
