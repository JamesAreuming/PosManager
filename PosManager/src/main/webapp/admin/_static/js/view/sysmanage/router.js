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

	'view/sysmanage/brandManage',
	'collections/FranchiseCollection',

	'view/sysmanage/storeManage',

	'view/sysmanage/appManage',

	'view/sysmanage/corpManage',

	'view/sysmanage/baseCodeManage',

	'view/sysmanage/menuManage',

	'view/sysmanage/userGroupManage',

	'view/sysmanage/userGroupAuthManage',
	'collections/UserGroupCollection',

	'view/sysmanage/userManage'
], function(
		bracket,
		BaseView,
		BrandManageView, FranchiseCollection,
		StoreManageVeiw,
		AppManageView,
		CorpManageView,
		BaseCodeManageView,
		MenuManageView,
		UserGroupManageView,
		UserGroupAuthManageView, UserGroupCollection,
		UserManageView){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	      'brandManage': 'brandManage',
	      'storeManage': 'storeManage',
	      'appManage': 'appManage',
	      'corpManage': 'corpManage',
	      'baseCodeManage': 'baseCodeManage',
	      'menuManage': 'menuManage',
	      'userGroupManage': 'userGroupManage',
	      'userGroupAuthManage' : 'userGroupAuthManage',
	      'userManage' : 'userManage',

	      '*actions': 'brandManage'
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
	    	"current": "000"  //"SysManage"
	    };
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "ORDER9 MEMBERSHIP - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": "Home", "link": "/admin/", hasNext: false},
				{"icon" : "", "title": "시스템관리", "link": "sysManage", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};

		router.on('route:brandManage', function(){
			beforerender('brand');

			pageNavi.nowPage = { "nowPage" : "브랜드관리", "nowLink" : "brandManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();

			var contentView = new BrandManageView({model : franchise});


	        bracket.hideProgress();

			form_select.init();
//			_datatables.init();
		});

		router.on('route:storeManage', function(){
			beforerender('store');

			pageNavi.nowPage = { "nowPage" : "매장관리", "nowLink" : "storeManage" };
			BaseView.pageHeader(pageNavi);


			var franchise = new FranchiseCollection();
			franchise.fetch();


			var contentView = new StoreManageVeiw({model: franchise});

	        bracket.hideProgress();

		});

		router.on('route:appManage', function(){
			beforerender('app');

			pageNavi.nowPage = { "nowPage" : "App버전관리", "nowLink" : "appManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new AppManageView();
			contentView.render();
	        bracket.hideProgress();

			form_select.init();
		});

		router.on('route:corpManage', function(){
			beforerender('corp');

			pageNavi.nowPage = { "nowPage" : "법인관리", "nowLink" : "corpManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new CorpManageView();
			contentView.render();
	        bracket.hideProgress();

			form_select.init();

		});

		router.on('route:baseCodeManage', function(){
			beforerender('basecode');

			pageNavi.nowPage = { "nowPage" : "기초코드관리", "nowLink" : "baseCodeManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new BaseCodeManageView();
			contentView.render();

	        bracket.hideProgress();
		});

		router.on('route:menuManage', function(){
			beforerender('menu1');

			pageNavi.nowPage = { "nowPage" : "메뉴관리", "nowLink" : "menuManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new MenuManageView();
			contentView.render();

	        bracket.hideProgress();
		});

		router.on('route:userGroupManage', function(){
			beforerender('userGroup');

			pageNavi.nowPage = { "nowPage" : "사용자그룹관리", "nowLink" : "userGroupManage" };
			BaseView.pageHeader(pageNavi);

			var contentView = new UserGroupManageView();
			contentView.render();

	        bracket.hideProgress();
		});

		router.on('route:userGroupAuthManage', function(){
			beforerender('userGroup');

			pageNavi.nowPage = { "nowPage" : "사용자그룹권한관리", "nowLink" : "userGroupAuthManage" };
			BaseView.pageHeader(pageNavi);

			var userGroup = new UserGroupCollection();

			userGroup.fetch();

			var contentView = new UserGroupAuthManageView({model : userGroup});

			contentView.render();

	        bracket.hideProgress();
		});

		router.on('route:userManage', function(){
			beforerender('userManage');

			pageNavi.nowPage = { "nowPage" : "사용자관리", "nowLink" : "userManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();

			var contentView = new UserManageView({model : franchise});

	        bracket.hideProgress();
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

