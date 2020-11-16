/*
 * Filename	: storeManage.js
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
  
  'text!templates/store/storeManage.html',
  'text!templates/store/modal/storemodal.html',
  'text!templates/store/modal/beacon_modal.html',
  'text!templates/store/modal/posLicense_modal.html',
  'text!templates/store/modal/cctv_modal.html',
  'text!templates/store/modal/storePrinterModal.html',
  'text!templates/common/modal_form_alert.html'
  
  ,'jquery-prettyPhoto'

], function (Backbone, Mustache, Common, i18n, template, modalform, beaconModal, posModal, cctvModal, printerModal, modalAlert) {
  'use strict';

//  var store_search = {
//	draw : 'search'
//  };

  var datatables;

  var beacon_tables;

  var pos_tables;
  
  var cctv_tables;

  var brandId = -1;

  var _data = {};

  var beaconCnt = 1;

  var posCnt = 1;

  var staffCnt = 1;

  var cctvCnt = 1;
  
  var nvrCnt = 1;

  var map;

  var targetUrl = './model/store/Store';

  var filesTargetUrl = "./model/store/Files";

  var filesUpTargetUrl = "./model/store/FilesUpload";

  var geocoder;

  var file_num = 0;

  var ContentView = Backbone.View.extend({
    el: $('#main-wrapper'),
    events: {
    	'click #store-tables button.store-detail' : 'load_detail',
    	'click #toggle_map' : 'load_map',
    	'click #new-store' : 'modal_init',
    	'click #new-beacon' : 'init_beacon',
    	'click #new-pos' : 'init_pos',
    	'click #new-staff' : 'init_staff',
    	'click #new-nvr' : 'init_nvr',
    	'click #new-cctv' : 'init_cctv',
    	'click #new-printer' : 'addPrinter',
    	'click #del-row' : 'del_row',
    	'click #del-cctv-row' : 'del_cctv_row',    	
    	'click #cctvSave' : 'save_cctv_row',
    	'click #modal-beacon' : 'modal_beacon',
    	'click #modal-pos' : 'modal_pos',
    	'click #modal-cctv' : 'modal_cctv',
    	'click #modal-printer' : 'modalPrinter',
    	'click #pgUseTp' : 'changeSetPg',
    	'click .select-beacon' : 'select_beacon',
    	'click .select-pos' : 'select_pos',
		'click ul#store-modal-tabs > li': 'load_detail',
    	'click button#search' : 'search',
    	'click button#add-file' : 'add_file',
		'click button.del-file-tag' : 'del_file_tag',
		'click button.del-file' : 'del_file',
		'click .add-on-datepicker' : 'trigger_calender'
    },
    initialize: function (options) {
        this.trigger('remove-event');
        this.template = template;
        this.listenTo(this.model, 'sync', this.render);
  	  //this.mode = (options != undefined && options['mode'] != undefined ? options.mode : 'info');
    },
    render: function () {
      var view = this;
      var data = {};
      data.cd = view.loadBaseCode('SvcSt',false);
      data.i18n = i18n;
      
      data.frList = (view.model != undefined ? view.model.toJSON()[0].frList : {});
      data.brList = (view.model != undefined ? view.model.toJSON()[0].brList : {});
      data.stList = (view.model != undefined ? view.model.toJSON()[0].stList : {});
      
      _data = view.model.toJSON()[0].session.userInfo;
      data.userInfo = view.model.toJSON()[0].session.userInfo;
      
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

      view.selectedRollInit();
      view.datatablesInit();

      $("a[data-rel^='prettyPhoto']").prettyPhoto();
      return view;
    },
    selectedRollInit : function(){
//    	$('#svcSt').select2();
//        $('#brand_select').select2();

    	$('select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});
    	
        $('#franchise_select').select2().on('change', function(){
        	
        		_data.franId = $(this).val();
        		_data.data = JSON.stringify({ franId: _data.franId, brandId :null });
        		$.ajax({
        			url : './model/Brand',
        			data : {
        				data : JSON.stringify({ franId : _data.franId })
        			},
        			success : function(result){
        				if(result.success){
        					var defualt_array = [{id: '', brandNm : i18n.BO1032}];

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
        		});
        		datatables.draw();
        	});
        
        $('#brand_select').select2().on('change', function(){
  			_data.brandId = $(this).val();
  			_data.data = JSON.stringify({ franId: _data.franId, brandId : _data.brandId }); 
  			//_data.data = JSON.stringify({brandId : $(this).val()});
  			datatables.draw();
  		});
    },
    loadBaseCode : function(codeAlias,option){
    	var data = {};
		$.ajax({
			url : './model/Codes',
			method : 'POST',
			async: false,
			data : {
				'codes' : codeAlias
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

				} else {
		    		$('#store-modal').html('');
		    		$('#store-modal').modal('toggle');
				}
			}
		});
		
		return data;
    },
    datatablesInit : function(){
    	datatables = $('#store-tables').DataTable({
	  		"serverSide": true,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : targetUrl,
	        	"type": 'POST',
		    	"data" : function(data){
		    		data._method = "GET";
		    		data.data = JSON.stringify(_data);
		    		return $.extend(data, _data);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
			  { "data" : "id", "name" : "id" },
			  { "data" : "franId", "name" : "fran_id", "visible": false},
			  { "data" : "storeCd", "name" : "store_cd" },
			  { "data" : "storeNm", "name" : "store_nm" },
			  { "data" : "bizNo", "name" : "biz_no" },
			  /*{ "data" : "region" },*/
			  { "data" : "tel", "name" : "tel" },
			  { "data" : "svcStNm", "name" : "svc_st_nm", "className": "dt-body-center" },
			  { "data" : "id" }
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary btn-sm store-detail" data-toggle="modal" data-target=".bs-store-modal-lg" target-code="'+data+'" >'+i18n.BO0022+'</button>';
		        },
		    	"bSortable": false,
		        "targets": 7
		    } ],
	        "order": [[ 1, 'asc' ]],
		    "pageLength": 10,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false
		});
    },
    search : function(){
    	var view = this;
    	var $form = $('#rollSearchFrom');

    	_data= $.extend(_data, $form.serializeObject());

    	$.each(Object.keys(_data), function(idx, _key){
    		if ( _data[_key] == "" || _data[_key] === null || (typeof _data[_key] == "object" && $.isEmptyObject(_data[_key])) ) {
    			delete _data[_key];
    		}
    	});

    	if(datatables != undefined){
    		datatables.draw();
    	}else{
    		view.datatablesReload();
    	}    	
    },
    datatablesReload : function(){
    	var view = this;
    	if(datatables != undefined){
    		datatables.draw();
    	}
    },
    initSelection : function(franId, brandId){
    	$.ajax({
  			url : './model/Brand',
  			data : {
  				data : JSON.stringify({ franId : franId })
  			},
  			success : function(result){
  				if(result.success){
  					var defualt_array = [{id: '', brandNm : i18n.BO1032}];

  					$('#modal_brand_select').find('option').remove();

  					$.each(defualt_array.concat(result.list), function(idx, _cdata) {
  						$('#modal_brand_select').append('<select value="'+_cdata.id+'" '+ +'>'+_cdata.brandNm+'</select>');
  					});

  					$('#modal_brand_select').select2({
  						data : defualt_array.concat(result.list),
  						templateResult : function(state) {
  							return state.brandNm;
  						},
  						templateSelection : function(data, container) {
  						    return data.brandNm;
  						}
  					});

  					if(brandId != ''){
  						$("#brandId").val(brandId);
  			    		$('#modal_brand_select').select2('val', brandId);
  			    	}
  				}
  			}
  		});
    	
    	
    },
    modal_init : function(){
    	var view = this;
    	var data ={}; 
    	
    	if ($("#franchise_select option:selected").val() != '') {
    		_data.franId = $("#franchise_select option:selected").val(); 
    	}
    	 
    		view.mode = 'info';
    		var code = view.loadBaseCode('SvcSt,StoreSt,StoreTp',false);
    		data.mode = view.mode;
    		data.id = ''; 
    		data.franId = _data.franId;
    		data.brandId = _data.brandId; 
    		
    		data.i18n = i18n;
    		
    		$.ajax({
        		url : targetUrl,
        		data : {
        			data : JSON.stringify({
        				mode : data.mode,
        				lang : _lang,
        				timezone : Intl.DateTimeFormat().resolvedOptions().timeZone
        			})
        		},
        		success : function(response) {
        			if (response.success) {
        				data.tz = response.timezone; 
        				
        				$('[name=mode]').val(view.mode);
        	    		
        				$('#store-modal').html(Mustache.to_html(modalform, view.buildOptions($.extend(data, code))));
        	        	
        	    		view.form_init('POST', $('#store-modal'));
        	    		view.initSelection(_data.franId, _data.brandId);
        			}
        		}
    		});
    
    },
    modal_beacon : function(event){
		var data = {};
		data.i18n = i18n;
		data.modal = '412002';

    	$('#beacon-modal').html(Mustache.to_html(beaconModal, data));
    	
    	beacon_tables = $('#beacon-table').DataTable({
	  		"serverSide": true,
	    	"language": {
	            "url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        },
	        "ajax" : {
	        	"url" : './model/store/Beacon',
	        	"type": 'POST',
		    	"data" : function(data){
		    		data._method = 'GET';
		    		return $.extend(data, _data);
		    	},
		        "dataSrc" : 'list'
	        },
		    "columns" : [
              { "data" : "id", "name" : "id"},
			  { "data" : "uuid", "name" : "uuid"},
			  { "data" : "major", "name" : "major"},
			  { "data" : "minor", "name" : "minor"},
			  { "data" : "id"}
			],
		    "columnDefs":  [ {
		        "render": function ( data, type, row ) {
		            return '<button class="btn btn-primary select-beacon" target-code="'+data+'" target-name="'+row.uuid+
		            '" target-label="'+row.label+'" alt="'+event.currentTarget.getAttribute('alt')+'">'+i18n.BO0024+'</button>';
		        },
		        "bSortable": false,
		        "targets": 4
		    },
            {
                "targets": [ 0 ],
                "visible": false
            } ],
		    "pageLength": 5,
		    "lengthChange": false,
		    "processing": true,
		    "rowReorder": true,
		    "searching": false,
		    "rowId" : 'id'
		});
    },
    select_beacon : function(event){
    	var row = event.currentTarget.getAttribute('alt');
    	var _this = $(event.target);
    	var id = _this.attr('target-code');
    	var flag = true;

    	$('#beaconlist_table').find('input[type=checkbox]').each(function(idx, elem) {
    		if (id == $(this).val()) {
    			flag = false;
    			return flag;
    		}
    	});

    	if (flag) {
    		$('.issue-uuid'+row).val(_this.attr('target-name'));
    		$('#issue-id'+row).val(id);
    		$('#issue-chkId'+row).val(id);
    		$('#issue-label'+row).val(_this.attr('target-label'));

    		_this.closest('#beacon-modal').modal('toggle');
    	}
    	else {
    		alert(i18n.BO4023);
    	}
    },
    init_beacon : function(){
        var t = $('#beaconlist_table').DataTable();

        t.row.add( [
            '<input type="checkbox" name="ckl_'+beaconCnt+'" class="table-checkbox" id="issue-chkId'+beaconCnt+'"><input type="hidden" name="beaconId'+beaconCnt+'I'+'" id="issue-id'+beaconCnt+'" ><input type="hidden" name="beaconIU'+beaconCnt+'I'+'" value="I">',
            '<input type="text" name="bId'+beaconCnt+'I'+'" id="modal-beacon" class="issue-uuid'+beaconCnt+'" alt="'+beaconCnt+'" data-toggle="modal" data-target=".bs-beacon-modal-lg" readonly="readonly" data-validation="required" placeholder="'+i18n.BO4044+'">',
            '',
            '',
            '<input type="text" name="label'+beaconCnt+'I'+'" id="issue-label'+beaconCnt+'" >',
            ''
        ] ).draw( false );

        beaconCnt++;
    },
    modal_pos : function(event){
		var __data = {};
		__data.i18n = i18n;
		_data.modal = 'Y';
		
    	$('#pos-modal').html(Mustache.to_html(posModal, __data));
    	
    	pos_tables = $('#pos-table').DataTable({
    		"serverSide": true,
    		"language": {
    			"url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
    		},
    		"ajax" : {
    			"url" : './model/store/PosLicense',
    			"type": 'POST',
    			"data" : function(data){
    				data._method = 'GET';    				
    				return data;
    			},
    			"dataSrc" : 'list'
    		},
    		"columns" : [
	             { "data" : "id", "name" : "id"},
	             { "data" : "licenseKey", "name" : "license_Key" },
	             { "data" : "deviceTpNm", "name" : "device_Tp_Nm"},
	             { "data" : "beginEnd", "name" : "begin_End"},
	             { "data" : "id"}
             ],
             "columnDefs":  [ {
            	 "render": function ( data, type, row ) {
            		 return '<button class="btn btn-primary select-pos" target-code="'+data+'" target-licenseKey="'+row.licenseKey+'" target-beginEnd="'+row.beginEnd+
            		 '" target-deviceTp="'+row.deviceTp+'" target-deviceTpNm="'+row.deviceTpNm+'" alt="'+event.currentTarget.getAttribute('alt')+'">'+i18n.BO0024+'</button>';
            	 },
            	 "bSortable": false,
            	 "targets": 4
             },
             {
            	 "targets": 0,
            	 "visible": false
             } ],
             "pageLength": 5,
             "lengthChange": false,
             "processing": true,
             "rowReorder": true,
             "searching": false,
             "rowId" : 'id'
    	});
    },
    modal_cctv : function(event){
    	var data = {};
    	data.nvrId = $(event.target).attr('target-code');
    	
    	$.ajax({
    		url : './model/store/Cctv',
    		type: 'POST',
    		data : {
    			"_method" : 'GET',
    			"data" : JSON.stringify(data)
    		},	
        		
    		success : function(response){
    			if (response.success) {
    				var data = {};
    				data.i18n = i18n;
    				data.nvrId = $(event.target).attr('target-code');
    				
    				data.brandId = $("#brandId").val();
    				data.storeId = $("#storeId").val();
    				data.list = response.list;
    				
    				cctvCnt = data.list.length;
    				cctvCnt++;
    				
    				$('#cctv-modal').html(Mustache.to_html(cctvModal, data));
    				
    				$('#cctv-table').DataTable({
    					"language": {
	        				"url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
	        			},
	        			"columnDefs":  [ {
	                   	 "bSortable": false,
	                   	 "targets": 0
	                    }],
	        			"lengthChange": false,
	        			"searching": false,
	        	        "paging": false,
	        		});
		        }
    		}
    	});
    },
    del_cctv_row : function(){
    	var view = this;
		var table = $('#cctv-table').DataTable();
    	var checkList = $('#cctv-table').find('.table-checkbox:checked');
    	
		if (checkList.length == 0) {
			alert(i18n.BO4025);
			return;
		}

		var flag = false;

        if (confirm(i18n.BO4015)) {
			checkList.each(function(idx, elem) {
				var id = $(elem).val().split('/')[0];				

				if (id > 0) {
		      		$.ajax({
		      			url : './model/store/Cctv',
		        		type: 'POST',
		        		async: false,
		      			data : {
		        			"_method" : 'DELETE',
		        			"data" : JSON.stringify({id : id})
		      			},
		      			success : function(result) {
		      				if (result.success) {
		      					flag = true;
		      				}
		      			}
		      		});
				}
				table.row($(elem).closest('tr')).remove().draw(false);
			});
			if (flag) alert(i18n.BO4029);
        }
    },
    save_cctv_row : function(){
    	var view = this;
		var table = $('#cctv-table').DataTable();
    	var checkList = $('#cctv-table').find('.table-checkbox:checked');
    	
		if (checkList.length == 0) {
			alert(i18n.BO4025);
			return;
		}

		var flag = false;

		var params = $('#cctvList').serializeObject();
    	
    	$.ajax({
    		url : './model/store/Cctv',
    		type: 'POST',
    		data : {
    			"_method" : 'POST',
    			"data" : JSON.stringify(params)
    		},
    		success : function(response) {
    			if (response.success) {

    				alert("save success");
    				$('#cctv-modal').modal('toggle');
    				$('#cctv-modal').empty();
    			}
    			else {
    				alert("save fail");
    			}
    		}
    	}); 	
    },
    select_pos : function(event){
    	var row = event.currentTarget.getAttribute('alt');
    	var _this = $(event.target);
    	var id = _this.attr('target-code');
    	var flag = true;

    	$('#poslist_table').find('input[type=checkbox]').each(function(idx, elem) {
    		if (id == $(this).val()) {
    			flag = false;
    			return flag;
    		}
    	});

    	if (flag) {
    		$('.issue-licenseKey'+row).val(_this.attr('target-licenseKey'));
    		$('#issue-id'+row).val(id);
    		$('#issue-chkId'+row).val(id);
    		$('#issue-deviceTp'+row).val(_this.attr('target-deviceTp'));
    		$('#issue-deviceTpNm'+row).val(_this.attr('target-deviceTpNm'));
    		$('#issue-beginEnd'+row).val(_this.attr('target-beginEnd'));

    		_this.closest('#pos-modal').modal('toggle');
    	}
    	else {
    		alert(i18n.BO4024);
    	}
    },
    init_pos : function(){
    	var t = $('#poslist_table').DataTable();

    	t.row.add( [
            '<input type="checkbox" name="ckl'+posCnt+'" value="Y" class="table-checkbox" id="issue-chkId'+posCnt+'"><input type="hidden" name="pId'+posCnt+'I'+'" id="issue-id'+posCnt+'" ><input type="hidden" name="posIU'+posCnt+'I'+'" value="I">',
            '<input type="text" name="licenseKey'+posCnt+'I'+'" id="modal-pos" class="col-sm-14 issue-licenseKey'+posCnt+'" alt="'+posCnt+'" data-toggle="modal" data-target=".bs-pos-modal-lg" readonly="readonly" data-validation="required" placeholder="'+i18n.BO4044+'">',
            '<input type="text" class="col-sm-12" id="issue-deviceTpNm'+posCnt+'" readonly="readonly"><input type="hidden" id="issue-deviceTp'+posCnt+'" readonly="readonly">',
            '<input type="text" class="col-sm-12" id="issue-beginEnd'+posCnt+'" readonly="readonly">',
            '<input type="text" class="col-sm-10" name="posNo'+posCnt+'I'+'" >',
            '',
            ''
        ] ).draw( false );

    	posCnt++;
    },
    init_staff : function(){
    	var t = $('#stafflist_table').DataTable();

    	t.row.add( [
            '<input type="checkbox" name="ckl_'+staffCnt+'" value="Y" class="table-checkbox"><input type="hidden" name="staffId'+staffCnt+'I'+'"><input type="hidden" name="staffIU'+staffCnt+'I'+'" value="I">',
            '<input type="text" name="username'+staffCnt+'I'+'" data-validation="required">',
            '<input type="password" name="password'+staffCnt+'I'+'" data-validation="required">',
            '<input type="text" name="name'+staffCnt+'I'+'" data-validation="required">',
            '<input type="text" name="mb'+staffCnt+'I'+'">',
            ''
        ] ).draw( false );

    	staffCnt++;
    },
    init_nvr : function(){
    	var t = $('#nvrlist_table').DataTable();

    	t.row.add( [
    	            '<input type="checkbox" name="ckl_'+nvrCnt+'" value="Y" class="table-checkbox"><input type="hidden" name="nvrId'+nvrCnt+'I'+'"><input type="hidden" name="nvrIU'+nvrCnt+'I'+'" value="I">',
    	            '<input type="text" class="col-sm-14" name="host'+nvrCnt+'I'+'" data-validation="required">',
    	            '<input type="text" class="col-sm-10" name="port'+nvrCnt+'I'+'" data-validation="required">',
    	            '<input type="text" class="col-sm-10" name="username'+nvrCnt+'I'+'" data-validation="required">',
    	            '<input type="password" class="col-sm-10" name="password'+nvrCnt+'I'+'" data-validation="required">',
    	            '',
    	            ''
    	            ] ).draw( false );

    	nvrCnt++;
    },
    init_cctv : function(){
    	var t = $('#cctv-table').DataTable();
    	
    	t.row.add( [
					'<input type="checkbox" name="ckl_'+cctvCnt+'" value="Y" class="table-checkbox"><input type="hidden" name="cctvId'+cctvCnt+'I'+'"><input type="hidden" name="cctvIU'+cctvCnt+'I'+'" value="I">',
					'<input type="text" name="name'+cctvCnt+'I'+'" value="" data-validation="required">',
					'<input type="text" name="no'+cctvCnt+'I'+'" value="" data-validation="required">',
//					'<input type="text" name="ip'+cctvCnt+'I'+'" value="" data-validation="required">',
					'<input type="text" name="model'+cctvCnt+'I'+'" value="" data-validation="required">'
    	            ] ).draw( false );

    	cctvCnt++;
    },
    del_row : function(){
    	var view = this;
    	var mode = view.mode;
		var table = $('#'+mode+'list_table').DataTable();
    	var checkList = $('#'+mode+'list_table').find('.table-checkbox:checked');

		if (checkList.length == 0) {
			alert(i18n.BO4025);
			return;
		}

		var flag = false;

        if (confirm(i18n.BO4015)) {
			checkList.each(function(idx, elem) {
				var id = $(elem).val().split('/')[0];
				var beaconId = $(elem).val().split('/')[1];

				if (id > 0) {
		      		$.ajax({
		      			url : targetUrl,
		        		type: 'POST',
		        		async: false,
		      			data : {
		        			"_method" : 'DELETE',
		        			"data" : JSON.stringify({
		        				id : id,
		        				mode : mode,
		        				beaconId : beaconId
		        			})
		      			},
		      			success : function(result) {
		      				if (result.success) {
		      					flag = true;
		      				}
		      			}
		      		});
				}
				table.row($(elem).closest('tr')).remove().draw(false);
			});
			if (flag) alert(i18n.BO4029);
        }

    },
    selected : function(data) {

		for (var n = 0; n < 2; n++) {
			var date = new Date(2016,1,1,0,0,0);
			var order;
			var val;
			var str;
			var selected;
			var option = [];
			for (var i = 0; i < 48; i++) {
				val = date.format("HHmm");
				str = date.format("HH:mm");
				option.push($('<option>', {value: val, text: str}));
				date = new Date(date.getTime() + 30*60000);

				if (n == 0) {
					order = data.orderBegin;
					if (order == val) selected = val;
				}
				else {
					order = data.orderEnd;
					if (order == val) selected = val;
				}
			}
			if (n == 0) {
				$('[name=orderBegin]').append(option);
				if (order == selected) $('[name=orderBegin]').val(selected);
			}
			else {
				$('[name=orderEnd]').append(option);
				if (order == selected) $('[name=orderEnd]').val(selected);
			}

		}
    },
    add_file: function () {
		file_num+=1;
		$('.fallback').append('<input type="file" name="files" id="files'+file_num
		+'"/><button type="button" class="btn btn-xs btn-danger del-file-tag" id="del_'+file_num+'">- '+i18n.BO3065+'</button>');
    },
    del_file_tag: function (event) {
    	var id = event.currentTarget.getAttribute('id');
    	id = id.split('_')[1];
    	$('#files'+id).remove();
    	$('#del_'+id).remove();
    	file_num = file_num-1;
    },
    del_file: function (event) {
    	var id = event.currentTarget.getAttribute('id');

        if (confirm(i18n.BO4015)) {
      		$.ajax({
      			url : filesTargetUrl,
        		type: 'POST',
        		async: false,
      			data : {
        			'_method' : 'DELETE',
        			'data' : JSON.stringify({ id : id })
      			},
      			success : function(result) {
//      				if (result.success) $('.img_'+id).remove();
      				$.ajax({
			  			url : './model/store/Files',
			  			data : {
			  				data : JSON.stringify({ id : $('[name=storeId]').val() })
			  			},
						async: false,
			  			success : function(result){
			  				if (result.success) {
			  					var html = '';

								$.each(result.list, function(idx, val) {
									var imgNm = val.imageView;

									if (imgNm != '') {
										imgNm = imgNm.split('_')[1];
										
										if(result.list.length > 4){
											if(idx % 4 == 0){
    											html += '<div class="row">';
    										}
										}
										
										html += '<div class="col-xs-6 col-sm-4 col-md-3 image">';
										html += '<div class="thmb">';
										html += '<div class="thmb-prev">';
										html += '<img src='+val.image+' class="img-responsive" alt="" />';
										html += '</div>';
										html += '<h5 class="fm-title">'+imgNm+'</h5>';
										html += '<small class="text-muted">Added: '+val.created+'</small>';
										html += '<button type="button" class="btn btn-xs btn-danger del-file" id="'+val.id+'">x</button>'
										html += '</div>';
										html += '</div>';
										
										if(result.list.length > 4){
											if(idx % 4 == 3 || result.list.length -1 == idx){
												console.log(idx + " idx % 4 = " + idx % 4);
												html += '</div>';
											}
										}
										
//    									html += '<li class="img_'+val.id+'">';
//    									html += imgNm+'<button type="button" class="btn btn-xs btn-danger del-file" id="'+val.id+'">x</button>';
//    									html += '</li>';
									}
								});
								$('#fileList').html(html);
			  				}
			  			}
			  		});
      			}
      		});
        }
    },
    load_detail : function(event) {    	
    	
    	var view = this;
    	var _this = $(event.target);
    	var id = _this.attr("target-code");
    	
    	var temp_franId = datatables.row($(_this).closest('tr').get(0)).data().franId;
    	var temp_brandId = datatables.row($(_this).closest('tr').get(0)).data().brandId;
    	var temp_storeId = datatables.row($(_this).closest('tr').get(0)).data().id;
    	
    	if (id == undefined){
    		id = $('[name=storeId]').val();
    		temp_storeId = $('[name=storeId]').val();
    	}

    	var div = event.currentTarget.getAttribute('value') == null ? 'info' : event.currentTarget.getAttribute('value');
    	
    	view.mode = div;
    	$.ajax({
    		url : targetUrl,
    		data : {
    			data : JSON.stringify({
    				id : temp_storeId,
    				brandId : temp_brandId,
    				franId : temp_franId,
    				mode : div,
    				lang : _lang,
    				timezone : Intl.DateTimeFormat().resolvedOptions().timeZone
    			})
    		},
    		success : function(response){
    			 
    			
    			if (response.success) {
    				var num = 0;
    				var method = 'PUT';
    				var data = response.list;
    				data.mode = div;
    				
    				var userInfo = view.model.toJSON()[0].session.userInfo;
    				
    				data.franId = datatables.row($(_this).closest('tr').get(0)).data().franId;
    				data.brandId = datatables.row($(_this).closest('tr').get(0)).data().brandId;
    				
    				data = view.buildOptions(data);
    				 
    				data.i18n = i18n;
    				
    				if(div == 'info'){
    					num = 0;
    					var code = view.loadBaseCode('StoreSt,StoreTp,SvcSt',true); 
    					
    					data.svcSet = response.svcSet;
    				    data.tz = response.timezone;
		    	        var _template = Mustache.to_html(modalform, $.extend(data, code));
		    	        
    					console.log(data);
    					
	    				$('#store-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));
	    				
			        	view.initSelection(data.franId, data.brandId);
			        	
    				}else if (div == 'service') {
    					num = 1;
    					var code = view.loadBaseCode('SvcSt',true);
    					
    					data.storeSetId = data.id;
		    			method = 'PUT';
    		    		
    		    		var _template = Mustache.to_html(modalform, $.extend(data, code));
		    	        
	    				$('#store-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));
			        	
    		    		view.selected(data);

    		    		if (!data.allowStoreSet) {
    		    			if (method != 'POST') {
	    		    			$('div#service input').attr('disabled', true);
	    		    			$('[name=orderBegin]').attr('disabled', true);
	    		    			$('[name=orderEnd]').attr('disabled', true);
    		    			}
    		    		} 
    		    		
    		    		if (data.pgUseTp != '') {
    		    			$(".storePg").addClass("hidden");
    		    		} else {
    		    			$(".storePg").removeClass("hidden");
    		    		}			        	
    				  
    				} else if (div == 'beacon' || div == 'pos' || div == 'staff' || div == 'nvr' || div == 'printer') {
    					 
    					  
			        	if (div == 'pos') num = 2; 
			        	else if (div == 'staff') num = 3; 
			        	else if (div =='printer') num = 4;    	 
			        	
	    				data.list = response.list;
	    				data.id = id;
	    			    
    			    	data = Common.JC_format.handleData(data); 
    			    	
//      					if (userInfo != null && userInfo.type == "300003"){
      						data.isShowButton = true;
//      					}else{
//      						data.isShowButton = false;
//      					}
	    			    
	    			    var _template = Mustache.to_html(modalform, $.extend(data, code));
	    				$('#store-modal').html(Mustache.to_html(_template, view.buildOptions(response.list)));
	    				
		        		$('#storeId').val(id);
		        		$('#'+div+'list_table').DataTable({
		        			"language": {
		        				"url": "//cdn.datatables.net/plug-ins/1.10.11/i18n/English.json"
		        			},
		        			"lengthChange": false,
		        			"searching": false,
		        	        "paging": true,
		        		});
		        	}
    				
    				view.$el.find('ul#store-modal-tabs > li').removeClass('active');
		        	view.$el.find('div.tab-content > div').removeClass('active');
		        	
    				view.$el.find('ul#store-modal-tabs > li').eq(num).addClass('active');
		        	view.$el.find('div.tab-content > div').eq(num).addClass('active');
		        	
		        	view.form_init(method, $('#store-modal'));
		        	
		        	
		        	 $('[name=storeId]').val(id);
		        	 $("#storeId").val(id);
    			}
    		}
    	});
    },
    form_init : function(method, $modal){
    	var view = this;
    	var mode = view.mode;
    	$('#mode').val(view.mode);

    	$modal.find('select').select2({
    		width: '100%',
            minimumResultsForSearch: -1
    	});

		$.validate({
			validateOnBlur : false,
		    scrollToTopOnError : false,
		    showHelpOnFocus : false,
		    errorMessagePosition : $('.form-validatoin-alert-detail'),
		    addSuggestions : false,
		    onError : function($form) {
		    	var _target;
		    	if ('printer' == mode) {
		    		var _target = $("#modal-form-printer"); 
		    	} else {
		    		var _target = $("#modal-form");
		    	}

		    	if(_target.find('.alert').length == 0){
		    		_target.prepend(Mustache.to_html(modalAlert, {i18n:i18n}));
		    	}
	        },
	        onSuccess : function($form) {

	        	if (mode == 'introduce') {
	        		var tipArr = '';
	        		tipArr += '{';
	        		tipArr += $(':input[name^=beacon]:checked').length == 1 ? '"beacon": true, ' : '"beacon": false, ';
	        		tipArr += $(':input[name^=parking]:checked').length == 1 ? '"parking": true, ' : '"parking": false, ';
	        		tipArr += $(':input[name^=babyChair]:checked').length == 1 ? '"babyChair": true, ' : '"babyChair": false, ';
	        		tipArr += $(':input[name^=smallRoom]:checked').length == 1 ? '"smallRoom": true, ' : '"smallRoom": false, ';
	        		tipArr += $(':input[name^=party]:checked').length == 1 ? '"party": true, ' : '"party": false, ';
	        		tipArr += $(':input[name^=outdoor]:checked').length == 1 ? '"outdoor": true' : '"outdoor": false';
	        		tipArr += '}';
					$('#tip').val(tipArr);
				}

        		var params = $form.serializeObject();
        		params.mode = mode;
        		
        		params.franId = $('#franId').val();
        		params.brandId = $('#brandId').val();
        		params.storeId = $('[name=storeId]').val();

        		if (mode == 'beacon' || mode == 'pos' || mode == 'staff' || mode == 'nvr') method = 'POST';
	        	
	        	$.ajax({
	        		url : targetUrl,
	        		type: 'POST',
	        		data : {
	        			"_method" : method,
	        			"data" : JSON.stringify(params)
	        		},
	        		success : function(response) {
	        			
	        			
	        			console.log(response);
	        			
	        			if (response.success) {
	        				if (mode == 'introduce') {
								var _file = $form.find('#files').get(0);
								if (_file.files[0]) {
									var _data = new FormData();
									_data = JSON.stringify(params);

		        		            $('#storeForm').attr('action', filesUpTargetUrl);
		        		       		var options = {
		        		       			success : function(response) {
		        		        			if (!response.success) alert(i18n.BO4026);
		        		       			},
		        		       			type : 'POST'
		        		       		};
		        		       		$('#storeForm').ajaxSubmit(options);
								}
	        				} else if (mode == 'printer') {
	        					var data = response.data;
//	        					$("#printerName").val(data.name);
//	        					$("#printerType").val(data.printerType);
//	        					$("#printerCompanyCd").val(data.printerCompanyCd);
//	        					$("#posNo").val(data.posNo);
//	        					$("#isUsed").val(data.isUsed); 
	        					view.datatablesReload();
	        					datatables.draw();
	        				}
	        				$modal.modal('toggle');
	        				if(datatables != undefined || datatables != ''){
	        		    		datatables.draw();
	        		    	}else{
	        		    		view.datatablesReload();
	        		    	}  
	        			}
	        			else {
	        				if (mode == 'info' || mode == '') alert(i18n.BO4027+' : "'+response.errMsg+'"');
	        				return false;
	        			}
	        		}
	        	});
	        	return false;
	        }
		});

		$('.date-picker').daterangepicker({
			singleDatePicker: true,
			locale: {
	            format: 'YYYY-MM-DD',
			}
		});
    },
    buildOptions : function(object) {
        for (var i in object) {
        	object[i.toLowerCase() + '=' + object[i]] = 'true';
        }
        return object;
    },
	load_map : function() {
		geocoder = new google.maps.Geocoder();
		var latlng = new google.maps.LatLng(55.7558,37.6173);
		var address = document.getElementById('gpsId').value;
		var mapOptions = {
		  zoom: 17,
		  center: latlng
		  //mapTypeId: google.maps.MapTypeId.ROADMAP
		}

		if (address == '') {
			alert(i18n.BO4028);
			return false;
		}
		else {
			$('#target_map').removeClass('hidden');
			$('#map-canvas').html('');
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			this.codeAddress(address);
		}
	},
	codeAddress : function(address) {
		var x ='';
		var y ='';

		geocoder.geocode({'address': address}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);

		        x = results[0].geometry.location.lat().toFixed(4);
		        y = results[0].geometry.location.lng().toFixed(4);

		        var marker = new google.maps.Marker({
		            map: map,
		            position: results[0].geometry.location
		        });
		        $('#gpsId').val(x+','+y);
		        $('[name=latitude]').val(x);
		        $('[name=longitude]').val(y);
		    }
		    else {
		      alert(i18n.BO4030);
		    }
	    });
	},
	trigger_calender : function(event){
    	var _this = $(event.target);
    	var _cal = _this.closest('div').find('.date-picker');
    	_cal.trigger('click');    	
    },
    
    addPrinter : function () {
    	var view = this;
    	var code = view.loadBaseCode('ModelTp,PortTp', false);
    	var data = _data;
    	data.name = ''; // 회원이름과 프린터 이름의 속성명이 같아 초기화

    	data.i18n = i18n;
    	$.extend(code, data)
    	$('#printer-modal').html(Mustache.to_html(printerModal, code));
    	this.form_init('POST', $('#printer-modal'));
    },
    
    modalPrinter : function (event) {
    	var view = this;
    	var data = {};
    	data.franId = $("#franId").val();
    	data.brandId = $("#brandId").val();
    	data.storeId = $("#storeId").val();
    	data.printerId = $(event.target).attr('target-code');
    	data.mode = view.mode;
    	data.i18n = '';
    	$.ajax({
    		url : targetUrl,
    		method : 'POST',
    		data : {
    			'_method' : 'GET',
    			'data' : JSON.stringify(data)
    		},
    		success : function (response) {
//    	    	var data = view.loadBaseCode('ModelTp,PortTp', false);
//    	    	data.name = ''; // 회원이름과 프린터 이름의 속성명이 같아 초기화
//
//    	    	data.i18n = i18n;
//    	    	$.extend(data, response.list[0]);
//	    		
//    	        var _template = Mustache.to_html(modalform, data);
//    	    	$('#printer-modal').html(Mustache.to_html(printerModal, view.buildOptions(data)));
    	    	
    			var code = view.loadBaseCode('ModelTp,PortTp', true);
				var data = {};
				
				data = $.extend(data, response.list[0]);
				data.i18n = i18n;
				data.ModelTp = data.modelTp;
				data.PortTp = data.portTp;
				
				var _template = Mustache.to_html(printerModal, view.buildOptions($.extend(data, code)));
				 
		    	$('#printer-modal').html(Mustache.to_html(_template,data));
    	    	view.form_init('PUT', $('#printer-modal'));
    	    	
    	    	return false;
    		}
    	});
    },
    
    changeSetPg : function(event) {
    	var checkBrandPg = $(event.target).is(":checked");
    	if (checkBrandPg) {
    		$(".storePg").addClass("hidden");
    	} else {
    		$(".storePg").removeClass("hidden");
    	}
    }

  });

  return ContentView;
});