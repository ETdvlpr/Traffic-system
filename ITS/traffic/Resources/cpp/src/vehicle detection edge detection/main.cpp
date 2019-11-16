#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <fstream>
#include <string>
#include "Blob.h"


using namespace cv;
using namespace std;

bool debug, file_loaded;
string title;

VideoCapture capture;
Mat mask, binary_mask, background_img;
vector< vector<Point> > contours;
int car_count;
int boundingArea, aspectRatioMax, aspectRatioMin, minWidth, minHeight, minDiagonalSize, contourByArea, cannyMinThresh;

void mark(Blob b, Mat i);
void check_blobs_for_cars(Mat frame);
void load_settings_from_file(char* file_name);

int main(int argc, char** argv) {
    std::cout << "Using OpenCV " << CV_MAJOR_VERSION << "." << CV_MINOR_VERSION << "." << CV_SUBMINOR_VERSION << std::endl;

    if (argc < 3) {
        std::cout << "Usage " << argv[0] << " background_image video.avi" << std::endl;
        return 0;
    } else if (argc >= 4) {
        debug = atoi(argv[3]) == 1;
    }

    cv::Mat frame, image;
    int input_resize_percent = 50;

    title = argv[2];
    capture = cv::VideoCapture(title);

    if (!capture.isOpened()) {
        std::cout << "unable to open video" << std::endl;
        return -1;
    }

    if (debug) {
        cvNamedWindow(argv[2], 1);
    }
    title = argv[2];
    capture.read(image);
    frame = cv::Mat::zeros((int) ((image.rows * input_resize_percent) / 100), (int) (image.cols * input_resize_percent / 100), image.type());
    mask = cv::Mat::zeros(image.rows, image.cols, image.type());

    if (argc == 5) {
        if (argv[4][0] != '0') {
            load_settings_from_file(argv[4]);
        }
    }
    cv::resize(mask, mask, cv::Size(frame.cols, frame.rows));
    cvtColor(mask, binary_mask, CV_BGR2GRAY);
    if (!file_loaded) {
        binary_mask = cv::Mat::ones(frame.rows, frame.cols, frame.type())*255;
        cvtColor(binary_mask, binary_mask, CV_BGR2GRAY);
    }
    threshold(binary_mask, binary_mask, 10, 255, CV_THRESH_BINARY);



    boundingArea = 400;
    aspectRatioMax = 40;
    aspectRatioMin = 2;
    minWidth = 30;
    minHeight = 30;
    minDiagonalSize = 60;
    contourByArea = 50;
    cannyMinThresh = 30;

    Mat structuringElement5x5 = getStructuringElement(MORPH_RECT, Size(5, 5));
    Mat structuringElement7x7 = getStructuringElement(MORPH_RECT, Size(7, 7));



    if (argv[1][0] != '0') {
        background_img = imread(argv[1]);
        cvtColor(background_img, background_img, CV_BGR2GRAY);
        threshold(background_img, background_img, 10, 255, CV_THRESH_BINARY);
        cv::resize(background_img, background_img, cv::Size(frame.cols, frame.rows));
        cv::resize(mask, mask, cv::Size(frame.cols, frame.rows));
        bitwise_and(background_img, binary_mask, background_img);
    }
    while (1) {
        capture.read(image);
        cv::resize(image, frame, cv::Size(frame.cols, frame.rows));

        GaussianBlur(frame, image, Size(5, 5), 4, 4);
        cvtColor(image, image, CV_BGR2GRAY);
        Canny(image, image, cannyMinThresh, 3 * cannyMinThresh, 3);
        bitwise_and(image, binary_mask, image);
        if (!background_img.empty())
            image -= background_img;

        dilate(image, image, structuringElement5x5);
        dilate(image, image, structuringElement7x7);
        erode(image, image, structuringElement7x7);

        dilate(image, image, structuringElement5x5);
        dilate(image, image, structuringElement5x5);
        erode(image, image, structuringElement5x5);

        findContours(image, contours, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
        for (unsigned int i = 0; i < contours.size(); i++) {
            convexHull(contours[i], contours[i]);
        }

        check_blobs_for_cars(frame);
        cout << car_count << endl;
        car_count = 0;
    }
    capture.release();
    destroyAllWindows();

    return 0;
}

void mark(Blob b, Mat i) {
    rectangle(i, b.currentBoundingRect, Scalar(0, 0, 255), 2);
    //    int intFontFace = CV_FONT_HERSHEY_SIMPLEX;
    //    double dblFontScale = b.dblCurrentDiagonalSize / 60.0;
    //    int intFontThickness = (int) round(dblFontScale * 1.0);
    //
    //    putText(i, to_string(car_count), b.centerPositions.back(), intFontFace, dblFontScale, Scalar(0, 255, 0), intFontThickness);

}

void check_blobs_for_cars(Mat frame) {
    for (int i = 0; i < contours.size(); i++) {
        Blob possibleBlob(contours[i]);
        if (possibleBlob.currentBoundingRect.area() > boundingArea &&
                possibleBlob.dblCurrentAspectRatio > aspectRatioMin / 10.0 &&
                possibleBlob.dblCurrentAspectRatio < aspectRatioMax / 10.0 &&
                possibleBlob.currentBoundingRect.width > minWidth &&
                possibleBlob.currentBoundingRect.height > minHeight &&
                possibleBlob.dblCurrentDiagonalSize > minDiagonalSize &&
                (cv::contourArea(possibleBlob.currentContour) / (double) possibleBlob.currentBoundingRect.area()) > contourByArea / 100.0) {
            car_count++;
            if (debug)
                mark(possibleBlob, frame);
        }
    }
    if (debug) {
        frame += mask;
        imshow(title, frame);
        if (waitKey(20) == 27) {
            destroyAllWindows();
            capture.release();
            exit(0);
        }
    }
}

void load_settings_from_file(char* file_name) {
    std::ifstream file(file_name);
    if (file.is_open()) {
        std::string line;
        std::vector <std::string> lines;
        while (std::getline(file, line)) {
            lines.push_back(line);
        }
        file.close();
        //        //output points for debugging
        //        for (int x = 0; x < lines.size(); x++)
        //            std::cout << lines[x] << std::endl;


        const int size = stoi(lines[0]);
        cv::Point focus[1][size];
        for (int i = 0; i < 2 * size; i += 2) {
            focus[0][i / 2] = cv::Point(stoi(lines[i + 1]), stoi(lines[i + 2]));
            //            std::cout << stoi(lines[i + 1]) << " , " << stoi(lines[i + 2]) << std::endl;
        }
        const cv::Point * ppt[1] = {focus[0]};
        int npt[] = {size};

        fillPoly(mask, ppt, npt, 1, cv::Scalar(0, 40, 0), 8);
        file_loaded = true;
    }
}
