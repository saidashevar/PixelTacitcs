const url = 'http://localhost:8080';

function create_game_button() {
	let login = document.getElementById("login").value;
	check_login(login, create_new_game(login));
}

function connect_to_random_game_button() {
	let login = document.getElementById("login").value;
	check_login(login, connect_to_random_game(login));
}

//Checks if player with that login exists. If not, creates new player and...
function check_login(login, fun) {
	$.ajax({
        url: url + "/players/checkLogin",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "string": login
        }),
        success: function (player) {
			fun(player.login);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function create_new_game(login) {
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

function connect_to_random_game(login) {
    $.ajax({
        url: url + "/games/connect/random",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "login": login
        }),
        success: function (game) {
			let gameId = game.id;
            document.location.href = url+"/game?id="+gameId+"&login="+login;
//            connectToSocket(gameId);
//            alert("Congrats you're playing with: " + data.player1.login);
        },
        error: function (error) {
			alert("No game found");
            console.log(error);
        }
    })
}