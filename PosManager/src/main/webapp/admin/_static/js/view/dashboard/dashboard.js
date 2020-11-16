/*
 * Filename	: dashboard.js
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
  
  'text!templates/dashboard/dashboard.html',
  'text!templates/dashboard/franchiseDashboard.html',
  'text!templates/dashboard/brandDashboard.html',
  'text!templates/dashboard/storeDashboard.html',

  ,'fusioncharts'
  ,'fusioncharts.theme.fint'  
  
], function (Backbone, Mustache, Common, i18n, template, franform, brandform, storeform) {
  'use strict';
  
  
  var _data = {};
  var targetUrl = './model/Dashboard';
  var dashboard = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click .minimize' : 'slideUpAndDown'
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
      $(view.el).tooltip({ selector : '[data-toggle=tooltip]' });
      
      /*
       * 유저별 권한 (user role)
       * 1 : admin
       * 2 : franchise
       * 3 : brand
       * 4  :store
       * */
      if(data.userInfo.userRole != "1"){
    	  view.getDashboard();
      }
      
      view.selectedRollInit();
      return view;
    },
    
    slideUpAndDown : function(event) {
    	var t = $(event.target);
        var p = t.closest('.panel');
        if(!t.hasClass('maximize')) {
           p.find('.panel-body, .panel-footer').slideUp(200);
           t.addClass('maximize');
           t.html('&plus;');
        } else {
           p.find('.panel-body, .panel-footer').slideDown(200);
           t.removeClass('maximize');
           t.html('&minus;');
        }
        return false;
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
      					var defualt_array = [{id: '', brandNm : i18n.BO1032}];
      					$("#brand_select").find('option').remove();
      					$('#brand_select').select2({
      						data : defualt_array.concat(result.list),
      						templateResult : function(state) {
      							return state.brandNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.brandNm;
      						}
      					})
      				}
      			}  //END : brand select result
      		});  //END : brand select ajax
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
      					})
      				}
      			}  //END : store select result
      		});  //END : store select ajax
        });  //END : brand select change
    	
    	$('#store_select').select2().on('change', function(){
    		_data.storeId = $(this).val();
        });
    },

    getDashboard : function(){
    	
    	var view = this;	

    	$.ajax({
    		url : targetUrl,
    		data : _data,
    		success : function(result) {

    	    	if (result.success) {
    	    		
    	    		var data = {};    	    		
    	    		data = Common.JC_format.handleData(data);
    	    		
    	    		if(_data.userInfo.userRole == "1"){
    	    			
    	            }else if(_data.userInfo.userRole == "2"){
    	            	data.brandlist = Common.JC_format.handleData(result.brandlist);
    	            	data.today = result.today;
    	            	data.i18n = i18n;
    	            	$('#dashboard-form-body').html(Mustache.to_html(franform, data));
    	            }else if(_data.userInfo.userRole == "3"){
    	            	
    	            	data = Common.JC_format.handleData(result);
    	            	data = view.buildRating(data);
    	            	data.i18n = i18n;
    	            	$('#dashboard-form-body').html(Mustache.to_html(brandform, data));
    	            	
    	            	view.convertChartToBrand(data);
    	            	view.convertChartToStore(data);
    	            }else if(_data.userInfo.userRole == "4"){
    	            	
    	            	data = Common.JC_format.handleData(result);
    	            	data = view.buildRating(data);
    	            	data.i18n = i18n;
    	            
    	            	$('#dashboard-form-body').html(Mustache.to_html(storeform, data));
    	            	
    	            	view.convertChartToStore(data);
    	            }
    			}
    			else {
    				alert(i18n.BO4020);
    				$('#dashboard-form-body').html('');
    				$('#dashboard-form-body').hide();
    			}
    		}
    	})
    },
    
    // 리뷰 평점대로 별 생성
    buildRating: function (data) {
    	var reviewList = data.reviewList;
    	for (var i = 0; i < reviewList.length; i++) {
    		var review = reviewList[i];
    		var div = $("<div>");
    		for (var j = 0; j < review.rating; j++) {
    			div.append('<span class="glyphicon glyphicon-star"></span>');
    		}
    		
    		var emptyRating = 5 - review.rating;
    		for (var j = 0; j < emptyRating; j++) {
    			div.append('<span class="glyphicon glyphicon-star-empty"></span>');
    		}
    		review.buildRating = div.html();
    	}
    	return data;
    },
    
    convertChartToBrand : function(charData){
    	var trendChart = new FusionCharts({
    		type: 'line',
    		renderAt: 'weekOfSalesChart',
    		width: '100%',
    		height: '250',
    		dataFormat: 'json',
    		dataSource: {
    			"chart": {
    				"theme": "fint",
    				"caption": "",
    				"subCaption": "DAY",
    				"xAxisName": "DAY",
    				"yAxisName": "Sales",
    				"showValues": "0",
			        "exportEnabled": "1"
    			},
    			"data": charData.trendList,
    		}
    	});
    	trendChart.render();
    	
    	// Today Sales Top 5 
		var salesTopChart = new FusionCharts({
			'type': 'column2d',
			'renderAt': 'salesTopChart',
			'width': '100%',
			'height': '250',
			'dataFormat': 'json',
			'dataSource': {
				'chart': {
					'caption': i18n.BO1004,
					'xAxisName': 'Store',
					'yAxisName': 'Sum',
					'theme': 'fint',
			        'exportEnabled': '1'
				},
				'data': charData.salesTopChart
			}
		});
		salesTopChart.render();
    	
		// Today Sales Bottom 5 
		var salesBottomChart = new FusionCharts({
			'type': 'column2d',
			'renderAt': 'salesBottomChart',
			'width': '100%',
			'height': '250',
			'dataFormat': 'json',
			'dataSource': {
				'chart': {
					'caption': i18n.BO1004,
					'xAxisName': 'Store',
					'yAxisName': 'Sum',
					'theme': 'fint',
			        'exportEnabled': '1'
				},
				'data': charData.salesBottomChart
			}
		});
		salesBottomChart.render();
		
		var label = [];
		var dataOfflineCustomers = [];
		var dataOnlineCustomers = [];
		var dataOffline = [];
		var dataOnline = [];
		
		$.each(charData.thisWeekCustomerList, function (index, value) {
			var labelObj = { 'label' : charData.thisWeekCustomerList[index].label };
			var tempOffline = { 'value' : charData.thisWeekCustomerList[index].offline};
			var tempOnline = { 'value' : charData.thisWeekCustomerList[index].online};
			
			label.push(labelObj);
			dataOffline.push(tempOffline);
			dataOnline.push(tempOnline);
		});
		
		var dataOffline = { 'seriesname' : i18n.BO2316, 'data' : dataOffline};
		var dataOnline = { 'seriesname' : i18n.BO2447, 'data' : dataOnline};
			
		dataOfflineCustomers.push(dataOffline);
		dataOnlineCustomers.push(dataOnline);
		
		var dataLastWeekCustomers = [];
		var dataLastWeekOnOffline = [];
		var dataLastWeekOnOffline = [];
		
		$.each(charData.lastWeekCustomerList, function (index, value) {
			var tempOnOffline = { 'value' : charData.lastWeekCustomerList[index].onOffline};
			
			dataLastWeekOnOffline.push(tempOnOffline);
		});
		
		var dataLastWeekOnOffline = { 'seriesname' : i18n.BO3003, 'data' : dataLastWeekOnOffline, 'renderAs' : 'Line', 'showValues' : '0'};
		
		var revenueChart = new FusionCharts({
	        type: 'stackedcolumn2dline',
	        renderAt: 'chart-customers',
	        width: '100%',
	        height: '350',
	        dataFormat: 'json',
	        dataSource: {            
	            "chart": {
	                "showvalues": "1",
	                "xaxisname": i18n.BO2353,
	                "yaxisname": i18n.BO1065,
	                "paletteColors": "#0075c2,#1aaf5d,#f2c500",
	                "bgColor": "#ffffff",
	                "borderAlpha": "20",               
	                "showCanvasBorder": "0",
	                "usePlotGradientColor": "0",
	                "plotBorderAlpha": "10",
	                "legendBorderAlpha": "0",
	                "legendShadow": "0",
	                "legendBgAlpha": "0",
	                "valueFontColor": "#ffffff",               
	                "showXAxisLine": "1",
	                "xAxisLineColor": "#999999",
	                "divlineColor": "#999999",               
	                "divLineIsDashed": "1",
	                "showAlternateHGridColor": "0",
	                "subcaptionFontBold": "0",
	                "subcaptionFontSize": "14",
	                "showHoverEffect":"1"
	            },
	            "categories": [
	                {
	                    "category": label
	                }
	            ],
	            "dataset": [
	                dataOffline,
	                dataOnline,
	                dataLastWeekOnOffline
	            ]
	        }
	    }).render();    
    },
    
    convertChartToStore : function(charData) {
    	  var _label = [];
    	  var dataItemSales = [];
    	  
    	  $.each(charData.itemList, function(idx, val) {
    		  var itemSales = [];
    		  var temp = [];
    		  var data = {};
    		  $.each(charData.itemList[idx], function (label, val2){
    			  if(label != "" && label != undefined){
      				if(label != "label"){
	        				var _cObj = {};
	        	    		_cObj.value = val2;
	        	    		temp.push(_cObj);
	        	    
	        	    		if (idx == 0) {
		        	    		var _cObj2 = {};
		            			_cObj2.label = label;
		            		  _label.push(_cObj2);
	        	    		}
      					}
    			  }
    		  });
    		  
    		
    		  data = { 'seriesname' : charData.itemList[idx].label, 'data' : temp};
    		  dataItemSales.push(data);
    	  });
    	  var _label = [];
    	  var dataItemSales = [];
    	  
    	  $.each(charData.itemList, function(idx, val) {
    		  var itemSales = [];
    		  var temp = [];
    		  var data = {};
    		  $.each(charData.itemList[idx], function (label, val2){
    			  if(label != "" && label != undefined){
      				if(label != "label"){
	        				var _cObj = {};
	        	    		_cObj.value = val2;
	        	    		temp.push(_cObj);
	        	    
	        	    		if (idx == 0) {
		        	    		var _cObj2 = {};
		            			_cObj2.label = label;
		            		  _label.push(_cObj2);
	        	    		}
      					}
    			  }
    		  });
    		  
    		
    		  data = { 'seriesname' : charData.itemList[idx].label, 'data' : temp};
    		  dataItemSales.push(data);
    	  });
    	// Top5 Item
		var iemChart = new FusionCharts({
			'type': 'msline',
			'renderAt': 'topItem',
			'width': '100%',
			'height': '250',
			'dataFormat': 'json',
			'dataSource': {
				'chart': {
					'caption': i18n.BO1006,
					 "subCaption": "Day Of Week",
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
                     "xAxisName": "Day Of Week",
                     "yAxisName": "Amount",
                     "showValues": "0",
                     "exportEnabled": "1"
				},
				'categories' : [
				  {
				      'category' :	_label
				   }
				],
				'dataset' : dataItemSales
			}
		});
		iemChart.render();
    	
		// Top5 Category
  	   _label = [];
  	  dataItemSales = [];
  	  
  	  $.each(charData.categoryList, function(idx, val) {
  		  var itemSales = [];
  		  var temp = [];
  		  var data = {};
  		  $.each(charData.categoryList[idx], function (label, val2){
  			  if(label != "" && label != undefined){
    				if(label != "label"){
	        				var _cObj = {};
	        	    		_cObj.value = val2;
	        	    		temp.push(_cObj);
	        	    
	        	    		if (idx == 0) {
		        	    		var _cObj2 = {};
		            			_cObj2.label = label;
		            		  _label.push(_cObj2);
	        	    		}
    					}
  			  }
  		  });
  		  
  		
  		  data = { 'seriesname' : charData.categoryList[idx].label, 'data' : temp};
  		  dataItemSales.push(data);
  	  });
		var categoryChart = new FusionCharts({
			'type': 'msline',
			'renderAt': 'topCategory',
			'width': '100%',
			'height': '250',
			'dataFormat': 'json',
			'dataSource': {
				'chart': {
					'caption': i18n.BO1007,
					'subCaption': "Day Of Week",
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
                    "xAxisName": "Day Of Week",
                    "yAxisName": "Amount",
                    "showValues": "0",
                    "exportEnabled": "1"
				},
				'categories' : [
				  {
				      'category' :	_label
				   }
				],
				'dataset' : dataItemSales
			}
		});
		categoryChart.render();
		
		
		// 최근 일주일 쿠폰 발행 수
		var chartData = [];
		  $.each(charData.couponIssueList, function (idx, value) {
		      $.each(charData.couponIssueList[idx], function (label, value) {
		        var data = {"label" : label, "value" : value};
		        chartData.push(data)
		      });
		    });
		    var couponIssueChart = new FusionCharts({
		      'type': 'column2d',
		      'renderAt': 'couponIssue',
		      'width': '100%',
		      'height': '250',
		      'dataFormat': 'json',
		      'dataSource': {
		        'chart': {
		          'caption': i18n.BO2295,
		          'xAxisName': 'DayOfWeek',
		          'yAxisName': 'issue',
		          'theme': 'fint',
		              'exportEnabled': '1'
		        },
		        'data': chartData
		      }
		    });
		   couponIssueChart.render();
		   
		// 최근 일주일 쿠폰 사용 수 
			var chartData = [];
			$.each(charData.couponUseList, function (idx, value) {
				$.each(charData.couponUseList[idx], function (label, value) {
						var data = {"label" : label, "value" : value};
						chartData.push(data)
				});
		    });
		    var couponUseChart = new FusionCharts({
		      'type': 'column2d',
		      'renderAt': 'couponUse',
		      'width': '100%',
		      'height': '250',
		      'dataFormat': 'json',
		      'dataSource': {
		        'chart': {
		          'caption': i18n.BO2295,
		          'xAxisName': 'DayOfWeek',
		          'yAxisName': 'Use',
		          'theme': 'fint',
		              'exportEnabled': '1'
		        },
		        'data': chartData
		      }
		    });
		    couponUseChart.render();
		    
		    var label = [];
			var dataOfflineCustomers = [];
			var dataOnlineCustomers = [];
			var dataOffline = [];
			var dataOnline = [];
			
			if (charData.thisMonthCustomerList != undefined) {
				$.each(charData.thisMonthCustomerList, function (index, value) {
					var labelObj = { 'label' : charData.thisMonthCustomerList[index].label };
					var tempOffline = { 'value' : charData.thisMonthCustomerList[index].offline};
					var tempOnline = { 'value' : charData.thisMonthCustomerList[index].online};
					
					label.push(labelObj);
					dataOffline.push(tempOffline);
					dataOnline.push(tempOnline);
				});
				
				var dataOffline = { 'seriesname' : i18n.BO2316, 'data' : dataOffline};
				var dataOnline = { 'seriesname' : i18n.BO2447, 'data' : dataOnline};
					
				dataOfflineCustomers.push(dataOffline);
				dataOnlineCustomers.push(dataOnline);
				
				var dataLastMonthCustomers = {};
				var dataLastMonthOnOffline = [];
				
				$.each(charData.lastMonthCustomerList, function (index, value) {
					var tempOnOffline = { 'value' : charData.lastMonthCustomerList[index].onOffline};
					
					dataLastMonthOnOffline.push(tempOnOffline);
				});
				
				dataLastMonthCustomers = { 'seriesname' : i18n.BO3006, 'data' : dataLastMonthOnOffline, 'renderAs' : 'Line', 'showValues' : '0'};
				
				var revenueChart = new FusionCharts({
			        type: 'stackedcolumn2dline',
			        renderAt: 'chart-customers',
			        width: '100%',
			        height: '350',
			        dataFormat: 'json',
			        dataSource: {            
			            "chart": {
			                "showvalues": "1",
			                "xaxisname": i18n.BO2354,
			                "yaxisname": i18n.BO1065,
			                "paletteColors": "#0075c2,#1aaf5d,#f2c500",
			                "bgColor": "#ffffff",
			                "borderAlpha": "20",               
			                "showCanvasBorder": "0",
			                "usePlotGradientColor": "0",
			                "plotBorderAlpha": "10",
			                "legendBorderAlpha": "0",
			                "legendShadow": "0",
			                "legendBgAlpha": "0",
			                "valueFontColor": "#ffffff",               
			                "showXAxisLine": "1",
			                "xAxisLineColor": "#999999",
			                "divlineColor": "#999999",               
			                "divLineIsDashed": "1",
			                "showAlternateHGridColor": "0",
			                "subcaptionFontBold": "0",
			                "subcaptionFontSize": "14",
			                "showHoverEffect":"1"
			            },
			            "categories": [
			                {
			                    "category": label
			                }
			            ],
			            "dataset": [
			                dataOffline,
			                dataOnline,
			                dataLastMonthCustomers
			            ]
			        }
			    }).render();    
			}
    }
  });

  return dashboard;
});