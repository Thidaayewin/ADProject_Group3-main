//create
$(document).ready(function() {
    
  
    $.ajax({
        url: "/api/rainfall/getAllRainfallData",
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
            
            // Extract timestamps and values for actual data
            const actualTimestamps = responseData.actualTimestamps;
            const actualValues = responseData.actualValues;

            // Generate predicted data based on actual data 
            const predictedValues = responseData.predictedValues; // Adjust the constant factor as needed
            
            option = {
            color: ['#80FFA5', '#00DDFF'],
            title: {
                text: 'Actual vs Predicted Rainfall'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
                }
            },
            legend: {
                data: ['Actual Rainfall', 'Predicted Rainfall']
            },
            toolbox: {
                feature: {
                saveAsImage: {}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                type: 'category',
                boundaryGap: false,
                data: actualTimestamps
                }
            ],
            yAxis: [
                {
                type: 'value'
                }
            ],
            series: [
                {
                name: 'Actual Rainfall',
                type: 'line',
                stack: 'Total',
                smooth: true,
                lineStyle: {
                    width: 0
                },
                showSymbol: false,
                areaStyle: {
                    opacity: 0.8,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: 'rgb(128, 255, 165)'
                    },
                    {
                        offset: 1,
                        color: 'rgb(1, 191, 236)'
                    }
                    ])
                },
                emphasis: {
                    focus: 'series'
                },
                data: actualValues,
                },
                {
                name: 'Predicted Rainfall',
                type: 'line',
                stack: 'Total',
                smooth: true,
                lineStyle: {
                    width: 0
                },
                showSymbol: false,
                areaStyle: {
                    opacity: 0.8,
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: 'rgb(0, 221, 255)'
                    },
                    {
                        offset: 1,
                        color: 'rgb(77, 119, 255)'
                    }
                    ])
                },
                emphasis: {
                    focus: 'series'
                },
                data: predictedValues
                }
            ]
            };

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


// var dom = document.getElementById('chart-container');
// var myChart = echarts.init(dom, null, {
//   renderer: 'canvas',
//   useDirtyRect: false
// });
// var app = {};

// var option;

// // Assuming you have actual and predicted data as follows:
// const actualData = [
//   ['2017-01-01T12:55:00+08:00', 0.2],
//   ['2017-01-01T13:00:00+08:00', 0.2],
//   ['2017-01-01T16:35:00+08:00', 0.4],
//   ['2017-01-01T16:40:00+08:00', 1],
//   ['2017-01-01T16:45:00+08:00', 0.4],
//   ['2017-01-01T16:50:00+08:00', 0.2],
//   ['2017-01-01T17:55:00+08:00', 0.2],
//   ['2017-01-01T19:25:00+08:00', 0.2],
//   ['2017-01-01T19:30:00+08:00', 0.2],
//   ['2017-01-01T19:35:00+08:00', 0.2],
//   ['2017-01-01T19:40:00+08:00', 0.2],
//   ['2017-01-01T19:55:00+08:00', 0.2],
//   ['2017-01-01T20:15:00+08:00', 0.2],
//   ['2017-01-01T21:05:00+08:00', 0.2],
//   ['2017-01-02T07:15:00+08:00', 0.2],
//   ['2017-01-02T15:40:00+08:00', 0.6],
//   ['2017-01-02T15:45:00+08:00', 0.4],
//   ['2017-01-02T15:50:00+08:00', 0.8],
//   ['2017-01-02T15:55:00+08:00', 0.8],
//   ['2017-01-02T16:00:00+08:00', 2.6],
//   ['2017-01-02T16:05:00+08:00', 1.2],
//   ['2017-01-02T16:10:00+08:00', 0.6],
//   ['2017-01-02T16:15:00+08:00', 2],
//   ['2017-01-02T16:20:00+08:00', 4.2],
//   ['2017-01-02T16:25:00+08:00', 0.8],
//   ['2017-01-02T16:30:00+08:00', 2.4],
//   ['2017-01-02T16:35:00+08:00', 1.6],
//   ['2017-01-02T16:40:00+08:00', 1.2],
//   ['2017-01-02T16:45:00+08:00', 0.8],
//   ['2017-01-02T16:50:00+08:00', 0.6],
//   ['2017-01-02T16:55:00+08:00', 0.4],
//   ['2017-01-02T17:00:00+08:00', 0.4],
//   ['2017-01-02T17:05:00+08:00', 1.8],
//   ['2017-01-02T17:10:00+08:00', 0.6],
//   ['2017-01-02T17:15:00+08:00', 0.4],
//   ['2017-01-02T17:20:00+08:00', 0.2],
//   ['2017-01-02T17:50:00+08:00', 0.2]
// ];



// // Extract timestamps and values for actual data
// const actualTimestamps = actualData.map((entry) => entry[0]);
// const actualValues = actualData.map((entry) => entry[1]);


// // Generate predicted data based on actual data (multiply actual values by a constant factor)
// const predictedValues = actualValues.map((value) => value * 1.5); // Adjust the constant factor as needed


// option = {
//   color: ['#80FFA5', '#00DDFF'],
//   title: {
//     text: 'Actual vs Predicted Rainfall'
//   },
//   tooltip: {
//     trigger: 'axis',
//     axisPointer: {
//       type: 'cross',
//       label: {
//         backgroundColor: '#6a7985'
//       }
//     }
//   },
//   legend: {
//     data: ['Actual Rainfall', 'Predicted Rainfall']
//   },
//   toolbox: {
//     feature: {
//       saveAsImage: {}
//     }
//   },
//   grid: {
//     left: '3%',
//     right: '4%',
//     bottom: '3%',
//     containLabel: true
//   },
//   xAxis: [
//     {
//       type: 'category',
//       boundaryGap: false,
//       data: actualTimestamps
//     }
//   ],
//   yAxis: [
//     {
//       type: 'value'
//     }
//   ],
//   series: [
//     {
//       name: 'Actual Rainfall',
//       type: 'line',
//       stack: 'Total',
//       smooth: true,
//       lineStyle: {
//         width: 0
//       },
//       showSymbol: false,
//       areaStyle: {
//         opacity: 0.8,
//         color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
//           {
//             offset: 0,
//             color: 'rgb(128, 255, 165)'
//           },
//           {
//             offset: 1,
//             color: 'rgb(1, 191, 236)'
//           }
//         ])
//       },
//       emphasis: {
//         focus: 'series'
//       },
//       data: actualValues,
//     },
//     {
//       name: 'Predicted Rainfall',
//       type: 'line',
//       stack: 'Total',
//       smooth: true,
//       lineStyle: {
//         width: 0
//       },
//       showSymbol: false,
//       areaStyle: {
//         opacity: 0.8,
//         color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
//           {
//             offset: 0,
//             color: 'rgb(0, 221, 255)'
//           },
//           {
//             offset: 1,
//             color: 'rgb(77, 119, 255)'
//           }
//         ])
//       },
//       emphasis: {
//         focus: 'series'
//       },
//       data: predictedValues
//     }
//   ]
// };

// if (option && typeof option === 'object') {
//   myChart.setOption(option);
// }

// window.addEventListener('resize', myChart.resize);