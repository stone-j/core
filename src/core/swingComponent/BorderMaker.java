package core.swingComponent;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class BorderMaker extends CompoundBorder {

	static public final int NONE = 0;
	static public final int RAISEDBEVEL = 1;
	static public final int THICKRAISEDBEVEL = 2;
	
	public BorderMaker(
		int borderType, 
		int outerMarginSize_Top, 
		int outerMarginSize_Left, 
		int outerMarginSize_Botttom, 
		int outerMarginSize_Right, 
		int innerMarginSize_Top, 
		int innerMarginSize_Left, 
		int innerMarginSize_Botttom, 
		int innerMarginSize_Right 
	) {

		super();

		ArrayList<Border> borders = new ArrayList<>();

		borders.add(new EmptyBorder(
			outerMarginSize_Top,
			outerMarginSize_Left,
			outerMarginSize_Botttom,
			outerMarginSize_Right));
		
		switch (borderType) {
		case RAISEDBEVEL:
			borders.add(BorderFactory.createRaisedBevelBorder());
			break;
		case THICKRAISEDBEVEL:
			borders.add(BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                    new Color(220,230,240),
                    new Color(150,160,170),
                    new Color(150,160,170),
                    new Color(150,160,170)
			));
			break;
		}
		
		borders.add(new EmptyBorder(
			innerMarginSize_Top,
			innerMarginSize_Left,
			innerMarginSize_Botttom,
			innerMarginSize_Right));

		CompoundBorder compoundBorder = new CompoundBorder(borders.get(0), null);

		for (int i = 1; i < borders.size(); i++) {

			compoundBorder = new CompoundBorder(compoundBorder, borders.get(i));

		}

		this.outsideBorder = compoundBorder;
	}

	public BorderMaker(int borderType, int outerMarginSize_TopBottom, int outerMarginSize_LeftRight, int innerMarginSize) {
		this(
			borderType, 
			outerMarginSize_TopBottom, 
			outerMarginSize_LeftRight, 
			outerMarginSize_TopBottom, 
			outerMarginSize_LeftRight, 
			innerMarginSize, 
			innerMarginSize, 
			innerMarginSize, 
			innerMarginSize);
	}
	
	public BorderMaker(
		int borderType, 
		int outerMarginSize_Top, 
		int outerMarginSize_Left, 
		int outerMarginSize_Bottom, 
		int outerMarginSize_Right,
		int innerMarginSize
	) {
		this(
			borderType, 
			outerMarginSize_Top, 
			outerMarginSize_Left, 
			outerMarginSize_Bottom, 
			outerMarginSize_Right, 
			innerMarginSize, 
			innerMarginSize, 
			innerMarginSize, 
			innerMarginSize);
	}

	public BorderMaker(int borderType, int outerMarginSize, int innerMarginSize) {

		this(
			borderType, 
			outerMarginSize, 
			outerMarginSize, 
			outerMarginSize, 
			outerMarginSize, 
			innerMarginSize, 
			innerMarginSize, 
			innerMarginSize, 
			innerMarginSize);
	}
}
