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
	'models/store/StoreModel'
], function(BaseCollection, Model){
	var Collection = BaseCollection.extend({
		model: Model,
		url: './model/store/Store'
	});

	return Collection;
});