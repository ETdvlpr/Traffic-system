/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.functions;

import java.io.OutputStream;
//import com.fazecast.jSerialComm.*;
import javax.swing.JOptionPane;

/**
 *
 * @author dave
 */
public class microController {

//    private static SerialPort comPort;
//    private static OutputStream out;
//
//    static {
////        try {
////            comPort = SerialPort.getCommPorts()[0];
////            System.out.println(comPort.getDescriptivePortName());
////            comPort.openPort();
////            comPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_SEMI_BLOCKING, 0, 0);
////            out = comPort.getOutputStream();
////        } catch (Exception e) {
////            if (e.getMessage() != null) {
////                JOptionPane.showMessageDialog(null, "micro controller error : " + e.getMessage());
////            }
////        }
//    }
//
//    public static boolean open_east() {
//        try {
//            System.out.println(" microcntrlr open east");
//            out.write('e');
//        } catch (Exception e) {
//            if (e.getMessage() != null) {
//                JOptionPane.showMessageDialog(null, "micro controller error : " + e.getMessage());
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean close_both() {
//        try {
//            System.out.println(" microcntrlr close both");
//            out.write('c');
//        } catch (Exception e) {
//            if (e.getMessage() != null) {
//                JOptionPane.showMessageDialog(null, "micro controller error : " + e.getMessage());
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public static boolean open_north() {
//        try {
//            System.out.println(" microcntrlr open north");
//            out.write('n');
//        } catch (Exception e) {
//            if (e.getMessage() != null) {
//                JOptionPane.showMessageDialog(null, "micro controller error : " + e.getMessage());
//            }
//            return false;
//        }
//        return true;
//    }
//
//    public static void close() {
//        try {
//            if (comPort != null) {
//                if (comPort.isOpen()) {
//                    comPort.closePort();
//                }
//            }
//        } catch (Exception e) {
//            if (e.getMessage() != null) {
//                JOptionPane.showMessageDialog(null, "micro controller error : " + e.getMessage());
//            }
//        }
//    }
//
//    private static int input;
//
//    public void listen() {
//        do {
//            if (comPort != null) {
//                setInput(comPort.bytesAvailable());
//            }
//        } while (!Thread.interrupted());
//    }
//
//    /**
//     * @return the input
//     */
//    public static int getInput() {
//        return input;
//    }
//
//    /**
//     * @param aInput the input to set
//     */
//    public static void setInput(int aInput) {
//        if (aInput != 0) {
//            input = aInput;
//        }
//    }
//
//    public static void clearInput() {
//        input = 0;
//    }
}
