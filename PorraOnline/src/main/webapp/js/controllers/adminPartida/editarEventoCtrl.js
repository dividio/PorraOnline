'use strict';

/* Controllers */
app.controller("editarEventoCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Eventos', 'User', 'Alertas', 
                                    function ($scope, $routeParams, $window, Partidas, Eventos, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	$scope.alertas = Alertas.getAlertas();
	
	$scope.evento = {};
	
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

			var idEvento = $routeParams.idEvento;
			if(idEvento) {
				Eventos.find(idEvento).then(
					function(value) {
						$scope.evento = value;
					},
					this.mostrarAlertas);
			} else {
				$scope.evento = {};
			}
		}
	};
	
	this.guardar = function() {
		if($scope.evento) {
			if($scope.evento.ev_id) {
				Eventos.edit($scope.evento).then(
					function(value) {
						$scope.editarEventoCtrl.mostrarAlertas('Evento guardado correctamente.');
					},
					this.mostrarAlertas);
			} else {
				Eventos.create($scope.idPartida, $scope.evento).then(
					function(value) {
						$scope.evento = value;
						$scope.editarEventoCtrl.mostrarAlertas('Evento guardado correctamente.');
					},
					this.mostrarAlertas);
			}
		}
	};

	this.inicializar();
		
	return $scope.editarEventoCtrl = this;
}]);