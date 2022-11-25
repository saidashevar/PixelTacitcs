/*const boxes = document.querySelectorAll('.box');

boxes.forEach(box => {
    box.addEventListener('dragenter', dragEnter)
    box.addEventListener('dragover', dragOver);
    box.addEventListener('dragleave', dragLeave);
    box.addEventListener('drop', drop);
});
*/

function onDragStart(e) {
	e.dataTransfer.setData('text/plain', e.target.id.split("")[4]);
	setTimeout(() => {
        e.target.classList.add('hide');
    }, 0);
    
    let query = 'td[id ^= "1_' + (lastGameSave.wave + 1) + '"]';
    const boxes = document.querySelectorAll(query);
	boxes.forEach(box => {
    	box.classList.add('readyToDrop');
	});
}

function onDragEnd(e) {
	reloadHand(lastGameSave);
	let places = document.querySelectorAll('.readyToDrop');
	places.forEach(place => {
		place.classList.remove("readyToDrop");
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
	placeCard(place.split("_")[2], cardInHand);
}

function onClickShowCard() {
	displayCard();
}