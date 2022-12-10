window.onload = loadPage;

function loadPage() {
	getGameIDandLogin();
	connectToSocket(gameId);
}

//request board
function connectToSocket() {
    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            lastGameSave = data;
            gameStatus = data.status;
            if (gameStatus != "NEW") requestBoard();
            console.log(data);
        })
    })
}

function requestBoard() {
    $.ajax({
        url: url + "/games/loadgame",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
			"string": gameId	
		}),
        success: function (data) {
			lastGameSave = data;
			loadBoard(data);
            loadHand(data);
            console.log("Successfully loaded board")
        },
        error: function (error) {
            console.log(error);
        }
    })
}

//load functions
function loadBoard(data) {
	let opponent = getOpponentLogin(data);
	for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            let place = document.getElementById("1_"+id); 
            if (data.players[login].board[i-1][j-1] === null) place.textContent = "";
            else place.textContent = prepareName(data.players[login].board[i-1][j-1].name);
            if (data.wave+1 == i) {
				place.addEventListener('dragenter', dragEnter);
		    	place.addEventListener('dragover', dragOver);
	    		place.addEventListener('dragleave', dragLeave);
	    		place.addEventListener('drop', dragDrop);  
			}
            if (opponent != undefined) $("#2_" + id).text(prepareName(data.players[opponent].board[i-1][j-1].name));
        }
    }		
}

function loadHand (data) {
	let cardsInHand = data.players[login].hand.length;
	if (cardsInHand <= 5) {
		let hand = document.getElementById("cardHolder");
		for (let i = 0; i < cardsInHand; i++) {
			let card = document.createElement('li');
			addCardInHand(data, hand, i);
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

function getOpponentLogin(game) {
	let opponent = "";
	if (game.logins[0] == login) opponent = game.logins[1];
	else opponent = game.logins[0];
	return opponent;
}