/*
 * Filename	: ManagerModel.js
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
	var ManagerModel = BaseModel.extend({
		urlRoot: '/admin/model/manager',
		idAttribute: 'svcKey'
	});

	return ManagerModel;
});