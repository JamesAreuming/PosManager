/*
 * Filename	: UserGroupModel.js
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
	var UserGroupModel = BaseModel.extend({
		urlRoot: './model/UserGroup'
	});

	return UserGroupModel;
});