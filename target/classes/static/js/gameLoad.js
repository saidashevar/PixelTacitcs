window.onload = loadPage;

function loadPage() {
	getGameIDandLogin();
	connectToSocket(gameId);
	requestHand();
	requestTurn();
	requestGame(); //Also requests Heroes if success (hero request depends on game)
}

function connectToSocket() {
    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) { // this function works when gets info form socket!
            let data = JSON.parse(response.body);
            console.log(data);
            switch (data.type) {
				case "BOARD":
					heroesSave = data.info;
					loadHeroes(heroesSave);
				break;
				case "CARD_COUNT":
					cardCountSave = data.info;
					if (opponentLogin != undefined)
						console.log("Your opponent has " + cardCountSave[opponentLogin]);
					else console.log("no opponent now, but you have " + cardCountSave[login] + " cards");
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

function requestGame() {
    $.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			gameSave = newGame;
			getOpponentLogin(gameSave);
			requestHeroes();
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