'use strict';

/* Controllers */
app.controller("adminPartidaCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'User', 'Alertas', 
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
			Partidas.esAdmin(id).then(
				function(value) {
					$scope.esAdmin = value;
					if(!value) {
						$scope.adminPartidaCtrl.mostrarAlertas('No tienes permisos para administrar esta partida.');
					}
				},
				this.mostrarAlertas);
		}
	};
	
	this.inicializar();
	
	return $scope.adminPartidaCtrl = this;
}]);