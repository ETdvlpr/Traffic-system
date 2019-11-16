#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <fstream>
#include <string>

int main(int argc, char** argv) {
    if (argc < 2) {
        std::cout << "Usage is " << argv[0] << " list_of_files" << std::endl;
        return 0;
    }
    std::ifstream file(argv[1]);
    if (file.is_open()) {
        std::string line;
        std::vector <std::string> lines;
        while (std::getline(file, line)) {
            lines.push_back(line);
        }
        file.close();
        cv::Mat image, newImage;
        for (int x = 0; x < lines.size(); x++) {
            image = cv::imread(lines[x], 0);
            if (!image.empty()) {
                newImage = cv::Mat::zeros(500, 500, image.type());
                cv::resize(image, newImage, cv::Size(500, 500));
                cv::imwrite(lines[x], newImage);
            }
        }
    }
    return 0;
}
