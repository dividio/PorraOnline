'use strict';


// Declare app level module which depends on filters, and services
var app = angular.module('porraOnline', ['porraOnline.filters', 'porraOnline.services', 'porraOnline.directives', 'ngRoute']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/inicio', {templateUrl: 'views/inicio.html', controller: "inicioCtrl"});
    $routeProvider.when('/listaPartidas', {templateUrl: 'views/listaPartidas/listaPartidas.html', controller: "listaPartidasCtrl"});
    $routeProvider.when('/mensajesPartida/:idPartida', {templateUrl: 'views/mensajesPartida/mensajesPartida.html', controller: "mensajesPartidaCtrl"});
    $routeProvider.otherwise({redirectTo: '/inicio'});
  }]);

var services = angular.module('porraOnline.services',['ngResource']);
var directives = angular.module('porraOnline.directives', []);