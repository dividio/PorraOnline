'use strict';

/* Controllers */
app.controller("listaPartidasCtrl", ['$scope','Partidas', 'User', 'Alertas', function ($scope, Partidas, User, Alertas) {
	
	$scope.user = User.getUser();
	
	$scope.alertas = Alertas.getAlertas();
	
	$scope.filtro = {suscritas: true, enCurso: false};
	
	this.mostrarAlertas = function(value) {
		$scope.alertas = Alertas.mostrarAlertas(value);
	};
	
	this.buscar = function(filtro) {
		Partidas.findAll(filtro).then(
			function(value) {
				$scope.listaPartidas = value;
			},
			this.mostrarAlertas);
	};
	
	this.inicializar = function() {
		if(!$scope.listaPartidas) {
			this.buscar($scope.filtro);
		}
	};
	
	this.inicializar();
	
	return $scope.listaPartidasCtrl = this;
}]);