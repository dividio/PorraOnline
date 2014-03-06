'use strict';

/* Controllers */
app.controller("clasificacionEventoCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Clasificacion', 'Eventos', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, Clasificacion, Eventos, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	$scope.idEvento = $routeParams.idEvento;
	
	$scope.alertas = Alertas.getAlertas();
	
	$scope.mostrarSeccionPrincipal = true;
	
	$scope.fechaActual = new Date();
	
	this.mostrarAlertas = function(value) {
		$scope.alertas = Alertas.mostrarAlertas(value);
	};
	
	this.mostrarEventos = function() {
		if($scope.idPartida && !$scope.listaEventos) {
			Eventos.findAll($scope.idPartida).then(
				function(value) {
					$scope.listaEventos = value;
				},
				this.mostrarAlertas);
		}
		$scope.mostrarSeccionPrincipal = false;
	};
	
	this.cancelarSeleccion = function() {
		$scope.mostrarSeccionPrincipal = true;
	};
	
	this.cargarEvento = function(evento) {
		if(evento.ev_id) {
			$scope.evento = evento;
			Clasificacion.evento($scope.idPartida, evento.ev_id).then(
				function(value) {
					$scope.clasificacion = value;
					$scope.mostrarSeccionPrincipal = true;
				},
				this.mostrarAlertas);
		}
	};
	
	this.cargarPartida = function() {
		var idPartida = $scope.idPartida;
		if(idPartida) {
			Partidas.find(idPartida).then(
				function(value) {
					$scope.partida = value;
				},
				this.mostrarAlertas);
			
			var idEvento = $scope.idEvento;
			
			if(idEvento) {
				Eventos.find(idEvento).then(
					function(value) {
						$scope.clasificacionEventoCtrl.cargarEvento(value);
					},
					this.mostrarAlertas);
			} else {
				 Eventos.ultimoEvento(idPartida).then(
					function(value) {
						$scope.clasificacionEventoCtrl.cargarEvento(value);
					},
					this.mostrarAlertas);
			};
		};
	};
	
	this.cargarPartida();
	
	return $scope.clasificacionEventoCtrl = this;
}]);