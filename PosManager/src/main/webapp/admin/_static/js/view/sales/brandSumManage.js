/*
 * Filename	: brandSumManage.js
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
		  
		  ,'text!templates/sales/brandSumManage.html'
		  ,'text!templates/sales/brandSumChartAndData.html'
		  
		  ,'bootstrap-daterangepicker'
		  ,'fusioncharts'
		  ,'fusioncharts.theme.fint'
		  ,'jquery.tabletoCSV'

], function (Backbone, Mustache, Common, i18n, template, chartAndData) {
  'use strict';

  var _data = {};

  var targetUrl = './model/sales/BrandSum';

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
      					// 브랜드ID, 전역변수 초기화 
      					_data.brandId = '';
      					
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
			        data.option2 = $('[name=option2] option:selected').val();
			        data.summaryYn = data.option1 == 'summary' || data.option1 == null ? true : false;
			        data.searchYn = true;
			        view.summaryYn = data.summaryYn;
			        view.searchYn = true;

//			        $('form').attr('id', 're-search');
			        data.i18n = i18n;
			        
			        // add format in data
			        data = Common.JC_format.handleData(data);
			        
					$('#search-table').html(Mustache.to_html(chartAndData, data));
					//테이블헤더
			        data.tableHeader = result.tableHeader;
			    	//테이블 데이터
			        data.tableList   = result.tableList;
			        
					view.tableHtml(result.list, data.option1 ,data.tableHeader, data.tableList);

//					$('#be-search').remove();
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
    tableHtml: function (data, option1,tableHeader, tableList) {	
    	
    	var html = '';
    	if(tableHeader != null) {

    	html += '<thead>';

    	// 1.Create Table-Header
    	html += "<tr class='dataTables_wrapper_th_min'>";
    	html += "<th>" + i18n.BO2040 + "</th>"; //Store Name
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
        	var _cData = [];
        	$.each(charData.list[charData.list.length - 1],function(label, value){
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
    						'caption': i18n.BO1004,
    						'xAxisName': i18n.BO2455,
    						'yAxisName': i18n.BO2303,
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

        	var dayMonth = charData.dayMonth;

        	var dayMonthChart = new FusionCharts({
        		type: 'line',
        		renderAt: 'dayMonthChart',
        		width: '100%',
        		height: '230',
        		dataFormat: 'json',
        		dataSource: {
        			"chart": {
        				"theme": "fint",
        				"caption": i18n.BO1004,
        				"subCaption": dayMonth == 'day' ? i18n.BO2487 : i18n.BO2489,
        				"xAxisName": dayMonth == 'day' ? i18n.BO2491 : i18n.BO2493,
        				"yAxisName": i18n.BO2303,
        				"showValues": "0",
//        				"numberprefix": i18n.BO0030,
				        "exportEnabled": "1",
				        "labelDisplay": "rotate",
                        "slantlabels": "1"
        			},
        			"data": dayMonth == 'day' ? charData.dayChart : charData.monthChart,
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
        				"caption": i18n.BO1004,
        				"subCaption": i18n.BO2488,
        				"xAxisName": i18n.BO2492,
        				"yAxisName": i18n.BO2303,
        				"showValues": "0",
//        				"numberprefix": i18n.BO0030,
				        "exportEnabled": "1"
        			},
        			"data": charData.dayofweekChart
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
        				"caption": i18n.BO1004,
        				"subCaption": i18n.BO2486,
        				"xAxisName": i18n.BO2490,
        				"yAxisName": i18n.BO2303,
        				"showValues": "0",
//        				"numberprefix": i18n.BO0030,
				        "exportEnabled": "1",
				        "labelDisplay": "rotate",
	                    "slantlabels": "1"
        			},
        			"data": charData.hourChart
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