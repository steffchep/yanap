var availabilityBoard = angular.module('availabilityBoard', ['ngSanitize']);

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
	var sum = calculateDevDays(developers);
	var total = developers.length * developers[0].days.length;
	return Math.round(sum / total * 100) + "%";
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
		allPopups.hide();
	}
};

var setAvail = function(array, index, value) {
	array[index] = value;
	$("ul").hide();
};

var getClassForStatus = function(status) {
	return status.replace(/\s/g, "_");
};

availabilityBoard.controller('availabilityController', function($scope) {
	 $scope.sprint = {
	 	id: 0815,
	 	name : "My Awesome Sprint",
		startDate: "2016.01.01",
		endDate: "2016.01.14",
		status: "in progress",
		developers : [
			{
				name : "Dawid",
				days : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]
			},
			{
				name : "Jana",
				days : [ 1, 1, 3, 3, 1, 1, 1, 1, 1, 1 ]
			},
			{
				name : "Jessica",
				days : [ 1, 2, 1, 1, 1, 1, 1, 0.5, 3, 3 ]
			},
			{
				name : "Michael",
				days : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]
			},
			{
				name : "Rauf",
				days : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]
			},
			{
				name : "Steffi",
				days : [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ]
			}
		],
		nondevelopers : [
			{
				name : "Tom",
				days : [ 1, 1, 1, 1, 1, 3, 3, 3, 3, 3 ]
			},
			{
				name : "Phil",
				days : [ 1, 1, 1, 1, 1, 3, 3, 3, 3, 3 ]
			}
		]
	 };

     $scope.calculateDevDays = calculateDevDays;
     $scope.percentTotal = percentTotal;
     $scope.getUnplannedAbsences = getUnplannedAbsences;
     $scope.getMarkup = getMarkup;
     $scope.getClassForStatus = getClassForStatus;
	 $scope.availabilityPopup = availabilityPopup;
	 $scope.setAvail = setAvail;
	 $scope.isNotClosed = $scope.sprint.status !== 'closed';
	 $scope.isInProgress = $scope.sprint.status === 'in progress';
});

availabilityBoard.controller('boardListController', function($scope) {
	 $scope.boards = [
		 {
		 	id: 0815,
		 	name: "My first sprint",
		 	startDate: "2016.01.01",
		 	endDate: "2016.01.14",
		 	status: "ended"
		 },
		 {
		 	id: 0816,
		 	name: "My second sprint",
		 	startDate: "2016.01.14",
		 	endDate: "2016.01.28",
            status: "in progress"
		 },
		 {
		 	id: 0817,
		 	name: "My third sprint",
		 	startDate: "2016.01.28",
		 	endDate: "2016.02.11",
            status: "upcoming"
		 }
	 ];

	$scope.boards.sort(function(sprintA, sprintB) {
         return sprintA.startDate > sprintB.startDate ? -1 : 1;
	});

     $scope.getClassForStatus = getClassForStatus;
});