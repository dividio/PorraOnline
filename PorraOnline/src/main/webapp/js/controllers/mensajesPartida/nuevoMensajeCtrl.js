'use strict';

/* Controllers */
app.controller("nuevoMensajeCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Mensajes', 'User', 'Alertas', 
                                    function ($scope, $routeParams, $window, Partidas, Mensajes, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	$scope.alertas = Alertas.getAlertas();
	
	$scope.mensaje = {};
	
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
		if($scope.mensaje) {
			Mensajes.create($scope.idPartida, $scope.mensaje).then(
				function(value) {
					$scope.mensaje = value;
					$window.location.href = '#/mensajesPartida/' + $scope.partida.pa_id;
				},
				this.mostrarAlertas);
		}
	};

	this.inicializar();
		
	return $scope.nuevoMensajeCtrl = this;
}]);