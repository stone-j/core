package core.variable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ArrayHelper {
	
//	public static void removeAll(List<Integer> list, Integer element) {
//	    int index;
//	    while ((index = list.indexOf(element)) >= 0) {
//	        list.remove(index);
//	    }
//	}
	public static void removeAll(List<?> list, Integer element) {
	    int index;
	    while ((index = list.indexOf(element)) >= 0) {
	        list.remove(index);
	    }
	}
	
	public static Integer[] StringArrayToIntegerArray(String[] stringArray) {
		Integer[] intArray = new Integer[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			intArray[i] = Integer.parseInt(stringArray[i]);
		}
		return intArray;
	}
	
	
	public static String ByteArrayToString(byte[] byteArray, int startPos) {

		String returnString = new String(byteArray); 
		return returnString.substring(startPos);
	}

	public static String ByteArrayToString (byte[] byteArray) {
		return ByteArrayToString(byteArray, 0);
	}

	static public void arrayCopy(Object src, int srcPosition,
			Object dst, int dstPosition,
			int length) {
		System.arraycopy(src, srcPosition, dst, dstPosition, length);
	}

	/**
	 * Convenience method for arraycopy().
	 * Identical to <CODE>arraycopy(src, 0, dst, 0, length);</CODE>
	 * https://github.com/processing/processing/blob/master/core/src/processing/core/PApplet.java
	 */
	static public void arrayCopy(Object src, Object dst, int length) {
		System.arraycopy(src, 0, dst, 0, length);
	}

	/**
	 * Shortcut to copy the entire contents of
	 * the source into the destination array.
	 * Identical to <CODE>arraycopy(src, 0, dst, 0, src.length);</CODE>
	 * https://github.com/processing/processing/blob/master/core/src/processing/core/PApplet.java
	 */
	static public void arrayCopy(Object src, Object dst) {
		System.arraycopy(src, 0, dst, 0, Array.getLength(src));
	}
	
	
	//---------------------------------------------------------------------------
	// SplitStringToIntegerArray
	//---------------------------------------------------------------------------
	public static int[] SplitStringToIntegerArray(String intList) {

		if (intList.equals("")) {
			return new int[] {-1};
		}

		String[] myStrings = intList.split(",");
		int[] myInts = new int[myStrings.length];

		for (int i = 0; i < myStrings.length; i++) {
			myInts[i] = Integer.parseInt(myStrings[i]);
		}

		return myInts;
	}


	//---------------------------------------------------------------------------
	// IntegerArrayToString
	//---------------------------------------------------------------------------
	public static String IntegerArrayToString(int[] myInts, String separator) {

		String myString = "";

		for (int i = 0; i < myInts.length; i++) {
			if (i == 0) {
				myString = Integer.toString(myInts[i]);
			} else {
				myString = myString + separator + Integer.toString(myInts[i]);
			}
		}

		return myString;
	}
	
	
	//---------------------------------------------------------------------------
	// GetArraySorted
	//---------------------------------------------------------------------------
	public static void GetArraySorted(int[][] inputArray, int sortColumnIndex) {
		
		Comparator_IntArray comparator_IntArray = new Comparator_IntArray(sortColumnIndex);
		
		int[][] outputArray = inputArray.clone();
		
		Arrays.sort(inputArray, comparator_IntArray);
		
		inputArray = outputArray.clone();
	}
}
