package core.imaging;

import java.awt.Image;
import processing.core.PImage;

public class PImageWithAlpha extends PImage {

	//------------------------------------------------------------
	//CONSTRUCTOR
	//------------------------------------------------------------
	public PImageWithAlpha(int width, int height) {
		super(width, height);
		//have to do this because the PImage constructor sets format to RGB, which eliminates transparency
		this.format = ARGB;
	}

	public PImageWithAlpha(Image img) {
		super(img);
		//have to do this because the PImage constructor sets format to RGB, which eliminates transparency
		this.format = ARGB;
	}

	//---------------------------------------------------------------------------
	//get
	//---------------------------------------------------------------------------
	@Override
	public synchronized PImageWithAlpha get() {
		return new PImageWithAlpha((Image)super.get().getNative());
	}

	//---------------------------------------------------------------------------
	//get
	//---------------------------------------------------------------------------
	@Override
	public synchronized PImageWithAlpha get(int x, int y, int w, int h) {
		return new PImageWithAlpha((Image)super.get(x, y, w, h).getNative());
	}
}
