package demo.data;

public class PageAttr {
	
	private long total;
	private int count;
	private int pageNumber;
	private int pageSize;
	
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public long getTotalPages() {
		return pageSize > 0 ? (total + pageSize - 1) / pageSize : 0;
	}
	
	public int getNextPageNumber() {
		return getPageNumber() < getTotalPages() ? getPageNumber() + 1 : getPageNumber();
	}
	
	public int getPrevPageNumber() {
		if ( getPageNumber() == 0 ) return 0;
		return getPageNumber() > 1 ? getPageNumber() - 1 : 1;
	}
	
	public int getLastPageNumber() {
		return (int) getTotalPages();
	}
	
	public int getStartCount() {
		return (getPageNumber() * getPageSize()) - (getPageSize() - 1);
	}


}
