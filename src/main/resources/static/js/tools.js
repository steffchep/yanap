
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
	}
	this.onDone = function(callback) {
		onDone = callback;
		if (todos == 0) {
			onDone();
		}
	}
}
	