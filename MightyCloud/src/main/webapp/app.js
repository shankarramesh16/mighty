function openNav() {
    document.getElementById("mySidenav").style.width = "250px";
    document.getElementById("PageContainer").style.marginLeft = "250px";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
    document.getElementById("PageContainer").style.marginLeft= "0";
}

var mightyApp = angular.module('mighty', ['ngRoute']);

mightyApp.config(function($routeProvider) {
    $routeProvider

        // route for the home page
        .when('/', {
            templateUrl : 'views/login.html'
        })
        .when('/admin', {
        	templateUrl : 'views/admin.html'
        })
        .when('/device', {
        	templateUrl : 'views/device.html',
        	controller : "deviceController"
        })
        // route for the about page
        ;
});

mightyApp.constant('config', {
    restURL: 'http://localhost:8080/MightyCloud/rest',
    restAllDevice:"/device"
});
