// use strict

document.addEventListener('DOMContentLoaded', function() {
	ko.applyBindings(new (function() {
		var me = this;
		
		function AvailabilityEntry(user, availability) {
			this.name = user.name;
			this.developer = user.developer;
			this.inOffice = availability.presence == 'IN_OFFICE';
			this.availability = availability.availability || 0;
			this.availabilityPercent = (this.availability * 100) + "%";
		}
		
		me.teamName = getParameter("team");
		
		me.teamMembers = ko.observableArray();
		
		if (me.teamName) {
			update();
			window.setInterval(update, 300000);
		}
		
		function update() {
			fetchSprint(me.teamName, function(sprint) {
				fetchUsers(sprint, function(users) {
					fetchAvailabilities(users, function(results) {
						me.teamMembers(results);
					});
				})
			})
		}
		
		function fetchSprint(teamName, callback) {
			get({
				url: '/sprints/search/findCurrent?team={}',
				urlParams: [teamName],
				success: unwrap("sprints", function(sprints) {
					if (sprints.length > 0) {
						callback(sprints[0]);
					} else {
						console.log("found no current sprint for team " + teamName);
					}
				}),
				error: function() {
					console.log('unable to fetch current Sprint')
				}
			})
		}
		
		function fetchUsers(sprint, callback) {
			get({
				url: sprint._links.users.href,
				success: unwrap("users", callback),
				error: function() {
					console.log('unable to fetch current Sprint')
				}
			})
		}
		
		function fetchAvailabilities(users, callback) {
			var coordinator = new DoStuffParallel(),
				entries = [],
				today = moment().format("YYYY-MM-DD");
			users.forEach(function(user) {
				coordinator.submit(
					function(callback) {
						get({
							url: "/userAvailabilities/search/findByUserAndDay?user={}&day={}",
							urlParams: [user._links.self.href, today],
							success: callback,
							error: callback
						});
					},
					function (availability) {
						entries.push(new AvailabilityEntry(user, availability || {}));
					});
			});
			coordinator.onDone(function() {
				entries.sort(function(e1, e2) {
					return e1.name < e2.name ? -1 : e1.name == e2.name ? 0 : 1;
				});
				callback(entries)
			});
		}
		
	})());
	
}, false);
