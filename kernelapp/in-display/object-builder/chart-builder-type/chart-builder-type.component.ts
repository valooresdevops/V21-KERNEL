import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Inject } from '@angular/core';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { DataService } from 'src/app/Kernel/services/data.service';
import { InformationService } from 'src/app/Kernel/services/information.service';
import Highcharts from 'highcharts';
import Highcharts3D from 'highcharts/highcharts-3d';
Highcharts3D(Highcharts);
import StockModule from 'highcharts/modules/stock'; // Import the stock module
import HighchartsMore from 'highcharts/highcharts-more';
import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);

StockModule(Highcharts); // Initialize the stock module

@Component({
  selector: 'app-chart-builder-type',
  templateUrl: './chart-builder-type.component.html',
  styleUrls: ['./chart-builder-type.component.css']
})
export class ChartBuilderTypeComponent implements OnInit {
  Highcharts: typeof Highcharts = Highcharts;

  public getQueryName = GlobalConstants.getQueryNameApi;
  public addChart = GlobalConstants.addChartApi;
  public getRowDate: string;
  public actionType: string = 'add';
  public legends: number;
  public chartData: any[] = [];
  public itemsData: any[] = [];
  public agGridSelectedNodes: any = '';
  public chartId: any;
  public identifier: string;
  public isUpdate: boolean = false;
  constructor(private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService) { }
    chartObject: any;
    newChartObject:any;
    stockObject:any;
    isAdvanced: any;
    isSource: any;
    chartType: String = "";
    chartForm: any = new UntypedFormGroup({
    title: new UntypedFormControl(''),
    query: new UntypedFormControl(''),
    Row: new UntypedFormControl(''),
    Column: new UntypedFormControl(''),
    describtion: new UntypedFormControl(''),
    is3d: new UntypedFormControl(''),
    isHorizental: new UntypedFormControl(''),
    isShowLegend: new UntypedFormControl(''),
    isSerie: new UntypedFormControl(''),
    isDrilldown: new UntypedFormControl(''),


  });


