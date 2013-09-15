package net.jupic.commons.model;

public abstract class AbstractPageParameters implements PageParameters {

	private static final long serialVersionUID = 647937191946578082L;
	
	private int pageIndex = 1;
	private int pageSize  = 10;

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
}
