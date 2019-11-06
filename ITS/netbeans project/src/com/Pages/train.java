/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.theme;
import com.GUI.Button;
import com.GUI.Label;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dave
 */
public class train extends JFrame {

    private Button startTraining, exit;
    private Label labelPositives, labelWidth, labelHeight, labelTraining, labelStages, labelFeature;
    private JSpinner selectWidth, selectHeight, selectStages;
    private JComboBox<String> selectFeature;
    private final handler handle;

    public train() {
        super.setDefaultCloseOperation(JInternalFrame.EXIT_ON_CLOSE);
        super.setVisible(true);

        startTraining = new Button("start training");
        exit = new Button("Exit");
        handle = new handler();
        exit.addActionListener(handle);
        startTraining.addActionListener(handle);
        labelPositives = new Label("Positives : ");
        labelWidth = new Label("width ");
        labelHeight = new Label("height ");
        labelTraining = new Label("Training : ");
        labelStages = new Label("Stages ");
        labelFeature = new Label("Feature type ");
        selectHeight = new JSpinner(new SpinnerNumberModel(48, 24, 100, 1));
        selectWidth = new JSpinner(new SpinnerNumberModel(48, 24, 100, 1));
        selectStages = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        selectFeature = new JComboBox<>();
        selectFeature.addItem("LBP");
        selectFeature.addItem("HAAR");

        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(labelPositives);
        p1.add(labelWidth);
        p1.add(selectWidth);
        p1.add(labelHeight);
        p1.add(selectHeight);

        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(labelTraining);
        p2.add(labelStages);
        p2.add(selectStages);
        p2.add(labelFeature);
        p2.add(selectFeature);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(startTraining);
        buttons.add(exit);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(p1);
        centerPanel.add(p2);
        super.add(centerPanel, BorderLayout.CENTER);
        super.add(buttons, BorderLayout.SOUTH);
        super.pack();
    }

    private class handler implements ActionListener {

        Process p;

        public handler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exit) {
                dispose();
            } else {
                if (theme.OS.startsWith("Windows")) {
                    JOptionPane.showMessageDialog(null, "cant run training on windows unless you have configured a server");
                } else {
                    try {
                        File file = new File(theme.wd + "images/sorted/positive");
                        String command = "opencv_createsamples -info cars.info -num " + file.list().length + " -w " + selectWidth.getValue() + " -h " + selectHeight.getValue() + " -vec cars.vec";
                        p = Runtime.getRuntime().exec(command, null, new File(theme.wd + "images/sorted"));
                        p.waitFor();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File f = new File(theme.wd + "images/sorted/data");
                                    if (!f.exists()) {
                                        f.mkdir();
                                    }
                                    f = new File(theme.wd + "images/sorted/positive");
                                    int pos = (int) (f.list().length * 0.9);
                                    f = new File(theme.wd + "images/sorted/negative");
                                    int neg = (int) (f.list().length * 0.9);
                                    Runtime.getRuntime().exec("/usr/bin/x-terminal-emulator --disable-factory -e opencv_traincascade -data data -vec cars.vec -bg bg.txt -numPos " + pos + " -numNeg " + neg + " -numStages " + selectStages.getValue() + " -w " + selectWidth.getValue() + " -h " + selectHeight.getValue() + " -featureType -" + selectFeature.getSelectedItem(), null, new File(theme.wd + "images/sorted"));
                                    System.out.println("opencv_traincascade -data data -vec cars.vec -bg bg.txt -numPos " + pos + " -numNeg " + neg + " -numStages " + selectStages.getValue() + " -w " + selectWidth.getValue() + " -h " + selectHeight.getValue() + " -featureType -" + selectFeature.getSelectedItem());
                                } catch (IOException ex) {
                                    if (ex.getMessage() != null) {
                                        JOptionPane.showMessageDialog(null, ex.getMessage());
                                    }
                                }
                            }
                        }).start();

                    } catch (InterruptedException | IOException ex) {
                        if (ex.getMessage() != null) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    } finally {
                        if (p != null) {
                            if (p.isAlive()) {
                                p.destroy();
                            }
                        }
                    }
                }
            }
        }
    }
}
