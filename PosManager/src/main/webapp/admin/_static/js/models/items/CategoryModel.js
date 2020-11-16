/*
 * Filename	: BrandModel.js
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
	var CategoryModel = BaseModel.extend({
		urlRoot: './model/items/Category'
	});

	return CategoryModel;
});