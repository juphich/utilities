package net.jupic.commons.model;

import java.util.Collection;

/**
 * @생성일 : 2010. 8. 25.
 * @작성자 : juphich
 * <pre>
 *     페이지 객체에 대한 인터페이스를 정의 한다.
 *     
 *     페이지 Interface 에 대한 용어 정의
 *     
 *     page size : 한 페이지에 보여줄 데이터 수
 *     page unit : 한 화면에 보여줄 페이지 단위의 페이지 갯수
 *                 예)  한 화면의 페이지 Navigator 가 다음과 같이 표현 될 경우
 *                       << < 1 2 3 4 5 6 7 8 9 10 > >>
 *                      page unit 은 10이 된다.
 *     data size : 조회된 총 데이터 수
 *     total page size : 구성된 페이지의 총 수
 * </pre>
 */
public interface Page<E> {

	/*
	 * 페이지 요청 파라미터 객체
	 */
	
	/*================*/
	/* data interface */
	/*================*/
	
	/**
	 * @Method Name : getSize
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     총 데이터 수
	 * </pre>
	 * @return total data count
	 */
	int getTotalRows();
	
	/**
	 * @Method Name : getPageSize
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     한 페이지에 보여줄 데이터 수. 페이지의 크기
	 * </pre>
	 * @return page size
	 */
	int getPagesize();
	
	
	/*================*/
	/* page interface */
	/*================*/
	
	/**
	 * @Method Name : getPageunit
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     한 화면에 보여줄 페이지 갯수. 페이지 단위
	 * </pre>
	 * @return page unit number
	 */
	int getPageunit();

	/**
	 * @Method Name : getCurrentPage
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     현재 페이지 번호
	 * </pre>
	 * @return current page number
	 */
	int getCurrentPage();
	
	/**
	 * @Method Name : getPreviousPage
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     이전 페이지 번호
	 * </pre>
	 * @return previous page number
	 */
	int getPreviousPage();
	
	/**
	 * @Method Name : getNextPage
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     다음 페이지 번호
	 * </pre>
	 * @return next page number
	 */
	int getNextPage();
	
	/**
	 * @Method Name : getBeginUnitPage
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     현재 페이지 단위의 시작 페이지 번호.
	 * </pre>
	 * @return begin unit page number
	 */
	int getBeginUnitPage();
	
	/**
	 * @Method Name : getEndUnitPage
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     현재 페이지 단위의 마지막 페이지 번호.
	 * </pre>
	 * @return end unit page number
	 */
	int getEndUnitPage();
	
	/**
	 * @Method Name : getStartOfPreviousPageUnit
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     이전 페이지 단위의 시작 페이지 번호
	 * </pre>
	 * @return start of previous page unit number 
	 */
	int getStartOfPreviousPageUnit();
	
	/**
	 * @Method Name : getStartOfNextPageUnit
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     다음 페이지 단위의 시작 페이지 번호
	 * </pre>
	 * @return start of next page unit number
	 */
	int getStartOfNextPageUnit();
	
	/**
	 * @Method Name : getMaxPage
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     마지막 페이지 번호
	 * </pre>
	 * @return last page number
	 */
	int getLastPage();
	
	
	/**
	 * @Method Name : getList
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     데이터 리스트를 가져온다.
	 * </pre>
	 * @return list
	 */
	Collection<E> getList();
	
	/**
	 * @Method Name : getRequestObject
	 * @작성일 : 2010. 8. 25.
	 * @작성자 : juphich
	 * @description
	 * <pre>
	 *     데이터 목록 조회의 요청 파라미터 객체를 가져온다.
	 * </pre>
	 * @return parameterObj 
	 */
	<P> P getParameterObject();
}
