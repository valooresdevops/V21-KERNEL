import { AfterViewInit, Component, OnInit, ViewEncapsulation , ChangeDetectorRef, HostListener    } from '@angular/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { GlobalConstants } from '../../common/GlobalConstants';
import { HttpClient } from '@angular/common/http';
import { CommonFunctions } from '../../../Kernel/common/CommonFunctions';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { Subscription } from 'rxjs';
import { EventEmitterService } from '../../../Kernel/services/event-emitter.service';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ChartBuilderFormComponent } from '../../../Kernel/kernelapp/in-display/object-builder/chart-builder-form/chart-builder-form.component';
import { GridBuilderPreviewComponent } from '../../../Kernel/kernelapp/in-display/object-builder/grid-builder-preview/grid-builder-preview.component';
import { KpiBuilderPreviewComponent } from '../../../Kernel/kernelapp/in-display/object-builder/kpi-builder-preview/kpi-builder-preview.component';
import { ChartFromKpiBuilderComponent } from '../../../Kernel/kernelapp/in-display/object-builder/chart-from-kpi-builder/chart-from-kpi-builder.component';
import { InformationService } from 'src/app/Kernel/services/information.service';
import Highcharts from 'highcharts';
import Highcharts3D from 'highcharts/highcharts-3d';
Highcharts3D(Highcharts);

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DashboardComponent implements OnInit, AfterViewInit  {
  Highcharts: typeof Highcharts = Highcharts;
  checkVisibiltyDashboard : boolean = false;
  public subsVar: Subscription;
  public showDashboard: boolean;
  constructor(private http: HttpClient,
    private eventEmitterService: EventEmitterService,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    public informationservice: InformationService,
    private cdr: ChangeDetectorRef,
  ) {document.addEventListener('click', this.onGlobalClick.bind(this));
    document.addEventListener('contextmenu', this.onGlobalRightClick.bind(this));

   }
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any[] = [];
  public agColumnsJson1: any[] = [];
  Labels: any[];
  Datasets: any[];
  Type: any[];
  chartObject: any[] = [];
  types: any[] = [];
  public tabs: any[];
  chartType: any;
  stockAd: number;
  stocktype: any;
  kpiHeader: any[] = [];
  kpiRecords: any[] = [];
  gridHeader: any[] = [];
  gridRecords: any[] = [];
  ids: string[] = [];
  names: number[] = [];
  chartValue: any[] = [];
  kpiValue: any[] = [];
  allData: any[] = [];
  gridValue: any[] = [];
  barRecords: any[] = [];
  radarRecords: any[] = [];
  public chartData :any;
  public gridData: any;
  public isChart: number = 0;
  public isStockChart: number = 0;
  tabName : string = "";
  transformedData: { NAME: string, Y: number }[] = [];

  newChartObject:any;
  stockObject: any;
public data : any;
allDataa : any[] = [];
public gridIndexes :number[]= [];
gridIndexesMapping :Map<number, number> = new Map();
 gridCounter : number = 0;



  // ngOnInit(): void {
  //   this.http.post<any>(GlobalConstants.getDashboardTemplateTab + this.informationservice.getLogeduserId(), { headers: GlobalConstants.headers }).subscribe(
  //     (res: any) => {
  //       this.tabs = res;
  //     })

  //   this.subsVar = this.eventEmitterService.onTabActionClick.subscribe(() => {
  //     this.showDashboard = false;
  //     this.onSelectTab();
  //     setTimeout(() => {
  //       this.showDashboard = true;
  //     }, 1000);
  //   });
  // }

  valueFromSecondObject: string;

  ngOnInit(): void {
    this.http.post<any>(GlobalConstants.getDashboardTemplateTab + this.informationservice.getLogeduserId(), { headers: GlobalConstants.headers }).subscribe(
      (res: any) => {
        this.tabs = res;
        console.log("this.info = ", JSON.stringify(this.tabs));

        
      })

    this.subsVar = this.eventEmitterService.onTabActionClick.subscribe(() => {
      this.showDashboard = false;
      this.onSelectTab();
      setTimeout(() => {
        this.showDashboard = true;
      }, 1000);
    });
  }

  
  currentIndexGrid : number = 0;
  conditionalIncrement() : number{
    // console.log("----Miky:::", this.currentIndexGrid);
    this.currentIndexGrid++;
  return this.currentIndexGrid-1;
  }
 
  

//  getGridIndex(index : number):number | undefined{
//   let value = this.gridIndexesMapping.get(index);
//     return value;
//  }

  // conditionalIncrement() :number
  // {
  //   // this.cdr.detach();
  //  this.currentIndexGrid++;
  //  return this.currentIndexGrid - 1;
  //   // this.cdr.detectChanges();
  //   // console.log("hhhhhhhhhhh_____",this.gridCounter);
  //   //   this.gridCounter++;
  //   //   this.cdr.detach();
  //   //   return this.gridCounter;
    
  //   // else if(!this.gridIndexes.includes(index) ){
  //   //   return index -1 ;
  //   // }
  //   // else{console.log("else---------->"); return index;}
  //   // this.currentIndexGrid++;
  //   // // if(this.currentIndexGrid== this.gridCounter) this.cdr.detach();
  //   // return this.currentIndexGrid-1;
  // }

  // triggerChangeDetection(){
  //   alert('hey')
  //   this.cdr.detectChanges();
  // }
  setupGridIndexes(): void {
    this.gridValue.forEach((data, index) => {
      this.gridIndexesMapping.set(data.ID, index);
      this.gridCounter++;
    });
    console.log("maps", this.gridIndexesMapping);
  }
   gridIndexMapping(id: number): number | undefined {
    return this.gridIndexesMapping.get(id);
  }
  onSelectTab() {
    this.checkVisibiltyDashboard = false;
    this.gridCounter=0;
    this.gridIndexesMapping.clear();
    this.ids = [];
    this.names = [];
    this.newChartObject = [];
    this.allData = this.newChartObject;
    this.data=this.http.post<any>(GlobalConstants.displayDashboard + this.informationservice.getSelectedTabId(), { headers: GlobalConstants.headers });
    
    this.data.subscribe(
      (res: any) => {
        // console.log("res---->",res)
        this.allData = res;
        // this.allDataa = this.allData;
        this.tabName = this.informationservice.getSelectedTabName();
        console.log("allData::::: ", this.allData );
        this.gridValue = [];
        for (let i = 0; i < this.allData.length; i++)
        {
          if (this.allData[i].type == 'Chart')
          {
            this.chartValue.push(this.allData[i]);
          }
          // console.log("7imarrrr ", this.chartValue.length); 
          
          if (this.allData[i].type == 'Grid') {
            this.gridValue.push(this.allData[i]);
            this.gridIndexes.push(i);
            // console.log("this.allData[i] ===== ", this.allData[i]);
          }
        }
        this.setupGridIndexes();
        console.log("grid valueeeee:",this.gridValue);
        this.gridCounter = this.gridIndexes.length;
    
        this.gridHeader = [];
        for (let i = 0; i < this.gridValue.length; i++)
        {
          this.gridRecords.push(this.gridValue[i].Records);
          this.gridHeader.push(this.gridValue[i].Header);

          this.agColumnsJson[i] = this.gridValue[i].Header;
          this.agColumns=[];
          this.agColumns.push(this.agColumnsJson[i]);
          this.agColumnsJson1.push(this.agColumns);
        }

        for (let i = 0; i < this.allData.length; i++)
        {
          if (this.allData[i].type == 'Chart') {
            if (this.allData[i].data.chartType == 1) {
              this.allData[i].stockAd = 0 
              this.chartType = 'heatmap';
            } else if (this.allData[i].data.chartType == 2) {
              this.allData[i].stockAd = 0 
 
              this.chartType = 'pie';
            } else if (this.allData[i].data.chartType == 3) {
              this.allData[i].stockAd = 0 

              this.chartType = 'bar';
            } else if (this.allData[i].data.chartType == 4) {
              this.allData[i].stockAd = 0 
   
              this.chartType = 'line';
            } else if (this.allData[i].data.chartType == 5) {
              this.allData[i].stockAd = 0 
      
              this.chartType = 'area';
            } else if (this.allData[i].data.chartType == 6) {
              this.allData[i].stockAd = 0 
      
              this.chartType = 'scatter';
            } else if (this.allData[i].data.chartType == 7) {
              this.allData[i].stockAd = 0 
         
              this.chartType = 'column';
            }else if (this.allData[i].data.chartType == 8) {
              this.allData[i].stockAd = 0 
        
              this.chartType = 'semiPie';
            }else  if (this.allData[i].data.chartType == 9) {
              this.allData[i].stockAd = 1
           
              this.chartType = 'candlestick';
            } else if (this.allData[i].data.chartType == 10) {
              this.allData[i].stockAd = 1 
              this.chartType = 'stockLine';
            } else if (this.allData[i].data.chartType == 11) {
              this.allData[i].stockAd = 1 
      
              this.chartType = 'stockColumn';
            } else if (this.allData[i].data.chartType == 12) {
              this.allData[i].stockAd = 1 
       
              this.chartType = 'ohlc';
            } else if (this.allData[i].data.chartType == 13) {
              this.allData[i].stockAd = 1 
       
              this.chartType = 'stockArea';
            }
          
            if (this.chartType == 'bar'){

                  for (let j = 0; j < this.allData[i].data.records.length; j++)
                    {
                      this.ids.push(this.allData[i].data.records[j].ID);
                      this.names.push(Number(this.allData[i].data.records[j].NAME));
                    }
  
                    if(this.allData[i].is3d == 1){
                      this.newChartObject = [
                        {
                          chart: {type: this.chartType,
                            options3d: {
                              enabled: true,
                              alpha: 10,
                              beta: 25,
                              depth: 70,
                              viewDistance: 25,
                            }
                          },
                          title: {text:this.allData[i].data.records[0].TITLE},
                          xAxis: {
                            categories: this.ids
                          },
                          yAxis: {
                            title: {
                              text: 'Value'
                            }
                          },
                          plotOptions: {
                            series: {
                              pointPadding: 1, // Adjust the spacing between bars
                              groupPadding: 0.8, // Adjust the spacing between groups
                              borderWidth: 0,
                            }
                          },
                          tooltip: {
                            headerFormat: '',
                            pointFormat: '{point.y}' // Changed from {point.name} to {point.x}
                            // pointFormat: '<b>{point.x}</b>: {point.y}' // Changed from {point.name} to {point.x}
                          },
                          series: [{
                            name: this.allData[i].data.records[0].TITLE,
                            data: this.names,
                          }]
                        }
                      ]
                    }else{
                      this.newChartObject = [
                        {
                          chart: {type: this.chartType},
                          title: {text: this.allData[i].data.records[0].TITLE},
                          xAxis: {
                            categories: this.ids
                          },
                          yAxis: {
                            title: {
                              text: 'Value'
                            }
                          },
                          plotOptions: {
                            series: {
                              pointPadding: 1, // Adjust the spacing between bars
                              groupPadding: 0.2, // Adjust the spacing between groups
                              borderWidth: 0,
                            }
                          },
                          tooltip: {
                            headerFormat: '',
                            pointFormat: '{point.y}' // Changed from {point.name} to {point.x}
                          },
                          series: [{
                            name: this.allData[i].data.records[0].TITLE,
                            data: this.names,
                          }]
                        }
                      ];
                    }
                 
              }else if (this.chartType == 'heatmap') {
                this.newChartObject = [{
                  chart: {
                      type: this.chartType,
                      marginTop: 60,
                      marginBottom: 80,
                      plotBorderWidth: 1
                  },
              
              
                  title: {
                      text: 'Sales per employee per weekday',
                      style: {
                          fontSize: '1em',
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
  
              }else if(this.chartType == 'line'){
  
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                  {
                    this.ids.push(this.allData[i].data.records[j].ID);
                    this.names.push(Number(this.allData[i].data.records[j].NAME));
                  }
  
                  if(this.allData[i].is3d == 1){
                    this.newChartObject = [
                      {
                        chart: {
                          type: 'line',
                          options3d: {
                            enabled: true,
                            alpha: 10,
                            beta: 30,
                            depth: 70,
                            viewDistance: 25
                          }
                        },
                        title: {
                          text: this.allData[i].data.records[0].TITLE
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
                  }else{
                this.newChartObject = [
                  {
                    chart: { type: 'line' },
                    title: { text: this.allData[i].data.records[0].TITLE},
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
              }else if (this.chartType == 'area') {
                for (let j = 0; j < this.allData[i].data.records.length; j++) {
                  this.ids.push(this.allData[i].data.records[j].ID);
                  this.names.push(Number(this.allData[i].data.records[j].NAME));
                }
              
                if (this.allData[i].is3d == 1) {
                  this.newChartObject = [
                    {
                      chart: {
                        type: 'area',
                        options3d: {
                          enabled: true,
                          alpha: 10,
                          beta: 25,
                          depth: 70,
                          viewDistance: 25
                        }
                      },
                      title: {
                        text: this.allData[i].data.records[0].TITLE
                      },
                      xAxis: {
                        categories: this.ids
                      },
                      yAxis: {
                        title: {
                          text: 'Value'
                        }
                      },
                      legend: {
                        enabled: true, // Enable the legend
                        layout: 'horizontal', // Layout options: 'horizontal' or 'vertical'
                        align: 'center', // Align the legend horizontally
                        verticalAlign: 'bottom', // Align the legend vertically
                        borderWidth: 0 // Add border to the legend (optional)
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
                      chart: {
                        type: 'area'
                      },
                      title: {
                        text: this.allData[i].data.records[0].TITLE
                      },
                      xAxis: {
                        categories: this.ids
                      },
                      yAxis: {
                        title: {
                          text: 'Value'
                        }
                      },
                      legend: {
                        enabled: true, // Enable the legend
                        layout: 'horizontal', // Layout options: 'horizontal' or 'vertical'
                        align: 'center', // Align the legend horizontally
                        verticalAlign: 'bottom', // Align the legend vertically
                        borderWidth: 0 // Add border to the legend (optional)
                      },
                      series: [{
                        name: 'Series 1',
                        data: this.names
                      }]
                    }
                  ];
                }
              }
              else if(this.chartType == 'semiPie')
                {
                  const transformedData = this.allData[i].data.records.map((item:any) => [item.ID, parseFloat(item.NAME)]);
                  if(this.allData[i].is3d == 1){
                    this.newChartObject  = [{
                      chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: 0,
                        plotShadow: false,
                        type: 'pie',
                        options3d: {
                          enabled: true,
                          alpha: 45,
                          beta: 0
                        }
                      },
                      title: {
                        text: this.allData[i].data.records[0].TITLE,
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
                            distance: -25,
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
                        data:transformedData
                      }]
                      }];
                  }else{
                    this.newChartObject = [{  chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: 0,
                    plotShadow: false,
                    type:'semiPie'
                },
                title: {
                    text: this.allData[i].data.records[0].TITLE,
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
              } else if(this.chartType == 'scatter')
                {
                  
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                  {
                    this.ids.push(this.allData[i].data.records[j].ID);
                    this.names.push(Number(this.allData[i].data.records[j].NAME));
                  }   
                  if(this.allData[i].is3d == 1){
                    this.newChartObject = [
                      {
                        chart: { type: 'scatter'},
                        title: { text: this.allData[i].data.records[0].TITLE },
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
                    ]


                  //   options3d: {
                  //     enabled: true,
                  //     alpha: 10,
                  //     beta: 30,
                  //     depth: 250,
                  //     viewDistance: 5,
                  //     fitToPlot: false,
                  //     frame: {
                  //         bottom: { size: 1, color: 'rgba(0,0,0,0.02)' },
                  //         back: { size: 1, color: 'rgba(0,0,0,0.04)' },
                  //         side: { size: 1, color: 'rgba(0,0,0,0.06)' }
                  //     }
                  // }
                  }else{           
                  this.newChartObject = [
                    {
                      chart: { type: 'scatter' },
                      title: { text: this.allData[i].data.records[0].TITLE },
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
                  ];}
              }else if(this.chartType == 'column'){
                  
             
                  for (let j = 0; j < this.allData[i].data.records.length; j++)
                    {
                      this.ids.push(this.allData[i].data.records[j].ID);
                      this.names.push(Number(this.allData[i].data.records[j].NAME));
                      // console.log(this.ids);
                    }
  // console.log( this.allData[i].data)
                    if(this.allData[i].is3d == 1){
  
  
                      // this.newChartObject  = [{
                      //   chart: { type: 'column',
                      //     options3d: {
                      //       enabled: true,
                      //       alpha: 15,
                      //       beta: 15,
                      //       depth: 50,
                      //       viewDistance: 25
                      //   }},
                      //   title: { text: this.allData[i].data.records[0].TITLE},
                      //   xAxis: [{ categories: this.ids}],
                      //   yAxis: [{
                      //     title: { text: 'Primary Axis' }
                      //   }, {
                      //     title: { text: 'Pareto' },
                      //     opposite: true
                      //   }],
                      //   tooltip: { shared: true },
                      //   series: [{
                      //     name: 'Primary',
                      //     type: 'column',
                      //     data: this.names
                      //   }, {
                      //     name: 'Pareto',
                      //     type: 'line',
                      //     yAxis: 1,
                      //     data:this.names,
                      //     zIndex: 10,
                      //     dashStyle: 'ShortDot'
                      //   }]
                      // }];

                      this.newChartObject  = [{
                        chart: {
                          renderTo: 'container',
                          type: 'column',
                          options3d: {
                              enabled: true,
                              alpha: 15,
                              beta: 15,
                              depth: 50,
                              viewDistance: 25
                          }
                      },
                      xAxis: {
                          categories:this.ids,
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
                          text: this.allData[i].data.records[0].TITLE,
                          align: 'left'
                      },
                      // subtitle: {
                      //     text: this.allData[i].data.records[0].TITLE,
                      //     align: 'left'
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
                      }]
                    }else{
  
                    
                  this.newChartObject  = [{
                    chart: { type: 'column' },
                    title: { text: this.allData[i].data.records[0].TITLE },
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
                  }];}
            
              }else if (this.chartType == 'pie') {
                let pieData: any[] = [];
                for (let j = 0; j < this.allData[i].data.records.length; j++) {
                  this.ids.push(this.allData[i].data.records[j].NAME);
                  this.names.push(Number(this.allData[i].data.records[j].Y));
                }
                let data1 = this.names.map((id, index) => {
                  return { name: this.ids[index], y: id };
                });
              
                if (this.allData[i].is3d == 1) {
                  const transformedData = this.allData[i].data.records.map((item: any) => [item.NAME, parseFloat(item.Y)]);
              
                  this.newChartObject = [{
                    chart: {
                      type: 'pie',
                      options3d: {
                        enabled: true,
                        alpha: 45
                      }
                    },
                    title: {
                      text: this.allData[i].data.records[0].TITLE,
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
                      name: this.allData[i].data.records[0].TITLE,
                      data: transformedData
                    }]
                  }];
                } else {
                  this.newChartObject = [{
                    chart: { type: 'pie' },
                    title: { text: this.allData[i].data.records[0].TITLE },
                    plotOptions: {
                      pie: {}
                    },
                    tooltip: {
                      headerFormat: '',
                      pointFormat: '<b>{point.name}</b>: {point.y}'
                    },
                    series: [{
                      name: this.allData[i].data.records[0].TITLE,
                      data: data1,
                    }]
                  }];
                }
              }
               else if (this.chartType == 'candlestick') {
                const transformedData1 =this.allData[i].data.records.map((item: any) => [ 
                  Number(item.timestamp),
                  Number(item.open_price),
                  Number(item.high_price),
                  Number(item.low_price),
                  Number(item.close_price)]);
                    
                this.newChartObject = [{
                  rangeSelector: {
                    selected: 1
                  },
          
                  title: {
                    text: this.allData[i].data.records[0].title
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
              } else if (this.chartType == 'ohlc') {
                const transformedData1 = this.allData[i].data.records.map((item: any) => [ 
                  Number(item.timestamp),
                  Number(item.open_price),
                  Number(item.high_price),
                  Number(item.low_price),
                  Number(item.close_price)]);
                this.newChartObject = [{
                  rangeSelector: {
                    selected: 2
                  },
          
                  title: {
                    text: this.allData[i].data.records[0].title
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
              } else if (this.chartType == 'stockColumn') {
                const transformedData1 = this.allData[i].data.records.map((item: any) => [ 
                  Number(item.timestamp),
                  Number(item.volume)]);
                  
                this.newChartObject = [{
                  chart: {
                    alignTicks: false
                  },
          
                  rangeSelector: {
                    selected: 1
                  },
          
                  title: {
                    text: this.allData[i].data.records[0].title
                  },
                  credits: {
                    enabled: false // Disable the credits link
                  },
          
                  series: [{
                    name: this.allData[i].data.records[0].title,
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
              } else if (this.chartType == 'stockLine') {
                const transformedData1 = this.allData[i].data.records.map((item: any) => [ 
                  Number(item.timestamp),
                  Number(item.close_price)]);
                this.newChartObject = [{
                  rangeSelector: {
                    selected: 1
                },
        
                title: {
                    text: this.allData[i].data.records[0].title
                },
        
                series: [{
                    name: this.allData[i].data.records[0].title,
                    type: 'line', 
                    data: transformedData1,
                    step: true,
                    tooltip: {
                        valueDecimals: 2
                    }
                }]
                }]
              } else if (this.chartType == 'stockArea') {
                const transformedData1 = this.allData[i].data.records.map((item: any) => [ 
                  Number(item.timestamp),
                  Number(item.close_price)]);

                this.newChartObject = [{
                  rangeSelector: {
                    selected: 1
                },
        
                title: {
                    text: this.allData[i].data.records[0].title
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
                }]}]
              } else{
              }
              this.ids = [];
              this.names = [];
              this.allData[i].data = this.newChartObject;
              this.newChartObject = [];
             }

  
    }});
 
    this.toggleAlertsVisibility();
  }

  ngAfterViewInit(): void {
    this.toggleAlertsVisibility();
  }
  toggleAlertsVisibility() : void{
    // Use setTimeout to delay setting checkVisibiltyDashboard to true
    setTimeout(() => {
      this.checkVisibiltyDashboard = true;
    }, 2000); // Adjust the delay as needed (100 milliseconds is just an example)
  }
  openChart(data:any){
    this.http.post<any>(GlobalConstants.selectChartRelatedToKpi + data.ID, { headers: GlobalConstants.headers }).subscribe(
      (res:any) => {
          this.chartData = res
  
  setTimeout(() => {
    
  const dialogConfig = new MatDialogConfig();
  dialogConfig.width = '700px';
  dialogConfig.height = '700px';
  
  const dialogRef = this.dialog.open(ChartFromKpiBuilderComponent, {
    data: this.chartData,
    width: '50%',
    height: '60%',
  });
}, 1000);
});

  }
  openGrid(data:any){

    this.http.post<any>(GlobalConstants.selectGridRelatedToKpi + data.ID, { headers: GlobalConstants.headers }).subscribe(
      (res:any) => {
          this.gridData ={data: res,
          flag:1}
            setTimeout(() => {
              const dialogConfig = new MatDialogConfig();
              dialogConfig.width = '1000px';
              dialogConfig.height = '1000px';
      
              const dialogRef = this.dialog.open(GridBuilderPreviewComponent, {
                data: this.gridData,
                width: '80%',
                height: '80%',
              });
            }, 1000);
      });
  }



  onExitButtonClick(data: any) {
    const selectedObject = data.type + '_' + data.ID
    this.http.post<any>(GlobalConstants.deleteSelectedObject + this.informationservice.getSelectedTabId() + '/' + selectedObject, { headers: GlobalConstants.headers }).subscribe(
      (res: any) => {
        this.commonFunctions.reloadPage('/dashboard');
      })
  }

  ngOnDestroy() {
    if (this.subsVar) {
      this.subsVar.unsubscribe()
    }
    document.removeEventListener('click', this.onGlobalClick.bind(this));
    document.removeEventListener('contextmenu', this.onGlobalRightClick.bind(this));
  }
  openSelected(data: any) {
    let info = {};
    if (data.type == 'Chart') {

      this.http.post<any>(GlobalConstants.getQueryData + data.ID, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = res
      


     

        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '700px';
        dialogConfig.height = '700px';

        const dialogRef = this.dialog.open(ChartBuilderFormComponent, {
          data: info,
          width: '50%',
          height: '60%',
        });
      });
    } else if (data.type == 'Grid') {
      this.http.post<any>(GlobalConstants.decodeGridQuery + data.ID, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = {
            data: res,
            id: data.ID,
            flag :0
          };
        });


      setTimeout(() => {

        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '700px';
        dialogConfig.height = '700px';

        const dialogRef = this.dialog.open(GridBuilderPreviewComponent, {
          data: info,
          width: '50%',
          height: '60%',
        });

      }, 1000);
    } else if (data.type == 'Kpi') {

      this.http.post<any>(GlobalConstants.decodeKpiQuery + data.ID, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = {
            data: res,
            kpiId: data.ID
          };
        });


      setTimeout(() => {

        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '700px';
        dialogConfig.height = '400px';

        const dialogRef = this.dialog.open(KpiBuilderPreviewComponent, {
          data: info,
          width: '50%',
          height: '30%',
        });


      }, 1000);

    }
  }

  drop(event: CdkDragDrop<any[]>) {
    moveItemInArray(this.allData, event.previousIndex, event.currentIndex);
  }


  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const contextMenu = document.getElementById('customContextMenu');
    if (contextMenu) {
      contextMenu.style.display = 'none';
    }
  }
  
  selectedChartIndex: number | null = null;

  onGlobalClick(event: MouseEvent) {
    if (!event.defaultPrevented) {
      this.selectedChartIndex = null;
    }
  }

  // Method to handle global right-clicks
  onGlobalRightClick(event: MouseEvent) {
    if (this.selectedChartIndex !== null) {
      event.preventDefault();
    }
  }

  onRightClick(event: MouseEvent, index: number) {
    console.log("1111")
    event.preventDefault();
    console.log("2222")
    event.stopPropagation();
    this.selectedChartIndex = index;

    const contextMenu = document.getElementById(`customContextMenu-${index}`);
    
    if (contextMenu) {
      contextMenu.style.display = 'none'; // Initially hide the context menu
      contextMenu.style.position = 'absolute'; // Set the position property

      // Find the closest parent that is a chart container
      let container = event.target as HTMLElement;
      while (container && !container.classList.contains('chart-container')) {
        container = container.parentElement as HTMLElement;
      }

      if (container) {
        // Function to calculate and set the position of the context menu
        const setPosition = () => {
          // Get the container dimensions and position
          const containerRect = container.getBoundingClientRect();
          
          // Get the context menu dimensions
          const menuWidth = contextMenu.offsetWidth;
          const menuHeight = contextMenu.offsetHeight;

          // Calculate the position of the menu relative to the container
          let left = event.clientX - containerRect.left;
          let top = event.clientY - containerRect.top;

          // Ensure the context menu stays within the container's bounds
          left = Math.max(0, Math.min(left, containerRect.width - menuWidth));
          top = Math.max(0, Math.min(top, containerRect.height - menuHeight));

          // Set the position of the context menu
          contextMenu.style.top = `${top}px`;
          contextMenu.style.left = `${left}px`;
        };

        // Show the context menu and immediately recalculate its position
        contextMenu.style.display = 'block';
        setPosition();

        // Listen for window resize events to update the position if necessary
        window.addEventListener('resize', setPosition);

        // Cleanup listener on component destroy or similar lifecycle hook
        return () => window.removeEventListener('resize', setPosition);
      }
    }
  }
  
  
  

  export(data: any) {
    console.log("data>>>>>>>>>>>>:::",data);
      this.http.post<any>(GlobalConstants.getQueryId + data.ID, { headers: GlobalConstants.headersCSV }).subscribe(
        (res: any) => {
          const fileType = 'text/csv';
          //the name of the saved file, starting with the tab name
          const fileName ="Query data "+data.ID.toString();
          
          //turning the string coming from the backend into a json string 
          const byteCharacters = JSON.stringify(res);
          //turning the json string into bytes 
          const byteNumbers = new Array(byteCharacters.length);
          //turning all characters to their respective ASCII numeric value
          for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
          }
          //converts the numeric values into a typed javascript array
          const byteArray = new Uint8Array(byteNumbers);
          //creates the file with the needed type
          const blob = new Blob([byteArray], { type: fileType });
          //creates a url for the file
          const url = URL.createObjectURL(blob);

          //creates a link <a> in html
          const link = document.createElement('a');
          //with a link
          link.href = url;
          //a name
          link.download = fileName;
          //and a click event to use the link
          link.click();

          //this deletes all the data from said link after the download was initiated
          URL.revokeObjectURL(url);
        },
        (error) => {
          console.error('Error fetching data', error);
        }
      );
    }
  }


  
