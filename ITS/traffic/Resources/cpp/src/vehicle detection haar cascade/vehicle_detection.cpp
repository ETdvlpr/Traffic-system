#include <fstream>
#include <iostream>
#include <string>
#include "vehicle_detection.h"

bool debug = true, file_loaded;
char* title;
cv::Mat mask, binary_mask, image, masked_image, gray;
cv::CascadeClassifier cascade;

int main(int argc, char** argv) {
    std::cout << "Using OpenCV " << CV_MAJOR_VERSION << "." << CV_MINOR_VERSION << "." << CV_SUBMINOR_VERSION << std::endl;

    if (argc < 3) {
        std::cout << "Usage " << argv[0] << " cascade.xml video.avi" << std::endl;
        return 0;
    } else if (argc >= 4) {
        debug = atoi(argv[3]);
    }

    cv::VideoCapture capture;
    cv::Mat frame;
    int input_resize_percent = 100;

    title = argv[2];
    cascade.load(argv[1]);
    capture = cv::VideoCapture(title);

    if (!capture.isOpened()) {
        std::cout << "unable to open video" << std::endl;
        return -1;
    }

    if (debug) {
        cvNamedWindow(title, 1);
    }
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

    int key = 0;
    do {
        if (!capture.read(image))
            break;

        cv::resize(image, frame, cv::Size(frame.cols, frame.rows));

        detect(frame);
        key = cvWaitKey(10);

        if (key == KEY_SPACE)
            key = cvWaitKey(0);

        if (key == KEY_ESC)
            break;
    } while (1);

    cvDestroyAllWindows();
    capture.release();
    image.release();
    frame.release();

    return 0;
}

void detect(cv::Mat img) {
    cv::cvtColor(img, gray, CV_BGR2GRAY);
    cv::bitwise_and(gray, binary_mask, masked_image);
    //    CvSeq *object = cvHaarDetectObjects(
    //            &masked_image,
    //            cascade,
    //            storage,
    //            1.2, //1.1,//1.5, //-------------------SCALE FACTOR
    //            1, //2        //------------------MIN NEIGHBOURS
    //            0, //CV_HAAR_DO_CANNY_PRUNING
    //            cvSize(0, 0), //cvSize( 30,30), // ------MINSIZE
    //            cvSize(300, 300) //cvSize(70,70)//cvSize(640,480)  //---------MAXSIZE
    //            );
    //       std::cout << object->total << std::endl;

    std::vector<cv::Rect> cars;
    cascade.detectMultiScale(
            masked_image,
            cars,
            1.1,
            2,
            0 | CV_HAAR_SCALE_IMAGE,
            cv::Size(30, 30)
            );
    std::cout << cars.size() << std::endl;
    if (debug) {
        img += mask;
        for (int i = 0; i < cars.size(); i++) {
            cv::rectangle(img,
                    cars[i],
                    CV_RGB(255, 0, 0), 2, 8, 0);
        }
        cv::imshow(title, img);
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