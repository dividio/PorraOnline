'use strict';

/* Directives */


directives.directive('alertas', ['Alertas', function(Alertas) {
    return {
        restrict: "E",
        templateUrl: "views/directives/alertasDirective.html",
        scope: {alertas: '=alertas'},
        controller: ['$scope', '$http', function($scope, $http) {
        	$scope.alertas = Alertas.getAlertas();
        	
        	this.limpiarAlertas = function(alertas) {
        		Alertas.limpiarAlertas();
        		$scope.alertas = Alertas.getAlertas();
        	};
        	
        	return $scope.alertasCtrl = this;
        }],
        link: function(scope, iElement, iAttrs) {
        	scope.alertas = Alertas.getAlertas();
        }
      };
  }]);
