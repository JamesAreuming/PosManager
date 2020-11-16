package com.jc.pico.utils.bean;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.util.StringUtils;

//import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.Nullable;

public class SingleMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public SingleMap() {
		super();
	}

	public SingleMap(java.util.Map<? extends String, ? extends Object> map) {
		super(map);
	}

	@Nullable
	public Integer getInt(String key, Integer defaultValue) {
		Object value = get(key);
		if (isEmpty(value)) {
			return defaultValue;
		}

		if (value instanceof Number) {
			return ((Number) value).intValue();
		}

		try {
			return Integer.parseInt(value.toString());
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Type miss match. " + key + " must be int.");
		}
	}

	public int getInt(String key) {
		Integer value = getInt(key, null);
		if (value == null) {
			throw new InvalidParameterException(key + " is empty.");
		}
		return value;
	}

	@Nullable
	public Long getLong(String key, Long defaultValue) {
		Object value = get(key);
		if (isEmpty(value)) {
			return defaultValue;
		}

		if (value instanceof Number) {
			return ((Number) value).longValue();
		}

		try {
			return Long.parseLong(value.toString());
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Type miss match. " + key + " must be long.");
		}
	}

	public long getLong(String key) {
		Long value = getLong(key, null);
		if (value == null) {
			throw new InvalidParameterException(key + " is empty.");
		}
		return value;
	}

	@Nullable
	public Double getDouble(String key, Double defaultValue) {
		Object value = get(key);
		if (isEmpty(value)) {
			return defaultValue;
		}

		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}

		try {
			return Double.parseDouble(value.toString());
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Type miss match. " + key + " must be double.");
		}
	}

	public double getDouble(String key) {
		Double value = getDouble(key, null);
		if (value == null) {
			throw new InvalidParameterException(key + " is empty.");
		}
		return value;
	}

	public boolean equalsValue(String key, Object value) {
		return Objects.equals(get(key), value);
	}

	public void putIfEmpty(String key, Object value) {
		if (StringUtils.isEmpty(get(key))) {
			put(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	public SingleMap getSingleMap(String key) {
		return new SingleMap((java.util.Map<? extends String, ? extends Object>) get(key));
	}

	public String getString(String key) {
		Object value = get(key);
		if (value == null) {
			throw new InvalidParameterException(key + " is empty.");
		}
		return value.toString();
	}

	public String getString(String key, String defaultValue) {
		Object value = get(key);
		if (isEmpty(value)) {
			return defaultValue;
		}
		return value.toString();
	}

	public boolean hasValue(String key) {
		return get(key) != null;
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		Object value = get(key);
		if (isEmpty(value)) {
			return defaultValue;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}

		// 문자열인 경우 "true"가 아니면 모두 false로 처리 
		return Boolean.parseBoolean(value.toString());
	}

	public Short getShort(String key, Short defaultValue) {
		Object value = get(key);
		if (isEmpty(value)) {
			return defaultValue;
		}

		if (value instanceof Number) {
			return ((Number) value).shortValue();
		}

		try {
			return Short.parseShort(value.toString());
		} catch (NumberFormatException e) {
			throw new InvalidParameterException("Type miss match. " + key + " must be short.");
		}
	}

	public short getShort(String key) {
		Short value = getShort(key, null);
		if (value == null) {
			throw new InvalidParameterException(key + " is empty.");
		}
		return value;
	}

	public Date getDate(String key, String dateFormat) {
		Object value = get(key);
		if (isEmpty(value)) {
			throw new InvalidParameterException(key + " is empty.");
		}
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		try {
			return format.parse(value.toString());
		} catch (ParseException e) {
			throw new InvalidParameterException("Type miss match. " + key + " must be " + dateFormat + " format date string.");
		}
	}

	private static boolean isEmpty(Object obj) {
		return obj == null || "".equals(obj);
	}

	/**
	 * 목록에서 해당 key와 value가 일치하는 항목을 찾아 반환한다.
	 * 없으면 null 반환
	 * 
	 * @param list
	 * @param key
	 * @param value
	 * @return
	 */
	public static SingleMap search(List<SingleMap> list, String key, Long value) {
		for (SingleMap item : list) {
			if (Objects.equals(item.getLong(key, null), value)) {
				return item;
			}
		}
		return null;

	}

}
