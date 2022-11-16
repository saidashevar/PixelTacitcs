let gameOn = false;
const url = 'http://localhost:8080';

function loadBoard(data) {
	let opBoard = data.upperBoard;
	let yourBoard = data.lowerBoard;
    for (let i = 1; i < 4; i++) {
        for (let j = 1; j < 4; j++) {
            let id = i + "_" + j;
            $(".opponent_board." + id).text(opBoard[i-1][j-1]);
            $(".your_board." + id).text(yourBoard[i-1][j-1]);
        }
    }
    gameOn = true;
}

function playerTurn(type, xCoordinate, yCoordinate) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "type": type,
            "coordinateX": xCoordinate,
            "coordinateY": yCoordinate,
            "gameId": gameId
        }),
        success: function (data) {
            gameOn = false;
            loadBoard(data);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

$(".your_squad,.opponent_squad").click(function () {
    var slot = $(this).attr('id');
    playerTurn(slot);
});