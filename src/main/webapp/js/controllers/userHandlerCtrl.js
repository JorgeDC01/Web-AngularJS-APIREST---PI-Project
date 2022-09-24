angular.module('airbnbApp')
	.controller('userHandlerCtrl', ['usersFactory', '$location', '$rootScope', '$window', function(usersFactory, $location, $rootScope, $window) {

		// Permite databinding entre las variables de userHandlerVM del template con los valores de userHandlerViewModel, enlazando ambas en el scope
		var userHandlerViewModel = this;

		userHandlerViewModel.user = {};
		userHandlerViewModel.functions = {
			// Esta función sirve para comprobar si la URL del navegador actual coincide con la del parámetro 'route'
			where: function(route) {
				return $location.path() == route;
			},
			readUser: function() {
				usersFactory.getUser()
					.then(function(response) {
						userHandlerViewModel.user = response;
						$rootScope.user = userHandlerViewModel.user;
						console.log("Getting user with id ", userHandlerViewModel.user.id, " Response: ", response);
					}, function(response) {
						console.log("Error reading user data");
					})
			},
			updateUser: function() {
				usersFactory.putUser(userHandlerViewModel.user)
					.then(function(response) {
						$rootScope.user = userHandlerViewModel.user;
						console.log("Updating user with id: ", userHandlerViewModel.user.id, userHandlerViewModel.user.username, " Response: ", response);
					}, function(response) {
						console.log("Error updating user");
					});
			},
			deleteUser: function() {
				usersFactory.deleteUser()
					.then(function(response) {
						console.log("Deleting user with id: ", userHandlerViewModel.user.id, "Response: ", response);
						delete $rootScope.user;
						$window.location.href = "https://localhost:8443/Proyecto_Airbnb/LogoutUserServlet.do";
					}, function(response) {
						console.log("Error deleting user");
					});
			},
			// Esta función tiene como objetivo redirigir la operación a realizar según la URL en la que nos encontremos
			userHandlerSwitcher: function() {

				if (userHandlerViewModel.functions.where('/editUser')) {
					console.log($location.path());
					userHandlerViewModel.functions.updateUser();
				}
				else if (userHandlerViewModel.functions.where('/deleteUser')) {
					console.log($location.path());
					userHandlerViewModel.functions.deleteUser();
				}
				else {
					console.log($location.path());
				}
				$location.path('/');
			},
			// Esta funcion simplemente sirve para devolver el objeto usuario, como si fuese el user de la sesión 
			user: function() {
				return $rootScope.user;
			}

		}

		console.log("Entering userHandlerCtrl");
		// Leemos el objeto usuario cada vez que usemos este controlador
		userHandlerViewModel.functions.readUser();
	}]);