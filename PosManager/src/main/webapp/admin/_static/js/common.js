/*
 * Filename	: common.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */


/*
 * ajax 통신 시  세션 타임아웃일 경우 로그인 페이지로 이동. 
 */
$(document).ajaxError(function(event, jqxhr, settings, exception) {
	if (exception == 'Unauthorized') {	
		// login page
		alert("Your session has expired. \nClick 'OK' to be redirected to the login page.");
		window.location = '/admin/login';
	}
});

// 테이터 테이블 에러 프롬프트 비활성화.
$.fn.dataTable.ext.errMode = 'none';

define([
	'jquery',
	'toastr',
	'moment'
], function($, toastr, moment, i18n) {
	'use strict';
	$('body').on('change', 'input.select-all[data-target]', function() {
		var target = $(this).attr('data-target');
		var checked = $(this).is(':checked');
		if (target != undefined && target != null) {
			$('input:checkbox[name="' + target +'"]').each(function(idx, elem) {
				$(elem).attr('checked', checked);
			});
		}
	});
	var listWrapper = function(collection) {
		var data = (collection != undefined ? collection.toJSON() : {});
		var wrapper = {};
		if (data.length == 1) {
			if ($.isEmptyObject(data[0])) {
				wrapper.items = [];
			} else {
				wrapper.items = data;
			}

		} else {
			wrapper.items = data;
		}

		if (collection != undefined) {
			wrapper.searchOptions = collection.searchOptions;
			wrapper.count = collection.count;
			wrapper.rowsPerPages = [
				{count: 10, selected: (collection.rowsPerPage == 10)},
				{count: 30, selected: (collection.rowsPerPage == 30)},
				{count: 50, selected: (collection.rowsPerPage == 50)}
			];
			wrapper.pageNo = collection.pageNo;
			wrapper.pageNumbers = [];

			// for test
			//wrapper.pageNo = 11;
			//wrapper.count = 128;
			//wrapper.rowsPerPage = 10;

			var naviCnt = config.pageNaviCount;
			var start = Math.floor(wrapper.pageNo / collection.rowsPerPage) * collection.rowsPerPage + 1;
			var last = Math.floor(wrapper.count / collection.rowsPerPage) + (wrapper.count % collection.rowsPerPage == 0 ? 0 : 1);
			var end = (start + (naviCnt - 1) > last) ? last : start + (naviCnt - 1);

			// invalid pageNo
			if (start > end) {
				start = 1;
				end = (start + (naviCnt - 1) > last) ? last : start + (naviCnt - 1);
			}

			for (var i = start; i <= end; i++) {
				wrapper.pageNumbers.push({
					no: i,
					selected : (i == wrapper.pageNo)
				});
			}
		}

		return wrapper;
	};

	var initPagination = function(collection) {
		var naviCnt = config.pageNaviCount;
		var last = Math.floor(collection.count / naviCnt) + (collection.count % naviCnt == 0 ? 0 : 1);
		var pageItems = $('.pagination li a');
		pageItems.click(function (event) {
			var page = $(event.currentTarget).attr('value');
			if ('next' == page) {
				if (collection.pageNo >= last) {
					toastr.warning('마지막 페이지 입니다.');
					return;
				} else {
					collection.pageNo++;
				}
			} else if ('prev' == page) {
				if (collection.pageNo < 2) {
					toastr.warning('첫 페이지 입니다.');
					return;
				} else {
					collection.pageNo--;
				}
			} else {
				if (page > last) {
					toastr.warning('마지막 페이지 입니다.');
					return;
				} else if (page < 1) {
					toastr.warning('첫 페이지 입니다.');
					return;
				} else {
					collection.pageNo = page;
				}
			}
			collection.fetch();
		});

		$('#rowControl').change(function () {
			var rowsPerPage = $('#rowControl option:selected').val();
			collection.rowsPerPage = rowsPerPage;

			collection.fetch();
		});
	};

	var clearContent = function() {
		$('#pageContent').children().each(function (idx, elem) {
			if (!$(elem).is('.page-title, .page-bar')) {
				$(elem).remove();
			}
		});
	};

	var getFirstErrorMsg = function(response) {
		var errMsg = "서버와 통신중 문제가 발생하였습니다.\n잠시후 다시 시도해 주십시오";
		if (jQuery.isArray(response.errors)) {
			if (response.errors.length > 0) {
				errMsg = response.errors[0].message;
			}
		}

		return errMsg;
	}

	var convertToMap = function(serializeArray) {
		var data = {};
		for (var i = 0; i < serializeArray.length; i++) {
			var obj = serializeArray[i];
			var key = obj.name;
			var val = obj.value;
			if (val.length > 0) {
				data[key] = val;
			}

		}

		return data;
	};

	var showErrorMessage = function(errorList) {
		if ($.isArray(errorList)) {
			$.each(errorList, function(idx, error) {
				if (error.hasOwnProperty('selector') && error.hasOwnProperty('message')) {
					var formGroup = $(error.selector).parent('div.form-group');

					// input -> input-group -> form-group 순서인경우
					if (formGroup == undefined || formGroup.length == 0) {
						var inputGroup = $(error.selector).parent('div.input-group');
						formGroup = inputGroup.parent('div.form-group');
					}
					if (formGroup == undefined || formGroup.length == 0) {
						formGroup = $(error.selector).parents('div.form-group').eq(0);
					}
					formGroup.addClass('has-error');
					formGroup.find(".help-block").text(error.message);
				}
			});
		}

		for (var key in errorList) {
			if (errorList.hasOwnProperty(key)) {
				var id = key;
				var errMsg = errorList[key];
				var formGroup = $("#" + id).parent('div.form-group');

				// input -> input-group -> form-group 순서인경우
				if (formGroup == undefined || formGroup.length == 0) {
					var inputGroup = $("#" + id).parent('div.input-group');
					formGroup = inputGroup.parent('div.form-group');
				}
				formGroup.addClass('has-error');
				formGroup.find(".help-block").text(errMsg);
			}
		}
	};

	var clearErrorMessage = function() {
		$('div.form-group').each(function(idx, elem) {
			$(elem).removeClass('has-error');
			$(elem).find(".help-block").text("");
		});
	};

	var isAlphanumeric = function(val, nullable) {
		/^[a-z0-9]+$/i
	};

	var genUUID = function () {
		var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = crypto.getRandomValues(new Uint8Array(1))[0]%16|0, v = c == 'x' ? r : (r&0x3|0x8);
			return v.toString(16);
		});

		return uuid.replace(/\-/g, "").toUpperCase();
	};

	
	
	/*
	 * 공통 js
	 */
	var loadBaseCode = function(codeAlias,option){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : codeAlias
			},
			success : function(result){
				if(result.success){
					if(option){
						$.each(Object.keys(result.codes), function(idx, code){
							$.each(result.codes[code], function(_idx, _code){
								_code.selections = '{{#'+code+'='+_code.baseCd+'}} selected="selected" {{/'+code+'='+_code.baseCd+'}}';
							});
						});
					}
					
					data = $.extend(data, result.codes);

				} else {
		    		$('#store-modal').html('');
		    		$('#store-modal').modal('toggle');
				}
			}
		});
		
		return data;
    };
    
    var buildOptions = function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    };
    
    /* Calendar */
	 var JC_calendar = {
	 	searchRange: function(i18n, isEmpty) {
	 		return function () {
	 	    	
	 	    	var rangeObj = new  Object();
	 	    	rangeObj[i18n.BO3001] = [moment().startOf('day'), moment().endOf('day')];	// 오늘 = 하루 시작, 하루 끝
	 	    	rangeObj[i18n.BO3002] = [moment().subtract(1, 'days'), moment().subtract(1, 'days')];		// 어제
	 	    	//rangeObj[i18n.BO3003] = [moment().subtract(6, 'days'), moment()];
	 	    	//rangeObj[i18n.BO3004] = [moment().subtract(29, 'days'), moment()];
	 	    	rangeObj[i18n.BO3005] = [moment().startOf('month'), moment().endOf('month')];
	 	    	rangeObj[i18n.BO3006] = [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')];
	 	    	rangeObj[i18n.BO3007] = [moment().startOf('year'), moment().endOf('year')];
	 	    	rangeObj[i18n.BO3008] = [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')];
	
	 	    	$('#daterangepicker').daterangepicker({
	 	        	  "alwaysShowCalendars": true,
	 	        	  // 초기값 설정을 하지않을 경우 autoUpdateInput 설정을 false로 변경
	 	        	  "autoUpdateInput": isEmpty == true ? false : true,
	 	        	  ranges: rangeObj,
	 	    		  locale: {
	 						format: _formatCalViewDay,
	 						separator: _formatCalSeparator,
	 						applyLabel: i18n.BO0023,
	 						cancelLabel: i18n.BO0026,
	 						fromLabel: i18n.BO3032,
	 						toLabel: i18n.BO3033,
	 						daysOfWeek: [i18n.BO3031, i18n.BO3025, i18n.BO3026, i18n.BO3027, i18n.BO3028, i18n.BO3029, i18n.BO3030],
	 						monthNames: [i18n.BO3013, i18n.BO3014, i18n.BO3015, i18n.BO3016, i18n.BO3017, i18n.BO3018, i18n.BO3019, i18n.BO3020, i18n.BO3021, i18n.BO3022, i18n.BO3023, i18n.BO3024],
	 						firstDay: 1
	 	    	      }, 
	 	    	      startDate: isEmpty == true ? "" : JC_init_value.firstMonthDay("from-date"),
	 	    	      endDate: isEmpty == true ? "" : JC_init_value.lastMonthDay("to-date")
	 	    	      
	 	    		},
	 	    		
	 	    		function (start, end, label) {
	 	    			if(isEmpty == false){
	 	    				$('#from-date').val(start.format(_formatCalParamDay));
		 	    			$('#to-date').val(end.format(_formatCalParamDay));
		 	    			$('#label-date').val(label);
	 	    			}
	 	    	    }
	 	    	);
	 	    	
	 	    	$('.date-picker').on('apply.daterangepicker', function(ev, picker) {
	 	    		$(this).val(picker.startDate.format(_formatCalViewDay) + ' - ' + picker.endDate.format(_formatCalViewDay));
	 	    		$('#from-date').val(picker.startDate.format(_formatCalParamDay));
 	    			$('#to-date').val(picker.endDate.format(_formatCalParamDay));
				});

				$('.date-picker').on('cancel.daterangepicker', function(ev, picker) {
					if(isEmpty == true){
						$(this).val('');
						$('#from-date').val('');
	 	    			$('#to-date').val('');
	 	    			$('#label-date').val('');
					}
				});
				
	 	    	// rangeTp == isEmpty 일 때 날짜는 빈 값
//	 	    	if (rangeTp != '' || rangeTp != undefined) {
//	 	    		if (rangeTp == 'isEmpty') {
//	 	    			$('#daterangepicker').val('');
//	 	    			$('#from-date').val('');
//	 	    			$('#to-date').val('');
//	 	    			$('#label-date').val('');
//	 	    		}
//	 	    	}	 	    	
	 		}
	 	}
	 };
	 
	 /* Initial Values */
	 var JC_init_value = {
	 	firstMonthDay: function(divId) {
	 		var curDate = new Date();
	 		var firstDate = new Date(curDate.getFullYear(), curDate.getMonth(), 1);
	 		$('#from-date').val(firstDate.format("yyyy-MM-dd"));
	 		return firstDate.format(_formatDay);
	 	},
	 	lastMonthDay: function(divId) {
	 		var curDate = new Date();
	 		var lastDate = new Date(new Date(curDate.getFullYear(), curDate.getMonth() + 1) - 1);
	 		$('#to-date').val(lastDate.format("yyyy-MM-dd"));
	 		return lastDate.format(_formatDay);
	 	}
	 };
	 
	 /* Format */
	 var _formatObj = new Object();
	 _formatObj['ko']= {numGrpChar:',', numPtChar:'.', day:'yyyy.MM.dd', time:'HH:mm:ss', date:'yyyy.MM.dd HH:mm:ss', termFormat:'yy/mm/dd'};
	 _formatObj['ja']= {numGrpChar:',', numPtChar:'.', day:'yyyy.MM.dd', time:'HH:mm:ss', date:'yyyy.MM.dd HH:mm:ss', termFormat:'yy/mm/dd'};
	 _formatObj['en']= {numGrpChar:',', numPtChar:'.', day:'dd.MM.yyyy', time:'HH:mm:ss', date:'dd.MM.yyyy HH:mm:ss', termFormat:'dd/mm/yy'};
	 _formatObj['ru']= {numGrpChar:' ', numPtChar:',', day:'dd.MM.yy', time:'HH:mm:ss', date:'dd.MM.yy HH:mm:ss', termFormat:'dd/mm/yy'};
	 //var _formatCalDay = _formatObj[_lang]['calDay'];
	 var _formatDay = _formatObj[_lang]['day'];
	 var _formatTime = _formatObj[_lang]['time'];
	 //var _formatDate = _formatDay + " " + _formatTime;
	 var _formatDate = _formatObj[_lang]['date'];
	 var _formatCalViewDay = _formatDay.toUpperCase();
	 var _formatCalViewDate = _formatDate.toUpperCase();
	 var _formatCalSeparator = ' ~ ';
	 var _formatCalParamDay = 'YYYY-MM-DD 00:00:00';
	 var _formatNumGrpChar = _formatObj[_lang]['numGrpChar'];
	 var _formatNumPtChar = _formatObj[_lang]['numPtChar'];
	 var _formatTerm = _formatObj[_lang]['termFormat'];

	 // date format
	 Date.prototype.format = function(f) {
	     if (!this.valueOf()) return " ";
	  
	     var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
	     var d = this;
	      
	     return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
	         switch ($1) {
	             case "yyyy": return d.getFullYear();
	             case "yy": return (d.getFullYear() % 1000).zf(2);
	             case "MM": return (d.getMonth() + 1).zf(2);
	             case "dd": return d.getDate().zf(2);
	             case "E": return weekName[d.getDay()];
	             case "HH": return d.getHours().zf(2);
	             case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
	             case "mm": return d.getMinutes().zf(2);
	             case "ss": return d.getSeconds().zf(2);
	             case "a/p": return d.getHours() < 12 ? "오전" : "오후";
	             default: return $1;
	         }
	     });
	 };
	 
	 String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	 String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	 Number.prototype.zf = function(len){return this.toString().zf(len);};

	 var JC_format = {
	 	handleData: function(data) {
	 		data.formatNumber = function() {
	             return function(val, render) {
	                 return JC_format.number(render(val));
	             }
	 		};
	 		data.formatNumber2 = function() {
	             return function(val, render) {
	                 return JC_format.number(render(val), true);
	             }
	 		};
	 		data.formatPrice = function() {
	             return function(val, render) {
	                 return JC_format.number(render(val));
	             }
	 		};
	 		data.formatPrice2 = function() {
	             return function(val, render) {
	                 return JC_format.number(render(val), true);
	             }
	 		};
	 		data.formatDay = function() {
	             return function(val, render) {
	                 return JC_format.day(render(val), true);
	             }
	 		};
	 		data.formatTime = function() {
	             return function(val, render) {
	                 return JC_format.time(render(val), true);
	             }
	 		};
	 		data.formatDate = function() {
	             return function(val, render) {
	                 return JC_format.date(render(val), true);
	             }
	 		};
	 		
	 		return data;
	 	},
	 	transTerm: function(term) {
	 		if (term == null) {return term;}
			var temp = term.replace(/[^0-9]/g, "");
			if (temp.length != 6) {return term;}
			var first = temp.substring(0, 2);
			var middle = temp.substring(2, 4);
			var last = temp.substring(4, 6);
			var termVal;
			if (_lang == 'ru' || _lang == 'en') {
				termVal = last + "/" + middle + "/" + first;
			}
			else /*if (_lang == 'ko')*/ {
				termVal = term;
			}
			return termVal;
	 	},
	 	formatTerm: function() {
			return _formatTerm;
	 	},
	 	number: function(nStr, isFloat) {
	 		if(nStr == '' || nStr == undefined){
	 			return 0;
	 		}else{
	 			nStr += '';
		 		var x = nStr.split('.');
		 		var x1 = x[0];
		 		var x2 = x.length > 1 ? _formatNumPtChar + x[1] : _formatNumPtChar + '00';
		 		var rgx = /(\d+)(\d{3})/;
		 		while (rgx.test(x1)) {
		 			x1 = x1.replace(rgx, '$1' + _formatNumGrpChar + '$2');
		 		}
		 		return x1 + (isFloat === true ? x2 : "");
	 		}	 		
	 	},
	 	day: function(nStr) {
	 		if (nStr == null) {return nStr;}
	 		var temp = nStr.replace(/[^0-9]/g, "");
	 		if (temp.length < 8) {return nStr;}
	 		var date = new Date(temp.substring(0, 4) + "-" + temp.substring(4, 6) + "-" + temp.substring(6, 8));
	 		
	 		return date.format(_formatDay);
	 	},
	 	time: function(nStr) {
	 		if (nStr == null) {return nStr;}
	 		var temp = nStr.replace(/[^0-9]/g, "");
	 		if (temp.length != 6) {return nStr;}
	 		var date = new Date(2016, 1, 1, parseInt(temp.substring(0, 2)), parseInt(temp.substring(2, 4)), parseInt(temp.substring(4, 6)));
	 		
	 		return date.format(_formatTime);
	 	},
	 	date: function(nStr) {
	 		if (nStr == null) {return nStr;}
	 		var temp = nStr.replace(/[^0-9]/g, "");
	 		if (temp.length != 14) {return nStr;}
	 		
	 		var date = new Date(parseInt(temp.substring(0, 4)), parseInt(temp.substring(4, 6) - 1), 
	 				parseInt(temp.substring(6, 8)), parseInt(temp.substring(8, 10)),
	 				parseInt(temp.substring(10, 12)), parseInt(temp.substring(12, 14)));

	 		return date.format(_formatDate);
	 	}
	 };
	 
	return {
		listWrapper: listWrapper,
		initPagination: initPagination,
		clearContent : clearContent,
		getFirstErrorMsg: getFirstErrorMsg,
		convertToMap: convertToMap,
		showErrorMessage: showErrorMessage,
		clearErrorMessage : clearErrorMessage,
		genUUID: genUUID,
		
		loadBaseCode: loadBaseCode,
		buildOptions : buildOptions,
		JC_calendar : JC_calendar,
		JC_google_util : JC_google_util,
		JC_init_value : JC_init_value,
		JC_format : JC_format,
	};
});
