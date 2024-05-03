$(document).ready(function() {
    localStorage.removeItem('filterData');
    $('#stationFilter').on('change', function() {
        var selectedStation = $(this).val();
        filterTableByStation(selectedStation);
    });

    $('#modelSelectionTableBody').on('change', '.model-checkbox', function() {
        var isChecked = $(this).prop('checked');
        var modelId = $(this).closest('tr').data('model-id'); 
        
        $.ajax({
            url: "/api/prediction-models/updateDefaultModel",
            type: "PUT",
            data: { modelId: modelId, newCheckboxState: isChecked },
            success: function(response) {
                swal("Successful!", "Default model update successfully!", "success").then((value) => {
                    console.log("Checkbox state updated:", response);
                    loadTableData(); 
                  });
                
            },
            error: function(error) {
                console.log("Error updating checkbox state:", error);
            }
        });
    });

    loadTableData();
    filterTableByStation("");
});

function loadTableData() {
    $.ajax({
        url: "/api/rainfall/getDefaultModel",
        type: "GET",
        dataType: "json",
        success: function(responseData) {
            var count = 0;
            var tableBody = $('#modelSelectionTableBody');
            tableBody.empty(); // Clear existing table rows

            responseData.forEach(function(item) {
                count++;
                var row = '<tr data-model-id="' + item.modelId + '">' +
                    '<td>' + count + '</td>' +
                    '<td class="station">' + item.stationName + '</td>' +
                    '<td>' + item.modelNumber + '</td>' +
                    '<td>' + item.start_date + '-' + item.end_date + '</td>' +
                    '<td><input type="checkbox" ' + (item.isDefault ? 'checked' : '') +
                    ' class="model-checkbox" data-model-id="' + item.modelId + '"></td>' + 
                    '</tr>';
                tableBody.append(row);
            });
        },
        error: function(error) {
          console.log("Error fetching rainfall data:", error);
        }
    });
}

function filterTableByStation(station) {
    $('#modelSelectionTableBody tr').each(function() {
        var rowStation = $(this).find('.station').text();
        if (station === "" || rowStation === station) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
}
