/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.Button;
import com.GUI.Label;
import com.GUI.theme;
import com.Run.Run;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dave
 */
public class settings extends JInternalFrame {

    private final Label label_font_size, label_font_style, label_theme;
    private final JSpinner label_font_size_select;
    private final JComboBox<String> label_font_style_select, theme_select;
    private final Button ok, cancel, defaults, clear_video, clear_intersections, Set_wd;
    private final handler handler;

    public settings() {
        super("Settings", true, true, true, true);
        super.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

        label_font_style = new Label("Font style");
        label_font_size = new Label("Font Size");
        label_theme = new Label("Theme");
        theme_select = new JComboBox<>();
        label_font_style_select = new JComboBox<>();
        label_font_size_select = new JSpinner(new SpinnerNumberModel(MainWindow.data.getInt("LF_size", 16), 9, 24, 1));
        ok = new Button("Ok");
        cancel = new Button("Cancel");
        clear_video = new Button("Clear videos");
        defaults = new Button("Restore defaults");
        clear_intersections = new Button("Clear intersections");
        Set_wd = new Button("Set working directory");
        handler = new handler();

        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            theme_select.addItem(info.getName());
        }
        for (String FontName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            label_font_style_select.addItem(FontName);
        }
        theme_select.setSelectedItem(MainWindow.data.get("selected theme", "Nimbus"));
        label_font_style_select.setSelectedItem(MainWindow.data.get("LF_style", "Bookman Old Style"));

        set_look();
        ok.addActionListener(handler);
        cancel.addActionListener(handler);
        defaults.addActionListener(handler);
        clear_video.addActionListener(handler);
        clear_intersections.addActionListener(handler);
        Set_wd.addActionListener(handler);
        super.setMinimumSize(new Dimension(400, 400));
        super.pack();
    }

    private JPanel two(Component a, Component b) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(a);
        p.add(b);
        return p;
    }

    private void set_look() {

        JPanel selection_panel = new JPanel(new GridLayout(5, 1));

        selection_panel.add(two(label_theme, theme_select));
        selection_panel.add(two(label_font_style, label_font_style_select));
        selection_panel.add(two(label_font_size, label_font_size_select));
        selection_panel.add(two(clear_intersections, clear_video));
        selection_panel.add(two(Set_wd,new JLabel(theme.wd)));

        JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        button_panel.add(defaults);
        button_panel.add(ok);
        button_panel.add(cancel);

        super.setLayout(new BorderLayout());
        super.add(selection_panel, BorderLayout.CENTER);
        super.add(button_panel, BorderLayout.SOUTH);
    }

    private class handler implements ActionListener {

        public handler() {
        }
        private boolean clear_vid, clear_int;
        private String new_wd = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancel) {
                dispose();
            } else if (e.getSource() == ok) {
                if (clear_vid) {
                    MainWindow.data.putInt("number of videos", 0);
                }
                if (clear_int) {
                    MainWindow.data.putInt("number of intersections", 0);
                }
                if (new_wd != null) {
                    MainWindow.data.put("working directory", new_wd);
                }
                MainWindow.data.put("selected theme", (String) theme_select.getSelectedItem());
                MainWindow.data.put("LF_style", (String) label_font_style_select.getSelectedItem());
                MainWindow.data.putInt("LF_size", (int) label_font_size_select.getValue());
                if (JOptionPane.showConfirmDialog(null, "changes will take effect once the application is restarted do you wish to restart the application now?", "Restart", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
                    MainWindow.restart();
                }
                dispose();
            } else if (e.getSource() == defaults) {
                MainWindow.data.remove("selected theme");
                MainWindow.data.remove("TF_style");
                MainWindow.data.remove("TF_size");
                MainWindow.data.remove("LF_style");
                MainWindow.data.remove("LF_size");

                if (JOptionPane.showConfirmDialog(null, "changes will take effect once the application is restarted do you wish to restart the application now?", "Restart", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
                    MainWindow.restart();
                }
                dispose();
            } else if (e.getSource() == clear_intersections) {
                clear_int = JOptionPane.showConfirmDialog(null, "All intersection data will be purged from the system if you press yes", "remove all intersections", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0;
            } else if (e.getSource() == clear_video) {
                clear_vid = JOptionPane.showConfirmDialog(null, "All video data will be purged from the system if you press yes", "remove all videos", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0;
            } else if (e.getSource() == Set_wd) {
                new_wd = JOptionPane.showInternalInputDialog(MainWindow.desktop, "Input the new working directory", "Working directory", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
