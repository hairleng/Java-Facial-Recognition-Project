package Recognizer;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FaceMatchUtils {
    int i = 0;
    FaceMatchUtils(){};
        // use gray histogram to calculate image similarity
    public static double histogramMatch(Mat faceMat, String testFace) {
        Mat testFaceMat = Imgcodecs.imread(testFace,0);
        // histogram equalization
        faceMat.convertTo(faceMat, CvType.CV_8UC1);
        testFaceMat.convertTo(testFaceMat, CvType.CV_8UC1);
        Imgproc.equalizeHist(faceMat, faceMat);
        Imgproc.equalizeHist(testFaceMat, testFaceMat);
        // convert faceMat to the type of Cv_32F
        faceMat.convertTo(faceMat, CvType.CV_32F);
        testFaceMat.convertTo(testFaceMat, CvType.CV_32F);
        // histogram match
        double similarity = Imgproc.compareHist(faceMat, testFaceMat, Imgproc.CV_COMP_CORREL);
        return similarity;
    }
}