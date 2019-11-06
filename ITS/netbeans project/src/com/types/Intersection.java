/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.types;

import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.Timer;

/**
 *
 * @author dave
 */
public abstract class Intersection extends JInternalFrame{
    public static int totalIntersections = 0;
    public Intersection(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconable){
        super(title, resizable, closable, maximizable, iconable);
    }
    public abstract void start();
    public abstract void close();
    public abstract int getType();
    public abstract ArrayList<Video> getVideos();
    public abstract boolean online();
    public abstract String getName();
}
