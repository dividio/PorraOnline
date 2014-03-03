'use strict';


// Declare app level module which depends on filters, and services
var app = angular.module('porraOnline', ['porraOnline.filters', 'porraOnline.services', 'porraOnline.directives', 'ngRoute']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/inicio', {templateUrl: 'views/inicio.html', controller: "inicioCtrl"});
    $routeProvider.when('/listaPartidas', {templateUrl: 'views/listaPartidas/listaPartidas.html', controller: "listaPartidasCtrl"});
    $routeProvider.when('/mensajesPartida/:idPartida', {templateUrl: 'views/mensajesPartida/mensajesPartida.html', controller: "mensajesPartidaCtrl"});
    $routeProvider.when('/nuevoMensaje/:idPartida', {templateUrl: 'views/mensajesPartida/nuevoMensaje.html', controller: "nuevoMensajeCtrl"});
    $routeProvider.when('/clasificacionPartida/:idPartida', {templateUrl: 'views/clasificacionPartida/clasificacionPartida.html', controller: "clasificacionPartidaCtrl"});
    $routeProvider.when('/clasificacionEvento/:idPartida/:idEvento', {templateUrl: 'views/clasificacionEvento/clasificacionEvento.html', controller: "clasificacionEventoCtrl"});
    $routeProvider.when('/clasificacionEvento/:idPartida', {templateUrl: 'views/clasificacionEvento/clasificacionEvento.html', controller: "clasificacionEventoCtrl"});
    $routeProvider.when('/suscripcionPartida/:idPartida', {templateUrl: 'views/suscripcionPartida/suscripcionPartida.html', controller: "suscripcionPartidaCtrl"});
    $routeProvider.when('/adminPartida/:idPartida', {templateUrl: 'views/adminPartida/adminPartida.html', controller: "adminPartidaCtrl"});
    $routeProvider.when('/adminPartida/partida/:idPartida', {templateUrl: 'views/adminPartida/partida.html', controller: "partidaCtrl"});
    $routeProvider.otherwise({redirectTo: '/inicio'});
  }]);

var services = angular.module('porraOnline.services',['ngResource']);
var directives = angular.module('porraOnline.directives', []);