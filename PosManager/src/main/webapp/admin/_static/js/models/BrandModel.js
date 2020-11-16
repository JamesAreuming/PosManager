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
	var BrandModel = BaseModel.extend({
		urlRoot: './model/Brand'
	});

	return BrandModel;
});