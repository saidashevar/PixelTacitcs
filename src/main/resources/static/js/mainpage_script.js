const url = 'http://localhost:8080';

function create_game_button() {
	let login = document.getElementById("login").value;
	get_player_and_start_game(login);
}

function connect_to_random_game() {
	let login = document.getElementById("login").value;
    $.ajax({
        url: url + "/games/connect/random",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "login": login
        }),
        success: function (data) {
			if (data == null) {
				alert("no game found");
			} else {
				let gameId = data.gameId;
	            document.location.href = url+"/game?id="+gameId+"&login="+login;
			}
//            connectToSocket(gameId);
//            alert("Congrats you're playing with: " + data.player1.login);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

//Checks if player with that login exists. If not, creates new player
function get_player_and_start_game(login) {
	$.ajax({
        url: url + "/players/checkLogin",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "string": login
        }),
        success: function (player) {
			create_game(player.login);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function create_game(login) {
	$.ajax({
        url: url + "/games/start",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "login": login
        }),
        success: function (game) {
			let gameId = game.id;
            document.location.href = url+"/game?id="+gameId+"&login="+login;
        },
        error: function (error) {
            console.log(error);
        }
    })
}