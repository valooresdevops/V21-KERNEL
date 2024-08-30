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
import { identifier } from 'sql-formatter/lib/src/lexer/regexFactory';
Highcharts3D(Highcharts);

import HighchartsMore from 'highcharts/highcharts-more';
import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);


@Component({
  selector: 'app-chart-builder-form',
  templateUrl: './chart-builder-form.component.html',
  styleUrls: ['./chart-builder-form.component.css']
})
export class ChartBuilderFormComponent implements OnInit {
  Highcharts: typeof Highcharts = Highcharts;
  public getQueryName = GlobalConstants.getQueryNameApi;
  public getchartType = GlobalConstants.selectChartType;
  public chartType1:any;
  chartObject: any;
  newChartObject: any;
  stockObject: any;
  public agGridSelectedNodes: any = '';
  chartForm = new UntypedFormGroup({
    title: new UntypedFormControl(''),
    query: new UntypedFormControl(''),
    chartType: new UntypedFormControl(''),

  });
  barRecords: any[] = [];
  radarRecords: any[] = [];
  constructor(public commonFunctions: CommonFunctions,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public datepipe: DatePipe,) { }
  ids: String[] = [];
  names: number[] = [];
  gaugeType: string[] = [];
  gaugeValue: number[] = [];
  gaugeLabel: string[] = [];
  gaugeTitle: string[] = [];
  gaugeAppendText: string[] = [];

  chartType: number;
  is3d: number;
  public description: String='';
  public isDescription: boolean = false;
  public newTitle: String = '';
  public newData : any;

