'use strict';

services.factory('User', [function() {
		
	var user = {};
	
	return {
		getUser: function() {
			return user;
		},
		setUser: function(value) {
			user = value;
		}
	};
}]);