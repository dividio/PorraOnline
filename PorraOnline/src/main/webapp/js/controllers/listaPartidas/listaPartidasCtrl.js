'use strict';

/* Controllers */
app.controller("listaPartidasCtrl", ['$scope','Partidas', function ($scope, Partidas) {
	this.mostrarMensajes = function(mensajes) {
		$scope.mensajes = mensajes;
	};
	
	$scope.limpiarMensajes = function(mensajes) {
		$scope.mensajes = null;
	};
	
	this.partidasSuscritas = function() {
		if(!$scope.listaPartidas) {
			Partidas.partidasSuscritas().then(
				function(value) {
					$scope.listaPartidas = value;
				},
				this.mostrarMensajes);
		}
	};
	
	this.partidasSuscritas();
	
	return $scope.listaPartidasCtrl = this;
}]);