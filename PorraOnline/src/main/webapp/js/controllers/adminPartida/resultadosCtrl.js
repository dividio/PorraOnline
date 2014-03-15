'use strict';

/* Controllers */
app.controller("resultadosCtrl", ['$scope','$routeParams', '$window', 'Partidas', 'Eventos', 'Resultados', 'Competidores', 'Penalizaciones', 'User', 'Alertas', 
                                    function ($scope, $routeParams, $window, Partidas, Eventos, Resultados, Competidores, Penalizaciones, User, Alertas) {
	
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
		$scope.mostrarPenalizaciones = false;
	};
	
	this.cargarEventoBuscador = function(evento) {
		if(evento.ev_id) {
			this.cargarResultados(evento)
			.then(this.asignarResultadosBonificaciones,
			      this.mostrarAlertas);
			this.volverSeccionPrincipal();
		}
	};
	
	this.mostrarCompetidores = function(bonificacion) {
		$scope.bonificacionSeleccionada = bonificacion;
		var lista = $scope.resultados[bonificacion.bo_id];
		if(!lista) {
			lista = [];
		}
		$scope.listaCompetidoresBonificacion = [];
		for(var i in $scope.listaCompetidores) {
			var elemento = $scope.listaCompetidores[i];
			var encontrado = false;
			var indice = 0;
			while(!encontrado && indice < lista.length) {
				encontrado = elemento.co_id === lista[indice].re_co_id.co_id;
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
		if(!$scope.resultados[$scope.bonificacionSeleccionada.bo_id]) {
			$scope.resultados[$scope.bonificacionSeleccionada.bo_id] = [];
		}
		var lista = $scope.resultados[$scope.bonificacionSeleccionada.bo_id];

		var posicion = lista.length + 1;
		var resultado = {re_co_id: competidor, re_posicion: posicion, re_bo_id: $scope.bonificacionSeleccionada};
		lista.push(resultado);

		this.volverSeccionPrincipal();
	};
	
	this.mostrarPenalizaciones = function(resultado) {
		$scope.resultadoSeleccionado = resultado;
		if($scope.idPartida && !$scope.listaPenalizaciones) {
			Penalizaciones.findAll($scope.idPartida).then(
				function(value) {
					$scope.listaPenalizaciones = value;
				},
				this.mostrarAlertas);
		}
		$scope.mostrarSeccionPrincipal = false;
		$scope.mostrarPenalizaciones = true;
	};
	
	this.cargarPenalizacionBuscador = function(penalizacion) {
		if($scope.resultadoSeleccionado) {
			$scope.resultadoSeleccionado.re_pe_id = penalizacion;
		}
		
		this.volverSeccionPrincipal();
	};
	
	this.eliminarPenalizacion = function(resultado) {
		resultado.re_pe_id = null;
	};
	
	this.cargarEvento = function() {
		var idEvento = $routeParams.idEvento;
		if(idEvento) {
			return Eventos.find(idEvento);
		} else {
			return Eventos.ultimoEvento($scope.idPartida);
		}
	};
	
	this.cargarResultados = function(evento) {
		if(evento) {
			$scope.evento = evento;
			$scope.eventoEditable = evento.ev_fecha_limite_pronosticos <= $scope.fechaActual;
			
			return Resultados.findAll(evento.ev_id);
		} else {
			$scope.resultadosCtrl.mostrarEventos();
		}
	};
	
	this.cargarCompetidores = function() {
		Competidores.findAll($scope.idPartida).then(
			function(value) {
				$scope.listaCompetidores = value;
			},
			this.mostrarAlertas);
	};
	
	this.asignarResultadosBonificaciones = function(value) {
		$scope.resultados = new Object();
		for(var elemento in value) {
			var bonificacion = value[elemento].re_bo_id;
			if(bonificacion) {
				if(!$scope.resultados[bonificacion.bo_id]) {
					$scope.resultados[bonificacion.bo_id] = [];
				}
				$scope.resultados[bonificacion.bo_id].push(value[elemento]);
			}
		}
	};
	
	this.bajarResultado = function(resultado) {
		var bonificacion = resultado.re_bo_id;
		if(resultado.re_posicion != bonificacion.bo_numero_posiciones) {
			var lista = $scope.resultados[bonificacion.bo_id];
			for(var elemento in lista) {
				var reAux = lista[elemento];
				if(reAux.re_posicion === resultado.re_posicion + 1) {
					reAux.re_posicion = reAux.re_posicion - 1;
				}
			}
			resultado.re_posicion = resultado.re_posicion + 1;
		}
	};
	
	this.subirResultado = function(resultado) {
		if(resultado.re_posicion != 1) {
			var bonificacion = resultado.re_bo_id;
			var lista = $scope.resultados[bonificacion.bo_id];
			for(var elemento in lista) {
				var reAux = lista[elemento];
				if(reAux.re_posicion === resultado.re_posicion - 1) {
					reAux.re_posicion = reAux.re_posicion + 1;
				}
			}
			resultado.re_posicion = resultado.re_posicion - 1;
		}
	};
	
	this.eliminarResultado = function(resultado) {
		var posicion = resultado.re_posicion;
		var bonificacion = resultado.re_bo_id;
		var lista = $scope.resultados[bonificacion.bo_id];
		var nuevaLista = [];
		for(var elemento in lista) {
			var reAux = lista[elemento];
			if(reAux != resultado) {
				if(reAux.re_posicion > posicion) {
					reAux.re_posicion = reAux.re_posicion - 1;
				}
				nuevaLista.push(reAux);
			}
		}
		$scope.resultados[bonificacion.bo_id] = nuevaLista;
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
				.then(this.cargarResultados)
				.then(this.asignarResultadosBonificaciones,
					  this.mostrarAlertas);
			this.cargarCompetidores();
		}
	};
	
	this.guardar = function() {
		if($scope.evento) {
			var lista = [];
			for(var i in $scope.resultados) {
				for(var j in $scope.resultados[i]) {
					lista.push($scope.resultados[i][j]);
				}
			}
			Resultados.create($scope.evento.ev_id, lista).then(
				function(value) {
					$scope.resultadosCtrl.mostrarAlertas('Resultados guardados correctamente.');
				},
				this.mostrarAlertas);
		}
	};

	this.inicializar();
		
	return $scope.resultadosCtrl = this;
}]);