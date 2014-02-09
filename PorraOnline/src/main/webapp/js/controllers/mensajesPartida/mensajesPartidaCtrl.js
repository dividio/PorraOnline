'use strict';

/* Controllers */
app.controller("mensajesPartidaCtrl", ['$scope','$routeParams', 'Partidas', 'Mensajes', 'User', 'Alertas', function ($scope, $routeParams, Partidas, Mensajes, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	this.mostrarAlertas = function(value) {
		$scope.alertas = Alertas.mostrarAlertas(value);
	};
	
	this.cargarPartida = function() {
		var id = $scope.idPartida;
		if(id) {
			Partidas.find(id).then(
				function(value) {
					$scope.partida = value;
				},
				this.mostrarAlertas);
			Mensajes.mensajesPartida(id).then(
				function(value) {
					$scope.listaMensajes = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.cargarPartida();
	
	return $scope.mensajesPartidaCtrl = this;
}]);