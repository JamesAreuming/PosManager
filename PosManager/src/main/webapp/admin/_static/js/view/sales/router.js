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

//	'view/sales/franchiseSumManage',
	'view/sales/brandSumManage',
	'view/sales/salesSumManage',
	'view/sales/salesGoalManage',
	'view/sales/salesGoalPerfManage',
	'view/sales/itemSalesManage',
	'view/sales/categorySalesManage',
	'view/sales/serviceSalesManage',
	'view/sales/salesDetailManage',
	'view/sales/cardApprovalManage',

	'collections/FranchiseCollection'
	,'i18n!templates/globals/lang/nls/language'
], function(
		Bracket,
		BaseView,

//		FranchiseSumManageView,
		BrandSumManageView,
		SalesSumManageView,
		SalesGoalManageView,
		SalesGoalPerfManageView,
		ItemSalesManageView,
		CategorySalesManageView,
		ServiceSalesManageView,
		SalesDetailManageView,
		CardApprovalManageView,

		FranchiseCollection,
		i18n
		){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
//	        'franchiseSumManage': 'franchiseSumManage',
	        'brandSumManage' : 'brandSumManage',
			'salesSumManage' : 'salesSumManage',
			'salesGoalManage' : 'salesGoalManage',
			'salesGoalPerfManage' : 'salesGoalPerfManage',
			'itemSalesManage' : 'itemSalesManage',
			'categorySalesManage' : 'categorySalesManage',
			'serviceSalesManage' : 'serviceSalesManage',
			'salesDetailManage' : 'salesDetailManage',
			'cardApprovalManage' : 'cardApprovalManage',

	        '*actions': 'brandSumManage'
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

	var removeEvent = function (contentView) {
        Backbone.View.prototype.on('remove-compnents',function(){
            Backbone.View.prototype.remove;
            contentView.undelegateEvents();
        })
	};

	var init = function() {

	    var router = new Router;

		var menuNavi = {
	    	"current": "020" //"Sales"
		};
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Sales - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},
				{"icon" : "", "title": i18n.BO1003, "link": "sales", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};

//		router.on('route:franchiseSumManage', function(){
//			beforerender('franchiseSum');
//
//			pageNavi.nowPage = { "nowPage" : "Franchise Summary", "nowLink" : "franchiseSumManage" };
//			BaseView.pageHeader(pageNavi);
//
//			var franchise = new FranchiseCollection();
//
//			franchise.fetch();
//
//			var contentView = new FranchiseSumManageView({ model : franchise });
//
//	        Bracket.hideProgress();
//		});

		router.on('route:brandSumManage', function(){
			beforerender('brandSum');

			pageNavi.nowPage = { "nowPage" : i18n.BO1004, "nowLink" : "brandSumManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();

			var contentView = new BrandSumManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:salesSumManage', function(){
			beforerender('salesSum');

			pageNavi.nowPage = { "nowPage" : i18n.BO1005, "nowLink" : "salesSumManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new SalesSumManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:salesGoalManage', function(){
			beforerender('salesGoal');

			pageNavi.nowPage = { "nowPage" : i18n.BO1062, "nowLink" : "salesGoalManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new SalesGoalManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:salesGoalPerfManage', function(){
			beforerender('salesGoalPerf');

			pageNavi.nowPage = { "nowPage" : i18n.BO1063, "nowLink" : "salesGoalPerfManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new SalesGoalPerfManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:itemSalesManage', function(){
			beforerender('itemSales');

			pageNavi.nowPage = { "nowPage" : i18n.BO1006, "nowLink" : "itemSalesManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new ItemSalesManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:categorySalesManage', function(){
			beforerender('categorySales');

			pageNavi.nowPage = { "nowPage" : i18n.BO1007, "nowLink" : "categorySalesManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new CategorySalesManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:serviceSalesManage', function(){
			beforerender('serviceSales');

			pageNavi.nowPage = { "nowPage" : i18n.BO1008, "nowLink" : "serviceSalesManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new ServiceSalesManageView({ model : franchise });
			removeEvent(contentView);

	        Bracket.hideProgress();
		});

		router.on('route:salesDetailManage', function(){
			beforerender('salesDetail');

			pageNavi.nowPage = { "nowPage" : i18n.BO1012, "nowLink" : "salesDetailManage" };
			BaseView.pageHeader(pageNavi);

			var salesDetail = new FranchiseCollection();
			salesDetail.fetch();

			var contentView = new SalesDetailManageView({model : salesDetail});

			removeEvent(contentView);
			Bracket.hideProgress();
		});

		router.on('route:cardApprovalManage', function(){
			beforerender('cardApproval');

			pageNavi.nowPage = { "nowPage" : i18n.BO1013, "nowLink" : "cardApprovalManage" };
			BaseView.pageHeader(pageNavi);

			var cardApproval = new FranchiseCollection();
			cardApproval.fetch();

			var contentView = new CardApprovalManageView({model : cardApproval});

			removeEvent(contentView);
			Bracket.hideProgress();
		});

	    Backbone.history.start();

	    return router;
	};

	return {
		init: init
	}
});

