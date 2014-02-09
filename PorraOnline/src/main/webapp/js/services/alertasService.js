'use strict';

services.factory('Alertas', [function() {
		
	var alertas = null;
	
	return {
		mostrarAlertas: function(value) {
    		alertas = value;
    		return alertas;
    	},
    	
    	limpiarAlertas: function() {
    		alertas = null;
    	},
    	
    	getAlertas: function() {
    		return alertas;
    	}
	};
}]);