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
	
	const days = ["day01", "day02", "day03", "day04", "day05", "day06", "day07", "day08", "day09", "day10",
		"day11", "day12", "day13", "day14", "day15", "day16", "day17", "day18", "day19", "day20"];
	function getCurrentDayIndex(sprint) {
		var currentDayStart = moment(sprint.startDate).startOf('day'),
			todayStart = moment(new Date()).startOf('day'),
			index = 0;
		
		while (currentDayStart.isBefore(todayStart) && index < days.length) {
			currentDayStart = currentDayStart.add(1, 'day');
			while (currentDayStart.format('e') == 0 || currentDayStart.format('e') == 6) {
				currentDayStart = currentDayStart.add(1, 'day');
			}
			index++;
		}
		return index;
	}
	ko.applyBindings(new (function() {
		var me = this;
		this.teamName = getTeamName();
		
		this.sprint = ko.observable();
		this.availabilities = ko.observableArray();
		
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
				sprintEnd = moment(result.sprint.endDate);
			for (var dayIndex = 0; dayIndex < days.length; dayIndex++) {
				var day = sprintStart.clone().add(dayIndex, 'days');
				if (day.isAfter(sprintEnd)) {
					break;
				}
				percentages.push({
					index: dayIndex,
					Day: day.format('dd\\n DD'),
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
		}
		
		me.availabilities.subscribe(function(data) {
			var svg = dimple.newSvg("#diagram", 450, 190);
			var myChart = new dimple.chart(svg, data);
			myChart.setBounds(20, 10, "100%,-10px", "100%,-58px");
			var x = myChart.addCategoryAxis("x", "Day");
			x.addOrderRule("index");
			myChart.addMeasureAxis("y", "Percent");
			var s = myChart.addSeries(null, dimple.plot.line);
			myChart.draw();
			// x.shapes.selectAll('text').attr('transform',
			// 	function () {
			// 		var transformAttributeValue = d3.select(this).attr('transform');
			//
			// 		if (transformAttributeValue) {
			// 			transformAttributeValue = transformAttributeValue.replace('rotate(90,', 'rotate(45,');
			// 		}
			//
			// 		return transformAttributeValue;
			// 	});
		});
	})());
	
	getTeamName();
}, false);
