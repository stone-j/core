package core.helper;

import java.util.List;
import java.util.Random;

public class MathHelper {
	
	//---------------------------------------------------------------------------
	// removeLeastSignificantBits
	//---------------------------------------------------------------------------
	public static int removeLeastSignificantBits(int value, int numberOfBitsToRemove) {
		value = value >> numberOfBitsToRemove;
		value = value << numberOfBitsToRemove;
		
		return value;
	}
		
		
	//---------------------------------------------------------------------------
	// isSimilarValue
	//---------------------------------------------------------------------------
	public static boolean isSimilarValue(float value1, float value2, float tolerancePercentage) {
		boolean isSimilar = false;
		float range = value1 * tolerancePercentage / 100.0f;
		if ((value1 - range <= value2) && (value1 + range >= value2)) {
			isSimilar = true;
		}
		return isSimilar;
	}
	
		
	//---------------------------------------------------------------------------
	// getMinimumValue
	//---------------------------------------------------------------------------
	public static int getMinimumValue(int value1, int value2) {
		return value1 > value2 ? value2 : value1;
	}
	
	
	//---------------------------------------------------------------------------
	// getMaximumValue
	//---------------------------------------------------------------------------
	public static int getMaximumValue(int value1, int value2) {
		return value1 > value2 ? value1 : value2;
	}
		
		
	//---------------------------------------------------------------------------
	// calculateAverage
	//---------------------------------------------------------------------------
	public static int calculateAverage(List <Integer> values) {
		Integer sum = 0;
		if(!values.isEmpty()) {
			for (Integer value : values) {
				sum += value;
			}
			return sum / values.size();
		}
		return sum;
	}
	
	//---------------------------------------------------------------------------
	// SetNonZeroValue
	//---------------------------------------------------------------------------
	public static int SetNonZeroValue(int myValue) {

		if (myValue == 0) {
			myValue = 1;
		}

		return myValue;
	}


	//---------------------------------------------------------------------------
	// ensureRange
	//---------------------------------------------------------------------------
	public static int ensureRange(int value, int min, int max) {
		return Math.min(Math.max(value, min), max);
	}
	
	//---------------------------------------------------------------------------
	// ensureRange
	//---------------------------------------------------------------------------
	public static float ensureRange(float value, float min, float max) {
		return Math.min(Math.max(value, min), max);
	}
	
		
	//---------------------------------------------------------------------------
	// getStandardDistributionRandomNumber
	//---------------------------------------------------------------------------
	public static int getStandardDistributionRandomNumber(int min, int max, int defaultValue) {
		/*
		this method assumes the default value is either the average of min and max (center point),
		or equal to the min or max. i.e.
		(0 to 100, 0 is default),
		(-50 to +50, 0 is default),
		(0 to 100, 50 is default),
		(0 to 200, 200 is default)
		*/
		
		Random rng = new Random();
		
		int offset = 0 - defaultValue;
		int offsetMin = min + offset;
		int offsetMax = max + offset;
		
		double result = 0;
		boolean isPositive;
		
		if (defaultValue == min) {
			isPositive = true;
			
		} else if (defaultValue == max) {
			isPositive = false;
		} else {
			isPositive = rng.nextDouble() >= 0.5 ? true : false;
		}
		
		result = 1.0d - (Math.pow(rng.nextDouble(), 1.0 / 3.0)); // this uses math.pow to get the nth root, where n is 3.0
		
		if (!isPositive) {
			result *= -1;			
		}
		
		result *= offsetMax;
		result += defaultValue;
		
		return (int)result;
	}
		
			
	//---------------------------------------------------------------------------
	// getStandardDistributionRandomNumber
	//---------------------------------------------------------------------------
	public static int getStandardDistributionRandomNumber(int min, int max, int defaultValue, int seed) {
		/*
		this method assumes the default value is either the average of min and max (center point),
		or equal to the min or max. i.e.
		(0 to 100, 0 is default),
		(-50 to +50, 0 is default),
		(0 to 100, 50 is default),
		(0 to 200, 200 is default)
		*/
		
		Random rng = new Random(seed);
		
		int offset = 0 - defaultValue;
		int offsetMin = min + offset;
		int offsetMax = max + offset;
		
		double result = 0;
		boolean isPositive;
		
		if (defaultValue == min) {
			isPositive = true;
			
		} else if (defaultValue == max) {
			isPositive = false;
		} else {
			isPositive = rng.nextDouble() >= 0.5 ? true : false;
		}
		
		result = 1.0d - (Math.pow(rng.nextDouble(), 1.0 / 3.0)); // this uses math.pow to get the nth root, where n is 3.0
		
		if (!isPositive) {
			result *= -1;			
		}
		
		result *= offsetMax;
		result += defaultValue;
		
		return (int)result;
	}
}
