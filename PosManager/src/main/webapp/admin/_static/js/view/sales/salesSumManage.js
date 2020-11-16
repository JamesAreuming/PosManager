/*
 * Filename	: salesSumManage.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
	   'backbone'
	  ,'mustache'
	  ,'common'
	  ,'i18n!templates/globals/lang/nls/language'
	  
	  ,'text!templates/sales/salesSumManage.html'
	  ,'text!templates/sales/salesSumChartAndData.html'
	  
	  ,'bootstrap-daterangepicker'
	  ,'fusioncharts'
	  ,'fusioncharts.theme.fint'
	  ,'jquery.tabletoCSV'

], function (Backbone, Mustache, Common, i18n, template, chartAndData) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/SalesSum';

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    initialize: function () {
    	this.trigger('remove-compnents');
        this.template = template;
        this.listenTo(this.model, 'sync', this.render);
    },
    events : {
    	'click button#search' : 'search',
		'click button#csv-exp' : 'csv_exp',
		'change #franchise_select' : 'selectBrand',
		'click .add-on-datepicker' : 'trigger_calender'
    },
    render: function () {
    	var view = this;
    	var data = {};
        data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
        data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
        
        _data.userInfo = view.model.toJSON()[0].session.userInfo;
        
        _data.franId = _data.userInfo.franId;
        _data.brandId = _data.userInfo.brandId;
        _data.storeId = _data.userInfo.storeId;
        
        data.userInfo = view.model.toJSON()[0].session.userInfo;
        data.i18n = i18n;
        
        if(data.frList == undefined || data.frList == ""){
      	  data.isFr = true;
        }else{
      	  data.isFr = false;
        }
        
        if(data.isFr == true && data.brList == undefined || data.brList == ""){
      	  data.isBr = true;
        }else{
      	  data.isBr = false;
        }
        
        if(data.frList == undefined && data.brList == undefined && data.stList == undefined){
      	  data.isSt = true;
        }else{
      	  data.isSt = false;
        }
        
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.setCalendarArea();
      view.selectedRollInit();

      // 스토어 사용자가 로그인시 자동조회
      if(data.isSt == true || data.isBr == true){
    	  view.search();
      }
      
      $('input[type="checkbox"]').click(function() {
          var index = $(this).attr('name').substr(3);
          index--;
          $('table tr').each(function() {
              $('td:eq(' + index + ')',this).toggle();
          });
          $('th.' + $(this).attr('name')).toggle();
      });
      
      return view;
    },
    
    // setup calendar
    setCalendarArea : Common.JC_calendar.searchRange(i18n),
    
    selectedRollInit : function (){
    	var view = this;

    	$('select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});
    	
    	$('#franchise_select').select2().on('change', function(){
      		_data.franId = $(this).val();
      		
      		$.ajax({
      			url : './model/Brand',
      			data : {
      				data : JSON.stringify({ franId : $(this).val() })
      			},
      			success : function(result){
      				if(result.success){
      					// 브랜드ID, 스토어ID 전역변수 초기화 
      					_data.brandId = '';
      		      		_data.storeId = '';
      		      		
      					// 브랜드 콤보 초기화
      					var brand_default = [{id: '', brandNm : i18n.BO1032}];
      					$("#brand_select").find('option').remove();
      					$('#brand_select').select2({
      						data : brand_default.concat(result.list),
      						templateResult : function(state) {
      							return state.brandNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.brandNm;
      						}
      					});
      					
      					// 스토어 콤보 초기화
      					var store_default = [{id: '', storeNm : i18n.BO1033}];
      					$("#store_select").find('option').remove();
      					$('#store_select').select2({
      						data : store_default.concat(result.list),
      						templateResult : function(state) {
      							return state.storeNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.storeNm;
      						}
      					});
      				}
      			}  //END : brand select result
      		});  //END : brand select ajax
      		$('#search-table').empty();
      	});  //END : franchise select change
    	
    	$('#brand_select').select2().on('change', function(){
    		_data.brandId = $(this).val();
			$.ajax({
      			url : './model/Store',
      			data : {
      				data : JSON.stringify({ brandId : $(this).val() })
      			},
      			success : function(result){
      				if(result.success){
      					// 스토어ID 전역변수 초기화 
      		      		_data.storeId = '';
      		      		
      					// 스토어 콤보 초기화
      					var defualt_array = [{id: '', storeNm : i18n.BO1033}];
      					$("#store_select").find('option').remove();
      					$('#store_select').select2({
      						data : defualt_array.concat(result.list),
      						templateResult : function(state) {
      							return state.storeNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.storeNm;
      						}
      					});
      				}
      			}  //END : store select result
      		});  //END : store select ajax
			$('#search-table').empty();
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
    		_data.storeId = $('#store_select').val();
    		$('#search-table').empty();
        });
    },
    
    search : function() {
    	var view = this;
    	var option1 = $("[name=option1] option:selected").val();

    	if (_data.franId == "" || _data.franId == undefined) {
    		alert(i18n.BO4001);
    		return false;
    	}

    	if (_data.brandId == "" || _data.brandId == undefined) {
    		alert(i18n.BO4002);
    		return false;
    	}
    	
    	if ($('#daterangepicker').val() == 'Invalid date - Invalid date') {
    		alert(i18n.BO4032);
    		return false;
    	}

    	// 검색데이터 세팅
    	var data = {};
    	
    	data._method = 'GET';
		
    	// 검색폼 정보
		data.data = $('#be-search').serializeObject();
		
		// 프랜차이즈 ID
		data.data.franId = _data.franId;
		// 브랜드 ID
		data.data.brandId = _data.brandId;
		// 스토어 ID
		data.data.storeId = _data.storeId;
		
		data.data = JSON.stringify(data.data);
		
		// 사용자 정보
		data.userInfo = JSON.stringify(_data.userInfo);
		
    	$.each(Object.keys(data), function(idx, key){
    		if ( data[key] == "" || data[key] === null || (typeof data[key] == "object" &&  $.isEmptyObject(data[key])) ) {
    			delete data[key];
    		}
    	});
    	
		$.ajax({
			"url" : targetUrl,
			"type" : 'POST',
			"data" : data,
			"async": false,
			success : function(result) {
				if (result.success) {
					var data = {};
					data.list = result.list;
			        data.option1 = $('[name=option1] option:selected').val();

			        data.summaryYn = data.option1 == 'summary' || data.option1 == null ? true : false;
			        
			        data.searchYn = true;
			        view.summaryYn = data.summaryYn;
			        view.searchYn = true;

			        data.i18n = i18n;
			        data = Common.JC_format.handleData(data);
			        
			        $('#search-table').empty();
			        
			        
					$('#search-table').html(Mustache.to_html(chartAndData, data));

					view.convertChart(data.summaryYn, result);
				}
			}
		});
    },
    csv_exp: function () {
    	if (this.searchYn) {
    		$(this.summaryYn == true ? '#summary-table' : '#etc-table').tableToCSV();
    	}
    	else {
    		alert(i18n.BO4030);
    	}
    },

    convertChart : function(summaryYn, charData) {

        if (summaryYn) {
        	var _cData = [];
        	
        	$.each(charData.list[0],function(label, value){
        		if(label != 'storeNm'){
    	    		var _cObj = {};
    	    		_cObj.label = label;
    	    		_cObj.value = value;
    	    		_cData.push(_cObj);
        		}
        	});
        	
    		var charts = new FusionCharts({
    				'type': 'column2d',
    				'renderAt': 'chart01',
    				'width': '100%',
    				'height': '230',
    				'dataFormat': 'json',
    				'dataSource': {
    					'chart': {
    						'caption': i18n.BO2393,
    						'xAxisName': i18n.BO2455,
    						'yAxisName': i18n.BO2215,
//    						"numberprefix": i18n.BO0030,
    						'theme': 'fint',
    				        'exportEnabled': '1'
    					},
    					'data': _cData
    				}
    			});
    			charts.render();
        }
        else {
        	
        	// 임시처리 
        	var dayMonth = charData.dayMonth;
        	var dayMonthData = dayMonth == 'day' ? charData.dayChart : charData.monthChart;	        	
        	
        	var _dailyLabel = [];
        	var _dailySales1 = [];
        	var _dailySales2 = [];
        	var _dailySales3 = [];
        	var _dailySales4 = [];
        	var _dailySales5 = [];
        	var _dailySales6 = [];
        	var _dailySales7 = [];
        	var _dailySales8 = [];
        	var _dailySales9 = [];

        	$.each(dayMonthData,function(idx, value){
        		$.each(dayMonthData[idx],function(label, value){        			
        			if(value != "Total"){
        				if(label == "label"){
                			var _cObj = {};
            	    		_cObj.label = value;        	    		
            	    		_dailyLabel.push(_cObj);
            			}else if(label == "sales"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales1.push(_cObj);
    					}else if(label == "discount"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales2.push(_cObj);
    					}else if(label == "realSales"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales3.push(_cObj);
    					}else if(label == "tax"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales4.push(_cObj);
    					}else if(label == "netSales"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales5.push(_cObj);
    					}else if(label == "customerCnt"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales6.push(_cObj);
    					}else if(label == "cash"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales7.push(_cObj);
    					}else if(label == "card"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales8.push(_cObj);
    					}else if(label == "etc"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_dailySales9.push(_cObj);
    					}
        			}
        		});
        	});
        	
        	var dayMonthChart = new FusionCharts({
                type: 'msline',
                renderAt: 'dayMonthChart',
                width: '100%',
                height: '270',
                dataFormat: 'json',
                dataSource: {
                    "chart": {
                        "caption": i18n.BO1005,
                        "subCaption": dayMonth == 'day' ? i18n.BO2487 : i18n.BO2489,
                        "captionFontSize": "14",
                        "subcaptionFontSize": "14",
                        "subcaptionFontBold": "0",
                        "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                        "bgcolor": "#ffffff",
                        "showBorder": "0",
                        "showShadow": "0",
                        "showCanvasBorder": "0",
                        "usePlotGradientColor": "0",
                        "legendBorderAlpha": "0",
                        "legendShadow": "0",
                        "showAxisLines": "0",
                        "showAlternateHGridColor": "0",
                        "divlineThickness": "1",
                        "divLineIsDashed": "1",
                        "divLineDashLen": "1",
                        "divLineGapLen": "1",
                        "xAxisName": dayMonth == 'day' ? i18n.BO2491 : i18n.BO2493,
                        "yAxisName": i18n.BO2215,
//                        "numberprefix": i18n.BO0030,
                        "showValues": "0",
                        "exportEnabled": "1",
                        "labelDisplay": "rotate",
                    	"slantlabels": "1"
                    },
                    
                    "categories": [
                        {
                            "category": _dailyLabel
                        }
                    ],
                    "dataset": [
                        {
                            "seriesname": "sales",
                            "data": _dailySales1
                        }, 
                        {
                            "seriesname": "discount",
                            "data": _dailySales2
                        },
                        {
                            "seriesname": "realSales",
                            "data": _dailySales3
                        },
                        {
                            "seriesname": "tax",
                            "data": _dailySales4
                        },
                        {
                            "seriesname": "netSales",
                            "data": _dailySales5
                        },
                        {
                            "seriesname": "customerCnt",
                            "data": _dailySales6
                        },
                        {
                            "seriesname": "cash",
                            "data": _dailySales7
                        },
                        {
                            "seriesname": "card",
                            "data": _dailySales8
                        },
                        {
                            "seriesname": "etc",
                            "data": _dailySales9
                        }
                    ]
                }
            }).render();

        	// 임시처리 
        	var _weeklyLabel = [];
        	var _weeklySales1 = [];
        	var _weeklySales2 = [];
        	var _weeklySales3 = [];
        	var _weeklySales4 = [];
        	var _weeklySales5 = [];
        	var _weeklySales6 = [];
        	var _weeklySales7 = [];
        	var _weeklySales8 = [];
        	var _weeklySales9 = [];

        	$.each(charData.dayofweekChart,function(idx, value){
        		$.each(charData.dayofweekChart[idx],function(label, value){
        			if(value != "Total"){
        				if(label == "label"){
                			var _cObj = {};
            	    		_cObj.label = value;        	    		
            	    		_weeklyLabel.push(_cObj);
            			}else if(label == "sales"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales1.push(_cObj);
    					}else if(label == "discount"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales2.push(_cObj);
    					}else if(label == "realSales"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales3.push(_cObj);
    					}else if(label == "tax"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales4.push(_cObj);
    					}else if(label == "netSales"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales5.push(_cObj);
    					}else if(label == "customerCnt"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales6.push(_cObj);
    					}else if(label == "cash"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales7.push(_cObj);
    					}else if(label == "card"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales8.push(_cObj);
    					}else if(label == "etc"){
    						var _cObj = {};
            	    		_cObj.value = value;
            	    		_weeklySales9.push(_cObj);
    					}
        			}        			
        		});
        	});
        	
        	var dayofweekChart = new FusionCharts({
        		type: 'msline',
                renderAt: 'dayofweekChart',
                width: '100%',
                height: '270',
                dataFormat: 'json',
        		dataSource: {
        			"chart": {
                        "caption": i18n.BO1005,
                        "subCaption": i18n.BO2488,
                        "captionFontSize": "14",
                        "subcaptionFontSize": "14",
                        "subcaptionFontBold": "0",
                        "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                        "bgcolor": "#ffffff",
                        "showBorder": "0",
                        "showShadow": "0",
                        "showCanvasBorder": "0",
                        "usePlotGradientColor": "0",
                        "legendBorderAlpha": "0",
                        "legendShadow": "0",
                        "showAxisLines": "0",
                        "showAlternateHGridColor": "0",
                        "divlineThickness": "1",
                        "divLineIsDashed": "1",
                        "divLineDashLen": "1",
                        "divLineGapLen": "1",
                        "xAxisName": i18n.BO2492,
                        "yAxisName": i18n.BO2215,
//                        "numberprefix": i18n.BO0030,
                        "showValues": "0",
                        "exportEnabled": "1"
                    },
                    
                    "categories": [
                        {
                            "category": _weeklyLabel
                        }
                    ],
                    "dataset": [
                        {
                            "seriesname": "sales",
                            "data": _weeklySales1
                        }, 
                        {
                            "seriesname": "discount",
                            "data": _weeklySales2
                        },
                        {
                            "seriesname": "realSales",
                            "data": _weeklySales3
                        },
                        {
                            "seriesname": "tax",
                            "data": _weeklySales4
                        },
                        {
                            "seriesname": "netSales",
                            "data": _weeklySales5
                        },
                        {
                            "seriesname": "customerCnt",
                            "data": _weeklySales6
                        },
                        {
                            "seriesname": "cash",
                            "data": _weeklySales7
                        },
                        {
                            "seriesname": "card",
                            "data": _weeklySales8
                        },
                        {
                            "seriesname": "etc",
                            "data": _weeklySales9
                        }
                    ]
        		}
        	}).render();

        	// 임시처리 
        	var _hourLabel = [];
        	var _hourSales1 = [];
        	var _hourSales2 = [];
        	var _hourSales3 = [];
        	var _hourSales4 = [];
        	var _hourSales5 = [];
        	var _hourSales6 = [];
        	var _hourSales7 = [];
        	var _hourSales8 = [];
        	var _hourSales9 = [];
        	
        	$.each(charData.hourChart,function(idx, value){	        		
        		$.each(charData.hourChart[idx],function(label, value){
        			if(value != "Total"){
	        			if(label == "label"){
	            			var _cObj = {};
	        	    		_cObj.label = value;        	    		
	        	    		_hourLabel.push(_cObj);
	        			}else if(label == "sales"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales1.push(_cObj);
						}else if(label == "discount"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales2.push(_cObj);
						}else if(label == "realSales"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales3.push(_cObj);
						}else if(label == "tax"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales4.push(_cObj);
						}else if(label == "netSales"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales5.push(_cObj);
						}else if(label == "customerCnt"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales6.push(_cObj);
						}else if(label == "cash"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales7.push(_cObj);
						}else if(label == "card"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales8.push(_cObj);
						}else if(label == "etc"){
							var _cObj = {};
	        	    		_cObj.value = value;
	        	    		_hourSales9.push(_cObj);
						}
        			}
        		});
        	});

        	var hourChart = new FusionCharts({
        		type: 'msline',
        		renderAt: 'hourChart',
        		width: '100%',
        		height: '270',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
                        "caption": i18n.BO1005,
                        "subCaption": i18n.BO2486,
                        "captionFontSize": "14",
                        "subcaptionFontSize": "14",
                        "subcaptionFontBold": "0",
                        "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
                        "bgcolor": "#ffffff",
                        "showBorder": "0",
                        "showShadow": "0",
                        "showCanvasBorder": "0",
                        "usePlotGradientColor": "0",
                        "legendBorderAlpha": "0",
                        "legendShadow": "0",
                        "showAxisLines": "0",
                        "showAlternateHGridColor": "0",
                        "divlineThickness": "1",
                        "divLineIsDashed": "1",
                        "divLineDashLen": "1",
                        "divLineGapLen": "1",
                        "xAxisName": i18n.BO2490,
                        "yAxisName": i18n.BO2215,
//                        "numberprefix": i18n.BO0030,
                        "showValues": "0",
                        "exportEnabled": "1",
                        "labelDisplay": "rotate",
                    	"slantlabels": "1"
                    },
                    
                    "categories": [
                        {
                            "category": _hourLabel
                        }
                    ],
                    "dataset": [
                        {
                            "seriesname": "sales",
                            "data": _hourSales1
                        }, 
                        {
                            "seriesname": "discount",
                            "data": _hourSales2
                        },
                        {
                            "seriesname": "realSales",
                            "data": _hourSales3
                        },
                        {
                            "seriesname": "tax",
                            "data": _hourSales4
                        },
                        {
                            "seriesname": "netSales",
                            "data": _hourSales5
                        },
                        {
                            "seriesname": "customerCnt",
                            "data": _hourSales6
                        },
                        {
                            "seriesname": "cash",
                            "data": _hourSales7
                        },
                        {
                            "seriesname": "card",
                            "data": _hourSales8
                        },
                        {
                            "seriesname": "etc",
                            "data": _hourSales9
                        }
                    ]
        		}
        	}).render();
        }
    },
    
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    }

  });

  return ContentView;
});