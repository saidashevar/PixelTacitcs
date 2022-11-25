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

// Some gameplay functions
function displayCard(e) {
	let blackBackground = document.createElement("div");
	let cardNumber = e.target.id.split("")[4];
	blackBackground.id = "BB";
	blackBackground.setAttribute('onclick', 'closeCard()');
	
	let cardImage = document.createElement("img");
	cardImage.id = "displayedCard";
	cardImage.setAttribute('onclick', 'turnCard()');
	
	cardImage.src = "images/Cards/" + lastGameSave.players[login].hand[cardNumber].name + ".png";
	
	document.body.appendChild(blackBackground);
	document.body.appendChild(cardImage);
}

//Event functions
function closeCard() {
	let back = document.getElementById("BB");
	let card = document.getElementById("displayedCard");
	back.remove();
	card.remove();
}

function turnCard() {
	let card = document.getElementById('displayedCard');
	if (card.style.getPropertyValue("transform") == "") {
		card.style.transform = "scaleY(-1) scaleX(-1)";
	} else {
		card.style.removeProperty("transform");
	}
}

//Support functions
function addCardInHand(data, hand, cardId) {
	let card = document.createElement('li');
	card.id = "hand"+cardId;
	card.textContent = prepareName(data.players[login].hand[cardId].name);
	card.setAttribute("draggable", "true");
	card.addEventListener('dragstart', onDragStart);
	card.addEventListener('dragend', onDragEnd);
	card.addEventListener('click', onClickShowCard);
	hand.append(card);
}

function prepareName(name) { //This function just deletes underscores
	let oldname = name.split("_");
	let newname = oldname[0];
	for (let n = 1; n < oldname.length; n++) {
		newname += " " + oldname[n];
	}
	return newname;
}

//Click functions
$("#deckPlayer1").click(function () {
	takeCard();
});