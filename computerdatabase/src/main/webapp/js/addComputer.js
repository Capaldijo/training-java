$(function() {
	// Init the datepickers	
	$('#introduced').datetimepicker({
		format: 'YYYY-MM-DD',
	});
	
	$('#discontinued').datetimepicker({
		format: 'YYYY-MM-DD',
	});
	
	$('#addForm').bootstrapValidator({
		feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
		fields: {
        	computerName: {
        		validators: {
        			regexp: {
        				regexp: /^[a-zA-Z\s_0-9\-.]+/,
        				message: 'You can only type in alphabetical and numerical characters.'
        			}
        		}
            }
        }
    });
});