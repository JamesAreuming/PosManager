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

	'view/reward/rewardSumManage',
	'view/reward/stampSumManage',
	'view/reward/stampDetailManage',	
	'view/reward/couponSumManage',
	'view/reward/couponDetailManage',
	'view/reward/stampAccManage',
	'collections/FranchiseCollection',
	'i18n!templates/globals/lang/nls/language',
	'form-select2'
], function(
		Bracket,
		BaseView,
		
		RewardSumManageView,
		StampSumManageView,
		StampDetailManageView,
		CouponSumManageView,
		CouponDetailManageView,
		StampAccManageView,
		
		FranchiseCollection,
		i18n
		){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	    	'rewardSumManage': 'rewardSumManage',
	    	'stampSumManage': 'stampSumManage',
	    	'stampDetailManage': 'stampDetailManage',
	    	'couponSumManage': 'couponSumManage',
	    	'couponDetailManage': 'couponDetailManage',
	    	'stampAccManage': 'stampAccManage',

	    	'*actions': 'rewardSumManage'
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
            contentView.undelegateEvents();
        })
	};

	var init = function() {

	    var router = new Router;

	    var menuNavi = {
	      "current": "040" //"Reward"
	    };
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Reword - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},
				{"icon" : "", "title": i18n.BO1024, "link": "reward", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", "link": "reward#{{nowLink}}", hasNext: false}
			]
		};

		router.on('route:rewardSumManage', function(){
			beforerender('rewardSum');

			pageNavi.nowPage = { "nowPage" : i18n.BO1025, "nowLink" : "rewardSumManage" };
			BaseView.pageHeader(pageNavi);

			var rewardSum = new FranchiseCollection();
			rewardSum.fetch();

			var contentView = new RewardSumManageView({model : rewardSum});

			removeEvent(contentView);
	        Bracket.hideProgress();
		});

		router.on('route:stampSumManage', function(){
			beforerender('stampSum');

			pageNavi.nowPage = { "nowPage" : i18n.BO1026, "nowLink" : "stampSumManage" };
			BaseView.pageHeader(pageNavi);

			var stampSum = new FranchiseCollection();
			stampSum.fetch();

			var contentView = new StampSumManageView({model : stampSum});

			removeEvent(contentView);
	        Bracket.hideProgress();
		});

		router.on('route:stampDetailManage', function(){
			beforerender('stampDetail');

			pageNavi.nowPage = { "nowPage" : i18n.BO1027, "nowLink" : "stampDetailManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();

			var contentView = new StampDetailManageView({model : franchise});

	        Bracket.hideProgress();
		});

		router.on('route:couponSumManage', function(){
			beforerender('couponSum');

			pageNavi.nowPage = { "nowPage" : i18n.BO1028, "nowLink" : "couponSumManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();


			var contentView = new CouponSumManageView({model : franchise});

			removeEvent(contentView);
	        Bracket.hideProgress();
		});

		router.on('route:couponDetailManage', function(){
			beforerender('couponDetail');

			pageNavi.nowPage = { "nowPage" : i18n.BO1029, "nowLink" : "couponDetailManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new CouponDetailManageView({model : franchise});

			removeEvent(contentView);
	        Bracket.hideProgress();
		});

		router.on('route:stampAccManage', function(){
			beforerender('stampAcc');

			pageNavi.nowPage = { "nowPage" : i18n.BO1030, "nowLink" : "stampAccManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();
			franchise.fetch();
			
			var contentView = new StampAccManageView({model : franchise});

			removeEvent(contentView);
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

