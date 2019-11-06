/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Pages;

import com.GUI.theme;
import com.functions.play;
import com.types.Video;
import com.types.Intersection;
import com.Run.Run;
import com.functions.countCars;
import com.sun.glass.events.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

/**
 *
 * @author dave
 */
public class MainWindow extends JFrame {

    public static JDesktopPane desktop;
    private JMenuBar menuBar;
    private JMenu file, edit, view, viewIntersection, viewVideo, videoMenu, intersectionMenu, help;
    private JMenuItem addCamera, addIntersection, settings, exit, train, fullScreen, about, contactUs;
    private JToolBar toolbar;
    private JButton toolNewCamera, toolNewIntersection, toolMaskVideo, toolTrainClassifier, toolDebugVideo;
    private final handler handle;
    private static ArrayList<Intersection> intersections;
    public static ArrayList<Video> videos;
    private static ArrayList<Thread> pool;

    private static addCamera addCameraFrame;
    private static trainer trainerFrame;
    private static list maskLs, debugLs;
    private static settings settingsFrame;
    private static contactUs contactUsFrame;
    private Process p;
    private addInt addIntersectionFrame;

    public static final Preferences data;

    static {
        data = Preferences.userNodeForPackage(com.Pages.MainWindow.class);
    }

    public MainWindow() {
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setMinimumSize(new Dimension(400, 300));

        handle = new handler();
        desktop = new JDesktopPane();

        initialize_intersections_and_videos();
        initialize_menus();
        initialize_frames();

        super.setJMenuBar(menuBar);
        super.add(toolbar, BorderLayout.NORTH);

        super.setExtendedState(JFrame.MAXIMIZED_BOTH);
        super.add(desktop);
        super.pack();
    }

