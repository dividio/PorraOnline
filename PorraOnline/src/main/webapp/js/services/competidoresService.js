'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.

services.factory('Competidores', ['$q', '$rootScope', function($q, $rootScope) {
	
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
		find: function(idCompetidor) {
			var deferred = $q.defer();

			CompetidoresRS.find({id:idCompetidor, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		},
		findAll: function(idPartida) {
			var deferred = $q.defer();
			
			CompetidoresRS.findAll({id:idPartida, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		create: function(idPartida, competidor) {
			var deferred = $q.defer();
			
			CompetidoresRS.create({id:idPartida, $entity:competidor, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		edit: function(competidor) {
			var deferred = $q.defer();
			
			CompetidoresRS.edit({$entity:competidor, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		remove: function(id) {
			var deferred = $q.defer();
			
			CompetidoresRS.remove({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		}
	};
}]);