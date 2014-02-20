'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.

services.factory('Mensajes', ['$q', '$rootScope', function($q, $rootScope) {
	
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

			MensajesRS.find({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		},
		mensajesPartida: function(id) {
			var deferred = $q.defer();
			
			MensajesRS.mensajesPartida({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		findAll: function() {
			var deferred = $q.defer();
			
			MensajesRS.findAll({$callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		create: function(idPartida, mensaje) {
			var deferred = $q.defer();
			
			MensajesRS.create({id:idPartida, $entity:mensaje, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		edit: function(mensaje) {
			var deferred = $q.defer();
			
			MensajesRS.edit({$entity:mensaje, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		remove: function(id) {
			var deferred = $q.defer();
			
			MensajesRS.remove({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		}
	};
}]);