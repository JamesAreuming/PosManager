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
	var StoreModel = BaseModel.extend({
		urlRoot: './model/nine/Store'
	});

	return StoreModel;
});