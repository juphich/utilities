package net.jupic.commons.model;

/**
 * @author chang jung pil
 *
 */
public interface PageParameters extends Parameters {

	void setPageIndex(int pageIndex);
	
	void setPageSize(int pageSize);
	
	void setPageUnit(int pageUnit);
	
	int getPageIndex();
	
	int getPageSize();
	
	int getPageUnit();
	
	int getOffset();
}
