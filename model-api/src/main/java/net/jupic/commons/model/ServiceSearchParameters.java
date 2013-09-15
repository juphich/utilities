package net.jupic.commons.model;

import java.util.Date;

/**
 * @author chang jung pil
 *
 */
public class ServiceSearchParameters implements PageParameters, SearchParameters {

	private static final long serialVersionUID = -8524472978309286260L;
	
	private String searchText;
	private String searchType;
	private String orderBy;
	
	/*@DateTimeFormat(pattern="yyyy-MM-dd")*/
	private Date fromDate;
	
	/*@DateTimeFormat(pattern="yyyy-MM-dd")*/
	private Date toDate;
	
	private String message;

	
	private int pageIndex = 1;
	private int pageSize  = 10;
	
	
	/**
	 * @param searchText
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * @param searchType
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * @return
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param pageIndex
	 */
	@Override
	public final void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @param pageSize
	 */
	@Override
	public final void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * @return
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @return
	 */
	@Override
	public int getOffset() {
		return (pageIndex - 1) * pageSize;
	}

	/**
	 * @param pageUnit
	 */
	@Override
	public void setPageUnit(int pageUnit) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 */
	@Override
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return
	 */
	@Override
	public int getPageUnit() {
		return 10;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
}