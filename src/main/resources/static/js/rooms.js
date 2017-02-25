/**
 * @typedef {object} Seat
 * @property {string} name
 * @property {number} x
 * @property {number} y
 */

/**
 * @param target
 * @param {Array<Seat>} seats
 */
function showUsers(target, seats) {
	function drawIt() {
		var root = target.hasOwnProperty('addElement')
				? target
				: document.querySelector(target),
			userData = {},
			coordinator = new DoStuffParallel();
		const TEMPLATE = document.getElementById("template").innerHTML;

		function unknownAvailability() {
			return {
				day: moment().format('YYYY-MM-DD'),
				presence: 'unknown',
				availability: 0,
				planned: false
			};
		}

		seats.forEach(function(seat) {
			if (!userData[seat.name]) {
				coordinator.submit(
					function(resultCallback) {
						get({
							url: "/users/search/findByName?name={}",
							urlParams: [seat.name],
							success: unwrap("users", resultCallback),
							error: console.log
						});
					},
					function(usersByName) {
						userData[seat.name] = {
							user: usersByName[0] || {name: seat.name},
							availability: ko.observable(unknownAvailability())
						};
					});
			}
			render(seat)
		});

		coordinator.onDone(function() {
			ko.applyBindings(new (function() {
				this.userData = userData;
				function update() {
					var today = moment().format("YYYY-MM-DD");
					for (var name in userData) if (userData.hasOwnProperty(name)) (function() {
						var myName = name;
						if (userData[myName].user._links) {
							get({
								url: "/userAvailabilities/search/findByUserAndDay?user={}&day={}",
								urlParams: [userData[myName].user._links.self.href, today],
								success: userData[myName].availability,
								error: function() {
									console.log('trouble loading availability for user ' + myName);
									userData[myName].availability(unknownAvailability());
								}
							});
						}
					})();
				}
				update();
				setTimeout(update, 60000);
			})(), root);
		});

		/**
		 * @param {Seat} seat
		 */
		function render(seat) {
			if (!seat.name || !seat.name.match(/[a-zA-Z0-9_\-+]+/)) {
				console.log("can't handle name " + seat.name);
				return;
			}
			var div = document.createElement('div');
			div.innerHTML = TEMPLATE.replace(/\{userdata}/gi, "userData['" + seat.name + "']");
			div.className = 'person';
			var abs = false;
			if (seat.hasOwnProperty('x')) {
				div.style.left = seat.x;
				abs = true;
			}
			if (seat.hasOwnProperty('y')) {
				div.style.top = seat.y;
				abs = true;
			}
			if (abs) {
				div.style.position = "absolute";
			}
			root.appendChild(div);
		}
	}


	if (document.readyState == 'complete') {
		drawIt();
	} else {
		document.addEventListener('DOMContentLoaded', drawIt);
	}
}