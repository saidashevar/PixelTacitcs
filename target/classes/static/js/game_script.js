// Script map
// 65. function displayCard(e) 
//Next are some support variables
var redSrc = "images/CardsDetails/Red.png";
var blueSrc = "images/CardsDetails/Blue.png";

//Few variables about non gameplay fields.
const url = 'http://localhost:8080';
//var gameStatus;
var gameId;
var login;
var opponentSave;
var youSave;

//Next variables save all information about game for that player.
var handSave;		//array of cards in your hand
var cardCountSave;  //two numbers that determine card count of both players (may be badly translated to eng)
var turnSave;		//info about your turn
var gameSave;		//info about game
var heroesSave;		//info about all heroes on board
var leaderSave;		//info about your leader

//Request functions
function placeCard(j, id) {
    $.ajax({
        url: url + "/heroes/hire-hero",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "login": login,
            "coordinateY": j,
            "cardId": id
        }),
        success: function (newHand) {
			handSave = newHand;
            reloadHand(handSave);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function takeCard() {
    $.ajax({
	    url: url + "/cards/take-card",
	    type: 'POST',
	    dataType: "json",
	    contentType: "application/json",
	    data: JSON.stringify({
	        "gameId": gameId,
	        "login": login,
	    }),
	    success: function (newHand) {
			handSave = newHand;
			if (handSave.length <= 5) { //some heroes' abilities can able player to take more than 5 cards...
				let handElement = document.getElementById("cardHolder");
				addCardInHand(handElement, handSave.length-1);
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
	
	cardImage.src = "images/Cards/" + handSave[cardNumber].name + ".png";
	
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
//chooseLeader is also event function...

//Support functions
function addCardInHand(hand, cardId) {
	let card = document.createElement('li');
	card.id = "hand"+cardId;
	card.setAttribute("draggable", "true");
	card.addEventListener('dragstart', onDragStart);
	card.addEventListener('dragend', onDragEnd);
	card.addEventListener('click', onClickShowCard);
	card.append(prepareToShow('attack', cardId));
	card.append(prepareToShow('maxHealth', cardId));
	card.append(prepareCardInHandName(cardId));
	
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

function prepareToShow(attribute, cardId) {
	let container = document.createElement('div');
	container.classList.add(attribute + 'Mini');
	container.append(prepareImage(attribute));
	container.append(prepareText(attribute, cardId));
	return container;
}

function prepareText(attribute, cardId) {
	let text = document.createElement('div');
	text.textContent = handSave[cardId][attribute];
	text.classList.add('centerText');
	return text;
}

function prepareImage(attribute) {
	let image = document.createElement('img');
	image.src = "images/mini/" + attribute + ".png";
	return image;
}

function prepareCardInHandName(cardId) {
	let cardName = document.createElement('div');
	cardName.classList.add('cardName');
	cardName.textContent = prepareName(handSave[cardId].name);
	return cardName;
}

//Click functions
$("#deckPlayer1").click(function () {
	takeCard();
});