
//Request functions
function placeCard(i, j, id) {
    $.ajax({
        url: url + "/heroes/hire-hero",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "login": login,
            "coordinateX": i,
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

function meleeDamage(attackerPlaceId, targetId) {
	$.ajax({
        url: url + "/heroes/damage",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
			"gameId": gameId,
			"login": login,
            "type": "MELEE",
            "attackerIsLeader": false, //for now, only heroes can attack
            "attackerId": findCardId(attackerPlaceId),
            "targetIsLeader": checkIfTargetIsLeader(targetId),
            "targetId": findCardId(targetId)
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
	    url: url + "/players/take-card",
	    type: 'POST',
	    dataType: "json",
	    contentType: "application/json",
	    data: JSON.stringify({
	        "gameId": gameId,
	        "login": login
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

function removeCorpse(placeId) { //not working properly, doesn't used anywhere... i think
	$.ajax({
        url: url + "/heroes/removeCorpse",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "login": login,
            "coordinateX": 0, //coordinates doesn't matter
            "coordinateY": 0,
            "cardId": findCardId(placeId)
        }),
        success: function (message) {
			let place = document.getElementById(placeId);
			console.log(place.id);
			place.innerHTML = '';
        },
        error: function (error) {
            console.log(error);
        }
	})
}

// Some gameplay functions
function displayCard(e) { // shows card in hand when you click it
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
function closeCard() { //closes opened card
	let back = document.getElementById("BB");
	let card = document.getElementById("displayedCard");
	back.remove();
	card.remove();
}

function turnCard() { //turns card upside down to help you reading this card's leader abilities
	let card = document.getElementById('displayedCard');
	if (card.style.getPropertyValue("transform") == "") {
		card.style.transform = "scaleY(-1) scaleX(-1)";
	} else {
		card.style.removeProperty("transform");
	}
}
//chooseLeader is also event function...

//Support functions
function addCardInHand(hand, cardId) { //inputs new card in your hand when you take new card.
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

function prepareToShow(attribute, cardId, hero) {
	let container = document.createElement('div');
	container.classList.add(attribute + 'Mini');
	container.append(prepareImage(attribute));
	container.append(prepareText(attribute, cardId));
	return container;
}

//This method will become more serious later...
function prepareToShow_HeroAttack(attribute, id) {
	let container = document.createElement('div');
	container.classList.add(attribute + 'MiniAtHero');
	container.append(prepareImage(attribute, true));
	container.append(prepareText_HeroAttack(id));
	return container;
}

//And this too
function prepareToShow_HeroHealth(attribute, id) {
	let container = document.createElement('div');
	container.classList.add(attribute + 'MiniAtHero');
	container.append(prepareImage(attribute, true));
	container.append(prepareText_HeroHealth(id));
	return container;
}

//Add text to card in hand, depending on card.
function prepareText(attribute, cardId) {
	let text = document.createElement('div');
	text.textContent = handSave[cardId][attribute];
	text.classList.add('centerText');
	return text;
}

function prepareText_HeroAttack(ID) {
	let text = document.createElement('div');
	text.textContent = heroesSave[ID].attack;
	text.classList.add('centerText');
	return text;
}

function prepareText_HeroHealth(ID) {
	let text = document.createElement('div');
	let damage = checkEffect(ID, "damaged");
	text.textContent = heroesSave[ID].maxHealth - damage; 
	text.classList.add('centerText');
	if (damage != 0) text.style.color = "red";
	return text;
}

function prepareImage(attribute, opacity) {
	let image = document.createElement('img');
	image.src = "images/mini/" + attribute + ".png";
	if (opacity != undefined && opacity == true) image.style.opacity = "0.8";
	return image;
}

function prepareCardInHandName(cardId) {
	let cardName = document.createElement('div');
	cardName.classList.add('cardName');
	cardName.textContent = prepareName(handSave[cardId].name);
	return cardName;
}

function findCardId(placeId) { // returns cardId knowing place of card. Used for attacks
	//in html coordinates of places in squad are like this: 1_1_1, where first number - squad, second and third - place in square
	let id = placeId.split("");
	let squad = id[0];
	let i = id[2];
	let j = id[4];
	//i = 1; j = 1 - center of the squad. Leader is there
	if (i == 1 && j == 1) { return -1 } //Leader case
	else {
		for(let x = 0; x < heroesSave.length; x++) {
			let hero = heroesSave[x];
			if (hero.coordX == i && hero.coordY == j) {
				console.log(hero.id);
				//next we have to be sure to attack opposite squad.
				//Reviewing this code again after half a year, i don't understand why this check was needed
				if((squad == 1 && hero.player.login == login) || 
				   (squad == 2 && hero.player.login == opponentSave.login)) 
					return hero.id;
			}
		}
	}
}

//temporary function... it must be
function checkIfTargetIsLeader(placeId) {
	//in html coordinates of places in squad are like this: 1_1_1, where first number - squad, second and third - place in square
	let id = placeId.split("");
	let squad = id[0];
	let i = id[2];
	let j = id[4];
	return i == 1 && j == 1;
}

//another temporary function (no, it is actually usefull, i just don't know where to place it)
//looks for some effect in hero's current effects, if finds, returns it's value
//if finds nothing returns 0
function checkEffect(heroId, effect) {
	for(let i = 0; i < heroesSave[heroId].effects.length; i++) {
		if (heroesSave[heroId].effects[i].name == effect)
		return heroesSave[heroId].effects[i].value;
	}
	return 0;
}

//Click functions
$("#deckPlayer1").click(function () {
	takeCard();
});