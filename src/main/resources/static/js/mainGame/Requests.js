async function requestFullGame(fun) {
	console.log("requesting initialization info about everything here");
    $.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			console.log("got info about game from initialization: " + newGame);
			gameSave = newGame;
			getOpponent();
			requestLeader(checkStatus);
			reloadTurns();			   //shield and sword at right of main board
			requestHeroes(function() { //i don't understand how does this work. Probably they should be in another order?
				loadHeroes();		   // anyway, this function is called once while page starts... and it works properly...
			});
			connectToSocket();
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Game wasn't loaded!" + error);
        }
    })
}

//the most important function. BAC!
function requestBoardActionsCards() {
	console.log("requesting BAC!");
    $.ajax({
        url: url + "/games/get-board-actions-cards?id="+gameId+"&login="+login,
        type: 'GET',
        success: function () {
			/*console.log(newGame);
			gameSave = newGame;
			getOpponent();
			requestLeader(checkStatus);
			reloadTurns(); //shield and sword at right of main board
			requestHeroes(function() { //i don't understand how is this work. Probably they should be in another order?
				loadHeroes();		   // anyway, this function is called once while page starts... and it works properly...
			});*/
        },
        error: function (error) {
            console.log("Requesting BAC failed!" + error);
        }
    })
}

function requestHand(fun) { //this function just updates your hand, can't return new cards
	$.ajax({
        url: url + "/players/get-hand?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHand) {
			handSave = newHand;
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Hand wasn't loaded!" + error);
        }
    })
}

function requestHeroes(fun) {
	console.log("requesting heroes");
    $.ajax({
		url: url + "/heroes/get-heroes?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newHeroes) {
			heroesSave = newHeroes;
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function requestLeader(fun) { //this smart function can show leaders or hide them.
    $.ajax({
        url: url + "/players/get-leader?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (leader) {
			leaderSave = leader;
			console.log("got your leader from initialization: " + leaderSave);
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Leader is broken! Nerf him!" + error);
        }
    })
}

function requestTurn() {
    $.ajax({
        url: url + "/players/get-turn?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newTurn) {
			turnSave = newTurn;
        },
        error: function (error) {
            console.log("Turn of one player wasn't loaded!" + error);
        }
    })
}

//probably useless...
function requestGame(fun) {
	$.ajax({
        url: url + "/games/get-game?id="+gameId+"&login="+login,
        type: 'GET',
        success: function (newGame) {
			console.log("Requested game: " + newGame);
			gameSave = newGame;
			getOpponent();
			if (fun != undefined) fun();
        },
        error: function (error) {
            console.log("Game wasn't loaded!" + error);
        }
    })
}