/*
 * Filename : eventManage.js
 * Function :
 * Comment  :
 * History  : 
 *
 * Version  : 1.0
 * Author   : 
 */

define([
  'backbone',
  'mustache',
  'common',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/management/eventManage.html',
  'text!templates/management/modal/event_modal.html',
  'text!templates/common/modal_form_alert.html',

  'summernote',
  'bootstrap-daterangepicker'

], function (Backbone, Mustache, Common, i18n, template, modalform, modalAlert) {
  'use strict';

  var targetUrl = "./model/management/Event";

  var imgUpTargetUrl = "./model/management/Event/Image";

  var main_tables;

  var _data = {};
  
  jQuery.browser = {};
  
  (function () {
      jQuery.browser.msie = false;
      jQuery.browser.version = 0;
      if (navigator.userAgent.match(/MSIE ([0-9]+)\./)) {
          jQuery.browser.msie = true;
          jQuery.browser.version = RegExp.$1;
      }
  })();

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
      'click .event-detail' : 'modal_detail',
      'click #new-event' : 'modal_init',
      'change #mainImage' : 'mainImagePreView',
      'click .add-on-datepicker' : 'trigger_calender'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = (view.model != undefined ? view.model.toJSON() : {});
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

      view.datatablesInit();
      
      return view;
    },
    modal_init : function(){
      var data = {};
      data.i18n = i18n;
      data.eventDate = "";
      
      var view = this;
      data.cd = view.loadBaseCode("0");

      $('#event-modal').html(Mustache.to_html(modalform, data));
      this.form_init('POST', $('#event-modal'));
    },
    modal_detail : function(event){
      var view = this;
      var _this = $(event.target);
      $.ajax({
        url : targetUrl,
        data : {
          data : JSON.stringify({
            id : _this.attr("target-code"),
          })
        },
        success : function(response){
          if(response.success){
            var data = response.list[0];
            data.i18n = i18n;
            data.cd = view.loadBaseCode(data.noticeSt);

            data = Common.JC_format.handleData(data);
            
            $('#event-modal').html(Mustache.to_html(modalform, view.buildOptions(data)));
            if(data.eventImgPath != null && data.eventImgPath.length > 0){
              $('#event-modal').find('#main_img_container').show();
            }
            view.form_init('PUT', $('#event-modal'), _this.attr("target-code"));
          }
        }
      });
    },
    datatablesInit : function(){
      main_tables = $('#event-table').DataTable({
        "serverSide": true,
        "language": {
          "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
        },
        "ajax" : {
          "url" : targetUrl,
          "type": 'GET',
          "data" : function(data){
            return $.extend(data, _data);
          },
          "dataSrc" : 'list'
        },
        "columns" : [
          { "data" : "id", "name" : "id" },
          { "data" : "title", "name" : "TITLE" },
          { "data" : "open", "name" : "CLOSE" },
          { "data" : "noticeSt", "name" : "NOTICE_ST" },
          { "data" : "created", "name" : "CREATED" 
        	  , "render": function (data, type, row) {
                  return Common.JC_format.day(data);
              }
          },
          { "data" : "id" }
        ],
        "columnDefs":  [ {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(row.open) + " ~ "+ Common.JC_format.day(row.close);
          },
          "targets": 2
        }, {
          "render": function ( data, type, row ) {
            var titleLabel = data;
            if (data == "201001") {
              titleLabel = i18n.BO2360;
            } else if (data == "201002") {
              titleLabel = i18n.BO2361;
            }
            return titleLabel;
          },
          "targets": 3
        }, {
          "render": function ( data, type, row ) {
            return Common.JC_format.day(data);
          },
          "targets": 4
        }, {
          "render": function ( data, type, row ) {
            return '<button class="btn btn-primary event-detail" data-toggle="modal" data-target=".bs-event-modal-lg" target-code="'+data+'" >' + i18n.BO0022 + '</button>';
          },
          "bSortable": false,
          "targets": 5
        } ],
        "order": [[ 2, 'desc' ]],
        "pageLength": 10,
        "lengthChange": false,
        "processing": true,
        "rowReorder": true,
        "rowId" : 'id'
      });
    },
    validateContent : function(){
      var _content = $('#event-content-layer');
      var sHTML = $('#event-content').code();
      if (sHTML.length == 0) {
        _content.removeClass('has-success');
        _content.addClass('has-error');
        return false;
      }
      else {
        _content.removeClass('has-error');
        _content.addClass('has-success');
        return true;
      }
    },
    form_init : function(method, modal, id){
      var view = this;

      modal.find('select').select2();
      
      $.validate({
        validateOnBlur : false,
        scrollToTopOnError : false,
        showHelpOnFocus : false,
        errorMessagePosition : $('.form-validatoin-alert-detail'),
        addSuggestions : false,
        onError : function($form) {
          view.validateContent();
          
          var _target = $('#modal-form');
          if(_target.find('.alert').length == 0){
        	  _target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
          }
        },
        onSuccess : function($form) {
          if (!view.validateContent()) {
            var _target = $('#modal-form');
            if(_target.find('.alert').length == 0){
            	_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
            }
            return false;
          }
          
          var params = $form.serializeObject();
          var sHTML = $('#event-content').code();
          params.content = sHTML;
          
          $.ajax({
            url : targetUrl,
            type: 'POST',
            data : {
              "_method" : method,
              "data" : JSON.stringify(params)
            },
            success : function(response){
              if(response.success){
                var _data = new FormData();
                var _file = $form.find('#mainImage').get(0);

                if(_file.files[0]){
                  _data.append("imagefile", _file.files[0]);
                  _data.append("type", "main");
                  _data.append("id", response.id);
                  $.ajax({
                    data: _data,
                    type: "POST",
                    url: imgUpTargetUrl,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function(result) {
                      if(!result.success){
                        alert(i18n.BO4008);
                      }
                    }
                  });
                }
                $('#event-modal').modal('toggle');
                main_tables.draw();
              }
            }
          });

          return false;
        }
      });
      
      view.setCalendarArea();
      
      $('#event-content').summernote({
        lang: 'ko-KR',
        height: 150,
        dialogsInBody: true,
        onChange: function(contents, $editable) {
          var isEmpty = $('#event-content').summernote('isEmpty');
          if(isEmpty){
            $('#event-content').val('');
          } else {
            $('#event-content').val($('#event-content').summernote('code'));
          }
        },
        onImageUpload: function(files, editor, welEditable) {
          view.uploadImage(files[0], editor, welEditable);
        }
      });
    },
    
    setCalendarArea : Common.JC_calendar.searchRange(i18n, true),
    
    loadBaseCode : function(codeValue){
      var data = {};
      if(codeValue != undefined){
        $.ajax({
          url : './model/Codes',
          method : 'POST',
          async: false,
          data : {
            'codes' : 'NoticeStatus'
          },
          success : function(result){
            if(result.success){
              $.each(Object.keys(result.codes), function(idx, code){
                $.each(result.codes[code], function(_idx, _code){
                  if (_code.subCd == "001" || _code.title == "진행중") {
                    _code.titleLabel = i18n.BO2360;
                  } else if (_code.subCd == "002" || _code.title == "종료") {
                    _code.titleLabel = i18n.BO2361;
                  } else {
                    _code.titleLabel = _code.title;
                  }
                  if (codeValue == _code.baseCd) {
                    _code.selections = ' selected="selected"';
                  }
                });
              });
              data = $.extend(result.codes, _data);
            } else {
              //
            }
          }
        });
      }
      return data;
    },
    buildOptions : function(object) {
      for (var i in object) {
        object[i + '=' + object[i]] = true;
      }
      return object;
    },
    mainImagePreView : function(event){
      var _this = $(event.target);
      var ext = _this.val().split('.').pop().toLowerCase(); //확장자
      if (ext.length > 0) {
        if($.inArray(ext, ['gif', 'png', 'jpg', 'jpeg']) == -1) {
          if ($.browser.msie) {
            // ie 일때 input[type=file] init.
            _this.replaceWith( $("#filename").clone(true) );
          } else {
            // other browser 일때 input[type=file] init.
            _this.val("");
          }
          window.alert(i18n.BO4009 + ' (' + i18n.BO4010 + ')');
        } else {
          var file = _this.prop("files")[0];
          var blobURL = window.URL.createObjectURL(file);
  
          var _image = $('<img />').attr('src', blobURL);
          $('#event-modal').find('#main_img_target').html(_image);
          $('#event-modal').find('#main_img_container').show();
        }
      }
    },
    uploadImage : function(file, editor, welEditable) {
      var _data = new FormData();
      _data.append("imagefile", file);
      _data.append("type", "content");
      _data.append("id", $('form').find("[name=id]").val());
      $.ajax({
        data: _data,
        type: "POST",
        url: imgUpTargetUrl,
        cache: false,
        contentType: false,
        processData: false,
        success: function(result) {
          if(result.success){
            welEditable.focus();
            editor.insertImage(welEditable, result.url);
            $('form').append('<input type="hidden" name="contentImage" value="'+result.url+'">');
          }
        }
      });
    },
    trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    }
  });

  return ContentView;
});