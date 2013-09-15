package net.jupic.commons.model;

/**
 * @author chang jung pil
 *
 */
public interface SearchParameters extends Parameters {

	void setSearchText(String searchText);

	void setSearchType(String searchType);
	
	String getSearchText();
	
	String getSearchType();
}
