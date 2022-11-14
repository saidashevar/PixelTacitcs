window.onload = getGameID;

function getGameID() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  	get: (searchParams, prop) => searchParams.get(prop),
	});
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent += params.gameid;
}

function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })
}