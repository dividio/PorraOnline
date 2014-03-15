'use strict';

/* Directives */


directives.directive('cabecera', function() {
    return {
        restrict: "E",
        replace: true,
        transclude: true,
        templateUrl: "views/directives/cabeceraDirective.html"
    };
  });
