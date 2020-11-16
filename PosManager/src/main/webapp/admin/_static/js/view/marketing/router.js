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

	'view/marketing/memberManage',
	'view/marketing/couponManage',
	'view/marketing/promotionManage',
	'view/marketing/couponIssueManage',
	'view/marketing/messageManage',
	'view/marketing/pushMarketingManage',
	

	'collections/FranchiseCollection',
	'i18n!templates/globals/lang/nls/language',
], function(
		Bracket,
		BaseView,

		MemberManageView,
		CouponManageView,
		PromotionManageView,
		CouponIssueManageView,
		MessageManageView,
		PushMarketingManageView,

		FranchiseCollection,
		i18n
		){
	'use strict';

	var Router = Backbone.Router.extend({
	    routes: {
	      'memberManage': 'memberManage',
	      'couponManage': 'couponManage',
	      'promotionManage': 'promotionManage',
	      'couponIssueManage': 'couponIssueManage',
	      'messageManage': 'messageManage',
	      'pushMarketingManage': 'pushMarketingManage',

	      '*actions': 'memberManage'
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
	  	  "current": "080"  //"Marketing"
	    };
	    BaseView.init(menuNavi);

		var pageNavi = {
			"windowTitle": "Marketing - ADMIN ({{nowPage}})",
			"pageTitle" : "{{nowPage}}",
			"pageDesc" : "reports & statics",
			"pageNavi" : [
              	{"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin/", hasNext: false},
				{"icon" : "", "title": i18n.BO1048, "link": "marketing", hasNext: false},
				{"icon" : "", "title": "{{nowPage}}", hasNext: false}
			]
		};

		router.on('route:memberManage', function(){
			beforerender('member');

			pageNavi.nowPage = { "nowPage" : i18n.BO1049, "nowLink" : "memberManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new MemberManageView({ model : franchise });

	        Bracket.hideProgress();
	        removeEvent(contentView);
		});

		router.on('route:couponManage', function(){
			beforerender('coupon');

			pageNavi.nowPage = { "nowPage" : i18n.BO1064, "nowLink" : "couponManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new CouponManageView({ model : franchise });

	        Bracket.hideProgress();
	        removeEvent(contentView);
		});
		
		router.on('route:promotionManage', function(){
			beforerender('promotion');

			pageNavi.nowPage = { "nowPage" : i18n.BO1050, "nowLink" : "promotionManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new PromotionManageView({ model : franchise });

	        Bracket.hideProgress();
	        removeEvent(contentView);
		});

		router.on('route:couponIssueManage', function(){
			beforerender('couponIssue');

			pageNavi.nowPage = { "nowPage" : i18n.BO1051, "nowLink" : "couponIssueManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new CouponIssueManageView({ model : franchise });

	        Bracket.hideProgress();
	        removeEvent(contentView);
		});

		router.on('route:messageManage', function(){
			beforerender('message');

			pageNavi.nowPage = { "nowPage" : i18n.BO1052, "nowLink" : "messageManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new MessageManageView({ model : franchise });

	        Bracket.hideProgress();
	        removeEvent(contentView);
		});

		router.on('route:pushMarketingManage', function(){
			beforerender('pushMarketing');

			pageNavi.nowPage = { "nowPage" : i18n.BO1053, "nowLink" : "pushMarketingManage" };
			BaseView.pageHeader(pageNavi);

			var franchise = new FranchiseCollection();

			franchise.fetch();

			var contentView = new PushMarketingManageView({ model : franchise });

	        Bracket.hideProgress();
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

