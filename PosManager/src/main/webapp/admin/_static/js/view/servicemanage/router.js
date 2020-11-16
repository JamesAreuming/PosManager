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
	
	'view/servicemanage/operManage',
	'collections/FranchiseCollection',
	
	'view/servicemanage/couponManage',
	
	'view/servicemanage/closeManage',
	
	'view/servicemanage/eventManage',
	
	'view/servicemanage/noticeManage',
	
	'view/servicemanage/tacManage'
], function(
		Bracket,
		BaseView, 
		OperManageView, FranchiseCollection,
		CouponManageView,
		CloseManageView,
		EventManageView,
		NoticeManageView,
		TacManageView){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	      'operManage': 'operManage',
	      
	      'couponManage': 'couponManage',
	      
	      'closeManage': 'closeManage',

	      'eventManage': 'eventManage',
	      
	      'noticeManage': 'noticeManage',
	      
	      'tacManage': 'tacManage',
	      
	      
	      '*actions': 'operManage'
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
	    	"current": "000"  //"ServiceManage"
	    };
	    BaseView.init(menuNavi);
	    
		var pageNavi = {
			"windowTitle": "ORDER9 MEMBERSHIP - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": "Home", "link": "/admin/", hasNext: false},
				{"icon" : "", "title": "서비스운영관리", "link": "serviceManage", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};
		
		router.on('route:operManage', function(){
			beforerender('oper');
			
			pageNavi.nowPage = { "nowPage" : "운영관리", "nowLink" : "operManage" };
			BaseView.pageHeader(pageNavi);
			
			var franchise = new FranchiseCollection();
			
			franchise.fetch();
			
			var contentView = new OperManageView({ model : franchise });

			Bracket.hideProgress();
		});
		
		router.on('route:couponManage', function(){
			beforerender('coupon');
			
			pageNavi.nowPage = { "nowPage" : "쿠폰관리", "nowLink" : "couponManage" };
			BaseView.pageHeader(pageNavi);
			
			var franchise = new FranchiseCollection();
			
			franchise.fetch();
			
			var contentView = new CouponManageView({ model : franchise });

	        Bracket.hideProgress();
		});
		
		router.on('route:closeManage', function(){
			beforerender('storeclose');
			
			pageNavi.nowPage = { "nowPage" : "폐점관리", "nowLink" : "closeManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new CloseManageView();
			contentView.render();
			
			form_select.init();
			_datatables.init();
	        Bracket.hideProgress();
		});

		router.on('route:eventManage', function(){
			beforerender('event');

			pageNavi.nowPage = { "nowPage" : "이벤트관리", "nowLink" : "eventManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new EventManageView();
			contentView.render();
			
			form_select.init();
			_datatables.init();
	        Bracket.hideProgress();
		});

		router.on('route:noticeManage', function(){
			beforerender('notice');

			pageNavi.nowPage = { "nowPage" : "공지관리", "nowLink" : "noticeManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new NoticeManageView();
			contentView.render();
			
			form_select.init();
			_datatables.init();
	        Bracket.hideProgress();
		});

		router.on('route:tacManage', function(){
			beforerender('tac');

			pageNavi.nowPage = { "nowPage" : "약관관리", "nowLink" : "tacManage" };
			BaseView.pageHeader(pageNavi);
			
			var contentView = new TacManageView();
			contentView.render();
			
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

