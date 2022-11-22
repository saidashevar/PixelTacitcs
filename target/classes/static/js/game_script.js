let gameOn = false;
const url = 'http://localhost:8080';
var gameStatus;

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

function loadHand(game) {
	let hand = document.getElementById("cardHolderParent");
	hand.removeChild(hand.firstChild);
	let cardHolder = document.createElement('ol');
	cardHolder.id = "cardHolder";
	hand.append(cardHolder);
	for (let page = 0; page < Math.ceil(game.players[login].hand.length / 5); page++) {
		for (let i = 0; i < 5; i++) {
			let card = document.createElement('li');
			card.id = "hand"+i;
			card.setAttribute("draggable", "true");
			card.textContent = game.players[login].hand[i];
			hand.append(card);
		}
		//For future: add code to new button, that will appear if player has more then 5 cards.		
	}
}

function playerChoice(squadnumber, i, j) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "requester": login,
            "squad": squadnumber,
            "coordinateX": i,
            "coordinateY": j
        }),
        success: function (data) {
            loadBoard(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function takeCard() {
    $.ajax({
	    url: url + "/game/takecard",
	    type: 'POST',
	    dataType: "json",
	    contentType: "application/json",
	    data: JSON.stringify({
	        "gameId": gameId,
	        "login": login,
	    }),
	    success: function (data) {
			let cardsInHand = data.players[login].hand.length;
			if (cardsInHand <= 5) {
				let hand = document.getElementById("cardHolder");
				let card = document.createElement('li');
				card.id = "hand"+(cardsInHand-1);
				card.setAttribute("draggable", "true");
				card.textContent = data.players[login].hand[cardsInHand-1].name;
				//card.addEventListener('mousedown', onmousedown);
				card.addEventListener('dragstart', onDragStart);
				hand.append(card);
			}
	    },
	    error: function (error) {
	        console.log(error);
	    }
	})
}

function getOpponentLogin(game) {
	let opponent = "";
	if (game.logins[0] == login) opponent = game.logins[1];
	else opponent = game.logins[0];
	return opponent;
}

//Click functions
$("[id ^= 1],[id ^= 2]").click(function () {
	if (gameStatus != "NEW") {
		let id = $(this).attr('id');
    	playerChoice(id.split("_")[0], id.split("_")[1], id.split("_")[2]);		
	}
});

$("#deckPlayer1").click(function () {
	takeCard();
});