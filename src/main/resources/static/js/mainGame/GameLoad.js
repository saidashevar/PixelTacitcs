//this script loads game when page loads
window.onload = loadPage;

function loadPage() {
	getGameIDandLogin();
	connectToSocket(gameId);
	requestFullGame();
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
            switch (data.type) { // With response there is type of info from server.
				case "BOARD":
					heroesSave = data.info;
					loadHeroes();
				break;
				case "CARD_COUNT":
					cardCountSave = data.info;
					document.getElementById("cardCounter").textContent = cardCountSave[opponentSave.login];
				break;
				case "ACTIONS_COUNT":
					actionsCountSave = data.info;
					document.getElementById("actionsCounter").textContent = actionsCountSave[youSave.login] - actionsCountSave[opponentSave.login];
				break;
				case "STATUS":
					requestGame(checkStatus);
				break;
			}
        })
    })
}

async function requestFullGame() {
    $.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			console.log(newGame);
			gameSave = newGame;
			getOpponent();
			requestLeader(checkStatus);
			reloadTurns();
			
			//Next functions are... useless for now
			requestHeroes(function() {
				loadHeroes();
			});
        },
        error: function (error) {
            console.log("Game wasn't loaded!" + error);
        }
    })
}

function requestGame(fun) {
	$.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			console.log("Requested game: " + newGame);
			gameSave = newGame;
			getOpponent();
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Game wasn't loaded!" + error);
        }
    })
}

function requestHand(fun) {
	$.ajax({
        url: url + "/players/get-hand?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHand) {
			handSave = newHand;
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Hand wasn't loaded!" + error);
        }
    })
}

function requestHeroes(fun) {
    $.ajax({
		url: url + "/heroes/get-heroes?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHeroes) {
			heroesSave = newHeroes;
			if (fun != undefined) fun();
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

function requestLeader(fun) { //this smart function can show leaders or hide them.
    $.ajax({
        url: url + "/players/get-leader?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (leader) {
			leaderSave = leader;
			console.log("Your leader is: " + leaderSave);
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Leader is broken! Nerf him!" + error);
        }
    })
}

//load functions
function loadHeroes(fun) {
	for (let x = 0; x < heroesSave.length; x++) {
		//get id and knoledge which squad this hero belongs to
		let i = heroesSave[x].coordX;
		let j = heroesSave[x].coordY;
		let id = i + "_" + j;
		let isYour = heroesSave[x].player.login == login;
		
		//find place where hero was hired
		let place;
		console.log(heroesSave[x]);
		if (isYour)	place = document.getElementById("1_"+id);
		else 		place = document.getElementById("2_"+id);
		
		if (checkEffect(x, "defeated") == 1) {
			//in case this hero is already defeated all we have to know is where his corpse lies
			place.innerHTML = '';
			if (isYour) {
				if (youSave.red) place.appendChild(createDiv(createCardImage(redSrc)));
				else place.appendChild(createDiv(createCardImage(blueSrc)));
			} else {
				if (opponentSave.red) place.appendChild(createDiv(createCardImage(redSrc)));
				else place.appendChild(createDiv(createCardImage(blueSrc)));
			}
		} else {
			//print hero's name, health and attack
			place.textContent = prepareName(heroesSave[x].name);
			place.appendChild(prepareToShow_HeroAttack('attack', x));
			place.appendChild(prepareToShow_HeroHealth('maxHealth', x));
			
			//This is for attacking
			//you can drag your hero to attack other heroes
			place.setAttribute("draggable", "true");
			place.addEventListener('dragstart', onAttackStart);
			place.addEventListener('dragend', onDragEnd); //this removes borders from other heroes, who are able to be attacked	
		}
	}
	if (fun != undefined) fun();
}

function loadHand () {
	if (handSave.length <= 5) {
		let handElement = document.getElementById("cardHolder");
		for (let i = 0; i < handSave.length; i++) {
			addCardInHand(handElement, i);
		}		
	}
}

function reloadHand() {
	const cardsInHand = document.querySelectorAll('li[id ^= "hand"]');
	cardsInHand.forEach(card => {
    	card.remove();
	});
	loadHand();
}

function loadLeaders() { //This methos also loads images for decks
	switch (gameSave.status) {
		case "PEACE":
			let oppLeaderDiv = document.getElementById("2_1_1"); 
			let yourLeaderDiv = document.getElementById("1_1_1");
			let oppDeck = document.getElementById("deckPlayer2");
			let yourDeck = document.getElementById("deckPlayer1");
			
			if (youSave.red == true) {
				oppLeaderDiv.appendChild(createDiv(createCardImage(blueSrc)));
				oppDeck.appendChild(createCardImage(blueSrc));
				yourLeaderDiv.appendChild(createDiv(createCardImage(redSrc)));
				yourDeck.appendChild(createCardImage(redSrc));
			} else {
				oppLeaderDiv.appendChild(createDiv(createCardImage(redSrc)));
				oppDeck.appendChild(createCardImage(redSrc));
				yourLeaderDiv.appendChild(createDiv(createCardImage(blueSrc)));
				yourDeck.appendChild(createCardImage(blueSrc));
			}
		break;
		case "":
		break;
	}
}

function loadTurns() {
	loadTurnImage(youSave);
	if (opponentSave != undefined) loadTurnImage(opponentSave);
}

function reloadTurns() {
	for (let x = 1; x <= 2; x++) {
		for(let y = 0; y < 3; y++) {
			let div = document.getElementById("0_" + x + "_" + y).firstChild;
			if (div != undefined) div.remove();
		}
	}
	loadTurns();
} //later

//support function
function loadTurnImage(player) { //This function loads image of sword and shield to show wave and turn order of players.
	let i = 2;
	if (player.login == login) i = 1;
	
	if (player.turn.attacking == true) { //maybe i have to avoid such contructions but i don't know how to do this.
		if (player.turn.actionsLeft == 0) {
			if (player.turn.wave == 2) {
				turnDiv = document.getElementById("0_"+i+"_2");
				turnDiv.appendChild(createDiv(createCardImage(actionsSrc)));	
			} else {
				turnDiv = document.getElementById("0_"+i+"_" + (player.turn.wave+1));
				turnDiv.appendChild(createDiv(createCardImage(firstSrc)));
			}
		} else {
			youTurnDiv = document.getElementById("0_"+i+"_" + player.turn.wave);
			youTurnDiv.appendChild(createDiv(createCardImage(firstSrc)));
		}
	} else {
		youTurnDiv = document.getElementById("0_"+i+"_" + player.turn.wave);
		youTurnDiv.appendChild(createDiv(createCardImage(secondSrc)));
	}
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
function getOpponent() { //loads both players, if they exist
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
			//reloadHand(handSave);
        },
        error: function (error) {
            console.log(error);
        }
    })
}



