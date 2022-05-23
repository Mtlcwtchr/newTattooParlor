function showPlus() {
	event.target.closest('.item').firstElementChild.style.display = "revert";
}

function hidePlus() {
	event.target.closest('.item').firstElementChild.style.display = "none";
}

function updateLabel() {
	event.target.closest('.update-profile-label').style.display = "revert";
	event.target.closest('.show-profile-label').style.display = "none";
}

function showLabel() {
	event.target.closest('.show-profile-label').style.display = "revert";
	event.target.closest('.update-profile-label').style.display = "none";
}