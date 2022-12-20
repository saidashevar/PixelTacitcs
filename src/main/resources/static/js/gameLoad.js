window.onload = loadPage;

function loadPage() {
	getGameIDandLogin();
	connectToSocket(gameId);
	requestFullGame();
}

//Script map for future:
// 61. function requestFullGame()
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
					document.getElementById("cardCounter").textContent = cardCountSave;
				break;
				case "STATUS":
					switch (data.info.status) {
						case "NO2PLAYER":
							let waitForAnybodyText = document.createElement("div");
							waitForAnybodyText.textContent = "Waiting for someone to play with...";
							waitForAnybodyText.id = "waitOpponentText";
							document.body.appendChild(waitForAnybodyText);
						break;
						case "CHOOSING_LEADERS":
							gameSave = data.info;
							let waitOpponentText = document.getElementById("waitOpponentText"); 
							if (waitOpponentText.textContent == "Waiting for someone to play with...") {
								getOpponent(gameSave);
								waitOpponentText.textContent = opponentSave.login + " chooses leader...";
							}
						break;
						case "PEACE":
							gameSave = data.info;
							getOpponent(gameSave);
							removeBackgroundAndText();
							requestHand(handSave);
						break;
						default: alert("something went wrong with status packages");
					}
				break;
			}
        })
    })
}

function requestFullGame() {
    $.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			console.log(newGame);
			gameSave = newGame;
			getOpponent(gameSave);
			
			switch (gameSave.status) {
				case "NO2PLAYER":
					requestHandSpecial(showLeaders);
				break;
				case "NO2PLAYER_1LEADER_CHOSEN":
					requestLeader();
				break;
				case "CHOOSING_LEADERS":
					requestHandSpecial(showLeaders);
				break;
				case "CHOOSING_LEADERS_1LEADER_CHOSEN":
					requestLeader();
				break;
				default:
					alert("this case of game status wasn't expected. You have caught a bug");
			}
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
            //loadHand(handSave);
        },
        error: function (error) {
            console.log("Hand wasn't loaded!" + error);
        }
    })
}

function requestHandSpecial(fun) {
	$.ajax({
        url: url + "/players/get-hand?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHand) {
			handSave = newHand;
			fun();
            //loadHand(handSave);
        },
        error: function (error) {
            console.log("Hand wasn't loaded!" + error);
        }
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

function requestTurn() {
    $.ajax({
        url: url + "/players/get-turn?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newTurn) {
			turnSave = newTurn;
        },
        error: function (error) {
            console.log("Turn of one player wasn't loaded!" + error);
        }
    })
}

function requestLeader() {
    $.ajax({
        url: url + "/players/get-leader?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (leader) {
			console.log("found leader:" + leader);
			if (leader == null) requestHandSpecial(showLeaders());
			else {
				requestHand();
				requestHeroes();
				requestTurn();
			}
        },
        error: function (error) {
            console.log("Leader is broken! Nerf him!" + error);
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

//support function
function getGameIDandLogin() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  		get: (searchParams, prop) => searchParams.get(prop),
	});
	gameId = params.id;
	login = params.login;
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent = login+", "+paragraph.textContent+gameId;
}

//Support function
function getOpponent(gameSave) { //loads both players, if they exist
	if (gameSave.players.length != 1) {
		if (gameSave.players[0].login == login) {
			opponentSave = gameSave.players[1];
			youSave = gameSave.players[0];
		} 
		else {
			opponentSave = gameSave.players[0];
			youSave = gameSave.players[1];	
		}
	} else youSave = gameSave.players[0];
	console.log(opponentSave);
	console.log(youSave);
}

//Another support function
//Creates background and table with two rows with 3 cards each to show player's first cards.
//And this function is too big
function showLeaders(e) {
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

function addBackground() {
	let blackBackground = document.createElement("div");
	blackBackground.id = "BB";
	document.body.appendChild(blackBackground);
}

function removeBackgroundAndText() {
	let background = document.getElementById("BB");
	let text1 = document.getElementById("waitOpponentText");
	if (background != undefined) document.body.removeChild(background);
	if (text1 != undefined) document.body.removeChild(text1);
}