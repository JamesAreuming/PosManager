/*
 * Filename	: serviceSalesManage.js
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
  
  ,'text!templates/sales/serviceSalesManage.html'
  ,'text!templates/sales/serviceSalesChartAndData.html'
  ,'text!templates/sales/modal/modal_form_alert.html'
  
  ,'bootstrap-daterangepicker'
  ,'fusioncharts'
  ,'fusioncharts.theme.fint'
  ,'jquery.tabletoCSV'

], function (Backbone, Mustache, Common, i18n, template, chartAndData, modalAlert) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/ServiceSales';

  var main_tables;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click button#search' : 'search',
    	'click button#csv-exp' : 'csv_exp',
    	'change #summary_select' : 'selectOptTp',
    	'click .add-on-datepicker' : 'trigger_calender'
    		
    	/*,
    	'click #serviceSales_table button.serviceSales-detail' : 'load_detail',
    	'click #new-serviceSales' : 'modal_init',
    	'click #search_oper' : 'search_oper'*/
    },
    initialize: function () {
      this.trigger('remove-compnents');
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
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

          $('table tr').each(function() {
              $('td:eq(' + index + ')',this).toggle();
              $('th:eq(' + index + ')',this).toggle();
          });
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
    		_data.storeId = $(this).val();
    		$('#search-table').empty();
        });
    },

    selectOptTp : function() {

    	var optTp = $('#summary_select').val();

    	if(optTp == 'summary'){
    		$('#forSummaryOption').show();
    		$('#option2_select').val('');
    		$('#forEtcOption').hide();    		
    	}else{
    		$('#forSummaryOption').hide();
    		$('#forEtcOption').show();
    	}
    },
    
    search : function() {
    	var view = this;

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
			        data.summaryYn = data.option1 == 'summary' || data.option1 == null ? true : false;
			        data.searchYn = true;
			        view.summaryYn = data.summaryYn;
			        view.searchYn = true;

			        data.i18n = i18n;
			        data = Common.JC_format.handleData(data);
					$('#search-table').html(Mustache.to_html(chartAndData, data));
					
					//테이블헤더
			        data.tableHeader = result.tableHeader;
			    	//테이블 데이터
			        data.tableList   = result.tableList;
					
					view.tableHtml(result.list, data.option1 ,data.tableHeader, data.tableList);
					
					view.convertChart(data.summaryYn, result);

				}
			}
		});
    },
    tableHtml: function (data, option1, tableHeader, tableList) {
    	var html = '';
    	
    	if (data.length > 0) {
    		var optTp = $('#summary_select').val();
    		
    		if(optTp == 'summary'){
    			
    			html += "<thead>";
        		
		    	// 1.Create Table-Header
		    	html += "<tr class='dataTables_wrapper_th_min'>";
		    	html += "<th>" + i18n.BO2385 + "</th>"; // Division
		    	html += "<th class='col1'>" + i18n.BO2313 + "</th>"; // Total
		    	html += "<th class='col1'>" + i18n.BO2303 + "</th>"; // Total
		    	html += "<th class='col1'>" + i18n.BO2314 + "</th>"; // Total
		    	html += "</tr>";
		    	html += "</thead>";
			
		    	html += "<tbody>";
    			$.each(data, function(idx, val) {
            		
            		if(val.subTitle == "OffLine"){
            			html += '<tr class="odd gradeX back_gray">';
            		}else if(val.subTitle == "OnLine"){
            			html += '<tr class="odd gradeX back_gray">';
            		}else if(val.subTitle == "ToTal"){
            			html += '<tr class="odd gradeX back_blue">';
            		}
        			
            		for (var key in val) {
        				if(key != 'subTitle'){
        					
        					if(key == 'orderCnt'){
        						html += '<td>'+ Common.JC_format.number(val[key]) +'</td>';
        					}else if(key == 'sales'){
        						html += '<td>'+ Common.JC_format.number(val[key]) +'</td>';
        					}else if(key == 'averageSales'){
        						html += '<td>'+ Common.JC_format.number(val[key]) +'</td>';
        					}else{
        						html += '<td>'+ val[key] +'</td>';
        					}
        				}
        			}
        			
        			html += '</tr>';
            		
        		});
    			html += "</tbody>";
    		}else{
    			if(tableHeader != null) {

    		    	html += '<thead>';
    		
    		    	// 1.Create Table-Header
    		    	html += "<tr class='dataTables_wrapper_th_min'>";
    		    	html += "<th>" + i18n.BO2385 + "</th>"; // Division
    		    	html += "<th>" + i18n.BO2278 + "</th>"; // Total
    		    	
    		    	$.each(tableHeader, function(idx, val) {				
						var value = val.LABEL;
						html += "<th>"+value+"</th>";
    		    	});
    		
    		    	html += "</tr>";
    		    	html += "</thead>";
    		
    		    	html += '<tbody>';
    		    	// END OF 1.Create Table-Header
    		    	
    		    	// 2.Create Table-ListData
    				$.each(tableList, function(idx, val) {
    					
    					if(val.SUB_TITLE == "OffLine"){
                			html += '<tr class="odd gradeX back_gray">';
                		}else if(val.SUB_TITLE == "OnLine"){
                			html += '<tr class="odd gradeX back_gray">';
                		}else if(val.SUB_TITLE == "Total"){
                			html += '<tr class="odd gradeX back_blue">';
                		}
    					
    					for (var key in val) {
    						//First header at Left
    						if( key == "HEADER"){
    							html += "<td class='title'>" + val[key] + "</td>";
    						}else if(key == "SUB_TITLE"){
    							continue;
    						} else {
    							html += "<td class='price'>" +  Common.JC_format.number(val[key]) + "</td>";
    						}											
    					}
    					
    					html += "</tr>";
    				});
    				// END OF 2.Create Table-ListData
    				
    				html += '</tbody>';
    	    	}
    		}
    		        	
    	}else{
    		html += '<tr class="even gradeC"><td colspan="4">'+i18n.BO0006+'</td></tr>';
    	}
		
    	$('#summary-table').html(html);
    },
    modal_init : function(){
    	var view = this;
    	if(_data.brandId != undefined && _data.brandId != ''){
    		var data = view.loadBaseCode();

			$('#serviceSales-modal').html(Mustache.to_html(serviceSalesModal, data));
			view.form_init('POST', $('#serviceSales-modal'));

    	} else {
    		alert(i18n.BO4002);
    		$('#serviceSales-modal').html('');
    		$('#serviceSales-modal').modal('toggle');
    	}
    },
    csv_exp: function () {
    	if (this.searchYn) {
    		$('#summary-table').tableToCSV();
    	}  
    	else {
    		alert(i18n.BO4030);
    	}
    },
    convertChart : function(summaryYn, charData) {

        if (summaryYn) {
        	var _label = [];
        	var _offline = [];
        	var _online = [];
        	
        	$.each(charData.chartList,function(idx, value){

        		$.each(charData.chartList[idx],function(label, value){
        			if(label == "label"){
            			var _cObj = {};
        	    		_cObj.label = value;        	    		
        	    		_label.push(_cObj);
        			}else if(label == "offline"){
            			var _cObj = {};
        	    		_cObj.value = value;
        	    		_offline.push(_cObj);
        			}else if(label == "online"){
            			var _cObj = {};        	    		
        	    		_cObj.value = value;
        	    		_online.push(_cObj);
        			}
        		})        		
        	});
			

    		var charts = new FusionCharts({
    			type: 'stackedcolumn2d',
    	        dataFormat: 'json',
				renderAt: 'chart01',
				width: '100%',
				height: '360',
    	        dataSource: {
    	            "chart": {
    	                "caption": i18n.BO1008
    	                ,"subCaption": i18n.BO2486
    	                ,"captionFontSize": "10"
    	                ,"subcaptionFontSize": "10"
    	                ,"subcaptionFontBold": "0"
    	                ,"xaxisname": i18n.BO2094
    	                ,"yaxisname": i18n.BO2303
    	                ,"showvalues": "0"
//    	                ,"numberprefix": i18n.BO0030
    	                ,"legendBgAlpha": "0"
    	                ,"legendBorderAlpha": "0"
    	                ,"legendShadow": "0"
    	                ,"showborder": "0"
    	                ,"bgcolor": "#ffffff"
    	                ,"showalternatehgridcolor": "0"
    	                ,"showplotborder": "0"
    	                ,"showcanvasborder": "0"
    	                ,"legendshadow": "0"
    	                ,"plotgradientcolor": ""
    	                ,"showCanvasBorder": "0"
    	                ,"showAxisLines": "0"
    	                ,"showAlternateHGridColor": "0"
    	                ,"divlineAlpha": "100"
    	                ,"divlineThickness": "1"
    	                ,"divLineIsDashed": "1"
    	                ,"divLineDashLen": "1"
    	                ,"divLineGapLen": "1"
    	                ,"lineThickness": "3"
    	                ,"flatScrollBars": "1"
    	                ,"scrollheight": "10"
    	                ,"numVisiblePlot": "12"
    	                ,"showHoverEffect":"1"
    	                ,"labelDisplay": "rotate"
                    	,"slantlabels": "1"
    	            },
    	            "categories": [
    	                {
    	                    "category": _label
    	                }
    	            ],
    	            "dataset": [
    	                {
    	                    "seriesname": "Offline",
    	                    "color": "008ee4",
    	                    "data": _offline
    	                },
    	                {
    	                    "seriesname": "Online",
    	                    "color": "f8bd19",
    	                    "data": _online
    	                }
    	            ]
    	        }

    			});
    			charts.render();
        }
        else {

        	var dayMonth = charData.dayMonth;
        	var chartData2 = {};
        	var monthChart = {};
        	
        	if(dayMonth == "day"){
        		var _label = [];
            	var _offline = [];
            	var _online = [];            	
            	
        		$.each(charData.dayChart,function(idx, value){
        			$.each(charData.dayChart[idx],function(label, value){
        				if(label == "label"){
                			var _cObj = {};
            	    		_cObj.label = value;        	    		
            	    		_label.push(_cObj);
            			}else if(label == "offline"){
                			var _cObj = {};
            	    		_cObj.value = value;
            	    		_offline.push(_cObj);
            			}else if(label == "online"){
                			var _cObj = {};        	    		
            	    		_cObj.value = value;
            	    		_online.push(_cObj);
            			}
            		})
            	});
        		
        		monthChart.label = _label;
        		monthChart.offline = _offline;
        		monthChart.online = _online;
        		
        		chartData2.dayChart = monthChart;
  
        	}else{
        		var _label = [];
            	var _offline = [];
            	var _online = [];
            	
        		$.each(charData.monthChart,function(idx, value){
            		$.each(charData.monthChart[idx],function(label, value){
            			
            			if(label == "label"){
                			var _cObj = {};
            	    		_cObj.label = value;        	    		
            	    		_label.push(_cObj);
            			}else if(label == "offline"){
                			var _cObj = {};
            	    		_cObj.value = value;
            	    		_offline.push(_cObj);
            			}else if(label == "online"){
                			var _cObj = {};        	    		
            	    		_cObj.value = value;
            	    		_online.push(_cObj);
            			}
            		})  
            		
            	});
        		
        		monthChart.label = _label;
        		monthChart.offline = _offline;
        		monthChart.online = _online;
        		
        		chartData2.monthChart = monthChart;
        	}
        	
        	var hourChart = {};
        	var _label_hr = [];
        	var _offline_hr = [];
        	var _online_hr = [];
        	
        	$.each(charData.hourChart,function(idx, value){
        		$.each(charData.hourChart[idx],function(label, value){
        			if(label == "label"){
            			var _cObj = {};
        	    		_cObj.label = value;        	    		
        	    		_label_hr.push(_cObj);
        			}else if(label == "offline"){
            			var _cObj = {};
        	    		_cObj.value = value;
        	    		_offline_hr.push(_cObj);
        			}else if(label == "online"){
            			var _cObj = {};        	    		
        	    		_cObj.value = value;
        	    		_online_hr.push(_cObj);
        			}
        		})        		
        	});
        	hourChart.label = _label_hr;
        	hourChart.offline = _offline_hr;
    		hourChart.online = _online_hr;
    		
    		chartData2.hourChart = hourChart;
    		
    		var dayofweekChart = {};
    		var _label_dayWeek = [];
        	var _offline_dayWeek = [];
        	var _online_dayWeek = [];
        	
    		$.each(charData.dayofweekChart,function(idx, value){
        		$.each(charData.dayofweekChart[idx],function(label, value){
        			if(label == "label"){
            			var _cObj = {};
        	    		_cObj.label = value;        	    		
        	    		_label_dayWeek.push(_cObj);
        			}else if(label == "offline"){
            			var _cObj = {};
        	    		_cObj.value = value;
        	    		_offline_dayWeek.push(_cObj);
        			}else if(label == "online"){
            			var _cObj = {};        	    		
        	    		_cObj.value = value;
        	    		_online_dayWeek.push(_cObj);
        			}
        		})        		
        	});
        	dayofweekChart.label = _label_dayWeek;
    		dayofweekChart.offline = _offline_dayWeek;
    		dayofweekChart.online = _online_dayWeek;
    		
    		chartData2.dayofweekChart = dayofweekChart;  
        	
        	var dayMonthChart = new FusionCharts({
        		type: 'stackedcolumn2d',
        		renderAt: 'dayMonthChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"caption": i18n.BO1008
        				,"subCaption": dayMonth == 'day' ? i18n.BO2487 : i18n.BO2489
    	                ,"captionFontSize": "10"
    	                ,"subcaptionFontSize": "10"
    	                ,"subcaptionFontBold": "0"
    	                ,"xAxisName": dayMonth == 'day' ? i18n.BO2491 : i18n.BO2493
    	                ,"yaxisname": i18n.BO2303
    	                ,"showvalues": "0"
//    	                ,"numberprefix": i18n.BO0030
    	                ,"legendBgAlpha": "0"
    	                ,"legendBorderAlpha": "0"
    	                ,"legendShadow": "0"
    	                ,"showborder": "0"
    	                ,"bgcolor": "#ffffff"
    	                ,"showalternatehgridcolor": "0"
    	                ,"showplotborder": "0"
    	                ,"showcanvasborder": "0"
    	                ,"legendshadow": "0"
    	                ,"plotgradientcolor": ""
    	                ,"showCanvasBorder": "0"
    	                ,"showAxisLines": "0"
    	                ,"showAlternateHGridColor": "0"
    	                ,"divlineAlpha": "100"
    	                ,"divlineThickness": "1"
    	                ,"divLineIsDashed": "1"
    	                ,"divLineDashLen": "1"
    	                ,"divLineGapLen": "1"
    	                ,"lineThickness": "3"
    	                ,"flatScrollBars": "1"
    	                ,"scrollheight": "10"
    	                ,"numVisiblePlot": "12"
    	                ,"showHoverEffect":"1"
    	                ,"labelDisplay": "rotate"
                    	,"slantlabels": "1"
        			},
        			"categories": [
          	    	                {
          	    	                    "category": dayMonth == 'day' ? chartData2.dayChart.label : chartData2.monthChart.label
          	    	                }
          	    	            ],
    	            "dataset": [
    	                {
    	                    "seriesname": "Offline",
    	                    "color": "008ee4",
    	                    "data": dayMonth == 'day' ? chartData2.dayChart.offline : chartData2.monthChart.offline
    	                },
    	                {
    	                    "seriesname": "Online",
    	                    "color": "f8bd19",
    	                    "data": dayMonth == 'day' ? chartData2.dayChart.online : chartData2.monthChart.online
    	                }
    	            ]
//        			"data": dayMonth == 'day' ? charData.dayChart : charData.monthChart,
        		}
        	});
        	dayMonthChart.render();

        	var dayofweekChart = new FusionCharts({
        		type: 'stackedcolumn2d',
        		renderAt: 'dayofweekChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"caption": i18n.BO1008
        				,"subCaption": i18n.BO2488
    	                ,"captionFontSize": "10"
    	                ,"subcaptionFontSize": "10"
    	                ,"subcaptionFontBold": "0"
    	                ,"xAxisName": i18n.BO2492
    	                ,"yaxisname": i18n.BO2303
    	                ,"showvalues": "0"
//    	                ,"numberprefix": i18n.BO0030
    	                ,"legendBgAlpha": "0"
    	                ,"legendBorderAlpha": "0"
    	                ,"legendShadow": "0"
    	                ,"showborder": "0"
    	                ,"bgcolor": "#ffffff"
    	                ,"showalternatehgridcolor": "0"
    	                ,"showplotborder": "0"
    	                ,"showcanvasborder": "0"
    	                ,"legendshadow": "0"
    	                ,"plotgradientcolor": ""
    	                ,"showCanvasBorder": "0"
    	                ,"showAxisLines": "0"
    	                ,"showAlternateHGridColor": "0"
    	                ,"divlineAlpha": "100"
    	                ,"divlineThickness": "1"
    	                ,"divLineIsDashed": "1"
    	                ,"divLineDashLen": "1"
    	                ,"divLineGapLen": "1"
    	                ,"lineThickness": "3"
    	                ,"flatScrollBars": "1"
    	                ,"scrollheight": "10"
    	                ,"numVisiblePlot": "12"
    	                ,"showHoverEffect":"1"
        			},
        			"categories": [
        	    	                {
        	    	                    "category": chartData2.dayofweekChart.label
        	    	                }
        	    	            ],
        	    	            "dataset": [
        	    	                {
        	    	                    "seriesname": "Offline",
        	    	                    "color": "008ee4",
        	    	                    "data": chartData2.dayofweekChart.offline
        	    	                },
        	    	                {
        	    	                    "seriesname": "Online",
        	    	                    "color": "f8bd19",
        	    	                    "data": chartData2.dayofweekChart.online
        	    	                }
        	    	            ]
        		}
        	});
        	dayofweekChart.render();

        	var hourChart = new FusionCharts({
        		type: 'stackedcolumn2d',
        		renderAt: 'hourChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"caption": i18n.BO1008
        				,"subCaption": i18n.BO2486
    	                ,"captionFontSize": "10"
    	                ,"subcaptionFontSize": "10"
    	                ,"subcaptionFontBold": "0"
    	                ,"xAxisName": i18n.BO2490
    	                ,"yaxisname": i18n.BO2303
    	                ,"showvalues": "0"
//    	                ,"numberprefix": i18n.BO0030
    	                ,"legendBgAlpha": "0"
    	                ,"legendBorderAlpha": "0"
    	                ,"legendShadow": "0"
    	                ,"showborder": "0"
    	                ,"bgcolor": "#ffffff"
    	                ,"showalternatehgridcolor": "0"
    	                ,"showplotborder": "0"
    	                ,"showcanvasborder": "0"
    	                ,"legendshadow": "0"
    	                ,"plotgradientcolor": ""
    	                ,"showCanvasBorder": "0"
    	                ,"showAxisLines": "0"
    	                ,"showAlternateHGridColor": "0"
    	                ,"divlineAlpha": "100"
    	                ,"divlineThickness": "1"
    	                ,"divLineIsDashed": "1"
    	                ,"divLineDashLen": "1"
    	                ,"divLineGapLen": "1"
    	                ,"lineThickness": "3"
    	                ,"flatScrollBars": "1"
    	                ,"scrollheight": "10"
    	                ,"numVisiblePlot": "12"
    	                ,"showHoverEffect":"1"
    	                ,"labelDisplay": "rotate"
                    	,"slantlabels": "1"
        			},
        			"categories": [
       	    	                {
       	    	                    "category": chartData2.hourChart.label
       	    	                }
       	    	            ],
       	    	            "dataset": [
       	    	                {
       	    	                    "seriesname": "Offline",
       	    	                    "color": "008ee4",
       	    	                    "data": chartData2.hourChart.offline
       	    	                },
       	    	                {
       	    	                    "seriesname": "Online",
       	    	                    "color": "f8bd19",
       	    	                    "data": chartData2.hourChart.online
       	    	                }
       	    	            ]
//        			"data": charData.hourChart
        		}
        	});
        	hourChart.render();
        }
    },
    load_detail : function(event){
    	var view = this;
    	var _this = $(event.target);
    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({
    				id : _this.attr("target-code"),
    			})
    		},
    		success : function(response){
    			if(response.success){
    				var data = view.loadBaseCode();
    				var _template = Mustache.to_html(serviceSalesModal, $.extend(data, _def_form_mustache));

    				$('#serviceSales-modal').html(Mustache.to_html(_template, view.buildOptions(response.data)));
    				view.form_init('PUT', $('#serviceSales-modal'));
    			}
    		}
    	});
    },
    form_init : function(method, modal){
    	var view = this;

    	modal.find('select').select2();

		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $("modal-body");

		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {
        		var params = $form.serializeObject();

	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response){
	        			if(response.success){
	    		    		$('#serviceSales-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}
	        		}
	        	});

	        	return false;
	        }
		});

		$('#timepicker1').daterangepicker({
			autoApply : true,

			locale: {
	              format: 'YYYY-MM-DD',
	              separator: ' - ',
	              applyLabel: i18n.BO0023,
	              cancelLabel: i18n.BO0026,
	              fromLabel: i18n.BO3032,
	              toLabel: i18n.BO3033,
	              daysOfWeek: [i18n.BO3031, i18n.BO3025, i18n.BO3026, i18n.BO3027, i18n.BO3028, i18n.BO3029, i18n.BO3030],
	              monthNames: [i18n.BO3013, i18n.BO3014, i18n.BO3015, i18n.BO3016, i18n.BO3017, i18n.BO3018, i18n.BO3019, i18n.BO3020, i18n.BO3021, i18n.BO3022, i18n.BO3023, i18n.BO3024],
	              firstDay: 1
	       }
		},
		function(start, end, label) {
		});

//		view.load_map();
    },
    loadBaseCode : function(){
    	var data = {};
		$.ajax({
			url : './model/management/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'DiscountTp,ExpireTp'
			},
			success : function(result){
				if(result.success){

					$.each(Object.keys(result.codes), function(idx, code){
						$.each(result.codes[code], function(_idx, _code){
							_code.selections = '{{#'+code+'='+_code.baseCd+'}} selected="selected" {{/'+code+'='+_code.baseCd+'}}';
						});
					});

					data = $.extend(result.codes, _data);
				} else {
		    		$('#serviceSales-modal').html('');
		    		$('#serviceSales-modal').modal('toggle');
				}
			}
		});

		return data;
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');
    }
  });

  return ContentView;
});