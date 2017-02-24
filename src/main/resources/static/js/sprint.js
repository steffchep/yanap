// use strict

document.addEventListener('DOMContentLoaded', function() {
	
	function getTeamName() {
		return window.location.search.replace(/.*team=([^&]*.*)/, '$1');
	}
	
	/**
	 * @param user
	 * @param availability
	 * @constructor
	 */
	function CalendarEntry(user, availability) {
		this["Type"] = user ? "Developers" : "Other";
		this["Persons Available"] = availability.availability || 0;
		this["Date"] = availability.day;
		this["Weekday"] = moment(availability.day).format('dd');
	}
	ko.applyBindings(new (function() {
		var me = this;
		me.teamName = getTeamName();
		
		me.sprint = ko.observable();
		me.availabilities = ko.observableArray();
		
		if (me.teamName) {
			update();
			window.setInterval(update, 300000);
		}
		
		function update() {
			fetchCurrentSprint(me.teamName, function(sprint) {
				fetchUsers(sprint, function(users) {
					var series = [],
						coordinator = new DoStuffParallel();
					
					users.forEach(function(user) {
						coordinator.submit(
							function(callback) {
								fetchAvailabilities(user, sprint.startDate, sprint.endDate, callback);
							},
							function(availabilities) {
								availabilities.forEach(function(availability) {
									series.push(new CalendarEntry(user, availability));
								})
							});
					});
					
					coordinator.onDone(function() {
						var currentDay = moment(sprint.startDate),
							endDay = moment(sprint.endDate);
						
						while (!currentDay.isAfter(endDay)) {
							series.push(new CalendarEntry({}, {
								day: currentDay.format('YYYY-MM-DD')
							}));
							currentDay.add(1, 'day');
							while (currentDay.format('e') == 0 || currentDay.format('e') == 6) {
								currentDay.add(1, 'day');
							}
						}
						me.availabilities(series);
					})
				});
			});
		}
		function fetchCurrentSprint(teamName, callback) {
			get({
				url: '/sprints/search/findCurrent?team={}',
				urlParams: [teamName],
				success: function(availability) {
					if (availability && availability._embedded.sprints.length > 0) {
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
				url: sprint._links.users.href,
				success: function(users) {
					if (users && users._embedded.users) {
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
				url: '/userAvailabilities/search/findByUserAndDayBetween?user={}&from={}&to={}',
				urlParams: [user._links.self.href, from, to],
				success: function(availability) {
					if (availability && availability._embedded.userAvailabilities) {
						callback(availability._embedded.userAvailabilities);
					}
				},
				error: function() {
					console.log('unable to fetch availibility')
				}
			})
			
		}
		
		me.availabilities.subscribe(function(data) {
			var node = document.getElementById('diagram');
			node.innerHTML = '';
			var svg = dimple.newSvg(node, 450, 170);
			var myChart = new dimple.chart(svg, data);
			myChart.setBounds(35, 15, '100%,-40px', '100%,-55px');
			var x = myChart.addCategoryAxis('x', 'Date');
			x.addOrderRule('Date');
			var y = myChart.addMeasureAxis('y', 'Persons Available');
			y.ticks = 5;
			y.tickFormat = 'r';
			myChart.addSeries("Type", dimple.plot.bar);
			myChart.addLegend(0, 3, '100%,-4px', 10, "right");
			myChart.draw();
			x.shapes.selectAll("text")
				.text(function (d) {
					for (var i = 0; i < data.length; i++) {
						if (data[i]["Date"] == d) {
							return data[i]["Weekday"];
						}
					}
				})
				.attr('transform', '');
		});
	})());
	
}, false);
