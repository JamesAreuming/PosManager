/*
 * Filename	: bracket.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
  'jquery',
  'toggles',
  'jquery-cookies',
  'jquery-sparkline'
], function ($) {
  'use strict';

  var init = function () {
    initLeftMenu();
    adjustmainpanelheight();
    initWidget();

    $(window).resize(function () {
      if ($('body').css('position') == 'relative') {
        $('body').removeClass('leftpanel-collapsed chat-view');
      } else {
        $('body').removeClass('chat-relative-view');
        $('body').css({left: '', marginRight: ''});
      }
      //reposition_searchform();
      reposition_topnav();
    });

    // Sticky Header
    if ($.cookie('sticky-header'))
      $('body').addClass('stickyheader');

    // Sticky Left Panel
    if ($.cookie('sticky-leftpanel')) {
      $('body').addClass('stickyheader');
      $('.leftpanel').addClass('sticky-leftpanel');
    }

    // Left Panel Collapsed
    if ($.cookie('leftpanel-collapsed')) {
      $('body').addClass('leftpanel-collapsed');
      $('.menutoggle').addClass('menu-collapsed');
    }

    // Changing Skin
    var c = $.cookie('change-skin');
    var cssSkin = 'css/style.' + c + '.css';
    if ($('body').css('direction') == 'rtl') {
      cssSkin = '../css/style.' + c + '.css';
      $('html').addClass('rtl');
    }
    if (c) {
      $('head').append('<link id="skinswitch" rel="stylesheet" href="' + cssSkin + '" />');
    }

    // Changing Font
    var fnt = $.cookie('change-font');
    if (fnt) {
      $('head').append('<link id="fontswitch" rel="stylesheet" href="css/font.' + fnt + '.css" />');
    }

    // Check if leftpanel is collapsed
    if ($('body').hasClass('leftpanel-collapsed'))
      $('.nav-bracket .children').css({display: ''});


    // Handles form inside of dropdown
    $('.dropdown-menu').find('form').click(function (e) {
      e.stopPropagation();
    });


    // This is not actually changing color of btn-primary
    // This is like you are changing it to use btn-orange instead of btn-primary
    // This is for demo purposes only
    var c = $.cookie('change-skin');
    if (c && c == 'greyjoy') {
      $('.btn-primary').removeClass('btn-primary').addClass('btn-orange');
      $('.rdio-primary').addClass('rdio-default').removeClass('rdio-primary');
      $('.text-primary').removeClass('text-primary').addClass('text-orange');
    }

    // Chat View
    $('#menu-favorite').click(function () {

      var body = $('body');
      var bodypos = body.css('position');

      if (bodypos != 'relative') {
        if (!body.hasClass('chat-view')) {
          body.addClass('leftpanel-collapsed chat-view');
          $('.nav-bracket ul').attr('style', '');
        } else {
          body.removeClass('chat-view');

          if (!$('.menutoggle').hasClass('menu-collapsed')) {
            $('body').removeClass('leftpanel-collapsed');
            $('.nav-bracket li.active ul').css({display: 'block'});
          } else {

          }
        }
      } else {
        if (!body.hasClass('chat-relative-view')) {
          body.addClass('chat-relative-view');
          body.css({left: ''});
        } else {
          body.removeClass('chat-relative-view');
        }
      }

    });

    /*
     sparkLine('#sidebar-chart3', [5, 9, 3, 8, 4, 10, 8, 5, 7, 6, 9, 3], {
     type: 'bar',
     height: '30px',
     barColor: '#1CAF9A'
     });
     */
  };

  var initLeftMenu = function () {
    // Toggle Left Menu
    $('.leftpanel .nav-parent > a').on('click', function () {

      var parent = $(this).parent();
      var sub = parent.find('> ul');

      // Dropdown works only when leftpanel is not collapsed
      if (!$('body').hasClass('leftpanel-collapsed')) {
        if (sub.is(':visible')) {
          sub.slideUp(200, function () {
            parent.removeClass('nav-active');
            $('.mainpanel').css({height: ''});
            adjustmainpanelheight();
          });
        } else {
          closeVisibleSubMenu();
          parent.addClass('nav-active');
          sub.slideDown(200, function () {
            adjustmainpanelheight();
          });
        }
      }
      return false;
    });
  };

  var closeVisibleSubMenu = function () {
    $('.leftpanel .nav-parent').each(function () {
      var t = $(this);
      if (t.hasClass('nav-active')) {
        t.find('> ul').slideUp(200, function () {
          t.removeClass('nav-active');
        });
      }
    });
  };

  var adjustmainpanelheight = function () {
    // Adjust mainpanel height
    var docHeight = $(document).height();
    if (docHeight > $('.mainpanel').height())
      $('.mainpanel').height(docHeight);
  };

  var initWidget = function () {
    $('#pageContent').on('DOMSubtreeModified', function () {
      // Tooltip
      if ($('.tooltips').length > 0) {
        $('.tooltips').tooltip({container: 'body'});
      }

      // Popover
      if ($('.popovers').length > 0) {
        $('.popovers').popover();
      }

      // Close Button in Panels
      $('.panel .panel-close').click(function () {
        $(this).closest('.panel').fadeOut(200);
        return false;
      });

      // Form Toggles
      $('.toggle').toggles({on: true});

      $('.toggle-chat1').toggles({on: false});

      // Minimize Button in Panels
      $('.minimize').click(function () {
        var t = $(this);
        var p = t.closest('.panel');
        if (!$(this).hasClass('maximize')) {
          p.find('.panel-body, .panel-footer').slideUp(200);
          t.addClass('maximize');
          t.html('&plus;');
        } else {
          p.find('.panel-body, .panel-footer').slideDown(200);
          t.removeClass('maximize');
          t.html('&minus;');
        }
        return false;
      });
    });

    // Menu Toggle
    $('.menutoggle').click(function () {
      var body = $('body');
      var bodypos = body.css('position');

      if (bodypos != 'relative') {
        if (!body.hasClass('leftpanel-collapsed')) {
          body.addClass('leftpanel-collapsed');
          $('.nav-bracket ul').attr('style', '');
          $(this).addClass('menu-collapsed');
        } else {
          body.removeClass('leftpanel-collapsed chat-view');
          $('.nav-bracket li.active ul').css({display: 'block'});
          $(this).removeClass('menu-collapsed');
        }
      } else {
        if (body.hasClass('leftpanel-show')) {
          body.removeClass('leftpanel-show');
        } else {
          body.addClass('leftpanel-show');
        }
        adjustmainpanelheight();
      }
    });

    // Add class everytime a mouse pointer hover over it
    $('.nav-bracket > li').hover(function () {
      $(this).addClass('nav-hover');
    }, function () {
      $(this).removeClass('nav-hover');
    });
  };

  /* This function will reposition search form to the left panel when viewed
   * in screens smaller than 767px and will return to top when viewed higher
   * than 767px
   */
  var reposition_searchform = function () {
    if ($('.searchform').css('position') == 'relative') {
      $('.searchform').insertBefore('.leftpanelinner .userlogged');
    } else {
      $('.searchform').insertBefore('.header-right');
    }
  }


  /* This function allows top navigation menu to move to left navigation menu
   * when viewed in screens lower than 1024px and will move it back when viewed
   * higher than 1024px
   */
  var reposition_topnav = function () {
    if ($('.nav-horizontal').length > 0) {

      // top navigation move to left nav
      // .nav-horizontal will set position to relative when viewed in screen below 1024
      if ($('.nav-horizontal').css('position') == 'relative') {

        if ($('.leftpanel .nav-bracket').length == 2) {
          $('.nav-horizontal').insertAfter('.nav-bracket:eq(1)');
        } else {
          // only add to bottom if .nav-horizontal is not yet in the left panel
          if ($('.leftpanel .nav-horizontal').length == 0)
            $('.nav-horizontal').appendTo('.leftpanelinner');
        }

        $('.nav-horizontal').css({display: 'block'})
          .addClass('nav-pills nav-stacked nav-bracket');

        $('.nav-horizontal .children').removeClass('dropdown-menu');
        $('.nav-horizontal > li').each(function () {

          $(this).removeClass('open');
          $(this).find('a').removeAttr('class');
          $(this).find('a').removeAttr('data-toggle');

        });

        if ($('.nav-horizontal li:last-child').has('form')) {
          $('.nav-horizontal li:last-child form').addClass('searchform').appendTo('.topnav');
          $('.nav-horizontal li:last-child').hide();
        }

      } else {
        // move nav only when .nav-horizontal is currently from leftpanel
        // that is viewed from screen size above 1024
        if ($('.leftpanel .nav-horizontal').length > 0) {

          $('.nav-horizontal').removeClass('nav-pills nav-stacked nav-bracket')
            .appendTo('.topnav');
          $('.nav-horizontal .children').addClass('dropdown-menu').removeAttr('style');
          $('.nav-horizontal li:last-child').show();
          $('.searchform').removeClass('searchform').appendTo('.nav-horizontal li:last-child .dropdown-menu');
          $('.nav-horizontal > li > a').each(function () {

            $(this).parent().removeClass('nav-active');

            if ($(this).parent().find('.dropdown-menu').length > 0) {
              $(this).attr('class', 'dropdown-toggle');
              $(this).attr('data-toggle', 'dropdown');
            }

          });
        }
      }
    }
  }

  var sparkLine = function (id, vals, options) {
    $(id).sparkline(vals, options);
  };

  var fitPosition = function () {
    var main = $(".mainpanel");
    var header = $("#header");
    var pageHeader = $("#pageHeader");
    var width = main.width();
    var height = main.height() - header.height() - pageHeader.height();

    var preloader = $('#preloader');
    preloader.width(width);
    preloader.height(height);

    //var spinner = $('#preloader-status');
    //var css = {top: (height / 2) - (spinner.height()), left: (width / 2) - (spinner.width()), position:'absolute'};

    //console.log("pageContent: width = " + width + ", height = " + height);
    //console.log(css);
    // $('#preloader-status').css(css);
  };

  var startResizer = function () {
    $(window).on('resize', function () {
      fitPosition();
    });
  };

  var stopResizer = function () {
    $(window).off('resize');
  };

  var showProgress = function () {
    //console.log("showProgress");
    if ($('#full-preloader').length > 0) { // when page first load
      // skip
    } else {
      // prepend preloader (if not exists)
      if ($(event.target).find("#preloader").length == 0) {
        // create progress if not exists
        console.log("preloader not exist!!");
        var preloader = $('<div id="preloader"> <div id="preloader-status"><i class="fa fa-spinner fa-spin"></i></div> </div>');
        $("#pageContent").prepend(preloader);
      }

      $('#preloader-status').fadeIn();
      $('#preloader').delay(350).fadeIn(function () {
        $('body').delay(350).css({'overflow': 'hidden'});
      });

      fitPosition();
      startResizer();
    }
  };

  var hideProgress = function () {
    //console.log("hideProgress = " + $('#full-preloader').length);
    if ($('#full-preloader').length > 0) { // when page first load
      $('#full-status').fadeOut();
      $('#full-preloader').delay(350).fadeOut(function () {
        $('body').delay(50).css({'overflow': 'visible'});
        $('#full-preloader').remove();
      });
    } else {
      $('#preloader-status').fadeOut();
      $('#preloader').delay(350).fadeOut(function () {
        $('body').delay(350).css({'overflow': 'visible'});
        $('#preloader').remove();
        stopResizer();
      });
    }
  };

  return {
    init: init,
    showProgress: showProgress,
    hideProgress: hideProgress,
    sparkline: sparkLine
  };
});