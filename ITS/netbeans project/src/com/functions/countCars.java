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
public class countCars implements Runnable {

    public boolean stop;
    private int index;
    private String xml, video, file;
    private boolean debug;
    private Process p;
    private Scanner sc;

    public countCars(String xml, String video) {
        this.video = video;
        this.xml = xml;
    }

    public countCars(String xml, String video, String file, boolean debug) {
        this.video = video;
        this.xml = xml;
        this.file = file;
        this.debug = debug;
    }

    public countCars(Video vid, boolean debug) {
        this(vid.getXml(), vid.getVideo(), vid.getMaskFile(), debug);
    }

    @Override
    public void run() {
        try {
            String[] command;

            command = file == null ? new String[4] : new String[5];
            command[0] = "./vehicle_detection_haarcascades";
            if (theme.OS.startsWith("Windows")) {
                command[0] = theme.wd + "Resources\\cpp\\vehicle_detection_haarcascades.exe";
            }
            command[1] = xml;
            command[2] = video;
            command[3] = debug ? "1" : "0";
            if (file != null) {
                command[4] = file;
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
        sc.close();
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
