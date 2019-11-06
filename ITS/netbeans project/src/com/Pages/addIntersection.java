/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.theme;
import com.types.Video;
import com.GUI.Button;
import com.GUI.Label;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

/**
 *
 * @author dave
 */
public class addIntersection extends JInternalFrame {

    private Label[] labels;
    private JComboBox<String>[] options;
    private JPanel panel;
    private SpringLayout layout;
    private Button addButton, cancelButton;
    private final handler handle;
    private String name;
    private int type;

    public addIntersection(ArrayList<Video> videos, String name, int type) {
        super("add intersection : " + name, true, true, true, true);
        super.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
        super.setVisible(true);

        this.type = type;
        this.name = name;

        labels = new Label[4];
        options = new JComboBox[type];

        labels[0] = new Label("North Video");
        labels[1] = new Label("East Video");
        labels[2] = new Label("South Video");
        labels[3] = new Label("West Video");

        for (int j = 0; j < type; j++) {
            options[j] = new JComboBox<>();
            for (int i = 0; i < videos.size(); i++) {
                options[j].addItem(videos.get(i).getDescription());
            }
        }

        layout = new SpringLayout();
        panel = new JPanel(layout);

        for (int i = 0; i < type; i++) {
            //adding components to jframe
            panel.add(labels[i]);
            layout.putConstraint(SpringLayout.WEST, labels[i], 20, SpringLayout.WEST, panel);
            if (i == 0) {
                layout.putConstraint(SpringLayout.NORTH, labels[i], 34, SpringLayout.NORTH, panel);
            } else {
                layout.putConstraint(SpringLayout.NORTH, labels[i], 34, SpringLayout.NORTH, labels[i - 1]);
            }
            panel.add(options[i]);
            layout.putConstraint(SpringLayout.WEST, options[i], theme.font.getSize() * 10, SpringLayout.WEST, labels[i]);
            if (i == 0) {
                layout.putConstraint(SpringLayout.NORTH, options[i], 34, SpringLayout.NORTH, panel);
            } else {
                layout.putConstraint(SpringLayout.NORTH, options[i], 30, SpringLayout.NORTH, labels[i - 1]);
            }
        }
        addButton = new Button("Add");
        cancelButton = new Button("Cancel");
        handle = new handler();
        addButton.addActionListener(handle);
        cancelButton.addActionListener(handle);

        JPanel p = new JPanel();
        p.add(addButton);
        p.add(cancelButton);

        JScrollPane sp = new JScrollPane(panel);
        sp.setSize(350, 280);

        super.add(sp, BorderLayout.CENTER);
        super.add(p, BorderLayout.SOUTH);
        super.pack();
    }

    private class handler implements ActionListener {

        public handler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addButton) {
                int i = MainWindow.data.getInt("number of intersections", 0);
                MainWindow.data.putInt(String.format("north%02d", i), options[0].getSelectedIndex());
                MainWindow.data.putInt(String.format("east%02d", i), options[1].getSelectedIndex());
                if (type > 2) {
                    MainWindow.data.putInt(String.format("south%02d", i), options[2].getSelectedIndex());
                }
                if (type > 3) {
                    MainWindow.data.putInt(String.format("west%02d", i), options[3].getSelectedIndex());
                }
                MainWindow.data.put(String.format("name%02d", i), name);
                MainWindow.data.putInt(String.format("type%02d", i), type);
                MainWindow.data.putInt("number of intersections", i + 1);

                JOptionPane.showMessageDialog(null, "intersection added");
                MainWindow.restart();
                dispose();
            } else {
                dispose();
            }
        }
    }
}
