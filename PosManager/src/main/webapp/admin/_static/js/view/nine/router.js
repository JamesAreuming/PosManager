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
	'i18n!templates/globals/lang/nls/language',
	'view/base',

	'view/nine/franchiseManage', 'collections/FranchiseCollection',
	'view/nine/brandManage',
	'view/nine/storeManage',
	'view/nine/stampSetManage',
	'view/nine/beaconManage',
	'view/nine/posLicenseManage',
	
	'form-select2'
], function(
		bracket,
		i18n,
		BaseView,

		FranchiseManageView, FranchiseCollection,
		BrandManageView,
		StoreManageVeiw,
		StampSetManageView,
		BeaconManageView,
		PosLicenseManageView
		){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
    	  'franchiseManage': 'franchiseManage',
	      'brandManage': 'brandManage',
	      'storeManage': 'storeManage',
	      'stampSetManage': 'stampSetManage',
	      'beaconManage': 'beaconManage',
	      'posLicenseManage': 'posLicenseManage',

	      '*actions': 'franchiseManage'
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
        Backbone.View.prototype.on('remove-event',function(){
            Backbone.View.prototype.remove;
            //Backbone.View.prototype.off();
            contentView.undelegateEvents();
        })
	};

	var init = function() {
	    var router = new Router;

	    var menuNavi = {
	    	"current": "050"  //"Store"
	    };
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Store - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},
				{"icon" : "", "title": i18n.BO1033, "link": "store", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};

		router.on('route:franchiseManage', function(){
			beforerender('franchise');

			pageNavi.nowPage = { "nowPage" : i18n.BO1031, "nowLink" : "franchiseManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new FranchiseManageView({model: franchise});

	        bracket.hideProgress();
	        removeEvent(contentView);
			//form_select.init();
		});

		router.on('route:brandManage', function(){
			
			beforerender('brand');
			pageNavi.nowPage = { "nowPage" : i18n.BO1032, "nowLink" : "brandManage" };
			BaseView.pageHeader(pageNavi);

			var brand = new FranchiseCollection();
			brand.fetch();

			var contentView = new BrandManageView({model : brand});

	        bracket.hideProgress();
	        removeEvent(contentView);

			form_select.init();
//			_datatables.init();
			
		});

		router.on('route:storeManage', function(){
			beforerender('store');

			pageNavi.nowPage = { "nowPage" : i18n.BO1033, "nowLink" : "storeManage" };
			BaseView.pageHeader(pageNavi);

			var store = new FranchiseCollection();
			store.fetch();

			var contentView = new StoreManageVeiw({model: store});

	        bracket.hideProgress();
	        removeEvent(contentView);
		});

		router.on('route:stampSetManage', function(){
			beforerender('stampSet');

			pageNavi.nowPage = { "nowPage" : i18n.BO1034, "nowLink" : "stampSetManage" };
			BaseView.pageHeader(pageNavi);

			var stampSet = new FranchiseCollection();
			stampSet.fetch();

			var contentView = new StampSetManageView({model : stampSet});

			bracket.hideProgress();
	        removeEvent(contentView);

			//form_select.init();
		});

		router.on('route:beaconManage', function(){
			beforerender('beacon');

			pageNavi.nowPage = { "nowPage" : i18n.BO1035, "nowLink" : "beaconManage" };
			BaseView.pageHeader(pageNavi);

			var beacon = new FranchiseCollection();
			beacon.fetch();

			var contentView = new BeaconManageView({model : beacon});

			bracket.hideProgress();
	        removeEvent(contentView);
		});

		router.on('route:posLicenseManage', function(){
			beforerender('posLicense');

			pageNavi.nowPage = { "nowPage" : i18n.BO1036, "nowLink" : "posLicenseManage" };
			BaseView.pageHeader(pageNavi);


			var posLicense = new FranchiseCollection();
			posLicense.fetch();

			var contentView = new PosLicenseManageView({model : posLicense});

			bracket.hideProgress();
	        removeEvent(contentView);
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

