/*
 * Filename	: BaseCollection.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
	'backbone',
	'toastr',
	'bootbox'
], function (Backbone, toastr, bootbox) {

	var getNo = function(pageNo, rowsPerPage, idx) {
		return ((pageNo - 1) * rowsPerPage) + idx + 1;
	}
	var BaseCollection = Backbone.Collection.extend({
		initialize: function() {
			//_.bindAll(this, 'searchOptions', 'count', 'rowsPerPage', 'pageNo');
			this.searchOptions = {};
			this.count = 0;
			this.rowsPerPage = 10;
			this.pageNo = 1;
		},
		parse: function (response) {
			if (response.success) {
//				var bean;
//				this.searchOptions = bean.searchOptions;
//				this.count = bean.count;
//				this.rowsPerPage = bean.rowsPerPage;
//				this.pageNo = bean.pageNo;
//				if ($.isArray(bean.items)) {
//					for (var i = 0; i < bean.items.length; i++) {
//						bean.items[i]["no"] = getNo(this.pageNo, this.rowsPerPage, i);
//					}
//				}
				return response	;
			} else {
				var errMsg = "서버와 연결 할 수 없습니다.";
				var needLogin = false;
				if ($.isArray(response.errors)) {
					for (var i = 0; i < response.errors.length; i++) {
						var err = response.errors[i];
						if ("001" == err.code) {
							needLogin = true;
							errMsg = err.message;
							break;
						} else {
							errMsg = err.message;
						}
					}
				}
				if (needLogin) {
					//alert(errMsg);
					bootbox.alert(errMsg, function(){
						window.location.hash = "";
						window.location = '/manager/login';
					});
				} else {
					toastr.warning(errMsg);
				}

				return [];
			}
		}, fetch: function (options) {
			var params = {};
			params.rowsPerPage = this.rowsPerPage;
			params.pageNo = this.pageNo;
			params.search = (options != undefined ? JSON.stringify(options.data) : {});
			params.codes = (options != undefined ? options.codes : '');
			Backbone.Collection.prototype.fetch.call(this, {
				data: params,
				processData: (options != undefined ? true : undefined),
				error: function (collection, response, options) {
					var errMsg = "서버와 연결 할 수 없습니다.";
					toastr.warning(errMsg);
				}
			});
		}
	});

	return BaseCollection;
});