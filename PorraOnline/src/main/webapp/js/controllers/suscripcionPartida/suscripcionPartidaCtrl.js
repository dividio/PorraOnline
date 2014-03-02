'use strict';

/* Controllers */
app.controller("suscripcionPartidaCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	$scope.alertas = Alertas.getAlertas();
	
	this.mostrarAlertas = function(value) {
		$scope.alertas = Alertas.mostrarAlertas(value);
	};
	
	this.inicializar = function() {
		var id = $scope.idPartida;
		if(id) {
			Partidas.find(id).then(
				function(value) {
					$scope.partida = value;
				},
				this.mostrarAlertas);
			Partidas.suscrito(id).then(
				function(value) {
					$scope.suscrito = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.suscripcion = function() {
		if($scope.suscrito) {
			if(confirm('Vas a cancelar tu suscripción a una partida ¿Estas seguro?')) {
				Partidas.suscribir($scope.partida.pa_id, false).then(
					function(value) {
						$window.location.href = '#/listaPartidas';
					},
					this.mostrarAlertas);
			}
		} else {
			if(confirm('Vas a suscribirte a una partida ¿Estas seguro?')) {
				Partidas.suscribir($scope.partida.pa_id, true).then(
					function(value) {
						$window.location.href = '#/mensajesPartida/' + $scope.partida.pa_id;
					},
					this.mostrarAlertas);
			}
		}
	};
	
	this.inicializar();
	
	return $scope.suscripcionPartidaCtrl = this;
}]);