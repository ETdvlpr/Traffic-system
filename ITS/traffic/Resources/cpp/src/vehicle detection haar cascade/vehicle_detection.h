/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   vehicle_detection.h
 * Author: dave
 *
 * Created on March 23, 2017, 1:11 PM
 */
#include <opencv2/opencv.hpp>

#ifndef VEHICLE_DETECTION_H
#define VEHICLE_DETECTION_H

const int KEY_SPACE = 32;
const int KEY_ESC = 27;

void detect(cv::Mat img);
void load_settings_from_file(char * file_name);

//int run(int argc, char** argv);

#endif /* VEHICLE_DETECTION_H */

