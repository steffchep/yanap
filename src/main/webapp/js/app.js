var emptySprint = {
	"developers": [],
	"nondevelopers": []
};

var weekDays = ["Mon", "Tue", "Wed", "Thu", "Fri", "", ""];
var daysInWeek = 7;

var createTableHeaders = function(sprint) {
	var start = moment(new Date(sprint.startDate)),
		end = moment(new Date(sprint.endDate)),
		days = end.diff(start, "days"),
		headers = [], daysPos = start.day() - 1, headersPos = 0;

	for (var i = 0; i < days; i++) {
		if (daysPos === weekDays.length) {
			daysPos = 0;
		}
		if (weekDays[daysPos] !== "") {
			headers[headersPos] = weekDays[daysPos];
			headersPos = headersPos + 1;
		}
		daysPos = daysPos + 1;
	}
	return headers;
};

var adjustSprintDaysForPersons = function(person, length) {
	var rest = person.days.length - length;
	person.days.splice(length, rest);
};

var	adjustSprintDays = function (sprint, length) {
	if (sprint.developers && sprint.developers.length > 0 && sprint.developers[0].days.length > length) {
		$.each(sprint.developers, function(index, value) {
			adjustSprintDaysForPersons(value, length);
		});
	}
	if (sprint.nondevelopers && sprint.nondevelopers.length > 0 && sprint.nondevelopers[0].days.length > length) {
		$.each(sprint.nondevelopers, function(index, value) {
			adjustSprintDaysForPersons(value, length);
		});
	}
};


var formatTime = function(timestamp) {
	var formatMe = new Date(timestamp); // work around deprecation warning
	return moment(formatMe).format('ll');
};

var checkDateSanity = function(sprint) {
	var now = moment(new Date()).startOf('day');
	var start = moment(new Date(sprint.startDate));
	var end = moment(new Date(sprint.endDate));
	return start.isBefore(end) && start.isAfter(now);
};

var checkSprintInput = function($scope) {
	$scope.lastError = "";
	if (!$scope.newSprint.name || $scope.newSprint.name === '') {
		$scope.lastError = "Sprint name must be set!<br>";
		$('#newSprintName').addClass("error");
	} else {
		if ($scope.newSprint.name.length > 50) {
			$scope.lastError = "Sprint name cannot be longer than 50 chars!<br>";
			$('#newSprintName').addClass("error");
		}
	}
	if (!$scope.newSprint.team || !$scope.newSprint.team === '') {
		$scope.lastError += "Team must be set!<br>";
		$('#newSprintTeam').addClass("error");
	}
	if ($scope.newSprint.users.length == 0) {
		$scope.lastError += "There must be at least one team member!<br>";
	}

	var datesValid = true;
	if (!moment($scope.newSprint.startDate, 'YYYY-MM-DD', true).isValid() ) {
		$scope.lastError += "StartDate must be valid!<br>";
		$('#newSprintStart').addClass("error");
		datesValid = false;
	}
	if (!moment($scope.newSprint.endDate, 'YYYY-MM-DD', true).isValid() ) {
		$scope.lastError += "EndDate must be valid!<br>";
		$('#newSprintEnd').addClass("error");
		datesValid = false;
	}
	if (datesValid && !checkDateSanity($scope.newSprint) ) {
		$scope.lastError += "StartDate must be in the future, EndDate must be after StartDate!<br>";
		$('#newSprintStart').addClass("error");
		$('#newSprintEnd').addClass("error");
	}

	if ($scope.lastError !== '') {
		$('#saveerror').show();
		return false;
	}
	return true;
}

var checkUser = function(user) {
	return true;
};

var getParameterByName = function (name) {
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
	var results = regex.exec(location.href);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
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

var getTeamList = function($scope, $http) {
	$http.get('boards/teams').success(function(res){
		$scope.teamlist = res;
	});
}

var availabilityBoard = angular.module('availabilityBoard', ['ngSanitize']);

availabilityBoard.controller('availabilityController', function($scope, $http) {
	var id = getParameterByName("id");
	$scope.sprint = emptySprint;

	$http.get('boards/' + id).success(function(res){
		$scope.sprint = res;
		$scope.tableHeaders = createTableHeaders($scope.sprint);
		adjustSprintDays($scope.sprint, $scope.tableHeaders.length);
		$('.summary').attr("colspan", $scope.tableHeaders.length + 1);
	}).error(function(err) {
		$('#loaderror').show();
		console.log("Error fetching the Sprint: " + JSON.stringify(err, null, " "));
	});

	var updateButtonOriginalText = $('#updateSprintButton').text();

	var disableSaveButton = function(disabledText) {
		var updateButton = $('#updateSprintButton');
		if (disabledText) {
			updateButton.text(disabledText)
		}
		updateButton.prop("disabled", true);
	}

	var enableSaveButton = function() {
		var updateButton = $('#updateSprintButton');
		updateButton.text(updateButtonOriginalText);
		updateButton.prop("disabled", false);
	}

	var saveSprint = function() {
		console.log("saving " + id);
		disableSaveButton("wait...");

		$http.post('boards', $scope.sprint)
		.success(function() {
			enableSaveButton();
		})
		.error(function(err) {
			$('#saveerror').show();
			enableSaveButton();
			console.log("Error saving the Sprint: " + JSON.stringify(err, null, " "));
		});
	};

	$scope.saveSprint = saveSprint;
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
		$http.post('boards/' + id + "/availability", { "userId" : person.id, "dayIndex" : index, "value" : value})
			.success(function(res) {
				console.log("save property okay!");
			})
			.error(function(err) {
				$('#saveerror').show();
				console.log("Error saving the Data: " + JSON.stringify(err, null, " "));
			});
	};
	$scope.setSprintStatus = function(status) {
		$("ul").hide();
		$scope.sprint.status = status;
	};
});

