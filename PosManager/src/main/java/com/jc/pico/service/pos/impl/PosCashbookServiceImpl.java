package com.jc.pico.service.pos.impl;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jc.pico.bean.SvcCashbook;
import com.jc.pico.bean.SvcCashbookExample;
import com.jc.pico.mapper.SvcCashbookMapper;
import com.jc.pico.service.pos.PosCashbookService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosCashBookInfo;
import com.jc.pico.utils.bean.PosCashbookSave;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.pos.PosCashbookMapper;

@Service
public class PosCashbookServiceImpl implements PosCashbookService {

	@Autowired
	private PosCashbookMapper posCashbookMapper;

	@Autowired
	private SvcCashbookMapper svcCashbookMapper;

	@Autowired
	private PosUtil posUtil;

	@Override
	public List<PosCashBookInfo> getCashBookInfo(SingleMap param) {
		return posCashbookMapper.selectCashbookInfoList(param);
	}

	@Override
	public long saveCashBook(PosCashbookSave param, Map<String, Object> posInfo) {

		SvcCashbook record = toSvcCashBook(param, new SingleMap(posInfo));

		// 포스에서 올라온 정보는 storeId, saleDt, ordinal 조합으로 키가 형성됨
		// 해서 해당으로 조회하여 레코드를 찾고 업으면 신구, 있으면 갱신 처리 함
		SvcCashbook oldRecord = getSvcCashBook(record.getStoreId(), record.getSaleDt(), record.getOrdinal());

		if (oldRecord == null) {
			// 신규
			svcCashbookMapper.insertSelective(record);

		} else {
			// 갱신
			record.setId(oldRecord.getId());
			svcCashbookMapper.updateByPrimaryKeySelective(record);
		}

		return record.getId();
	}

	private SvcCashbook getSvcCashBook(Long storeId, Date saleDt, Integer ordinal) {
		SvcCashbookExample example = new SvcCashbookExample();
		example.createCriteria() // 조건
				.andStoreIdEqualTo(storeId) // 상점
				.andSaleDtEqualTo(saleDt) // 개점일
				.andOrdinalEqualTo(ordinal); // 일련번호 (포스에서 부여)
		List<SvcCashbook> list = svcCashbookMapper.selectByExample(example);
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 포스로 부터 받은 PosCashbookSave 을 서버에서 사용하는 SvcCashbook 으로 변환한다
	 * 
	 * @param param
	 * @param posInfo
	 * @return
	 */
	private SvcCashbook toSvcCashBook(PosCashbookSave param, SingleMap posInfo) {

		// 갱신자 정보가 있으면 갱신으로, 없으면 생성자 정보 사용
		final Long staffId = StringUtils.isEmpty(param.getCdEmployeeUpdate())
				? posUtil.parseLong(param.getCdEmployeeUpdate(), null)
				: posUtil.parseLong(param.getCdEmployeeInsert(), null);
		final Date saleDt = posUtil.parseDate(param.getYmdSale(), null);

		if (saleDt == null) {
			throw new InvalidParameterException("YMD_SALE is empty or invalid format.");
		}

		SvcCashbook result = new SvcCashbook();
		result.setBrandId(posInfo.getLong(PosUtil.BASE_INFO_BRAND_ID, null));
		result.setStoreId(posUtil.parseLong(param.getCdStore(), null));
		result.setSaleDt(saleDt);
		result.setOrdinal(param.getSeq());
		result.setStaffId(staffId);
		result.setAccountTp(posUtil.getBaseCode("830", param.getCdAccount())); // 계정 과목
		result.setIsOut(param.getCdCashbookType() != 1); // 출납 유형 1=입금, 2=출금
		result.setAmount(param.getAmtCashbook());
		result.setMemo(param.getMemo());

		return result;
	}

}
