//This script about starting of the game, when players connect to the game and should choose their leaders.


//Creates background and table with two rows with 3 cards each to show player's first cards.
//And this function is too big
function showLeaders() {
	let table = document.createElement("table");
	let tbody = document.createElement("tbody");
	
	let tr1 = document.createElement("tr");
	for (let i = 0; i < 3; i++) {
		let td = document.createElement("td");
		let img = document.createElement("img");
		img.classList.add("leaderChoise");
		img.classList.add("upsideDown");
		img.addEventListener('click', chooseLeader);
		img.src = "images/Cards/" + handSave[i].name + ".png";
		img.id = i + "Leader";
		td.appendChild(img);
		tr1.appendChild(td);
	}
	
	let tr2 = document.createElement("tr");
	for (let i = 3; i < 6; i++) {
		let td = document.createElement("td");
		let img = document.createElement("img");
		img.classList.add("leaderChoise");
		img.classList.add("upsideDown");
		img.addEventListener('click', chooseLeader);
		img.src = "images/Cards/" + handSave[i].name + ".png";
		img.id = i + "Leader";
		td.appendChild(img);
		tr2.appendChild(td);
	}
	
	tbody.appendChild(tr1);
	tbody.appendChild(tr2);
	table.id = "showLeaders";
	table.appendChild(tbody);
	
	document.body.appendChild(table);
	addBackground();
}

function chooseLeader (e) {
	let chosenCard = e.target.id.split("")[0];
	let cardId = handSave[chosenCard].id;
	$.ajax({
        url: url + "/heroes/hire-leader",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "login": login,
            "coordinateY": 2,
            "cardId": cardId
        }),
        success: function (newHand) {
			removeBackgroundAndTextAndLeaders();
			handSave = newHand;
			reloadHand(handSave);
        },
        error: function (error) {
            console.log(error);
        }
    })
}