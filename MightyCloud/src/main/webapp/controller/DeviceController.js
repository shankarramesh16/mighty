/**
 * 
 */
mightyApp.controller('deviceController', function( $modal,$scope,$location, $http) {
	
    // init
    $scope.sort = {       
                sortingOrder : 'id',
                reverse : false
            };
    
    $scope.gap = 5;
    
    $scope.filteredItems = [];
    $scope.groupedItems = [];
	$scope.itemsPerPage = 5;
	$scope.currentPage = 0;
	$scope.pagedItems = [];
	
	$scope.groupToPages = function () {
        $scope.pagedItems = [];
        
        for (var i = 0; i < $scope.filteredItems.length; i++) {
            if (i % $scope.itemsPerPage === 0) {
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.filteredItems[i] ];
            } else {
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.filteredItems[i]);
            }
        }
    };
    
    var $ctrl = this;
    $ctrl.items = ['item1', 'item2', 'item3'];
    
    $ctrl.selected = {
      item: $ctrl.items[0]
    };

    $ctrl.ok = function () {
     
    };

    $ctrl.cancel = function () {
    };

    $scope.deviceInfo = [{id: 'choice1', deviceId : "",manufDate : ""}];
    
    $scope.addDeviceInfoChoice = function() {
        var newItemNo = $scope.deviceInfo.length+1;
        $scope.deviceInfo.push({'id':'choice'+newItemNo, deviceId : "",manufDate : ""});
      };
      
      $scope.removeDeviceInfoChoice = function() {
    	    var lastItem = $scope.deviceInfo.length-1;
    	    $scope.deviceInfo.splice(lastItem);
    	  };
    	  
    $scope.open = function () {

    var modalInstance = $modal.open({
        templateUrl: 'addModelContent.html',
        size: 'sm',
        resolve: {
          items: function () {
            return $ctrl.items;
          }
        }
      });
    };
    
   
    
	$scope.prevPage = function () {
        if ($scope.currentPage > 0) {
            $scope.currentPage--;
        }
    };
    
    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.pagedItems.length - 1) {
            $scope.currentPage++;
        }
    };
    
    $scope.setPage = function () {
        $scope.currentPage = this.n;
    };

    $scope.range = function (size,start, end) {
        var ret = [];        
        console.log(size,start, end);
                      
        if (size < end) {
            end = size;
            start = size-$scope.gap;
        }
        for (var i = start; i < end; i++) {
            ret.push(i);
        }        
         console.log(ret);        
        return ret;
    };
	
    $scope.editDevice = function(deviceId) {
    	console.log("Edit Device" +deviceId);
    };
    
    $scope.deleteDevice = function(deviceId) {
    	console.log("Delete Device "+deviceId);
    };
    
	$scope.listAllDevice = function() {
		console.log("DeviceController.listAllDevice");
		$http.get("http://localhost:8080/MightyCloud/rest/admin/device").
		then(function success(response){
			console.log(response.data);
			$scope.items = response.data;
		}, function error(response) {
			console.log(reponse);
		})
		
	}
	
	$scope.uploadFile = function(){
        var file = $scope.myFile;
        console.log('file is ' );
        console.dir(file);
        var uploadUrl = "http://localhost:8080/MightyCloud/rest/admin/device/upload";
        
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': "plain/text"}
        })
        .success(function(){
        	console.log("Success");
        })
        .error(function(){
        	console.log("Failed");
        });
        
    };
});

mightyApp.$inject = ['$scope', '$filter'];

mightyApp.directive("customSort", function() {
return {
    restrict: 'A',
    transclude: true,    
    scope: {
      order: '=',
      sort: '='
    },
    template : 
      ' <a ng-click="sort_by(order)" style="color: #555555;">'+
      '    <span ng-transclude></span>'+
      '    <i ng-class="selectedCls(order)"></i>'+
      '</a>',
    link: function(scope) {
                
    // change sorting order
    scope.sort_by = function(newSortingOrder) {       
        var sort = scope.sort;
        
        if (sort.sortingOrder == newSortingOrder){
            sort.reverse = !sort.reverse;
        }                    

        sort.sortingOrder = newSortingOrder;        
    };
    
   
    scope.selectedCls = function(column) {
        if(column == scope.sort.sortingOrder){
            return ('icon-chevron-' + ((scope.sort.reverse) ? 'down' : 'up'));
        }
        else{            
            return'icon-sort' 
        } 
    };      
  }// end link
}
});

mightyApp.directive('fileModel', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          var model = $parse(attrs.fileModel);
          var modelSetter = model.assign;
          
          element.bind('change', function(){
             scope.$apply(function(){
                modelSetter(scope, element[0].files[0]);
             });
          });
       }
    };
 }]);
