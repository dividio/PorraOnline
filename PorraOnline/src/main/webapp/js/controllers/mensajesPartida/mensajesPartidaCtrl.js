'use strict';

/* Controllers */
app.controller("mensajesPartidaCtrl", ['$scope','$routeParams', 'Partidas', 'Mensajes', function ($scope, $routeParams, Partidas, Mensajes) {
	
	$scope.idPartida = $routeParams.idPartida;
	
	this.mostrarMensajes = function(mensajes) {
		$scope.mensajes = mensajes;
	};
	
	$scope.limpiarMensajes = function(mensajes) {
		$scope.mensajes = null;
	};
	
	this.cargarPartida = function() {
		var id = $scope.idPartida;
		if(id) {
			Partidas.find(id).then(
				function(value) {
					$scope.partida = value;
				},
				this.mostrarMensajes);
			Mensajes.mensajesPartida(id).then(
				function(value) {
					$scope.listaMensajes = value;
				},
				this.mostrarMensajes);
		}
	};
	
	this.cargarPartida();
	
	return $scope.mensajesPartidaCtrl = this;
}]);