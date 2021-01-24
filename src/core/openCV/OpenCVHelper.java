package core.openCV;

import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import processing.core.PImage;
import processing.core.PVector;

public class OpenCVHelper {
	
	public static Mat getPerspectiveTransformation(ArrayList<PVector> inputPoints, int w, int h) {

		Point[] canonicalPoints = new Point[4];
		canonicalPoints[0] = new Point(w, 0);
		canonicalPoints[1] = new Point(0, 0);
		canonicalPoints[2] = new Point(0, h);
		canonicalPoints[3] = new Point(w, h);

		MatOfPoint2f canonicalMarker = new MatOfPoint2f();
		canonicalMarker.fromArray(canonicalPoints); 

		Point[] points = new Point[4];
		for (int i = 0; i < 4; i++) {
			points[i] = new Point(inputPoints.get(i).x, inputPoints.get(i).y);
		}
		MatOfPoint2f marker = new MatOfPoint2f(points);
		return Imgproc.getPerspectiveTransform(marker, canonicalMarker);
	}

	public static Mat warpPerspective(OpenCV myOpenCV, ArrayList<PVector> inputPoints, int w, int h) {
		Mat transform = getPerspectiveTransformation(inputPoints, w, h);
		Mat unWarpedMarker = new Mat(w, h, CvType.CV_8UC1);    
		Imgproc.warpPerspective(myOpenCV.getColor(), unWarpedMarker, transform, new Size(w, h));
		return unWarpedMarker;
	}
	
	public static PImage getWarpPerspectivePImage(OpenCV myOpenCV, PImage img, ArrayList<PVector> inputPoints, int w, int h) {
		Mat warped = OpenCVHelper.warpPerspective(myOpenCV, inputPoints, w, h);
		myOpenCV.toPImage(warped, img);
		return img;
	}
}
