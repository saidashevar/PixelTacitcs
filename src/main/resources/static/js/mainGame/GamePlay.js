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

// Loading functions
//
//
function loadAvailablePlaces(places) {
	for (let x = 0; x < places.length; x++)	{
		//Get coordinates
		let i = places[x].coordX;
		let j = places[x].coordY;
		let id = i + "_" + j;
		
		let place = document.getElementById("1_"+id); //This means, we may hire only in our squad. it may be not like that for some future heroes.
		
		place.classList.add('readyToDrop');
		place.addEventListener('dragenter', dragEnter);
		place.addEventListener('dragover', dragOver);
	    place.addEventListener('dragleave', dragLeave);
	    place.addEventListener('drop', dragDrop);
	}
}

function loadAvailableTargets(heroes) {
	for (let x = 0; x < heroes.length; x++)	{
		//Get coordinates
		let i = heroes[x].coordX;
		let j = heroes[x].coordY;
		let id = i + "_" + j;
		//Get place with coordinate
		console.log("2_" + id);
		let hero = document.getElementById("2_"+id);
		
		hero.classList.add('readyToDrop');
		hero.addEventListener('dragenter', dragEnter);
		hero.addEventListener('dragover', dragOver);
	    hero.addEventListener('dragleave', dragLeave);
	    hero.addEventListener('drop', dragMeleeAttacked);
	}
}