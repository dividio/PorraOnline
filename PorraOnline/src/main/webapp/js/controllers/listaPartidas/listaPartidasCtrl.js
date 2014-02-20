'use strict';

/* Controllers */
app.controller("listaPartidasCtrl", ['$scope','Partidas', 'User', 'Alertas', function ($scope, Partidas, User, Alertas) {
	
	$scope.user = User.getUser();
	
	$scope.alertas = Alertas.getAlertas();
	
	this.mostrarAlertas = function(value) {
		$scope.alertas = Alertas.mostrarAlertas(value);
	};
	
	this.partidasSuscritas = function() {
		if(!$scope.listaPartidas) {
			Partidas.partidasSuscritas().then(
				function(value) {
					$scope.listaPartidas = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.partidasSuscritas();
	
	return $scope.listaPartidasCtrl = this;
}]);