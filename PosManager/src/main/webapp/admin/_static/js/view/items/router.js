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

	'view/items/categoryManage',
	'view/items/itemsManage',
	'view/items/setItemManage',
	'view/items/couseItemManage',
	'view/items/posPluCateManage',
	'view/items/posPluSetManage',
	'view/items/tabDpCateManage',
	'view/items/tabDpSetManage',
	'view/items/mobileDpCateManage',
	'view/items/mobileDpSetManage',
	'view/items/posTableSetManage',
	'view/items/posMessageSetManage',

	'collections/FranchiseCollection',
	'collections/items/CategoryCollection',
	'collections/items/PosTableSetCollection',
	'i18n!templates/globals/lang/nls/language',
], function(
		Bracket,
		BaseView,

		CategoryManageView,
		ItemsManageView,
		SetItemManageView,
		CouseItemManageView,
		PosPluCateManageView,
		PosPluSetManageView,
		TabDpCateManageView,
		TabDpSetManageView,
		MobileDpCateManageView,
		MobileDpSetManageView,
		PosTableSetManageView,
		PosMessageSetManageView,

		FranchiseCollection,
		CategoryCollection,
		PosTableSetCollection,
		i18n
		){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	        'categoryManage'	: 'categoryManage',
	        'itemsManage'		: 'itemsManage',
	        'setItemManage'		: 'setItemManage',
	        'couseItemManage'	: 'couseItemManage',
	        'posPluCateManage'	: 'posPluCateManage',
	        'posPluSetManage'	: 'posPluSetManage',
	        'tabDpCateManage'	: 'tabDpCateManage',
	        'tabDpSetManage'	: 'tabDpSetManage',
	        'mobileDpCateManage': 'mobileDpCateManage',
	        'mobileDpSetManage'	: 'mobileDpSetManage',
	        'posTableSetManage' : 'posTableSetManage',
	        'posMessageSetManage' : 'posMessageSetManage',			

	        '*actions': 'categoryManage'
	    }
	});

	var beforeViewRemove = function (view){
	    Backbone.View.prototype.on('remove-compnents',function(){
	        Backbone.View.prototype.remove;
	        view.undelegateEvents();
	    })
	};

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
	    "current": "030" //"Items"
	  };
	  BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Store Commerce - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},
				{"icon" : "", "title": i18n.BO1015, "link": "category", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};

		router.on('route:categoryManage', function(){
			beforerender('category');

			pageNavi.nowPage = { "nowPage" : i18n.BO1014, "nowLink" : "categoryManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();

			var contentView = new CategoryManageView({model: franchise});
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:itemsManage', function(){
			beforerender('items');

			pageNavi.nowPage = { "nowPage" : i18n.BO1015, "nowLink" : "itemsManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new ItemsManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		//현재 메뉴 비활성
		router.on('route:setItemManage', function(){
			beforerender('setItem');

			pageNavi.nowPage = { "nowPage" : "Set Item", "nowLink" : "setItemManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new SetItemManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		//현재 메뉴 비활성
		router.on('route:couseItemManage', function(){
			beforerender('couseItem');

			pageNavi.nowPage = { "nowPage" : "Couse Item", "nowLink" : "couseItemManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new CouseItemManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:posPluCateManage', function(){
			beforerender('posPluCate');

			pageNavi.nowPage = { "nowPage" : i18n.BO1016, "nowLink" : "posPluCateManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new PosPluCateManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:posPluSetManage', function(){
			beforerender('posPluSet');

			pageNavi.nowPage = { "nowPage" : i18n.BO1017, "nowLink" : "posPluSetManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new PosPluSetManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:tabDpCateManage', function(){
			beforerender('tabDpCate');

			pageNavi.nowPage = { "nowPage" : i18n.BO1018, "nowLink" : "tabDpCateManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new TabDpCateManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:tabDpSetManage', function(){
			beforerender('tabDpSet');

			pageNavi.nowPage = { "nowPage" : i18n.BO1019, "nowLink" : "tabDpSetManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new TabDpSetManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:mobileDpCateManage', function(){
			beforerender('mobileDpCate');

			pageNavi.nowPage = { "nowPage" : i18n.BO1020, "nowLink" : "mobileDpCateManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new MobileDpCateManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:mobileDpSetManage', function(){
			beforerender('mobileDpSet');

			pageNavi.nowPage = { "nowPage" : i18n.BO1021, "nowLink" : "mobileDpSetManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new MobileDpSetManageView({ model : franchise });
			beforeViewRemove(contentView);

	        Bracket.hideProgress();
		});		

		router.on('route:posTableSetManage', function(){
			beforerender('posTableSet');

			pageNavi.nowPage = { "nowPage" : i18n.BO1022, "nowLink" : "posTableSetManage" };
			BaseView.pageHeader(pageNavi);

			//var franchise = new PosTableSetCollection();
			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new PosTableSetManageView({ model : franchise });

			beforeViewRemove(contentView);
	        Bracket.hideProgress();
		});

		router.on('route:posMessageSetManage', function(){
			beforerender('posMessageSet');

			pageNavi.nowPage = { "nowPage" : i18n.BO1023, "nowLink" : "posMessageSetManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new PosMessageSetManageView({ model : franchise });

			beforeViewRemove(contentView);
	        Bracket.hideProgress();
		});

	    Backbone.history.start();

	    return router;
	};

	return {
		init: init
	}
});

