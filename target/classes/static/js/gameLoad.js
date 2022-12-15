window.onload = loadPage;

function loadPage() {
	getGameIDandLogin();
	connectToSocket(gameId);
	requestFullGame(); //Also requests Heroes if success (hero request depends on game)
}

//Script map for future:
// 56. function requestGame()
// 98. function loadHeroes(heroesSave)
// 124. function loadHand (hand)

function connectToSocket() {
    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) { // this function works when gets info form socket!
            let data = JSON.parse(response.body);
            console.log(data);
            switch (data.type) { //With response there is type of info from server.
				case "BOARD":
					heroesSave = data.info;
					loadHeroes(heroesSave);
				break;
				case "CARD_COUNT":
					cardCountSave = data.info;
					if (opponentLogin != undefined)
						console.log("Your opponent has " + cardCountSave[opponentLogin]);
					else console.log("no opponent now, but you have " + cardCountSave[login] + " cards");
					// Here must be function that loads card count of your opponent
				break;
			}
        })
    })
}

function requestHeroes() {
    $.ajax({
		url: url + "/heroes/get-heroes?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHeroes) {
			heroesSave = newHeroes;
			console.log(heroesSave);
			loadHeroes(heroesSave);
            console.log("Successfully loaded board");
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function requestFullGame() {
    $.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			gameSave = newGame;
			getOpponentLogin(gameSave);
			requestHand();
			requestHeroes();
			requestTurn();
        },
        error: function (error) {
            console.log("Game wasn't loaded!" + error);
        }
    })
}

function requestHand() {
    $.ajax({
        url: url + "/players/get-hand?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHand) {
			handSave = newHand;
            loadHand(handSave);
            if (gameSave.status == "NEW") chooseLeader();
        },
        error: function (error) {
            console.log("Hand wasn't loaded!" + error);
        }
    })
}

function requestTurn() {
    $.ajax({
        url: url + "/players/get-turn?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newTurn) {
			turnSave = newTurn;
        },
        error: function (error) {
            console.log("Hand wasn't loaded!" + error);
        }
    })
}

//load functions
function loadHeroes(heroesSave) {
	for (let x = 0; x < heroesSave.length; x++) {
		let i = heroesSave[x].coordX;
		let j = heroesSave[x].coordY;
		let id = i + "_" + j;
		let place;
		if (heroesSave[x].player.login == login) 
			place = document.getElementById("1_"+id);
		else place = document.getElementById("2_"+id);
		place.textContent = prepareName(heroesSave[x].card.name);
	}
	
	for (let i = 0; i < 3; i++) {
        for (let j = 0; j < 3; j++) {
            let id = i + "_" + j;
            let place = document.getElementById("1_"+id);
            if (i == gameSave.wave && place.textContent == "") {
				place.addEventListener('dragenter', dragEnter);
		    	place.addEventListener('dragover', dragOver);
	    		place.addEventListener('dragleave', dragLeave);
	    		place.addEventListener('drop', dragDrop);
			}
        }
    }
}

function loadHand (hand) {
	if (hand.length <= 5) {
		let handElement = document.getElementById("cardHolder");
		for (let i = 0; i < hand.length; i++) {
			addCardInHand(handElement, i);
		}		
	}
}

function reloadHand(data) {
	const cardsInHand = document.querySelectorAll('li[id ^= "hand"]');
	cardsInHand.forEach(card => {
    	card.remove();
	});
	loadHand(data);
}

//get functions
function getGameIDandLogin() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  		get: (searchParams, prop) => searchParams.get(prop),
	});
	gameId = params.id;
	login = params.login;
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent = login+", "+paragraph.textContent+gameId;
}

function getOpponentLogin(gameSave) {
	if (gameSave.players.length != 1) {//returns zero string, if no opponent now
		if (gameSave.players[0].login == login) opponentLogin = gameSave.players[1].login;
		else opponentLogin = gameSave.logins[0].login;
	}
}

//Another support function
//Creates background and table with two rows with 3 cards each to show player's first cards.
function chooseLeader(e) {
	let table = document.createElement("table");
	let tbody = document.createElement("tbody");
	
	let tr1 = document.createElement("tr");
	for (let i = 0; i < 3; i++) {
		let td = document.createElement("td");
		let img = document.createElement("img");
		img.classList.add("leaderChoise");
		img.classList.add("upsideDown");
		img.setAttribute('onclick', 'chooseLeader()');
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
		img.setAttribute('onclick', 'chooseLeader()');
		img.src = "images/Cards/" + handSave[i].name + ".png";
		img.id = i + "Leader";
		td.appendChild(img);
		tr2.appendChild(td);
	}
	
	tbody.appendChild(tr1);
	tbody.appendChild(tr2);
	table.id = "showLeaders";
	table.appendChild(tbody);
	
	//next paragraph is another function
	let blackBackground = document.createElement("div");
	//blackBackground.setAttribute('onclick', 'closeCard()'); Need to remake. hmmm maybe no need in this function at all.
	blackBackground.id = "BB";
	document.body.appendChild(blackBackground);
	
	document.body.appendChild(table);
}