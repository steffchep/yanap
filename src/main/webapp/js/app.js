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
			return { class: "", character : ""};
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

var availabilityPopup = function(id) {
	$('#' + id).show();
};

var setAvail = function(array, index, value) {
	array[index] = value;
	$("ul").hide();
};

availabilityBoard.controller('availabilityController', function($scope) {
	 $scope.sprint = {
	 	name : "My Awesome Sprint",
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
				days : [ 1, 2, 3, 0.5, 1, 1, 1, 1, 1, 1 ]
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
     $scope.getMarkup = getMarkup;
	 $scope.availabilityPopup = availabilityPopup;
	 $scope.setAvail = setAvail;
});