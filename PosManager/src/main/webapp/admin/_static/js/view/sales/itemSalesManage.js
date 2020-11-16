/*
 * Filename	: itemSalesManage.js
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
		  
		  ,'text!templates/sales/itemSalesManage.html'
		  ,'text!templates/sales/itemSalesChartAndData.html'
		  
		  ,'bootstrap-daterangepicker'
		  ,'fusioncharts'
		  ,'fusioncharts.theme.fint'
		  ,'jquery.tabletoCSV'

], function (Backbone, Mustache, Common, i18n, template, chartAndData) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/ItemSales';

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
			'change #summary_select' : 'selectOptTp',
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
	      
	      //데이터 테이블 컬럼 숨기기/보이기
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

	    selectOptTp : function() {

	    	var optTp = $('#summary_select').val();

	    	if(optTp == 'summary'){
	    		$('#forSummaryOption').show();
	    		$('#forEtcOption').hide();
	    		$('#option2_select').val('');
	    	}else{
	    		$('#forSummaryOption').hide();
	    		$('#forEtcOption').show();
	    	}
	    },
	    search : function() {
	    	var view = this;
	    	var option1 = $("[name=option1] option:selected").val();
	    	var option2 = $("[name=option2] option:selected").val();

	    	if (_data.franId == "" || _data.franId == undefined) {
	    		alert(i18n.BO4001);
	    		return false;
	    	}

	    	if (_data.brandId == "" || _data.brandId == undefined) {
	    		alert(i18n.BO4002);
	    		return false;
	    	}
	    	if (option1 != 'summary') {
		    	if (option2 == "") {
		    		alert(i18n.BO4031);
		    		return false;
		    	}
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
				"async" : false,
				success : function(result) {
					if (result.success) {
						var data = {};
						data.list = result.list;
				        data.option1 = $('[name=option1] option:selected').val();
				        data.option2 = $('[name=option2] option:selected').val();
				        data.summaryYn = data.option1 == 'summary' || data.option1 == null ? true : false;
				        data.searchYn = true;
				        view.summaryYn = data.summaryYn;
				        view.searchYn = true;
				        //테이블헤더
				        data.tableHeader = result.tableHeader
				    	//테이블 데이터
				        data.tableList   = result.tableList
				        data.i18n = i18n;
				        data = Common.JC_format.handleData(data);
				        
						$('#search-table').html(Mustache.to_html(chartAndData, data));
						
						view.convertChart(data.summaryYn, result);
						
						//테이블 Create.
						view.tableHtml(result.list, data.option1, data.tableHeader, data.tableList);
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
	    tableHtml: function (data, option1,tableHeader, tableList) {
	    	var html = '';

	    	html += '<thead>';
	    	if(tableHeader != null) {    	
	    	// 1.Create Table-Header
	    	html += "<tr class='dataTables_wrapper_th_min'>";
	    	html += "<th>" + i18n.BO2243 + "</th>";
	    	html += "<th>" + i18n.BO2278 + "</th>";
	    	
	    	$.each(tableHeader, function(idx, val) {				
					var value = val.LABEL;
					html += "<th>"+value+"</th>";
	    	});

	    	html += "</tr>";
	    	html += "</thead>";

	    	html += "<tbody>";
	    	// END OF Create Table-Header
	    	
	    	// 2.Create Table-ListData
			$.each(tableList, function(idx, val) {
				html += "<tr class='even gradeC'>";
				
				for (var key in val) {
					//First header at Left
					if( key == "HEADER"){
						html += "<td class='title'>" + val[key] + "</td>";
					} else {
						html += "<td class='price'>" +  Common.JC_format.number(val[key]) + "</td>";
					}					
				}
				
				html += "</tr>";
				
			});
			// END OF 2.Create Table-ListData
			html += '</tbody>';
	    	}
	    	$('#etc-table').html(html);
	    },

	    convertChart : function(summaryYn, charData) {

	        if (summaryYn) {
	    		var lineChart = new FusionCharts({
	    				'type': 'column2d',
	    				'renderAt': 'chart01',
	    				'width': '100%',
	    				'height': '450',
	    				'dataFormat': 'json',
	    				'dataSource': {
	    					'chart': {
	    						'caption': i18n.BO2494,
	    						"subCaption": i18n.BO2351,
	    						"paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
	    						'xAxisName': i18n.BO1015,
	    						'yAxisName': i18n.BO2303,
//	    						"numberprefix": i18n.BO0030,
	    						'theme': 'fint',	    						
	    				        'exportEnabled': '1'
	    					},
	    					'data': charData.ItemSalesTop5Chart
	    				}
	    			});
	    		lineChart.render();

	    		var pieChart = new FusionCharts({
	    				'type': 'doughnut2d',
	    				'renderAt': 'chart02',
	    				'width': '100%',
	    				'height': '450',
	    				'dataFormat': 'json',
	    				'dataSource': {
	    					'chart': {
	    						'caption': i18n.BO2494,
	    						"subCaption": i18n.BO2351,
//	    						"numberprefix": i18n.BO0030,
	    		                "paletteColors": "#0075c2,#1aaf5d,#f2c500,#f45b00,#8e0000",
	    		                "bgColor": "#ffffff",
	    		                "showBorder": "0",
	    		                "use3DLighting": "0",
	    		                "showShadow": "0",
	    		                "enableSmartLabels": "0",
	    		                "startingAngle": "310",
	    		                "showLabels": "0",
	    		                "showPercentValues": "1",
	    		                "showLegend": "1",
	    		                "legendShadow": "0",
	    		                "legendBorderAlpha": "0",
	    		                "defaultCenterLabel": "Total revenue: $64.08K",
	    		                "centerLabel": "Revenue from $label: $value",
	    		                "centerLabelBold": "1",
	    		                "showTooltip": "0",
	    		                "decimals": "0",
	    		                "captionFontSize": "14",
	    		                "subcaptionFontSize": "14",
	    		                "subcaptionFontBold": "0",
	    		                'exportEnabled': '1'
	    		                
	    					},
	    					'data': charData.ItemSalesTop5Chart
	    				}
	    			});
	    		pieChart.render();
	        }
	        else {

	        	// 임시처리 
	        	var dayMonth = charData.dayMonth;
	        	var dayMonthData = dayMonth == 'day' ? charData.dayChart : charData.monthChart;	        	

	        	var _dayMonData = [];
	        	var _dayMonItem = [];
	        	var _label = [];
	        	
	        	$.each(dayMonthData,function(idx, value){
	        		var temp = [];
	        		$.each(dayMonthData[idx],function(label, value){
	        			if(label != "" && label != undefined){
		        			if(label != "itemNm"){
		        				var _cObj = {};
		        	    		_cObj.value = value;
		        	    		temp.push(_cObj);
		        	    		
		        	    		if(idx == 0){
		        	    			var _cObj2 = {};
		        	    			_cObj2.label = label;
		        	    			_label.push(_cObj2);
		        	    		}
		        			}
	        			}
	        		});
	        		_dayMonData.push({ 'seriesname' : dayMonthData[idx].itemNm, 'data' : temp});
	        	});
	        	var dayMonthChart = new FusionCharts({
	                type: 'msline',
	                renderAt: 'dayMonthChart',
	                width: '100%',
	                height: '230',
	                dataFormat: 'json',
	                dataSource: {
	                    "chart": {
	                        "caption": i18n.BO2494,
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
	                        "yAxisName": i18n.BO2303,
	                        "showValues": "0",
//	                        "numberprefix": i18n.BO0030,
	                        "exportEnabled": "1",
	                        	
	                        "labelDisplay": "rotate",
                        	"slantlabels": "1"
	                    },
	                    
	                    "categories": [
	                        {
	                            "category": _label
	                        }
	                    ],
	                    "dataset" :_dayMonData
	                }
	            }).render();

	        	var _weeklyData = [];
	        	var _weeklyItem = [];
	        	var _weeklyLabel = [];
	        	
	        	$.each(charData.dayofweekChart,function(idx, value){
	        		var temp = [];
	        		$.each(charData.dayofweekChart[idx],function(label, value){
	        			if(label != "" && label != undefined){
	        				if(label != "itemNm"){
		        				var _cObj = {};
		        	    		_cObj.value = value;
		        	    		temp.push(_cObj);
		        	    		
		        	    		if(idx == 0){
		        	    			var _cObj2 = {};
		        	    			_cObj2.label = label;
		        	    			_weeklyLabel.push(_cObj2);
		        	    		}
		        			}
	        			}
	        		});
	        		_weeklyData.push({ 'seriesname' : charData.dayofweekChart[idx].itemNm, 'data' : temp});
	        	});	        	

	        	var dayofweekChart = new FusionCharts({
	        		type: 'msline',
	                renderAt: 'dayofweekChart',
	                width: '100%',
	                height: '230',
	                dataFormat: 'json',
	        		dataSource: {
	        			"chart": {
	                        "caption": i18n.BO2494,
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
	                        "yAxisName": i18n.BO2303,
	                        "showValues": "0",
//	                        "numberprefix": i18n.BO0030,
	                        "exportEnabled": "1"
	                    },
	                    
	                    "categories": [
	                        {
	                            "category": _weeklyLabel
	                        }
	                    ],
	                    "dataset": _weeklyData
	        		}
	        	}).render();
	        	
	        	var _hourData = [];
	        	var _hourItem = [];
	        	var _hourLabel = [];
	        	
	        	$.each(charData.hourChart,function(idx, value){
	        		var temp = [];
	        		$.each(charData.hourChart[idx],function(label, value){
	        			if(label != "" && label != undefined){
		        			if(label != "itemNm"){
		        				var _cObj = {};
		        	    		_cObj.value = value;
		        	    		temp.push(_cObj);
		        	    		
		        	    		if(idx == 0){
		        	    			var _cObj2 = {};
		        	    			_cObj2.label = label;
		        	    			_hourLabel.push(_cObj2);
		        	    		}
		        			}
	        			}
	        		});
	        		_hourData.push({ 'seriesname' : charData.hourChart[idx].itemNm, 'data' : temp});
	        	});
	        	
	        	
	        	var hourChart = new FusionCharts({
	        		type: 'msline',
	        		renderAt: 'hourChart',
	        		width: '100%',
	        		height: '230',
	        		dataFormat: 'json',
	        		dataSource: {
	        			"chart": {
	                        "caption": i18n.BO2494,
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
	                        "yAxisName": i18n.BO2303,
	                        "showValues": "0",
//	                        "numberprefix": i18n.BO0030,
	                        "exportEnabled": "1"
//	                        ,"labelDisplay": "rotate"	
//                        	,"slantlabels": "1"	                        
	                        
	                    },
	                    
	                    "categories": [
	                        {
	                            "category": _hourLabel
	                        }
	                    ],
	                    "dataset": _hourData
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