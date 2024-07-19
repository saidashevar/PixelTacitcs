window.onload = loadPage;
var waitTime = 100;

function sleep(ms) {
	return new Promise(resolve => setTimeout(resolve, ms));
}

function loadPage() {
	getGameIDandLogin(); //sync function
	requestFullGame();   //async function
}

function connectToSocket(fun) {
	//I don't know where or when this function start... It doesn't matter for now.
    console.log("connecting to socket");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) { 
			// this function works when gets info form socket!
        	//In this function we get messages from server whenever server wants to warn us about something
        	//Here you get almost ALL updatable info: your hand, number of cards in your opponent's hand, his and your number of actions
        	//Even Hp and effects of heroes may be sent here, you may not find some special functions anywhere how to update this information
        	//Workssss strangely sometimes, be careful with this
        	
            let data = JSON.parse(response.body);
            console.log(data); //logging everything right now
            
            reloadSwordAndShield(); //if we get some info from server, whatever we get we will update shield and sword
            
            switch (data.type) { // With response there is type of info from server.
            
				case "HEROES": //we wanna update info about our heroes, their hp, buffs and we need to know if they are dead
					heroesSave = data.info;
					cleanBoard(function () {
						//I am using nested functions, because I need to compile them in that exact order.
						//I don't know how to do it other way. 
						loadHeroes();
						loadLeaders(); 
					});
				break;
				case "LEADERS": //we wanna update info about our leaders, their hp, buffs and we need to know if they are dead
					console.log("Leaders came");
					cleanBoard(function () {
						loadHeroes();
						loadLeaders(); 
					});
				break;
				case "CARD_COUNT": //non-updatable through other functions!!! Non-requirable
					cardCountSave = data.info;
					document.getElementById("cardCounter").textContent = cardCountSave[opponentSave.login];
				break;
				case "ACTIONS_COUNT": //non-updatable through other functions!!! 
					actionsCountSave = data.info;
					document.getElementById("actionsCounter").textContent = actionsCountSave[youSave.login] - actionsCountSave[opponentSave.login];
				break;
				case "STATUS": // actually requires client to update this whole game... i don't remember where it is used
					requestGame(checkStatus);
				break;
				case "TURNS":
					console.log("got turns! LOOk at them:");
					console.log(data.info);
					//youSave = data.info[]
					reloadSwordAndShield();
				break;
			}
        });
        if(gameSave.status == "PEACE") requestBoardActionsCards();
        if (fun != undefined) fun();
    })
}

function getPlayers() { 
	//Loads both players, or only your, if second hasn't connected yet.
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
	
	console.log("got info about players from initialization!");
	console.log(opponentSave);
	console.log(youSave);
}

//Another support function


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
		case "CHOOSING_LEADERS": //Both players connected and no one has chosen leader
			let leaders = document.getElementById("showLeaders");
			if(leaders == undefined)
				requestLeader(function() { requestHand(showLeaders); });
		break;
		case "CHOOSING_LEADERS_1LEADER_CHOSEN":
			requestLeader(function() { 
				if(leaderSave != "") {// this case you have chosen leader but your opponent hasn't
					removeBackgroundAndTextAndLeaders();		
					addBackgroundAndText("Your opponent chooses leader...");					
				}
				else //that case you haven't chosen leader, but your opponent had chosen
					requestHand(showLeaders); //that's a bug!!!!
			}); 
		break;
		case "PEACE":
			removeBackgroundAndTextAndLeaders();
			requestHand(reloadHand);
			requestHeroes();
			loadLeaders();
			reloadSwordAndShield();
		break;
		default: alert("something went wrong with status packages");
	}		
}