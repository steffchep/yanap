var availabilityBoard = angular.module('availabilityBoard', ['ngSanitize']);

var emptySprint = {
	"developers": [],
	"nondevelopers": []
};

var formatTime = function(timestamp) {
	return moment(timestamp).format('ll');
};

var getParameterByName = function (name) {
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
	var results = regex.exec(location.href);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
};


var clone = function (obj) {
	return angular.copy(obj);
};


var isEqual = function(o1, o2) {
	return angular.equals(o1, o2) ;
};

var makeId = function(string) {
	return string.replace(/\s/g, "_");
}

var getMarkup = function(availability) {
	switch(availability) {
		case 0:
			return { class: "noneselected", character : "?"};
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
			return { class: "noneselected", character : availability};
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

var totalDays = function(developers) {
	if (developers.length > 0) {
		return developers.length * (developers[0].days.length - 1);
	}
	return 0;
};

var percentTotal = function(developers) {
	if (developers.length > 0) {
		var sum = calculateDevDays(developers);
		var total = totalDays(developers);
		return Math.round(sum / total * 100) + "%";
	}
	return 0;
};

var availabilityPopup = function(id) {
	console.log("popup");
	var status = $('#sprintStatus').html(),
		currentPopup = $('#' + id),
		allPopups = $("ul");
	if (status === "ended") {
		alert("This sprint has already ended, it is read-only");
	} else if(!currentPopup.is(":visible")) {
	console.log("popup show");
		allPopups.hide();
		currentPopup.show(); // TODO: set absolute position manually
		if	(status === "upcoming") {
			$('.hideonupcoming').hide();
		}
	} else {
		console.log("popup hide");
		$('.hideonupcoming').show();
		allPopups.hide();
	}
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

var getClassForStatus = function(status) {
	return getStatusText(status).replace(/\s/g, "_");
};

availabilityBoard.controller('availabilityController', function($scope, $http) {
	var id = getParameterByName("id");
	$scope.sprint = emptySprint;
	console.log("id = " + id);

	$http.get('/boards/' + id).success(function(res){
		$scope.sprint = res;
		$scope.sprintLast = clone(res);
	});

	$scope.saveSprint = function() {
		console.log("saving " + id);


		$http.get('/boards/' + id).success(function(res){
			console.log("copies equal ? " + isEqual(res, $scope.sprintLast));
			console.log(res);
			if (isEqual(res, $scope.sprintLast)) {
				$http.post('/boards/0815', $scope.sprint);
				$scope.sprintLast = clone($scope.sprint);
			} else {
				$scope.sprint = res;
				$scope.sprintLast = clone(res);
				$('#saveerror').show();
			}
		});
	};

	$scope.calculateDevDays = calculateDevDays;
	$scope.percentTotal = percentTotal;
	$scope.totalDays = totalDays;
	$scope.getUnplannedAbsences = getUnplannedAbsences;
	$scope.getMarkup = getMarkup;
	$scope.getStatusText = getStatusText;
	$scope.getClassForStatus = getClassForStatus;
	$scope.availabilityPopup = availabilityPopup;
	$scope.isNotClosed = $scope.sprint.status !== 'closed';
	$scope.isInProgress = $scope.sprint.status === 'in progress';
	$scope.statusPopup = statusPopup;
	$scope.formatTime = formatTime;
	$scope.makeId = makeId;
	$scope.setAvail = function(person, index, value) {
		person.days[index] = value;
		$('.hideonupcoming').show();
		$("ul").hide();
		// TODO: save avail
	};
	$scope.setSprintStatus = function(status) {
		$("ul").hide();
		$scope.sprint.status = status;
		// TODO: save status
	};
});

availabilityBoard.controller('boardListController', function($scope, $http) {
	$scope.boards = [];

	$http.get('/boards').success(function(res){
		$scope.boards = res;
	});

	 $scope.formatTime = formatTime;
     $scope.getClassForStatus = getClassForStatus;
     $scope.getStatusText = getStatusText;
});