/*
 * Filename	: rewardSumManage.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
    'backbone',
    'mustache',
    'common',
    'i18n!templates/globals/lang/nls/language',
    
    'text!templates/reward/rewardSumManage.html',
    'text!templates/reward/rewardSumChartAndData.html',
        
    'bootstrap-daterangepicker'
    ,'fusioncharts'
    ,'fusioncharts.theme.fint'
    //,'fusioncharts-jquery-plugin'
    ,'jquery.tabletoCSV'
], function (Backbone, Mustache, Common, i18n, template, chartAndData ) {
    'use strict';

    var _data = {};

    var targetUrl = './model/reward/RewardSum';

	var ContentView = Backbone.View.extend({
		el: $('#main-wrapper'),
	    initialize: function () {
	        this.trigger('remove-event');
	    	this.template = template;
	    	this.listenTo(this.model, 'sync', this.render);
	    },
	    events: {
	    	'click button#search' : 'search',
    		'click button#csv-exp' : 'csv_exp',
    		'change #summary_select' : 'selectOptTp',
    		'click .add-on-datepicker' : 'trigger_calender'
	    },
	    render: function () {
	    	var view = this;
	    	var data = {};
	        data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
	        data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
	        
	        _data.userInfo = view.model.toJSON()[0].session.userInfo;
	        
	        _data.franId = _data.userInfo.franId;
	        _data.brandId = _data.userInfo.brandId;
	        
	        data.userInfo = view.model.toJSON()[0].session.userInfo;
	        
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
	        
	        if(data.frList == undefined && data.brList == undefined){
	      	  data.isSt = true;
	        }else{
	      	  data.isSt = false;
	        }
	        data.i18n = i18n;
	        
	    	var rendered = Mustache.to_html(view.template, data);
	    	$(view.el).append(rendered);

	    	view.setCalendarArea();
	    	view.selectedRollInit();
	    	
	    	// 브랜드 사용자가 로그인시 자동조회
	        if(data.isSt == true || data.isBr == true){
	      	  view.search();
	        }
	        
	    	return view;
	    },
	    
	    setCalendarArea : Common.JC_calendar.searchRange(i18n),
	    
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
	      				}
	      			}  //END : brand select result
	      		});  //END : brand select ajax
	      		$('#search-table').empty();
	      	});  //END : franchise select change
	    	
	    	$('#brand_select').select2().on('change', function(){
				//브랜드와 해당 매장에 관련된 카테고리 분류정보 셋팅
				_data.brandId =  $(this).val();
				$('#search-table').empty();
	        });
	    },
	    search : function() {
	    	var view = this;
	    	var option1 = $("[name=option1] option:selected").val();
	    	var option2 = $("[name=option2] option:selected").val();

	    	if(_data.franId == "" || _data.franId == undefined){
	    		alert(i18n.BO4001);
	    		return false;
	    	}
	    	
	    	if(_data.brandId == "" || _data.brandId == undefined){
	    		alert(i18n.BO4002);
	    		return false;
	    	}
	    	if (option1 != 'summary') {
		    	if (option2 == '') {
		    		alert(i18n.BO4031);
		    		return false;
		    	}
	    	}
	    	if ($('#daterangepicker').val() == 'Invalid date - Invalid date') {
	    		alert(i18n.BO4032);
	    		return false;
	    	}
	    	if (option2 != '' && option1 == 'summary') {
	    		alert(i18n.BO4033);
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
						data.i18n = i18n;
				        data.option1 = $('[name=option1] option:selected').val();
				        data.option2 = $('[name=option2] option:selected').val();
				        data.summaryYn = data.option1 == 'summary' || data.option1 == null ? true : false;
				        data.searchYn = true;
				        view.summaryYn = data.summaryYn;
				        view.searchYn = true;

						$('#search-table').html(Mustache.to_html(chartAndData, data));
						view.tableHtml(result.list, data.option1);
						
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
	    tableHtml: function (data, option1) {
	    	var html = '';

	    	html += '<thead>';
	    	$.each(data, function(idx, val) {
	    		if (idx == 0) {
					html += '<tr class="dataTables_wrapper_th_min">';
					for (var key in val) {
						var value = val[key];

						if (option1 == 'weekly' && key != 'header') value = value.split('/', -1)[1];

						html += '<th>'+value+'</th>';
					}
					html += '</tr>';
	    		}
	    		return false;
	    	});
	    	html += '</thead>';

	    	html += '<tbody>';
			$.each(data, function(idx, val) {
	    		if (idx > 0) {
	    			if (idx == 1) html += '<tr class="odd gradeX back_blue">';
	    			else html += '<tr class="even gradeC">';

					for (var key in val) {
						html += '<td>'+val[key]+'</td>';
					}
					html += '</tr>';
	    		}
			});
			html += '</tbody>';

	    	$('#etc-table').html(html);
	    },
	    convertChart : function(summaryYn, data) {

            if (summaryYn) {
        		for (var i = 1; i < 4; i++) {
        			var chartList = '';
        			var caption = '';
        			var charts = 'chart0' + i;

        			if (i == 1) {
        				chartList = data.chartList01;
        				caption = i18n.BO2195;
        			}
        			else if (i == 2) {
        				chartList = data.chartList02;
        				caption = i18n.BO2189;
        			}
        			else if (i == 3) {
        				chartList = data.chartList03;
        				caption = i18n.BO2270;
        			}

        			charts = new FusionCharts({
        				'type': 'column2d',
        				'renderAt': 'chart0' + i,
        				'width': '100%',
        				'height': '230',
        				'dataFormat': 'json',
        				'dataSource': {
        					'chart': {
        						'caption': caption,
        						'xAxisName': 'Store',
        						'yAxisName': 'Count',
        						'theme': 'fint',
        				        'exportEnabled': '1'
        					},
        					'data': chartList
        				}
        			});
        			charts.render();
        		}
            }
            else {
            	var dayMonth = data.dayMonth;

            	var dayMonthChart = new FusionCharts({
            		type: 'line',
            		renderAt: 'dayMonthChart',
            		width: '100%',
            		height: '230',
            		dataFormat: 'json',
            		dataSource: {
            			"chart": {
            				"theme": "fint",
            				"caption": "",
            				"subCaption": dayMonth == 'day' ? "DAY" : "MONTH",
            				"xAxisName": dayMonth == 'day' ? "DAY" : "MONTH",
            				"yAxisName": "Count",
            				"showValues": "0",
    				        "exportEnabled": "1"
            			},
            			"data": dayMonth == 'day' ? data.dayChart : data.monthChart,
            		}
            	});
            	dayMonthChart.render();

            	var dayofweekChart = new FusionCharts({
            		type: 'column2d',
            		renderAt: 'dayofweekChart',
            		width: '100%',
            		height: '230',
            		dataFormat: 'json',
            		dataSource: {
            			"chart": {
            				"theme": "fint",
            				"caption": "",
            				"subCaption": "DAY OF WEEK",
            				"xAxisName": "Day Of Week",
            				"yAxisName": "Count",
            				"showValues": "0",
    				        "exportEnabled": "1"
            			},
            			"data": data.dayofweekChart
            		}
            	});
            	dayofweekChart.render();

            	var hourChart = new FusionCharts({
            		type: 'line',
            		renderAt: 'hourChart',
            		width: '100%',
            		height: '230',
            		dataFormat: 'json',
            		dataSource: {
            			"chart": {
            				"theme": "fint",
            				"caption": "",
            				"subCaption": "TIME DF DAY",
            				"xAxisName": "Hour",
            				"yAxisName": "Count",
            				"showValues": "0",
    				        "exportEnabled": "1",
   	                        "labelDisplay": "rotate",
                           	"slantlabels": "1"
            			},
            			"data": data.hourChart
            		}
            	});
            	hourChart.render();
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