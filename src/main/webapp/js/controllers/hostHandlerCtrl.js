angular.module('airbnbApp')
	.controller('hostHandlerCtrl', ['hostsFactory', '$routeParams', '$location', '$rootScope',
		function(hostsFactory, $routeParams, $location, $rootScope) {

			var hostHandlerViewModel = this;
			hostHandlerViewModel.host = {};

			// Estas variables almacenan si están activadas las casillas de cada categoría
			hostHandlerViewModel.category0 = false;
			hostHandlerViewModel.category1 = false;
			hostHandlerViewModel.category2 = false;
			hostHandlerViewModel.category3 = false;
			hostHandlerViewModel.category4 = false;

			// Estas variables almacenan si están activadas las casillas de cada servicio
			hostHandlerViewModel.service0 = false;
			hostHandlerViewModel.service1 = false;
			hostHandlerViewModel.service2 = false;
			hostHandlerViewModel.service3 = false;
			hostHandlerViewModel.service4 = false;


			hostHandlerViewModel.functions = {
				where: function(route) {
					return $location.path() == route;
				},
				readHost: function(id) {
					hostsFactory.getHost(id)
						.then(function(response) {
							console.log("Reading host with id: ", id, " Response: ", response);
							hostHandlerViewModel.host = response;

							if (!hostHandlerViewModel.functions.where('/insertHost') && ($rootScope.user.id != hostHandlerViewModel.host.first.idu)) {
								$location.path('/');
							}

							// Obtenemos las categorias del host
							for (let i = 0; i < hostHandlerViewModel.host.third.length; i++) {
								switch (hostHandlerViewModel.host.third[i].idct) {
									case 0:
										hostHandlerViewModel.category0 = true;
										break;
									case 1:
										hostHandlerViewModel.category1 = true;
										break;
									case 2:
										hostHandlerViewModel.category2 = true;
										break;
									case 3:
										hostHandlerViewModel.category3 = true;
										break;
									case 4:
										hostHandlerViewModel.category4 = true;
										break;
								}
							}

							// Obtenemos los servicios del host
							var servicesVector = hostHandlerViewModel.host.first.services.split(',');
							console.log(servicesVector);

							for (let i = 0; i < servicesVector.length; i++) {
								switch (servicesVector[i]) {
									case "Cocina":
										hostHandlerViewModel.service0 = true;
										break;
									case "Limpieza":
										hostHandlerViewModel.service1 = true;
										break;
									case "Desayuno":
										hostHandlerViewModel.service2 = true;
										break;
									case "Wifi":
										hostHandlerViewModel.service3 = true;
										break;
									case "Lavadora":
										hostHandlerViewModel.service4 = true;
										break;
								}
							}
							console.log(hostHandlerViewModel.service0, " ", hostHandlerViewModel.service1, " ", hostHandlerViewModel.service2, " ", hostHandlerViewModel.service3, " ", hostHandlerViewModel.service4);

						}, function(response) {
							console.log("Error reading host");
							$location.path('/');
						})
				},
				updateHost: function() {

					// Insertamos las categorias elegidas en los checkbox en el host
					hostHandlerViewModel.host.third = [];
					if (hostHandlerViewModel.category0) {
						hostHandlerViewModel.host.third.push({ idct: 0, idh: hostHandlerViewModel.host.first.id });
					}
					if (hostHandlerViewModel.category1) {
						hostHandlerViewModel.host.third.push({ idct: 1, idh: hostHandlerViewModel.host.first.id });
					}
					if (hostHandlerViewModel.category2) {
						hostHandlerViewModel.host.third.push({ idct: 2, idh: hostHandlerViewModel.host.first.id });
					}
					if (hostHandlerViewModel.category3) {
						hostHandlerViewModel.host.third.push({ idct: 3, idh: hostHandlerViewModel.host.first.id });
					}
					if (hostHandlerViewModel.category4) {
						hostHandlerViewModel.host.third.push({ idct: 4, idh: hostHandlerViewModel.host.first.id });
					}

					// Insertamos los servicios elegidos en los checkbox en el host
					var servicesAux = ' ';
					if (hostHandlerViewModel.service0) {
						servicesAux += 'Cocina,';
					}
					else if (hostHandlerViewModel.service1) {
						servicesAux += 'Limpieza,';
					}
					else if (hostHandlerViewModel.service2) {
						servicesAux += 'Desayuno,';
					}
					else if (hostHandlerViewModel.service3) {
						servicesAux += 'Wifi,';
					}
					else if (hostHandlerViewModel.service4) {
						servicesAux += 'Lavadora,';
					}
					else {
						servicesAux += ' ';
					}

					hostHandlerViewModel.host.first.services = servicesAux;
					console.log("Servicios: ", servicesAux);

					hostsFactory.putHost(hostHandlerViewModel.host)
						.then(function(response) {
							console.log("Updating host with id: ", hostHandlerViewModel.host.first.id, " Response: ", response);
						}, function(response) {
							console.log("Error updating host with id:", hostHandlerViewModel.host.first.id);
						})
				},
				createHost: function() {
					// Insertamos las categorias elegidas en los checkbox en el host
					hostHandlerViewModel.host.third = [];
					if (hostHandlerViewModel.category0) {
						hostHandlerViewModel.host.third.push({ idct: 0, idh: -1 });
					}
					if (hostHandlerViewModel.category1) {
						hostHandlerViewModel.host.third.push({ idct: 1, idh: -1 });
					}
					if (hostHandlerViewModel.category2) {
						hostHandlerViewModel.host.third.push({ idct: 2, idh: -1 });
					}
					if (hostHandlerViewModel.category3) {
						hostHandlerViewModel.host.third.push({ idct: 3, idh: -1 });
					}
					if (hostHandlerViewModel.category4) {
						hostHandlerViewModel.host.third.push({ idct: 4, idh: -1 });
					}

					// Insertamos los servicios elegidos en los checkbox en el host
					var servicesAux = '';
					if (hostHandlerViewModel.service0) {
						servicesAux += 'Cocina,';
					}
					else if (hostHandlerViewModel.service1) {
						servicesAux += 'Limpieza,';
					}
					else if (hostHandlerViewModel.service2) {
						servicesAux += 'Desayuno,';
					}
					else if (hostHandlerViewModel.service3) {
						servicesAux += 'Wifi,';
					}
					else if (hostHandlerViewModel.service4) {
						servicesAux += 'Lavadora,';
					}
					else {
						servicesAux += ' ';
					}

					hostHandlerViewModel.host.first.services = servicesAux;

					hostsFactory.postHost(hostHandlerViewModel.host)
						.then(function(response) {
							console.log("Creating host. Response: ", response);
						}, function(response) {
							console.log("Error creating the host");
						})

				},
				deleteHost: function(id) {
					hostsFactory.deleteHost(id)
						.then(function(response) {
							console.log("Deleting host with id: ", id, " Response: ", response);
						}, function(response) {
							console.log("Error deleting host");
						})
				},
				hostHandlerSwitcher: function() {

					if (hostHandlerViewModel.functions.where('/insertHost')) {
						console.log($location.path());
						hostHandlerViewModel.host.first.idu = $rootScope.user.id;
						hostHandlerViewModel.functions.createHost();
					}
					else if (hostHandlerViewModel.functions.where('/editHost/' + hostHandlerViewModel.host.first.id)) {
						console.log($location.path());
						hostHandlerViewModel.functions.updateHost();
					}
					else if (hostHandlerViewModel.functions.where('/deleteHost/' + hostHandlerViewModel.host.first.id)) {
						console.log($location.path());
						console.log(hostHandlerViewModel.host.first.id);
						hostHandlerViewModel.functions.deleteHost(hostHandlerViewModel.host.first.id);
					}
					else {
						console.log($location.path());
					}
					$location.path('/');
				}
			}



			console.log("Entering hostHandlerCtrl with $routeParams.ID=", $routeParams.ID);
			if ($routeParams.ID != undefined) {
				hostHandlerViewModel.functions.readHost($routeParams.ID);
			}
		}]);