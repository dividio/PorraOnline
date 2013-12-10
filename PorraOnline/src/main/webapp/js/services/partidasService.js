'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.

services.factory('Partidas', ['$q', '$rootScope', function($q, $rootScope) {
	
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

			PartidasRS.find({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		},
		partidasSuscritas: function() {
			var deferred = $q.defer();
			
			PartidasRS.partidasSuscritas({$callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		partidasNoSuscritas: function() {
			var deferred = $q.defer();
			
			PartidasRS.partidasNoSuscritas({$callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		create: function(partida) {
			var deferred = $q.defer();
			
			PartidasRS.create({$entity:partida, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		edit: function(partida) {
			var deferred = $q.defer();
			
			PartidasRS.edit({$entity:partida, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		remove: function(id) {
			var deferred = $q.defer();
			
			PartidasRS.remove({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		}
	};
}]);