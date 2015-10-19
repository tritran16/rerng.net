package net.rerng.models;
public class Page {
	private int page;
	private int count;
	private int pageCount;
	
	public int GetPage() {
		return page;
	}
	
	public void SetPage(int page) {
		this.page = page;
	}
	
	public int GetCount() {
		return count;
	}
	
	public void SetCount(int count) {
		this.count = count;
	}
	
	public int GetPageCount() {
		return pageCount;
	}
	
	public void SetPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
}

