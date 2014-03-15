'use strict';

/* Directives */


directives.directive('titulo',function(Alertas) {
    return {
        restrict: "E",
        replace: true,
        transclude: true,
        templateUrl: "views/directives/tituloDirective.html"
      };
  });
