// use strict

document.addEventListener('DOMContentLoaded', function() {
	function getTeamName() {
		return window.location.search.replace(/.*team=([^&]*.*)/, '$1');
	}
	function get(params) {
		var oReq = new XMLHttpRequest();
		oReq.open("GET", params.url);
		if (params.success) {
			oReq.addEventListener("load", function() {
				params.success(JSON.parse(oReq.responseText))
			});
		}
		if (params.error) {
			oReq.addEventListener("error", function() {
				params.error()
			});
		}
		oReq.send();
	}
	
	function getCurrentDay(sprint) {
		var currentDayStart = moment(sprint.startDate).startOf('day'),
			indizes = ["day01", "day02", "day03", "day04", "day05", "day06", "day07", "day08", "day09", "day10",
				"day11", "day12", "day13", "day14", "day15", "day16", "day17", "day18", "day19", "day20"],
			todayStart = moment(new Date()).startOf('day'),
			index = 0;
		
		while (currentDayStart.isBefore(todayStart) && index < indizes.length) {
			currentDayStart = currentDayStart.add(1, 'day');
			while (currentDayStart.format('e') == 0 || currentDayStart.format('e') == 6) {
				currentDayStart = currentDayStart.add(1, 'day');
			}
			index++;
		}
		return indizes[index];
	}
	ko.applyBindings(new (function() {
		var me = this;
		me.teamName = getTeamName();
		
		me.teamMembers = ko.observableArray();
		
		function fetchAvailability(teamName) {
			get({
				url: '/userBySprints/search/findCurrent?team=' + teamName,
				success: function(availability) {
					if (!(!availability || availability._embedded.userBySprints.length == 0)) {
						resolveLinkedValues(availability._embedded.userBySprints);
					}
				},
				error: function() {
					console.log('unable to fetch availibility')
				}
			})
		}

		function resolveLinkedValues(availabilities) {
			var todo = 1,
				result = {
					users: []
				};
			
			get({
				url: availabilities[0]._links.sprint.href,
				success: function(sprint) {
					result.sprint = sprint;
					todo--;
					if (todo == 0) {
						showResult(result);
					}
				}
			});
			availabilities.forEach(function(oneUser) {
				todo++;
				get({
					url: oneUser._links.user.href,
					success: function(user) {
						user.availability = oneUser;
						result.users.push(user);
						todo--;
						if (todo == 0) {
							showResult(result);
						}
					}
				})
			})
		}
		
		function formatAvailability(dbvalue) {
			if (dbvalue <= 1) {
				return parseInt(dbvalue * 100) + " %";
			}
			return "0%";
		}
		
		function showResult(result) {
			const currentDay = getCurrentDay(result.sprint);
			
			result.users.sort(function (u1, u2) {
				// var a1 = mapAvailabilityToSort(u1.availability[currentDay]);
				// var a2 = mapAvailabilityToSort(u2.availability[currentDay]);
				// return a2 > a1 ? 1 : a1 == a2 ? (u1.name > u2.name ? 1 : u1.name == u2.name ? 0 : -1) : -1;
				return u1.name > u2.name ? 1 : u1.name == u2.name ? 0 : -1;
			});
			result.users.forEach(function(user) {
				user.available = user.availability[currentDay] <= 1;
				user.availability = formatAvailability(user.availability[currentDay]);
			});

			me.teamMembers(result.users);
			
			// var inOffice = $('#inOffice'),
			// 	outOfOffice = $('#outOfOffice');
			// $.forEach(result.users, function(i, user) {
			// 	var target = user.availability[currentDay] > 0 && user.availability[currentDay] <= 1 ? inOffice : outOfOffice;
			// 	$('<tr class="user"><td class="name"></td><td class="availability"></td><td class="developer"></td></tr>')
			// 		.find('.name').text(user.name).end()
			// 		.find('.availability').text(formatAvailability(user.availability[currentDay])).end()
			// 		.find('.developer').text(user.developer).end()
			// 		.appendTo(target);
			// })
		}

		if (me.teamName) {
			fetchAvailability(me.teamName);
			window.setInterval(fetchAvailability, 120000, me.teamName);
		}
	})());
	
	getTeamName();
}, false);