    private void initialize_menus() // <editor-fold defaultstate="collapsed" desc="Code to initialize the menubar">
    {
        menuBar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V);
        videoMenu = new JMenu("Video");
        videoMenu.setMnemonic(KeyEvent.VK_D);
        intersectionMenu = new JMenu("Intersection");
        intersectionMenu.setMnemonic(KeyEvent.VK_I);
        help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);

        viewIntersection = new JMenu("Intersections");
        viewIntersection.setMnemonic(KeyEvent.VK_I);
        viewVideo = new JMenu("Video");
        viewVideo.setMnemonic(KeyEvent.VK_D);

        settings = new JMenuItem("Settings");
        settings.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/settings.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        settings.setMnemonic(KeyEvent.VK_S);
        settings.addActionListener(handle);
        exit = new JMenuItem("Exit");
        exit.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/quit.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(handle);
        addIntersection = new JMenuItem("Add Intersection");
        addIntersection.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/add intersection.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        addIntersection.setMnemonic(KeyEvent.VK_I);
        addIntersection.addActionListener(handle);
        addCamera = new JMenuItem("Add Camera");
        addCamera.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/add camera.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        addCamera.setMnemonic(KeyEvent.VK_C);
        addCamera.addActionListener(handle);
        train = new JMenuItem("Train detection system");
        train.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/train cascade.png")).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        train.setMnemonic(KeyEvent.VK_T);
        train.addActionListener(handle);
        fullScreen = new JMenuItem("Full Screen");
        fullScreen.setMnemonic(KeyEvent.VK_F);
        fullScreen.addActionListener(handle);
        about = new JMenuItem("About");
        about.setMnemonic(KeyEvent.VK_A);
        about.addActionListener(handle);
        contactUs = new JMenuItem("Contact us");
        contactUs.setMnemonic(KeyEvent.VK_C);
        contactUs.addActionListener(handle);

        // add listeners to all intersection menu items on view
        // add options with listeners to intersections menu
        for (int i = 0; i < intersections.size(); i++) {
            JMenuItem viewMnuIntersect = new JMenuItem(intersections.get(i).getName());
            viewMnuIntersect.addActionListener(new intersection_view(i));
            viewIntersection.add(viewMnuIntersect);
            JMenu intersectMenu = new JMenu(intersections.get(i).getName());
            for (JMenuItem item : intersection_menu(i)) {
                intersectMenu.add(item);
            }
            intersectionMenu.add(intersectMenu);
        }

        // initialize all video items in view
        // add options with listeners to all video menus
        for (int i = 0; i < videos.size(); i++) {
            JMenuItem viewMnuvid = new JMenuItem(videos.get(i).getDescription());
            viewMnuvid.addActionListener(new video_view(i));
            viewVideo.add(viewMnuvid);
            JMenu vidMenu = new JMenu(videos.get(i).getDescription());
            videoMenu.add(vidMenu);
            for (JMenuItem item : video_menu(i)) {
                vidMenu.add(item);
            }
        }

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(view);
        menuBar.add(videoMenu);
        menuBar.add(intersectionMenu);
        menuBar.add(help);

        file.add(settings);
        file.addSeparator();
        file.add(exit);
        edit.add(addIntersection);
        edit.add(addCamera);
        edit.add(train);
        view.add(viewIntersection);
        view.add(viewVideo);
        view.add(fullScreen);
        help.add(contactUs);
        help.add(about);

        toolbar = new JToolBar();
        toolNewCamera = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/add camera.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        toolNewCamera.setToolTipText("Add New camera");
        toolNewIntersection = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/add intersection.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        toolNewIntersection.setToolTipText("Add new intersection");
        toolMaskVideo = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/mask2.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        toolMaskVideo.setToolTipText("Add mask to video");
        toolDebugVideo = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/debug video.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        toolDebugVideo.setToolTipText("Debug video");
        toolTrainClassifier = new JButton(new ImageIcon(new ImageIcon(getClass().getResource("/com/Resources/images/train cascade.png")).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        toolTrainClassifier.setToolTipText("Train classifier");

        toolbar.add(toolNewCamera);
        toolbar.add(toolNewIntersection);
        toolbar.add(toolMaskVideo);
        toolbar.add(toolDebugVideo);
        toolbar.add(toolTrainClassifier);

        toolNewCamera.addActionListener(handle);
        toolNewIntersection.addActionListener(handle);
        toolMaskVideo.addActionListener(handle);
        toolDebugVideo.addActionListener(handle);
        toolTrainClassifier.addActionListener(handle);
    }
    // </editor-fold>

    private JMenuItem[] video_menu(int i) {
        JMenuItem[] items = new JMenuItem[4];
        items[0] = new JMenuItem("Debug");
        items[1] = new JMenuItem("Add Mask");
        items[2] = new JMenuItem("Use to train");
        items[3] = new JMenuItem("Remove");

        video_menu_listener listener = new video_menu_listener(items, i);
        items[0].addActionListener(listener);
        items[1].addActionListener(listener);
        items[2].addActionListener(listener);
        items[3].addActionListener(listener);

        return items;
    }

    private JMenuItem[] intersection_menu(int i) {
        JMenuItem[] items = new JMenuItem[3];
        items[0] = new JMenuItem("start");
        items[1] = new JMenuItem("Debug");
        items[2] = new JMenuItem("remove");

        intersection_menu_listener listener = new intersection_menu_listener(items, i);
        items[0].addActionListener(listener);
        items[1].addActionListener(listener);
        items[2].addActionListener(listener);
        return items;
    }

    private void initialize_intersections_and_videos() {
        pool = new ArrayList<>();

        videos = new ArrayList<>();
        for (int i = 0; i < data.getInt("number of videos", 0); i++) {
            videos.add(new Video(data.get(String.format("video%02d", i), ""), data.get(String.format("xml%02d", i), ""), data.get(String.format("mask%02d", i), "0"), data.get(String.format("description%02d", i), "")));
        }

               //     data.putInt("number of intersections", 0);
        intersections = new ArrayList<>();
        for (int i = 0; i < data.getInt("number of intersections", 0); i++) {
            switch (data.getInt(String.format("type%02d", i), 0)) {
                case 4:
                    intersections.add(new intersectionFour(videos.get(data.getInt(String.format("north%02d", i), 0)), videos.get(data.getInt(String.format("south%02d", i), 0)), videos.get(data.getInt(String.format("east%02d", i), 0)), videos.get(data.getInt(String.format("west%02d", i), 0)), false, data.get(String.format("name%02d", i), "")));
                    break;
                case 3:
                    intersections.add(new intersectionThree(videos.get(data.getInt(String.format("north%02d", i), 0)), videos.get(data.getInt(String.format("south%02d", i), 0)), videos.get(data.getInt(String.format("east%02d", i), 0)), false, data.get(String.format("name%02d", i), "")));
                    break;
                case 2:
                    intersections.add(new intersectionTwo(videos.get(data.getInt(String.format("north%02d", i), 0)), videos.get(data.getInt(String.format("west%02d", i), 0)), false, data.get(String.format("name%02d", i), "")));
                    break;
                default:
                    break;
            }
        }

        // start all the intersections and add them to main display
        for (Intersection intersection1 : intersections) {
            //intersection1.start();
            desktop.add(intersection1);
        }

        // intialize all videos
    }

    private void initialize_frames() {
        addCameraFrame = new addCamera();
        desktop.add(addCameraFrame);

        trainerFrame = new trainer();
        desktop.add(trainerFrame);

        maskLs = new list(0, videos);
        desktop.add(maskLs);

        debugLs = new list(1, videos);
        desktop.add(debugLs);

        addIntersectionFrame = new addInt(videos);
        desktop.add(addIntersectionFrame);

        settingsFrame = new settings();
        desktop.add(settingsFrame);
        
        contactUsFrame = new contactUs();
        desktop.add(contactUsFrame);
    }

    private class handler extends AbstractAction {

        private boolean fullscreen;

        public handler() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == settings) {
                settingsFrame.setVisible(true);
            } else if (e.getSource() == exit) {
                System.exit(0);
            } else if (e.getSource() == addCamera || e.getSource() == toolNewCamera) {
                addCameraFrame.setVisible(true);
            } else if (e.getSource() == addIntersection || e.getSource() == toolNewIntersection) {
                addIntersectionFrame.setVisible(true);
            } else if (e.getSource() == train || e.getSource() == toolTrainClassifier) {
                trainerFrame.setVisible(true);
            } else if (e.getSource() == fullScreen) {
                if (!fullscreen) {
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(Run.win);
                    fullScreen.setText("Exit full screen");
                    fullscreen = true;
                } else {
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].setFullScreenWindow(null);
                    fullScreen.setText("Full screen");
                    fullscreen = false;
                }
            } else if (e.getSource() == about) {
                try {
                    java.awt.Desktop.getDesktop().browse(URI.create("file://" + theme.wd + "/javadoc/index.html"));
                } catch (IOException ex) {
                    if (ex.getMessage() != null) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            } else if (e.getSource() == contactUs) {
                contactUsFrame.setVisible(true);
            } else if (e.getSource() == toolMaskVideo) {
                maskLs.setVisible(true);
            } else if (e.getSource() == toolDebugVideo) {
                debugLs.setVisible(true);
            }
        }
    }

    private class intersection_menu_listener implements ActionListener {

        private final JMenuItem[] items;
        private final int i;

        public intersection_menu_listener(JMenuItem[] items, int i) {
            this.items = items;
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == items[0]) {
                if (!intersections.get(i).online()) {
                    intersections.get(i).start();
                }
            } else if (e.getSource() == items[1]) {
                ArrayList<Video> temp = intersections.get(i).getVideos();
                String tempName = intersections.get(i).getName();
                int tempType = intersections.get(i).getType();
                intersections.get(i).close();
                intersections.remove(i);
                switch (tempType) {
                    case 4:
                        intersections.add(i, new intersectionFour(temp, true, tempName));
                        break;
                    case 3:
                        intersections.add(i, new intersectionThree(temp, true, tempName));
                        break;
                    case 2:
                        intersections.add(i, new intersectionTwo(temp, true, tempName));
                        break;
                    default:
                        break;
                }
                desktop.add(intersections.get(i));
                intersections.get(i).setVisible(true);
                intersections.get(i).start();
            } else if (e.getSource() == items[2]) {
                if (JOptionPane.showConfirmDialog(desktop, "removing this intersection will require a restart. continue?") == 0) {
                    intersections.remove(i);
                    for (int j = i; j < data.getInt("number of intersections", 0) - 1; j++) {
                        data.putInt(String.format("north%02d", j), data.getInt(String.format("north%02d", j + 1), 0));
                        data.putInt(String.format("east%02d", j), data.getInt(String.format("east%02d", j + 1), 0));
                        data.putInt(String.format("south%02d", j), data.getInt(String.format("south%02d", j + 1), 0));
                        data.putInt(String.format("west%02d", j), data.getInt(String.format("west%02d", j + 1), 0));
                        data.putInt(String.format("type%02d", j), data.getInt(String.format("type%02d", j + 1), 0));
                        data.put(String.format("name%02d", j), data.get(String.format("name%02d", j + 1), ""));
                    }
                    data.putInt("number of intersections", data.getInt("number of intersections", 1) - 1);
                    restart();
                }
            }
        }
    }

    private class video_menu_listener implements ActionListener {

        private final JMenuItem[] items;
        private final int i;

        public video_menu_listener(JMenuItem[] items, int i) {
            this.items = items;
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == items[0]) {
                debug(videos.get(i));
            } else if (e.getSource() == items[1]) {
                mask(videos.get(i), i);
            } else if (e.getSource() == items[2]) {
                train(videos.get(i));
            } else if (e.getSource() == items[3]) {
                if (JOptionPane.showConfirmDialog(desktop, "removing this video will require a restart. continue?") == 0) {
                    if (!check_used()) {
                        videos.remove(i);
                        for (int j = i; j < data.getInt("number of videos", 0) - 1; j++) {
                            data.put(String.format("video%02d", j), data.get(String.format("video%02d", j + 1), ""));
                            data.put(String.format("description%02d", j), data.get(String.format("description%02d", j + 1), ""));
                            data.put(String.format("xml%02d", j), data.get(String.format("xml%02d", j + 1), ""));
                            data.put(String.format("mask%02d", j), data.get(String.format("mask%02d", j + 1), ""));
                        }
                        data.putInt("number of videos", data.getInt("number of videos", 1) - 1);
                        restart();
                    }
                }
            }
        }

        private boolean check_used() {
            return false;
        }
    }

    private class intersection_view implements ActionListener {

        private int i;

        public intersection_view(int i) {
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            intersections.get(i).setVisible(!intersections.get(i).isVisible());
        }
    }

    private class video_view implements ActionListener {

        private final int i;

        public video_view(int i) {
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            pool.add(new Thread(new play(videos.get(i).getVideo())));
            pool.get(pool.size() - 1).start();
        }
    }

    public static void debug(Video vid) {
        pool.add(new Thread(new countCars(vid, true)));
        pool.get(pool.size() - 1).start();
    }

    public static void mask(Video vid, int i) {
        mask m = new mask(vid, i);
        desktop.add(m);
    }

    public void train(Video vid) {

        File train_file = null;
        try {
            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                train_file = new File(theme.wd + "Resources/cpp/train");
            } else if (theme.OS.startsWith("Windows")) {
                train_file = new File(theme.wd + "Resources\\cpp\\train.exe");
            }
            Files.copy(train_file.toPath(), Paths.get(theme.wd, trainer.unsorted_images, "train"));
            train_file = new File(theme.wd + trainer.unsorted_images + "train");
            String[] command = {"./train", vid.getVideo()};
            if (theme.OS.startsWith("Windows")) {
                command[0] = theme.wd + trainer.unsorted_images + "train.exe";
            }
            ProcessBuilder builder = new ProcessBuilder(command);

            if ("Linux".equals(theme.OS) || "Mac OS X".equals(theme.OS)) {
                builder.directory(new File(theme.wd + trainer.unsorted_images));
            }

            p = builder.start();
            p.waitFor();
            JOptionPane.showMessageDialog(desktop, "training images extracted from " + vid.getDescription());
        } catch (IOException | NoSuchElementException | InterruptedException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } finally {
            train_file.delete();
            if (p.isAlive()) {
                p.destroy();
            }
        }
    }

    public static void close() {
        System.out.println("killing all threads");
        pool.forEach((thread1) -> {
            thread1.interrupt();
            System.out.println(thread1 + " stoped");
        });
        intersections.forEach((intersection1) -> {
            intersection1.close();
        });
        try {
            // give it sufficient time to properly close all threads
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            if (ex.getMessage() != null) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }
    }

    public static void restart() {
        Run.win.dispose();
        theme.reset();
        Run.main(new String[1]);
    }
}
