let gameOn = false;

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