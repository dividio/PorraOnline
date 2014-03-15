'use strict';

/* Directives */


directives.directive('login', ['Login', 'User', 'Alertas', function(Login, User, Alertas) {
    return {
        restrict: "E",
        scope: {currentUser: '=user'},
        templateUrl: "views/directives/loginDirective.html",
        controller: ['$scope', '$http', function($scope, $http) {
        	this.signin = function() {
        		navigator.id.request();
        	};
        	
        	this.signout = function() {
        		navigator.id.logout();
        	};
        	
        	return $scope.loginCtrl = this;
        }],
        link: function(scope, iElement, iAttrs) {
        	if(navigator.id) {
	        	navigator.id.watch({
	        		loggedInUser: User.getUser().usu_email,
	        		onlogin: function(assertion) {
	        		    // A user has logged in! Here you need to:
	        		    // 1. Send the assertion to your backend for verification and to create a session.
	        		    // 2. Update your UI.
	        			
	        			if(!User.getUser().usu_email) {
	        				Login.login(assertion).then(
								function(value) {
									User.setUser(value.usuario);
									scope.currentUser = value.usuario;
								},
								Alertas.mostrarMensajes);
	        			}
	        		},
	        		onlogout: function() {
	        		    // A user has logged out! Here you need to:
	        		    // Tear down the user's session by redirecting the user or making a call to your backend.
	        		    
						Login.logout().then(
							function(value) {
								User.setUser({});
								scope.currentUser = {};
								//window.location.reload();
							},
							Alertas.mostrarMensajes);
	        		}
	        	});
	        }
        }
      };
  }]);
