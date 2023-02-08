angular.module('airbnbApp')
	.controller('hostDetailCtrl', ['hostsFactory', '$routeParams', '$location',
		function(hostsFactory, $routeParams, $location) {

			var hostDetailViewModel = this;
			hostDetailViewModel.host = {};
			hostDetailViewModel.hostsRelated = [];

			hostDetailViewModel.isServiceEmpty = false;
			hostDetailViewModel.isCatEmpty = false;
			hostDetailViewModel.isRedSocialEmpty = false;

			hostDetailViewModel.functions = {
				readHosts: function(codeCurrent) {
					hostsFactory.getHosts(codeCurrent, 0, $routeParams.ID)
						.then(function(response) {
							console.log("Reading all the hosts: ", response, " with code: ", codeCurrent, " and minLikes: 0");
							hostDetailViewModel.hostsRelated = response;
						}, function(response) {
							console.log("Error reading hosts");
						})
				},
				readHost: function(id) {
					hostsFactory.getHost(id)
						.then(function(response) {
							console.log("Reading host with id: ", id, " Response: ", response);
							hostDetailViewModel.host = response;

							hostDetailViewModel.isServiceEmpty = hostDetailViewModel.host.first.services == 0;
							hostDetailViewModel.isCatEmpty = hostDetailViewModel.host.third.length == 0;
							if(!hostDetailViewModel.host.first.redSocial){
								console.log("Red social vacio");
								hostDetailViewModel.isRedSocialEmpty = true;
							}
						}, function(response) {
							console.log("Error reading host");
							$location.path('/');
						})
				}
			}


			console.log("Entering hostDetailCtrl with $routeParams.ID=", $routeParams.ID);
			if ($routeParams.ID != undefined) {
				hostDetailViewModel.functions.readHost($routeParams.ID);
				// Se le pasa como parámetro el codigo 5, para devolver hosts relacionados segun el ID del host detallado
				hostDetailViewModel.functions.readHosts(5);
			}
		}]);