'use strict';

/* Controllers */
app.controller("administradoresCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Usuarios', 'User', 'Alertas', 
                                       function ($scope, $routeParams, $window, Partidas, Usuarios, User, Alertas) {
	
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
			Usuarios.administradores(id).then(
				function(value) {
					$scope.listaAdministradores = value;
				},
				this.mostrarAlertas);
		}
		$scope.mostrarSeccionPrincipal = true;
	};
	
	this.buscar = function() {
		if($scope.idPartida && !$scope.listaUsuarios) {
			Usuarios.findAll($scope.idPartida).then(
				function(value) {
					$scope.listaUsuarios = value;
				},
				this.mostrarAlertas);
		}
		$scope.mostrarSeccionPrincipal = false;
		$scope.mostrarUsuarios = true;
	};
	
	this.guardarAdministrador = function(elemento) {
		if(elemento.usu_username) {
			Usuarios.guardarAdministrador($scope.idPartida, elemento)
				.then(
					function() {
						Usuarios.administradores($scope.idPartida).then(
							function(value) {
								$scope.listaAdministradores = value;
								$scope.administradoresCtrl.volverSeccionPrincipal();
							},
							$scope.administradoresCtrl.mostrarAlertas);
					},
					this.mostrarAlertas);
		}
	};
	
	this.eliminarAdministrador = function(elemento) {
		if(elemento.usu_username) {
			Usuarios.eliminarAdministrador($scope.idPartida, elemento.usu_id)
				.then(
					function() {
						Usuarios.administradores($scope.idPartida).then(
							function(value) {
								$scope.listaAdministradores = value;
							},
							$scope.administradoresCtrl.mostrarAlertas);
					},
					this.mostrarAlertas);
		}
	};
	
	this.volverSeccionPrincipal = function() {
		$scope.mostrarSeccionPrincipal = true;
		$scope.mostrarUsuarios = false;
	};
	
	this.inicializar();
	
	return $scope.administradoresCtrl = this;
}]);