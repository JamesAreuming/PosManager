/*
 * Filename	: router.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
	'bracket',
	'view/base',
	
	'view/servicestatusmanage/stampManage',
	'collections/FranchiseCollection',
	'collections/BaseCollection',
	
	'view/servicestatusmanage/couponManage',
	
	'view/servicestatusmanage/closeManage',
	
	'view/servicestatusmanage/giftManage'
	
], function(
		Bracket,
		BaseView, 
		StampManageView, FranchiseCollection,
		BaseCollection,
		CouponManageView,
		CloseManageView,
		GiftManageView){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	      'stampManage': 'stampManage',
	      
	      'couponManage': 'couponManage',

	      'closeManage': 'closeManage',
	      
	      'giftManage': 'giftManage',
	      
	      '*actions': 'stampManage'
	    }
	});

	var beforeClass = '';
	
	var beforerender = function (className) {
		if(!$('#main-wrapper').hasClass(className)){
			beforeClass = className;
			$('#main-wrapper').removeClass(beforeClass);
		    $('#main-wrapper').addClass(className);
		}
		
		$('#main-wrapper').children().remove();
	};


	var init = function() {

	    var router = new Router;

	    var menuNavi = {
	    	"current": "000"  //"ServiceStatus"
	    };
	    BaseView.init(menuNavi);
	    
		var pageNavi = {
			"windowTitle": "ORDER9 MEMBERSHIP - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": "Home", "link": "/admin/", hasNext: false},
				{"icon" : "", "title": "서비스현황", "link": "serviceManage", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", "link": "serviceManage#{{nowLink}}", hasNext: false}
			]
		};
		
		router.on('route:stampManage', function(){
			beforerender('stamp');
			
			pageNavi.nowPage = { "nowPage" : "스템프조회", "nowLink" : "stampManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StampManageView({model : franchise});

	        Bracket.hideProgress();
		});

		router.on('route:couponManage', function(){
			beforerender('coupon');
			
			pageNavi.nowPage = { "nowPage" : "쿠폰조회", "nowLink" : "couponManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new CouponManageView();
			contentView.render();
			
			form_select.init();
			_datatables.init();
	        Bracket.hideProgress();
		});

		router.on('route:closeManage', function(){
			beforerender('storeclose');

			pageNavi.nowPage = { "nowPage" : "폐점전환정산", "nowLink" : "closeManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new CloseManageView();
			contentView.render();
			
			form_select.init();
			_datatables.init();
	        Bracket.hideProgress();
		});

		router.on('route:giftManage', function(){
			beforerender('gift');

			pageNavi.nowPage = { "nowPage" : "쿠폰선물조회", "nowLink" : "giftManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new GiftManageView();
			contentView.render();
			
			form_select.init();
			_datatables.init();
	        Bracket.hideProgress();
		});

	    //router.on('route:default', function() {
	      //bootbox.alert('페이지가 존재하지 않습니다.');
		//  beforerender('brand');
	    //  window.location.hash = "brandManage";
	    //});
		// get model
		//sysmanage.fetch();

	    Backbone.history.start();

	    return router;
	};

	return {
		init: init
	}
});

