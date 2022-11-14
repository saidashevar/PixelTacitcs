window.onload = getID;

function getID() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  	get: (searchParams, prop) => searchParams.get(prop),
	});
	let gameId = params.gameid;
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent += gameId;	
}