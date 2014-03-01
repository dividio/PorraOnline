'use strict';

/* Controllers */
app.controller("clasificacionPartidaCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Clasificacion', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, Clasificacion, User, Alertas) {
	
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
			Clasificacion.general(id).then(
				function(value) {
					$scope.clasificacion = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.cargarPartida();
	
	return $scope.clasificacionPartidaCtrl = this;
}]);