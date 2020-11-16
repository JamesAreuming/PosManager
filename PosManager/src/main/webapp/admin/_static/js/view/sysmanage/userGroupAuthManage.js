/*
 * Filename	: dashboard.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
  'backbone',
  'mustache',

  'text!templates/sysmanage/userGroupAuthManage.html',
  'i18n!templates/globals/lang/nls/language',

  'bootstrap-treeview',
  'form-select2'
  
  /*
  'text!templates/sysmanage/modal/basecode_modal_sub.html',
  
  */
], function (Backbone, Mustache, template, i18n) {
  'use strict';
  
  var tree;
  
  var modelUrl = './model/UserGroupAuth';
  
  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click button.treeview-submit' : 'treeview_submit'
    },
    initialize: function () {
      this.template = template;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = (view.model != undefined ? (view.model.toJSON()[0]) : {});
      data.i18n = i18n;
      var rendered = Mustache.to_html(view.template, data);
      
      $(view.el).append(rendered);
    	    
      $('select').select2().on('change', function(){
    	  view.renderTree($(this).val());
      });

      return view;
    },
    init_treeview : function(list){
    	var view = this;
    	$('#tree_view').treeview({
		  customCheckboxed: true,
		  isUnboundClickEv: true,
    	  data: list,         // data is not optional
    	  levels: 2
    	});
    	
    	$('.treeview-submit').get(0).disabled = false;
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i + '=' + object[i]] = true;
        }
        return object;
    },
    renderTree : function( usergroup ){
    	var view = this;
    	
     	
        $.ajax({
	       	 url : modelUrl,
	       	 data : {
	       		data : JSON.stringify({groupId : usergroup})
	       	 },
	       	 method: 'get',
	       	 success : function(result){
	       		 if(result.success){
	       			 view.init_treeview(result.list);
	       		 }
	       	 }
         });
    },
    treeview_submit : function(){
    	
    	var submit_node = [];
    	
    	$('#tree_view')
    		.find('li.node-tree_view')
    		.each(function(index, node){
    		var _this = $(this);
    		var _node = $('#tree_view').treeview('getNode', parseInt($(node).attr('data-nodeid'), 10));
    		var _checkbox_group = _this.find('[type=checkbox]');
    		
    		for(var key in _node){
    			var _checkbox = _this.find('.'+key);
    			
    			if(_checkbox.length == 1){
    				_node[key] = _checkbox.is(':checked');
    			}
    		}
    		
    		delete _node.nodes;
    		delete _node.state;
    		delete _node.parentId;
    		delete _node.selectable;
    		delete _node.nodeId;
    		
    		submit_node.push(_node);
    	});
    	
    	
    	$.ajax({
    		url : modelUrl,
    		type: 'POST',
    		data : {
    			"_method" : 'PUT',
    			"data" : JSON.stringify(submit_node) 
    		},
    		success : function(response){
    			if(response.success){
    				view.renderTree($('select').val());
    			}
    		}
    	});
    	
    	return false;
    }
    
  });

  return ContentView;
});	