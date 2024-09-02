import { AfterViewInit, Component, OnInit, ViewEncapsulation , ChangeDetectorRef, HostListener, Inject, Optional    } from '@angular/core';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { GlobalConstants } from '../../common/GlobalConstants';
import { HttpClient } from '@angular/common/http';
import { CommonFunctions } from '../../../Kernel/common/CommonFunctions';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { from, lastValueFrom, Subscription } from 'rxjs';
import { EventEmitterService } from '../../../Kernel/services/event-emitter.service';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { ChartBuilderFormComponent } from '../../../Kernel/kernelapp/in-display/object-builder/chart-builder-form/chart-builder-form.component';
import { GridBuilderPreviewComponent } from '../../../Kernel/kernelapp/in-display/object-builder/grid-builder-preview/grid-builder-preview.component';
import { KpiBuilderPreviewComponent } from '../../../Kernel/kernelapp/in-display/object-builder/kpi-builder-preview/kpi-builder-preview.component';
import { ChartFromKpiBuilderComponent } from '../../../Kernel/kernelapp/in-display/object-builder/chart-from-kpi-builder/chart-from-kpi-builder.component';
import { InformationService } from 'src/app/Kernel/services/information.service';
import Highcharts from 'highcharts';
import Highcharts3D from 'highcharts/highcharts-3d';
Highcharts3D(Highcharts);

import HighchartsMore from 'highcharts/highcharts-more';
import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';
import { string } from 'sql-formatter/lib/src/lexer/regexFactory';
import axios from 'axios';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { EditorPreviewComponent } from '../in-display/object-builder/editor/editor-preview/editor-preview.component';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);

