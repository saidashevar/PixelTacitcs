window.onload = getID;

function getID() {
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent += gameId;	
}