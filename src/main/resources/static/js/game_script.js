let gameOn = false;
const url = 'http://localhost:8080';

function loadBoard(data) {
	let opponent = data.logins[0];
	alert(data.logins[0]);
	alert(data.players[0]);
	alert(data.players);
	alert(data.players[object]);
	alert(data.players[Object]);
	alert(data.players.login);
	for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            $("#1_" + id).text(data.players.Object.login.board[i-1][j-1]);
            $("#2_" + id).text(data.players.opponent.board[i-1][j-1]);
        }
    }		
}

function loadHand(data) {
	
}

function playerChoice(squadnumber, i, j) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "requester": login,
            "squad": squadnumber,
            "coordinateX": i,
            "coordinateY": j
        }),
        success: function (data) {
            loadBoard(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function takeCard() {
    $.ajax({
	    url: url + "/game/takecard",
	    type: 'POST',
	    dataType: "json",
	    contentType: "application/json",
	    data: JSON.stringify({
	        "gameId": gameId,
	        "requester": login,
	    }),
	    success: function (data) {
	        loadHand(data);
	    },
	    error: function (error) {
	        console.log(error);
	    }
	})
}

$("[id ^= 1],[id ^= 2]").click(function () {
    let id = $(this).attr('id');
    playerChoice(id.split("_")[0], id.split("_")[1], id.split("_")[2]);
});

$("#deckPlayer2").click(function () {
	takeCard();
});