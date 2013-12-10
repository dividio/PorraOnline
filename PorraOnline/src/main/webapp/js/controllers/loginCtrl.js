'use strict';

/* Controllers */
app.controller("loginCtrl", ['$scope', 'Login', function ($scope, Login) {
	this.mostrarMensajes = function(mensajes) {
		$scope.mensajes = mensajes;
	};
	
	this.limpiarMensajes = function(mensajes) {
		$scope.mensajes = null;
	};
	
	this.signin = function() {
		navigator.id.request();
	};
	
	this.signout = function() {
		navigator.id.logout();
	};
	
	$scope.currentUser = {};

	navigator.id.watch({
	  loggedInEmail: $scope.currentUser.usu_email,
	  onlogin: function(assertion) {
	    // A user has logged in! Here you need to:
	    // 1. Send the assertion to your backend for verification and to create a session.
	    // 2. Update your UI.
//	    $.ajax({ /* <-- This example uses jQuery, but you can use whatever you'd like */
//	      type: 'POST',
//	      url: '/auth/login', // This is a URL on your website.
//	      data: {assertion: assertion},
//	      success: function(res, status, xhr) { window.location.reload(); },
//	      error: function(res, status, xhr) { alert("login failure" + res); }
//	    });
		  if(!$scope.currentUser.usu_email) {
			  Login.login(assertion).then(
				function(value) {
					$scope.currentUser = value.usuario;
				},
				this.mostrarMensajes);
		  }
	  },
	  onlogout: function() {
	    // A user has logged out! Here you need to:
	    // Tear down the user's session by redirecting the user or making a call to your backend.
//	    $.ajax({
//	      type: 'POST',
//	      url: '/auth/logout', // This is a URL on your website.
//	      success: function(res, status, xhr) { window.location.reload(); },
//	      error: function(res, status, xhr) { alert("logout failure" + res); }
//	    });
	    
		  Login.logout().then(
			function(value) {
				$scope.currentUser = {};
				//window.location.reload();
			},
			this.mostrarMensajes);
	  }
	});
	
	return $scope.loginCtrl = this;
}]);