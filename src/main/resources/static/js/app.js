var emptySprint = {
	"developers": [],
	"nondevelopers": []
};

var weekDays = ["Mon", "Tue", "Wed", "Thu", "Fri", "", ""];

var createTableHeaders = function(sprint) {
	"use strict";
	var start = moment(new Date(sprint.startDate)),
		end = moment(new Date(sprint.endDate)),
		days = end.diff(start, "days"),
		headers = [], daysPos = start.day() - 1, headersPos = 0;
	var i;
	for (i = 0; i < days; i++) {
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
	"use strict";
	var rest = person.days.length - length;
	person.days.splice(length, rest);
};

var	adjustSprintDays = function (sprint, length) {
	"use strict";
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
	"use strict";
	var formatMe = new Date(timestamp); // work around deprecation warning
	return moment(formatMe).format('ll');
};

function getMinStartDate() {
	"use strict";
	return moment(new Date()).startOf('day').subtract(7, "days").startOf('isoweek');
}
var checkDateSanity = function(sprint) {
	"use strict";
	var minStartDate = getMinStartDate();
	var start = moment(new Date(sprint.startDate));
	var end = moment(new Date(sprint.endDate));
	return start.isBefore(end) && start.isAfter(minStartDate);
};

var checkSprintInput = function($scope) {
	"use strict";
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
	if ($scope.newSprint.users.length === 0) {
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
};

var checkUser = function(user) {
	"use strict";
	return true;
};

var getParameterByName = function (name) {
	"use strict";
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)");
	var results = regex.exec(location.href);
	return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
};

var makeId = function(string) {
	"use strict";
	return string.replace(/\s/g, "_");
};

var getMarkup = function(availability) {
	"use strict";
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
	"use strict";
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
	"use strict";
	var sum = 0;
	var i, j;
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
	"use strict";
	var sum = 0;
	var i, j;
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
	"use strict";
	if (developers.length > 0) {
		return developers.length * (developers[0].days.length - 1);
	}
	return 0;
};

var percentTotal = function(developers) {
	"use strict";
	if (developers.length > 0) {
		var sum = calculateDevDays(developers);
		var total = totalDays(developers);
		return Math.round(sum / total * 100) + "%";
	}
	return 0;
};

var availabilityPopup = function(id) {
	"use strict";
	var status = $('#sprintStatus').html(),
		currentPopup = $('#' + id),
		allPopups = $("ul");
	if (status === "ended") {
		alert("This sprint has already ended, it is read-only");
	} else if(!currentPopup.is(":visible")) {
		allPopups.hide();
		currentPopup.show();
		if	(status === "upcoming") {
			$('.hideonupcoming').hide();
		}
	} else {
		$('.hideonupcoming').show();
		allPopups.hide();
	}
};


var statusPopup = function(id) {
	"use strict";
	var currentPopup = $('#' + id),
		allPopups = $("ul");
	if(!currentPopup.is(":visible")) {
		allPopups.hide();
		currentPopup.show();
	} else {
		allPopups.hide();
	}
};

var confirmPopup = function(text, yescallback, nocallback) {
	"use strict";
	var html= $('<div class="errorpopup"><div class="confirm"><p>' + text + '</p><input type="submit" value="yes" name="Yes" id="confirm_ok"><input type="reset" value="no" name="No" id="confirm_cancel"></div></div>');
	$("body").append(html);
	$('#confirm_ok').on("click", function() {
		if (yescallback) {
			yescallback();
		}
		html.remove();
	});
	$('#confirm_cancel').on("click", function() {
		if (nocallback) {
			nocallback();
		}
		html.remove();
	});
};

var getClassForStatus = function(status) {
	"use strict";
	return getStatusText(status).replace(/\s/g, "_");
};

function getTeamList($scope, $http) {
	"use strict";
	$http.get('teams').success(function(res){
		$scope.teamlist = (res._embedded || {}).teams || [];
	});
}

var availabilityBoard = angular.module('availabilityBoard', ['ngSanitize', 'xeditable']);


availabilityBoard.controller('availabilityController', function($scope, $http) {
	"use strict";
	var id = getParameterByName("id");
	$scope.sprint = emptySprint;
	function hash(sprint) {
		return JSON.stringify({
			"name": sprint.name,
			"team": sprint.team,
			"startDate": sprint.startDate,
			"endDate": sprint.endDate,
			"status": sprint.status,
			"pointsPlanned": sprint.pointsPlanned,
			"pointsCompleted": sprint.pointsCompleted
		});
	}
	var loadedHash = hash(emptySprint);
	$scope.sprintChanged = function() {
		var current = hash($scope.sprint);
		return current != loadedHash;
	};
	$http.get('boards/' + id).success(function(res){
		res.startDate = new Date(res.startDate);
		res.endDate = new Date(res.endDate);
		$scope.sprint = res;
		loadedHash = hash(res);
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
			updateButton.text(disabledText);
		}
		updateButton.prop("disabled", true);
	};
	
	var enableSaveButton = function() {
		var updateButton = $('#updateSprintButton');
		updateButton.text(updateButtonOriginalText);
		updateButton.prop("disabled", false);
	};
	
	$scope.saveSprint = function() {
		console.log("saving " + id);
		disableSaveButton("wait...");
		
		$http.put('sprints/' + $scope.sprint.id, {
			name: $scope.sprint.name,
			endDate: $scope.sprint.endDate,
			startDate: $scope.sprint.startDate,
			status: $scope.sprint.status,
			pointsPlanned: $scope.sprint.pointsPlanned,
			pointsCompleted: $scope.sprint.pointsCompleted,
			team: $scope.sprint.team
		})
			.success(function() {
				location.reload();
			})
			.error(function(err) {
				$('#saveerror').show();
				enableSaveButton();
				console.log("Error saving the Sprint: " + JSON.stringify(err, null, " "));
			});
	};
	$scope.removePerson = function(person) {
		confirmPopup("Are you sure you want to remove <b>" + person.name + "</b> from sprint <b>'" + $scope.sprint.name + "'</b>?", function() {
			$http.post("/removeUserFromSprint?user=" + person.id + "&sprint=" + $scope.sprint.id)
				.success(function(){
					console.log("user removed, updating list");
					location.reload();
				});
		});
	};
	$scope.addPerson = function() {
		$http.get("/users/")
			.success(function(response) {
				var html= $(
					'<div class="errorpopup">' +
						'<div class="confirm">' +
							'<form method="post" action="/addUserToSprint">' +
								'<p>' +
									'Pick User to add: ' +
									'<select class="me" name="user"></select>' +
								'</p>' +
								'<input type="submit" value="Add"/>' +
								'<input type="reset" class="cancel" value="Cancel">' +
								'<input type="hidden" name="sprint" value="' + $scope.sprint.id + '" />' +
							'</form>' +
						'</div>' +
					'</div>'
				);
				response._embedded.users.forEach(function(user) {
					html.find('.me').append($('<option></option>').text(user.name).val(user._links.self.href))
				});
				html.find('.cancel').click(function() {
					html.remove();
				});
				html.appendTo('body');
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
		$http.post('boards/' + id + "/availability", { "userId" : person.id, "dayIndex" : index, "value" : value})
			.success(function() {
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

var pageSize = 20;
availabilityBoard.controller('boardListController', function($scope, $http) {
	"use strict";
	$scope.boards = [];
	
	getTeamList($scope, $http);
	
	function reload() {
		$http.get('sprints?sort=startDate,desc&size=' + pageSize).success(function(res){
			$scope.sprints = (res._embedded || {}).sprints || [];
			$scope.hasMore = (res.page || {}).totalPages > 1;
		});
		
		$scope.newSprint = {
			startDate: "",
			endDate: "",
			team: "",
			users: []
		};
		$scope.getMinStartDate = function() {
			return getMinStartDate().format('YYYY-MM-DD');
		};
		$scope.getMinEndDate = function() {
			return $scope.newSprint.startDate;
		};
	}
	reload();
	$scope.loadMoreSprints = function() {
		pageSize *= 2;
		reload();
	};
	$scope.updateDates = function() {
		var latestFoundSprintEnd = null;
		$scope.sprints.forEach(function (sprint) {
			if (sprint.team == $scope.newSprint.team && sprint.endDate > '' && (latestFoundSprintEnd == null || latestFoundSprintEnd.before(moment(sprint.endDate, 'YYYY-MM-DD')))) {
				latestFoundSprintEnd = moment(sprint.endDate, 'YYYY-MM-DD');
			}
		});
		if (latestFoundSprintEnd == null) {
			latestFoundSprintEnd = (new Date().getDay() === 1 ? moment() : moment().endOf('isoweek').add(1, 'day'));
		}
		var sprintDays = 14;
		$scope.teamlist.forEach(function(team) {
			if (team.name == $scope.newSprint.team) {
				sprintDays = team.sprintDays;
			}
		});
		$scope.newSprint.startDate = latestFoundSprintEnd.format('YYYY-MM-DD');
		$scope.newSprint.endDate = moment(latestFoundSprintEnd).add(sprintDays, 'days').format('YYYY-MM-DD');
	};
	//TODO: remove need for this
	$scope.getId = function(entity) {
		return entity._links.self.href.replace(/^.*\/(\d+)$/, '$1');
	};
	
	$scope.usersByTeam = [];
	
	$scope.getUsersByTeam = function() {
		$('#add_users_popup').hide();
		$('#newSprintTeam').removeClass('error');
		if ($scope.newSprint.team && $scope.newSprint.team !== '') {
			$http.get('users/search/findByTeam?team=' + $scope.newSprint.team).success(function(res){
				$scope.newSprint.users = (res._embedded || {}).users || [];
				$scope.newSprint.users.forEach(function(user) {
					user.id = $scope.getId(user);
				});
				console.log("Done fetching userlist:");
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
			$http.post('boards', $scope.newSprint).success(function(){
				console.log("Sprint created, updating list");
				reload();
				$('#newSprintName').focus();
			});
		}
		console.log($scope.newSprint);
	};
	
	$scope.deleteSprint = function(sprint) {
		confirmPopup("Are you sure you want to delete sprint <b>'" + sprint.name + "'</b>?", function() {
			$http.delete(sprint._links.self.href).success(function(){
				console.log("Sprint deleted, updating list");
				reload();
			});
		});
	};
	
	$scope.formatTime = formatTime;
	$scope.getClassForStatus = getClassForStatus;
	$scope.getStatusText = getStatusText;
});

availabilityBoard.controller('userListController', function($scope, $http) {
	"use strict";
	$scope.users = [];
	$scope.newUser = { team: "" };
	
	getTeamList($scope, $http);
	
	$scope.getUsers = function(team) {
		$http.get('users' + (team ? '/search/findByTeam?team=' + team : '')).success(function(res){
			$scope.users = (res._embedded || {}).users || [];
			console.log("Done fetching userlist:");
			console.log($scope.users);
		});
	};
	
	$scope.createUser = function() {
		if (checkUser($scope.newUser)) {
			console.log("create User");
			$http.post('users', $scope.newUser).success(function(){
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
			$http.put(userObject._links.self.href, userObject).success(function(){
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
	"use strict";
	
	function reload() {
		getTeamList($scope, $http);
		$('#newTeamName').focus();
	}

	$scope.newTeam = { name: "" };
	reload();
	
	$scope.createTeam = function() {
		if ($scope.newTeam.name.trim() !== "") {
			console.log("create team");
			$http.post('teams', $scope.newTeam).success(function(){
				console.log("Team created, updating list");
				$scope.newTeam = { name: "" };
				getTeamList($scope, $http);
				$('#newTeamName').focus();
			});
		} else {
			$('#newTeamName').addClass("error");
			$scope.lastError = "Team name must be set.";
			$('#saveerror').show();
		}
	};
	$scope.saveTeam = function(teamObject) {
		$http.put(teamObject._links.self.href, teamObject).success(function() {
			console.log("Team updated");
			reload();
		})
	};
	
	$scope.deleteTeam = function(teamObject) {
		if (checkUser(teamObject)) {
			console.log("delete Team");
			$http.delete(teamObject._links.self.href).success(function(){
				console.log("Team deleted, updating list");
				reload();
			});
		}
	};
	
});
