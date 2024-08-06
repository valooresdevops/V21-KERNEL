import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { Subscription } from 'rxjs';
import { identifier, reservedWord, string } from 'sql-formatter/lib/src/lexer/regexFactory';
import { CellRenderer } from './cellRenderer';
import { ButtonRendererComponent } from 'src/app/Kernel/kernelapp/qbe/query-builder/buttonRenderer.component';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { ApplicationListComponent } from './application-list/application-list.component';
import { ParentMenuListComponent } from './parent-menu-list/parent-menu-list.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpClient } from '@angular/common/http';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { ActivatedRoute, Router } from '@angular/router';
import { PreviewScreenComponent } from './preview-screen/preview-screen.component';
import { ChartBuilderFormComponent } from '../../object-builder/chart-builder-form/chart-builder-form.component';
import { KpiBuilderPreviewComponent } from '../../object-builder/kpi-builder-preview/kpi-builder-preview.component';
import { GridBuilderPreviewComponent } from '../../object-builder/grid-builder-preview/grid-builder-preview.component';
import { ChartFromKpiBuilderComponent } from '../../object-builder/chart-from-kpi-builder/chart-from-kpi-builder.component';
import Highcharts from 'highcharts';
import Highcharts3D from 'highcharts/highcharts-3d';
Highcharts3D(Highcharts);


import HighchartsMore from 'highcharts/highcharts-more';
import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);


declare var CKEDITOR: any;
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-screen-builder-tool',
  templateUrl: './screen-builder-tool.component.html',
  styleUrls: ['./screen-builder-tool.component.css']
})
export class ScreenBuilderToolComponent implements OnInit {
  @ViewChild("editorElement") editorElement: ElementRef;
  Highcharts: typeof Highcharts = Highcharts;

  public identifier: string;

  public isEditor: any;
  public isForm: any;
  public gridCellRender: AgColumns[] = [];
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public agColumns1: AgColumns[] = [];
  public agColumnsJson1: any;
  public agColumns2: AgColumns[] = [];
  public agColumnsJson2: any;
  public agColumns3: AgColumns[] = [];
  public agColumnsJson3: any;
  public content: any;
  public agGridSelectedNodes: any = '';
  public comboDatasource = [{}];
  subsVar: Subscription;
  public inDisplayGrid = GlobalConstants.fetchInDispMappingApi;
  public fetchGridsTableData = GlobalConstants.fetchGridsTableData;
  public getAllChartScreenBuilder = GlobalConstants.getAllChartScreenBuilder;
  public getAllGridScreenBuilder = GlobalConstants.getAllGridScreenBuilder;
  public getAllKpiScreenBuilder = GlobalConstants.getAllKpiScreenBuilder;
  public getGridQueryData: any;

  public isUpdate: boolean = false;
  frameworkComponents: any;
  frameworkComponents1: any;
  public status: any;
  public screenId: any;
  public showPanel: boolean = true;
  public showForm: boolean = false;
  public showGrid: boolean = false;
  public showChart: boolean = false;
  public showKpi: boolean = false;
  public actionType: any = '';
  public gridData: any;

  public objectId: any;
  public multiSelection: any;
  stockAd:number = 0;


  // editorConfig = {
  //   startupMode: 'source',
  //   extraPlugins: 'sourcearea', // Include the sourcearea plugin
  //   forcePasteAsPlainText: true
  // };


  public itemsData: any[] = [];
  public chartsData: any[] = [];

  showRightButtons = true;
  arrowDirection: string = 'fa-arrow-left';

  public screenData: any[] = [];

  public chartData: any[] = [];
  public formData: any[] = [];
  ids: string[] = [];
  names: number[] = [];
  chartValue: any[] = [];
  chartObject: any[] = [];
  newChartObject: any[] = [];
  stockChartObject: any[] = [];

  barRecords: any[] = [];
  radarRecords: any[] = [];
  chartType: any;
  gridObject: any[] = [];

  constructor(private sanitizer: DomSanitizer, private dialog: MatDialog,
    private eventEmitterService: EventEmitterService,
    private snackBar: MatSnackBar,
    private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private _Activatedroute: ActivatedRoute,
    private route: Router,
    public informationservice: InformationService,


  ) {

    this.onRunButtonClick = this.onRunButtonClick.bind(this);
    this.onRunCellButtonClickGrid = this.onRunCellButtonClickGrid.bind(this);
    this.onRunCellButtonClickChart = this.onRunCellButtonClickChart.bind(this);
    this.onRunCellButtonClickKpi = this.onRunCellButtonClickKpi.bind(this);
    this.frameworkComponents = {
      cellRenderer: CellRenderer,
      buttonRenderer: ButtonRendererComponent
    };



    this.onRunButtonClickChart = this.onRunButtonClickChart.bind(this);
    this.onRunButtonClickGrid = this.onRunButtonClickGrid.bind(this);

    // this.frameworkComponents1 = {
    //   buttonRenderer: ButtonRendererComponent,
    // };
  }


  screenForm = new UntypedFormGroup({
    screenName: new UntypedFormControl(''),
    screenIsTemplate: new UntypedFormControl(''),
    screenApplication: new UntypedFormControl(''),
    screenParentMenu: new UntypedFormControl(''),
    screenIsSuspended: new UntypedFormControl(''),
    screenParentMenuId: new UntypedFormControl(''),
    screenApplicationId: new UntypedFormControl(''),
    nextAction: new UntypedFormControl(''),

  });



