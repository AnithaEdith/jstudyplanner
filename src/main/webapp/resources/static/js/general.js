/* ---------------------------------------
-- General scripts	                    --
-- this executes after page load		--
----------------------------------------*/

$(document).ready(function() {
	
	/* ---------------------------------------
	-- Login form in modal         			--
	----------------------------------------*/
	$('#loginForm').on('shown.bs.modal', function () {
		lastfocus = $(this);
		$(this).find('input:text:visible:first').focus();
	});
	
	
	/* ---------------------------------------
	-- Bootstrap Date Picker       			--
	------------------------------------------
	Add .date-field to all date input fields
	*/
	$(".date-field").datepicker({
		/*format: 'yyyy-mm-dd',*/
		format: 'dd-mm-yyyy',
		autoclose: true
	});
	
});