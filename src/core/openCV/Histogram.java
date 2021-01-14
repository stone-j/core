package core.openCV;

import org.opencv.core.Mat;

public class Histogram {
	private Mat mat;
	//private PApplet parent;
	
	public Histogram(Mat mat){
		this.mat = mat;
		//this.parent = parent;
	}
	
//	public void draw(int x, int y, int w, int h) {
//		parent.pushMatrix();
//		parent.translate(x, y);
//		int numBins = mat.height();
//		float binWidth = w/(float)numBins;
//
//		for (int i  = 0; i < numBins; i++) {
//			float v = (float)mat.get(i, 0)[0];
//			parent.rect(i*binWidth, h, binWidth, -h*v);
//		}
//		parent.popMatrix();
//	}
	
	public Mat getMat(){
		return mat;
	}
}
