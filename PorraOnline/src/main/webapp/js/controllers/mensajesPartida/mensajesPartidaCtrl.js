'use strict';

/* Controllers */
app.controller("mensajesPartidaCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Mensajes', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, Mensajes, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	$scope.alertas = Alertas.getAlertas();
	
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
	
	this.nuevoMensaje = function() {
		$window.location.href = '#/nuevoMensaje/' + $scope.partida.pa_id;
	};
	
	this.cargarPartida();
	
	return $scope.mensajesPartidaCtrl = this;
}]);