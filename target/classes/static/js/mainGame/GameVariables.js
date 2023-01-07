//Next are some support variables
var redSrc = "images/CardsDetails/Red.png";
var blueSrc = "images/CardsDetails/Blue.png";
var firstSrc = "images/CardsDetails/FirstPlayer.png";
var secondSrc = "images/CardsDetails/SecondPlayer.png";
var actionsSrc = "images/CardsDetails/Actions.png";

//Few variables about non gameplay fields.
const url = 'http://localhost:8080';
var gameId;
var login;

//Saves of both players
var opponentSave;
var youSave;

//Next variables save all information about game for that player.
var handSave;			//array of cards in your hand
var cardCountSave;  	//two numbers that determine card count of both players (may be badly translated to eng)
var actionsCountSave;	//numbers that show actions count. negative values mean actions of your opponent
var turnSave;			//info about your turn
var gameSave;			//info about game
var heroesSave;			//info about all heroes on board
var leaderSave;			//info about your leader