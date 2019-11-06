/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Run;

import com.Pages.intersectionFour;
import com.Pages.MainWindow;
import com.Pages.mask;
import com.GUI.theme;
import com.Pages.train;
import com.Pages.trainer;
import javax.swing.JOptionPane;
/**
 *
 * @author dave
 */
public class Run {

    public static MainWindow win;
    public static void main(String[] args){
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (info.getName().equals(MainWindow.data.get("selected theme", "Nimbus"))) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "theme class not found!","unable to set look and feel", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException ex) {
            JOptionPane.showMessageDialog(null, "theme could not be instantiated!","unable to set look and feel", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "theme is demanding unauthorized access!","unable to set look and feel", JOptionPane.ERROR_MESSAGE);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(null, "the selected look and feel is not supported by your system!","unable to set look and feel", JOptionPane.ERROR_MESSAGE);
        }
        
        
        //mask m = new mask(theme.wd + "Resources/video/CarsDrivingUnderBridge.mp4");
//        trainer t = new trainer();
//        t.setVisible(true);
//train t = new train(); 
        win = new MainWindow();
        Runtime.getRuntime().addShutdownHook(new Thread(MainWindow::close));
    }
}
