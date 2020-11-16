/*
 * Filename	: PlatformModel.js
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
	var PlatformModel = BaseModel.extend({
		urlRoot: '/admin/common/model/platform',
		idAttribute: 'platKey'
	});

	return PlatformModel;
});