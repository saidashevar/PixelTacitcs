function loadSwordAndShield() { // loads images of sword and shield
	console.log ("placing sword and shield images");
	loadSwordOrShield(youSave);
	if (opponentSave != undefined) loadSwordOrShield(opponentSave);
}

function loadHeroes(fun) { //this function is my masterpiece of javascripting... but probably too large
	//here we have an array of every hero (your and not) that needs to be produced
	console.log("loading heroes");
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
		
		//check if it is alive
		if (checkEffect(x, "defeated") == 1) {
			//if it is dead we show a shirt of card
			place.innerHTML = '';
			if (isYour) {
				if (youSave.red) place.appendChild(createCardImage(redSrc));
				else place.appendChild(createCardImage(blueSrc));
			} else {
				if (opponentSave.red) place.appendChild(createCardImage(redSrc));
				else place.appendChild(createCardImage(blueSrc));
			}
			
			//prepare dragging if we will want to remove corpse
			place.setAttribute("draggable", "true");
			place.addEventListener('dragstart', onCorpseRemovingDragStart);
			
		//in case it is alive 
		} else {
			//print hero's name, health and attack
			place.textContent = prepareName(heroesSave[x].name);
			place.appendChild(prepareToShow_HeroAttack('attack', x));
			place.appendChild(prepareToShow_HeroHealth('maxHealth', x));
			
			//This is for attacking
			//you can drag your hero to attack other heroes
			place.setAttribute("draggable", "true");
			place.addEventListener('dragstart', onAttackStart); //prepare to attack!
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

function loadLeaders() { //This methos also loads images for decks (totally shouldn't be like this)
	switch (gameSave.status) {
		case "PEACE":
			let oppLeaderDiv = document.getElementById("2_1_1"); 
			let yourLeaderDiv = document.getElementById("1_1_1");
			let oppDeck = document.getElementById("deckPlayer2");
			let yourDeck = document.getElementById("deckPlayer1");
			
			if (youSave.red == true) {
				helpLoadLeaders(oppLeaderDiv, oppDeck, blueSrc);
				helpLoadLeaders(yourLeaderDiv, yourDeck, redSrc);
			} else {
				helpLoadLeaders(oppLeaderDiv, oppDeck, redSrc);
				helpLoadLeaders(yourLeaderDiv, yourDeck, blueSrc);
			}
		break;
		case "": //next updates are coming! They are about turning leaders face up, when peace round ends.
		break;
	}
}

//support function
//for normal functioning this function must have up to date players' saves!
function loadSwordOrShield(player) { 
	//This function loads image of sword and shield to show wave and turn order of given player.
	let i = 2;
	if (player.login == login) i = 1;
	
	//maybe i have to avoid such contructions but i don't know how to do this.
	if (player.turn.attacking == true) { //in case we are attacking
		if (player.turn.actionsLeft == 0) { //we have no actions
			if (player.turn.wave == 2) { //this is the last wave
				turnDiv = document.getElementById("0_"+i+"_2");
				turnDiv.appendChild(createCardImage(actionsSrc));	
			} else { //this is not last wave
				turnDiv = document.getElementById("0_"+i+"_" + (player.turn.wave+1));
				turnDiv.appendChild(createCardImage(firstSrc));
			}
		} else { //we are attacking and we have actions
			youTurnDiv = document.getElementById("0_"+i+"_" + player.turn.wave);
			youTurnDiv.appendChild(createCardImage(firstSrc));
		}
	} else { // we are not attacking
		youTurnDiv = document.getElementById("0_"+i+"_" + player.turn.wave);
		youTurnDiv.appendChild(createCardImage(secondSrc));
	}
}

function helpLoadLeaders(leaderDiv, deck, src) { //support function to ease last function
	leaderDiv.innerHTML = '';
	deck.innerHTML = '';
	leaderDiv.appendChild(createCardImage(src));
	deck.appendChild(createCardImage(src));
}