availabilityBoard.controller('boardListController', function($scope, $http) {
	$scope.boards = [];

	getTeamList($scope, $http);

	$http.get('boards').success(function(res){
		$scope.boards = res;
	});

	$scope.newSprint = { users: [] };

	$scope.usersByTeam = [];

	$scope.getUsersByTeam = function() {
		$('#add_users_popup').hide();
 		$('#newSprintTeam').removeClass('error');
 		if ($scope.newSprint.team && $scope.newSprint.team !== '') {
			$http.get('boards/users/' + $scope.newSprint.team).success(function(res){
				$scope.newSprint.users = res;
				console.log("Done fetching userlist:");
				console.log($scope.newSprint.users);
			});
		}
	};

	$scope.removeUserFromSprint = function (user) {
		var deleteIndex = -1;
		$.each($scope.newSprint.users, function(index, value) {
			if (user.id === value.id) {
				deleteIndex = index;
			}
		});
		$scope.newSprint.users.splice(deleteIndex, 1);
	};

	$scope.usersPopup = function() {
		if (!$scope.newSprint.team || $scope.newSprint.team === '') {
			$('#error_no_team').show();
		} else {
			$('#error_no_team').hide();
		}
		$('#add_users_popup').show();
	};

	$scope.lastError = "";

	$scope.createSprint = function() {
		if (checkSprintInput($scope)) {
			console.log("create Sprint");
			$http.put('boards', $scope.newSprint).success(function(res){
				console.log("Sprint created, updating list");
				$http.get('boards').success(function(res){
					$scope.boards = res;
					$scope.newSprint = {users: []};
					$('#newSprintName').focus();
				});
			});
		}
		console.log($scope.newSprint);
	};

	$scope.deleteSprint = function(sprint) {
		if (confirm("Are you sure you want to delete this sprint?")) {
			$http.delete('boards/' + sprint.id).success(function(res){
				console.log("Sprint deleted, updating list");
				$http.get('boards').success(function(res){
					$scope.boards = res;
					$scope.newSprint = {users: []};
					$('#newSprintName').focus();
				});
			});
		}
	};

	$scope.formatTime = formatTime;
    $scope.getClassForStatus = getClassForStatus;
    $scope.getStatusText = getStatusText;
});

availabilityBoard.controller('userListController', function($scope, $http) {
	$scope.users = [];
	$scope.newUser = { team: "" };

	getTeamList($scope, $http);

	$scope.getUsers = function(team) {
		$http.get('boards/users/' + (team || "all")).success(function(res){
			$scope.users = res;
			console.log("Done fetching userlist:");
			console.log($scope.users);
		});
	};

	$scope.createUser = function() {
		if (checkUser($scope.newUser)) {
			console.log("create User");
			$http.put('boards/users', $scope.newUser).success(function(res){
				console.log("User created, updating list");
				$scope.newUser = { team: "" };
				$scope.getUsers();
				$('#newUserName').focus();
			});
		}
		console.log($scope.newUser);
	};

	$scope.updateUser = function(userObject) {
		if (checkUser(userObject)) {
			console.log("update User");
			$http.post('boards/users', userObject).success(function(res){
				console.log("User updated, updating list");
				$scope.getUsers();
				$('#newUserName').focus();
			});
		}
	};

	$scope.enableSaveButton = function(id) {
		$('#' + id).prop('disabled', false);
	};

	$scope.deleteUser = function(user) {
		alert("Not yet supported");
	};

	$scope.getUsers();

});

availabilityBoard.controller('teamListController', function($scope, $http) {
	getTeamList($scope, $http);
	
	$scope.newTeam = { name: "" };

	$scope.createTeam = function() {
		if ($scope.newTeam.name.trim() !== "") {
			console.log("create team");
			$http.put('boards/teams', $scope.newTeam).success(function(res){
				console.log("Team created, updating list");
				$scope.newTeam = { name: "" };
				getTeamList($scope, $http);
				$('#newTeamName').focus();
			});
		} else {
			$('#newTeamName').addClass("error");
			$scope.lastError = "Team name must be set."
      		$('#saveerror').show();
      	}
	};

	$scope.deleteTeam = function(teamObject) {
		if (checkUser(teamObject)) {
			console.log("delete Team");
			$http.delete('boards/teams/' + teamObject.id).success(function(res){
				console.log("Team deleted, updating list");
				getTeamList($scope, $http);
				$('#newTeamName').focus();
			});
		}
	};

});
