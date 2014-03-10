'use strict';

/* Controllers */
app.controller("pronosticosCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Eventos', 'Pronosticos', 'Competidores', 'User', 'Alertas', 
                                    function ($scope, $routeParams, $window, Partidas, Eventos, Pronosticos, Competidores, User, Alertas) {
	
	$scope.user = User.getUser();
		
	$scope.idPartida = $routeParams.idPartida;
	
	$scope.alertas = Alertas.getAlertas();
	
	$scope.evento = {};
	
	$scope.fechaActual = new Date();
	
	$scope.eventoEditable = false;
	
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
		$scope.mostrarEventos = true;
	};
	
	this.volverSeccionPrincipal = function() {
		$scope.mostrarSeccionPrincipal = true;
		$scope.mostrarEventos = false;
		$scope.mostrarCompetidores = false;
	};
	
	this.cargarEventoBuscador = function(evento) {
		if(evento.ev_id) {
			this.cargarPronosticos(evento)
			.then(this.asignarPronosticosBonificaciones,
			      this.mostrarAlertas);
			this.volverSeccionPrincipal();
		}
	};
	
	this.mostrarCompetidores = function(bonificacion) {
		$scope.bonificacionSeleccionada = bonificacion;
		var lista = $scope.pronosticos[bonificacion.bo_id];
		if(!lista) {
			lista = [];
		}
		$scope.listaCompetidoresBonificacion = [];
		for(var i in $scope.listaCompetidores) {
			var elemento = $scope.listaCompetidores[i];
			var encontrado = false;
			var indice = 0;
			while(!encontrado && indice < lista.length) {
				encontrado = elemento.co_id === lista[indice].pr_co_id.co_id;
				indice++;
			}
			if(!encontrado) {
				$scope.listaCompetidoresBonificacion.push(elemento);
			}
		}
		$scope.mostrarSeccionPrincipal = false;
		$scope.mostrarCompetidores = true;
	};
	
	this.cargarCompetidorBuscador = function(competidor) {
		if(!$scope.pronosticos[$scope.bonificacionSeleccionada.bo_id]) {
			$scope.pronosticos[$scope.bonificacionSeleccionada.bo_id] = [];
		}
		var lista = $scope.pronosticos[$scope.bonificacionSeleccionada.bo_id];

		if(lista.length < $scope.bonificacionSeleccionada.bo_numero_posiciones) {
			var posicion = lista.length + 1;
			var pronostico = {pr_co_id: competidor, pr_posicion: posicion, pr_bo_id: $scope.bonificacionSeleccionada};
			lista.push(pronostico);
		} else {
			this.mostrarAlertas('Ya has asignado el máximo de pilotos.');
		}
		this.volverSeccionPrincipal();
	};
	
	this.cargarEvento = function() {
		var idEvento = $routeParams.idEvento;
		if(idEvento) {
			return Eventos.find(idEvento);
		} else {
			return Eventos.proximoEvento($scope.idPartida);
		}
	};
	
	this.cargarPronosticos = function(evento) {
		if(evento) {
			$scope.evento = evento;
			$scope.eventoEditable = evento.ev_fecha_inicio_pronosticos <= $scope.fechaActual && evento.ev_fecha_limite_pronosticos >= $scope.fechaActual;
			
			if($scope.user.usu_id) {
				return Pronosticos.findAll(evento.ev_id, $scope.user.usu_id);
			}
		} else {
			$scope.pronosticosCtrl.mostrarEventos();
		}
	};
	
	this.cargarCompetidores = function() {
		Competidores.findAll($scope.idPartida).then(
			function(value) {
				$scope.listaCompetidores = value;
			},
			this.mostrarAlertas);
	};
	
	this.asignarPronosticosBonificaciones = function(value) {
		$scope.pronosticos = new Object();
		for(var elemento in value) {
			var bonificacion = value[elemento].pr_bo_id;
			if(bonificacion) {
				if(!$scope.pronosticos[bonificacion.bo_id]) {
					$scope.pronosticos[bonificacion.bo_id] = [];
				}
				$scope.pronosticos[bonificacion.bo_id].push(value[elemento]);
			}
		}
	};
	
	this.bajarPronostico = function(pronostico) {
		var bonificacion = pronostico.pr_bo_id;
		if(pronostico.pr_posicion != bonificacion.bo_numero_posiciones) {
			var lista = $scope.pronosticos[bonificacion.bo_id];
			for(var elemento in lista) {
				var prAux = lista[elemento];
				if(prAux.pr_posicion === pronostico.pr_posicion + 1) {
					prAux.pr_posicion = prAux.pr_posicion - 1;
				}
			}
			pronostico.pr_posicion = pronostico.pr_posicion + 1;
		}
	};
	
	this.subirPronostico = function(pronostico) {
		if(pronostico.pr_posicion != 1) {
			var bonificacion = pronostico.pr_bo_id;
			var lista = $scope.pronosticos[bonificacion.bo_id];
			for(var elemento in lista) {
				var prAux = lista[elemento];
				if(prAux.pr_posicion === pronostico.pr_posicion - 1) {
					prAux.pr_posicion = prAux.pr_posicion + 1;
				}
			}
			pronostico.pr_posicion = pronostico.pr_posicion - 1;
		}
	};
	
	this.eliminarPronostico = function(pronostico) {
		var posicion = pronostico.pr_posicion;
		var bonificacion = pronostico.pr_bo_id;
		var lista = $scope.pronosticos[bonificacion.bo_id];
		var nuevaLista = [];
		for(var elemento in lista) {
			var prAux = lista[elemento];
			if(prAux != pronostico) {
				if(prAux.pr_posicion > posicion) {
					prAux.pr_posicion = prAux.pr_posicion - 1;
				}
				nuevaLista.push(prAux);
			}
		}
		$scope.pronosticos[bonificacion.bo_id] = nuevaLista;
	};
	
	this.inicializar = function() {
		this.volverSeccionPrincipal();
		
		var id = $scope.idPartida;
		
		if(id) {
			Partidas.find(id).then(
				function(value) {
					$scope.partida = value;
				},
				this.mostrarAlertas);
			
			this.cargarEvento()
				.then(this.cargarPronosticos)
				.then(this.asignarPronosticosBonificaciones,
					  this.mostrarAlertas);
			this.cargarCompetidores();
		}
	};
	
	this.guardar = function() {
		if($scope.evento) {
			var lista = [];
			for(var i in $scope.pronosticos) {
				for(var j in $scope.pronosticos[i]) {
					lista.push($scope.pronosticos[i][j]);
				}
			}
			Pronosticos.create($scope.evento.ev_id, lista).then(
				function(value) {
					$scope.pronosticosCtrl.mostrarAlertas('Pronosticos guardados correctamente.');
				},
				this.mostrarAlertas);
		}
	};

	this.inicializar();
		
	return $scope.pronosticosCtrl = this;
}]);