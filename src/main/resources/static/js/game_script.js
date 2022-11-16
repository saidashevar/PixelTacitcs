let gameOn = false;
const url = 'http://localhost:8080';

function loadBoard(data) {
	if (data.player1.login == login) {
		for (let i = 1; i < 4; i++) {
	        for (let j = 1; j < 4; j++) {
	            let id = i + "_" + j;
	            $("#1_" + id).text(data.boardPlayer1[i-1][j-1]);
	            $("#2_" + id).text(data.boardPlayer2[i-1][j-1]);
	        }
	    }		
	} else if (data.player2.login == login) {
		for (let i = 1; i < 4; i++) {
	        for (let j = 1; j < 4; j++) {
	            let id = i + "_" + j;
	            $("#2_" + id).text(data.boardPlayer1[i-1][j-1]);
	            $("#1_" + id).text(data.boardPlayer2[i-1][j-1]);
	        }
	    }
	} else {
		alert("Login was not recognized");
	}
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

$("[id ^= 1],[id ^= 2]").click(function () {
    let id = $(this).attr('id');
    playerChoice(id.split("_")[0], id.split("_")[1], id.split("_")[2]);
});