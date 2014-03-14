'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.

services.factory('Resultados', ['$q', '$rootScope', function($q, $rootScope) {
	
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
		findAll: function(idEvento) {
			var deferred = $q.defer();
			
			ResultadosRS.findAll({idEvento:idEvento, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		create: function(idEvento, listaResultados) {
			var deferred = $q.defer();
			
			ResultadosRS.create({idEvento:idEvento, $entity:listaResultados, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		}
	};
}]);