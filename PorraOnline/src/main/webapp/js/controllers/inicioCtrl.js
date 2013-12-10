'use strict';

/* Controllers */
app.controller("inicioCtrl", ['$scope','Noticias', function ($scope, Noticias) {
	this.mostrarMensajes = function(mensajes) {
		$scope.mensajes = mensajes;
	};
	
	this.limpiarMensajes = function(mensajes) {
		$scope.mensajes = null;
	};
	
	this.buscar = function() {
		if(!$scope.listaNoticias) {
			Noticias.findAll().then(
				function(value) {
					$scope.listaNoticias = value;
				},
				this.mostrarMensajes);
		}
	};
	
	this.buscar();
	
	return $scope.inicioCtrl = this;
}]);