  ngOnInit(): void {
    const newString = this.data.chartTitle.replace(/"/g, '');
    this.newTitle = newString;
    this.newData = JSON.stringify(this.data.info);

    this.is3d = this.data.info.is3d;
    this.chartType = this.data.info.chartType;
    let ids = this.ids
    let stockChartType;
    this.description=this.data.info.description;
    if(this.description != 'null'){
      this.isDescription=true;
    }

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
      this.chartType1 = 'heatmap';
    } else if (this.chartType == 2) {
      this.chartType1 = 'pie';
    } else if (this.chartType == 3) {
      this.chartType1 = 'bar';
    } else if (this.chartType == 4) {
      this.chartType1 = 'line';
    } else if (this.chartType == 5) {
      this.chartType1 = 'area';
    } else if (this.chartType == 6) {
      this.chartType1 = 'scatter';
    } else if (this.chartType == 7) {
      this.chartType1 = 'column';
    } else if (this.chartType == 8) {
      this.chartType1 = 'semiPie';
    }else if (this.chartType == 9) {
      stockChartType = 'candlestick';
    } else if (this.chartType == 10) {
      stockChartType = 'line';
    } else if (this.chartType == 11) {
      stockChartType = 'column';
    } else if (this.chartType == 12) {
      stockChartType = 'ohlc';
    } else if (this.chartType == 13) {
      stockChartType = 'area';
    }else if (this.chartType == 14) {
      this.chartType1 = 'VU solid';
    }else if (this.chartType == 15) {
      this.chartType1 = 'VU meter';
    }else if (this.chartType == 16) {
      this.chartType1 = 'Speedometer';
    }else if (this.chartType == 17) {
      this.chartType1 = 'Dual Axes Speedometer';
    }else if (this.chartType == 18) {
      this.chartType1 = 'Speedometer solid';
    }
    // else if (this.chartType == 19) {
    //   this.chartType1 = 'Multiple KPI gauge';
    // }
    // else if (this.chartType == 20) {
    //   this.chartType1 = 'clock gauge';
    // }

    if (this.chartType1 == 'heatmap') {
      if (this.data.info.is3d == 1) { } else {
        this.newChartObject = [{
          chart: {
            type: 'heatmap',
            width: 550,
            height: '40%',
            identifier: 'heatmap',
            marginTop: 60,
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
            }
          }],

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

        }];
      }
      //done
    } else if (this.chartType1 == 'bar') {

      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(parseInt(this.data.info.records[i].NAME));
      }
      console.log("this.data.records = ", this.data.info.records);

      if (this.data.info.is3d == 1) {
        this.newChartObject = [
          {
            chart: {
              type: this.chartType1,
              width: 550,
              height: '50%',
              identifier: this.data.info.records[0].identifier,
              marginTop: 60,
              marginBottom: 80,
              plotBorderWidth: 1,
              options3d: {
                enabled: true,
                alpha: 10,
                beta: 25,
                depth: 70,
                viewDistance: 25
              }
            },
            title: { text: this.data.info.records[0].TITLE },
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
        ]
      } else {
        this.newChartObject = [
          {
            chart: { type: this.chartType1,
              width: 550, 
              identifier: this.data.info.records[0].identifier, },
            title: { text: this.data.info.records[0].TITLE },
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
        ];
      }
    } else if (this.chartType1 == 'pie') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].NAME);
        this.names.push(parseInt(this.data.info.records[i].Y));
      }


      let data1 = this.names.map((id, index) => {
        return { name: this.ids[index], y: id };
      });
      if (this.data.info.is3d == 1) {
        const transformedData = this.data.info.records.map((item: any) => [item.NAME, parseFloat(item.Y)]);

        this.newChartObject = [{
          chart: {
            type: 'pie',
            identifier: 'pie',
            width: 550,
            height: '40%',
            options3d: {
              enabled: true,
              alpha: 45
            }
          },
          title: {
            text: this.data.info.records[0].TITLE,
            align: 'left'
          },
          plotOptions: {
            pie: {
              innerSize: 100,
              depth: 45
            }
          },
          tooltip: {
            headerFormat: '',
            pointFormat: '<b>{point.name}</b>: {point.y}'
          },
          series: [{
            name: 'this.data.info[i].data.records[0].TITLE',
            data: transformedData
          }]
        }];
      } else {
        this.newChartObject = [
          {
            chart: { type: 'pie',
              width: 550,
              height: '50%',
              identifier: 'pie' },
            title: { text: this.data.info.records[0].TITLE },
            plotOptions: {
              pie: {}
            },
            tooltip: {
              headerFormat: '',
              pointFormat: '<b>{point.name}</b>: {point.y}'
            },
            series: [{
              name: 'this.data[i].data.records[0].TITLE',
              data: data1,
            }]
          }
        ];
      }
    } else if (this.chartType1 == 'line') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(Number(this.data.info.records[i].NAME));
      }
      if (this.data.info.is3d == 1) {
        this.newChartObject = [
          {
            chart: {
              type: 'line', identifier: 'line',
              height: '50%',
              width: 500,
              options3d: {
                enabled: true,
                alpha: 10,
                beta: 30,
                depth: 70,
                viewDistance: 25,
                width: 550
              }
            },
            title: {
              text: this.data.info.records[0].TITLE
            },
            xAxis: {
              categories: this.ids
            },
            yAxis: {
              title: {
                text: 'Value'
              }
            },
            plotOptions: {
              line: {
                depth: 25,
                marker: {
                  enabled: true,
                  symbol: 'circle',
                  radius: 3
                }
              }
            },
            series: [{
              name: 'Series 1',
              data: this.names
            }]
          }
        ];
      } else {
        this.newChartObject = [
          {
            chart: { type: 'line',
              width: 500,
              height: '55%', identifier: 'line' },
            title: { text: this.data.info.records[0].TITLE },
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
        ];
      }
    } else if (this.chartType1 == 'area') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(Number(this.data.info.records[i].NAME));
      }
      if (this.data.is3d == 1) {
        this.newChartObject = [
          {
            chart: {
              type: 'area',
              identifier: 'area',
              width: 550,
              height: '50%',
              options3d: {
                enabled: true,
                alpha: 10,
                beta: 25,
                depth: 70,
                viewDistance: 25
              }
            },
            title: { text: this.data.info.records[0].TITLE },
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
        ];
      } else {
        this.newChartObject = [
          {
            chart: { type: 'area',
              identifier: 'area',
              width: 550,
              height: '50%', },
            title: { text: this.data.info.records[0].TITLE },
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
        ];
      }
    } else if (this.chartType1 == 'scatter') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.names.push(Number(this.data.info.records[i].NAME));
      }
      if (this.data.info.is3d == 1) {
        this.newChartObject = [
          {
            chart: {
              type: 'scatter',
              identifier: 'scatter',
              width: 550,
              options3d: {
                enabled: true,
                alpha: 10,
                beta: 30,
                depth: 250,
                viewDistance: 5,
                width: 550,
                height: '50%',
                frame: {
                  bottom: { size: 1, color: 'rgba(0,0,0,0.02)' },
                  back: { size: 1, color: 'rgba(0,0,0,0.04)' },
                  side: { size: 1, color: 'rgba(0,0,0,0.06)' }
                }
              }
            },
            title: {
              text: this.data.info.records[0].TITLE
            },
            xAxis: {
              categories: this.ids,
              gridLineWidth: 1
            },
            yAxis: {
              title: {
                text: 'Value'
              }
            },
            zAxis: {
              min: 0,
              max: 10
            },
            plotOptions: {
              scatter: {
                width: 10,
                height: 10,
                depth: 10
              }
            },
            series: [{
              name: 'Series 1',
              data: this.names.map((name, index) => [index, this.names[index]]),  // Transform data for 3D
            }]
          }
        ];
      } else {
        this.newChartObject = [
          {
            chart: { type: 'scatter', width: 500,
              height: '40%', identifier: 'scatter' },
            title: { text: this.data.info.records[0].TITLE },
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
        ];
      }
    } else if (this.chartType1 == 'semiPie') {

      const transformedData = this.data.info.records.map((item: any) => [item.ID, parseFloat(item.NAME)]);
      if (this.data.info.is3d == 1) {
        this.newChartObject = [{
          chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 0,
            plotShadow: false,
            type: 'pie',
            identifier: 'pie',
            width: 550,
            height: '55%',
            options3d: {
              enabled: true,
              alpha: 45,
              beta: 0
            }
          },
          title: {
            text: this.data.info.records[0].TITLE,
            align: 'center',
            verticalAlign: 'top',
            y: 60,
            style: {
              fontSize: '1.1em'
            }
          },
          tooltip: {
           pointFormat: '{series.name} <b>{point.percentage:.1f}%</b>'
          },
          accessibility: {
            point: {
              valueSuffix: '%'
            }
          },
          plotOptions: {
            pie: {
              innerSize: '50%',
              depth: 45,
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
            name: '',
            data: transformedData
          }]
        }];
      } else {
        this.newChartObject = [{
          chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 0,
            plotShadow: false,
            type: 'semiPie',
            width: 550,
            height: '55%',
            identifier: 'semiPie'
          },
          title: {
            text: this.data.info.records[0].TITLE,
            align: 'center',
            verticalAlign: 'top',
            y: 60,
            style: {
              fontSize: '1.1em'
            }
          },
          tooltip: {
            pointFormat: '{series.name} <b>{point.percentage:.1f}%</b>'
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
            name: '',
            innerSize: '50%',
            data: transformedData
          }]
        }];
      }
    } else if (this.chartType1 == 'column') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(Number(this.data.info.records[i].NAME));
      }
      if (this.data.is3d == 1) {
        this.newChartObject = [{
          chart: {
            renderTo: 'container',
            type: 'column',
            identifier: 'column',
            width: 550,
            height: '50%',
            options3d: {
              enabled: true,
              alpha: 15,
              beta: 15,
              depth: 50,
              viewDistance: 25
            }
          },
          xAxis: {
            categories: this.ids,
          },
          yAxis: {
            title: {
              enabled: false
            }
          },
          tooltip: {
            headerFormat: '<b>{point.key}</b><br>',
            pointFormat: 'Cars sold: {point.y}'
          },
          title: {
            text: this.data.info.records[0].TITLE,
            align: 'left'
          },
          // subtitle: {
          //   text: this.data.records[0].TITLE,
          //   align: 'left'
          // },
          legend: {
            enabled: false
          },
          plotOptions: {
            column: {
              depth: 25
            }
          },
          series: [{
            data: this.names,
            colorByPoint: true
          }]
        }];
      } else {
        this.newChartObject = [{
          chart: { type: 'column', width: 550,
            height: '50%', identifier: 'column' },
          title: { text: this.data.info.records[0].TITLE },
          xAxis: [{ categories: this.ids }],
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
            data: this.names,
            zIndex: 10,
            dashStyle: 'ShortDot'
          }]
        }];
      }
    } else if (stockChartType == 'candlestick') {
      const transformedData1 = this.data.info.records.map((item: any) => [ 
        Number(item.timestamp),
        Number(item.open_price),
        Number(item.high_price),
        Number(item.low_price),
        Number(item.close_price)]);


      this.stockObject = [{

        chart: {
          width: 550,
          height: '50%',
        },

        rangeSelector: {
          selected: 1
        },

        title: {
          text: this.data.info.records[0].title
        },

        credits: {
          enabled: false
        },

        series: [{
          type: 'candlestick',
          name: 'AAPL Stock Price',
          data: transformedData1,
          dataGrouping: {
            units: [
              [
                'week', // unit name
                [1] // allowed multiples
              ], [
                'month',
                [1, 2, 3, 4, 6]
              ]
            ]
          }
        }]
      }]
    } else if (stockChartType == 'ohlc') {
      const transformedData1 = this.data.info.records.map((item: any) => [ 
        Number(item.timestamp),
        Number(item.open_price),
        Number(item.high_price),
        Number(item.low_price),
        Number(item.close_price)]);
      this.stockObject = [{
        chart: {
          width: 550,
          height: '50%',
        },
        rangeSelector: {
          selected: 2
        },

        title: {
          text: this.data.info.records[0].title
        },
        credits: {
          enabled: false // Disable the credits link
        },

        series: [{
          type: 'ohlc',
          name: 'AAPL Stock Price',
          data: transformedData1,
          dataGrouping: {
            units: [[
              'week', // unit name
              [1] // allowed multiples
            ], [
              'month',
              [1, 2, 3, 4, 6]
            ]]
          }
        }]
      }]
    } else if (stockChartType == 'column') {
      const transformedData1 = this.data.info.records.map((item: any) => [ 
        Number(item.timestamp),
        Number(item.volume)]);
      this.stockObject = [{
        chart: {
          alignTicks: false,
          width: 550,
          height: '50%',
        },

        rangeSelector: {
          selected: 1
        },

        title: {
          text: this.data.info.records[0].title
        },
        credits: {
          enabled: false // Disable the credits link
        },

        series: [{
          name: this.data.info.records[0].title,
          type: 'column',
          data: transformedData1,
          dataGrouping: {
            units: [[
              'week', // unit name
              [1] // allowed multiples
            ], [
              'month',
              [1, 2, 3, 4, 6]
            ]]
          }
        }]
      }]
    } else if (stockChartType == 'line') {
      const transformedData1 = this.data.info.records.map((item: any) => [ 
        Number(item.timestamp),
        Number(item.close_price)]);
      this.stockObject = [{

        chart: {
          width: 550,
          height: '50%',
        },

        rangeSelector: {
          selected: 1
      },

      title: {
          text: this.data.info.records[0].title
      },

        credits: {
          enabled: false
        },

      series: [{
          name: this.data.info.records[0].title,
          type: 'line', 
          data: transformedData1,
          step: true,
          tooltip: {
              valueDecimals: 2
          }
      }]
      }]
    } else if (stockChartType == 'area') {
      const transformedData1 = this.data.info.records.map((item: any) => [ 
        Number(item.timestamp),
        Number(item.close_price)]);
      this.stockObject = [{
        chart: {
          width: 550,
          height: '50%',
        },
          title: {
              text: 'AAPL Stock Price'
          },

          credits: {
            enabled: false
          },
  
          series: [{
              name: 'AAPL Stock Price',
              data: transformedData1,
              type: 'areaspline',
              threshold: null,
              tooltip: {
                  valueDecimals: 2
              },
              fillColor: {
                  linearGradient: {
                      x1: 0,
                      y1: 0,
                      x2: 0,
                      y2: 1
                  },
                  stops: [
                      [0, Highcharts.getOptions().colors[0]],
                      [
                          1,
                          Highcharts.color(
                              Highcharts.getOptions().colors[0]
                          ).setOpacity(0).get('rgba')
                      ]
                  ]
              }
        }]
      }]
    } else if (this.chartType1 == 'VU solid') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(parseInt(this.data.info.records[i].NAME));

        this.gaugeValue.push(this.data.info.records[i].gaugevalue);
        this.gaugeLabel.push(this.data.info.records[i].gaugelabel);
        this.gaugeTitle.push(this.data.info.records[i].title);
      }
      
      const gaugeValueData = this.data.info.records[0].gaugevalue;
      const gaugeLabelData = this.data.info.records[0].gaugelabel;
      const gaugeTitleData = this.data.info.records[0].title;
      
      this.newChartObject = [{
        chart: {
          type: 'solidgauge',
          height: 400, // Increased height to accommodate the larger gauge
          width: 550,
          spacingTop: 50, // Add spacing to avoid clipping at the top
          spacingBottom: 50, // Add spacing to avoid clipping at the bottom
          identifier:'VU solid'
      },
  
      title: {
          text: gaugeTitleData,
          style: {
              fontSize: '28px' // Adjusted font size to match the increased size
          }
      },
      credits: {
          enabled: false
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
          max: 150,
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
              text: gaugeValueData + '<br/><span style="font-size:20px">' + gaugeLabelData + '</span>', // Adjusted font size
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
          name: gaugeLabelData,
          data: [gaugeValueData - 0],
          dataLabels: {
              format: '<div style="text-align:center">' +
                      '<span style="font-size:50px">{y}</span><br/>' +
                      '<span style="font-size:24px;opacity:0.4">Value</span>' +
                      '</div>'
          }
        }]
      }];
      
    } else if (this.chartType1 == 'VU meter') {
      for (let i = 0; i < this.data.records.length; i++) {
        this.ids.push(this.data.records[i].ID);
        this.names.push(parseInt(this.data.info.records[i].NAME));

        this.gaugeValue.push(this.data.info.records[i].gaugevalue);
        this.gaugeLabel.push(this.data.info.records[i].gaugelabel);
        this.gaugeTitle.push(this.data.info.records[i].title);
      }
      
      const gaugeValueData = this.data.info.records[0].gaugevalue;
      const gaugeLabelData = this.data.info.records[0].gaugelabel;
      const gaugeTitleData = this.data.info.records[0].title;
      
      this.newChartObject = [{
        chart:
        {
          type: 'gauge',
          width: 550,
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
        credits: {
            enabled: false
        },
        title: {
            text: gaugeTitleData,
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
            min: 0,
            max: 220,
            minorTickPosition: 'outside',
            tickPosition: 'outside',
            labels: {
                rotation: 'auto',
                distance: 10 // Adjusted distance to fit the gauge
            },
            plotBands: [{
                from: 150,
                to: 220,
                color: '#C02316',
                innerRadius: '100%',
                outerRadius: '105%'
            }],
            title: {
              text: gaugeValueData + '<br/><span style="font-size:20px">' + gaugeLabelData + '</span>', // Adjusted font size
              y: -10 // Adjusted vertical position
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
            data: [gaugeValueData - 0]
        }]
      }];
      
    } else if (this.chartType1 == 'Speedometer') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(parseInt(this.data.info.records[i].NAME));

        this.gaugeValue.push(this.data.info.records[i].gaugevalue);
        this.gaugeLabel.push(this.data.info.records[i].gaugelabel);
        this.gaugeTitle.push(this.data.info.records[i].title);
      }
      
      const gaugeValueData = this.data.info.records[0].gaugevalue;
      const gaugeLabelData = this.data.info.records[0].gaugelabel;
      const gaugeTitleData = this.data.info.records[0].title;
      
      this.newChartObject = [{
        chart: {
          width: 400,
          renderTo: 'container', // Make sure this matches your container ID
          type: 'gauge',
          plotBackgroundColor: null,
          plotBackgroundImage: null,
          plotBorderWidth: null,
          plotShadow: false,
          height: '85%',
          identifier: 'Speedometer'
        },
        title: {
          text: gaugeTitleData
        },
        credits: {
            enabled: false
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
            // text: this.allData[i].data.records[0].gaugevalue,
            style: {
              fontSize: '17px'
            }
          }
        },
        series: [{
          // name: gaugelabelData,
          data: [gaugeValueData - 0],
          dataLabels: {
            format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
              '<div style="opacity:0.4; font-size:12px; text-align:center">' + gaugeLabelData + '</div></div>'
          },
          dial: {
            baseWidth: 10,
            rearLength: 0
          }
        }]
      }];
      
    } else if (this.chartType1 == 'Dual Axes Speedometer') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(parseInt(this.data.records[i].NAME));

        this.gaugeValue.push(this.data.info.records[i].gaugevalue);
        this.gaugeLabel.push(this.data.info.records[i].gaugelabel);
        this.gaugeTitle.push(this.data.info.records[i].title);
      }
      
      const gaugeValueData = this.data.info.records[0].gaugevalue;
      const gaugeLabelData = this.data.info.records[0].gaugelabel;
      const gaugeTitleData = this.data.info.records[0].title;
      
      this.newChartObject = [{
        chart: {
          width: 550,
          type: 'gauge',
          alignTicks: false,
          plotBackgroundColor: null,
          plotBackgroundImage: null,
          plotBorderWidth: 0,
          plotShadow: false,
          identifier: 'Dual Axes Speedometer'
      },

      title: {
          text: gaugeTitleData
      },
      credits: {
          enabled: false
      },
      pane: {
          startAngle: -150,
          endAngle: 150
      },

      yAxis: [{
          min: 0,
          max: 200,
          lineColor: '#339',
          tickColor: '#339',
          minorTickColor: '#339',
          offset: -25,
          lineWidth: 2,
          labels: {
              distance: -20,
              rotation: 'auto'
          },
          tickLength: 5,
          minorTickLength: 5,
          endOnTick: false
      }, {
          min: 0,
          max: 124,
          tickPosition: 'outside',
          lineColor: '#933',
          lineWidth: 2,
          minorTickPosition: 'outside',
          tickColor: '#933',
          minorTickColor: '#933',
          tickLength: 5,
          minorTickLength: 5,
          labels: {
              distance: 12,
              rotation: 'auto'
          },
          offset: -20,
          endOnTick: false,
      }],

      series: [{
          name: gaugeLabelData,
          data: [gaugeValueData - 0],
          dataLabels: {
            format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
              '<div style="opacity:0.4; font-size:12px; text-align:center">' + gaugeLabelData + '</div></div>',
              backgroundColor: {
                  linearGradient: {
                      x1: 0,
                      y1: 0,
                      x2: 0,
                      y2: 1
                  },
                  stops: [
                      [0, '#DDD'],
                      [1, '#FFF']
                  ]
              }
          },
          tooltip: {
              valueSuffix: ' km/h'
          }
      }],
      }];
      
    } else if (this.chartType1 == 'Speedometer solid') {
      for (let i = 0; i < this.data.info.records.length; i++) {
        this.ids.push(this.data.info.records[i].ID);
        this.names.push(parseInt(this.data.info.records[i].NAME));

        this.gaugeValue.push(this.data.info.records[i].gaugevalue);
        this.gaugeLabel.push(this.data.info.records[i].gaugelabel);
        this.gaugeTitle.push(this.data.info.records[i].title);
      }
      
      const gaugeValueData = this.data.info.records[0].gaugevalue;
      const gaugeLabelData = this.data.info.records[0].gaugelabel;
      const gaugeTitleData = this.data.info.records[0].title;

      this.newChartObject = [{
        chart: {
          width: 350,
          spacingLeft: 60,
          type: 'gauge',
          plotBackgroundColor: null,
          plotBackgroundImage: null,
          plotBorderWidth: 0,
          plotShadow: false,
          height: '70%',
          identifier: 'Speedometer solid'
      },
  
      title: {
          text: gaugeTitleData
      },
      credits: {
          enabled: false
      },
      pane: {
          startAngle: -90,
          endAngle: 89.9,
          background: null,
          center: ['50%', '75%'],
          size: '110%'
      },
  
      // the value axis
      yAxis: {
          min: 0,
          max: 200,
          tickPixelInterval: 72,
          tickPosition: 'inside',
          tickColor: Highcharts.defaultOptions.chart.backgroundColor || '#FFFFFF',
          tickLength: 20,
          tickWidth: 2,
          minorTickInterval: null,
          labels: {
              distance: 20,
              style: {
                  fontSize: '14px'
              }
          },
          lineWidth: 0,
          plotBands: [{
              from: 0,
              to: 130,
              color: '#55BF3B', // green
              thickness: 20,
              borderRadius: '50%'
          }, {
              from: 150,
              to: 200,
              color: '#DF5353', // red
              thickness: 20,
              borderRadius: '50%'
          }, {
              from: 120,
              to: 160,
              color: '#DDDF0D', // yellow
              thickness: 20
          }]
      },
  
      series: [{
          name: gaugeLabelData,
          data: [gaugeValueData - 0],
          tooltip: {
              valueSuffix: ' km/h'
          },
          dataLabels: {
              format: '{y} km/h',
              borderWidth: 0,
              color: (
                  Highcharts.defaultOptions.title &&
                  Highcharts.defaultOptions.title.style &&
                  Highcharts.defaultOptions.title.style.color
              ) || '#333333',
              style: {
                  fontSize: '16px'
              }
          },
          dial: {
              radius: '80%',
              backgroundColor: 'gray',
              baseWidth: 12,
              baseLength: '0%',
              rearLength: '0%'
          },
          pivot: {
              backgroundColor: 'gray',
              radius: 6
          }
  
      }]
      }];
      
    } 
    // else if (this.chartType1 == 'Multiple KPI gauge') {
    //   for (let i = 0; i < this.data.records.length; i++) {
    //     this.ids.push(this.data.records[i].ID);
    //     this.names.push(parseInt(this.data.records[i].NAME));

    //     this.gaugeValue.push(this.data.records[i].gaugevalue);
    //     this.gaugeLabel.push(this.data.records[i].gaugelabel);
    //     this.gaugeTitle.push(this.data.records[i].title);
    //   }
      
    //   const gaugeValueData = this.data.records[0].gaugevalue;
    //   const gaugeLabelData = this.data.records[0].gaugelabel;
    //   const gaugeTitleData = this.data.records[0].title;
      
    //   this.newChartObject = [{
    //     chart: {
    //       width: 550,
    //       type: 'solidgauge',
    //       height: '85%',
    //       identifier: 'Multiple KPI gauge'
    //       // events: {
    //       //     render: renderIcons
    //       // }
    //   },

    //   title: {
    //       text: gaugeTitleData,
    //       style: {
    //           fontSize: '24px'
    //       }
    //   },
    //   credits: {
    //       enabled: false
    //   },
    //   tooltip: {
    //       borderWidth: 0,
    //       backgroundColor: 'none',
    //       shadow: false,
    //       style: {
    //           fontSize: '16px'
    //       },
    //       valueSuffix: '%',
    //       pointFormat: '{series.name}<br>' +
    //           '<span style="font-size: 2em; color: {point.color}; ' +
    //           'font-weight: bold">{point.y}</span>',
    //       // positioner: function (labelWidth) {
    //       //     return {
    //       //         x: (this.chart.chartWidth - labelWidth) / 2,
    //       //         y: (this.chart.plotHeight / 2) + 15
    //       //     };
    //       // }
    //   },

    //   pane: {
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
    //   },

    //   yAxis: {
    //       min: 0,
    //       max: 100,
    //       lineWidth: 0,
    //       tickPositions: []
    //   },

    //   plotOptions: {
    //       solidgauge: {
    //           dataLabels: {
    //               enabled: false
    //           },
    //           linecap: 'round',
    //           stickyTracking: false,
    //           rounded: true
    //       }
    //   },

    //   series: [{
    //       name: gaugeLabelData,
    //       data: [{
    //           color: Highcharts.getOptions().colors[0],
    //           radius: '112%',
    //           innerRadius: '88%',
    //           y: gaugeValueData,
    //       }],
    //       custom: {
    //           icon: 'filter',
    //           iconColor: '#303030'
    //       }
    //   }, {
    //       name: 'Engagement',
    //       data: [{
    //           color: Highcharts.getOptions().colors[1],
    //           radius: '87%',
    //           innerRadius: '63%',
    //           y: gaugeValueData - 20,
    //       }],
    //       custom: {
    //           icon: 'comments-o',
    //           iconColor: '#ffffff'
    //       }
    //   }, {
    //       name: 'Feedback',
    //       data: [{
    //           color: Highcharts.getOptions().colors[2],
    //           radius: '62%',
    //           innerRadius: '38%',
    //           y: gaugeValueData + 14.5,
    //       }],
    //       custom: {
    //           icon: 'commenting-o',
    //           iconColor: '#303030'
    //       }
    //   }]
    //   }];
      
    // } 
    // else if (this.chartType1 == 'clock gauge') {
    //   for (let i = 0; i < this.data.records.length; i++) {
    //     this.ids.push(this.data.records[i].ID);
    //     this.names.push(parseInt(this.data.records[i].NAME));
    //   }
      
    //     this.newChartObject = [
    //       {
    //         chart: {
    //                   type: 'gauge',
    //                   plotBackgroundColor: null,
    //                   plotBackgroundImage: null,
    //                   plotBorderWidth: 0,
    //                   plotShadow: false,
    //                   height: '50%',
    //                   width: 550,
    //                   identifier:'clock gauge'
    //               },
            
    //               credits: {
    //                   enabled: false
    //               },
            
    //               title: {
    //                   text: 'Clock gauge'
    //               },
            
    //               pane: {
    //                 background: [{
    //                   // default background
    //                 }, {
    //                   // reflex for supported browsers
    //                   backgroundColor: {
    //                     radialGradient: {
    //                       cx: 0.5,
    //                       cy: -0.4,
    //                       r: 1.9
    //                     },
    //                     stops: [
    //                       [0.5, 'rgba(255, 255, 255, 0.2)'],
    //                       [0.5, 'rgba(200, 200, 200, 0.2)']
    //                     ]
    //                   }
    //                 }]
    //               },
            
    //               yAxis: {
    //                   labels: {
    //                       distance: -23,
    //                       style: {
    //                           fontSize: '18px'
    //                       }
    //                   },
    //                   min: 0,
    //                   max: 12,
    //                   lineWidth: 0,
    //                   showFirstLabel: false,
            
    //                   minorTickInterval: 'auto',
    //                   minorTickWidth: 3,
    //                   minorTickLength: 5,
    //                   minorTickPosition: 'inside',
    //                   minorGridLineWidth: 0,
    //                   minorTickColor: '#666',
            
    //                   tickInterval: 1,
    //                   tickWidth: 4,
    //                   tickPosition: 'inside',
    //                   tickLength: 10,
    //                   tickColor: '#666',
    //                   title: {
    //                       // text: 'Powered by<br/>Highcharts',
    //                       style: {
    //                           color: '#BBB',
    //                           fontWeight: 'normal',
    //                           fontSize: '10px',
    //                           lineHeight: '10px'
    //                       },
    //                       y: 10
    //                   }
    //               },
            
    //               tooltip: {
    //                   format: '{series.chart.tooltipText}'
    //               },
            
    //               series: [{
    //                   data: [{
    //                       id: 'hour',
    //                       y: now.hours,
    //                       dial: {
    //                           radius: '60%',
    //                           baseWidth: 4,
    //                           baseLength: '95%',
    //                           rearLength: 0
    //                       }
    //                   }, {
    //                       id: 'minute',
    //                       y: now.minutes,
    //                       dial: {
    //                           baseLength: '95%',
    //                           rearLength: 0
    //                       }
    //                   }, {
    //                       id: 'second',
    //                       y: now.seconds,
    //                       dial: {
    //                           radius: '100%',
    //                           baseWidth: 1,
    //                           rearLength: '20%'
    //                       }
    //                   }],
    //                   animation: false,
    //                   dataLabels: {
    //                       enabled: false
    //                   }
    //               }]
    //       }
    //     ];
      
    // }
    else {
      this.data.info = null;
      this.chartType = null;
      this.names = null;
      this.ids = null;
    }
  }

  drop(event: CdkDragDrop<string[]>)
  {
    moveItemInArray(this.newChartObject, event.previousIndex, event.currentIndex);
  }
}
