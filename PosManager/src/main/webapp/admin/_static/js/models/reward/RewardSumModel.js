/*
 * Filename	: RewardSumModel.js
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
	var RewardSumModel = BaseModel.extend({
		urlRoot: './model/reward/RewardSum'
	});

	return RewardSumModel;
});