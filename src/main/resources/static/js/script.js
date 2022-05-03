function showPlus() {
	event.target.closest('.item').firstElementChild.style.display = "revert";
}

function hidePlus() {
	event.target.closest('.item').firstElementChild.style.display = "none";
}