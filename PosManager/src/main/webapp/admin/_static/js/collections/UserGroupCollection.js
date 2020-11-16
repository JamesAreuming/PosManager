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
	'models/UserGroupModel'
], function(BaseCollection, Model){
	var Collection = BaseCollection.extend({
		model: Model,
		url: './model/UserGroup'
	});

	return Collection;
});