package core.helper;

import core.logging.ConsoleHelper;

public class PagingHelper {
	
	public ConsoleHelper consoleHelper = new ConsoleHelper();
	
	private int currentPageIndex = 0;
	private int focusItemIndex;
	private int focusItemPageIndex;
	private int itemsPerPage;
	
	public PagingHelper(int myItemsPerPage) {
		this(myItemsPerPage, 0);
	}
	
	public PagingHelper(int myItemsPerPage, int startingPageIndex) {
		itemsPerPage = myItemsPerPage;
		currentPageIndex = startingPageIndex;
	}
	
	public boolean focusItemExistsOnCurrentPage() {
		return(currentPageIndex == focusItemPageIndex);
	}
	
	public int getCurrentPageIndex() {
		return currentPageIndex;
	}
	
	public void setCurrentPageIndex(int n) {
		consoleHelper.PrintMessage("pagingHelper.setCurrentPageIndex: " + n);		
		currentPageIndex = n;
	}
	
	public boolean previousPage() {
		boolean pageChanged = false;
		if (currentPageIndex > 0) {
			pageChanged = true;
			currentPageIndex--;
		}
		return pageChanged;
	}
	
	public boolean nextPage() {
		currentPageIndex++;
		return true;
	}
	
	public void setItemsPerPage(int n) {
		itemsPerPage = n;
	}
	
	public int getItemsPerPage() {
		return itemsPerPage;
	}
	
	public void setFocusItem(int myFocusItemIndex) {
		consoleHelper.PrintMessage("pagingHelper.setFocusItem: " + myFocusItemIndex);		
		focusItemIndex = myFocusItemIndex;
		focusItemPageIndex = currentPageIndex;
	}
	
	public int[] getFocusItem() {
		return new int[] {focusItemPageIndex, focusItemIndex};
	}
	
	public boolean isFocusItem(int index) {
		if (focusItemPageIndex == currentPageIndex && focusItemIndex == index) {
			return true;
		} else {
			return false;
		}
	}
}
