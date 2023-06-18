// Request functions
//
//This function requests from servers places, where we may hire our heroes
function requestAvailablePlaces(fun) {
	$.ajax({
        url: url + "/players/get-places?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (data) {
			loadAvailablePlaces(data);
			if (fun != undefined) fun(); //yeah, this may be useless
        },
        error: function (error) {
            console.log("We don't know where you can hire heroes, sorry" + error);
        }
    })
}

function requestAvailableTargets() {
	$.ajax({
        url: url + "/players/get-targets?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (heroes) {
			loadAvailableTargets(heroes);
        },
        error: function (error) {
            console.log("This hero's attack is pointed on server and it has crushed: " + error);
        }
    })
}