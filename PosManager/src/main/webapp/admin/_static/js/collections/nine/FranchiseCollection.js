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
	'models/nine/FranchiseModel'
], function(BaseCollection, Model){
	var Collection = BaseCollection.extend({
		model: Model,
		url: './model/nine/Franchise'
	});

	return Collection;
});