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
	'models/items/CategoryModel'
], function(BaseCollection, Model){
	var Collection = BaseCollection.extend({
		model: Model,
		url: './model/items/Category'
	});

	return Collection;
});