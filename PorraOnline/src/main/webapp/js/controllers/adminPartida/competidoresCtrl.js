'use strict';

/* Controllers */
app.controller("competidoresCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Competidores', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, Competidores, User, Alertas) {
	
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
			Competidores.findAll(id).then(
				function(value) {
					$scope.listaCompetidores = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.nuevo = function() {
		$window.location.href = '#/adminPartida/editarCompetidor/' + $scope.partida.pa_id;
	};
	
	this.inicializar();
	
	return $scope.competidoresCtrl = this;
}]);