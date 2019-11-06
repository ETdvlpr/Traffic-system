/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.theme;
import com.GUI.Label;
import com.GUI.Button;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author dave
 */
public class trainer extends JInternalFrame {

    private JPanel mainPanel, southPanel, northPanel;
    private Button next, previous, Train, cancel, invalid, clear;
    private ArrayList<String> files, positives, negatives;
    private Label image;
    private Label msg;
    private final handler handle;
    private int currentImage = 0, maxValues = 0;
    private int[][] values;
    public static String unsorted_images;
    private File file;
    private BufferedImage img;

    static {
        if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
            unsorted_images = "images/unsorted/";
        } else if (theme.OS.startsWith("Windows")) {
            unsorted_images = "images\\unsorted\\";
        }
    }

    public trainer() {
        super("Trainer", true, true, true, true);
        super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        next = new Button("Next");
        previous = new Button("Previous");
        invalid = new Button("Invalid");
        Train = new Button("Train");
        cancel = new Button("Cancel");
        clear = new Button("Clear");
        handle = new handler();
        image = new Label();
        msg = new Label();
        next.addActionListener(handle);
        invalid.addActionListener(handle);
        previous.addActionListener(handle);
        Train.addActionListener(handle);
        cancel.addActionListener(handle);
        clear.addActionListener(handle);
        southPanel.add(Train);
        southPanel.add(cancel);

        mainPanel.add(image, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(previous);
        buttons.add(next);
        buttons.add(invalid);
        mainPanel.add(buttons, BorderLayout.SOUTH);
        northPanel.add(clear);
        northPanel.add(msg);

        load_files();
        super.setLayout(new BorderLayout());
        super.add(northPanel, BorderLayout.NORTH);
        super.add(mainPanel, BorderLayout.CENTER);
        super.add(southPanel, BorderLayout.SOUTH);
        super.pack();
    }

    private void load_files() {
        files = new ArrayList<>();
        BufferedReader br = null;
        String line;

        try {
            //create file with list of pictures
            FileWriter writer = new FileWriter(theme.wd + unsorted_images + "list.txt");
            for (File listFile : new File(theme.wd + unsorted_images).listFiles()) {
                if (listFile.isFile()) {
                    if (listFile.getName().endsWith(".jpg")) {
                        writer.write(listFile.getName() + "\n");
                    }
                }
            }
            writer.close();

            // read file into array
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(theme.wd + unsorted_images + "/list.txt"))));
            } else if (theme.OS.startsWith("Windows")) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(theme.wd + unsorted_images + "\\list.txt"))));
            }
            while ((line = br.readLine()) != null) {
                files.add(line);
            }

            // resize all images
            File resize = null;
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                resize = new File(theme.wd + "Resources/cpp/resize");
            } else if (theme.OS.startsWith("Windows")) {
                resize = new File(theme.wd + "Resources\\cpp\\resize.exe");
            }

            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                Files.copy(resize.toPath(), Paths.get(theme.wd, unsorted_images, "resize"));
                resize = new File(theme.wd + unsorted_images + "resize");
            } else if (theme.OS.startsWith("Windows")) {
                Files.copy(resize.toPath(), Paths.get(theme.wd, unsorted_images, "resize.exe"));
                resize = new File(theme.wd + unsorted_images + "resize.exe");
            }
            Process p = null;
            try {
                String[] command = {"./resize", "list.txt"};
                if (theme.OS.startsWith("Windows")) {
                    command[0] = theme.wd + unsorted_images + "resize.exe";
                }
                ProcessBuilder builder = new ProcessBuilder(command);
                if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                    builder.directory(new File(theme.wd + unsorted_images));
                }
                p = builder.start();
                p.waitFor();
            } catch (InterruptedException ex) {
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
            resize.delete();

        } catch (IOException | NoSuchElementException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                if (ex.getMessage() != null) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }
        values = new int[files.size()][];
        if (files.size() > 0) {
            update_image(files.get(0));
        }
        image.addMouseListener(new MouseInputListener() {
            int firstPressX, firstPressY, width, height, x, y;

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                firstPressX = e.getX();
                firstPressY = e.getY();
                e.consume();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                x = e.getX() < 0 ? 0 : e.getX();
                x = e.getX() > 500 ? 500 : x;
                y = e.getY() < 0 ? 0 : e.getY();
                y = e.getY() > 500 ? 500 : y;
                width = x - firstPressX;
                if (width < 0) {
                    firstPressX += width;
                    width = 0 - width;
                }
                height = y - firstPressY;
                if (height < 0) {
                    firstPressY += height;
                    height = 0 - height;
                }
                if (!(width == 0 || height == 0)) {
                    image.getGraphics().drawRect(firstPressX, firstPressY, width, height);
                    int[] temp;
                    if (values[currentImage] == null || values[currentImage].length == 1) {
                        values[currentImage] = new int[]{1, firstPressX, firstPressY, width, height};
                    } else {
                        temp = values[currentImage];
                        values[currentImage] = new int[temp.length + 4];
                        System.arraycopy(temp, 0, values[currentImage], 0, temp.length);
                        values[currentImage][0]++;
                        System.arraycopy(new int[]{firstPressX, firstPressY, width, height}, 0, values[currentImage], temp.length, 4);
                    }
                    System.out.println("Trainer - :" + Arrays.toString(values[currentImage]));
                }
                e.consume();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

    }

    private void update_image(String file) {
        try {
            img = ImageIO.read(new File(theme.wd + unsorted_images + file));
            image.setIcon(new ImageIcon(img));

        } catch (IOException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    private class handler implements ActionListener {

        private boolean alive = true;

        public handler() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (values[currentImage] == null) {
                values[currentImage] = new int[]{0};
            }
            if (e.getSource() == next) {
                if (currentImage < files.size() - 1) {
                    currentImage++;
                    maxValues = currentImage > maxValues ? currentImage : maxValues;
                }
            } else if (e.getSource() == previous) {
                if (currentImage > 0) {
                    currentImage--;
                }
            } else if (e.getSource() == invalid) {
                values[currentImage] = new int[]{-1};
            } else if (e.getSource() == clear) {
                values[currentImage] = null;
            } else if (e.getSource() == Train) {
                commit();
                train t = new train();
                alive = false;
            } else if (e.getSource() == cancel) {
                setVisible(false);
                alive = false;
            }
            if (alive) {
                update_image(files.get(currentImage));
                if (values[currentImage] == null) {
                    msg.setText("");
                    northPanel.setBackground(Color.lightGray);
                    clear.setEnabled(false);
                } else if (values[currentImage].length == 1) {
                    if (values[currentImage][0] == 0) {
                        msg.setText("negative image");
                        northPanel.setBackground(Color.yellow);
                    } else {
                        msg.setText("invalid image");
                        northPanel.setBackground(Color.magenta);
                    }
                    clear.setEnabled(false);
                } else {
                    msg.setText("positive image");
                    northPanel.setBackground(Color.green);
                    clear.setEnabled(true);
                }

                repaint();
            }
        }

        private void commit() {

            positives = new ArrayList<>();
            negatives = new ArrayList<>();
            String line, moveTo;
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                file = new File(theme.wd + "images/sorted/positive/");
            } else if (theme.OS.startsWith("Windows")) {
                file = new File(theme.wd + "images\\sorted\\positive\\");
            }
            if (!file.exists()) {
                file.mkdirs();
            }

            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                file = new File(theme.wd + "images/sorted/negative/");
            } else if (theme.OS.startsWith("Windows")) {
                file = new File(theme.wd + "images\\sorted\\negative\\");
            }
            if (!file.exists()) {
                file.mkdirs();
            }
            
            for (int i = 0; i < maxValues; i++) {
                moveTo = null;
                System.out.println(i + "   " + maxValues);
                switch (values[i][0]) {
                    case -1:
                        //delete
                        new File(theme.wd + unsorted_images + files.get(i)).delete();
                        break;
                    case 0:
                        //add file name to negative
                        negatives.add("negative/" + files.get(i));

                        if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                            moveTo = "images/sorted/negative/";
                        } else if (theme.OS.startsWith("Windows")) {
                            moveTo = "images\\sorted\\negative\\";
                        }

                        break;
                    default:
                        //move file to /sorted/positive

                        if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                            moveTo = "images/sorted/positive/";
                        } else if (theme.OS.startsWith("Windows")) {
                            moveTo = "images\\sorted\\positive\\";
                        }
                        //add file name to positive
                        line = "";
                        for (int x : values[i]) {
                            line += " " + String.valueOf(x);
                        }
                        positives.add("positive/" + files.get(i) + line);
                        break;
                }
                if (moveTo != null) {
                    try {
                        file = new File(theme.wd + unsorted_images + files.get(i));
                        file.renameTo(new File(theme.wd + moveTo + files.get(i)));
                        file.delete();
                    } catch (Exception ex) {
                        if (ex.getMessage() != null) {
                            JOptionPane.showMessageDialog(null, file.getAbsoluteFile() + ex.getMessage());
                        }
                    }
                }
            }
            //write to txt files
            try {
                FileWriter writer = null;

                if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                    writer = new FileWriter(theme.wd + "images/sorted/cars.info", true);
                } else if ("Windows".equals(theme.OS)) {
                    writer = new FileWriter(theme.wd + "images\\sorted\\cars.info", true);
                }
                for (String positive : positives) {
                    writer.write(positive + "\n");
                }
                writer.close();

                if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                    writer = new FileWriter(theme.wd + "images/sorted/bg.txt", true);
                } else if ("Windows".equals(theme.OS)) {
                    writer = new FileWriter(theme.wd + "images\\sorted\\bg.txt", true);
                }
                for (String negative : negatives) {
                    writer.write(negative + "\n");
                }
                writer.close();
            } catch (IOException ex) {
                if (ex.getMessage() != null) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
            setVisible(false);
        }
    }

    private void draw_boxes() {
        if (files.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No images to sort through, exiting trainer");
            dispose();
        } else if (values != null) {
            if (values[currentImage] != null) {
                if (values[currentImage].length > 1) {
                    for (int i = 0; i < values[currentImage][0]; i++) {
                        img.getGraphics().drawRect(values[currentImage][4 * i + 1], values[currentImage][4 * i + 2], values[currentImage][4 * i + 3], values[currentImage][4 * (i + 1)]);
                        image.setIcon(new ImageIcon(img));
                    }
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.
        draw_boxes();
    }
}
