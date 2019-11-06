package com.functions;

import com.GUI.theme;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author dave
 */
public class play implements Runnable {

    private String video;
    private Process p;
    
    public play(String video) {
        this.video = video;
    }

    @Override
    public void run() {
        try {
            String[] command = {"./play", video};
            if (theme.OS.startsWith("Windows")) {
                command[0] = theme.wd + "Resources\\cpp\\play.exe";
            }
            ProcessBuilder builder = new ProcessBuilder(command);
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                builder.directory(new File(theme.wd + "Resources/cpp"));
            }
            p = builder.start();
            while (!Thread.interrupted() && p.isAlive());
            if (p.isAlive()) {
                p.getOutputStream().write(27);
            }
        } catch (IOException | NoSuchElementException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } finally {
            if (p.isAlive()) {
                p.destroy();
            }
        }
    }
}
