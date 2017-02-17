// use strict

document.addEventListener('DOMContentLoaded', function() {
	function getTeamName() {
		return window.location.search.replace(/.*team=([^&]*.*)/, '$1');
	}
	function get(params) {
		var oReq = new XMLHttpRequest();
		oReq.open('GET', params.url);
		if (params.success) {
			oReq.addEventListener('load', function() {
				params.success(JSON.parse(oReq.responseText))
			});
		}
		if (params.error) {
			oReq.addEventListener('error', function() {
				params.error()
			});
		}
		oReq.send();
	}
	
	const days = ['day01', 'day02', 'day03', 'day04', 'day05', 'day06', 'day07', 'day08', 'day09', 'day10',
		'day11', 'day12', 'day13', 'day14', 'day15', 'day16', 'day17', 'day18', 'day19', 'day20'];
	ko.applyBindings(new (function() {
		var me = this;
		this.teamName = getTeamName();
		
		this.sprint = ko.observable();
		this.availabilities = ko.observableArray();
		
		function fetchCurrentSprint(teamName, callback) {
			get({
				url: '/sprints/search/findCurrent?team=' + teamName,
				success: function(availability) {
					if (!(!availability || availability._embedded.sprints.length == 0)) {
						callback(availability._embedded.sprints[0]);
					}
				},
				error: function() {
					console.log('unable to fetch sprint')
				}
			})
		}
		
		function fetchUsers(sprint, callback) {
			get({
				url: sprint._links.users,
				success: function(users) {
					if (!(!users || users._embedded.users.length == 0)) {
						callback(users._embedded.users);
					}
				},
				error: function() {
					console.log('unable to fetch users')
				}
			})
		}

		function fetchAvailabilities(user, from, to, callback) {
			get({
				url: '/userAvailability/search/findByUserAndDayBetween?user=' + user._links.self + '&from=' + from + '&to=' + to,
				success: function(availability) {
					if (!(!availability || availability._embedded.sprints.length == 0)) {
						callback(availability._embedded.sprints[0]);
					}
				},
				error: function() {
					console.log('unable to fetch availibility')
				}
			})
			
		}

		function resolveLinkedValues(availabilities) {
			get({
				url: availabilities[0]._links.sprint.href,
				success: function(sprint) {
					showResult({
						sprint: sprint,
						availabilities: availabilities
					});
				}
			});
		}
		
		function showResult(result) {
			var total = {};
			result.availabilities.forEach(function(user) {
				days.forEach(function(day) {
					var userToday = user[day];
					total[day] = (total[day] || 0) + (userToday <= 1 ? userToday : 0);
				});
			});
			var percentages = [],
				sprintStart = moment(result.sprint.startDate),
				sprintEnd = moment(result.sprint.endDate),
				weekendCounter = 0;
			for (var dayIndex = 0; dayIndex < days.length; dayIndex++) {
				var day = sprintStart.clone().add(dayIndex + weekendCounter, 'days');
				while (day.format('e') == 0 || day.format('e') == 6) {
					day.add(1, 'day');
					weekendCounter++;
				}
				if (day.isAfter(sprintEnd)) {
					break;
				}
				
				percentages.push({
					index: dayIndex,
					Day: day.format('dd DD'),
					Total: total[days[dayIndex]],
					Percent: total[days[dayIndex]] / result.availabilities.length * 100
				});
			}
			days.forEach(function(day) {
			});
			me.availabilities(percentages);
		}

		if (me.teamName) {
			fetchAvailability(me.teamName);
			window.setInterval(fetchAvailability, 300000, me.teamName);
		}
		
		me.availabilities.subscribe(function(data) {
			var node = document.getElementById('diagram');
			node.innerHTML = '';
			var svg = dimple.newSvg(node, 450, 170);
			var myChart = new dimple.chart(svg, data);
			myChart.setBounds(20, 10, '100%,-10px', '100%,-50px');
			var x = myChart.addCategoryAxis('x', 'Day');
			x.addOrderRule('index');
			myChart.addMeasureAxis('y', 'Percent');
			myChart.addSeries(null, dimple.plot.line);
			myChart.draw();
		});
	})());
	
	getTeamName();
}, false);
