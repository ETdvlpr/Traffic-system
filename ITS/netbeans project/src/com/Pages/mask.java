/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.Label;
import com.GUI.theme;
import com.types.Video;
import com.GUI.Button;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dave
 */
public class mask extends JInternalFrame {

    private Label img;
    private Video video;
    private Process p;
    private BufferedImage im;
    private JScrollPane scrollPane;
    private JPanel buttonPanel;
    private Button setButton, clearButton, cancelButton, addPointButton;
    private final handler handle;

    private int npoints = 0;
    private boolean addPointPressed;
    private ArrayList<Integer> Xpoints, Ypoints;
    private int x, y, x2, y2, num;

    public mask(Video video, int num) {
        super("Mask creator : " + video.getDescription(), true, true, true, true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
        super.setMinimumSize(new Dimension(300, 400));

        this.video = video;
        this.num = num;  // num is the video index to be saved in preferences

        Xpoints = new ArrayList<>();
        Ypoints = new ArrayList<>();

        try {
            String[] command = {"./snapshot", this.video.getVideo()};
            if (theme.OS.startsWith("Windows")) {
                command[0] = theme.wd + "Resources\\cpp\\resize.exe";
            }
            ProcessBuilder builder = new ProcessBuilder(command);
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                builder.directory(new File(theme.wd + "Resources/cpp"));
            }
            p = builder.start();
            p.waitFor();
        } catch (InterruptedException | IOException | NoSuchElementException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } finally {
            if (p.isAlive()) {
                p.destroy();
            }
        }
        File imageFile = null;
        try {
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                imageFile = new File(theme.wd + "Resources/cpp/snap.jpg");
            } else if (theme.OS.startsWith("Windows")) {
                imageFile = new File(theme.wd + "Resources\\cpp\\snap.jpg");
            }
            im = ImageIO.read(imageFile);
        } catch (IOException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
        imageFile.delete();
        img = new Label(new ImageIcon(im.getScaledInstance(im.getWidth(), im.getHeight(), Image.SCALE_SMOOTH)));
        scrollPane = new JScrollPane(img, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setSize(new Dimension(700, 700));

        img.addMouseListener(new MouseListenerImpl());

        setButton = new Button("Set");
        cancelButton = new Button("Cancel");
        clearButton = new Button("Clear");
        addPointButton = new Button("Add Point");
        handle = new handler();
        addPointButton.addActionListener(handle);
        cancelButton.addActionListener(handle);
        clearButton.addActionListener(handle);
        setButton.addActionListener(handle);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(clearButton);
        buttonPanel.add(setButton);
        buttonPanel.add(cancelButton);

        super.add(addPointButton, BorderLayout.NORTH);
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.SOUTH);
        super.pack();
    }

    private class handler implements ActionListener {

        public handler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelButton) {
                dispose();
            } else if (e.getSource() == clearButton) {
                npoints = 0;
                Xpoints.clear();
                Ypoints.clear();
                repaint();
            } else if (e.getSource() == addPointButton) {
                addPointPressed = !addPointPressed;
                if (addPointPressed) {
                    addPointButton.setText("stop");
                } else {
                    addPointButton.setText("Add Point");
                }
            } else if (e.getSource() == setButton) {
                try {

                    File file = null;

                    if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                        file = new File(theme.wd + "data/" + video.getDescription() + ".dat");
                    } else if (theme.OS.startsWith("Windows")) {
                        file = new File(theme.wd + "data\\" + video.getDescription() + ".dat");
                    }
//                    File file = new File(theme.wd + "data/" + video + ".dat");
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    }
//                    FileWriter writer = new FileWriter(file, true);
                    FileWriter writer = new FileWriter(file);
                    writer.write(npoints + "\n");
                    for (int i = 0; i < npoints; i++) {
                        writer.write(Xpoints.get(i) + "\n");
                        writer.write(Ypoints.get(i) + "\n");
                    }
                    writer.close();

                    MainWindow.data.put(String.format("mask%02d", num), file.toString());
                    MainWindow.restart();
                } catch (IOException ex) {
                    if (ex.getMessage() != null) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
                JOptionPane.showMessageDialog(null, "mask added to video");
                dispose();
            }
        }
    }

    private class MouseListenerImpl implements MouseListener {

        private int[] temp;

        public MouseListenerImpl() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (addPointPressed) {
                npoints++;
                if (npoints == 1) {
                    y = e.getY();
                    x = e.getX();
                } else if (npoints == 2) {
                    y2 = e.getY();
                    x2 = e.getX();
                }

                Xpoints.add(e.getX());
                Ypoints.add(e.getY());

                repaint();
                e.consume();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        if (im != null) {
            g = im.getGraphics();
//            if (npoints == 1) {
//                for (int j = 1; j < listOfPositions.size(); ++j) {
//                    Point A = (Point) (listOfPositions.get(j - 1));
//                    Point B = (Point) (listOfPositions.get(j));
//                    g.drawLine(A.x, A.y, B.x, B.y);
//                }
//            } else {
//            }
            if (npoints > 2) {
                int[] xpoint = new int[Xpoints.size()];
                for (int i = 0; i < Xpoints.size(); i++) {
                    xpoint[i] = Xpoints.get(i);
                }
                int[] ypoint = new int[Ypoints.size()];
                for (int i = 0; i < Ypoints.size(); i++) {
                    ypoint[i] = Ypoints.get(i);
                }
                g.drawPolygon(xpoint, ypoint, npoints);
            } else {
                if (x2 != 0) {
                    g.drawLine(x, y, x2, y2);
                }
            }
            img.setIcon(new ImageIcon(im));
        }
    }
}
