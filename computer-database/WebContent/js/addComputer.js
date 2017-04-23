$(function() {
	// Init the datepickers	
	$('#introduced').datetimepicker({
		format: 'YYYY-MM-DD',
	});
	
	$('#discontinued').datetimepicker({
		format: 'YYYY-MM-DD',
	});
	
	$('#addForm').validate({
        rules: {
        	computerName: {
                alphanumeric: true
            }
        }
    });
});