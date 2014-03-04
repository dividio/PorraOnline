'use strict';

/* Controllers */
app.controller("eventosCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Eventos', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, Eventos, User, Alertas) {
	
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
			Eventos.findAll(id).then(
				function(value) {
					$scope.listaEventos = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.nuevoEvento = function() {
		$window.location.href = '#/adminPartida/editarEvento/' + $scope.partida.pa_id;
	};
	
	this.inicializar();
	
	return $scope.eventosCtrl = this;
}]);