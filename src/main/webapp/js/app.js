angular.module('airbnbApp', ['ngRoute'])
.config(function($routeProvider){
	$routeProvider
		.when("/", {
    		controller: "listCtrl",
    		controllerAs: "listVM",
    		templateUrl: "listHostsTemplate.html",
    		resolve: {
    			// produce 500 miliseconds (0,5 seconds) of delay that should be enough to allow the server
    			//does any requested update before reading the orders.
    			// Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
    			delay: function($q, $timeout) {
    			var delay = $q.defer();
    			$timeout(delay.resolve, 600);
    			return delay.promise;
    			}
    		}
    	})
    	.when("/editUser", {
    		controller: "userHandlerCtrl",
    		controllerAs: "userHandlerVM",
    		templateUrl: "userHandlerTemplate.html"
    	})
    	.when("/deleteUser", {
			controller: "userHandlerCtrl",
    		controllerAs: "userHandlerVM",
    		templateUrl: "userHandlerTemplate.html"
		})
		.when("/insertHost", {
			controller: "hostHandlerCtrl",
			controllerAs: "hostHandlerVM",
			templateUrl: "hostHandlerTemplate.html"
		})
		.when("/editHost/:ID", {
			controller: "hostHandlerCtrl",
			controllerAs: "hostHandlerVM",
			templateUrl: "hostHandlerTemplate.html"
		})
		.when("/deleteHost/:ID", {
			controller: "hostHandlerCtrl",
			controllerAs: "hostHandlerVM",
			templateUrl: "hostHandlerTemplate.html"
		})
		.when("/hostDetails/:ID", {
			controller: "hostDetailCtrl",
			controllerAs: "hostDetailVM",
			templateUrl: "hostDetailTemplate.html"
		});
})