import { Component, OnInit, Inject } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import Highcharts from 'highcharts';
import Highcharts3D from 'highcharts/highcharts-3d';
Highcharts3D(Highcharts);

import HighchartsMore from 'highcharts/highcharts-more';
import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);

@Component({
  selector: 'app-chart-from-kpi-builder',
  templateUrl: './chart-from-kpi-builder.component.html',
  styleUrls: ['./chart-from-kpi-builder.component.css']
})
export class ChartFromKpiBuilderComponent implements OnInit {
  Highcharts: typeof Highcharts = Highcharts;


  public getQueryName = GlobalConstants.getQueryNameApi;
  public getchartType = GlobalConstants.selectChartType;
  chartObject: any[] = [];
  public agGridSelectedNodes: any = '';
  chartForm = new UntypedFormGroup({
    title: new UntypedFormControl(''),
    query: new UntypedFormControl(''),
    chartType: new UntypedFormControl(''),

  });
  barRecords: any[] = [];
  radarRecords: any[] = [];
  constructor(private http: HttpClient, public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog, private snackBar: MatSnackBar,
    private _Activatedroute: ActivatedRoute,
    public datepipe: DatePipe,
    private route: Router) { }
  ids: string[] = [];
  names: number[] = [];
  gaugeType: string[] = [];
  gaugeValue: number[] = [];
  gaugeLabel: string[] = [];
  gaugeAppendText: string[] = [];

