/*
 * Filename	: CodeModel.js
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

define([
	'backbone',
	'models/BaseModel'
], function (Backbone, BaseModel) {
	var CODE = 0;
	var NAME = 1;
	var DESC = 2;
	var ALIAS = 3;

	var CodeModel = BaseModel.extend({
		urlRoot: '/common/model/code',
		idAttribute: 'codeAlias',
		getCode: function (codeDtlNo) {
			var list = this.attributes;
			var matched = null;
			$.each(list, function(index, value){
				if (!isNaN(index) && parseInt(index) == index
					&& $.isArray(value)) {
					var code = {};
					code.codeDtlNo = value[0];
					code.codeDtlNm = value[1];
					code.codeDtlDesc = value[3];
					code.codeDtlAlias = value[4];

					if (codeDtlNo == code.codeDtlNo) {
						matched = code;
						return false;
					}
				}
			});
			return matched;
		}
	});

	return CodeModel;
});