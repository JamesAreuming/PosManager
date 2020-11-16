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
  'common',
  'i18n!templates/globals/lang/nls/language',
  
  'text!templates/management/userManage.html',
  'text!templates/management/modal/user_modal.html',
  'text!templates/management/modal/userPermition.html',
  'text!templates/common/modal_form_alert.html',
  
  'bootstrap-treeview'

], function (Backbone, Mustache, Common, i18n, template, modalform, permition, modalAlert) {
  'use strict';

  var targetUrl = './model/management/User';
  var modelUrl = './model/management/UserPermition';
	  
  var main_tables;

  var _data = {};

  var _def_form_mustache =
  {
    'id' : '{{id}}',
    'franId' : '{{franId}}',
    'brandId' : '{{brandId}}',
    'storeId' : '{{storeId}}',
    'name': '{{name}}',
    'username' : '{{username}}',
    'userGroupId' : '{{userGroupId}}',
    'fax' : '{{fax}}',
    'mb' : '{{mb}}',
    'email' : '{{email}}'
  };

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events : {
    	'click #new-user' : 'modal_init',
    	'click #user-tables button.user-detail' : 'load_detail',
    	'click #user-tables button.permition' : 'renderTree',
    	'click button.treeview-submit' : 'treeview_submit',
    	'change #type' : 'showUserRole'
    },
    initialize: function () {
      this.template = template;
      //this.mainHeader = mainHeader;
      this.listenTo(this.model, 'sync', this.render);
    },
    render: function () {
      var view = this;
      var data = {};
      
      data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
      data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
      data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
      
      _data = view.model.toJSON()[0].session.userInfo;
      data.userInfo = view.model.toJSON()[0].session.userInfo;
      data.i18n = i18n;
      
      if(data.frList == undefined || data.frList == ""){
    	  data.isFr = true;
      }else{
    	  data.isFr = false;
      }
      
      if(data.isFr == true && data.brList == undefined || data.brList == ""){
    	  data.isBr = true;
      }else{
    	  data.isBr = false;
      }
      
      if(data.frList == undefined && data.brList == undefined && data.stList == undefined){
    	  data.isSt = true;
      }else{
    	  data.isSt = false;
      }
      
      var rendered = Mustache.to_html(view.template, data);
      $(view.el).append(rendered);

//      var mainHeader = Mustache.to_html(view.mainHeader, data);
//      
//      $('#mainHeader').append(mainHeader);      
      
      view.selectedInit();

      view.datatablesInit();
      
      return view;
    },
    showUserRole : function(){
    	if($('#type').val() != "300004" && $('#type').val() != "300006"){
    		$('#m_franchise_select').val('');
    		$('#m_franchise_select').attr("data-validation","");
    		$('#franchiseDiv').hide();
    		
    		$('#m_brand_select').val('');
    		$('#m_brand_select').attr("data-validation","");
    		$('#brandDiv').hide();
    		
    		$('#m_store_select').val('');
    		$('#m_store_select').attr("data-validation","");
    		$('#storeDiv').hide();
    	}else{
    		$('#m_franchise_select').val('');
    		$('#m_franchise_select').attr("data-validation","required");
    		$('#franchiseDiv').show();
    		
    		$('#m_brand_select').val('');
    		$('#m_brand_select').attr("data-validation","");
    		$('#brandDiv').show();
    		
    		$('#m_store_select').val('');
    		$('#m_store_select').attr("data-validation","");
    		$('#storeDiv').show();
    	}
    	
    	if($('#type').val() == "300003" || $('#type').val() == "300004"){
    		$('#userRole').val('');
    		$('#userRole').attr("data-validation","required");
    		$('#userRoleDiv').show();
    	}else{
    		$('#userRole').val('');
    		$('#userRole').attr("data-validation","");
    		$('#userRoleDiv').hide();
    	}
    },
    datatablesInit : function(){
    	main_tables = $('#user-tables').DataTable({
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
			  { "data" : "id", "name" : "id"},
			  { "data" : "username", "name" : "username"},
			  { "data" : "type", "name" : "type"},
			  { "data" : "name", "name" : "name"},
			  { "data" : "mb", "name" : "mb"},
			  { "data" : "status", "name" : "status"},
			  { "data" : "id"},
			  { "data" : "id"}
			],
			"columnDefs":  [ {
		        "render": function ( data, type, row ) {
		        	return '<button class="btn btn-primary btn-sm user-detail" data-toggle="modal" data-target=".bs-user-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>'; 
		        },
		        "bSortable": false,
		        "targets":6
		    },{
		        "render": function ( data, type, row ) {
		        	var temp = ""; 
		        	//if(row.type == "Store Admin" || row.type == "System Admin"){
		        	//if(row.type == "시스템관리자" || row.type == "매장관리자"){
		        		temp = '<button class="btn btn-primary btn-sm permition" data-toggle="modal" data-target=".bs-user-permition-modal-lg" target-code="'+data+'" >퍼미션</button>';
		        	//}
		        	
		        	return temp; 
		        },
		        "bSortable": false,
		        "targets":7
		    } ],
		    "order": [[ 1, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "rowId" : 'id'
		});
    },
    selectedInit : function (){

    	$('#store_select').select2();
    	$('#brand_select').select2();

        $('#franchise_select').select2().on('change', function(){

    		_data.franId = $(this).val();
    		
    		$.ajax({
    			url : './model/Brand',
    			data : {
    				data : JSON.stringify({ franId : $(this).val() })
    			},
    			success : function(result){
    				if(result.success){
    					var defualt_array = [{id: '', storeNm : i18n.BO1033}];
    					
      					$("#store_select").find('option').remove();

      					$('#store_select').select2({
      						data : defualt_array.concat(result.list),
      						templateResult : function(state) {
      							return state.storeNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.storeNm;
      						}
      					})
      					
    					defualt_array = [{id: '', brandNm : i18n.BO1032}];

    					$("#brand_select").find('option').remove();
    					
    					$('#brand_select').select2({
    						data : defualt_array.concat(result.list),
    						templateResult : function(state) {
    							return state.brandNm;
    						},
    						templateSelection : function(data, container) {
    						    return data.brandNm;
    						}
    					})
    				}
    			}
    		})
    		main_tables.draw();
    	});
        
        $('#brand_select').select2().on('change', function(){
        	_data.data = JSON.stringify({brandId : $(this).val()});
			_data.brandId = $(this).val();
			
    		$.ajax({
    			url : './model/Store',
    			data : {
    				data : JSON.stringify({ brandId : $(this).val() })
    			},
    			success : function(result){
    				if(result.success){
    					var defualt_array = [{id: '', storeNm : i18n.BO1033}];

    					$("#store_select").find('option').remove();

    					$('#store_select').select2({
    						data : defualt_array.concat(result.list),
    						templateResult : function(state) {
    							return state.storeNm;
    						},
    						templateSelection : function(data, container) {
    						    return data.storeNm;
    						}
    					})
    				}
    			}
    		})
				
			main_tables.draw();
        });
        
        $('#store_select').select2().on('change', function(){
        	_data.data = JSON.stringify({storeId : $(this).val()});
			_data.storeId = $(this).val();
			
			main_tables.draw();
        });        
    },
    selectedInit2 : function (){
    	
    	$('#m_store_select').select2();
    	$('#m_brand_select').select2();

        $('#m_franchise_select').select2().on('change', function(){
    		$.ajax({
    			url : './model/Brand',
    			data : {
    				data : JSON.stringify({ franId : $(this).val() })
    			},
    			success : function(result){
    				if(result.success){
    					var defualt_array = [{id: '', storeNm : i18n.BO1033}];
    					
      					$("#m_store_select").find('option').remove();

      					$('#m_store_select').select2({
      						data : defualt_array.concat(result.list),
      						templateResult : function(state) {
      							return state.storeNm;
      						},
      						templateSelection : function(data, container) {
      						    return data.storeNm;
      						}
      					})
      					
    					defualt_array = [{id: '', brandNm : i18n.BO1032}];

    					$("#m_brand_select").find('option').remove();
    					
    					$('#m_brand_select').select2({
    						data : defualt_array.concat(result.list),
    						templateResult : function(state) {
    							return state.brandNm;
    						},
    						templateSelection : function(data, container) {
    						    return data.brandNm;
    						}
    					})
    					
    					if(_data.brandId != undefined){
      			    		$('#modal_brand_select').select2('val', _data.brandId );
      			    	}
    				}
    			}
    		})
    	});
        
        $('#m_brand_select').select2().on('change', function(){			
    		$.ajax({
    			url : './model/Store',
    			data : {
    				data : JSON.stringify({ brandId : $(this).val() })
    			},
    			success : function(result){
    				if(result.success){
    					var defualt_array = [{id: '', storeNm : i18n.BO1033}];

    					$("#m_store_select").find('option').remove();

    					$('#m_store_select').select2({
    						data : defualt_array.concat(result.list),
    						templateResult : function(state) {
    							return state.storeNm;
    						},
    						templateSelection : function(data, container) {
    						    return data.storeNm;
    						}
    					})
    					
    					if(_data.storeId != undefined){
      			    		$('#modal_brand_select').select2('val', _data.storeId );
      			    	}
    				}
    			}
    		})
        });
    },
    modal_init : function(){
    	$('#user-modal').empty();
    	
    	var view = this;
    	
    	if(_data.franId == ""){
    		// BO4001 : 법인을 선택해 주세요
    		alert(i18n.BO4001);
    		$("#franchise_select").focus();
    		return false;
    	}
    	var data = {};
    	data.i18n = i18n;
    	
    	var code = view.loadBaseCode(true);
    	
    	var userInfo = view.model.toJSON()[0].session.userInfo;
    	code.memberTypeList = view.modifyMemberType(code.membertype, userInfo);
		
		data.i18n = i18n;
		data.franId = $('#franchise_select').val();
		data.brandId = $('#brand_select').val();
		data.storeId = $('#store_select').val();
		
		var _template = Mustache.to_html(modalform, $.extend(code, data));
		
		$('#user-modal').html(Mustache.to_html(_template, view.buildOptions(data)));  
		
		view.form_init('POST', $('#user-modal'));
		
    },
    form_init : function(method, modal){
    	var view = this;
    	
    	modal.find('select').select2();
    	view.selectedInit2();
    	
		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target = $('#user-modal-form');
		    	
		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {
	        	
	        	if($('#type').val() == "300004"){
	        		if($('#userRole').val() == "2"){
	        			if($('#m_franchise_select').val() == ""){
	            			alert(i18n.BO4001);
	            			return false;
	            		}
	        		}
	        		
	        		if($('#userRole').val() == "3"){
	        			if($('#m_franchise_select').val() == ""){
	            			alert(i18n.BO4001);
	            			return false;
	            		}
	        			
	        			if($('#m_brand_select').val() == ""){
	            			alert(i18n.BO4002);
	            			return false;
	            		}
	        		}
	        		
	        		if($('#userRole').val() == "4"){
	        			if($('#m_franchise_select').val() == ""){
	            			alert(i18n.BO4001);
	            			return false;
	            		}
	        			
	        			if($('#m_brand_select').val() == ""){
	            			alert(i18n.BO4002);
	            			return false;
	            		}
	        			
	        			if($('#m_store_select').val() == ""){
	            			alert(i18n.BO4003);
	            			return false;
	            		}
	        		}
	        	}else if($('#type').val() == "300006"){
	    			if($('#m_franchise_select').val() == ""){
	        			alert(i18n.BO4001);
	        			return false;
	        		}
	    		}
	        	
        		var params = $form.serializeObject();

	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response){
	        			if(response.success){
	        				$('#user-modal').modal('toggle');
	    		    		main_tables.draw();
	        			}else{
	        				alert(response.errMsg);
	        			}
	        		}
	        	});

	        	return false;
	        }
		});
    },
    load_detail : function(event){
    	$('#user-modal').empty();
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
    				var code = view.loadBaseCode(true,response.list);
    				
    				var userInfo = view.model.toJSON()[0].session.userInfo;
    				code.memberTypeList = view.modifyMemberType(code.membertype, userInfo);
    				
    				var data = response.list;
    				data.MemberType = data.type;
    				data.MemberStatus = data.status;
    				data.i18n = i18n;
    				
    				if(response.list.type == "300003" || response.list.type == "300004"){
    					
    					data.showUserRole = true;
    				}
    				
    				console.log(code);
    				
    				var _template = Mustache.to_html(modalform, $.extend(code, data ));
    				
    				console.log(response.list);
    				$('#user-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));  

    				if($('#type').val() != "300003" && $('#type').val() != "300004"){
    					$('#userRole').val('');
    		    		$('#userRole').attr("data-validation","");
    		    		$('#userRoleDiv').hide();
    		    	}
    				
    				if($('#type').val() != "300004" && $('#type').val() != "300006"){
    					$('#m_franchise_select').val('');
    		    		$('#m_franchise_select').attr("data-validation","");
    		    		$('#franchiseDiv').hide();
    		    		
    		    		$('#m_brand_select').val('');
    		    		$('#m_brand_select').attr("data-validation","");
    		    		$('#brandDiv').hide();
    		    		
    		    		$('#m_store_select').val('');
    		    		$('#m_store_select').attr("data-validation","");
    		    		$('#storeDiv').hide();
    		    	}
    				
    				view.form_init('PUT', $('#user-modal'));
    			}
    		}
    	});
    },
    modifyMemberType : function(membertype, userInfo){
    	var _memberTypeList = [];
			if (membertype!= null && membertype != undefined) {
				$.each(membertype, function(idx, _code){
					var isAddMemberType = true;
					if (userInfo != null && userInfo != undefined && userInfo.type == "300004") {
						if (_code.baseCd != "300004" && _code.baseCd != "300006") {
							isAddMemberType = false;
						}
					}
					if (isAddMemberType) {
						_memberTypeList.push(_code);
					}
				});
			}
			return _memberTypeList;
    },
    buildOptions : function(object) {
        for (var i in object) {
            object[i.toLowerCase() + '=' + object[i]] = true;
        }
        return object;
    },    
    loadBaseCode : function(option, getData){
    	
    	var temp = {};
    	
    	if(getData == "" || getData == undefined){
    		if(_data.franId == "" || _data.franId == undefined){
    			temp.franId = "";
    		}else{
    			temp.franId = _data.franId;
    		}
    		
    		temp.brandId = _data.brandId;
    	}else{
    		temp.franId = getData.franId;
    		temp.brandId = getData.brandId;
    	}
    	
    	var data = {};
    	
    	$.ajax({
			url : './model/Franchise',
			data : {
				data : JSON.stringify({ id : temp.franId })
			},
			async : false,
			success : function(result){
				if(result.success){
					if(option){
						$.each(result.list, function(idx, code){
							code.selections = '{{#franid='+code.id+'}} selected="selected" {{/franid='+code.id+'}}';
						});
					}
					data.fran = result.list;
				}
			}
		});
    	
    	$.ajax({
			url : './model/Brand',
			data : {
				data : JSON.stringify({ franId : temp.franId })
			},
			async : false,
			success : function(result){
				if(result.success){
					if(option){
						$.each(result.list, function(idx, code){
							code.selections = '{{#brandid='+code.id+'}} selected="selected" {{/brandid='+code.id+'}}';
						});
					}
					data.brand = result.list;
				}
			}
		});
    	
    	$.ajax({
			url : './model/Store',
			data : {
				data : JSON.stringify({ franId : temp.franId, brandId : temp.brandId })
			},
			async : false,
			success : function(result){
				if(result.success){
					if(option){
						$.each(result.list, function(idx, code){
							code.selections = '{{#storeid='+code.id+'}} selected="selected" {{/storeid='+code.id+'}}';
						});
					}
					data.store = result.list;
				}
			}
		});
    	
    	$.ajax({
			url : './model/management/UserGroup',
			async : false,
			success : function(result){
				if(result.success){
					if(option){
						$.each(result.list, function(idx, code){
							code.selections = '{{#userrole='+code.id+'}} selected="selected" {{/userrole='+code.id+'}}';
						});
					}
					data.userGroup = result.list;
				}
			}
		});
    	
    	$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : 'MemberType,MemberStatus'
			},
			success : function(result){
				if(result.success){

					if(option){
						$.each(Object.keys(result.codes), function(idx, code){
							$.each(result.codes[code], function(_idx, _code){
								_code.selections = '{{#'+code+'='+_code.baseCd+'}} selected="selected" {{/'+code+'='+_code.baseCd+'}}';
							});
						});
					}
					
					data = $.extend(data, result.codes);
				}
			}
		});
    	
    	return data;
    },
    // permition 관련
    renderTree : function( event ){
    	var view = this;
    	var _this = $(event.target);
    	
        $.ajax({
	       	 url : modelUrl,
	       	 type: 'POST',
	       	 data : {
	       		"_method" : 'GET',
	       		"data" : JSON.stringify({id : _this.attr("target-code")})       		
	       	 }, 
	       	 success : function(result){
	       		 
	       		 if(result.success){
					var data = {};
					data.userId = _this.attr("target-code");
					data = $.extend(data, result.list);
					data.i18n = i18n;
					
					$('#user-permition-modal').html(Mustache.to_html(permition, data));  
					view.form_init('PUT', $('#user-permition-modal'));
					
					// userId를 Permition 리스트에 추가한다. 
					$.each(Object.keys(result.list), function(idx, id){						
						result.list[idx].userId = $('#userId').val();
						
						if(result.list[idx].nodes != null && result.list[idx].nodes != undefined){
							$.each(result.list[idx].nodes, function(_idx, _id){
								result.list[idx].nodes[_idx].userId = $('#userId').val();
							});
						}
					});
					
	       			view.init_treeview(result.list);
	       		 } 
	       	 }
         });
    },
    init_treeview : function(list){
    	var view = this;
    	$('#tree_view').treeview({
		  customCheckboxed: true,
		  isUnboundClickEv: true,
    	  data: list,         // data is not optional
    	  levels: 2, 
    	  i18n:i18n
    	});
    },
    treeview_submit : function(){

    	var submit_node = [];

    	$('#tree_view').find('li.node-tree_view').each(function(index, node){
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
    				alert(i18n.BO4046);
    				$('#user-permition-modal').modal('toggle');
    				$('#user-permition-modal').empty();
    			}else{
    				alert(i18n.BO4047);
    				$('#user-permition-modal').modal('toggle');
    				$('#user-permition-modal').empty();
    			}
    		}
    	});

    	return false;
    }
  });

  return ContentView;
});