  ngOnInit(): void {



    this._Activatedroute.paramMap.subscribe((params:any) => {

      this.screenId = params.get('id');

      this.actionType = params.get('actionType');
    });


    if (this.actionType == 'update') {
      this.isUpdate = true;

      this.http.get<any>(GlobalConstants.getScreenData + this.screenId, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          if (res.status == 'Fail') {
          } else {

            this.screenForm.controls['screenName'].setValue(res[0].screenName);
            // this.screenForm.controls['screenIsTemplate'].setValue(res[0].screenIsTemplate);
            this.screenForm.controls['screenApplication'].setValue(res[0].screenApplication);
            this.screenForm.controls['screenParentMenu'].setValue(res[0].screenParentMenu);
            if (res[0].screenIsSuspended == 'Active') {
              this.screenForm.controls['screenIsSuspended'].setValue(false);
            } else {
              this.screenForm.controls['screenIsSuspended'].setValue(true);
            }
            if (res[0].nextAction == 'false') {
              this.screenForm.controls['nextAction'].setValue(false);
            } else {
              this.screenForm.controls['nextAction'].setValue(true);
            }
            this.screenForm.controls['screenParentMenuId'].setValue(res[0].screenParentMenuId);
            this.screenForm.controls['screenApplicationId'].setValue(res[0].screenApplicationId);
            this.screenData = JSON.parse(res[1].data);
            for (let i = 0; i < this.screenData.length; i++) {


              setTimeout(() => {
                this.itemsData[this.screenData[i].index] = { id: this.screenData[i].id, type: this.screenData[i].type, value: this.screenData[i].value, mode: this.screenData[i].mode, formData: this.screenData[i].formData };
              }, 1000);

            }
          }
        });

    }

    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        defaultMinWidth: '40',
        maxWidth: '40',
        cellRenderer: CellRenderer,
        cellRendererParams: {
          onClick: this.onRunButtonClick.bind(this),
          label: 'Click 1'
        }
      },
      {
        headerName: 'Menu Name',
        field: 'menuName',
        filter: 'agTextColumnFilter',
        sortable: true,
        defaultMinWidth: '180',
        maxWidth: '300',

      },
      {
        headerName: 'Sequence Id',
        field: 'objectId',
        defaultMinWidth: '180',
        maxWidth: '300',
        filter: 'agTextColumnFilter',
        sortable: true
      }
    ]

    this.agColumnsJson1 = [
      {
        headerName: '',
        field: '',
        defaultMinWidth: '50',
        maxWidth: '50',
        cellRenderer: CellRenderer,
        cellRendererParams: {
          onClick: this.onRunCellButtonClickGrid.bind(this),
          label: 'Click 1'
        }
      },
      {
        headerName: 'Id',
        field: 'GRID_ID',
        filter: 'agTextColumnFilter',
        sortable: true,
        defaultMinWidth: '180',
        maxWidth: '300',

      },
      {
        headerName: 'Grid Name',
        field: 'GRID_NAME',
        defaultMinWidth: '180',
        maxWidth: '300',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Preview',
        field: '',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClickGrid.bind(this),
          label: 'Click 1'
        }
      }
    ]

    this.agColumnsJson2 = [
      {
        headerName: '',
        field: '',
        defaultMinWidth: '0',
        maxWidth: '0',
        cellRenderer: CellRenderer,
        cellRendererParams: {
          onClick: this.onRunCellButtonClickChart.bind(this),
          label: 'Click 1'
        }
      }, {
        headerName: '',
        field: '',
        checkboxSelection: true,
        headerCheckboxSelection: true
      },
      {
        headerName: 'Id',
        field: 'CHART_ID',
        filter: 'agTextColumnFilter',
        sortable: true,
        defaultMinWidth: '180',
        maxWidth: '300',

      },
      {
        headerName: 'Chart Name',
        field: 'CHART_NAME',
        defaultMinWidth: '180',
        maxWidth: '300',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Preview',
        field: '',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClickChart.bind(this),
          label: 'Click 1'
        }
      }

    ]



    this.agColumnsJson3 = [
      {
        headerName: '',
        field: '',
        defaultMinWidth: '50',
        maxWidth: '50',
        cellRenderer: CellRenderer,
        cellRendererParams: {
          onClick: this.onRunCellButtonClickKpi.bind(this),
          label: 'Click 1'
        }
      },
      {
        headerName: 'Id',
        field: 'KPI_ID',
        filter: 'agTextColumnFilter',
        sortable: true,
        defaultMinWidth: '180',
        maxWidth: '300',

      },
      {
        headerName: 'Kpi Name',
        field: 'KPI_NAME',
        defaultMinWidth: '180',
        maxWidth: '300',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Preview',
        field: '',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClickKpi.bind(this),
          label: 'Click 1'
        }
      }

    ]


    this.agColumns.push(this.agColumnsJson);
    this.agColumns1.push(this.agColumnsJson1);
    this.agColumns2.push(this.agColumnsJson2);
    this.agColumns3.push(this.agColumnsJson3);

  }

  onRunButtonClickChart(e: any) {
    let info = {};
    let type = {};

    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {

    } else {
      // alert(111)

      this.http.post<any>(GlobalConstants.getQueryData + this.informationservice.getAgGidSelectedNode(), { headers: GlobalConstants.headers }).subscribe(
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

    }
  }

  onRunButtonClickGrid(e: any) {
    let info = {};
    let type = {};

    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {

    } else {

      this.http.post<any>(GlobalConstants.decodeGridQuery + selectedNodes, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = {
            data: res,

            id: this.informationservice.getAgGidSelectedNode(),
            flag: 0
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


    }
  }
  

  onRunButtonClickKpi(e: any) {
    let info = {};

    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {

    } else {

      this.http.post<any>(GlobalConstants.decodeKpiQuery + selectedNodes, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = {
            data: res,

            kpiId: this.informationservice.getAgGidSelectedNode()
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


  addForm() {
    this.showForm = true;
    this.showPanel = false;
    this.showGrid = false;
  }

  goBack() {
    this.showPanel = true;
    this.showForm = false;
    this.showGrid = false;
    this.showChart = false;
    this.showKpi = false;

  }

  addCharts() {
    this.showPanel = true;
    this.showForm = false;
    this.showGrid = false;
    this.showChart = false;
    this.showKpi = false;
  }

  addEditor() {
    this.itemsData.push({ id: this.generateSerial(), type: 'Editor', value: '', mode: 'wysiwyg', formData: '' });
  }

  addGrid() {
    this.showGrid = true;
    this.showPanel = false;
    this.showForm = false;
    this.showChart = false;
    this.showKpi = false;
  }

  addChart() {
    this.showChart = true;
    this.showPanel = false;
    this.showForm = false;
    this.showGrid = false;
    this.showKpi = false;
  }
  addKpi() {
    this.showKpi = true;
    this.showPanel = false;
    this.showForm = false;
    this.showGrid = false;
    this.showChart = false;
  }

  toggleRightButtons() {
    this.showRightButtons = !this.showRightButtons;
    this.arrowDirection = this.showRightButtons ? 'fa-arrow-left' : 'fa-arrow-right';
  }




  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.itemsData, event.previousIndex, event.currentIndex);

    for (let i = 0; i < this.itemsData.length; i++) {
    }
  }

  // showScreenBuilderList(list :any){
  //   for(let i=0;list.size();i++){
  //     this.sanitizeHtml(list[i]);
  //   }
  // }

  sanitizeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }


  onRunButtonClick(e: any) {
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {
    } else {
      setTimeout(() => {

        this.http.get<any>(GlobalConstants.getAllTabs + this.informationservice.getAgGidSelectedNode(), {
          headers: GlobalConstants.headers,
        }).subscribe((res: any) => {
          for (let i = 0; i < res.length; i++) {
            this.formData.push({
              "tableName": res[i].menuName,
              "canAdd": res[i].canAdd,
              "canDelete": res[i].canDelete,
              "canModify": res[i].canModify,
              "isAdvancedSearch": res[i].isAdvancedSearch,
              "hasMultipleSelection": res[i].hasMultipleSelection,
              "isQueryForm": res[i].isQueryForm,
              "isDynamicReport": res[i].isDynamicReport,
              "isGrid": res[i].isGrid,
              "isReadOnly": res[i].isReadOnly,
              "objectId": res[i].objectId
            })

          }
        },
        );

        this.itemsData.push({ id: this.generateSerial(), type: 'Form', value: this.informationservice.getAgGidSelectedNode(), mode: '', formData: this.formData });
      }, 50);
      setTimeout(() => {

        this.formData = []
          ;
      }, 100);

    }
  }




  onRunCellButtonClickGrid(e: any) {
    let info = {};
    this.gridCellRender = [];
    this.gridObject = [];
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {
    } else {

        this.http.post<any>(GlobalConstants.decodeGridQuery + this.informationservice.getAgGidSelectedNode(), { headers: GlobalConstants.headers }).subscribe(
          (res: any) => {
            this.gridCellRender.push(res);
            this.getGridQueryData = GlobalConstants.getGridQueryData + this.informationservice.getAgGidSelectedNode();
            this.gridObject.push(
              [this.gridCellRender],
              [this.getGridQueryData]
            );

          });

      this.itemsData.push({
        formData: this.gridObject,
        id: this.generateSerial(),
        mode: "",
        type: "Grid",

        value: this.informationservice.getAgGidSelectedNode()
      })
    }

  }

  async onRunCellButtonClickChart(e: any) {
    let info = {};

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

    if (this.informationservice.getAgGidSelectedNode().indexOf(',') != -1) {
      let parts = this.informationservice.getAgGidSelectedNode().split(',');
      for (let i = 0; i < parts.length; i++) {
        let part = parts[i];
      this.http.post<any>(GlobalConstants.getQueryData + part, { headers: GlobalConstants.headers }).subscribe(
          (res: any) => {
            if (res.chartType == 1) {
              this.chartType = 'heatmap';
            } else if (res.chartType == 2) {
              this.chartType = 'pie';
            } else if (res.chartType == 3) {
              this.chartType = 'bar';
            } else if (res.chartType == 4) {
              this.chartType = 'line';
            } else if (res.chartType == 5) {
              this.chartType = 'area';
            } else if (res.chartType == 6) {
              this.chartType = 'scatter';
            } else if (res.chartType == 7) {
              this.chartType = 'column';
            } else if (res.chartType == 8) {
              this.chartType = 'semiPie';
            } else if (res.chartType == 9) {
              this.chartType = 'candlestick';
            } else if (res.chartType == 10) {
              this.chartType = 'stockLine';
            } else if (res.chartType == 11) {
              this.chartType = 'stockColumn';
            } else if (res.chartType == 12) {
              this.chartType = 'ohlc';
            } else if (res.chartType == 13) {
              this.chartType = 'stockArea';
            } else if (res.chartType == 14) {
              this.chartType == "VU solid"
            } else if (res.chartType == 15) {
              this.chartType == "VU meter"
            } else if (res.chartType == 16) {
              this.chartType == "Speedometer"
            } else if (res.chartType == 17) {
              this.chartType == "Dual Axes Speedometer"
            } else if (res.chartType == 18) {
              this.chartType == "Speedometer solid"
            } else if (res.chartType == 19) {
              this.chartType == "Multiple KPI gauge"
            } else if (res.chartType == 20) {
              this.chartType == "clock gauge"
            }
            

            if (this.chartType == 'heatmap') {
              this.newChartObject.push({
                type:'Chart',
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
              });
            } else if(this.chartType == 'scatter')
              {
                
              for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }   
                if(res.is3d == 1){
                  this.newChartObject.push(
                    {
                      type:'Chart',
                      chart: { type: 'scatter'},
                      title: { text: res.records[0].TITLE },
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
                }else{           
                this.newChartObject.push(
                  {
                    type:'Chart',
                    chart: { type: 'scatter' },
                    title: { text: res.records[0].TITLE },
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
      );}
            } else if (this.chartType == 'pie') {
              for (let j = 0; j < res.records.length; j++) {
                this.ids.push(res.records[j].NAME);
                this.names.push(Number(res.records[j].Y));
              }
              let data1 = this.names.map((id, index) => {
                return { name: this.ids[index], y: id };
              });
              if (res.is3d == 1) {
                const transformedData = res.records.map((item: any) => [item.NAME, parseFloat(item.Y)]);

                this.newChartObject.push({
                  type:'Chart',
                  chart: {
                    type: 'pie',
                    options3d: {
                      enabled: true,
                      alpha: 45
                    }
                  },
                  title: {
                    text: res.records[0].TITLE,
                    align: 'left'
                  },
                  plotOptions: {
                    pie: {
                      innerSize: 100,
                      depth: 45
                    }
                  },
                  series: [{
                    name: 'Medals',
                    data: transformedData
                  }]
                });
              } else {
                this.newChartObject.push(
                  {
                    type:'Chart',
                    chart: { type: 'pie' },
                    title: { text: res.records[0].TITLE },
                    series: [{
                      name: '',
                      data: data1,
                    }]
                  }
                );
              }
             
            } else if (this.chartType == 'bar') {
              
              for (let i = 0; i < res.records.length; i++) {
                this.ids.push(res.records[i].ID);
                this.names.push(Number(res.records[i].NAME));
              }
              if (res.is3d == 1) {
                this.newChartObject.push(
                  {
                    type:'Chart',
                    chart: {
                      type: this.chartType,
                      options3d: {
                        enabled: true,
                        alpha: 10,
                        beta: 25,
                        depth: 70,
                        viewDistance: 25
                      }
                    },
                    title: { text: res.records[0].TITLE },
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
              } else {
                this.newChartObject.push(
                  {
                    type:'Chart',
                    chart: { type: this.chartType },
                    title: { text: res.records[0].TITLE },
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
                  });
              }
            } else if(this.chartType == 'area')
              {
          
                for (let j = 0; j < res.records.length; j++)
                  {
                    this.ids.push(res.records[j].ID);
                    this.names.push(Number(res.records[j].NAME));
                  }
                  if(res.is3d == 1){
                    this.newChartObject.push(
                      {
                        type:'Chart',
                        chart: { type: 'area',
                          options3d: {
                            enabled: true,
                            alpha: 10,
                            beta: 25,
                            depth: 70,
                            viewDistance: 25
                          } },
                        title: { text:res.records[0].TITLE},
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
                  }else{
                this.newChartObject.push(
                  {
                    type:'Chart',
                    chart: { type: 'area' },
                    title: { text: res.records[0].TITLE },
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
              }
            } else if (this.chartType == 'line') {
               
              for (let i = 0; i < res.records.length; i++) {
                this.ids.push(res.records[i].ID);
                this.names.push(Number(res.records[i].NAME));
              }
              if (res.is3d == 1) {
                this.newChartObject.push(
                  {
                    type:'Chart',
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
                      text: res.records[0].TITLE
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
                );
              } else {
                this.newChartObject.push(
                  {
                    type:'Chart',
                    chart: { type: 'line' },
                    title: { text: res.records[0].TITLE },
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
                  });
              }
            } else if (this.chartType == 'column') {
              for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }
  
                if(res.is3d == 1){
  
  
                  this.newChartObject.push({
                    type:'Chart',
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
                      text: res.records[0].TITLE,
                      align: 'left'
                  },
                  subtitle: {
                      text:res.records[0].TITLE,
                      align: 'left'
                  },
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
                  })
                }else{
  
                
              this.newChartObject.push({
                type:'Chart',
                chart: { type: 'column' },
                title: { text: res.records[0].TITLE },
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
              });}

            } else if (this.chartType == 'candlestick') {
              const transformedData1 = res.records.map((item: any) => [
                Number(item.timestamp),
                Number(item.open_price),
                Number(item.high_price),
                Number(item.low_price),
                Number(item.close_price)
              ]);
              this.newChartObject.push(
                {
                  type:'Stock',
                  rangeSelector: {
                    selected: 1
                  },

                  title: {
                    text: res.records[0].title
                  },

                  series: [{
                    type: 'candlestick',
                    name: res.records[0].title,
                    data: transformedData1,
                    dataGrouping: {
                      units: [
                        [
                          'week',
                          [1]
                        ], [
                          'month',
                          [1, 2, 3, 4, 6]
                        ]
                      ]
                    }
                  }]
                });

            } else if (this.chartType == 'stockLine') {
              const transformedData1 = res.records.map((item: any) => [
                Number(item.timestamp),
                Number(item.close_price)
              ]);
              this.newChartObject.push(
                {
                  type:'Stock',
                  rangeSelector: {
                    selected: 1
                  },

                  title: {
                    text: res.records[0].title
                  },

                  series: [{
                    name: res.records[0].title,
                    type: 'line',
                    data: transformedData1,
                    step: true,
                    tooltip: {
                      valueDecimals: 2
                    }
                  }]
                }
              );
            } else if (this.chartType == 'stockColumn') {
              const transformedData1 = res.records.map((item: any) => [
                Number(item.timestamp),
                Number(item.volume)
              ]);
              this.newChartObject.push(
                {
                  type:'Stock',
                  chart: {
                    alignTicks: false
                  },

                  rangeSelector: {
                    selected: 1
                  },

                  title: {
                    text: res.records[0].title
                  },
                  credits: {
                    enabled: false // Disable the credits link
                  },

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
                }
              );
            } else if (this.chartType == 'ohlc') {
              const transformedData1 = res.records.map((item: any) => [ 
                Number(item.timestamp),
                Number(item.open_price),
                Number(item.high_price),
                Number(item.low_price),
                Number(item.close_price)]);
                this.newChartObject.push({
                  type:'Stock',
                rangeSelector: {
                  selected: 2
                },
        
                title: {
                  text: res.records[0].title
                },
                credits: {
                  enabled: false // Disable the credits link
                },
        
                series: [{
                  type: 'ohlc',
                  name: res.records[0].title,
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
              });
            } else if (this.chartType == 'stockArea') {
              const transformedData1 = res.records.map((item: any) => [ 
                Number(item.timestamp),
                Number(item.close_price)]);
  
              this.newChartObject = [{
                type:'Stock',
                rangeSelector: {
                  selected: 1
              },
      
              title: {
                  text: res.records[0].title
              },
      
              series: [{
                  name: res.records[0].title,
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
            } else if (this.chartType == 'semiPie') {
              const transformedData = res.records.map((item: any) => [item.ID, parseFloat(item.NAME)]);
              if (res.is3d == 1) {
                this.newChartObject.push(
                  {
                    type:'Chart',
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
                      text: res.records[0].TITLE,
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
                      name: 'Browser share',
                      data: transformedData
                    }]
                  });
              } else {
                if (res.is3d == 1) {
                  this.newChartObject.push(
                    {
                      type:'Chart',
                      chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: 0,
                        plotShadow: false,
                        type: 'semiPie'
                      },
                      title: {
                        text: res.records[0].TITLE,
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

                }
          }
            } else if(this.chartType == 'VU solid')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'VU solid',
                      
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
                  }
                );
            } else if(this.chartType == 'VU meter')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                  
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'Multiple KPI gauge',
                      
                    chart:
                    {
                      type: 'solidgauge',
                      height: '100%',
                      identifier: 'Multiple KPI gauge'
                      // events: {
                      // render: renderIcons
                      // }
                    },
              
                    title: {
                        text: 'Multiple KPI gauge',
                        style: {
                            fontSize: '24px'
                        }
                    },
              
                    tooltip: {
                        borderWidth: 0,
                        backgroundColor: 'none',
                        shadow: false,
                        style: {
                            fontSize: '16px'
                        },
                        valueSuffix: '%',
                        pointFormat: '{series.name}<br>' +
                            '<span style="font-size: 2em; color: {point.color}; ' +
                            'font-weight: bold">{point.y}</span>',
                        // positioner: function (labelWidth) {
                        //     return {
                        //         x: (this.chart.chartWidth - labelWidth) / 2,
                        //         y: (this.chart.plotHeight / 2) + 15
                        //     };
                        // }
                    },
              
                    pane: {
                        startAngle: 0,
                        endAngle: 360,
                        background: [{ // Track for Conversion
                            outerRadius: '112%',
                            innerRadius: '88%',
                            backgroundColor: trackColors[0],
                            borderWidth: 0
                        }, { // Track for Engagement
                            outerRadius: '87%',
                            innerRadius: '63%',
                            backgroundColor: trackColors[1],
                            borderWidth: 0
                        }, { // Track for Feedback
                            outerRadius: '62%',
                            innerRadius: '38%',
                            backgroundColor: trackColors[2],
                            borderWidth: 0
                        }]
                    },
              
                    yAxis: {
                        min: 0,
                        max: 100,
                        lineWidth: 0,
                        tickPositions: []
                    },
              
                    plotOptions: {
                        solidgauge: {
                            dataLabels: {
                                enabled: false
                            },
                            linecap: 'round',
                            stickyTracking: false,
                            rounded: true
                        }
                    },
              
                    series: [{
                        name: 'Conversion',
                        data: [{
                            color: Highcharts.getOptions().colors[0],
                            radius: '112%',
                            innerRadius: '88%',
                            y: 80
                        }],
                        custom: {
                            icon: 'filter',
                            iconColor: '#303030'
                        }
                    }, {
                        name: 'Engagement',
                        data: [{
                            color: Highcharts.getOptions().colors[1],
                            radius: '87%',
                            innerRadius: '63%',
                            y: 65
                        }],
                        custom: {
                            icon: 'comments-o',
                            iconColor: '#ffffff'
                        }
                    }, {
                        name: 'Feedback',
                        data: [{
                            color: Highcharts.getOptions().colors[2],
                            radius: '62%',
                            innerRadius: '38%',
                            y: 50
                        }],
                        custom: {
                            icon: 'commenting-o',
                            iconColor: '#303030'
                        }
                    }]
                    }
                  );
            } else if(this.chartType == 'Dual Axes Speedometer')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                  
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'Multiple KPI gauge',
                      
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
                  }
                );
            } else if(this.chartType == 'Speedometer solid')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                  
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'Multiple KPI gauge',
                      
                    
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
                  }
                );
            } else if(this.chartType == 'Multiple KPI gauge')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                  
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'Multiple KPI gauge',
                      
                    chart: {
                      type: 'solidgauge',
                      height: '100%',
                      identifier: 'Multiple KPI gauge'
                      // events: {
                      //     render: renderIcons
                      // }
                  },
            
                  title: {
                      text: 'Multiple KPI gauge',
                      style: {
                          fontSize: '24px'
                      }
                  },
            
                  tooltip: {
                      borderWidth: 0,
                      backgroundColor: 'none',
                      shadow: false,
                      style: {
                          fontSize: '16px'
                      },
                      valueSuffix: '%',
                      pointFormat: '{series.name}<br>' +
                          '<span style="font-size: 2em; color: {point.color}; ' +
                          'font-weight: bold">{point.y}</span>',
                      // positioner: function (labelWidth) {
                      //     return {
                      //         x: (this.chart.chartWidth - labelWidth) / 2,
                      //         y: (this.chart.plotHeight / 2) + 15
                      //     };
                      // }
                  },
            
                  pane: {
                      startAngle: 0,
                      endAngle: 360,
                      background: [{ // Track for Conversion
                          outerRadius: '112%',
                          innerRadius: '88%',
                          backgroundColor: trackColors[0],
                          borderWidth: 0
                      }, { // Track for Engagement
                          outerRadius: '87%',
                          innerRadius: '63%',
                          backgroundColor: trackColors[1],
                          borderWidth: 0
                      }, { // Track for Feedback
                          outerRadius: '62%',
                          innerRadius: '38%',
                          backgroundColor: trackColors[2],
                          borderWidth: 0
                      }]
                  },
            
                  yAxis: {
                      min: 0,
                      max: 100,
                      lineWidth: 0,
                      tickPositions: []
                  },
            
                  plotOptions: {
                      solidgauge: {
                          dataLabels: {
                              enabled: false
                          },
                          linecap: 'round',
                          stickyTracking: false,
                          rounded: true
                      }
                  },
            
                  series: [{
                      name: 'Conversion',
                      data: [{
                          color: Highcharts.getOptions().colors[0],
                          radius: '112%',
                          innerRadius: '88%',
                          y: 80
                      }],
                      custom: {
                          icon: 'filter',
                          iconColor: '#303030'
                      }
                  }, {
                      name: 'Engagement',
                      data: [{
                          color: Highcharts.getOptions().colors[1],
                          radius: '87%',
                          innerRadius: '63%',
                          y: 65
                      }],
                      custom: {
                          icon: 'comments-o',
                          iconColor: '#ffffff'
                      }
                  }, {
                      name: 'Feedback',
                      data: [{
                          color: Highcharts.getOptions().colors[2],
                          radius: '62%',
                          innerRadius: '38%',
                          y: 50
                      }],
                      custom: {
                          icon: 'commenting-o',
                          iconColor: '#303030'
                      }
                  }]
                  }
                );
            } else if(this.chartType == 'clock gauge')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                  
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'Multiple KPI gauge',
                      
                    chart: {
                      type: 'gauge',
                      plotBackgroundColor: null,
                      plotBackgroundImage: null,
                      plotBorderWidth: 0,
                      plotShadow: false,
                      height: '80%',
                      identifier:'clock gauge'
                  },
            
                  credits: {
                      enabled: false
                  },
            
                  title: {
                      text: 'The Highcharts clock'
                  },
            
                  pane: {
                    background: [{
                      // default background
                    }, {
                      // reflex for supported browsers
                      backgroundColor: {
                        radialGradient: {
                          cx: 0.5,
                          cy: -0.4,
                          r: 1.9
                        },
                        stops: [
                          [0.5, 'rgba(255, 255, 255, 0.2)'],
                          [0.5, 'rgba(200, 200, 200, 0.2)']
                        ]
                      }
                    }]
                  },
            
                  yAxis: {
                      labels: {
                          distance: -23,
                          style: {
                              fontSize: '18px'
                          }
                      },
                      min: 0,
                      max: 12,
                      lineWidth: 0,
                      showFirstLabel: false,
            
                      minorTickInterval: 'auto',
                      minorTickWidth: 3,
                      minorTickLength: 5,
                      minorTickPosition: 'inside',
                      minorGridLineWidth: 0,
                      minorTickColor: '#666',
            
                      tickInterval: 1,
                      tickWidth: 4,
                      tickPosition: 'inside',
                      tickLength: 10,
                      tickColor: '#666',
                      title: {
                          // text: 'Powered by<br/>Highcharts',
                          style: {
                              color: '#BBB',
                              fontWeight: 'normal',
                              fontSize: '10px',
                              lineHeight: '10px'
                          },
                          y: 10
                      }
                  },
            
                  tooltip: {
                      format: '{series.chart.tooltipText}'
                  },
            
                  series: [{
                      data: [{
                          id: 'hour',
                          // y: now.hours,
                          dial: {
                              radius: '60%',
                              baseWidth: 4,
                              baseLength: '95%',
                              rearLength: 0
                          }
                      }, {
                          id: 'minute',
                          // y: now.minutes,
                          dial: {
                              baseLength: '95%',
                              rearLength: 0
                          }
                      }, {
                          id: 'second',
                          // y: now.seconds,
                          dial: {
                              radius: '100%',
                              baseWidth: 1,
                              rearLength: '20%'
                          }
                      }],
                      animation: false,
                      dataLabels: {
                          enabled: false
                      }
                  }]
                  }
                );
            }





            else
            {}
            this.ids = [];
            this.names = [];
              if((this.newChartObject.length) == parts.length)
              {
                console.log(parts.length-1)
            
                console.log("newChartObject ==== ", this.newChartObject);

                console.log("formdata = " + this.newChartObject)
                console.log("formdata = ", JSON.stringify(this.newChartObject, null, 2));

                if(this.newChartObject.length > 0)
                {
                  this.itemsData.push
                  ({
                    formData: this.newChartObject,
                    id: this.generateSerial(),
                    mode: "",
                    type: "Chart",
                    value: this.informationservice.getAgGidSelectedNode(),
                    number: res.chartType,
                  });
                }
                  console.log('itemsData----->',this.itemsData);
                // if(this.stockChartObject.length > 0)
                // {
                //   this.itemsData.push
                //   ({
                //     formData: this.newChartObject,
                //     id: this.generateSerial(),
                //     mode: "",
                //     type: "Stock",
                //     value: this.informationservice.getAgGidSelectedNode()
                //   });
                // }

                this.newChartObject = []
                this.stockChartObject = []
              }
          });
      }
    }
    else {
      this.http.post<any>(GlobalConstants.getQueryData + this.informationservice.getAgGidSelectedNode(), { headers: GlobalConstants.headers }).subscribe(
         (res: any) => {
          if (res.chartType == 1) {
            this.chartType = 'heatmap';
          } else if (res.chartType == 2) {
            this.chartType = 'pie';
          } else if (res.chartType == 3) {
            this.chartType = 'bar';
          } else if (res.chartType == 4) {
            this.chartType = 'line';
          } else if (res.chartType == 5) {
            this.chartType = 'area';
          } else if (res.chartType == 6) {
            this.chartType = 'scatter';
          } else if (res.chartType == 7) {
            this.chartType = 'column';
          } else if (res.chartType == 8) {
            this.chartType = 'semiPie';
          }else if (res.chartType == 9) {
            this.chartType = 'candlestick';
          } else if (res.chartType == 10) {
            this.chartType = 'stockLine';
          } else if (res.chartType == 11) {
            this.chartType = 'stockColumn';
          } else if (res.chartType == 12){
            this.chartType = 'ohlc';
          } else if (res.chartType == 13) {
            this.chartType = 'stockArea';
          }else if (res.chartType == 14) {
            this.chartType == "VU solid"
          }else if (res.chartType == 15) {
            this.chartType == "VU meter"
          } else if (res.chartType == 16) {
            this.chartType == "Speedometer"
          } 

          else if (res.chartType == 17) {
            this.chartType == "Dual Axes Speedometer"
          } else if (res.chartType == 18) {
            this.chartType == "Speedometer solid"
          } else if (res.chartType == 19) {
            this.chartType == "Multiple KPI gauge"
          } else if (res.chartType == 20) {
            this.chartType == "clock gauge"
          }
          

          if (this.chartType == 'heatmap') {
            this.newChartObject.push({
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
            });
          } else if(this.chartType == 'area')
            {
        
              for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }
                if(res.is3d == 1){
                  this.newChartObject.push(
                    {
                      chart: { type: 'area',
                        options3d: {
                          enabled: true,
                          alpha: 10,
                          beta: 25,
                          depth: 70,
                          viewDistance: 25
                        } },
                      title: { text:res.records[0].TITLE},
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
                }else{
              this.newChartObject.push(
                {
                  chart: { type: 'area' },
                  title: { text: res.records[0].TITLE },
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
            }
          } else if(this.chartType == 'scatter')
            {
              
            for (let j = 0; j < res.records.length; j++)
              {
                this.ids.push(res.records[j].ID);
                this.names.push(Number(res.records[j].NAME));
              }   
              if(res.is3d == 1){
                this.newChartObject.push(
                  {
                    chart: { type: 'scatter'},
                    title: { text: res.records[0].TITLE },
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
              }else{           
              this.newChartObject.push(
                {
                  chart: { type: 'scatter' },
                  title: { text: res.records[0].TITLE },
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
    );}
          } else if (this.chartType == 'pie') {
            for (let j = 0; j < res.records.length; j++) {
              this.ids.push(res.records[j].NAME);
              this.names.push(Number(res.records[j].Y));
            }
            let data1 = this.names.map((id, index) => {
              return { name: this.ids[index], y: id };
            });
            if (res.is3d == 1) {
              const transformedData = res.records.map((item: any) => [item.NAME, parseFloat(item.Y)]);

              this.newChartObject.push({
                chart: {
                  type: 'pie',
                  options3d: {
                    enabled: true,
                    alpha: 45
                  }
                },
                title: {
                  text: res.records[0].TITLE,
                  align: 'left'
                },
                plotOptions: {
                  pie: {
                    innerSize: 100,
                    depth: 45
                  }
                },
                series: [{
                  name: 'Medals',
                  data: transformedData
                }]
              });
            } else {
              this.newChartObject.push(
                {
                  chart: { type: 'pie' },
                  title: { text: res.records[0].TITLE },
                  series: [{
                    name: '',
                    data: data1,
                  }]
                }
              );
            }
           
          } else if (this.chartType == 'bar') {
            for (let i = 0; i < res.records.length; i++) {
              this.ids.push(res.records[i].ID);
              this.names.push(Number(res.records[i].NAME));
            }
            if (res.is3d == 1) {
              this.newChartObject.push(
                {
                  chart: {
                    type: this.chartType,
                    options3d: {
                      enabled: true,
                      alpha: 10,
                      beta: 25,
                      depth: 70,
                      viewDistance: 25
                    }
                  },
                  title: { text: res.records[0].TITLE },
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
            } else {
              this.newChartObject.push(
                {
                  chart: { type: this.chartType },
                  title: { text: res.records[0].TITLE },
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
                });
            }
          } else if (this.chartType == 'line') {
             
            for (let i = 0; i < res.records.length; i++) {
              this.ids.push(res.records[i].ID);
              this.names.push(Number(res.records[i].NAME));
            }
            if (res.is3d == 1) {
              this.newChartObject.push(
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
                    text: res.records[0].TITLE
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
              );
            } else {
              this.newChartObject.push(
                {
                  chart: { type: 'line' },
                  title: { text: res.records[0].TITLE },
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
                });
            }
          } else if (this.chartType == 'column') {
            for (let j = 0; j < res.records.length; j++)
              {
                this.ids.push(res.records[j].ID);
                this.names.push(Number(res.records[j].NAME));
              }

              if(res.is3d == 1){


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
                    text: res.records[0].TITLE,
                    align: 'left'
                },
                subtitle: {
                    text:res.records[0].TITLE,
                    align: 'left'
                },
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
              title: { text: res.records[0].TITLE },
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

          } else if (this.chartType == 'candlestick') {
            const transformedData1 = res.records.map((item: any) => [
              Number(item.timestamp),
              Number(item.open_price),
              Number(item.high_price),
              Number(item.low_price),
              Number(item.close_price)
            ]);
            this.newChartObject.push(
              {
                type:'Stock',
                rangeSelector: {
                  selected: 1
                },

                title: {
                  text: res.records[0].title
                },

                series: [{
                  type: 'candlestick',
                  name: res.records[0].title,
                  data: transformedData1,
                  dataGrouping: {
                    units: [
                      [
                        'week',
                        [1]
                      ], [
                        'month',
                        [1, 2, 3, 4, 6]
                      ]
                    ]
                  }
                }]
              });

          } else if (this.chartType == 'stockLine') {
            const transformedData1 = res.records.map((item: any) => [
              Number(item.timestamp),
              Number(item.close_price)
            ]);
            this.newChartObject.push(
              {
                type:'Stock',
                rangeSelector: {
                  selected: 1
                },

                title: {
                  text: res.records[0].title
                },

                series: [{
                  name: res.records[0].title,
                  type: 'line',
                  data: transformedData1,
                  step: true,
                  tooltip: {
                    valueDecimals: 2
                  }
                }]
              }
            );
          } else if (this.chartType == 'stockColumn') {
            const transformedData1 = res.records.map((item: any) => [
              Number(item.timestamp),
              Number(item.volume)
            ]);
            this.newChartObject.push(
              {
                type:'Stock',
                chart: {
                  alignTicks: false
                },

                rangeSelector: {
                  selected: 1
                },

                title: {
                  text: res.records[0].title
                },
                credits: {
                  enabled: false // Disable the credits link
                },

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
              }
            );
          } else if (this.chartType == 'ohlc') {
            const transformedData1 = res.records.map((item: any) => [ 
              Number(item.timestamp),
              Number(item.open_price),
              Number(item.high_price),
              Number(item.low_price),
              Number(item.close_price)]);
              this.newChartObject.push({
              type:'Stock',
              rangeSelector: {
                selected: 2
              },
      
              title: {
                text: res.records[0].title
              },
              credits: {
                enabled: false // Disable the credits link
              },
      
              series: [{
                type: 'ohlc',
                name: res.records[0].title,
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
            })
          } else if (this.chartType == 'stockArea') {
            const transformedData1 = res.records.map((item: any) => [ 
              Number(item.timestamp),
              Number(item.close_price)]);

            this.newChartObject = [{
              type:'Stock',
              rangeSelector: {
                selected: 1
            },
    
            title: {
                text: res.records[0].title
            },
    
            series: [{
                name: res.records[0].title,
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
          } else if (this.chartType == 'semiPie') {

            const transformedData = res.records.map((item: any) => [item.ID, parseFloat(item.NAME)]);
            if (res.is3d == 1) {
              this.newChartObject.push(
                {
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
                    text: res.records[0].TITLE,
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
                    name: 'Browser share',
                    data: transformedData
                  }]
                });
            } else {
              if (res.is3d == 1) {
                this.newChartObject.push(
                  {
                    chart: {
                      plotBackgroundColor: null,
                      plotBorderWidth: 0,
                      plotShadow: false,
                      type: 'semiPie'
                    },
                    title: {
                      text: res.records[0].TITLE,
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

              }
          }
          } else if(this.chartType == 'VU solid')
                        {
                          for (let j = 0; j < res.records.length; j++)
                            {
                              this.ids.push(res.records[j].ID);
                              this.names.push(Number(res.records[j].NAME));
                            }
                            
                            

                          this.newChartObject.push(
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
                                startAngle: -45,
                                endAngle: 45,
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
                                    y: 32, // Adjusted label position to match the new size
                                    rotation: 'auto',
                                    distance: 20 // Increased distance to match new size
                                },
                                title: {
                                    text: 'VU<br/><span style="font-size:20px">Channel A</span>', // Adjusted font size
                                    y: -20 // Adjusted vertical position
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
                            }
                          );
          } else if(this.chartType == 'VU meter')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }        
                  
                this.newChartObject.push
                (
                  {
                    type:'solidgauge', identifier: 'Multiple KPI gauge',
                      
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
                    }
                  );
          } else if(this.chartType == 'Speedometer')
          {
            for (let j = 0; j < res.records.length; j++)
            {
              this.ids.push(res.records[j].ID);
              this.names.push(Number(res.records[j].NAME));
            }
                
                

            this.newChartObject.push(
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
              }
            );
          } else if(this.chartType == 'Dual Axes Speedometer')
            {
              for (let j = 0; j < res.records.length; j++)
              {
                this.ids.push(res.records[j].ID);
                this.names.push(Number(res.records[j].NAME));
              }
                  
                  
  
              this.newChartObject.push(
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
                }
              );
          } else if(this.chartType == 'Speedometer solid')
              {
                for (let j = 0; j < res.records.length; j++)
                {
                  this.ids.push(res.records[j].ID);
                  this.names.push(Number(res.records[j].NAME));
                }
                    
                    
    
                this.newChartObject.push(
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
                  }
                );
          } else if(this.chartType == 'Multiple KPI gauge')
                {
                  for (let j = 0; j < res.records.length; j++)
                  {
                    this.ids.push(res.records[j].ID);
                    this.names.push(Number(res.records[j].NAME));
                  }
                      
                      
      
                  this.newChartObject.push(
                    {
                      chart: {
                        type: 'solidgauge',
                        height: '100%',
                        identifier: 'Multiple KPI gauge'
                        // events: {
                        //     render: renderIcons
                        // }
                    },
              
                    title: {
                        text: 'Multiple KPI gauge',
                        style: {
                            fontSize: '24px'
                        }
                    },
              
                    tooltip: {
                        borderWidth: 0,
                        backgroundColor: 'none',
                        shadow: false,
                        style: {
                            fontSize: '16px'
                        },
                        valueSuffix: '%',
                        pointFormat: '{series.name}<br>' +
                            '<span style="font-size: 2em; color: {point.color}; ' +
                            'font-weight: bold">{point.y}</span>',
                        // positioner: function (labelWidth) {
                        //     return {
                        //         x: (this.chart.chartWidth - labelWidth) / 2,
                        //         y: (this.chart.plotHeight / 2) + 15
                        //     };
                        // }
                    },
              
                    pane: {
                        startAngle: 0,
                        endAngle: 360,
                        background: [{ // Track for Conversion
                            outerRadius: '112%',
                            innerRadius: '88%',
                            backgroundColor: trackColors[0],
                            borderWidth: 0
                        }, { // Track for Engagement
                            outerRadius: '87%',
                            innerRadius: '63%',
                            backgroundColor: trackColors[1],
                            borderWidth: 0
                        }, { // Track for Feedback
                            outerRadius: '62%',
                            innerRadius: '38%',
                            backgroundColor: trackColors[2],
                            borderWidth: 0
                        }]
                    },
              
                    yAxis: {
                        min: 0,
                        max: 100,
                        lineWidth: 0,
                        tickPositions: []
                    },
              
                    plotOptions: {
                        solidgauge: {
                            dataLabels: {
                                enabled: false
                            },
                            linecap: 'round',
                            stickyTracking: false,
                            rounded: true
                        }
                    },
              
                    series: [{
                        name: 'Conversion',
                        data: [{
                            color: Highcharts.getOptions().colors[0],
                            radius: '112%',
                            innerRadius: '88%',
                            y: 80
                        }],
                        custom: {
                            icon: 'filter',
                            iconColor: '#303030'
                        }
                    }, {
                        name: 'Engagement',
                        data: [{
                            color: Highcharts.getOptions().colors[1],
                            radius: '87%',
                            innerRadius: '63%',
                            y: 65
                        }],
                        custom: {
                            icon: 'comments-o',
                            iconColor: '#ffffff'
                        }
                    }, {
                        name: 'Feedback',
                        data: [{
                            color: Highcharts.getOptions().colors[2],
                            radius: '62%',
                            innerRadius: '38%',
                            y: 50
                        }],
                        custom: {
                            icon: 'commenting-o',
                            iconColor: '#303030'
                        }
                    }]
                    }
                  );
          } else if(this.chartType == 'clock gauge')
                  {
                    for (let j = 0; j < res.records.length; j++)
                    {
                      this.ids.push(res.records[j].ID);
                      this.names.push(Number(res.records[j].NAME));
                    }
                        
                        
        
                    this.newChartObject.push(
                      {
                        chart: {
                          type: 'gauge',
                          plotBackgroundColor: null,
                          plotBackgroundImage: null,
                          plotBorderWidth: 0,
                          plotShadow: false,
                          height: '80%',
                          identifier:'clock gauge'
                      },
                
                      credits: {
                          enabled: false
                      },
                
                      title: {
                          text: 'The Highcharts clock'
                      },
                
                      pane: {
                        background: [{
                          // default background
                        }, {
                          // reflex for supported browsers
                          backgroundColor: {
                            radialGradient: {
                              cx: 0.5,
                              cy: -0.4,
                              r: 1.9
                            },
                            stops: [
                              [0.5, 'rgba(255, 255, 255, 0.2)'],
                              [0.5, 'rgba(200, 200, 200, 0.2)']
                            ]
                          }
                        }]
                      },
                
                      yAxis: {
                          labels: {
                              distance: -23,
                              style: {
                                  fontSize: '18px'
                              }
                          },
                          min: 0,
                          max: 12,
                          lineWidth: 0,
                          showFirstLabel: false,
                
                          minorTickInterval: 'auto',
                          minorTickWidth: 3,
                          minorTickLength: 5,
                          minorTickPosition: 'inside',
                          minorGridLineWidth: 0,
                          minorTickColor: '#666',
                
                          tickInterval: 1,
                          tickWidth: 4,
                          tickPosition: 'inside',
                          tickLength: 10,
                          tickColor: '#666',
                          title: {
                              // text: 'Powered by<br/>Highcharts',
                              style: {
                                  color: '#BBB',
                                  fontWeight: 'normal',
                                  fontSize: '10px',
                                  lineHeight: '10px'
                              },
                              y: 10
                          }
                      },
                
                      tooltip: {
                          format: '{series.chart.tooltipText}'
                      },
                
                      series: [{
                          data: [{
                              id: 'hour',
                              y: now.hours,
                              dial: {
                                  radius: '60%',
                                  baseWidth: 4,
                                  baseLength: '95%',
                                  rearLength: 0
                              }
                          }, {
                              id: 'minute',
                              y: now.minutes,
                              dial: {
                                  baseLength: '95%',
                                  rearLength: 0
                              }
                          }, {
                              id: 'second',
                              y: now.seconds,
                              dial: {
                                  radius: '100%',
                                  baseWidth: 1,
                                  rearLength: '20%'
                              }
                          }],
                          animation: false,
                          dataLabels: {
                              enabled: false
                          }
                      }]
                      }
                    );
          } else {
           }
           if(this.newChartObject.length > 0)
           {
              this.itemsData.push
              ({
                formData: this.newChartObject,
                id: this.generateSerial(),
                mode: "",
                type: "Chart",
                value: this.informationservice.getAgGidSelectedNode(),
                number: res.chartType,
              });
            }


           this.newChartObject = []
           this.stockChartObject=[]
           this.ids = [];
           this.names = [];
           this.chartType = null;
         });
     }
   }
 
 
 
   onRunCellButtonClickKpi(e: any) {
     let info = {};
     let info1 = {};
     if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {
     } else {
 
         this.http.post<any>(GlobalConstants.decodeKpiQuery + this.informationservice.getAgGidSelectedNode(), { headers: GlobalConstants.headers }).subscribe(
           (res: any) => {
 
             this.http.post<any>(GlobalConstants.getKpiQueryData + this.informationservice.getAgGidSelectedNode(), { headers: GlobalConstants.headers }).subscribe(
               (res1: any) => {
                 info = {
                   formData: {
                     Headers: res,
                     Records: res1
                   },
                   id: this.generateSerial(),
                   mode: "",
                   type: "Kpi",
 
                   value: this.informationservice.getAgGidSelectedNode()
                 }
                 this.itemsData.push(info);
               });
           });
 
     }
 
   }
 
 
   openChart(data: any) {
     this.http.post<any>(GlobalConstants.selectChartRelatedToKpi + data.value, { headers: GlobalConstants.headers }).subscribe(
       (res: any) => {
         this.chartData = res
 
         setTimeout(() => {
 
           const dialogConfig = new MatDialogConfig();
           dialogConfig.width = '500px';
           dialogConfig.height = '500px';
 
           const dialogRef = this.dialog.open(ChartFromKpiBuilderComponent, {
             data: this.chartData,
             width: '50%',
             height: '60%',
           });
         }, 1000);
       });
 
   }
 
   openGrid(data: any) {
     this.http.post<any>(GlobalConstants.selectGridRelatedToKpi + data.value, { headers: GlobalConstants.headers }).subscribe(
       (res: any) => {
         this.gridData = {
           data: res,
           flag: 1
         }
         setTimeout(() => {
           const dialogConfig = new MatDialogConfig();
           dialogConfig.width = '500px';
           dialogConfig.height = '600px';
 
           const dialogRef = this.dialog.open(GridBuilderPreviewComponent, {
             data: this.gridData,
             width: '50%',
             height: '60%',
           });
         }, 1000);
       });
   }
 
   editorChange(i: number, serial: string) {
     let editor = CKEDITOR.instances['editor' + (i + 1)];
     for (let i = 0; i < this.itemsData.length; i++) {
       if (this.itemsData[i].id == serial) {
 
         if (editor.mode == 'source') {
           this.itemsData[i].mode = 'source';
           this.itemsData[i].value = editor.getData();
         } else if (editor.mode == 'wysiwyg') {
           this.itemsData[i].mode = 'wysiwyg';
         }
       }
     }
   }
 
   generateSerial() {
 
     'use strict';
 
     var chars = '1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz',
 
       serialLength = 10,
 
       randomSerial = "",
 
       i,
 
       randomNumber;
 
     for (i = 0; i < serialLength; i = i + 1) {
 
       randomNumber = Math.floor(Math.random() * chars.length);
 
       randomSerial += chars.substring(randomNumber, randomNumber + 1);
 
     }
 
     return randomSerial;
   }
 
   submitScreenForm() {
     if (this.screenForm.get('screenIsSuspended').value) {
       this.status = 'Suspended';
     } else {
       this.status = 'Active';
     };
 
     if (this.screenForm.get('nextAction').value) {
       this.status = 'nextAction';
     } else {
       this.status = 'false';
     };
 
     for (let i = 0; i < this.itemsData.length; i++) {
       this.screenData.push({
         index: i,
         id: this.itemsData[i].id,
         type: this.itemsData[i].type,
         value: this.itemsData[i].value,
         mode: this.itemsData[i].mode,
         formData: this.itemsData[i].formData
 
       });
     }
     if (this.screenData.length == 0) {
       this.showSnackBar('The Screen Is Empty');
     } else {
 
       let chartData = {
         screenName: this.screenForm.get('screenName').value,
         isTemplate: this.screenForm.get('screenIsTemplate').value,
         application: this.screenForm.get('screenApplication').value,
         parentMenu: this.screenForm.get('screenParentMenu').value,
         isSuspended: this.status,
         nextAction: this.status,
         parentMenuId: this.screenForm.get('screenParentMenuId').value,
         applicationId: this.screenForm.get('screenApplicationId').value,
         userId: this.informationservice.getLogeduserId(),
         data: JSON.stringify(this.screenData)
       }
       this.http.post<any>(GlobalConstants.addScreen, chartData, { headers: GlobalConstants.headers }).subscribe(
         (res: any) => {
           this.showSnackBar('Screen Saved');
           this.commonFunctions.navigateToPage("/dsp/augmentedConfigScratch");
         })
     };
 
 
   }
 
   removeScreenElement(serial: string) {
     for (let i = 0; i < this.itemsData.length; i++) {
       if (this.itemsData[i].id == serial) {
         this.itemsData.splice(i, 1);
       }
     }
   }
 
   showApplicationList() {
 
     const dialogConfig = new MatDialogConfig();
     dialogConfig.width = '700px';
     dialogConfig.height = '700px';
 
     const dialogRef = this.dialog.open(ApplicationListComponent, {
       // data: info,
       width: '40%',
       height: '70%',
     });
 
 
     dialogRef.afterClosed().subscribe(result => {
 
       if (this.informationservice.getAgGidSelectedNode() != '') {
         let json = JSON.parse(this.informationservice.getAgGidSelectedNode());
         this.screenForm.controls['screenApplicationId'].setValue(json[0].code);
 
         this.screenForm.controls['screenApplication'].setValue(json[0].application);
 
         this.screenForm.controls['screenParentMenuId'].setValue('');
         this.screenForm.controls['screenParentMenu'].setValue('');
       }
     });
 
   }
 
 
   showParentMenu() {
     if (this.screenForm.controls['screenApplicationId'].value != '' && this.screenForm.controls['screenApplication'].value != '') {
       const dialogConfig = new MatDialogConfig();
       dialogConfig.width = '700px';
       dialogConfig.height = '700px';
 
       const dialogRef = this.dialog.open(ParentMenuListComponent, {
         data: this.screenForm.controls['screenApplicationId'].value,
         width: '40%',
         height: '70%',
       });
 
 
       dialogRef.afterClosed().subscribe(result => {
 
         if (this.informationservice.getAgGidSelectedNode() != '') {
 
           let jsonMenu = JSON.parse(this.informationservice.getAgGidSelectedNode());
           this.screenForm.controls['screenParentMenuId'].setValue(jsonMenu[0].menu_code);
 
           this.screenForm.controls['screenParentMenu'].setValue(jsonMenu[0].menu_name);
 
         }
       });
     } else {
       this.showSnackBar('Missing: Application');
     }
   }
 
 
   showSnackBar(message: string) {
     this.snackBar.open(message, 'Close', {
       duration: 5000,
       verticalPosition: 'top',
       horizontalPosition: 'center',
       panelClass: ['my-snackbar'],
     });
   }
 
   updateScreenForm() {
     this.screenData = [];
     for (let i = 0; i < this.itemsData.length; i++) {
 
       this.screenData.push({
         index: i,
         id: this.itemsData[i].id,
         type: this.itemsData[i].type,
         value: this.itemsData[i].value,
         mode: this.itemsData[i].mode,
         formData: this.itemsData[i].formData
       });
     }
     if (this.screenData.length == 0) {
       this.showSnackBar('The Screen Is Empty');
     } else {
 
       let allData = {
         screenId: this.screenId,
         screenName: this.screenForm.get('screenName').value,
         isTemplate: this.screenForm.get('screenIsTemplate').value,
         application: this.screenForm.get('screenApplication').value,
         parentMenu: this.screenForm.get('screenParentMenu').value,
         isSuspended: this.screenForm.get('screenIsSuspended').value,
         nextAction: this.screenForm.get('nextAction').value,
         parentMenuId: this.screenForm.get('screenParentMenuId').value,
         applicationId: this.screenForm.get('screenApplicationId').value,
         userId: this.informationservice.getLogeduserId(),
         data: JSON.stringify(this.screenData)
       }
       this.http.post<any>(GlobalConstants.updateScreen, allData, { headers: GlobalConstants.headers }).subscribe(
         (res: any) => {
           this.showSnackBar('Screen Updated');
           this.commonFunctions.navigateToPage("/dsp/augmentedConfigScratch");
         })
     };
 
 
   }
 
   showPreview() {
     const dialogConfig = new MatDialogConfig();
     dialogConfig.width = '700px';
     dialogConfig.height = '700px';
 
     const dialogRef = this.dialog.open(PreviewScreenComponent, {
       data: this.itemsData,
       width: '95%',
       height: '95%',
     });
 
   }
 
 
 
 }
 
 