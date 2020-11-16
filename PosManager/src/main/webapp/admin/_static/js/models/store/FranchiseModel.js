/*
 * Filename	: FranchiseModel.js
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
	var FranchiseModel = BaseModel.extend({
		urlRoot: './model/store/Franchise'
	});

	return FranchiseModel;
});