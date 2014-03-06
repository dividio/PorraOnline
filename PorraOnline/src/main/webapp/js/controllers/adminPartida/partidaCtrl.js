'use strict';

/* Controllers */
app.controller("partidaCtrl", ['$scope','$routeParams', '$window', '$filter', 'Partidas', 'User', 'Alertas', 
                                    function ($scope, $routeParams, $window, $filter, Partidas, User, Alertas) {
	
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
		}
	};
	
	this.guardar = function() {
		if($scope.partida) {
			if($scope.partida.pa_id) {
				Partidas.edit($scope.partida).then(
					function(value) {
						$scope.partida = value;
					},
					this.mostrarAlertas);
			} else {
				Partidas.create($scope.partida).then(
					function(value) {
						$scope.partida = value;
					},
					this.mostrarAlertas);
			}
		}
	};
	
	this.inicializar();
		
	return $scope.partidaCtrl = this;
}]);