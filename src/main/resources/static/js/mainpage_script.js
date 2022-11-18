const url = 'http://localhost:8080';

function create_game() {
	let login = document.getElementById("login").value;
	$.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "string": login
            }),
            success: function (data) {
				let gameId = data.gameId;
                document.location.href = url+"/game?gameid="+gameId+"&login="+login;
            },
            error: function (error) {
                console.log(error);
            }
        })
}

function connectToRandomGame() {
	let login = document.getElementById("login").value;
    $.ajax({
        url: url + "/game/connect/random",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "string": login
        }),
        success: function (data) {
			let gameId = data.gameId;
            document.location.href = url+"/game?gameid="+gameId+"&login="+login;
//            connectToSocket(gameId);
//            alert("Congrats you're playing with: " + data.player1.login);
        },
        error: function (error) {
            console.log(error);
        }
    })
}