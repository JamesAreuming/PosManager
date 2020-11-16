/*
 * Filename	: base.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */


define([
  'models/common/PlatformModel',
  'models/ManagerModel',

  'view/base/header',
  'view/base/pageHeader',
  'view/base/leftPanel',
  'view/base/rightPanel',
  'view/base/footer',

  // global exported libs.
  'bracket'
], function (PlatformModel, ManagerModel,
             HeaderView, PageHeaderView, LeftPanelView, RightPanelView, FooterView,
             Bracket) {
  'use strict';
  var header = null;
  var footer = null;
  var leftPanel = null;
  var rightPanel = null;
  var pgHeader = null;

  var init = function (menuNavi) {
    // create model
    var platform = new PlatformModel();
    var manager = new ManagerModel();

    // create view
    if (header == null) {
      header = new HeaderView({model: manager});
    }
    header.render();

    if (footer == null) {
      footer = new FooterView({model: platform});
    }
    footer.render();

    if (leftPanel == null) {
      leftPanel = new LeftPanelView();
    }
    leftPanel.render();

    if (rightPanel == null) {
      rightPanel = new RightPanelView();
    }
    rightPanel.render();

    // get model
    // platform.fetch();
    // manager.fetch();

    Bracket.init(); // init modern theme core componets
  };

  var pageHeader = function (pageNavi) {
    if (pgHeader == null) {
      pgHeader = new PageHeaderView();
    }
    pgHeader.render(pageNavi);
  };

  return {
    init: init,
    pageHeader: pageHeader
  }
});