interface SanitizedRecord {
  ID: number;  // or the appropriate type for your IDs
  Records: SafeHtml;
}
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
  constructor(
    private http: HttpClient,
    private eventEmitterService: EventEmitterService,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    public informationservice: InformationService,
    private cdr: ChangeDetectorRef,
    private sanitizer: DomSanitizer,
    @Optional() private dialogRef?: MatDialogRef<EditorPreviewComponent>,
    @Optional() @Inject(MAT_DIALOG_DATA) public dataa?: any
  ) {
    document.addEventListener('click', this.onGlobalClick.bind(this));
    document.addEventListener('contextmenu', this.onGlobalRightClick.bind(this));
  }
  

   
  public chartTitle: String = '';
  public description: String='';
  public objectWidth: String='';

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
  editorRecords: any[]= [];
  ids: string[] = [];
  names: number[] = [];
  gaugeValue: any[] = [];
  gaugeLabel: any[] = [];
  gaugeTitle: any[] = [];
  chartValue: any[] = [];
  kpiValue: any[] = [];
  editorValue: any[] = [];
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
 editorIndexes: number[] = [];

  valueFromSecondObject: string;


  title: string;
  content: SafeHtml;
  sanitizedContent: SanitizedRecord[] = [];

  // chartOptions: Highcharts.Options = {};
  // chart: Highcharts.Chart | undefined;
  // ids1 = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul','Jan1', 'Feb1', 'Mar1', 'Apr1', 'May1', 'Jun1', 'Jul1','Jan2', 'Feb2', 'Mar2', 'Apr2', 'May2', 'Jun2', 'Jul2']; // Example categories
  // names1 = [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 45, 545, 235 , 4656 ,354 ,354 ,354, 35,4341]; // Example data
  // allData1 = [{ Title: 'Live Data Chart' }]; // Example title


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
        // console.log(111)
      }, 1000);
    });
  }

  
  currentIndexGrid : number = 0;
  conditionalIncrement() : number{
    this.currentIndexGrid++;
  return this.currentIndexGrid-1;
  }

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


  // startUpdatingData() {
  //   setInterval(() => {
  //     if (this.chart)
  //     {
  //       const x = (new Date()).getTime();
  //       const y = Math.random() * 100;
  //       this.chart.series[0].addPoint([x, y], true, true, true);
  //     }
  //   }, 1000);
  // }
  
  //////Sigma
  extractScripts(html: string): string[] {
    const parser = new DOMParser();
    const doc = parser.parseFromString(html, 'text/html');
    const scripts = doc.getElementsByTagName('script');
    
    const scriptContents: string[] = [];
    for (let i = 0; i < scripts.length; i++) {
      const script = scripts[i];
      const scriptContent = script.textContent || script.innerText;
      scriptContents.push(scriptContent);
    }
    
    return scriptContents;
  }
  

  executeScripts(scriptContents: string[]) {
    scriptContents.forEach((scriptContent) => {
      // console.log('Executing Script Content:', scriptContent);
  
      try {
        // Clean up the script content
        const cleanedScriptContent = scriptContent
          .replace(/\\"/g, '"')  // Replace escaped double quotes with actual double quotes
          .replace(/\\n/g, '\n'); // Handle escaped newline characters
  
        // Create a script element
        const script = document.createElement('script');
        script.textContent = cleanedScriptContent;
  
        // Append the script to the document
        document.body.appendChild(script);
  
        // Remove the script after execution
        window.setTimeout(() => {
          document.body.removeChild(script);
        }, 0);
      } catch (e) {
        console.error('Error executing script:', e);
      }
    });
  }
  
  
  editorIndexesMapping: Map<number, any> = new Map();

  setupEditorIndexesMapping(): void {
    this.sanitizedContent.forEach((data: SanitizedRecord) => {
      this.editorIndexesMapping.set(data.ID, data.Records);
    });
    // console.log("Editor Indexes Mapping", this.editorIndexesMapping);
  }
  
  

  editorIndexMapping(id: number): SafeHtml | undefined {
    return this.editorIndexesMapping.get(id);
  }
  

  

  onSelectTab() {
    this.editorValue = [];
    this.editorRecords = [];
    this.sanitizedContent = [];
    this.checkVisibiltyDashboard = false;
    this.gridCounter=0;
    this.gridIndexesMapping.clear();
    this.ids = [];
    this.names = [];
    this.newChartObject = [];
    this.allData = this.newChartObject;
    console.log("tabId>>>>>>>>",this.informationservice.getSelectedTabId());
    // this.data=this.http.post<any>(GlobalConstants.displayDashboard + this.informationservice.getSelectedTabId(), { headers: GlobalConstants.headers , responseType: 'text'});
   this.data = from(axios.post(GlobalConstants.displayDashboard + this.informationservice.getSelectedTabId(),{}));
    console.log("data>>>>>",this.data);
    this.tabName = this.informationservice.getSelectedTabName();
    this.data.subscribe(
      (res: any) => {
        console.log("entered the res")
 
        this.allData = res.data;
        console.log("allData::::: ", this.allData );
        this.gridValue = [];
        for (let i = 0; i < this.allData.length; i++)
        {
          if (this.allData[i].type == 'Chart')
          {
            this.chartValue.push(this.allData[i]);
            this.description=this.allData[i].description;
          }
          
          if(this.allData[i].type == 'Grid')
          {
            this.gridValue.push(this.allData[i]);
            this.gridIndexes.push(i);
          }

          if (this.allData[i].type == 'CkEditor')
            {
              this.editorValue.push(this.allData[i]);
              this.editorIndexes.push(i);
              // this.description=this.allData[i].description;
            }
            // console.log("editorValue>>>>>",this.editorValue);

          this.objectWidth = this.allData[i].objectWidth;
          console.log("objectWidth>>>>",this.objectWidth);
          // alert(this.objectWidth)
       //   this.calculateFlexBasis(this.objectWidth);
        }

        this.setupGridIndexes();
        console.log("grid valueeeee:",this.gridValue);
        this.gridCounter = this.gridIndexes.length;
    
        this.gridHeader = [];
        
        for (let i = 0; i < this.gridValue.length; i++)
        {
          this.gridRecords.push(this.gridValue[i].Records);

          // for(let x = 0; x < this.gridValue[i].Header.length; x ++)
          // {
          //   if(this.gridValue[i].Header[x].headerName != "TITLE")
          //   {
          //     alert(1)
          //   }
          //   else
          //   {
          //     alert(2)
          //   }
          // }
          
          this.gridHeader.push(this.gridValue[i].Header);

          this.agColumnsJson[i] = this.gridValue[i].Header;
          this.agColumns=[];
          this.agColumns.push(this.agColumnsJson[i]);
          this.agColumnsJson1.push(this.agColumns);
        }

        for (let i = 0; i < this.allData.length; i++)
        {
          if (this.allData[i].type == 'Chart')
          {
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
            } else  if (this.allData[i].data.chartType == 10) {
              this.allData[i].stockAd = 1
           
              this.chartType = 'stockLine';
            } 
            // else if (this.chartType == 11) {
            //   stockChartType = 'column';
            // } else if (this.chartType == 12) {
            //   stockChartType = 'ohlc';
            // } else if (this.chartType == 13) {
            //   stockChartType = 'area';
            // }
            
            else if (this.allData[i].data.chartType == 11) {
              this.allData[i].stockAd = 1 
              this.chartType = 'stockColumn';
            } else if (this.allData[i].data.chartType == 12) {
              this.allData[i].stockAd = 1 
      
              this.chartType = 'ohlc';
            } else if (this.allData[i].data.chartType == 13) {
              this.allData[i].stockAd = 1 
       
              this.chartType = 'stockArea';
            } else if (this.allData[i].data.chartType == 14) {
              this.allData[i].stockAd = 0 
       
              this.chartType = 'VU solid';
            } else if (this.allData[i].data.chartType == 15) {
              this.allData[i].stockAd = 0 
       
              this.chartType = 'VU meter';
            } else if (this.allData[i].data.chartType == 16) {
              this.allData[i].stockAd = 0 
       
              this.chartType = 'Speedometer';
            } else if (this.allData[i].data.chartType == 17) {
              this.allData[i].stockAd = 0 
       
              this.chartType = 'Dual Axes Speedometer';
            } else if (this.allData[i].data.chartType == 18) {
              this.allData[i].stockAd = 0 
       
              this.chartType = 'Speedometer solid';
            } 
            // else if (this.allData[i].data.chartType == 19) {
            //   this.allData[i].stockAd = 0 
       
            //   this.chartType = 'Multiple KPI gauge';
            // } 
            // else if (this.allData[i].data.chartType == 20) {
            //   this.allData[i].stockAd = 0 
       
            //   this.chartType = 'clock gauge';
            // }

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
                          title: {text:this.allData[i].Title, align: 'center', style: { fontSize: '16px' }},
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
                              pointPadding: 1,
                              groupPadding: 0.8,
                              borderWidth: 0,
                            }
                          },
                          tooltip: {
                            headerFormat: '',
                            pointFormat: '{point.y}'
                          },
                          series: [{
                            name: this.allData[i].Title,
                            data: this.names,
                          }],
                          credits: {
                              enabled: false
                          },
                        }
                      ]
                    }else{
                      this.newChartObject = [
                        {
                          chart: {type: this.chartType},
                          title: {text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }},
                          xAxis: {
                            categories: this.ids
                          },
                          yAxis: {
                            title: {
                              text: 'Value', align: 'center'
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
                            name: this.allData[i].Title,
                            data: this.names,
                          }]
                        }
                      ];
                    }
                 
              } else if (this.chartType == 'heatmap') {
                this.newChartObject = [{
                  chart: {
                      type: this.chartType,
                      marginTop: 60,
                      marginBottom: 80,
                      plotBorderWidth: 1
                  },
              
              
                  title: {
                      text: 'Sales per employee per weekday', align: 'center',
                      style: {
                          fontSize: '16px',
                      }
                  },
              
                  credits: {
                    enabled: false
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
  
              } else if(this.chartType == 'line'){
  
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
                          text: this.allData[i].Title, align: 'center',style: {
                            fontSize: '16px' // Change this to your desired font size
                          }
                        },
                        xAxis: {
                          categories: this.ids
                        },
                        yAxis: {
                          title: {
                            text: 'Value'
                          }
                        },
                        credits: {
                            enabled: false
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
                    title: { text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }},
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
                    

                    // chart: {
                    //   type: 'line',
                    //   events: {
                    //     load: () => {
                    //       this.chart = Highcharts.charts[0];
                    //       this.startUpdatingData();
                    //     }
                    //   }
                    // },
                    // title: {
                    //   text: this.allData1[0].Title,
                    //   align: 'center'
                    // },
                    // xAxis: {
                    //   categories: this.ids1
                    // },
                    // yAxis: {
                    //   title: {
                    //     text: 'Value'
                    //   }
                    // },
                    // series: [{
                    //   name: 'Series 1',
                    //   data: this.names1
                    // }]
                  }
                ];
              }
              } else if (this.chartType == 'area') {
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
                        text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
                      },
                      credits: {
                          enabled: false
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
                        text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
              } else if(this.chartType == 'semiPie')
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
                        text: this.allData[i].Title,
                        align: 'center',
                        verticalAlign: 'top',
                        y: 60,
                        style: {
                          fontSize: '16px'
                        }
                      },
                      credits: {
                          enabled: false
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
                    text: this.allData[i].Title,
                    align: 'center',
                    verticalAlign: 'top',
                    y: 60,
                    style: {
                        fontSize: '16px'
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
                        title: { text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }},
                        credits: {
                            enabled: false
                        },
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
                      title: { text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }},
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
                      credits: {
                          enabled: false
                      },
                      series: [{
                        name: 'Series 1',
                        data: this.names,
                      }]
                    }
                  ];}
              } else if(this.chartType == 'column'){
                  
             
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
                      //   title: { text: this.allData[i].Title},
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
                          text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
                      },
                      credits: {
                          enabled: false
                      },
                      // subtitle: {
                      //     text: this.allData[i].Title,
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
                    title: { text: this.allData[i].Title },
                    xAxis: [{ categories: this.ids}],
                    yAxis: [{
                      title: { text: 'Primary Axis' }
                    }, {
                      title: { text: 'Pareto', align: 'center', style: { fontSize: '16px' }},
                      opposite: true
                    }],
                    credits: {
                        enabled: false
                    },
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
            
              } else if (this.chartType == 'pie') {
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
                      text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
                    },
                    plotOptions: {
                      pie: {
                        innerSize: 100,
                        depth: 45
                      }
                    },
                    credits: {
                        enabled: false
                    },
                    tooltip: {
                      headerFormat: '',
                      pointFormat: '<b>{point.name}</b>: {point.y}'
                    },
                    series: [{
                      name: this.allData[i].Title,
                      data: transformedData
                    }]
                  }];
                } else {
                  this.newChartObject = [{
                    chart: { type: 'pie' },
                    title: { text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' } },
                    plotOptions: {
                      pie: {}
                    },
                    tooltip: {
                      headerFormat: '',
                      pointFormat: '<b>{point.name}</b>: {point.y}'
                    },
                    credits: {
                        enabled: false
                    },
                    series: [{
                      name: this.allData[i].Title,
                      data: data1,
                    }]
                  }];
                }
              } else if (this.chartType == 'candlestick') {
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
                    text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
                    text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
                    text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
                  },
                  credits: {
                    enabled: false // Disable the credits link
                  },
          
                  series: [{
                    name: this.allData[i].Title,
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
                    text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
                },
                credits: {
                    enabled: false
                },
        
                series: [{
                    name: this.allData[i].Title,
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
                    text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
                }]}]
              } else if (this.chartType == 'VU solid') {
                let pieData: any[] = [];
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                {
                  this.gaugeValue.push(this.allData[i].data.records[j].gaugevalue);
                  this.gaugeLabel.push(this.allData[i].data.records[j].gaugelabel);
                  this.gaugeTitle.push(this.allData[i].data.records[j].title);
                }
                let data1 = this.names.map((id, index) => {
                  return { name: this.ids[index], y: id };
                });              
                const gaugeValueData = this.allData[i].data.records[0].gaugevalue;
                const gaugelabelData = this.allData[i].data.records[0].gaugelabel;

                console.log("this.allData = " + JSON.stringify(this.allData[i]))
                  this.newChartObject = [{
                    chart: {
                      type: 'solidgauge',
                      height: 400, // Increased height to accommodate the larger gauge
                      spacingTop: 50, // Add spacing to avoid clipping at the top
                      spacingBottom: 50, // Add spacing to avoid clipping at the bottom
                      identifier:'VU solid'
                  },
              
                  title: {
                      text: this.allData[i].Title, align: 'center',
                      style: {
                          fontSize: '16px' // Adjusted font size to match the increased size
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
                          text: gaugeValueData + '<br/><span style="font-size:20px">' + gaugelabelData + '</span>', // Adjusted font size
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
                      name: gaugelabelData,
                      data: [gaugeValueData - 0],
                      dataLabels: {
                          format: '<div style="text-align:center">' +
                                  '<span style="font-size:50px">{y}</span><br/>' +
                                  '<span style="font-size:24px;opacity:0.4">Value</span>' +
                                  '</div>'
                      }
                    }]
                  }];
                
              } else if (this.chartType == 'VU meter') {
                let pieData: any[] = [];
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                  {
                    this.gaugeValue.push(this.allData[i].data.records[j].gaugevalue);
                    this.gaugeLabel.push(this.allData[i].data.records[j].gaugelabel);
                    this.gaugeTitle.push(this.allData[i].data.records[j].title);
                  }
                  let data1 = this.names.map((id, index) => {
                    return { name: this.ids[index], y: id };
                  });
                  console.log("this.allData = " + JSON.stringify(this.allData[i]))
                
                  const gaugeValueData = this.allData[i].data.records[0].gaugevalue;
                  const gaugelabelData = this.allData[i].data.records[0].gaugelabel;
                
                  this.newChartObject = [{
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
                    credits: {
                        enabled: false
                    },
                    title: {
                        text: this.allData[i].Title, align: 'center',
                        style: {
                            fontSize: '16px' // Font size adjusted to match the gauge
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
                          text: gaugeValueData + '<br/><span style="font-size:20px">' + gaugelabelData + '</span>', // Adjusted font size
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
                
              } else if (this.chartType == 'Speedometer') {
                let pieData: any[] = [];
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                  {
                    this.gaugeValue.push(this.allData[i].data.records[j].gaugevalue);
                    this.gaugeLabel.push(this.allData[i].data.records[j].gaugelabel);
                    this.gaugeTitle.push(this.allData[i].data.records[j].title);
                  }
                  let data1 = this.names.map((id, index) => {
                    return { name: this.ids[index], y: id };
                  });
                
                  const gaugeValueData = this.allData[i].data.records[0].gaugevalue;
                  const gaugelabelData = this.allData[i].data.records[0].gaugelabel;
                
                  this.newChartObject = [{
                    chart: {
                      renderTo: 'container', // Make sure this matches your container ID
                      type: 'gauge',
                      plotBackgroundColor: null,
                      plotBackgroundImage: null,
                      plotBorderWidth: null,
                      plotShadow: false,
                      height: '100%',
                      identifier: 'Speedometer'
                    },
                    title: {
                      text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
                          fontSize: '16px'
                        }
                      }
                    },
                    series: [{
                      // name: gaugelabelData,
                      data: [gaugeValueData - 0],
                      dataLabels: {
                        format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
                          '<div style="opacity:0.4; font-size:12px; text-align:center">' + gaugelabelData + '</div></div>'
                      },
                      dial: {
                        baseWidth: 10,
                        rearLength: 0
                      }
                    }]
                  }];
                
              } else if (this.chartType == 'Dual Axes Speedometer') {
                let pieData: any[] = [];
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                  {
                    this.gaugeValue.push(this.allData[i].data.records[j].gaugevalue);
                    this.gaugeLabel.push(this.allData[i].data.records[j].gaugelabel);
                    this.gaugeTitle.push(this.allData[i].data.records[j].title);
                  }
                  let data1 = this.names.map((id, index) => {
                    return { name: this.ids[index], y: id };
                  });
                
                  const gaugeValueData = this.allData[i].data.records[0].gaugevalue;
                  const gaugelabelData = this.allData[i].data.records[0].gaugelabel;

                  this.newChartObject = [{
                    chart: {
                      type: 'gauge',
                      alignTicks: false,
                      plotBackgroundColor: null,
                      plotBackgroundImage: null,
                      plotBorderWidth: 0,
                      plotShadow: false,
                      identifier: 'Dual Axes Speedometer'
                  },
            
                  title: {
                      text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
                      name: gaugelabelData,
                      data: [gaugeValueData - 0],
                      dataLabels: {
                        format: '<div style="text-align:center"><span style="font-size:25px">{y}</span><br/>' +
                          '<div style="opacity:0.4; font-size:12px; text-align:center">' + gaugelabelData + '</div></div>',
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
                
              } else if (this.chartType == 'Speedometer solid') {
                let pieData: any[] = [];
                for (let j = 0; j < this.allData[i].data.records.length; j++)
                  {
                    this.gaugeValue.push(this.allData[i].data.records[j].gaugevalue);
                    this.gaugeLabel.push(this.allData[i].data.records[j].gaugelabel);
                    this.gaugeTitle.push(this.allData[i].data.records[j].title);
                  }
                  let data1 = this.names.map((id, index) => {
                    return { name: this.ids[index], y: id };
                  });
                
                  const gaugeValueData = this.allData[i].data.records[0].gaugevalue;
                  const gaugelabelData = this.allData[i].data.records[0].gaugelabel;
                
                  this.newChartObject = [{
                    chart: {
                      type: 'gauge',
                      plotBackgroundColor: null,
                      plotBackgroundImage: null,
                      plotBorderWidth: 0,
                      plotShadow: false,
                      height: '80%',
                      identifier: 'Speedometer solid'
                  },
              
                  title: {
                      text: this.allData[i].Title, align: 'center', style: { fontSize: '16px' }
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
                      name: gaugelabelData,
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
              //  else if (this.chartType == 'Multiple KPI gauge') {
              //   let pieData: any[] = [];
              //   for (let j = 0; j < this.allData[i].data.records.length; j++)
              //     {
              //       this.gaugeValue.push(this.allData[i].data.records[j].gaugevalue);
              //       this.gaugeLabel.push(this.allData[i].data.records[j].gaugelabel);
              //       this.gaugeTitle.push(this.allData[i].data.records[j].title);
              //     }
              //     let data1 = this.names.map((id, index) => {
              //       return { name: this.ids[index], y: id };
              //     });
                
              //     const gaugeValueData = this.allData[i].data.records[0].gaugevalue;
              //     const gaugelabelData = this.allData[i].data.records[0].gaugelabel;
              //     const gaugeTitlelData = this.allData[i].Title;
              
                
              //     this.newChartObject = [{
              //       chart: {
              //         type: 'solidgauge',
              //         height: '85%',
              //         identifier: 'Multiple KPI gauge'
              //         // events: {
              //         //     render: renderIcons
              //         // }
              //     },
            
              //     title: {
              //         text: gaugeTitlelData,
              //         style: {
              //             fontSize: '24px'
              //         }
              //     },
              //     credits: {
              //         enabled: false
              //     },
              //     tooltip: {
              //         borderWidth: 0,
              //         backgroundColor: 'none',
              //         shadow: false,
              //         style: {
              //             fontSize: '16px'
              //         },
              //         valueSuffix: '%',
              //         pointFormat: '{series.name}<br>' +
              //             '<span style="font-size: 2em; color: {point.color}; ' +
              //             'font-weight: bold">{point.y}</span>',
              //         // positioner: function (labelWidth) {
              //         //     return {
              //         //         x: (this.chart.chartWidth - labelWidth) / 2,
              //         //         y: (this.chart.plotHeight / 2) + 15
              //         //     };
              //         // }
              //     },
            
              //     pane: {
              //         startAngle: 0,
              //         endAngle: 360,
              //         background: [{ // Track for Conversion
              //             outerRadius: '112%',
              //             innerRadius: '88%',
              //             backgroundColor: trackColors[0],
              //             borderWidth: 0
              //         }, { // Track for Engagement
              //             outerRadius: '87%',
              //             innerRadius: '63%',
              //             backgroundColor: trackColors[1],
              //             borderWidth: 0
              //         }, { // Track for Feedback
              //             outerRadius: '62%',
              //             innerRadius: '38%',
              //             backgroundColor: trackColors[2],
              //             borderWidth: 0
              //         }]
              //     },
            
              //     yAxis: {
              //         min: 0,
              //         max: 100,
              //         lineWidth: 0,
              //         tickPositions: []
              //     },
            
              //     plotOptions: {
              //         solidgauge: {
              //             dataLabels: {
              //                 enabled: false
              //             },
              //             linecap: 'round',
              //             stickyTracking: false,
              //             rounded: true
              //         }
              //     },
            
              //     series: [{
              //         name: gaugelabelData,
              //         data: [{
              //             color: Highcharts.getOptions().colors[0],
              //             radius: '112%',
              //             innerRadius: '88%',
              //             y: gaugeValueData,
              //         }],
              //         custom: {
              //             icon: 'filter',
              //             iconColor: '#303030'
              //         }
              //     }, {
              //         name: 'Engagement',
              //         data: [{
              //             color: Highcharts.getOptions().colors[1],
              //             radius: '87%',
              //             innerRadius: '63%',
              //             y: gaugeValueData - 20,
              //         }],
              //         custom: {
              //             icon: 'comments-o',
              //             iconColor: '#ffffff'
              //         }
              //     }, {
              //         name: 'Feedback',
              //         data: [{
              //             color: Highcharts.getOptions().colors[2],
              //             radius: '62%',
              //             innerRadius: '38%',
              //             y: gaugeValueData + 14.5,
              //         }],
              //         custom: {
              //             icon: 'commenting-o',
              //             iconColor: '#303030'
              //         }
              //     }]
              //     }];
                
              // } 
              // else if (this.chartType == 'clock gauge') {
              //   let pieData: any[] = [];
              //   for (let j = 0; j < this.allData[i].data.records.length; j++) {
              //     this.ids.push(this.allData[i].data.records[j].NAME);
              //     this.names.push(Number(this.allData[i].data.records[j].Y));
              //   }
              //   let data1 = this.names.map((id, index) => {
              //     return { name: this.ids[index], y: id };
              //   });
              
                
              //     this.newChartObject = [{
              //       chart: {
              //         type: 'gauge',
              //         plotBackgroundColor: null,
              //         plotBackgroundImage: null,
              //         plotBorderWidth: 0,
              //         plotShadow: false,
              //         height: '85%',
              //         identifier:'clock gauge'
              //     },
              //     credits: {
              //         enabled: false
              //     },
            
              //     title: {
              //         text: 'The Highcharts clock'
              //     },
            
              //     pane: {
              //       background: [{
              //         // default background
              //       }, {
              //         // reflex for supported browsers
              //         backgroundColor: {
              //           radialGradient: {
              //             cx: 0.5,
              //             cy: -0.4,
              //             r: 1.9
              //           },
              //           stops: [
              //             [0.5, 'rgba(255, 255, 255, 0.2)'],
              //             [0.5, 'rgba(200, 200, 200, 0.2)']
              //           ]
              //         }
              //       }]
              //     },
              //     yAxis: {
              //         labels: {
              //             distance: -23,
              //             style: {
              //                 fontSize: '18px'
              //             }
              //         },
              //         min: 0,
              //         max: 12,
              //         lineWidth: 0,
              //         showFirstLabel: false,
            
              //         minorTickInterval: 'auto',
              //         minorTickWidth: 3,
              //         minorTickLength: 5,
              //         minorTickPosition: 'inside',
              //         minorGridLineWidth: 0,
              //         minorTickColor: '#666',
            
              //         tickInterval: 1,
              //         tickWidth: 4,
              //         tickPosition: 'inside',
              //         tickLength: 10,
              //         tickColor: '#666',
              //         title: {
              //             // text: 'Powered by<br/>Highcharts',
              //             style: {
              //                 color: '#BBB',
              //                 fontWeight: 'normal',
              //                 fontSize: '10px',
              //                 lineHeight: '10px'
              //             },
              //             y: 10
              //         }
              //     },
            
              //     tooltip: {
              //         format: '{series.chart.tooltipText}'
              //     },
            
              //     series: [{
              //         data: [{
              //             id: 'hour',
              //             y: now.hours,
              //             dial: {
              //                 radius: '60%',
              //                 baseWidth: 4,
              //                 baseLength: '95%',
              //                 rearLength: 0
              //             }
              //         }, {
              //             id: 'minute',
              //             y: now.minutes,
              //             dial: {
              //                 baseLength: '95%',
              //                 rearLength: 0
              //             }
              //         }, {
              //             id: 'second',
              //             y: now.seconds,
              //             dial: {
              //                 radius: '100%',
              //                 baseWidth: 1,
              //                 rearLength: '20%'
              //             }
              //         }],
              //         animation: false,
              //         dataLabels: {
              //             enabled: false
              //         }
              //     }]
              //     }];
                
              // } 
              else{
              }
              this.ids = [];
              this.names = [];
              this.allData[i].data = this.newChartObject;
              this.newChartObject = [];
             }

  
    }

    //////Sigma
    for (let i = 0; i < this.editorValue.length; i++) {
      this.editorRecords.push(this.editorValue[i].Records);
    
      const record = this.editorRecords[i];
      // console.log("typeofffff>>>>>", typeof record);
    
      if (typeof record === 'object') {
        // Convert the object to a string if necessary
        const recordStr = JSON.stringify(record);
    
        // Remove the JSON object markers at the beginning and end
        const trimmedContent = recordStr.slice(13, -3);
    
        // Replace special character representations with their original forms
        const decodedContent = trimmedContent
        .replace(/&lt;/g, '<')
        .replace(/&gt;/g, '>')
        .replace(/&amp;/g, '&')
        .replace(/&nbsp;/g, ' ')
        .replace(/&quot;/g, '"')     // Replace &quot; with "
        .replace(/&apos;/g, "'")      // Replace &apos; with '
        .replace(/&copy;/g, '©')      // Replace &copy; with ©
        .replace(/&reg;/g, '®')       // Replace &reg; with ®
        .replace(/&euro;/g, '€')      // Replace &euro; with €
        .replace(/&pound;/g, '£')     // Replace &pound; with £
        .replace(/&yen;/g, '¥')       // Replace &yen; with ¥
    
        // Remove any automatically added <br> tags (if they are not intended)
        const cleanedContent = decodedContent.replace(/<br\s*\/?>/gi, '');
    
        // console.log("content>>>>>>>", cleanedContent);
    
        // Sanitize the content
        // this.sanitizedContent.push(this.sanitizer.bypassSecurityTrustHtml(cleanedContent));

// Sanitize and wrap the content with the ID
const sanitizedObj: SanitizedRecord = {
  ID: this.editorValue[i].ID, // Assuming `ID` is present in `editorValue`
  Records: this.sanitizer.bypassSecurityTrustHtml(decodedContent)
};

this.sanitizedContent.push(sanitizedObj);

        // Extract and execute scripts
        const scripts = this.extractScripts(cleanedContent);
        this.executeScripts(scripts);
      }
    }

    this.setupEditorIndexesMapping();
    // console.log("sanitized>>>>>>",this.sanitizedContent);
    // for (let i = 0; i < this.editorValue.length; i++) {
    // console.log("final Value>>>>>>>", this.editorIndexMapping(this.data.ID).value);
    // }
    
  },
    (error: any) => {
      console.error("Error occurred:", error);
      // Handle error here
    });
 
    this.toggleAlertsVisibility();
  }

  // calculateFlexBasis(objectWidth: string): string {
  //   const baseWidth = 33.33; // Base percentage for flex-basis
  //   return `calc(${baseWidth * objectWidth}% - 20px)`;
  // }

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
  async openSelected(data: any) {
    let info = {};
    if (data.type == 'Chart') {
      let titleInsideData:String = '';
      const dataArray = data.data; // This is an array
      if (dataArray && dataArray.length > 0) {
        const firstItem = dataArray[0]; // Accessing the first object in the array
        titleInsideData = firstItem.title.text; // Access the title's text
      }
      this.chartTitle = titleInsideData;
      this.http.post<any>(GlobalConstants.getQueryData + data.ID, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = res;
        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '700px';
        dialogConfig.height = '700px';

        const dialogRef = this.dialog.open(ChartBuilderFormComponent, {
          data: { info,
          chartTitle: this.chartTitle },
          width: '50%',
          height: '55%',
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
          height: '50%',
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
    else if(data.type == 'CkEditor'){
      let selectedData =this.data = from(axios.post(GlobalConstants.getHtmlElementData + data.ID,{}));
      const dataa = await lastValueFrom(selectedData);
      // console.log("ckeditor data>>>>>", dataa);
      // let selectedDataa = this.editorIndexMapping(data.ID);
      let editorContent = dataa.data[0].html_element; // Content from CKEditor
      let editorTitle = dataa.data[0].title; // Title from CKEditor
    // console.log("editorContent>>>>>",editorContent);
    // console.log("editorTitle>>>>>",editorTitle)
      // Open a dialog with the EditorPreviewComponent
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '50%';
      dialogConfig.height = '50%';
      dialogConfig.data = {
        content: editorContent,
        title: editorTitle
      };
      
      this.dialog.open(EditorPreviewComponent, dialogConfig);
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
    


    isDescriptionLong(description: string): boolean {
      return description.length > 100; // You can adjust this number based on your line length
    }
    
    toggleFullDescription(index: number): void {
      // Implement the logic to expand the description and hide the "Read More" button
      console.log("show more pressed");
    }
   
  }