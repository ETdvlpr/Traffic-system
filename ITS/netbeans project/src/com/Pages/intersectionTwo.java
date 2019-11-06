/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.Label;
import com.functions.microController;
import com.functions.countCars;
import com.functions.countCars2;
import com.types.Video;
import com.types.Intersection;
import com.types.Lane;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ImageIcon;

/**
 *
 * @author dave
 */
public final class intersectionTwo extends Intersection {

    private boolean debug;
    private String name;
    public boolean online;
    private Timer timer;
    private ArrayList<Thread> pool;
    private int time, Type;

    private ArrayList<Video> videos;
    private Video northVideo, eastVideo;
    private countCars2 north, east;
    private Image east_west_open, north_south_open, display_img, bg;
    private Label display, time_display, north_display, east_display;
    private Lane open_lane, next_lane;

    public intersectionTwo(Video northVid, Video eastVid, boolean debug, String name) {
        super(name + " : Traffic intersection lights control", true, true, true, true);
        super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        Type = 2;

        northVideo = northVid;
        eastVideo = eastVid;
        videos = new ArrayList<>();
        videos.add(northVid);
        videos.add(eastVid);
        this.debug = debug;
        this.name = name;
        totalIntersections++;

        initialize();

        JPanel top_panel = new JPanel(new GridLayout(2, 1));
        top_panel.add(time_display);
        top_panel.add(north_display);

        super.add(east_display, BorderLayout.EAST);
        super.add(center(top_panel), BorderLayout.NORTH);
        super.add(display, BorderLayout.CENTER);
        super.pack();
    }

    public intersectionTwo(ArrayList<Video> videos, boolean debug, String name) {
        this(videos.get(0), videos.get(1), debug, name);
    }

    public void start() {
        pool.forEach((thread) -> {
            thread.start();
        });
        time = 3;
        open_north_south();
        timer.start();
        online = true;
    }

    private JPanel center(Component comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
        p.setOpaque(true);
        p.setBackground(Color.black);
        p.add(comp);
        return p;
    }

    private void initialize() {
        bg = new ImageIcon(getClass().getResource("/com/Resources/images/type2/background.png")).getImage();
        display_img = new BufferedImage(bg.getWidth(null), bg.getHeight(null), Image.SCALE_DEFAULT);
        east_west_open = new ImageIcon(getClass().getResource("/com/Resources/images/type2/east.png")).getImage();
        north_south_open = new ImageIcon(getClass().getResource("/com/Resources/images/type2/north.png")).getImage();

        display = new Label(new ImageIcon(bg));
        display.setOpaque(true);
        display.setBackground(Color.black);
        north_display = new Label("0");
        north_display.setOpaque(true);
        north_display.setBackground(Color.black);
        north_display.setForeground(Color.yellow);
        east_display = new Label("0");
        east_display.setOpaque(true);
        east_display.setBackground(Color.black);
        east_display.setForeground(Color.yellow);
        time_display = new Label("00");
        time_display.setFont(new Font("Bookman Old Style", Font.BOLD, 30));
        time_display.setOpaque(true);
        time_display.setBackground(Color.black);
        time_display.setForeground(Color.yellow);

        north = new countCars2(northVideo, debug);
        east = new countCars2(eastVideo, debug);

        pool = new ArrayList<>();
        pool.add(new Thread(north));
        pool.add(new Thread(east));

        timer = new Timer(1000, new timer_handler());
    }

    private void open_north_south() {
        open_lane = Lane.NORTH;
        next_lane = Lane.EAST;
        time = north.getIndex() + 10;
    }

    private void open_east_west() {
        open_lane = Lane.EAST;
        next_lane = Lane.NORTH;
        time = east.getIndex() + 10;
    }

    private void draw_lane() {
        Graphics g = display_img.getGraphics();
        g.clearRect(0, 0, display_img.getWidth(null), display_img.getHeight(null));
        g.drawImage(bg, 0, 0, null);
        if (open_lane != null) {
            switch (open_lane) {
                case EAST:
                    g.drawImage(east_west_open, 0, 0, null);
                    break;
                case NORTH:
                    g.drawImage(north_south_open, 0, 0, null);
                    break;
                default:
                    break;
            }
        }
        display.setIcon(new ImageIcon(display_img));
        display.revalidate();
        display.repaint();
    }

    public void close() {
        pool.forEach((thread) -> {
            thread.interrupt();
            System.out.println(thread.toString() + " stoped");
        });
        dispose();
    }

    class timer_handler implements ActionListener {

        boolean enter;

        public timer_handler() {
            enter = true;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            time--;
            if (time == 0) {
                enter = true;
                if (next_lane != null) {
                    switch (next_lane) {
                        case EAST:
                            open_east_west();
                            break;
                        case NORTH:
                            open_north_south();
                            break;
                        default:
                            break;
                    }
                }
                draw_lane();
            }
            north_display.setText(String.valueOf(north.getIndex()));
            east_display.setText(String.valueOf(east.getIndex()));
                time_display.setText(String.format("%02d", time));
            repaint();
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the videos
     */
    public ArrayList<Video> getVideos() {
        return videos;
    }

    public boolean online() {
        return online;
    }

    @Override
    public int getType() {
        return Type;
    }
}
