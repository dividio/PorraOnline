'use strict';

/* Services */

// Demonstrate how to register services
// In this case it is a simple value service.

services.factory('Usuarios', ['$q', '$rootScope', function($q, $rootScope) {
	
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

			UsuariosRS.find({id:id, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise;
		},
		findAll: function(idPartida) {
			var deferred = $q.defer();
			
			UsuariosRS.findAll({idPartida:idPartida, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		administradores: function(idPartida) {
			var deferred = $q.defer();
			
			UsuariosRS.administradores({idPartida:idPartida, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});
			
			return deferred.promise; 
		},
		guardarAdministrador: function(idPartida, usuario) {
			var deferred = $q.defer();
			
			UsuariosRS.guardarAdministrador({idPartida:idPartida, $entity:usuario, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		eliminarAdministrador: function(idPartida, idUsuario) {
			var deferred = $q.defer();
			
			UsuariosRS.eliminarAdministrador({idPartida:idPartida, idUsuario:idUsuario, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		create: function(usuario) {
			var deferred = $q.defer();
			
			UsuariosRS.create({$entity:usuario, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		},
		edit: function(usuario) {
			var deferred = $q.defer();
			
			UsuariosRS.edit({$entity:usuario, $callback:function(httpCode, xmlHttpRequest, value){
				callback(httpCode, xmlHttpRequest, value, deferred, $rootScope);
			}});

			return deferred.promise;
		}
	};
}]);