  chartType: number;
  ngOnInit(): void {
    for (let i = 0; i < this.data.length; i++) {
      this.chartType = this.data[i].chartType;
      let ids = this.ids
      let chartType1;


      const trackColors = Highcharts.getOptions().colors.map(color =>
        new Highcharts.Color(color).setOpacity(0.3).get());

      const getNow = () => {
        const now = new Date();
    
        return {
            date: now,
            hours: now.getHours() + now.getMinutes() / 60,
            minutes: now.getMinutes() * 12 / 60 + now.getSeconds() * 12 / 3600,
            seconds: now.getSeconds() * 12 / 60
        };
    };
    
    let now = getNow();
    


      if (this.chartType == 1) {
        chartType1 = 'heatmap';
      } else if (this.chartType == 2) {
      chartType1 = 'pie';
      } else if (this.chartType == 3) {
        chartType1 = 'bar';
      } else if (this.chartType == 4) {
        chartType1 = 'line';
      } else if (this.chartType == 5) {
        chartType1 = 'area';
      } else if (this.chartType == 6) {
        chartType1 = 'scatter';
      } else if (this.chartType == 7) {
        chartType1 = 'column';
      }else if (this.chartType == 8) {
        chartType1= 'semiPie';
      }else if (this.chartType == 14) {
        chartType1= 'VU solid';
      }else if (this.chartType == 15) {
        chartType1= 'VU meter';
      }else if (this.chartType == 16) {
        chartType1= 'Speedometer';
      }
      
      else if (this.chartType == 17) {
        chartType1= 'Dual Axes Speedometer';
      }else if (this.chartType == 18) {
        chartType1= 'Speedometer solid';
      }
      
      // else if (this.chartType == 19) {
      //   chartType1= 'Multiple KPI gauge';
      // }else if (this.chartType == 20) {
      //   chartType1= 'clock gauge';
      // }

      if (chartType1 == 'heatmap') {
        this.chartObject.push(
          {
            chart: {
              type: 'heatmap',
              marginTop: 40,
              marginBottom: 80,
              plotBorderWidth: 1
          },
      
      
          title: {
              text: 'Sales per employee per weekday',
              style: {
                  fontSize: '1em'
              }
          },
      
          xAxis: {
              categories: [
                  'Alexander', 'Marie', 'Maximilian', 'Sophia', 'Lukas',
                  'Maria', 'Leon', 'Anna', 'Tim', 'Laura'
              ]
          },
      
          yAxis: {
              categories: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'],
              title: null,
              reversed: true
          },
      
          accessibility: {
              point: {
                  descriptionFormat: '{(add index 1)}. ' +
                      '{series.xAxis.categories.(x)} sales ' +
                      '{series.yAxis.categories.(y)}, {value}.'
              }
          },
      
          colorAxis: {
              min: 0,
              minColor: '#FFFFFF',
              maxColor: Highcharts.getOptions().colors[0]
          },
      
          legend: {
              align: 'right',
              layout: 'vertical',
              margin: 0,
              verticalAlign: 'top',
              y: 25,
              symbolHeight: 280
          },
      
          tooltip: {
              format: '<b>{series.xAxis.categories.(point.x)}</b> sold<br>' +
                  '<b>{point.value}</b> items on <br>' +
                  '<b>{series.yAxis.categories.(point.y)}</b>'
          },
      
          series: [{
              name: 'Sales per employee',
              borderWidth: 1,
              data: [
                  [0, 0, 10], [0, 1, 19], [0, 2, 8], [0, 3, 24], [0, 4, 67],
                  [1, 0, 92], [1, 1, 58], [1, 2, 78], [1, 3, 117], [1, 4, 48],
                  [2, 0, 35], [2, 1, 15], [2, 2, 123], [2, 3, 64], [2, 4, 52],
                  [3, 0, 72], [3, 1, 132], [3, 2, 114], [3, 3, 19], [3, 4, 16],
                  [4, 0, 38], [4, 1, 5], [4, 2, 8], [4, 3, 117], [4, 4, 115],
                  [5, 0, 88], [5, 1, 32], [5, 2, 12], [5, 3, 6], [5, 4, 120],
                  [6, 0, 13], [6, 1, 44], [6, 2, 88], [6, 3, 98], [6, 4, 96],
                  [7, 0, 31], [7, 1, 1], [7, 2, 82], [7, 3, 32], [7, 4, 30],
                  [8, 0, 85], [8, 1, 97], [8, 2, 123], [8, 3, 64], [8, 4, 84],
                  [9, 0, 47], [9, 1, 114], [9, 2, 31], [9, 3, 48], [9, 4, 91]
              ],
              dataLabels: {
                  enabled: true,
                  color: '#000000'
              } }],
    
              responsive: {
                  rules: [{
                      condition: {
                          maxWidth: 500
                      },
                      chartOptions: {
                          yAxis: {
                              labels: {
                                  format: '{substr value 0 1}'
                              }
                          }
                      }
                  }]
              }
          })
      } else if (chartType1 == 'pie') {
        for (let j = 0; j < this.data[i].records.length; j++) {
          this.ids.push(this.data[i].records[i].NAME);
          this.names.push(parseInt(this.data[i].records[j].Y));
        }
  
  
        let data1 = this.names.map((id, index) => {
          return { name: this.ids[index], y: id };
        });
    
        this.chartObject.push(
          {
            chart: { type: 'pie' },
            title: { text: this.data[0].records[0].TITLE },
            series: [{
              name: 'Serie',
              data:data1,
            }]
          }
        );
      } else if (chartType1 == 'bar') {
        console.log('ids------>',this.ids);
        console.log('names--------->',this.names)
        for (let j = 0; j < this.data[i].records.length; j++) {
          this.ids.push(this.data[i].records[i].ID);
          this.names.push(Number(this.data[i].records[j].NAME));
        }
        console.log('ids---ad--->',this.ids);
        console.log('names------ad--->',this.names)
        this.chartObject.push(
          {
            chart: {type: chartType1},
            title: {text: this.data[i].records[0].TITLE},
            xAxis: {
              categories: this.ids
            },
            yAxis: {
              title: {
                text: 'Value'
              }
            },
            series: [{
              name: 'Series 1',
              data: this.names,
            }]
          }
        );

      }else if(chartType1 == 'line'){
        for (let j = 0; j < this.data[i].records.length; j++) {
          this.ids.push(this.data[i].records[i].ID);
          this.names.push(Number(this.data[i].records[j].NAME));
        }
        this.chartObject.push(
          {
            chart: { type: 'line' },
            title: { text: this.data[i].records[0].TITLE },
            xAxis: {
              categories: this.ids
            },
            yAxis: {
              title: {
                text: 'Value'
              }
            },
            series: [{
              name: 'Series 1',
              data: this.names,
            }]
          }
        );
      }else if(chartType1 == 'area'){
        for (let j = 0; j < this.data[i].records.length; j++) {
          this.ids.push(this.data[i].records[i].ID);
          this.names.push(Number(this.data[i].records[j].NAME));
        }
        this.chartObject.push(
          {
            chart: { type: 'area' },
            title: { text: this.data[i].records[0].TITLE },
            xAxis: {
              categories: this.ids
            },
            yAxis: {
              title: {
                text: 'Value'
              }
            },
            series: [{
              name: 'Series 1',
              data: this.names,
            }]
          }
        );
      }else if(chartType1 == 'scatter'){
        for (let j = 0; j < this.data[i].records.length; j++) {
          this.ids.push(this.data[i].records[i].ID);
          this.names.push(Number(this.data[i].records[j].NAME));
        }
        this.chartObject.push(
          {
            chart: { type: 'scatter' },
            title: { text: this.data[i].records[0].TITLE },
            xAxis: {
              title: {
                text: 'X Axis'
              }
            },
            yAxis: {
              title: {
                text: 'Y Axis'
              }
            },
            series: [{
              name: 'Series 1',
              data: this.names,
            }]
          }
        );
      }else if(chartType1 =='semiPie'){
        const transformedData = this.data[i].records.map((item:any) => [item.ID, parseFloat(item.NAME)]);
        this.chartObject.push({  chart: {
          plotBackgroundColor: null,
          plotBorderWidth: 0,
          plotShadow: false,
          type:'semiPie'
      },
      title: {
          text: 'Browser<br>shares<br>January<br>2024',
          align: 'center',
          verticalAlign: 'middle',
          y: 60,
          style: {
              fontSize: '1.1em'
          }
      },
      tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      accessibility: {
          point: {
              valueSuffix: '%'
          }
      },
      plotOptions: {
          pie: {
              dataLabels: {
                  enabled: true,
                  distance: -50,
                  style: {
                      fontWeight: 'bold',
                      color: 'white'
                  }
              },
              startAngle: -90,
              endAngle: 90,
              center: ['50%', '75%'],
              size: '110%'
          }
      },
      series: [{
          type: 'pie',
          name: 'Browser share',
          innerSize: '50%',
          data: transformedData
      }]
  });
      }else if(chartType1 == 'column'){
        for (let j = 0; j < this.data[i].records.length; j++) {
          this.ids.push(this.data[i].records[i].ID);
          this.names.push(Number(this.data[i].records[j].NAME));
        }
        this.chartObject.push({
          chart: { type: 'column' },
          title: { text: 'Pareto Chart' },
          xAxis: [{ categories: this.ids}],
          yAxis: [{
            title: { text: 'Primary Axis' }
          }, {
            title: { text: 'Pareto' },
            opposite: true
          }],
          tooltip: { shared: true },
          series: [{
            name: 'Primary',
            type: 'column',
            data: this.names
          }, {
            name: 'Pareto',
            type: 'line',
            yAxis: 1,
            data:this.names,
            zIndex: 10,
            dashStyle: 'ShortDot'
          }]
        });
  
      }else if(chartType1 == 'VU solid'){
        // for (let j = 0; j < this.data[i].records.length; j++) {
        //   this.ids.push(this.data[i].records[i].ID);
        //   this.names.push(Number(this.data[i].records[j].NAME));
        // }
        this.chartObject.push
        ({
          chart: {
            type: 'solidgauge',
            height: 400, // Increased height to accommodate the larger gauge
            spacingTop: 50, // Add spacing to avoid clipping at the top
            spacingBottom: 50, // Add spacing to avoid clipping at the bottom
            identifier:'VU solid'
        },
    
        title: {
            text: 'VU solid',
            style: {
                fontSize: '28px' // Adjusted font size to match the increased size
            }
        },
    
        pane: {
            center: ['50%', '50%'], // Center the gauge vertically and horizontally
            size: '100%', // Adjust size for the gauge to ensure it's centered properly
            startAngle: -90,
            endAngle: 90,
            background: {
                backgroundColor: Highcharts.defaultOptions.legend.backgroundColor || '#fafafa',
                borderRadius: 5,
                innerRadius: '60%',
                outerRadius: '100%',
                shape: 'arc'
            }
        },
    
        exporting: {
            enabled: false
        },
    
        tooltip: {
            enabled: false
        },
    
        yAxis: {
            min: -20,
            max: 6,
            stops: [
                [0.1, '#55BF3B'], // green
                [0.5, '#DDDF0D'], // yellow
                [0.9, '#DF5353'] // red
            ],
            lineWidth: 0,
            tickWidth: 0,
            minorTickInterval: null,
            tickAmount: 2,
            labels: {
                y: 10, // Adjusted label position to match the new size
                rotation: 'auto',
                distance: 0 // Increased distance to match new size
            },
            title: {
                text: 'VU<br/><span style="font-size:20px">Channel A</span>', // Adjusted font size
                y: 50 // Adjusted vertical position
            }
        },
    
        plotOptions: {
            solidgauge: {
                dataLabels: {
                    enabled: false
                },
                dial: {
                    radius: '85%' // Adjusted radius for the larger gauge
                }
            }
        },
    
        series: [{
            name: 'Channel A',
            data: [-20],
            dataLabels: {
                format: '<div style="text-align:center">' +
                        '<span style="font-size:50px">{y}</span><br/>' +
                        '<span style="font-size:24px;opacity:0.4">Value</span>' +
                        '</div>'
            }
          }]
        });
  
      }else if(chartType1 == 'VU meter'){
        // for (let j = 0; j < this.data[i].records.length; j++) {
        //   this.ids.push(this.data[i].records[i].ID);
        //   this.names.push(Number(this.data[i].records[j].NAME));
        // }
        this.chartObject.push
        ({
          chart:
        {
          type: 'gauge',
          plotBorderWidth: 1,
          plotBackgroundColor: {
            linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1 },
            stops: [
              [0, '#FFF4C6'],
              [0.3, '#FFFFFF'],
              [1, '#FFF4C6']
            ]
            },
            plotBackgroundImage: null,
            height: 200,
            identifier:'VU meter'
        },
        
        title: {
            text: 'VU meter',
            style: {
                fontSize: '14px' // Font size adjusted to match the gauge
            }
        },
        
        pane: {
            startAngle: -45,
            endAngle: 45,
            background: null,
            center: ['50%', '75%'], // Adjusted center position to start lower
            size: '100%' // Increased size to make the gauge a bit larger
        },
        
        exporting: {
            enabled: false
        },
        
        tooltip: {
            enabled: false
        },
        
        yAxis: {
            min: -20,
            max: 6,
            minorTickPosition: 'outside',
            tickPosition: 'outside',
            labels: {
                rotation: 'auto',
                distance: 10 // Adjusted distance to fit the gauge
            },
            plotBands: [{
                from: 0,
                to: 6,
                color: '#C02316',
                innerRadius: '100%',
                outerRadius: '105%'
            }],
            title: {
                text: 'VU<br/><span style="font-size:10px">Channel A</span>',
                y: -10 // Adjusted y position to fit the gauge better
            }
        },
        
        plotOptions: {
            gauge: {
                dataLabels: {
                    enabled: false
                },
                dial: {
                    radius: '85%' // Adjusted radius to make the gauge larger
                }
            }
        },
        
        series: [{
            name: 'Channel A',
            data: [-20]
        }]
        });
  
      }else if(chartType1 == 'Speedometer'){
        // for (let j = 0; j < this.data[i].records.length; j++) {
        //   this.ids.push(this.data[i].records[i].ID);
        //   this.names.push(Number(this.data[i].records[j].NAME));
        // }
        this.chartObject.push
        ({
          chart: {
            renderTo: 'container', // Make sure this matches your container ID
            type: 'gauge',
            plotBackgroundColor: null,
            plotBackgroundImage: null,
            plotBorderWidth: null,
            plotShadow: false,
            height: '200px',
            identifier: 'Speedometer'
          },
          title: {
            text: 'Speedometer'
          },
          pane: {
            startAngle: -90,
            endAngle: 90,
            background: {
              backgroundColor: Highcharts.defaultOptions.legend.backgroundColor || '#EEE',
              innerRadius: '60%',
              outerRadius: '100%',
              shape: 'arc'
            }
          },
          yAxis: {
            min: 0,
            max: 200,
            tickPixelInterval: 30,
            tickWidth: 2,
            tickPosition: 'inside',
            tickLength: 10,
            labels: {
              step: 2,
              rotation: 'auto',
              style: {
                fontSize: '10px'
              }
            },
            title: {
              text: 'km/h',
              style: {
                fontSize: '17px'
              }
            }
          },
          series: [{
            name: 'Speed',
            data: [140],
            dataLabels: {
              format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
                '<div style="opacity:0.4; font-size:12px; text-align:center">km/h</div></div>'
            },
            dial: {
              baseWidth: 10,
              rearLength: 0
            }
          }]
        });
  
      }else if(chartType1 == 'Dual Axes Speedometer'){
        // for (let j = 0; j < this.data[i].records.length; j++) {
        //   this.ids.push(this.data[i].records[i].ID);
        //   this.names.push(Number(this.data[i].records[j].NAME));
        // }
        this.chartObject.push
        ({
          chart: {
            renderTo: 'container', // Make sure this matches your container ID
            type: 'gauge',
            plotBackgroundColor: null,
            plotBackgroundImage: null,
            plotBorderWidth: null,
            plotShadow: false,
            height: '200px',
            identifier: 'Speedometer'
          },
          title: {
            text: 'Speedometer'
          },
          pane: {
            startAngle: -90,
            endAngle: 90,
            background: {
              backgroundColor: Highcharts.defaultOptions.legend.backgroundColor || '#EEE',
              innerRadius: '60%',
              outerRadius: '100%',
              shape: 'arc'
            }
          },
          yAxis: {
            min: 0,
            max: 200,
            tickPixelInterval: 30,
            tickWidth: 2,
            tickPosition: 'inside',
            tickLength: 10,
            labels: {
              step: 2,
              rotation: 'auto',
              style: {
                fontSize: '10px'
              }
            },
            title: {
              text: 'km/h',
              style: {
                fontSize: '17px'
              }
            }
          },
          series: [{
            name: 'Speed',
            data: [140],
            dataLabels: {
              format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
                '<div style="opacity:0.4; font-size:12px; text-align:center">km/h</div></div>'
            },
            dial: {
              baseWidth: 10,
              rearLength: 0
            }
          }]
        });
  
      }else if(chartType1 == 'Speedometer solid'){
        // for (let j = 0; j < this.data[i].records.length; j++) {
        //   this.ids.push(this.data[i].records[i].ID);
        //   this.names.push(Number(this.data[i].records[j].NAME));
        // }
        this.chartObject.push
        ({
          chart: {
            renderTo: 'container', // Make sure this matches your container ID
            type: 'gauge',
            plotBackgroundColor: null,
            plotBackgroundImage: null,
            plotBorderWidth: null,
            plotShadow: false,
            height: '200px',
            identifier: 'Speedometer'
          },
          title: {
            text: 'Speedometer'
          },
          pane: {
            startAngle: -90,
            endAngle: 90,
            background: {
              backgroundColor: Highcharts.defaultOptions.legend.backgroundColor || '#EEE',
              innerRadius: '60%',
              outerRadius: '100%',
              shape: 'arc'
            }
          },
          yAxis: {
            min: 0,
            max: 200,
            tickPixelInterval: 30,
            tickWidth: 2,
            tickPosition: 'inside',
            tickLength: 10,
            labels: {
              step: 2,
              rotation: 'auto',
              style: {
                fontSize: '10px'
              }
            },
            title: {
              text: 'km/h',
              style: {
                fontSize: '17px'
              }
            }
          },
          series: [{
            name: 'Speed',
            data: [140],
            dataLabels: {
              format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
                '<div style="opacity:0.4; font-size:12px; text-align:center">km/h</div></div>'
            },
            dial: {
              baseWidth: 10,
              rearLength: 0
            }
          }]
        });
  
      } 
      
      // else if(chartType1 == 'Multiple KPI gauge'){
      //   // for (let j = 0; j < this.data[i].records.length; j++) {
      //   //   this.ids.push(this.data[i].records[i].ID);
      //   //   this.names.push(Number(this.data[i].records[j].NAME));
      //   // }
      //   this.chartObject.push
      //   ({
      //     chart: {
      //       renderTo: 'container', // Make sure this matches your container ID
      //       type: 'gauge',
      //       plotBackgroundColor: null,
      //       plotBackgroundImage: null,
      //       plotBorderWidth: null,
      //       plotShadow: false,
      //       height: '200px',
      //       identifier: 'Speedometer'
      //     },
      //     title: {
      //       text: 'Speedometer'
      //     },
      //     pane: {
      //       startAngle: 0,
      //       endAngle: 360,
      //       background: [{ // Track for Conversion
      //           outerRadius: '112%',
      //           innerRadius: '88%',
      //           backgroundColor: trackColors[0],
      //           borderWidth: 0
      //       }, { // Track for Engagement
      //           outerRadius: '87%',
      //           innerRadius: '63%',
      //           backgroundColor: trackColors[1],
      //           borderWidth: 0
      //       }, { // Track for Feedback
      //           outerRadius: '62%',
      //           innerRadius: '38%',
      //           backgroundColor: trackColors[2],
      //           borderWidth: 0
      //       }]
      //     },
      //     yAxis: {
      //       min: 0,
      //       max: 200,
      //       tickPixelInterval: 30,
      //       tickWidth: 2,
      //       tickPosition: 'inside',
      //       tickLength: 10,
      //       labels: {
      //         step: 2,
      //         rotation: 'auto',
      //         style: {
      //           fontSize: '10px'
      //         }
      //       },
      //       title: {
      //         text: 'km/h',
      //         style: {
      //           fontSize: '17px'
      //         }
      //       }
      //     },
      //     series: [{
      //       name: 'Speed',
      //       data: [140],
      //       dataLabels: {
      //         format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
      //           '<div style="opacity:0.4; font-size:12px; text-align:center">km/h</div></div>'
      //       },
      //       dial: {
      //         baseWidth: 10,
      //         rearLength: 0
      //       }
      //     }]
      //   });
  
      // } else if(chartType1 == 'clock gauge'){
      //   // for (let j = 0; j < this.data[i].records.length; j++) {
      //   //   this.ids.push(this.data[i].records[i].ID);
      //   //   this.names.push(Number(this.data[i].records[j].NAME));
      //   // }
      //   this.chartObject.push
      //   ({
      //     chart: {
      //       type: 'gauge',
      //       plotBackgroundColor: null,
      //       plotBackgroundImage: null,
      //       plotBorderWidth: 0,
      //       plotShadow: false,
      //       height: '80%',
      //       identifier:'clock gauge'
      //   },
  
      //   credits: {
      //       enabled: false
      //   },
  
      //   title: {
      //       text: 'The Highcharts clock'
      //   },
  
      //   pane: {
      //     background: [{
      //       // default background
      //     }, {
      //       // reflex for supported browsers
      //       backgroundColor: {
      //         radialGradient: {
      //           cx: 0.5,
      //           cy: -0.4,
      //           r: 1.9
      //         },
      //         stops: [
      //           [0.5, 'rgba(255, 255, 255, 0.2)'],
      //           [0.5, 'rgba(200, 200, 200, 0.2)']
      //         ]
      //       }
      //     }]
      //   },
  
      //   yAxis: {
      //       labels: {
      //           distance: -23,
      //           style: {
      //               fontSize: '18px'
      //           }
      //       },
      //       min: 0,
      //       max: 12,
      //       lineWidth: 0,
      //       showFirstLabel: false,
  
      //       minorTickInterval: 'auto',
      //       minorTickWidth: 3,
      //       minorTickLength: 5,
      //       minorTickPosition: 'inside',
      //       minorGridLineWidth: 0,
      //       minorTickColor: '#666',
  
      //       tickInterval: 1,
      //       tickWidth: 4,
      //       tickPosition: 'inside',
      //       tickLength: 10,
      //       tickColor: '#666',
      //       title: {
      //           // text: 'Powered by<br/>Highcharts',
      //           style: {
      //               color: '#BBB',
      //               fontWeight: 'normal',
      //               fontSize: '10px',
      //               lineHeight: '10px'
      //           },
      //           y: 10
      //       }
      //   },
  
      //   tooltip: {
      //       format: '{series.chart.tooltipText}'
      //   },
  
      //   series: [{
      //       data: [{
      //           id: 'hour',
      //           y: now.hours,
      //           dial: {
      //               radius: '60%',
      //               baseWidth: 4,
      //               baseLength: '95%',
      //               rearLength: 0
      //           }
      //       }, {
      //           id: 'minute',
      //           y: now.minutes,
      //           dial: {
      //               baseLength: '95%',
      //               rearLength: 0
      //           }
      //       }, {
      //           id: 'second',
      //           y: now.seconds,
      //           dial: {
      //               radius: '100%',
      //               baseWidth: 1,
      //               rearLength: '20%'
      //           }
      //       }],
      //       animation: false,
      //       dataLabels: {
      //           enabled: false
      //       }
      //   }]
      //   });
  
      // } 
      
      
      
      
      else {

      }
      
      this.chartType = null;
      this.names = [];
      this.ids = [];
    }

  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.chartObject, event.previousIndex, event.currentIndex);

  }


}
