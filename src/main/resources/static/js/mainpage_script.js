const url = 'http://localhost:8080';
let gameId;

function create_game() {
	let login = document.getElementById("login").value;
	$.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                document.location.href = url+"/game?gameid="+gameId;
            },
            error: function (error) {
                console.log(error);
            }
        })
}