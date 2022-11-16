let gameOn = false;
const url = 'http://localhost:8080';

function loadBoard(data) {
	let opBoard = data.upperBoard;
	let yourBoard = data.lowerBoard;
    for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            $("#2_" + id).text(opBoard[i-1][j-1]);
            $("#1_" + id).text(yourBoard[i-1][j-1]);
        }
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