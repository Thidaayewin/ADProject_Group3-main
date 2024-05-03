
//create
$(document).ready(function() {
  localStorage.removeItem('filterData');
    $.ajax({
        url: "/api/rainfall/getHistoryLog",
        type: "GET",
        dataType: "json",
        success: function(responseData) {
            // updateTable(responseData);
            responseData.forEach(function(item) {
            var row = '<tr>' +
            '<td>' + item.trainId + '</td>' +
            '<td>' + item.station.stationName + '</td>' +
            '<td>' + item.modelNumber + '</td>' +
            // '<td>' + item.modelType + '</td>' +
            '<td>' + item.startDate + '</td>' +
            '<td>' + item.endDate + '</td>' +
            '<td>' + (item.isDefault ? 'Yes' : '')+ '</td>' 
            '</tr>';

            $('#modelTableBody').append(row);
            });
        },
        error: function(error) {
          console.log("Error fetching rainfall data:", error);
        }
      });

      $.ajax({
        url: "/api/rainfall/getDefaultPredictedAndActualRainfallData",
        type: "GET",
        dataType: "json",
        success: function(responseData) {
            var dom = document.getElementById('chart-container');
            var myChart = echarts.init(dom, null, {
            renderer: 'canvas',
            useDirtyRect: false
            });
            var app = {};
  
            var option;
            
            const actualTimestamps = responseData.actualTimestamps;
            const actualValues = responseData.actualValues;
  
            const predictedValues = responseData.predictedValues; // Adjust the constant factor as needed
            
            
      option = {
          title: {
            text: 'Actual vs Predicted Rainfall',
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
          },
          legend: {
            data: ['Actual Rainfall', 'Predicted Rainfall'],
            left:'left',
            top:0,
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
            data: actualTimestamps 
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: 'Actual Rainfall',
              type: 'line',
              data: actualValues,
              stack: 'Total'
            },
            {
              name: 'Predicted Rainfall',
              type: 'line',
              data: predictedValues,
              stack: 'Total'
            }
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
              error: function(error) {
                console.log("Error fetching rainfall data:", error);
              }
            });
  });

  
  
 
