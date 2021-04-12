package core.variable;

import java.util.Comparator;

public class Comparator_IntArray implements Comparator<int[]> {
	
	int compareIndex;
	
	public Comparator_IntArray(int myCompareIndex) {
		this.compareIndex = myCompareIndex;
	}
	
	@Override
	public int compare(int[] o1, int[] o2) {
		return Integer.compare(o1[compareIndex], o2[compareIndex]); //the index here is the column you want to sort by
	}

}
