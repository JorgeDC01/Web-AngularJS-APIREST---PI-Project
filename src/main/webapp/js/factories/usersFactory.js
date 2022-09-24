angular.module('airbnbApp')
.factory('usersFactory',['$http', function($http){
	
	var url = 'https://localhost:8443/Proyecto_Airbnb/rest/users/';
	var usersInterface = {
    	getUser : function(){
			return $http.get(url)
				.then(function(response){
					return response.data;
				});
    	},
    	putUser : function(user){
			return $http.put(url,user)
				.then(function(response){
					return response.status;
				});
    	},
    	deleteUser : function(){
			return $http.delete(url)
				.then(function(response){
					return response.status;
				});
		}
    }
    return usersInterface; 
}])