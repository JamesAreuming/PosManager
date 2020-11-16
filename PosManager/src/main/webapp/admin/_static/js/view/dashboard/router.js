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
  'bracket'      
  ,'view/base'
  ,'view/dashboard/dashboard'

  ,'models/DashboardModel'
  ,'collections/FranchiseCollection'
  ,'i18n!templates/globals/lang/nls/language'
  
], function (Bracket, BaseView, MainView, DashboardModel, FranchiseCollection, i18n) {
  'use strict';

  var Router = Backbone.Router.extend({
    routes: {
      'dashboard': 'dashboard',
      '*actions': 'default'
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
	
  var init = function () {
    var router = new Router;
    
    var menuNavi = {
      "current": "010"  //"Dashboard"
    };
    BaseView.init(menuNavi);

    var pageNavi = {
        "windowTitle": "Store Commerce - ADMIN ({{nowPage}})",
        "pageTitle": "{{nowPage}}",
        "pageDesc": "reports & statics",
        "pageNavi": [
          {"icon" : "fa fa-home", "title": i18n.BO1001, "link": "/admin", hasNext: false},
          {"icon" : "", "title": i18n.BO1002, "link": "/admin", hasNext: false}
        ]
      };
    
    // dashboard
    router.on('route:dashboard', function () {
		beforerender('dashboard');

		pageNavi.nowPage = { "nowPage" : i18n.BO1002, "nowLink" : "dashboard" };
		BaseView.pageHeader(pageNavi);

		var franchise = new FranchiseCollection();
		franchise.fetch();

		// create model
		var dashboard = new DashboardModel();
		// get model
		dashboard.fetch();
		
		
		var contentView = new MainView({model: franchise});
		beforeViewRemove(contentView);
		
        Bracket.hideProgress();
      
    });

    // default(홈)
    router.on('route:default', function () {
      //bootbox.alert('페이지가 존재하지 않습니다.');
      window.location.hash = "dashboard";
    });

    Backbone.history.start();

    return router;
  };

  return {
    init: init
  };
});

