/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.types.Video;
import com.GUI.Table;
import com.GUI.Table_Model;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author dave
 */
public class list extends JInternalFrame {

    private JScrollPane scrollpane;
    private Table table;
    private String[] cols = {"videos"};
    private String[] titles = {"mask video", "debug video"};
    private String[][] data;

    public list(int option, ArrayList<Video> vid) {
        super("videos", true, true, true, true);
        super.setTitle(titles[option]);
        super.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

        data = new String[vid.size()][1];
        for (int i = 0; i < vid.size(); i++) {
            data[i][0] = vid.get(i).getDescription();
        }
        table = new Table();
        Table_Model tm = new Table_Model(data, cols);
        table.setModel(tm);
        scrollpane = new JScrollPane(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = ((Table) e.getSource()).rowAtPoint(e.getPoint());
                if (e.getClickCount() == 2) {
                    setVisible(false);
                    if (option == 1) {
                        MainWindow.debug(vid.get(row));
                    } else if(option == 0){
                        MainWindow.mask(vid.get(row), row);
                    }
                }
            }
        });

        super.add(scrollpane);
        super.pack();
    }
}
