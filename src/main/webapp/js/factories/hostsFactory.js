angular.module('airbnbApp')
.factory("hostsFactory", ['$http',function($http){
	
	var url = 'https://localhost:8443/Proyecto_Airbnb/rest/hosts/';
	var hostsInterface = {
		getHosts : function(code,minLikes,id_hostDetails){
			var urlcode = url + 'code/' + code;
			return $http.get(urlcode,{
					    headers: {'minLikes': minLikes,'id_hostDetails': id_hostDetails}
				})
				.then(function(response){
					return response.data;
				});
		},
		getHost : function(id){
			var urlid = url + id;
			return $http.get(urlid)
				.then(function(response){
					return response.data;
				});
		},
		putHost : function(host){
			var urlid = url + host.first.id;
			return $http.put(urlid,host)
				.then(function(response){
					return response.status;
				});
		},
		postHost : function(host){
			return $http.post(url,host).
				then(function(response){
					return response.status;
				});
		},
		deleteHost : function(id){
			var urlid = url + id;
			return $http.delete(urlid)
				.then(function(response){
					return response.status;
				});
		},
		LikeNotLikeHost : function(id){
			var urlid = url + 'like/' + id;
			return $http.put(urlid,null)
				.then(function(response){
					return response.status;
				});
		},
	}
	return hostsInterface;
}])