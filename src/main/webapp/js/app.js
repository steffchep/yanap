var availabilityBoard = angular.module('availabilityBoard', ['ngSanitize']);

var emptySprint = {
	"developers": [],
	"nondevelopers": []
};

var getMarkup = function(availability) {
	switch(availability) {
		case 1:
			return { class: "available", character : "&#x2713;"};
		case 0.5:
			return { class: "available", character : "(&#x2713;)"};
		case 2:
			return { class: "notavailable", character : "O"};
		case 3:
			return { class: "notavailable", character : "X"};
		case 4:
			return { class: "notavailable", character : ":("};
		default:
			return { class: "noneselected", character : "?"};
	}
};

var getStatusText = function(status) {
	switch(status) {
		case 1:
			return "upcoming";
		case 2:
			return "in progress";
		default:
			return "ended";
	}
};

var calculateDevDays = function(developers) {
	var sum = 0;
	for (i = 0; i < developers.length; i++) {
		for (j = 0; j < developers[i].days.length; j++) {
			if (developers[i].days[j] < 2) {
				sum += developers[i].days[j];
			}
		}
	}
	return sum;
};

var getUnplannedAbsences = function(developers) {
	var sum = 0;
	for (i = 0; i < developers.length; i++) {
		for (j = 0; j < developers[i].days.length; j++) {
			if (developers[i].days[j] === 4) {
				sum += 1;
			}
		}
	}
	return sum;
};

var percentTotal = function(developers) {
	if (developers.length > 0) {
		var sum = calculateDevDays(developers);
		var total = developers.length * (developers[0].days.length - 1);
		return Math.round(sum / total * 100) + "%";
	}
	return 0;
};

var availabilityPopup = function(id) {
	var status = $('#sprintStatus').html(),
		currentPopup = $('#' + id),
		allPopups = $("ul");
	if (status === "ended") {
		alert("This sprint has already ended, it is read-only");
	} else if(!currentPopup.is(":visible")) {
		allPopups.hide();
		currentPopup.show(); // TODO: set absolute position manually
		if	(status === "upcoming") {
			$('.hideonupcoming').hide();
		}
	} else {
		$('.hideonupcoming').show();
		allPopups.hide();
	}
};


var setAvail = function(array, index, value) {
	array[index] = value;
	$('.hideonupcoming').show();
	$("ul").hide();
};

var statusPopup = function(id) {
	var currentPopup = $('#' + id),
		allPopups = $("ul");
	if(!currentPopup.is(":visible")) {
		allPopups.hide();
		currentPopup.show(); // TODO: set absolute position manually
	} else {
		allPopups.hide();
	}
};

var setSprintStatus = function(sprint, status) {
	$("ul").hide();
	sprint.status = status;
};

var getClassForStatus = function(status) {
	return getStatusText(status).replace(/\s/g, "_");
};

var saveSprintData = function(json) {

};

availabilityBoard.controller('availabilityController', function($scope, $http) {
	$scope.sprint = emptySprint;

	$http.get('single-sprint.json').success(function(res){
		$scope.sprint = res;
	});

	$scope.calculateDevDays = calculateDevDays;
	$scope.percentTotal = percentTotal;
	$scope.getUnplannedAbsences = getUnplannedAbsences;
	$scope.getMarkup = getMarkup;
	$scope.getStatusText = getStatusText;
	$scope.getClassForStatus = getClassForStatus;
	$scope.availabilityPopup = availabilityPopup;
	$scope.isNotClosed = $scope.sprint.status !== 'closed';
	$scope.isInProgress = $scope.sprint.status === 'in progress';
	$scope.statusPopup = statusPopup;
	$scope.setAvail = setAvail;
	$scope.setSprintStatus = setSprintStatus;
	$scope.saveSprint = function() {
		console.log("saving");
		$http.post('/boards', $scope.sprint);
		console.log($scope.sprint);
	};
});

availabilityBoard.controller('boardListController', function($scope) {
	 $scope.boards = [
		 {
		 	id: 815,
		 	name: "My first sprint",
		 	startDate: "2016.01.01",
		 	endDate: "2016.01.14",
		 	status: 3
		 },
		 {
		 	id: 816,
		 	name: "My second sprint",
		 	startDate: "2016.01.14",
		 	endDate: "2016.01.28",
            status: 2
		 },
		 {
		 	id: 817,
		 	name: "My third sprint",
		 	startDate: "2016.01.28",
		 	endDate: "2016.02.11",
            status: 1
		 }
	 ];

//	$scope.boards.sort(function(sprintA, sprintB) {
//         return sprintA.startDate > sprintB.startDate ? -1 : 1;
//	});

     $scope.getClassForStatus = getClassForStatus;
     $scope.getStatusText = getStatusText;
});