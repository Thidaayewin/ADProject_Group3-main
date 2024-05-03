//create
$(document).ready(function() {
    var storedFilterData = localStorage.getItem('filterData');
    if (storedFilterData) {
        var filterData = JSON.parse(storedFilterData);
    }
  
        $.ajax({
            url: "/api/rainfall/getConceptDrift",
            type: "GET",
            data: {
              modelId: filterData.model,
              station: filterData.weatherStation,
              modelType: filterData.startDate,
              modelType: filterData.endDate,
              rollingMSEslope: 12
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
                console.log(filterData.model);
                //rainfall values
                const actualTimestamps = responseData.actualTimestamps;
                console.log(actualTimestamps);
                const actualValues = responseData.actualValues;
                const predictedValues = responseData.predictedValues; // Adjust the constant factor as needed
                
                //Past training dates
                const previousTrainingDates = responseData.pastTrainingDates;
                console.log(filterData.weatherStation);
                console.log(filterData.model);
                console.log(responseData.pastTrainingDates);
                const latestTraining = previousTrainingDates[0];
                // console.log(responseData.pastTrainingDates[0]);
  
                const priorTrainingDates = previousTrainingDates.slice(1,previousTrainingDates.length);
                console.log(priorTrainingDates);
                
  
                // //El Nino/La Nina periods
                const elNinoList = responseData.dataDriftPeriods['El Nino'];
                const laNinaList = responseData.dataDriftPeriods['La Nina'];
                
                // El Nino 
                 const elNinoGroups = [];
                for (let i = 0; i < elNinoList.length; i += 2) {
                  if (i + 1 < elNinoList.length) {
                    const group = [elNinoList[i], elNinoList[i + 1]];
                    elNinoGroups.push(group);
                  }
                }
                // console.log("elNinoGroups:", elNinoGroups);
                
                const elNinoMarkAreaData = [];
                for (let i = 0; i < elNinoGroups.length; i++) {
                  const group = elNinoGroups[i];
                  const startDate = group[0].monthly;
                  const endDate = group[1].monthly;
  
                  elNinoMarkAreaData.push([
                    { xAxis: startDate },
                    { xAxis: endDate }
                  ]);
                }
                // console.log("elNinoMarkAreaData:", elNinoMarkAreaData);
  
                //La Nina
              const laNinaGroups = [];
              for (let i = 0; i < laNinaList.length; i += 2) {
                if (i + 1 < laNinaList.length) {
                  const group = [laNinaList[i], laNinaList[i + 1]];
                  laNinaGroups.push(group);
                }
              }
              // console.log("laNinaGroups:", laNinaGroups);
              
              const laNinaMarkAreaData = [];
  
              for (let i = 0; i < laNinaGroups.length; i++) {
                const group = laNinaGroups[i];
                const startDate = group[0].monthly;
                const endDate = group[1].monthly;
  
                // console.log("La Nina Group:", i + 1, "Start Date:", startDate, "End Date:", endDate);
  
                laNinaMarkAreaData.push([
                  { xAxis: startDate },
                  { xAxis: endDate }
                ]);
              }
  
              //Chart Details    
                 option = {
                  title: {
                    text: 'Monthly Rainfall Forecast',
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
                    data: actualTimestamps.concat([new Date('2019-01-01')]),
                    axisLabelFormatter: function(value) {
                      return value.substring(0, 7);
                    },
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
                    data: ['Actual Values', 'Forecasted Values', 'El Nino','La Nina'],
                    left:'left',
                    top:0,
                  },
                  series: [
                    {
                      name: 'Actual Values',
                      type: 'line', 
                      data: actualValues,
                      color: '#03991f',  
                      silent:true,
                      xAxisIndex: 0,
                      yAxisIndex: 0,
                      // itemStyle: {
                      //     borderWidth: 2,
                      //     borderColor: '#03991f',
                      // },
                    },
                    {
                      name: 'Forecasted Values',
                      type: 'line',
                      data: predictedValues,
                      color: '#033fad',
                      stack: 'Total',
                      silent:true,
                      xAxisIndex:0,
                      yAxisIndex:0,
                      
                    },
                    {//Latest retraining date line
                      type:'line',
                      markLine:{
                        symbol:'none',
                        data:[
                          {
                            xAxis: latestTraining, 
                            lineStyle:{
                              type:'dotted',
                              color:'#fa9b9b',
                              width: 3 ,
                            },
                            label:{
                              show:true,
                              formatter:'Latest Retraining',
                              position:'end',
                            }
                          }
                        ]
                      }
                    },
                    {//Past Training Dates
                      type: 'line',
                      markLine: {
                        symbol: 'none',
                        data: priorTrainingDates.map(date => ({ xAxis: date })),
                        lineStyle: {
                          type: 'dotted',
                          color: '#969695',
                          width: 3,
                        },
                        label: {
                          show: true,
                          formatter: 'Retraining',
                          position: 'end',
                          }
                      }
                    },
                    // El Nino periods
                    {
                      name: 'El Nino',
                      type: 'line',
                      data: [],
                      markArea: {
                        itemStyle: {
                          color: 'rgba(255, 0, 0, 0.1)' // Change this to the color you want for the El Nino areas
                        },
                        data: elNinoMarkAreaData
                      }
                    },
                    {
                      name: 'La Nina',
                      type: 'line',
                      data: [],
                      markArea: {
                        itemStyle: {
                          color: 'rgba(0, 0, 255, 0.1)' // Change this to the color you want for the La Nina areas
                        },
                        data: laNinaMarkAreaData
                      }
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
                  error: function(xhr, status, error) {
                    
                    var errorMessage = xhr.responseJSON.error; // Assuming the response JSON has a 'message' field
                    if (filterData != null){
                      swal("Failed forecasting!", errorMessage, "error");
                    }
                    
                    console.log("Error fetching rainfall data:", error);
                  }
                });
      });
    