/*
 * Filename	: UserModel.js
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
       ], 
       function (Backbone, BaseModel) {
			var UserModel = BaseModel.extend({
				urlRoot: './model/store/User'
			});
			
			/* initialize: function () {}; */
			
			return UserModel;
	   }
);