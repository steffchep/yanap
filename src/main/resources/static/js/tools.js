
function get(params) {
	var oReq = new XMLHttpRequest(),
		url = params.url,
		urlParams = (params.urlParams || []).slice(0);
	for (var pos = url.indexOf('{}'); pos >= 0; pos = url.indexOf('{}', pos)) {
		var param = String(urlParams.shift());
		url = url.substring(0, pos) + encodeURIComponent(param) + url.substring(pos + 2);
		pos += param.length;
	}
	
	oReq.open('GET', url);
	if (params.success) {
		oReq.onloadend = function() {
			if (oReq.status >= 200 && oReq.status < 300) {
				params.success(JSON.parse(oReq.responseText))
			} else if (params.error) {
				params.error();
			}
		};
	}
	if (params.error) {
		oReq.onerror = params.error;
	}
	try {
		oReq.send();
	} catch (err) {
		console.log(err);
	}
}

function DoStuffParallel() {
	var todos = 0,
		onDone = null;
	
	this.submit = function(task, resultCallback) {
		todos ++;
		task(function(result) {
			todos --;
			resultCallback(result);
			if (todos == 0 && onDone != null) {
				onDone();
			}
		})
	};
	this.onDone = function(callback) {
		onDone = callback;
		if (todos == 0) {
			onDone();
		}
	}
}

function getParameter(paramName) {
	return window.location.search.replace(new RegExp(".*" + paramName + "=([^&]*.*)"), '$1');
}

function unwrap(name, callback) {
	return function(result) {
		if (result && result["_embedded"] && result["_embedded"][name]) {
			callback(result["_embedded"][name]);
		} else {
			console.log("found no " + name + " in response");
			callback([]);
		}
	};
}
