'use strict';

/* Controllers */
app.controller("inicioCtrl", ['$scope','Noticias', 'User', 'Alertas', function ($scope, Noticias, User, Alertas) {
	
	$scope.user = User.getUser();
	
	$scope.alertas = Alertas.getAlertas();
	
	this.mostrarAlertas = function(value) {
		$scope.alertas = Alertas.mostrarAlertas(value);
	};

	
	this.buscar = function() {
		if(!$scope.listaNoticias) {
			Noticias.findAll().then(
				function(value) {
					$scope.listaNoticias = value;
				},
				this.mostrarAlertas);
		}
	};
	
	this.buscar();
	
	return $scope.inicioCtrl = this;
}]);