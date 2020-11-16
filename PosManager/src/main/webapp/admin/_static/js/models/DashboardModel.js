/*
 * Filename	: DashboardModel.js
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
	var DashboardModel = BaseModel.extend({
		urlRoot: './model/Dashboard'
	});

	return DashboardModel;
});