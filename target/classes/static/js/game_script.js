let gameOn = false;
const url = 'http://localhost:8080';
var gameStatus;
var gameId;
var login;
var lastGameSave;

//Request functions
function placeCard(j, number) {
    $.ajax({
        url: url + "/game/placecard",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "login": login,
            "coordinateY": j,
            "cardNumber": number
        }),
        success: function (data) {
			lastGameSave = data;
            loadBoard(data);
            reloadHand(data);
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
			lastGameSave = data;
			let cardsInHand = data.players[login].hand.length;
			if (cardsInHand <= 5) {
				let hand = document.getElementById("cardHolder");
				addCardInHand(data, hand, cardsInHand-1);
			}
	    },
	    error: function (error) {
	        console.log(error);
	    }
	})
}

//Support functions
function addCardInHand(data, hand, cardId) {
	let card = document.createElement('li');
	card.id = "hand"+cardId;
	card.textContent = data.players[login].hand[cardId].name;
	card.setAttribute("draggable", "true");
	card.addEventListener('dragstart', onDragStart);
	card.addEventListener('dragend', onDragEnd);
	hand.append(card);
}

//Click functions
$("#deckPlayer1").click(function () {
	takeCard();
});