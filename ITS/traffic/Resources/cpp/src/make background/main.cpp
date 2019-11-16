#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <string>

int main(int argc, char** argv) {
    if (argc < 2) {
        std::cout << "usage is " << argv[0] << " video_title output_title" << std::endl;
        return -1;
    }
    cv::Mat structuringElement5x5 = cv::getStructuringElement(cv::MORPH_RECT, cv::Size(5, 5));
    cv::VideoCapture cap = cv::VideoCapture(argv[1]);
    cv::Mat background_img;
    cap.read(background_img);
    if (!background_img.empty()) {
        blur(background_img, background_img, cv::Size(5, 5));
        cvtColor(background_img, background_img, CV_BGR2GRAY);
        Canny(background_img, background_img, 30, 100);
        cv::dilate(background_img, background_img, structuringElement5x5);
        std::cout << "about to write" << std::endl;
        cv::imwrite(argv[2], background_img);
    }
    return 0;
}
