/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.GUI;

import com.Pages.MainWindow;
import java.awt.Font;

/**
 *
 * @author dave
 */
public class theme {

    public static Font font;
    public static String wd;
    public static String OS;

    static {
        reset();
    }

    public static void reset() {
        OS = System.getProperty("os.name");
        if ("Linux".equals(OS) || "Mac OS X".equals(OS)) {
            wd = MainWindow.data.get("working directory", "/usr/local/traffic/");
        } else if (OS.startsWith("Windows")) {
            wd = MainWindow.data.get("working directory", "C:\\traffic\\");
        } else {
            throw new RuntimeException("Unsupported Operating System");
        }
        font = new Font(MainWindow.data.get("LF_style", "Bookman Old Style"), Font.PLAIN, MainWindow.data.getInt("LF_size", 16));
    }
}
