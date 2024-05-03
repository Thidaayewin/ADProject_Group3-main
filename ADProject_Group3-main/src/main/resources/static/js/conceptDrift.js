var storedFilterData = localStorage.getItem('filterData');
const startDateInput = document.getElementById('start_date');
const endDateInput = document.getElementById('end_date');
const dateError = document.getElementById('dateError');

var selectedWeatherStationInput = document.getElementById('weather_station');
var selectedModelInput = document.getElementById('model');
var selectedWRMSEInput = document.getElementById('wRMSE');
var selectedWMAPEInput = document.getElementById('wMAPE');

$(document).ready(function() {
    $('#uploadButton').on('click', function() {
        const formData = new FormData($('#uploadForm')[0]);

        $.ajax({
            type: 'POST',
            url: '/api/upload',
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                swal("Successful!", "File uploaded successfully!", "success").then((value) => {
                    window.location.reload();
                  });
            },
            error: function(error) {
                swal("Failed updating!", "An error occurred while uploading the file." + xhr.responseText , "error");
            }
        });
    });
  var storedFilterData = localStorage.getItem('filterData');
  var filterData = JSON.parse(storedFilterData);  
  var modelSelectIndex = 0;
  var stationSelectIndex = 0;
      $.ajax({
          url: "/api/prediction-models/getConceptDrift",
          type: "GET",
          dataType: "json",
          success: function(responseData) {
            const modelselectElement = $('#model');
            const stationselectElement = $('#weather_station');

            $.each(responseData.modelResults, function(index, item) {
                const option = $('<option>', {
                    value: item[0], 
                    text: item[0] 
                });
                modelselectElement.append(option);
                if (filterData != null){if(filterData.model == item[0]) modelSelectIndex =index;}
            });
           
            $.each(responseData.stationResults, function(index, item) {
              const option = $('<option>', {
                  value: item[1], 
                  text: item[0] 
              });
              stationselectElement.append(option);
              if (filterData != null){if(filterData.weatherStation == item[1]) stationSelectIndex =index;}
            });

            selectedWeatherStationInput.selectedIndex = stationSelectIndex;
            selectedModelInput.selectedIndex = modelSelectIndex;
                },
                error: function(error) {
                  console.log("Error fetching rainfall data:", error);
                }
              });
             
 
          if (storedFilterData) {
            // selectedWeatherStationInput.value = filterData.weatherStation;
            // selectedModelInput.value = filterData.model;
            
            selectedWRMSEInput.value = filterData.wRMSE;
            selectedWMAPEInput.value = filterData.wMAPE;
            startDateInput.value = filterData.startDate;
            endDateInput.value = filterData.endDate;
          }
          
      });
  
    // document.getElementById('submitBtn').addEventListener('click', function() {
    // if (!submitBtn.disabled){
    //     var selectedWeatherStation = document.getElementById('weather_station').value;
    //     var selectedModel = document.getElementById('model').value;
    //     var selectedWRMSE = document.getElementById('wRMSE').value;
    //     var selectedWMAPE = document.getElementById('wMAPE').value;
    //     var selectedStart = document.getElementById('start_date').value;
    //     var selectedEnd = document.getElementById('end_date').value;

    //     // var selectedWeatherStationInput = document.getElementById('weather_station');
    //     // var selectedOption = selectedWeatherStationInput.options[selectedWeatherStationInput.selectedIndex];
    //     // var selectedOptionText = selectedOption.textContent;
    //     // var selectedModelInput = document.getElementById('model');
    //     // var selectedModelText = selectedModelInput.options[selectedModelInput.selectedIndex].textContent;
    //     var filterData = {
    //         weatherStation: selectedWeatherStation,
    //         model: selectedModel,
    //         wRMSE: selectedWRMSE,
    //         wMAPE: selectedWMAPE,
    //         startDate: selectedStart,
    //         endDate: selectedEnd
    //     };

       

    //     localStorage.setItem('filterData', JSON.stringify(filterData));
      
    //    location.reload();
//     // }
// });


document.getElementById('saveBtn').addEventListener('click', function() {
    var selectedWeatherStation = selectedWeatherStationInput.value;
    var selectedModel = selectedModelInput.value;
    // var selectedWRMSE = selectedWRMSEInput.value;
    // var selectedWMAPE = selectedWMAPEInput.value;
    var selectedStart = startDateInput.value;
    var selectedEnd = endDateInput.value;
    
    // New fields
    var weatherStation=document.getElementById('weather_station').value;
    var modelNumber = document.getElementById('model').value;
    var modelStartDate = document.getElementById('start_date').value;
    var modelEndDate = document.getElementById('end_date').value;


    $.ajax({
        type: 'POST',
        url: '/saveModelData',
        data: {
            weatherStation: selectedWeatherStation,
            model: selectedModel,
            wRMSE: 0,
            wMAPE: 0,
            startDate: selectedStart,
            endDate: selectedEnd,
            modelNumber: modelNumber, // Add model number
            modelStartDate: modelStartDate, // Add model start date
            modelEndDate: modelEndDate // Add model end date
        },
        success: function(response) {
            swal("Successful!", "Model create successfully!", "success").then((value) => {
                window.location.reload();
              });
        },
        error: function(error) {
            
            swal("Failed updating!", "Not Found", "error");
        }
    });
});
