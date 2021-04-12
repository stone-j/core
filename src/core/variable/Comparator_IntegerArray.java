package core.variable;

import java.util.Comparator;

public class Comparator_IntegerArray implements Comparator<Integer[]> {
	
	int compareIndex;
	
	public Comparator_IntegerArray(int myCompareIndex) {
		this.compareIndex = myCompareIndex;
	}
	
	@Override
	public int compare(Integer[] o1, Integer[] o2) {
		return o1[compareIndex].compareTo(o2[compareIndex]); //the index here is the column you want to sort by
	}

}
