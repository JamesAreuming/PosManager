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

	'view/inventory/stockStatusManage',
	'view/inventory/actualStockManage',
	'view/inventory/stockImportReg',
	'view/inventory/stockImportManage',
	'view/inventory/stockExportManage',
	'view/inventory/stockAdjustManage',
	'view/inventory/supplierManage',

	'collections/FranchiseCollection',
	'i18n!templates/globals/lang/nls/language',
	'form-select2'
], function(
		Bracket,
		BaseView,

		StockStatusManageView,
		ActualStockManageView,
		StockImportRegView,
		StockImportManageView,
		StockExportManageView,
		StockAdjustManageView,
		SupplierManageView,
		FranchiseCollection,
		i18n){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
    	  'stockStatusManage': 'stockStatusManage',
	      'actualStockManage': 'actualStockManage',
	      'stockImportReg': 'stockImportReg',
	      'stockImportManage': 'stockImportManage',
	      'stockExportManage': 'stockExportManage',
	      'stockAdjustManage': 'stockAdjustManage',
	      'supplierManage': 'supplierManage',

	      '*actions': 'stockStatusManage'
	    }
	});
	
	var removeEvent = function (view){
	    Backbone.View.prototype.on('remove-event',function(){
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
	    	"current": "060"  //"Store"
	    };
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Inventory - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},
				{"icon" : "", "title": i18n.BO1054, "link": "inventory", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};

		router.on('route:stockStatusManage', function(){
			beforerender('stockStatus');

			pageNavi.nowPage = { "nowPage" : i18n.BO1055, "nowLink" : "stockStatusManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StockStatusManageView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		/* ************************************************************ */
		
		router.on('route:actualStockManage', function(){
			beforerender('actualStock');

			pageNavi.nowPage = { "nowPage" : i18n.BO1056, "nowLink" : "actualStockManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new ActualStockManageView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:stockImportReg', function(){
			beforerender('stockImportReg');

			pageNavi.nowPage = { "nowPage" : i18n.BO1057, "nowLink" : "stockImportReg" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StockImportRegView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:stockImportManage', function(){
			beforerender('stockImport');

			pageNavi.nowPage = { "nowPage" : i18n.BO1058, "nowLink" : "stockImportManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StockImportManageView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:stockExportManage', function(){
			beforerender('stockExport');

			pageNavi.nowPage = { "nowPage" : i18n.BO1059, "nowLink" : "stockExportManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StockExportManageView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:stockAdjustManage', function(){
			beforerender('stockAdjust');

			pageNavi.nowPage = { "nowPage" : i18n.BO1060, "nowLink" : "stockAdjustManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StockAdjustManageView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		router.on('route:supplierManage', function(){
			beforerender('supplier');

			pageNavi.nowPage = { "nowPage" : i18n.BO1061, "nowLink" : "supplierManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new SupplierManageView({model: franchise});
			removeEvent(contentView);

	        Bracket.hideProgress();
		});
		
		/* ************************************************************ */

	    Backbone.history.start();

	    return router;
	};

	return {
		init: init
	}
});

