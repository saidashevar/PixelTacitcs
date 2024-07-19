//Functions in this script doesn't actually require anything special from server and
//they are not really related to game's mechanics and logic

//Gets gameid and login from URI immediately after loading page


function getGameIDandLogin() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  		get: (searchParams, prop) => searchParams.get(prop),
	});
	gameId = params.id;
	login = params.login;
	console.log("got your login and id");
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent = login+", "+paragraph.textContent+gameId;
}

