package core.openCV;

import java.util.ArrayList;
import java.util.List;

import core.helper.MathHelper;
import core.variable.ArrayHelper;

public class Threshold {
	
	int
		minValue,
		maxValue,
		defaultValue,
		currentValue,
		maximumMutation;
	
	List<Integer> viableValues = new ArrayList<>();
	//List<Integer> nonViableValues = new ArrayList<>();
	
	boolean viableValueIsKnown = false;
	//boolean nonViableValueIsKnown = false;
	
	public Threshold (int myMinValue, int myMaxValue, int myValue, int myMaximumMutation) {
		this.minValue = myMinValue;
		this.maxValue = myMaxValue;		
		this.defaultValue = myValue;
		this.currentValue = myValue;
		this.maximumMutation = myMaximumMutation;
	}
	
	public void addViableValue() {
		ArrayHelper.removeAll(viableValues, currentValue);
		viableValues.add(currentValue);
		viableValueIsKnown = true;
		
		//nonViableValues.remove(currentValue);
		//ArrayHelper.removeAll(nonViableValues, currentValue);
		//if (nonViableValues.size() == 0) {
		//	nonViableValueIsKnown = false;
		//}
		
	}
	
	public void addNonViableValue() {
		//ArrayHelper.removeAll(nonViableValues, currentValue);
		//nonViableValues.add(currentValue);
		//nonViableValueIsKnown = true;
		
		//viableValues.remove(currentValue);
		ArrayHelper.removeAll(viableValues, currentValue);
		if (viableValues.size() == 0) {
			viableValueIsKnown = false;
		}
	}
	
//	public int getBestPotentialThresholdValue() {
//		
//		ConsoleHelper.PrintMessage("Threshold value is set to " + currentValue);
//		
//		//if we know a good value and a bad value, move farther away from the bad value
//		if (viableValueIsKnown && nonViableValueIsKnown && currentValue != minValue && currentValue != maxValue) {
//
//			int viableAvg = MathHelper.calculateAverage(viableValues);
//			int nonViableAvg = MathHelper.calculateAverage(nonViableValues);
//			
//			if (viableAvg > nonViableAvg) {
//				currentValue = MathHelper.ensureRange(++currentValue, minValue, maxValue);
//			} else if (viableAvg < nonViableAvg) {
//				currentValue = MathHelper.ensureRange(--currentValue, minValue, maxValue);
//			}
//		} else {
//			//otherwise, pick a random value to attempt to find both a good value and a bad value reference points
//			currentValue = MathHelper.getStandardDistributionRandomNumber(minValue, maxValue, defaultValue);
//		}
//		
//		return currentValue;
//	}
	
	
	public int getBestPotentialThresholdValue(boolean reducedMutation) {
		
		//ConsoleHelper.PrintMessage("Threshold value is set to " + currentValue);
		
		if (viableValueIsKnown) {
			currentValue = MathHelper.ensureRange(MathHelper.calculateAverage(viableValues) + MathHelper.getStandardDistributionRandomNumber(0 - maximumMutation / (reducedMutation ? 2 : 1), maximumMutation / (reducedMutation ? 2 : 1), 0), minValue, maxValue);
		} else {
			currentValue = MathHelper.getStandardDistributionRandomNumber(minValue, maxValue, defaultValue);
		}
		
		return currentValue;
	}
	
	
	public int getBestPotentialThresholdValue() {		
		return getBestPotentialThresholdValue(true);
	}
}
