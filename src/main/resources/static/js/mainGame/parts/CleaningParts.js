function cleanSwordAndShield () {
	console.log("removin shield and sword images");
	for (let x = 1; x <= 2; x++) {
		for(let y = 0; y < 3; y++) {
			let div = document.getElementById("0_" + x + "_" + y).firstChild;
			if (div != undefined) div.remove();
		}
	}
}

function cleanBoard(fun) { //clears all board table and then reloads heroes
	for (let squad = 1; squad < 2; squad++) {
		for (let i = 0; i < 2; i++){
			for (let j = 0; j < 2; j++){
				let place = document.getElementById(squad + "_" + i + "_" + j);
				place.innerHTML = '';
			}
		}
	}
	if (fun != undefined) fun();
}

