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

	'collections/FranchiseCollection',

	'view/management/reviewManage',
	'view/management/eventManage',
	'view/management/noticeManage',
	'view/management/tacManage',
	'view/management/appManage',
	
	'view/management/baseCodeManage',
	'view/management/menuManage',
	'view/management/userGroupManage',
	'view/management/userGroupAuthManage',
	'collections/UserGroupCollection',

	'view/management/userManage',
	'view/management/customerManage',
	'i18n!templates/globals/lang/nls/language'
], function(
		Bracket,
		BaseView,
		
		FranchiseCollection,
		
		ReviewManageView,
		EventManageView,
		NoticeManageView,
		TacManageView,
		AppManageView,
		
		BaseCodeManageView,
		MenuManageView,
		UserGroupManageView,
		UserGroupAuthManageView,
		UserGroupCollection,
		
		UserManageView,
		CustomerManageView,
		i18n
		){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	      'reviewManage' : 'reviewManage',
	      'eventManage': 'eventManage',
	      'noticeManage': 'noticeManage',
	      'tacManage': 'tacManage',
	      'appManage': 'appManage',
	      
	      'baseCodeManage': 'baseCodeManage',
	      'menuManage': 'menuManage',
	      'userGroupManage': 'userGroupManage',
	      'userGroupAuthManage' : 'userGroupAuthManage',
	      'userManage' : 'userManage',
	      'customerManage' : 'customerManage',

	      '*actions': 'eventManage'
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
	      "current": "070" //"Management"
	    };
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Management - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},	// page-navi depth 1
				{"icon" : "", "title": i18n.BO1037, "link": "management", hasNext: false},			// page-navi depth 2
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}													// page-navi depth 3
			]
		};

		router.on('route:reviewManage', function(){
			beforerender('reviewManage');

			pageNavi.nowPage = { "nowPage" : i18n.BO1038, "nowLink" : "reviewManage" };
			BaseView.pageHeader(pageNavi);

			var review = new FranchiseCollection();
			review.fetch();

			var contentView = new ReviewManageView({model : review});

			Bracket.hideProgress();
		});
		
		router.on('route:eventManage', function(){
			beforerender('event');

			pageNavi.nowPage = { "nowPage" : i18n.BO1039, "nowLink" : "eventManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new EventManageView();
			contentView.render();

			form_select.init();
	        Bracket.hideProgress();
		});

		router.on('route:noticeManage', function(){
			beforerender('notice');

			pageNavi.nowPage = { "nowPage" : i18n.BO1040, "nowLink" : "noticeManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new NoticeManageView();
			contentView.render();

	        Bracket.hideProgress();
		});

		router.on('route:tacManage', function(){
			beforerender('tac');

			pageNavi.nowPage = { "nowPage" : i18n.BO1041, "nowLink" : "tacManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new TacManageView();
			contentView.render();

	        Bracket.hideProgress();
		});

		router.on('route:appManage', function(){
			beforerender('app');

			pageNavi.nowPage = { "nowPage" : i18n.BO1042, "nowLink" : "appManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new AppManageView();
			contentView.render();
			Bracket.hideProgress();

			form_select.init();
		});

		router.on('route:baseCodeManage', function(){
			beforerender('basecode');

			pageNavi.nowPage = { "nowPage" : i18n.BO1043, "nowLink" : "baseCodeManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new BaseCodeManageView();
			contentView.render();

			Bracket.hideProgress();
		});

		router.on('route:menuManage', function(){
			beforerender('menu1');

			pageNavi.nowPage = { "nowPage" : i18n.BO1044, "nowLink" : "menuManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new MenuManageView();
			contentView.render();

			Bracket.hideProgress();
		});

		router.on('route:userGroupManage', function(){
			beforerender('userGroup');

			pageNavi.nowPage = { "nowPage" : i18n.BO1045, "nowLink" : "userGroupManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new UserGroupManageView();
			contentView.render();

			Bracket.hideProgress();
		});

		router.on('route:userGroupAuthManage', function(){
			beforerender('userGroup');

			pageNavi.nowPage = { "nowPage" : i18n.BO1046, "nowLink" : "userGroupAuthManage" };
			BaseView.pageHeader(pageNavi);

			var userGroup = new UserGroupCollection();

			userGroup.fetch();

			var contentView = new UserGroupAuthManageView({model : userGroup});

			Bracket.hideProgress();
		});

		router.on('route:userManage', function(){
			beforerender('userManage');

			pageNavi.nowPage = { "nowPage" : i18n.BO1047, "nowLink" : "userManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();

			var contentView = new UserManageView({model : franchise});

			Bracket.hideProgress();
		});
		
		router.on('route:customerManage', function () {
			beforerender('customerManage');		// className 변경 후 content panel children 제거
			
			pageNavi.nowPage = { "nowPage" : i18n.BO1065};		// 현재 페이지 타이틀 
			BaseView.pageHeader(pageNavi);		// 페이지 헤더에 페이지 네비게이션 표시 (depth, title, link)
			
			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new CustomerManageView({model : franchise});
			
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

