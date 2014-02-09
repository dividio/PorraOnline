'use strict';

/* Controllers */
app.controller("inicioCtrl", ['$scope','Noticias', 'User', 'Alertas', function ($scope, Noticias, User, Alertas) {
	
	$scope.user = User.getUser();
	
	$scope.alertas = Alertas.getAlertas();

	
	this.buscar = function() {
		if(!$scope.listaNoticias) {
			Noticias.findAll().then(
				function(value) {
					$scope.listaNoticias = value;
				},
				Alertas.mostrarMensajes);
		}
	};
	
	this.buscar();
	
	return $scope.inicioCtrl = this;
}]);