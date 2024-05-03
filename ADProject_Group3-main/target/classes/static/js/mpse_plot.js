//create
$(document).ready(function() {
  var storedFilterData = localStorage.getItem('filterData');
    if (storedFilterData) {
        var filterData = JSON.parse(storedFilterData);
    }
    $.ajax({
        url: "/api/rainfall/getRainfallMPSE",
        type: "GET",
        data: {
          model: filterData.model,
          modelNum: filterData.modelNum,

          station: filterData.weatherStation,
          start_date: filterData.startDate,
          end_date: filterData.endDate,
          periods: 12,
          wRMSE:filterData.wRMSE,
          wMAPE:filterData.wMAPE
      },
        dataType: "json",
        success: function(responseData) {
            const actualTimestamps = responseData.actualTimestamps;
            const actualValues = responseData.actualValues;
            
            var dom = document.getElementById('mpse-container');
            var myChart = echarts.init(dom, null, {
              renderer: 'canvas',
              useDirtyRect: false
            });
            var app = {};

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
            var option;

            option = {
              title: {
                text: 'Mean Absolute Percentage Error (MAPE) Over Time',
                textStyle: {
                  fontSize: 12,
                  align: 'center', 
                  verticalAlign: 'bottom', 
                  lineHeight: 1 
                },
                padding: [15, 0, 0, 0], // Add 10 pixels of top padding
                left:'center',
                top:'20px',
              },
              tooltip: {
                trigger: 'axis',
                position:function(point,params,dom,rect,size){
                  return [size.viewSize[0]/2,20];
                },
                axisPointer: {
                  type: 'cross',
                  label: {
                    backgroundColor: '#6a7985'
                  }
                }
              },
              // legend: {
              //   data: ['RMSE']
              // },
              toolbox: {
                feature: {
                  saveAsImage: {}
                }
              },
              grid: {
                left: '3%',
                right: '4%',
                bottom: '20%',
                containLabel: true
              },
              xAxis: [
                {
                  type: 'category',
                  boundaryGap: false,
                  data: actualTimestamps,
                  name:"Monthly"
                }
              ],
              yAxis: [
                {
                  type: 'value',
                  name:"MPSE"
                }
              ],
              series: [
                {
                  name: 'MPSE',
                  type: 'line',
                  stack: 'Total',
                  areaStyle: {},
                  emphasis: {
                    focus: 'series'
                  },
                  data: actualValues
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
                end: 100
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

  
  