'use strict';


// Declare app level module which depends on filters, and services
var app = angular.module('porraOnline', ['porraOnline.filters', 'porraOnline.services', 'porraOnline.directives', 'ngRoute']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/inicio', {templateUrl: 'views/inicio.html', controller: "inicioCtrl"});
    $routeProvider.otherwise({redirectTo: '/inicio'});
  }]);
