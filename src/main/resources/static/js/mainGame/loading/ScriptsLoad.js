// This script loads other scripts
// This is the first and only script added to html page
loadJS("js/mainGame/loading/ChoosingLeaders.js");
loadJS("js/mainGame/parts/CleaningParts.js");
loadJS("js/mainGame/parts/LoadingParts.js");
loadJS("js/mainGame/parts/ReloadingParts.js");
loadJS("js/mainGame/CardDragAndDrop.js");
loadJS("js/mainGame/GameLoad.js");
loadJS("js/mainGame/GamePlay.js");
loadJS("js/mainGame/GameScript.js");
loadJS("js/mainGame/GameVariables.js");
loadJS("js/mainGame/Requests.js");
loadJS("js/mainGame/SupportLogic.js");

function loadJS(FILE_URL) {
	let script = document.createElement("script");
  
	script.setAttribute("src", FILE_URL);
	script.setAttribute("type", "text/javascript");
	script.setAttribute("async", true);

	document.body.appendChild(script);

	// success event 
	script.addEventListener("load", () => {
		console.log("File loaded");
	});
	// error event
	script.addEventListener("error", (ev) => {
		console.log("Error on loading file", ev);
	});
}