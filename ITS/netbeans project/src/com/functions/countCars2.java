package com.functions;

import com.GUI.theme;
import com.types.Video;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author dave
 */
public class countCars2 implements Runnable {

    public boolean stop;
    private int index;
    private String xml, video, mask_file, desc;
    private boolean debug;
    private Process p;
    private Scanner sc;

    public countCars2(String xml, String video) {
        this.video = video;
        this.xml = xml;
    }

    public countCars2(String xml, String video, String mask_file, boolean debug, String desc) {
        this.video = video;
        this.xml = xml;
        this.mask_file = mask_file;
        this.debug = debug;
        this.desc = desc;
    }

    public countCars2(Video vid, boolean debug) {
        this(vid.getXml(), vid.getVideo(), vid.getMaskFile(), debug, vid.getDescription());
    }

    @Override
    public void run() {
        try {
            String[] command;

            Process p = null;
            String data = "data/";
            File bg = new File(theme.wd + data + desc + ".jpg");
            if (!bg.exists()) {
                try {
                    command = new String[]{"./make_background_img", video, desc + ".jpg"};
                    if (theme.OS.startsWith("Windows")) {
                        command[0] = theme.wd + data + "make_background_img.exe";
                    }
                    System.out.println(Arrays.toString(command));
                    ProcessBuilder builder = new ProcessBuilder(command);
                    if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                        builder.directory(new File(theme.wd + data));
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
            }

            command = mask_file == null ? new String[4] : new String[5];
            command[0] = "./vehicle_detection_edge_detection";
            if (theme.OS.startsWith("Windows")) {
                command[0] = theme.wd + "Resources\\cpp\\vehicle_detection_edge_detection.exe";
            }
            command[1] = theme.wd + data + desc + ".jpg";
            command[2] = video;
            command[3] = debug ? "1" : "0";
            if (mask_file != null) {
                command[4] = mask_file;
            }
            System.out.println(Arrays.toString(command));
            ProcessBuilder builder = new ProcessBuilder(command);
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                builder.directory(new File(theme.wd + "Resources/cpp"));
            }

            p = builder.start();
            sc = new Scanner(p.getInputStream());
            String input;
            while (!Thread.interrupted()) {
                input = sc.next();
                try {
                    index = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                }
            }
            p.getOutputStream().write(27);
        } catch (IOException | NoSuchElementException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } finally {
            close();
        }
    }

    public int getIndex() {
        return index;
    }

    public void close() {
        if (sc != null) {
            sc.close();
        }
        if (p != null) {
            if (p.isAlive()) {
                try {
                    p.getOutputStream().write(27);
                } catch (IOException ex) {
                    if (ex.getMessage() != null) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
                if (p.isAlive()) {
                    p.destroy();
                }
            }
        }
    }
}
