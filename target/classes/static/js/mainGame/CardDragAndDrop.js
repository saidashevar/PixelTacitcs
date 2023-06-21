/*const boxes = document.querySelectorAll('.box');

boxes.forEach(box => {
    box.addEventListener('dragenter', dragEnter)
    box.addEventListener('dragover', dragOver);
    box.addEventListener('dragleave', dragLeave);
    box.addEventListener('drop', drop);
});
*/

function onAttackStart(event) {
	console.log(event.target);
	event.dataTransfer.setData('text/plain', event.target.id);
	requestAvailableTargets();
}

function onDragStart(event) {
	event.dataTransfer.setData('text/plain', event.target.id.split("")[4]);
	setTimeout(() => {
        event.target.classList.add('hide');
    }, 0);
    requestAvailablePlaces();
}

function onCorpseRemovingDragStart(event) {
	event.dataTransfer.setData('text/plain', event.target.id);
	let pile = document.getElementById('pilePlayer1');
	pile.classList.add('readyToDrop');
	pile.addEventListener('dragenter', dragEnter);
	pile.addEventListener('dragover', dragOver);
	pile.addEventListener('dragleave', dragLeave);
	pile.addEventListener('drop', dragCorpseRemoved);
}

//This function can be made much more complicated in future...
function onDragEnd() {
	reloadHand(handSave);
	let places = document.querySelectorAll('.readyToDrop');
	places.forEach(place => {
		place.classList.remove('readyToDrop');
		place.removeEventListener('dragenter', dragEnter);
		place.removeEventListener('dragover', dragOver);
		place.removeEventListener('dragleave', dragLeave);
		place.removeEventListener('drop', dragDrop);
	});
}

function dragEnter(e) {
    e.preventDefault();
    e.target.classList.add('drag-over');
}

function dragOver(e) {
    e.preventDefault();
    e.target.classList.add('drag-over');
}

function dragLeave(e) {
    e.target.classList.remove('drag-over');
}

function dragDrop(e) {
	e.target.classList.remove('drag-over');
	let place = e.target.id;
	let cardInHand = e.dataTransfer.getData('text/plain');
	console.log(handSave);
	placeCard(place.split("_")[1], place.split("_")[2], handSave[cardInHand].id); //Sending coordinate x, y and id of card
}

function dragMeleeAttacked(e) {
	e.target.classList.remove('drag-over');
	console.log(leaderSave);
	console.log("Melee attacked: " + e.target.id);
	let attackerPlaceId = e.dataTransfer.getData('text/plain');
	meleeDamage(attackerPlaceId, e.target.id);
}

function dragCorpseRemoved(e) {
	e.target.classList.remove('drag-over');
	let corpsePlaceId = e.dataTransfer.getData('text/plain');
	cleanBoard(function () { 
		loadHeroes();
		loadLeaders(); 
	});
	//removeCorpse(corpsePlaceId); seems not working
}

function onClickShowCard(e) {
	displayCard(e);
}

function loadAvailablePlaces(places) {
	for (let x = 0; x < places.length; x++)	{
		//Get coordinates
		let i = places[x].coordX;
		let j = places[x].coordY;
		let id = i + "_" + j;
		
		let place = document.getElementById("1_"+id); //This means, we may hire only in our squad. it may be not like that for some future heroes.
		
		place.classList.add('readyToDrop');
		place.addEventListener('dragenter', dragEnter);
		place.addEventListener('dragover', dragOver);
	    place.addEventListener('dragleave', dragLeave);
	    place.addEventListener('drop', dragDrop);
	}
}

function loadAvailableTargets(heroes) {
	for (let x = 0; x < heroes.length; x++)	{
		//Get coordinates
		let i = heroes[x].coordX;
		let j = heroes[x].coordY;
		let id = i + "_" + j;
		//Get place with coordinate
		let hero = document.getElementById("2_"+id);
		
		hero.classList.add('readyToDrop');
		hero.addEventListener('dragenter', dragEnter);
		hero.addEventListener('dragover', dragOver);
	    hero.addEventListener('dragleave', dragLeave);
	    hero.addEventListener('drop', dragMeleeAttacked);
	}
}