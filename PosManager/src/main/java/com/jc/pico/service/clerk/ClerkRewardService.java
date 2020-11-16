package com.jc.pico.service.clerk;

import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.SingleMap;

public interface ClerkRewardService {

	SingleMap getCouponDetail(SingleMap param) throws RequestResolveException, InvalidJsonException;

}
