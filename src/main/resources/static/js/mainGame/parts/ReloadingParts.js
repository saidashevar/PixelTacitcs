function reloadSwordAndShield() {
	cleanSwordAndShield();
	loadSwordAndShield();
}

function reloadHand() {
	const cardsInHand = document.querySelectorAll('li[id ^= "hand"]');
	cardsInHand.forEach(card => {
    	card.remove();
	});
	loadHand();
}