/*
 * Filename	: BaseModel.js
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
	var convertYn2Bool = function(obj, stack) {
		for (var key in obj) {
			if (obj.hasOwnProperty(key)) {
				if (typeof obj[key] == "object") {
					convertYn2Bool(obj[key], stack + '.' + key);
				} else {
					if (key.endsWith('Yn')) {
						var boolKey = "is" + key.charAt(0).toUpperCase() + key.substring(1, key.length - 2);
						obj[boolKey] = ("Y" == obj[key]);
					}
				}
			}
		}
	};

	var processResponse = function(response) {
		if (response.success != undefined &&
			(response.bean != undefined || response.errors != undefined)) {
			if (response.success) {
				// ??Yn 필드의 경우 -> is?? 필드 추가
				convertYn2Bool(response.bean);

				//for (var key in response.bean) {
				//	if (response.bean.hasOwnProperty(key) && key.endsWith('Yn')) {
				//		var boolKey = key.charAt(0).toUpperCase() + key.substring(1, key.length - 2);
				//		response.bean[boolKey] = ("Y" == response[key]);
				//	}
				//}
				return response.bean;
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
						window.location = '/admin/login';
					});
				} else {
					toastr.warning(errMsg);
				}
			}
			return null;
		} else {
			return response;
		}
	};

	var BaseModel = Backbone.Model.extend({
		parse: function (response) {
			return processResponse(response);
		}, 
		fetch: function (options) {
//			var successCallback = (options != undefined && $.isFunction(options.success)) ? options.success : undefined;
//			Backbone.Model.prototype.fetch.call(this, {
//				data: (options != undefined ? options.data : undefined),
//				processData: (options != undefined ? true : undefined),
//				success: successCallback,
//				error: function (model, response) {
//					var errMsg = "서버와 연결 할 수 없습니다.";
//					toastr.warning(errMsg);
//				}
//			});
		}
	});

	return BaseModel;
});