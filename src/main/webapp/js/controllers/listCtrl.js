angular.module('airbnbApp')
	.controller('listCtrl', ['hostsFactory', '$rootScope', function(hostsFactory, $rootScope) {

		var listViewModel = this;
		// Esta variable almacena el código de filtrado que se mandará en la request a la API REST. Por defecto es 0 = TODO HOST
		listViewModel.codeCurrent = 0;
		// Esta variable almacena el minimo de likes con el que se filtrarán los alojamientos
		listViewModel.minLikesCurrent = 0;

		listViewModel.hosts = [];
		listViewModel.functions = {
			readHosts: function(codeCurrent) {
				hostsFactory.getHosts(codeCurrent, listViewModel.minLikesCurrent, 0)
					.then(function(response) {
						console.log("Reading all the hosts: ", response, " with code: ", codeCurrent, " and minLikes: ", listViewModel.minLikesCurrent);
						listViewModel.hosts = response;
					}, function(response) {
						console.log("Error reading hosts");
					})
				listViewModel.minLikesCurrent = 0;
			},
			updateHost: function(id) {
				hostsFactory.LikeNotLikeHost(id)
					.then(function(response) {
						console.log("Updating host with id: ", id, " Response: ", response);
					}, function(response) {
						console.log("Error updating host");
					})
			},
			// Esta funcion simplemente sirve para devolver el objeto usuario, como si fuese el user de la sesión 
			user: function() {
				return $rootScope.user;
			},
			// Esta funcion actualiza si el host tiene un like por el usuario de la session y envia request para actualizarlo en la API REST
			doLikeHost: function(host) {
				var index = listViewModel.hosts.indexOf(host);
				listViewModel.hosts[index].first.isFav = 'YES';
				listViewModel.hosts[index].first.likes++;
				listViewModel.functions.updateHost(host.first.id);

			},
			// Esta funcion actualiza el host, quitandole el like del usuario de la session y llama al servicio para comunicarse con la API REST
			deleteLikeHost: function(host) {
				var index = listViewModel.hosts.indexOf(host);
				listViewModel.hosts[index].first.isFav = 'NO';
				listViewModel.hosts[index].first.likes--;
				listViewModel.functions.updateHost(host.first.id);
			}
		}
		listViewModel.functions.readHosts(listViewModel.codeCurrent, listViewModel.minLikesCurrent);
	}])
