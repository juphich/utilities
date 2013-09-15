package net.jupic.mybatis.model;

import java.util.Collection;

import net.jupic.commons.model.AbstractPage;
import net.jupic.commons.model.PageParameters;


/**
 * @author chang jung pil
 *
 */
public class DefaultPage<E> extends AbstractPage<E> {

	public <P extends PageParameters> DefaultPage(int totalRows, Collection<E> list, P parameters) {
		super(totalRows, list, parameters);
	}
	
	/**
	 * @param pageIndex
	 * @param totalRows
	 * @param pagesize
	 * @param pageunit
	 * @param list
	 * @param parameters
	 */
	public <P> DefaultPage(int pageIndex, 
						   int totalRows,
						   int pagesize,
						   int pageunit, 
						   Collection<E> list, 
						   P parameters) {
		
		super(pageIndex, totalRows, pagesize, pageunit, list, parameters);
	}

}
