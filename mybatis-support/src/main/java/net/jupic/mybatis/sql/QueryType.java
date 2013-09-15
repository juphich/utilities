package net.jupic.mybatis.sql;

/**
 * @author chang jung pil
 *
 */
public enum QueryType {
	UNKNOWN(0), COUNTING(1), PAGING(2);

	private String prefixOfId;
	private String suffixOfId;
	
	private QueryType(int value) {
		switch (value) {
		case 1 : 
			this.prefixOfId = "_";
			this.suffixOfId = ".counting";
			
			break;
		case 2 :
			this.prefixOfId = "_";
			this.suffixOfId = ".paginated";
			
			break;
		default :
			break;
		}
	}

	/**
	 * @return the prefixOfId
	 */
	public String getPrefixOfId() {
		return prefixOfId;
	}

	/**
	 * @return the suffixOfId
	 */
	public String getSuffixOfId() {
		return suffixOfId;
	}
}