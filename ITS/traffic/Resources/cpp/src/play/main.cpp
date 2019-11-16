#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <fstream>
#include <string>

int main(int argc, char** argv) {
    if (argc < 2) {
        std::cout << "Usage is " << argv[0] << " video_file" << std::endl;
        return 0;
    }
    cv::namedWindow(argv[1], cv::WINDOW_AUTOSIZE);
    cv::VideoCapture cap = cv::VideoCapture(argv[1]);
//    cv::VideoCapture cap = cv::VideoCapture("http://192.168.43.1:8080/video?x.mjpeg");
    cv::Mat image;
    if(!cap.isOpened())
    {
        std::cout << "can't open video" << std::endl;
        return -1;
    }
    while(1){
        if(!cap.read(image))
            break;
        cv::imshow(argv[1], image);
        if(cv::waitKey(20) == 27)
            break;
    }
    cap.release();
    cv::destroyAllWindows();

    return 0;
}
