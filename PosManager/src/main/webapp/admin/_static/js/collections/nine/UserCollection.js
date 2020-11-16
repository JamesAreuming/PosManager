/*
 * Filename	: HistoryCollection.js
 * Function	:
 * Comment 	:
 * History	:
 *
 * Version	: 1.0
 * Author   : 
 */

define([
	'collections/BaseCollection',
	'models/nine/UserModel'
], function(BaseCollection, Model){
	var Collection = BaseCollection.extend({
		model: Model,
		url: './model/nine/User'
	});

	return Collection;
});