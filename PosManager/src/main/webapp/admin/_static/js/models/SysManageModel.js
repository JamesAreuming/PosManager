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
	var SysManageModel = BaseModel.extend({
		urlRoot: 'model/sysmanage'
	});

	return SysManageModel;
});