'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.

services.factory('Penalizaciones', ['$q', '$rootScope', function($q, $rootScope) {
	
	var callback = function(httpCode, xmlHttpRequest, value, deferred, $rootScope) {
		if(httpCode == '200' || httpCode == '201' || httpCode == '204') {
			deferred.resolve(value);
		} else if(httpCode == '500') {
			deferred.reject('Error interno del servidor.');
		} else {
			var mensaje = JSON.parse(xmlHttpRequest.responseText);
			deferred.reject(httpCode + ' ' + xmlHttpRequest.statusText + ':\n' + mensaje.message + '\n' + mensaje.reason);
		}
		$rootScope.$apply();
	};
	
	return {
		find: function(id) {
			var deferred = $q.defer();

			PenalizacionesRS.find({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		},
		findAll: function(idPartida) {
			var deferred = $q.defer();
			
			PenalizacionesRS.findAll({idPartida:idPartida, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		remove: function(id) {
			var deferred = $q.defer();
			
			PenalizacionesRS.remove({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		}
	};
}]);