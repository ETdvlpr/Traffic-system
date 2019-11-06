/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.types.Video;
import com.GUI.Button;
import com.GUI.Label;
import com.Run.Run;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dave
 */
public class addInt extends JInternalFrame {

    private Button continueButton, cancelButton;
    private Label typeLabel, nameLabel;
    private JSpinner typeSelect;
    private JTextField nameField;

    public addInt(ArrayList<Video> videos) {
        super("add intersection", true, true, true, true);
        super.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        continueButton = new Button("Continue");
        cancelButton = new Button("cancel");
        typeLabel = new Label("Type");
        nameLabel = new Label("Name");
        typeSelect = new JSpinner(new SpinnerNumberModel(4, 2, 4, 1));
        nameField = new JTextField(20);

        handler handle = new handler(videos);
        continueButton.addActionListener(handle);
        cancelButton.addActionListener(handle);

        JPanel center = new JPanel(new GridLayout(2, 2));
        center.add(typeLabel);
        center.add(typeSelect);
        center.add(nameLabel);
        center.add(nameField);

        JPanel south = new JPanel();
        south.add(continueButton);
        south.add(cancelButton);

        super.add(center, BorderLayout.CENTER);
        super.add(south, BorderLayout.SOUTH);
        super.pack();
    }

    private class handler implements ActionListener {

        private final ArrayList<Video> videos;

        public handler(ArrayList<Video> videos) {
            this.videos = videos;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == continueButton) {
                if (nameField.getText() != null && !"".equals(nameField.getText())) {
                    addIntersection newFrame = new addIntersection(videos, nameField.getText(), (int) typeSelect.getValue());
                    MainWindow.desktop.add(newFrame);
                    MainWindow.desktop.getDesktopManager().maximizeFrame(newFrame);
                } else {
                    JOptionPane.showMessageDialog(MainWindow.desktop, "you must add a name for the intersection to be identified by");
                }
            } else {
                setVisible(false);
            }
        }
    }
}
