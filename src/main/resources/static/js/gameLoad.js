window.onload = loadPage;
var gameId;
var login;

function loadPage() {
	getGameID();
	connectToSocket(gameId);
	requestBoard(gameId);
}

function getGameID() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  		get: (searchParams, prop) => searchParams.get(prop),
	});
	gameId = params.gameid;
	login = params.login;
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent = login+", "+paragraph.textContent+gameId;
}

function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            gameStatus = data.status;
        })
    })
}

function requestBoard(gameId) {
    $.ajax({
        url: url + "/game/loadgame",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
			"string": gameId	
		}),
        success: function (data) {
            loadHand(data);
            console.log("Successfully loaded board")
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function loadBoard(data) {
	let opponent = getOpponentLogin(data);
	for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            $("#1_" + id).text(data.players[login].board[i-1][j-1]);
            $("#2_" + id).text(data.players[opponent].board[i-1][j-1]);
        }
    }		
}

function loadHand (data) {
	let cardsInHand = data.players[login].hand.length;
	if (cardsInHand <= 5) {
		let hand = document.getElementById("cardHolder");
		for (let i = 0; i < cardsInHand; i++) {
			let card = document.createElement('li');
			card.id = "hand"+i;
			card.innerHTML = data.players[login].hand[i].name;
			hand.append(card);
		}		
	}
}