'use strict';

/* Controllers */
app.controller("editarCompetidorCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Competidores', 'User', 'Alertas', 
                                    function ($scope, $routeParams, $window, Partidas, Competidores, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	$scope.alertas = Alertas.getAlertas();
	
	$scope.competidor = {};
	
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

			var idCompetidor = $routeParams.idCompetidor;
			if(idCompetidor) {
				Competidores.find(idCompetidor).then(
					function(value) {
						$scope.competidor = value;
					},
					this.mostrarAlertas);
			} else {
				$scope.competidor = {};
			}
		}
	};
	
	this.guardar = function() {
		if($scope.competidor) {
			if($scope.competidor.co_id) {
				Competidores.edit($scope.competidor).then(
					function(value) {
						$scope.editarCompetidorCtrl.mostrarAlertas('Piloto guardado correctamente.');
					},
					this.mostrarAlertas);
			} else {
				Competidores.create($scope.idPartida, $scope.competidor).then(
					function(value) {
						$scope.competidor = value;
						$scope.editarCompetidorCtrl.mostrarAlertas('Piloto guardado correctamente.');
					},
					this.mostrarAlertas);
			}
		}
	};

	this.inicializar();
		
	return $scope.editarCompetidorCtrl = this;
}]);