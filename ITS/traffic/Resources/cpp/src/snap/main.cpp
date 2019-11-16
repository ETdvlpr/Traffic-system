#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <string>
#include <sys/time.h>


using namespace cv;
using namespace std;

int main(int argc, char** argv) {

    if (argc < 2) {
        cout << "Usage is " << argv[0] << " video_title_and_location" << endl;
        return 0;
    }
    bool successful_read;
    VideoCapture cap;
    Mat image;
    cap = VideoCapture(argv[1]);

    if (!cap.isOpened()) {
        cout << " Error : cant open video file " << argv[1] << endl;
        return -1;
    }

    for (int i = 0; i < 30; i++) {
        successful_read = cap.read(image); // Read the file

        if (!successful_read) // Check for invalid input
        {
            cout << "Could not open or find the image" << std::endl;
            return -1;
        }
    }
    imwrite("snap.jpg", image);
    cap.release();
    destroyAllWindows();

    return 0;
}
