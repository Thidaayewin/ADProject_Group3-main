var storedFilterData = localStorage.getItem('filterData');
var selectedStationInput=document.getElementById('station');
var selectedModelInput = document.getElementById('model');
var selectedWRMSEInput = document.getElementById('wRMSE');


//create
$(document).ready(function() {
    var storedFilterData = localStorage.getItem('filterData');
    
        var filterData = JSON.parse(storedFilterData);
        var modelSelectIndex = 0;
        var stationSelectIndex = 0;
        $.ajax({
          url: "/api/prediction-models/getBindData",
          type: "GET",
          dataType: "json",
          success: function(responseData) {
            const modelselectElement = $('#model');
            const stationselectElement = $('#station');
            console.log("ajax bind data");
            
            $.each(responseData.modelResults, function(index, item) {
                const option = $('<option>', {
                    value: item[0], 
                    text: item[1] 
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
           
            selectedWRMSEInput.value = filterData.wRMSE;
            selectedStationInput.value = filterData.station;
            selectedModelInput.value = filterData.model;
          }
      
    });

    document.getElementById('saveParamsBtn').addEventListener('click', function() {
      if (!saveParamsBtn.disabled){
          var selectedWeatherStation = document.getElementById('station').value;
          var selectedModel = document.getElementById('model').value;
          var selectedWRMSE = document.getElementById('wRMSE').value;

          var filterData = {
              weatherStation: selectedWeatherStation,
              model: selectedModel,
              wRMSE: selectedWRMSE,
          };
  
         localStorage.setItem('filterData', JSON.stringify(filterData));
         location.reload();
      }
  
        $.ajax({
            url: "/api/rainfall/getModelDrift",
            type: "GET",
            data: {
              modelId: filterData.model,
              station: filterData.weatherStation,
              rollingMSEslope: filterData.rollingMSEslope
          },
            dataType: "json",
            success: function(responseData) {
                var dom = document.getElementById('chart-container');
                var myChart = echarts.init(dom, null, {
                renderer: 'canvas',
                useDirtyRect: false
                });
                var app = {};
    
                var option;
                console.log("reached json");

                var modelDetails = responseData.modelName + " " + responseData.modelType;
                var rollingMSEslope=responseData.rollingMSEslope;
  
              //Chart Details    
                 option = {
                  title: {
                    text: 'Rolling RMSE Slope across All Models',
                    textStyle: {
                      fontSize: 12,
                      align: 'center',
                      verticalAlign: 'bottom',
                      lineHeight: 1
                    },
                    padding: [15, 0, 0, 0],
                    left:'center',
                    top:'20px',
                  },
                  tooltip: {
                    trigger: 'axis',
                    position:function(point,params,dom,rect,size){
                      return [size.viewSize[0]/2,20];
                    },
                    formatter: function(params){
                      var result = params[0].name + '<br/>';
                      params.forEach(function(item) {
                        result += '<div style="display: flex; justify-content: space-between;">' +
                        '<span style="text-align: left;">' + item.marker + item.seriesName + '</span>' +
                        '<span style="text-align: right;">' + item.value.toFixed(3) + '</span>' +
                        '</div>';
                      });
                      return result;
                    }
                  },
                  
                  grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '10%',
                    containLabel: true
                  },
                  toolbox: {
                    top:20,
                    feature: {
                      saveAsImage: {},
                    }
                  },
                  xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: modelDetails,
                  },
                  yAxis: {
                    type: 'value',
                    axisLabel:{
                      formatter: function(value){
                        return value.toFixed(3);
                      }
                    }
                  },
                  legend: {
                    data: ['Rolling MSE Slope'],
                    left:'left',
                    top:0,
                  },
                  series: [
                    {
                      name: 'Rolling MSE Slope',
                      type: 'line', 
                      data: rollingMSEslope,
                      color: '#03991f',  
                      silent:true,
                      xAxisIndex: 0,
                      yAxisIndex: 0,
                      // itemStyle: {
                      //     borderWidth: 2,
                      //     borderColor: '#03991f',
                      // },
                    },
                ]
              };
    
            option.dataZoom = [
              {
                type: 'inside',
                xAxisIndex: 0,
                filterMode: 'filter'
              },
              {
                type: 'slider',
                xAxisIndex: 0,
                filterMode: 'filter',
                start: 0,
                end: 100,
              }
            ];
            
            if (option && typeof option === 'object') {
              myChart.setOption(option);
            }
            
            window.addEventListener('resize', myChart.resize);
                  },
                  error: function(xhr, status, error) {
                    
                    var errorMessage = xhr.responseJSON.error; // Assuming the response JSON has a 'message' field
                    if (filterData != null){
                      swal("Failed forecasting!", errorMessage, "error");
                    }
                    
                    console.log("Error fetching rainfall data:", error);
                  }
                });
      });
    