//Another support function
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

function addBackground() {
	let blackBackground = document.createElement("div");
	blackBackground.id = "BB";
	document.body.appendChild(blackBackground);
}

function addWaitText(text) {
	let waitForAnybodyText = document.createElement("div");
	waitForAnybodyText.textContent = text;
	waitForAnybodyText.id = "waitOpponentText";
	document.body.appendChild(waitForAnybodyText);	
}

function addBackgroundAndText(text) {
	addBackground();
	addWaitText(text);
}

function removeBackgroundAndTextAndLeaders() {
	let background = document.getElementById("BB");
	let text1 = document.getElementById("waitOpponentText");
	let leaders = document.getElementById("showLeaders");
	if (background != undefined) document.body.removeChild(background);
	if (text1 != undefined) document.body.removeChild(text1);
	if (leaders != undefined) document.body.removeChild(leaders);
}

function createCardImage(src) {
	let img = document.createElement("img");
	img.src = src;
	img.classList.add("fill");
	return img;
}

function createDiv(img) {
	let div = document.createElement("div");
	div.appendChild(img);
	return div;
}

function checkStatus() { //this works very bad, when you enter new game with used login. 
						 //Probably i should make account system later
						 //Bug! When Player 1 starts game and player 2 connects to it and chooses leader before player 1 does the same, player 1 gets 12 cards at the start of the game
	//Here we should remember that this function is called when:
	//	1. Page loads first time
	//	2. Game Status changed with message 
	switch (gameSave.status) {
		case "NO2PLAYER":
			requestHand(showLeaders);
		break;
		case "NO2PLAYER_1LEADER_CHOSEN": //Player has started game and chosen leader, but no second player
			addBackgroundAndText("Waiting for your opponent...");
		break;
		case "CHOOSING_LEADERS":
			let leaders = document.getElementById("showLeaders");
			if(leaders == undefined)
				requestLeader(function() { requestHand(showLeaders); });
		break;
		case "CHOOSING_LEADERS_1LEADER_CHOSEN":
			requestLeader(function() {
				if(leaderSave != "") {
					alert("trying to change text");
					removeBackgroundAndTextAndLeaders();		
					addBackgroundAndText("Your opponent chooses leader...");					
				}
				else
					requestHand(showLeaders);
			}); 
		break;
		case "PEACE":
			removeBackgroundAndTextAndLeaders();
			requestHand(reloadHand);
			requestHeroes();
			loadLeaders();
		break;
		default: alert("something went wrong with status packages");
	}		
}