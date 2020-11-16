package com.jc.pico.utils.customMapper.store;

import com.jc.pico.utils.bean.SingleMap;

/**
 * 통계용 날짜 조회 테이블
 * 
 * @author hyo
 *
 */
public interface StoreCalendarMapper {

	/**
	 * 
	 * start, end 기간 중 limit, offset 범위에 해당하는 날짜의 시작일 종료일을 반환
	 * 
	 * @param param
	 *            startDate
	 *            endDate
	 *            limit
	 *            offset
	 * @return
	 * 		startDate
	 *         endDate
	 */
	SingleMap selectPaginationDaily(SingleMap param);

	/**
	 * 
	 * start, end 기간 중 limit, offset 범위에 해당하는 주차의 시작일 종료일을 반환
	 * 
	 * @param param
	 *            startDate
	 *            endDate
	 *            limit
	 *            offset
	 * @return
	 * 		startDate
	 *         endDate
	 */
	SingleMap selectPaginationWeekly(SingleMap param);

	/**
	 * 
	 * start, end 기간 중 limit, offset 범위에 해당하는 월의 시작일 종료일을 반환
	 * 
	 * @param param
	 *            startDate
	 *            endDate
	 *            limit
	 *            offset
	 * @return
	 * 		startDate
	 *         endDate
	 */
	SingleMap selectPaginationMonthly(SingleMap param);

}
