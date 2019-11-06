/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.theme;
import com.types.Video;
import com.GUI.Label;
import com.Run.Run;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author dave
 */
public class addCamera extends JInternalFrame {

    private final Label locationLabel, description, xmlLabel;
    private final JTextField locationField, descriptionField, xmlField;
    private final JButton add, close, chooseCascade, chooseVideo;
    private final handler handle;

    public addCamera() {
        super("Add Camera", true, true, true);
        super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        locationLabel = new Label("Location");
        locationField = new JTextField(20);
        locationField.setText("http://192.168.");
        description = new Label("Description");
        descriptionField = new JTextField(20);
        xmlLabel = new Label("xml file");
        xmlField = new JTextField(20);

        add = new JButton("Add");
        close = new JButton("close");
        chooseCascade = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/add file.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        chooseVideo = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/add file.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        handle = new handler();
        add.addActionListener(handle);
        close.addActionListener(handle);
        chooseCascade.addActionListener(handle);
        chooseVideo.addActionListener(handle);

        super.setLayout(new GridLayout(4, 1));
        super.add(three(locationLabel, locationField, chooseVideo));
        super.add(three(description, descriptionField, new Label()));
        super.add(three(xmlLabel, xmlField, chooseCascade));
        super.add(three(add, close, new Label()));
        super.pack();
    }
    private JPanel three(Component a, Component b, Component c){
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(a);
        p.add(b);
        p.add(c);
        return p;
    }

    private class handler implements ActionListener {

        public handler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add) {
                if (check_valid()) {
                    int i = MainWindow.data.getInt("number of videos", 0);
                    MainWindow.data.put(String.format("video%02d", i), locationField.getText());
                    MainWindow.data.put(String.format("description%02d", i), descriptionField.getText());
                    MainWindow.data.put(String.format("xml%02d", i), xmlField.getText());
                    MainWindow.data.put(String.format("mask%02d", i), "0");
                    MainWindow.data.putInt("number of videos", i + 1);

                    MainWindow.videos.add(new Video(locationField.getText(), xmlField.getText(), "0", descriptionField.getText()));
                    JOptionPane.showMessageDialog(null, "video added");
                    MainWindow.restart();
                }
            } else if (e.getSource() == chooseVideo || e.getSource() == chooseCascade) {
                JFileChooser c = new JFileChooser();
                c.setCurrentDirectory(new File(theme.wd));
                if (c.showOpenDialog(Run.win) == JFileChooser.APPROVE_OPTION) {
                    if (e.getSource() == chooseVideo) {
                        locationField.setText(c.getSelectedFile().toPath().toString());
                    } else if (e.getSource() == chooseCascade) {
                        xmlField.setText(c.getSelectedFile().toPath().toString());
                    }
                }
            } else if (e.getSource() == close) {
                clear();
                setVisible(false);
            }
        }

        private boolean check_valid() {
            return true;
        }
    }

    public void clear() {
        locationField.setText("");
        descriptionField.setText("");
        xmlField.setText("");
    }
}
