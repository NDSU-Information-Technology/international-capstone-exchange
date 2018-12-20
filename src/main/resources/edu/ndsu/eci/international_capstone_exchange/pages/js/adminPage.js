$(function(){
	//Bold proposal count if greater than or equal to threshold
	let proposal = document.getElementById("proposalCount");
	let propCount = parseInt(proposal.innerText);
	
	if ( propCount >= countToBold ) {
		proposal.style.fontWeight = 'bold';
	} 
	
	//Bold user count if greater than or equal to threshold
	let user = document.getElementById("userCount");
	let userCount = parseInt(user.innerText);
	
	if ( userCount >= countToBold ) {
		user.style.fontWeight = 'bold';
	} 
});