  ngOnInit(): void {

    this.actionType = this.dataservice.getactionType();

    if (this.actionType == 'update') {
      this.chartId = this.informationservice.getAgGidSelectedNode();
      this.isSource = 1;
      this.isAdvanced = 1;

      this.isUpdate = true;

      this.http.get<any>(GlobalConstants.getSelectChartApi + this.chartId, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          if (res.status == 'Fail') {
          } else {
            let toggleshowlegendValue;
            let toggleIs3dValue;
            let toggleIsHorizontalValue;
            let toggleisDrilldownValue;

            if (res[0].is3d == 1) {
              toggleIs3dValue = true;
            } else {
              toggleIs3dValue = false;
            }

            if (res[0].drilldown == 1) {
              toggleisDrilldownValue = true;
            } else {
              toggleisDrilldownValue = false;
            }
            if (res[0].isShowLegend == 1) {
              toggleshowlegendValue = true;
            } else {
              toggleshowlegendValue = false;
            }
            if (res[0].isHorizental == 1) {
              toggleIsHorizontalValue = true;
            } else {
              toggleIsHorizontalValue = false;
            }
            this.chartForm.get("title").setValue(res[0].chartName);
            this.chartForm.get("query").setValue(res[0].query);
            this.chartForm.get("Row").setValue(res[0].chartHTitle);
            this.chartForm.get("Column").setValue(res[0].chartVTitle);
            this.chartForm.get("is3d").setValue(toggleIs3dValue);
            this.chartForm.get("isHorizental").setValue(toggleIsHorizontalValue);
            this.chartForm.get("isShowLegend").setValue(toggleshowlegendValue);
            this.chartForm.get("isDrilldown").setValue(toggleisDrilldownValue);
            //describtion
            this.chartForm.get("describtion").setValue(res[0].chartDescription);

            this.chartData = JSON.parse(res[1].data);
            for (let i = 0; i < this.chartData.length; i++) {


              setTimeout(() => {
                this.itemsData[this.chartData[i].index] = { id: this.chartData[i].id, type: this.chartData[i].type, value: this.chartData[i].value, mode: this.chartData[i].mode, formData: this.chartData[i].formData };
              }, 1000);

            }
          }
        });

    }
    var sdata = [
      [1483228800000, 50.11],
      [1483315200000, 50.23],
      [1483401600000, 50.42],
      [1483660800000, 50.38],
      [1483747200000, 50.69],
      // Add more data points here...
  ];
  this.stockObject =[
    {

      rangeSelector: {
          selected: 1
      },

      title: {
          text: 'AAPL Stock Price'
      },

      series: [{
          name: 'AAPL Stock Price',
          type: 'line',
          identifier: 'line',
          data: [[1655818200000,133.42,137.06,133.32,135.87],[1655904600000,134.79,137.76,133.91,135.35],[1655991000000,136.82,138.59,135.63,138.27],[1656077400000,139.9,141.91,139.77,141.66],[1656336600000,142.7,143.49,140.97,141.66],[1656423000000,142.13,143.42,137.32,137.44],[1656509400000,137.46,140.67,136.67,139.23],[1656595800000,137.25,138.37,133.77,136.72],[1656682200000,136.04,139.04,135.66,138.93],[1657027800000,137.77,141.61,136.93,141.56],[1657114200000,141.35,144.12,141.08,142.92],[1657200600000,143.29,146.55,143.28,146.35],[1657287000000,145.26,147.55,145,147.04],[1657546200000,145.67,146.64,143.78,144.87],[1657632600000,145.76,148.45,145.05,145.86],[1657719000000,142.99,146.45,142.12,145.49],[1657805400000,144.08,148.95,143.25,148.47],[1657891800000,149.78,150.86,148.2,150.17],[1658151000000,150.74,151.57,146.7,147.07],[1658237400000,147.92,151.23,146.91,151],[1658323800000,151.12,153.72,150.37,153.04],[1658410200000,154.5,155.57,151.94,155.35],[1658496600000,155.39,156.28,153.41,154.09],[1658755800000,154.01,155.04,152.28,152.95],[1658842200000,152.26,153.09,150.8,151.6],[1658928600000,152.58,157.33,152.16,156.79],[1659015000000,156.98,157.64,154.41,157.35],[1659101400000,161.24,163.63,159.5,162.51],[1659360600000,161.01,163.59,160.89,161.51],[1659447000000,160.1,162.41,159.63,160.01],[1659533400000,160.84,166.59,160.75,166.13],[1659619800000,166.01,167.19,164.43,165.81],[1659706200000,163.21,165.85,163,165.35],[1659965400000,166.37,167.81,164.2,164.87]],
          step: true,
          tooltip: {
              valueDecimals: 2
          }
      }]
  },{
      rangeSelector: {
        selected: 1
    },

    title: {
        text: 'AAPL Stock Price'
    },

    series: [{
        type: 'candlestick',
        identifier: 'candlestick',
        name: 'AAPL Stock Price',
        data: [[1655818200000,133.42,137.06,133.32,135.87],[1655904600000,134.79,137.76,133.91,135.35],[1655991000000,136.82,138.59,135.63,138.27],[1656077400000,139.9,141.91,139.77,141.66],[1656336600000,142.7,143.49,140.97,141.66],[1656423000000,142.13,143.42,137.32,137.44],[1656509400000,137.46,140.67,136.67,139.23],[1656595800000,137.25,138.37,133.77,136.72],[1656682200000,136.04,139.04,135.66,138.93],[1657027800000,137.77,141.61,136.93,141.56],[1657114200000,141.35,144.12,141.08,142.92],[1657200600000,143.29,146.55,143.28,146.35],[1657287000000,145.26,147.55,145,147.04],[1657546200000,145.67,146.64,143.78,144.87],[1657632600000,145.76,148.45,145.05,145.86],[1657719000000,142.99,146.45,142.12,145.49],[1657805400000,144.08,148.95,143.25,148.47],[1657891800000,149.78,150.86,148.2,150.17],[1658151000000,150.74,151.57,146.7,147.07],[1658237400000,147.92,151.23,146.91,151],[1658323800000,151.12,153.72,150.37,153.04],[1658410200000,154.5,155.57,151.94,155.35],[1658496600000,155.39,156.28,153.41,154.09],[1658755800000,154.01,155.04,152.28,152.95],[1658842200000,152.26,153.09,150.8,151.6],[1658928600000,152.58,157.33,152.16,156.79],[1659015000000,156.98,157.64,154.41,157.35],[1659101400000,161.24,163.63,159.5,162.51],[1659360600000,161.01,163.59,160.89,161.51],[1659447000000,160.1,162.41,159.63,160.01],[1659533400000,160.84,166.59,160.75,166.13],[1659619800000,166.01,167.19,164.43,165.81],[1659706200000,163.21,165.85,163,165.35],[1659965400000,166.37,167.81,164.2,164.87]],
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
    },{
      chart: {
        alignTicks: false
    },

    rangeSelector: {
        selected: 1
    },

    title: {
        text: 'AAPL Stock Volume'
    },
    credits: {
      enabled: false // Disable the credits link
  },

    series: [{
        name: 'AAPL Stock Volume',
        type: 'column',
        identifier: 'column',
        data: [[1655818200000,81000500],[1655904600000,73409200],[1655991000000,72433800],[1656077400000,89116800],[1656336600000,70207900],[1656423000000,67083400],[1656509400000,66242400],[1656595800000,98964500],[1656682200000,71051600],[1657027800000,73353800],[1657114200000,74064300],[1657200600000,66253700],[1657287000000,64547800],[1657546200000,63141600],[1657632600000,77588800],[1657719000000,71185600],[1657805400000,78140700],[1657891800000,76259900],[1658151000000,81420900],[1658237400000,82982400],[1658323800000,64823400],[1658410200000,65086600],[1658496600000,66675400],[1658755800000,53623900],[1658842200000,55138700],[1658928600000,78620700],[1659015000000,81378700],[1659101400000,101786900],[1659360600000,67829400],[1659447000000,59907000],[1659533400000,82507500],[1659619800000,55474100],[1659706200000,56697000],[1659965400000,60276900],[1660051800000,63135500],[1660138200000,70170500],[1660224600000,57149200],[1660311000000,68039400],[1660570200000,54091700],[1660656600000,56377100],[1660743000000,79542000],[1660829400000,62290100],[1660915800000,70346300],[1661175000000,69026800],[1661261400000,54147100],[1661347800000,53841500],[1661434200000,51218200],[1661520600000,78961000]],
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
    },
    {
      rangeSelector: {
        selected: 2
    },

    title: {
        text: 'AAPL Stock Price'
    },
    credits: {
      enabled: false // Disable the credits link
  },

    series: [{
        type: 'ohlc',
        identifier: 'ohlc',
        name: 'AAPL Stock Price',
        data: [[1655818200000,133.42,137.06,133.32,135.87],[1655904600000,134.79,137.76,133.91,135.35],[1655991000000,136.82,138.59,135.63,138.27],[1656077400000,139.9,141.91,139.77,141.66],[1656336600000,142.7,143.49,140.97,141.66],[1656423000000,142.13,143.42,137.32,137.44],[1656509400000,137.46,140.67,136.67,139.23],[1656595800000,137.25,138.37,133.77,136.72],[1656682200000,136.04,139.04,135.66,138.93],[1657027800000,137.77,141.61,136.93,141.56],[1657114200000,141.35,144.12,141.08,142.92],[1657200600000,143.29,146.55,143.28,146.35],[1657287000000,145.26,147.55,145,147.04],[1657546200000,145.67,146.64,143.78,144.87],[1657632600000,145.76,148.45,145.05,145.86],[1657719000000,142.99,146.45,142.12,145.49],[1657805400000,144.08,148.95,143.25,148.47],[1657891800000,149.78,150.86,148.2,150.17],[1658151000000,150.74,151.57,146.7,147.07],[1658237400000,147.92,151.23,146.91,151],[1658323800000,151.12,153.72,150.37,153.04],[1658410200000,154.5,155.57,151.94,155.35],[1658496600000,155.39,156.28,153.41,154.09],[1658755800000,154.01,155.04,152.28,152.95],[1658842200000,152.26,153.09,150.8,151.6],[1658928600000,152.58,157.33,152.16,156.79],[1659015000000,156.98,157.64,154.41,157.35],[1659101400000,161.24,163.63,159.5,162.51],[1659360600000,161.01,163.59,160.89,161.51],[1659447000000,160.1,162.41,159.63,160.01],[1659533400000,160.84,166.59,160.75,166.13],[1659619800000,166.01,167.19,164.43,165.81],[1659706200000,163.21,165.85,163,165.35],[1659965400000,166.37,167.81,164.2,164.87],[1660051800000,164.02,165.82,163.25,164.92],[1660138200000,167.68,169.34,166.9,169.24],[1660224600000,170.06,170.99,168.19,168.49],[1660311000000,169.82,172.17,169.4,172.1],[1660570200000,171.52,173.39,171.35,173.19],[1660656600000,172.78,173.71,171.66,173.03],[1660743000000,172.77,176.15,172.57,174.55],[1660829400000,173.75,174.9,173.12,174.15],[1660915800000,173.03,173.74,171.31,171.52],[1661175000000,169.69,169.86,167.14,167.57],[1661261400000,167.08,168.71,166.65,167.23],[1661347800000,167.32,168.11,166.25,167.53],[1661434200000,168.78,170.14,168.35,170.03],[1661520600000,170.57,171.05,163.56,163.62],[1661779800000,161.15,162.9,159.82,161.38],[1661866200000,162.13,162.56,157.72,158.91],[1661952600000,160.31,160.58,157.14,157.22],[1662039000000,156.64,158.42,154.67,157.96],[1662125400000,159.75,160.36,154.97,155.81],[1662471000000,156.47,157.09,153.69,154.53],[1662557400000,154.82,156.67,153.61,155.96],[1662643800000,154.64,156.36,152.68,154.46],[1662730200000,155.47,157.82,154.75,157.37],[1662989400000,159.59,164.26,159.3,163.43],[1663075800000,159.9,160.54,153.37,153.84],[1663162200000,154.79,157.1,153.61,155.31],[1663248600000,154.65,155.24,151.38,152.37],[1663335000000,151.21,151.35,148.37,150.7],[1663594200000,149.31,154.56,149.1,154.48],[1663680600000,153.4,158.08,153.08,156.9],[1663767000000,157.34,158.74,153.6,153.72],[1663853400000,152.38,154.47,150.91,152.74],[1663939800000,151.19,151.47,148.56,150.43],[1664199000000,149.66,153.77,149.64,150.77],[1664285400000,152.74,154.72,149.95,151.76],[1664371800000,147.64,150.64,144.84,149.84],[1664458200000,146.1,146.72,140.68,142.48],[1664544600000,141.28,143.1,138,138.2],[1664803800000,138.21,143.07,137.69,142.45],[1664890200000,145.03,146.22,144.26,146.1],[1664976600000,144.07,147.38,143.01,146.4],[1665063000000,145.81,147.54,145.22,145.43],[1665149400000,142.54,143.1,139.45,140.09]],
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
    },

    {
      chart: {
        height: 400
    },
    credits: {
      enabled: false // Disable the credits link
  },

    title: {
        text: 'Highcharts Stock Responsive Chart'
    },

    subtitle: {
        text: 'Click small/large buttons or change window size to test ' +
            'responsiveness'
    },

    rangeSelector: {
        selected: 1
    },

    series: [{
        name: 'AAPL Stock Price',
        data: [[1655818200000,135.87],[1655904600000,135.35],[1655991000000,138.27],[1656077400000,141.66],[1656336600000,141.66],[1656423000000,137.44],[1656509400000,139.23],[1656595800000,136.72],[1656682200000,138.93],[1657027800000,141.56],[1657114200000,142.92],[1657200600000,146.35],[1657287000000,147.04],[1657546200000,144.87],[1657632600000,145.86],[1657719000000,145.49],[1657805400000,148.47],[1657891800000,150.17],[1658151000000,147.07],[1658237400000,151],[1658323800000,153.04],[1658410200000,155.35],[1658496600000,154.09],[1658755800000,152.95],[1658842200000,151.6],[1658928600000,156.79],[1659015000000,157.35],[1659101400000,162.51],[1659360600000,161.51],[1659447000000,160.01],[1659533400000,166.13],[1659619800000,165.81],[1659706200000,165.35],[1659965400000,164.87],[1660051800000,164.92],[1660138200000,169.24],[1660224600000,168.49],[1660311000000,172.1],[1660570200000,173.19],[1660656600000,173.03],[1660743000000,174.55],[1660829400000,174.15],[1660915800000,171.52],[1661175000000,167.57],[1661261400000,167.23],[1661347800000,167.53],[1661434200000,170.03],[1661520600000,163.62],[1661779800000,161.38],[1661866200000,158.91],[1661952600000,157.22],[1662039000000,157.96],[1662125400000,155.81],[1662471000000,154.53],[1662557400000,155.96],[1662643800000,154.46],[1662730200000,157.37],[1662989400000,163.43],[1663075800000,153.84],[1663162200000,155.31],[1663248600000,152.37],[1663335000000,150.7],[1663594200000,154.48],[1663680600000,156.9],[1663767000000,153.72],[1663853400000,152.74],[1663939800000,150.43],[1664199000000,150.77],[1664285400000,151.76],[1664371800000,149.84],[1664458200000,142.48],[1664544600000,138.2],[1664803800000,142.45],[1664890200000,146.1],[1664976600000,146.4],[1665063000000,145.43],[1665149400000,140.09],[1665408600000,140.42],[1665495000000,138.98],[1665581400000,138.34],[1665667800000,142.99],[1665754200000,138.38],[1666013400000,142.41],[1666099800000,143.75],[1666186200000,143.86],[1666272600000,143.39],[1666359000000,147.27],[1666618200000,149.45],[1666704600000,152.34],[1666791000000,149.35],[1666877400000,144.8],[1666963800000,155.74],[1667223000000,153.34],[1667309400000,150.65],[1667395800000,145.03],[1667482200000,138.88],[1667568600000,138.38],[1667831400000,138.92],[1667917800000,139.5],[1668004200000,134.87],[1668090600000,146.87],[1668177000000,149.7],[1668436200000,148.28],[1668522600000,150.04],[1668609000000,148.79],[1668695400000,150.72],[1668781800000,151.29],[1669041000000,148.01],[1669127400000,150.18],[1669213800000,151.07],[1669386600000,148.11],[1669645800000,144.22],[1669732200000,141.17],[1669818600000,148.03],[1669905000000,148.31],[1669991400000,147.81],[1670250600000,146.63],[1670337000000,142.91],[1670423400000,140.94],[1670509800000,142.65],[1670596200000,142.16]],
        type: 'area',
        identifier: 'area',
        threshold: null,
        tooltip: {
            valueDecimals: 2
        }
    }],

    responsive: {
        rules: [{
            condition: {
                maxWidth: 500
            },
            chartOptions: {
                chart: {
                    height: 300
                },
                subtitle: {
                    text: null
                },
                navigator: {
                    enabled: false
                }
            }
        }]
    }
    }
    
  ];

  // const gaugeConfig = ['gauge', 'arch', 80.3, "Speed", "%", ''];
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


    this.newChartObject =
    [
      {
        chart: {
            type: 'heatmap',
            marginTop: 40,
            marginBottom: 80,
            plotBorderWidth: 1,
            identifier: 'heatmap'
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
    
      },
      {
          chart: { type: 'pie',
            identifier: 'pie' },
          title: { text: 'Fruit Consumption' },
          series: [{
            name: 'Fruits',
            data: [
              { name: 'Apples', y: 10 },
              { name: 'Oranges', y: 20 },
              { name: 'Bananas', y: 30 },
              { name: 'Grapes', y: 40 }
            ],
          }]
      },
      {
          chart: {type: 'bar',
            identifier: 'bar'},
          title: {text: 'Employee Performance'},
          xAxis: {
            categories: ['Alexander', 'Marie', 'Maximilian', 'Sophia', 'Lukas']
          },
          yAxis: {
            title: {
              text: 'Sales'
            }
          },
          series: [{
            name: 'Sales',
            data: [107, 31, 635, 203, 215],
          }]
      },
      {
          chart: { type: 'line',
            identifier: 'line' },
          title: { text: 'Monthly Revenue' },
          xAxis: {
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
          },
          yAxis: {
            title: {
              text: 'Revenue (in USD)'
            }
          },
          series: [{
            name: '2024',
            data: [12000, 15000, 18000, 17000, 19000, 22000, 25000, 24000, 23000, 21000, 20000, 18000],
          }]
      },
      {
          chart: { type: 'area',
            identifier: 'area' },
          title: { text: 'Website Traffic' },
          xAxis: {
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
          },
          yAxis: {
            title: {
              text: 'Visitors'
            }
          },
          series: [{
            name: '2024',
            data: [3000, 4000, 3500, 5000, 4500, 6000, 7000, 7500, 8000, 6500, 6000, 5000],
          }]
      },
      {
          chart: { type: 'scatter',
            identifier: 'scatter' },
          title: { text: 'Height vs Weight' },
          xAxis: {
            title: {
              text: 'Height (cm)'
            }
          },
          yAxis: {
            title: {
              text: 'Weight (kg)'
            }
          },
          series: [{
            name: 'Individuals',
            data: [[167, 60], [170, 65], [175, 70], [180, 75], [185, 80], [190, 85], [195, 90], [200, 95]],
          }]
      },
      {
            chart: { type: 'column',
              identifier: 'column' },
            title: { text: 'Pareto Chart' },
            xAxis: [{ categories: ['Defect A', 'Defect B', 'Defect C', 'Defect D', 'Defect E']}],
            yAxis: [{
              title: { text: 'Frequency' }
            }, {
              title: { text: 'Cumulative Percentage' },
              opposite: true
            }],
            tooltip: { shared: true },
            series: [{
              name: 'Defects',
              type: 'column',
              data: [30, 20, 15, 10, 5]
            }, {
              name: 'Cumulative Percentage',
              type: 'line',
              yAxis: 1,
              data: [30, 50, 65, 75, 80],
              zIndex: 10,
              dashStyle: 'ShortDot'
            }]
      },
      {
          chart: {
              plotBackgroundColor: null,
              plotBorderWidth: 0,
              plotShadow: false,
              type:'semiPie',
            identifier: 'semiPie'
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
              data: [
                  ['Chrome', 63.86],
                  ['Edge', 17.97],
                  ['Firefox', 10.52],
                  ['Safari', 4.98],
                  ['Internet Explorer', 1.90],
                  ['Other', 0.77]
              ]
          }]
      },
      {
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
      },
      {
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
      },
      {
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
      },

      {
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
          text: 'Speedometer with Dual Axes'
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
          endOnTick: false
      }],

      series: [{
          name: 'Speed',
          data: [80],
          dataLabels: {
              format: '<span style="color:#339">{y} km/h</span><br/>' +
                  '<span style="color:#933">{(multiply y 0.621):.0f} mph</span>',
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
      }]
      },
      {
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
            text: 'Speedometer'
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
            name: 'Speed',
            data: [80],
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
      },

      // {
      //   chart: {
      //     type: 'solidgauge',
      //     height: '100%',
      //     identifier: 'Multiple KPI gauge'
      //     // events: {
      //     //     render: renderIcons
      //     // }
      // },

      // title: {
      //     text: 'Multiple KPI gauge',
      //     style: {
      //         fontSize: '24px'
      //     }
      // },

      // tooltip: {
      //     borderWidth: 0,
      //     backgroundColor: 'none',
      //     shadow: false,
      //     style: {
      //         fontSize: '16px'
      //     },
      //     valueSuffix: '%',
      //     pointFormat: '{series.name}<br>' +
      //         '<span style="font-size: 2em; color: {point.color}; ' +
      //         'font-weight: bold">{point.y}</span>',
      //     // positioner: function (labelWidth) {
      //     //     return {
      //     //         x: (this.chart.chartWidth - labelWidth) / 2,
      //     //         y: (this.chart.plotHeight / 2) + 15
      //     //     };
      //     // }
      // },

      // pane: {
      //     startAngle: 0,
      //     endAngle: 360,
      //     background: [{ // Track for Conversion
      //         outerRadius: '112%',
      //         innerRadius: '88%',
      //         backgroundColor: trackColors[0],
      //         borderWidth: 0
      //     }, { // Track for Engagement
      //         outerRadius: '87%',
      //         innerRadius: '63%',
      //         backgroundColor: trackColors[1],
      //         borderWidth: 0
      //     }, { // Track for Feedback
      //         outerRadius: '62%',
      //         innerRadius: '38%',
      //         backgroundColor: trackColors[2],
      //         borderWidth: 0
      //     }]
      // },

      // yAxis: {
      //     min: 0,
      //     max: 100,
      //     lineWidth: 0,
      //     tickPositions: []
      // },

      // plotOptions: {
      //     solidgauge: {
      //         dataLabels: {
      //             enabled: false
      //         },
      //         linecap: 'round',
      //         stickyTracking: false,
      //         rounded: true
      //     }
      // },

      // series: [{
      //     name: 'Conversion',
      //     data: [{
      //         color: Highcharts.getOptions().colors[0],
      //         radius: '112%',
      //         innerRadius: '88%',
      //         y: 80
      //     }],
      //     custom: {
      //         icon: 'filter',
      //         iconColor: '#303030'
      //     }
      // }, {
      //     name: 'Engagement',
      //     data: [{
      //         color: Highcharts.getOptions().colors[1],
      //         radius: '87%',
      //         innerRadius: '63%',
      //         y: 65
      //     }],
      //     custom: {
      //         icon: 'comments-o',
      //         iconColor: '#ffffff'
      //     }
      // }, {
      //     name: 'Feedback',
      //     data: [{
      //         color: Highcharts.getOptions().colors[2],
      //         radius: '62%',
      //         innerRadius: '38%',
      //         y: 50
      //     }],
      //     custom: {
      //         icon: 'commenting-o',
      //         iconColor: '#303030'
      //     }
      // }]
      // },
      // {
      //   chart: {
      //     type: 'gauge',
      //     plotBackgroundColor: null,
      //     plotBackgroundImage: null,
      //     plotBorderWidth: 0,
      //     plotShadow: false,
      //     height: '80%',
      //     identifier:'clock gauge'
      // },

      // credits: {
      //     enabled: false
      // },

      // title: {
      //     text: 'The Highcharts clock'
      // },

      // pane: {
      //   background: [{
      //     // default background
      //   }, {
      //     // reflex for supported browsers
      //     backgroundColor: {
      //       radialGradient: {
      //         cx: 0.5,
      //         cy: -0.4,
      //         r: 1.9
      //       },
      //       stops: [
      //         [0.5, 'rgba(255, 255, 255, 0.2)'],
      //         [0.5, 'rgba(200, 200, 200, 0.2)']
      //       ]
      //     }
      //   }]
      // },

      // yAxis: {
      //     labels: {
      //         distance: -23,
      //         style: {
      //             fontSize: '18px'
      //         }
      //     },
      //     min: 0,
      //     max: 12,
      //     lineWidth: 0,
      //     showFirstLabel: false,

      //     minorTickInterval: 'auto',
      //     minorTickWidth: 3,
      //     minorTickLength: 5,
      //     minorTickPosition: 'inside',
      //     minorGridLineWidth: 0,
      //     minorTickColor: '#666',

      //     tickInterval: 1,
      //     tickWidth: 4,
      //     tickPosition: 'inside',
      //     tickLength: 10,
      //     tickColor: '#666',
      //     title: {
      //         // text: 'Powered by<br/>Highcharts',
      //         style: {
      //             color: '#BBB',
      //             fontWeight: 'normal',
      //             fontSize: '10px',
      //             lineHeight: '10px'
      //         },
      //         y: 10
      //     }
      // },

      // tooltip: {
      //     format: '{series.chart.tooltipText}'
      // },

      // series: [{
      //     data: [{
      //         id: 'hour',
      //         y: now.hours,
      //         dial: {
      //             radius: '60%',
      //             baseWidth: 4,
      //             baseLength: '95%',
      //             rearLength: 0
      //         }
      //     }, {
      //         id: 'minute',
      //         y: now.minutes,
      //         dial: {
      //             baseLength: '95%',
      //             rearLength: 0
      //         }
      //     }, {
      //         id: 'second',
      //         y: now.seconds,
      //         dial: {
      //             radius: '100%',
      //             baseWidth: 1,
      //             rearLength: '20%'
      //         }
      //     }],
      //     animation: false,
      //     dataLabels: {
      //         enabled: false
      //     }
      // }]
      // }
      
      
    ];
  }

  
  onQueryChange(newValue: any)
  {
    // Handle the change event here
    this.getRowDate = GlobalConstants.rowSourceApi + localStorage.getItem("agGidSelectedLookup_(query)_id");
  }

  Next() {
    var sum = 0;

    if (this.chartForm.controls['title']?.value == "" || this.chartForm.controls['Row']?.value == "" || this.chartForm.controls['Column']?.value == "" || this.chartForm.controls['query']?.value == "") {
    } else {
      sum++;
    }
    this.isAdvanced = sum;
  }

  nextTab(type:any, identifier: string, e: any,) {
    if(e == 'c'){
    this.chartType = identifier;
    var sum = 0
    sum++;
    this.isSource = sum;
    }
    else
    {
      if(identifier == "candlestick")
      {
        this.chartType = "candlestick";
        var sum = 0
        sum++;
        this.isSource = sum;
      }else
        if(identifier == "line")
        {
          this.chartType = "stockLine";
          var sum = 0
          sum++;
          this.isSource = sum;

        }else
          if(identifier == "column")
          {
            this.chartType = 'stockColumn';
            var sum = 0
            sum++;
            this.isSource = sum;

          }else
            if(identifier == "ohlc")
            {
              this.chartType = identifier;
              var sum = 0
              sum++;
              this.isSource = sum;
            }else
              if(identifier == "area")
              {
                this.chartType = "stockArea";
                var sum = 0
                sum++;
                this.isSource = sum;
              }else
              if(identifier == "VU solid")
              {
                this.chartType = type;
                var sum = 0
                sum++;
                this.isSource = sum;
              }else
              if(identifier == "VU meter")
              {
                this.chartType = type;
                var sum = 0
                sum++;
                this.isSource = sum;
              }
                if(identifier == "Speedometer")
                {
                  this.chartType = type;
                  var sum = 0
                  sum++;
                  this.isSource = sum;
                }
                else
                  if(identifier == "Dual Axes Speedometer")
                  {
                    this.chartType = type;
                    var sum = 0
                    sum++;
                    this.isSource = sum;
                  }else
                    if(identifier == "Speedometer solid")
                    {
                      this.chartType = type;
                      var sum = 0
                      sum++;
                      this.isSource = sum;
                    }
                    // else
                    //   if(identifier == "Multiple KPI gauge")
                    //   {
                    //     this.chartType = type;
                    //     var sum = 0
                    //     sum++;
                    //     this.isSource = sum;
                    //   }
                      // else
                      //   if(identifier == "clock gauge")
                      //   {
                      //     this.chartType = type;
                      //     var sum = 0
                      //     sum++;
                      //     this.isSource = sum;
                      //   }
    }
  }
  toggle() {
    if (this.chartForm.get("isShowLegend").value == 'true') {
      this.legends = 1;
    } else this.legends = 0;
  }
  submit() {
    let toggleshowlegendValue;
    let toggleIs3dValue;
    let toggleIsHorizontalValue;
    let toggleisDrilldownValue;
    let type;
    if (this.chartForm.get("isShowLegend").value == true) {
      toggleshowlegendValue = 1;
    } else {
      toggleshowlegendValue = 0;
    }
    if (this.chartForm.get("is3d").value == true) {
      toggleIs3dValue = 1;
    } else {
      toggleIs3dValue = 0;
    }
    if (this.chartForm.get("isHorizental").value == true) {
      toggleIsHorizontalValue = 1;
    } else {
      toggleIsHorizontalValue = 0;
    }
    if (this.chartForm.get("isDrilldown").value == true) {
      toggleisDrilldownValue = 1;
    } else {
      toggleisDrilldownValue = 0;
    }
    if (this.chartType == "heatmap") {
      type = 1;
    } else if (this.chartType == "pie") {
      type = 2;
    } else if (this.chartType == "bar") {
      type = 3;
    } else if (this.chartType == "line") {
      type = 4;
    } else if (this.chartType == "area") {
      type = 5;
    } else if (this.chartType == "scatter") {
      type = 6;
    } else if (this.chartType == "column") {
      type = 7;
    }else if (this.chartType == "semiPie") {
      type = 8;
    }else if ( this.chartType == "candlestick"){
      type = 9;
    }else if (this.chartType == "stockLine"){
      type = 10;
    }else if (this.chartType == "stockColumn"){
      type = 11;
    }else if ( this.chartType == "ohlc"){
      type = 12;
    }else if ( this.chartType == "stockArea"){
      type = 13;
    }else if (this.chartType == "VU solid") {
      type = 14;
    }else if (this.chartType == "VU meter") {
      type = 15;
    } else if (this.chartType == "Speedometer") {
      type = 16;
    } else if (this.chartType == "Dual Axes Speedometer") {
      type = 17;
    }else if (this.chartType == "Speedometer solid") {
      type = 18;
    }
    // else if (this.chartType == "Multiple KPI gauge") {
    //   type = 19;
    // }
    // else if (this.chartType == "clock gauge") {
    //   type = 20;
    // }


    if (this.actionType == 'update') {
      let selectedNodes = this.informationservice.getAgGidSelectedNode();
      let allData = {
        chartId: this.informationservice.getAgGidSelectedNode(),
        chartName: this.chartForm.get("title").value,
        // objectKpiId: 0,
        query: localStorage.getItem("agGidSelectedLookup_(query)_id"),
        // chartSize:"",
        chartHTitle: this.chartForm.get("Row").value,
        chartVTitle: this.chartForm.get("Column").value,
        showLegend: toggleshowlegendValue,
        is3d: toggleIs3dValue,
        isHorizontal: toggleIsHorizontalValue,
        userId: this.informationservice.getLogeduserId(),
        queryFieldName: localStorage.getItem("agGidSelectedLookup_(query)_name"),
        serieType: 0,
        drilldown: toggleisDrilldownValue,
        drilldownType: 1,
        chartType: type,
      }
      this.http.post<any>(GlobalConstants.updateChartApi, allData, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          this.commonFunctions.navigateToPage("/dsp/chartBuilder");
          this.commonFunctions.reloadPage("/dsp/chartBuilder");
        })
    } else {
      let describtionValue: String;
      if(this.chartForm.get("describtion").value == null)
      {
        describtionValue = "";
      }
      else
      {
        describtionValue = this.chartForm.get("describtion").value;
      }
      
      this.agGridSelectedNodes = localStorage.getItem('agGidSelectedLookup_(Row)_id');
      let allData = {
        chartName: this.chartForm.get("title").value,
        // objectKpiId:"",
        query: localStorage.getItem("agGidSelectedLookup_(query)_id"),
        // chartSize:"",
        chartHTitle: this.chartForm.get("Column").value,
        chartVTitle: this.chartForm.get("Row").value,
        chartDescription: describtionValue,
        showLegend: toggleshowlegendValue,
        is3d: toggleIs3dValue,
        isHorizontal: toggleIsHorizontalValue,
        userId: this.informationservice.getLogeduserId(),
        // fieldName:this.chartForm.get("Row").value,
        queryFieldName: localStorage.getItem("agGidSelectedLookup_(query)_name"),
        drilldown: toggleisDrilldownValue,
        drilldownType: 1,
        chartType: type,
      }
      this.http.post<any>(GlobalConstants.addChartApi, allData, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          this.commonFunctions.navigateToPage("/dsp/chartBuilder");
          this.commonFunctions.reloadPage("/dsp/chartBuilder");
        })
    }

    this.closeDialog();
  }
  closeDialog() {
    this.dialog.closeAll();
  }


}