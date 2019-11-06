/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.GUI;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class Table_Model extends DefaultTableModel{

    public Table_Model(String[][] rowData, String[] columnName) {
        super(rowData,columnName);
    }
    
    public Table_Model(ArrayList<String[]> rowData, String[] columnName) {
        this(rowData.toArray(new String[rowData.size()][5]),columnName);
    }

    public Table_Model() {
        super();
    }
    public boolean isCellEditable(int row, int column) {
        return false;//This causes all cells to be not editable
    }
}
