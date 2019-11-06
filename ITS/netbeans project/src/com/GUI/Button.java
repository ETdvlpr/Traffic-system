/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.GUI;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author dave
 */
public class Button extends JButton{

    public Button() {
        super.setFont(theme.font);
    }

    public Button(String add) {
        super(add);
        super.setFont(theme.font);
    }
    
    public Button(Icon icon) {
        super(icon);
        super.setFont(theme.font);
    }
    
}
