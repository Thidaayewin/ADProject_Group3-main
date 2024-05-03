var storedFilterData = localStorage.getItem('filterData');
const startDateInput = document.getElementById('start_date');
const endDateInput = document.getElementById('end_date');
const dateError = document.getElementById('dateError');

var selectedWeatherStationInput = document.getElementById('weather_station');
var selectedModelInput = document.getElementById('model');
var selectedModelNumberInput = document.getElementById('modelNumber');
var selectedWRMSEInput = document.getElementById('wRMSE');
var selectedWMAPEInput = document.getElementById('wMAPE');

$(document).ready(function() {
  var storedFilterData = localStorage.getItem('filterData');
  var filterData = JSON.parse(storedFilterData);
  var modelSelectIndex = 0;
  var modelNumberSelectIndex = 0;
  var stationSelectIndex = 0;
      $.ajax({
          url: "/api/prediction-models/getBindData",
          type: "GET",
          dataType: "json",
          success: function(responseData) {
            const modelselectElement = $('#model');
            const modelNumsselectElement=$('#modelNumber')
            const stationselectElement = $('#weather_station');
        

            // Create an object to store unique values
            const uniqueModelTypes = {};

            // Model Type - Neural Prophet etc
            $.each(responseData.modelResults, function(index, item) {
                // Check if the value is not in the uniqueValues object
                if (!uniqueModelTypes[item[0]]) {
                  uniqueModelTypes[item[0]] = true; // Add the value to the uniqueValues object
                    
                    const option = $('<option>', {
                        value: item[0], 
                        text: item[1] 
                    });
                    modelselectElement.append(option);
                    
                    if (filterData != null && filterData.model == item[0]) {
                        modelSelectIndex = index;
                    }
                }
            });

            //Model Number - 0600/0601 etc
            $.each(responseData.modelResults, function(index, item) {
              const option = $('<option>', {
                  value: item[0], 
                  text: item[0] 
              });
              modelNumsselectElement.append(option);
              if (filterData != null){if(filterData.model == item[0]) modelNumberSelectIndex =index;}
          });
           
          //Station
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
            selectedModelNumberInput.selectedIndex = modelNumberSelectIndex;

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
  
    document.getElementById('submitBtn').addEventListener('click', function() {
    if (!submitBtn.disabled){
        var selectedWeatherStation = document.getElementById('weather_station').value;
        var selectedModelNum=document.getElementById('modelNumber').value;
        var selectedModel = document.getElementById('model').value;
        var selectedWRMSE = document.getElementById('wRMSE').value;
        var selectedWMAPE = document.getElementById('wMAPE').value;
        var selectedStart = document.getElementById('start_date').value;
        var selectedEnd = document.getElementById('end_date').value;

        // var selectedWeatherStationInput = document.getElementById('weather_station');
        // var selectedOption = selectedWeatherStationInput.options[selectedWeatherStationInput.selectedIndex];
        // var selectedOptionText = selectedOption.textContent;
        // var selectedModelInput = document.getElementById('model');
        // var selectedModelText = selectedModelInput.options[selectedModelInput.selectedIndex].textContent;
        var filterData = {
            weatherStation: selectedWeatherStation,
            modelNum: selectedModelNum,
            model: selectedModel,
            wRMSE: selectedWRMSE,
            wMAPE: selectedWMAPE,
            startDate: selectedStart,
            endDate: selectedEnd
        };
        console.log(filterData);
       localStorage.setItem('filterData', JSON.stringify(filterData));
       location.reload();
    }
});
