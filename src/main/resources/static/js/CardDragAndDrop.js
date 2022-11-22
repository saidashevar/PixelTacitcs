/*const boxes = document.querySelectorAll('.box');

boxes.forEach(box => {
    box.addEventListener('dragenter', dragEnter)
    box.addEventListener('dragover', dragOver);
    box.addEventListener('dragleave', dragLeave);
    box.addEventListener('drop', drop);
});
*/

//This code id working!
function onDragStart(e) {
	//e.dataTransfer.setData('text/plain', e.target.id); To make this work, set id first
	setTimeout(() => {
        e.target.classList.add('hide');
    }, 0);
    
    const boxes = document.querySelectorAll('td[id ^= "1"]');
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