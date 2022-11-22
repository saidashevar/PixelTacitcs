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
}

let cardDrag;
function onmousedown(event) {
	cardDrag = event.target;
	event.dataTransfer.dropEffect = "move";
	cardDrag.style.postition = 'absolute';
	cardDrag.style.zIndex = 1001;
	document.body.append(cardDrag);
	
	/*function moveAt(pageX, pageY) {
    	cardDrag.style.left = pageX;//- cardDrag.offsetWidth / 2 + 'px';
    	cardDrag.style.top = pageY;//- cardDrag.offsetHeight / 2 + 'px';
  	}*/
  	
  	/*document.addEventListener('mousemove', onMouseMove);
  	
  	function onMouseMove(event) {
    	moveAt(event.pageX, event.pageY);
  	}
  	
  	cardDrag.onmouseup = function() {
    	document.removeEventListener('mousemove', onMouseMove);
   		cardDrag.onmouseup = null;
  	};*/
}

function ondragstart() {
  return false;
};
/*
card.onmousedown = function (event) {
	alert("you have tapped on card!");
}

$('[id ^= hand]').click(function(){
	alert("log");
	let card = this;
	card.ondragstart = function() {
  		return false;
	};
	card.onmousedown = function(event) { // (1) отследить нажатие
		// (2) подготовить к перемещению:
  		// разместить поверх остального содержимого и в абсолютных координатах
	  	card.style.position = 'absolute';
	  	card.style.zIndex = 1000;
	  	// переместим в body, чтобы мяч был точно не внутри position:relative
	  	document.body.append(card);
	  	// и установим абсолютно спозиционированный мяч под курсор
	  	moveAt(event.pageX, event.pageY);
	  	// передвинуть мяч под координаты курсора
	 	// и сдвинуть на половину ширины/высоты для центрирования
	  	function moveAt(pageX, pageY) {
	   		card.style.left = pageX - card.offsetWidth / 2 + 'px';
	    	card.style.top = pageY - card.offsetHeight / 2 + 'px';
	  	}
	  	// (3) перемещать по экрану
	  	document.addEventListener('mousemove', onMouseMove);
	  	function onMouseMove(event) {
	   		moveAt(event.pageX, event.pageY);
	  	}
	};
});
//let card = document.querySelector('li[id^="hand"]');*/