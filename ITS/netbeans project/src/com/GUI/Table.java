/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.GUI;

import javax.swing.JTable;

/**
 *
 * @author dave
 */
public class Table extends JTable{

    public Table() {
        
        super.setFont(theme.font);
        super.setRowHeight(20);
    }
    
}
