package net.jupic.commons.model;

import java.util.Collection;
import java.util.Collections;

/**
 * @author chang jung pil
 *
 */
public abstract class AbstractPage<E> implements Page<E> {

	private int totalRows;
	private int pagesize = 10;
	private int pageunit = 10;
	private int pageIndex;
	
	/**
     * 현재 화면에서 페이지 네비게이션을 시작할 페이지 번호
     */
    private int beginUnitPage;

    /**
     * 현재 화면에서 페이지 네비게이션에 마지막으로 보여줄 번호
     */
    private int endUnitPage;
	
	private Collection<E> list;
	
	private Object parameters;
	
	public <P> AbstractPage(int pageIndex,
							int totalRows,
							int pagesize,
							int pageunit,
							Collection<E> list,
							P parameters) {
		
		if (pageunit <= 0 || pagesize <= 0) {
            throw new IllegalArgumentException("Page unit or page size should be over 0.");
        }
		
		this.pageIndex = pageIndex;
		this.pagesize = pagesize;
		this.pageunit = pageunit;
		this.totalRows = totalRows;
		this.list = list;
		this.parameters = parameters;
		
		this.beginUnitPage = ((pageIndex - 1) / pageunit) * pageunit + 1;
        this.endUnitPage = beginUnitPage + (pageunit - 1);
	}
	
	/**
	 * @param totalRows
	 * @param list
	 * @param parameters
	 */
	public <P extends PageParameters> AbstractPage(int totalRows, Collection<E> list, P parameters) {
		this(parameters.getPageIndex(), 
			 totalRows,
			 parameters.getPageSize(),
			 parameters.getPageUnit(),
			 list,
			 parameters);
	}

	/**
	 * @return
	 */
	@Override
	public int getTotalRows() {
		return totalRows;
	}

	/**
	 * @return
	 */
	@Override
	public int getPagesize() {
		return pagesize;
	}

	/**
	 * @return
	 */
	@Override
	public int getPageunit() {
		return pageunit;
	}

	/**
	 * @return
	 */
	@Override
	public int getCurrentPage() {
		return pageIndex;
	}

	/**
	 * Desc    : 이전 페이지 번호를 반환한다. 
	 * @return : int - 이전 페이지 번호
	 */
	@Override
	public int getPreviousPage() {
		return (pageIndex - 1) <= 0 ? 1 : (pageIndex - 1);
	}

	/**
	 * Desc    : 다음 페이지 번호를 반환한다.
	 * @return : int - 다음 페이지 번호
	 */
	@Override
	public int getNextPage() {
		return (pageIndex + 1) >= this.getLastPage() ? this.getLastPage() : (pageIndex + 1);
	}

	/**
	 * Desc    : 현재 화면에서 페이지 네비게이션에 보여줄 시작 페이지 번호를 반환한다. 
	 * @return : int - 페이지 네비게이션의 첫 페이지 번호
	 */
	@Override
	public int getBeginUnitPage() {
		return this.beginUnitPage;
	}

	/**
	 * Desc    : 현재 화면에서 페이지 네비게이션에 보여줄 마지막 페이지 번호를 반환한다.
	 * @return : int - 페이지 네비게이션의 마지막 페이지 번호
	 */
	@Override
	public int getEndUnitPage() {
		return this.endUnitPage;
	}

	/**
	 * Desc    : 이전 페이지 단위의 시작번호를 반환한다.
	 * @return : int - 이전 페이지 단위의 시작번호
	 */
	@Override
	public int getStartOfPreviousPageUnit() {
		return ((beginUnitPage - 1) < 1) ? 1 : (beginUnitPage - 1);
	}

	/**
	 * Desc    : 다음 페이지 단위의 시작 번호를 반환한다. 
	 * @return : int - 다음 페이지 단위의 시작번호
	 */
	@Override
	public int getStartOfNextPageUnit() {
		return ((endUnitPage + 1) > this.getLastPage()) ? this.getLastPage() : (endUnitPage + 1);
	}

	/**
	 * Desc    : 마지막 페이지 번호를 반환한다.
	 * @return : int - 마지막 페이지 번호
	 */
	@Override
	public int getLastPage() {
		return (totalRows - 1) / pagesize + 1;
	}

	/**
	 * @return
	 */
	@Override
	public Collection<E> getList() {
		return Collections.unmodifiableCollection(list);
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <P> P getParameterObject() {
		return (P) parameters;
	}
}