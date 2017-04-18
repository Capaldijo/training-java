$(function() {
	// Init the datepickers
	var nowTemp = new Date();
	var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), nowTemp.getHours(), nowTemp.getMinutes(), 0, 0);
	
	$('#introduced').datetimepicker({
		format: 'YYYY-MM-DD HH:mm',
		minDate: now
	});
	
	$('#discontinued').datetimepicker({
		format: 'YYYY-MM-DD HH:mm',
		minDate: now
	});
});