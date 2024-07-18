import { ChangeDetectorRef, Component,Input, ComponentFactoryResolver, ElementRef, Inject, OnInit, ViewChild, ViewContainerRef, Renderer2, Type } from '@angular/core';

import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DatacrowdService } from '../Services/datacrowd.service';
import { DataService } from '../Services/data.service';
import { DatePickerComponent } from '../component/datepicker/datepicker.component';
import { VAgGridComponent } from '../component/v-ag-grid/v-ag-grid.component';

declare var TL: any;
declare var markerId: any;
declare var dataFilter: any;
declare var filter: any;


@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css']
})
export class TimelineComponent implements OnInit {
  @ViewChild(DatePickerComponent) datePicker: DatePickerComponent;
  @ViewChild('popupContent1', { static: false }) popupContent: ElementRef;
  isHidden = true;
  form: FormGroup;
  simulationTypesArray:any[]=[];
  selectedField: string = 'DeviceId';
  @Input() value: Number ;
  contextMenuReady: number = 0;


  constructor(private elementRef: ElementRef,
    private datacrowdService: DatacrowdService,
    private dataservice: DataService,
    public dialog: MatDialog,
    private renderer: Renderer2,
    private viewContainerRef: ViewContainerRef,
    private componentFactoryResolver: ComponentFactoryResolver,
    private modalService: NgbModal,
    private formBuilder: FormBuilder
  ) { }
  public generatData: any;
  public test: number = 0;
  public fromButton: number = 0;
  public clickedButtonId: any;
  public doubleClickId: any;
  public datePickerObj: any;
  public flagDisplay: number = 0;
  public markerids:any;
  public previousMarkerids:any;
  public fromBack:number =0;
  public filterDataSimulationType:any;
  public filterDataSimulationName:any;
  public filterDataDeviceId:any;
  public offlineOnlineflag:number = 0;
  public counterReportContainer:number = 0;
  public countertimelineReportContainer:number = 0;





  GridType1: string = 'btn-54';

  modalRef: any;
  Devices: any;
 

  rowData = [{ Device_id: "" }];
  columnDefs = [
    {
      headerName: "Device_id",
      field: "Device_id",
      sortable: true,
      filter: "agSetColumnFilter",
      width: 400,
      wrapText: true,
      autoHeight: true,
      rowGroup: true,
      hide: true,
      checkboxSelection: true,
      cellStyle: { color: "blue" },
      cellRenderer: 'btnCellRenderer',
    },
  

  ];
  @Input() timelineIndex: Number ;

  headlineValue: string = '';
  async ngOnInit() {
     
    this.form = this.formBuilder.group({
      DeviceId: '',
      Simulation_name: '',
      Type: ['']
    });

    this.datacrowdService.getSimulationTypes().then((response: any) => {
      this.simulationTypesArray = response;
    });
    this.generateTimeline();

    document.addEventListener('dblclick', this.onMarkerDoubleClick.bind(this));
    document.addEventListener('contextmenu', this.onMarkerMouseDown.bind(this));
    document.addEventListener('click', this.timestamp.bind(this));

    
  }
  ngOnChanges(changes: any) {

if(this.dataservice.getTimelineFromMap()==1){
 this.fromButton =5;
this.generateTimeline();
}
}


  async generateTimeline() {
    const lightColors = ["#FFCCE5", "#FFDEAD", "#FFA07A", "#FFD700", "#98FB98"];

    function getRandomColor() {
      return lightColors[Math.floor(Math.random() * lightColors.length)];
    }
    let timelineData;
    timelineData = {
      events: await this.generateTimelineData(),

    };
    let countEvent = 0;
    let initialZoom = 0;
    timelineData.events.forEach((event: any) => {
      countEvent++;
      const randomColor = getRandomColor();
      const eventClassName = event.classname;
      const customCSS = document.createElement('style');
      customCSS.innerHTML = `.${eventClassName} .tl-timemarker-content { background-color: ${randomColor}; }`;
      document.head.appendChild(customCSS);
    });
    if (countEvent < 6 ) {
      initialZoom = 0.05
    }else if(countEvent < 12){
      initialZoom = 0.5

    } else {
      initialZoom = 1
    }
    if (this.fromButton == 1) {
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 300,
        trackResize: true,
        timenav_position: "top",
        slide_padding_lr: 100,
        scale_factor: 0.5
      };
      new TL.Timeline('timeline-container', timelineData, options);

    } else if (this.fromButton == 2) {
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 250,
        trackResize: true,
        timenav_position: "top",
        slide_padding_lr: 100,
        scale_factor: initialZoom


      };
      new TL.Timeline('timeline-container', timelineData, options);
    }else if(this.fromButton == 4){
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 250,
        trackResize: true,
        timenav_position: "top",
        slide_padding_lr: 100,
        scale_factor: 0.9


      };
      new TL.Timeline('timeline-container', timelineData, options);
    }else if(this.fromButton == 5){
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 250,
        trackResize: true,
        timenav_position: "top",
        slide_padding_lr: 100,
        scale_factor: initialZoom


      };
      new TL.Timeline('timeline-container', timelineData, options);
    }else if(this.fromButton == 6){
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 250,
        trackResize: true,
        timenav_position: "top",
        slide_padding_lr: 100,
        scale_factor: 10
      };
      new TL.Timeline('timeline-container', timelineData, options);
    } else if (this.fromButton == 7) {
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 250,
        trackResize: true,
        timenav_position: "top",
        slide_padding_lr: 100,
        scale_factor: initialZoom


      };
      new TL.Timeline('timeline-container', timelineData, options);
    }else {
      const options = {
        start_at_end: false,
        hash_bookmark: true,
        font: "Raleway, sans-serif",
        lang: "en",
        timenav_height_min: 250,
        trackResize: true,
        timenav_position: "top",
        scale_factor: 0.5
      };
      new TL.Timeline('timeline-container', timelineData, options);
    } setTimeout(() => {
      const eventContainers = document.querySelectorAll('.tl-timemarker-content-container') as NodeListOf<HTMLElement>;
      let count = 0;
      eventContainers.forEach(container => {
        const contentElement = container.querySelector('.tl-timemarker-content') as HTMLElement;
        let id = (contentElement.lastChild.lastChild.lastChild as HTMLElement).id;
         let simulationId = this.dataservice.getTimelineSimulID();
         if(parseInt(id)==simulationId){
            $('#'+id).trigger('click');
              }
        if (contentElement) {
          if (contentElement.querySelector('.node_transparent')) {
            contentElement.style.backgroundColor = 'none';
            contentElement.style.color = 'blue'
            container.style.border = 'none';
            container.parentElement.style.border = 'none';

            const timespanElement = container.previousElementSibling as HTMLElement;
            if (timespanElement && timespanElement.classList.contains('tl-timemarker-timespan')) {
              timespanElement.classList.add('custom-border')
            }
          }else if (contentElement.querySelector('.node_timeOfCase')) {

            const timespanElement = container.previousElementSibling as HTMLElement;
            if (timespanElement && timespanElement.classList.contains('tl-timemarker-timespan')) {
              timespanElement.classList.add('timeOfCase')
            }
          }else if (contentElement.querySelector('.node_startEndCase')) {

            contentElement.style.backgroundColor = 'none';
            contentElement.style.color = 'blue'
            container.style.border = 'none';
            container.parentElement.style.border = 'none';

            const timespanElement = container.previousElementSibling as HTMLElement;
            if (timespanElement && timespanElement.classList.contains('tl-timemarker-timespan')) {
              timespanElement.classList.add('startEndCase')
            }
          }else if (contentElement.querySelector('.node_openCase')) {

            const timespanElement = container.previousElementSibling as HTMLElement;
            if (timespanElement && timespanElement.classList.contains('tl-timemarker-timespan')) {
              timespanElement.classList.add('openCase')
            }
          }else if (contentElement.querySelector('.node_sysDate')) {

            const timespanElement = container.previousElementSibling as HTMLElement;
            if (timespanElement && timespanElement.classList.contains('tl-timemarker-timespan')) {
              timespanElement.classList.add('sysDate')
            }
          }else if(contentElement.querySelector('.node_simulationData')){
            contentElement.style.backgroundColor = 'none';
            contentElement.style.color = 'blue'
            container.style.border = 'none';
            container.parentElement.style.border = 'none';
          } else {
            const newColor = this.getRandomColor(count);
            if (count == 9) {
              count = 0;
            } else {
              count++;
            }

            contentElement.style.backgroundColor = newColor;
            contentElement.style.borderColor = newColor;
            contentElement.style.borderWidth = '1px';
            contentElement.style.borderStyle = 'solid';
            const timespanElement = container.previousElementSibling as HTMLElement;
            if (timespanElement && timespanElement.classList.contains('tl-timemarker-timespan')) {
              timespanElement.style.borderColor = newColor;
              timespanElement.style.borderWidth = '2px';
              timespanElement.style.borderStyle = 'solid';

            }
          }
        }
      });
      this.addEventListeners()
    }, 0);


  }
  getRandomColor(count: any) {
    const lightColors = [
      "#e377c2",
      "#1f77b4",
      "#ff7f0e",
      "#d62728",
      "#17becf",
      "#2ca02c",
      "#bcbd22",
      "#8c564b",
      "#9467bd",
      "#ffbb78",
    ];
    return lightColors[count];
  }
  clear() {
    const emptyEvent = {
      start_date: { year: 0, month: 0, day: 0 },
    };
    this.createTimeline([emptyEvent])
  }

  createTimeline(generatedData: any) {
    const timelineData = {
      events: generatedData
    };
    const options = {
      start_at_end: false,
      start_zoom_adjust: 1,
      hash_bookmark: true,
      font: "Raleway, sans-serif",
      lang: "en",
      timenav_height: 0,
      timenav_position: "top",
    };
    new TL.Timeline('timeline-container', timelineData, options);
  }
  async timestamp() {
    if(filter == 1){
      
        var formContainer = document.getElementById('formContainer');
        if (formContainer.classList.contains('hidden')) {
          formContainer.classList.remove('hidden');
      }else  if (formContainer && !formContainer.contains(event.target as Node)) {
            filter = 0;
             formContainer.classList.add('hidden');
     }
    }
    if (dataFilter == 1) {
      const dialogRef = this.dialog.open(DatePickerComponent);
      dialogRef.afterClosed().subscribe(result => {
        this.fromButton = 3;
        this.generateTimeline();
      });
    }
    dataFilter = null;
  }

  addEventListeners() {

    const buttons = document.querySelectorAll('.custom-button');
    const marker: any = document.querySelectorAll('.tl-timemarker-content');
    buttons.forEach(button => {
      const htmlButton = button as HTMLButtonElement;
      htmlButton.style.backgroundColor = 'lightgray';
      htmlButton.style.color = '#ffffff';
      htmlButton.style.padding = '8px 16px';
      htmlButton.style.border = 'none';
      htmlButton.style.cursor = 'pointer';
      htmlButton.style.transition = 'background-color 0.3s, color 0.3s';
      htmlButton.addEventListener('click', (event) => {
        this.buttonClick(event);
      });


    });
  }

  async buttonClick(event: MouseEvent) {
    event.preventDefault();
    if (event.button === 2) {
      return;
    }
    const clickedButton = event.target as HTMLButtonElement;
    this.clickedButtonId = clickedButton.id;
    if (this.clickedButtonId.startsWith("drillDown-")) {
      const numericPart = this.clickedButtonId.substring("drilldown-".length);
    } else {
      if (this.test == 0) {
        this.fromButton = 1;
        this.generateTimeline();
      } else if (this.test == 1) {
        this.fromButton = 2;
        this.generateTimeline();
      } else {
        $('#Map').css('display', '');
        $('#Graph').css('display', 'none');
        this.dataservice.setTimelineSimulID(this.clickedButtonId);
        $('#displayTimelineSimul').click();
      }

    }
  }

  addDynamicHtml() {
    if(this.countertimelineReportContainer !=0){
      this.removeReportContainer();
      this.countertimelineReportContainer =0;
    }
    const testDiv = this.elementRef.nativeElement.querySelector('#report-container');
    if (testDiv) {
      const iframe = document.createElement('iframe');
      iframe.src = '/cybercrowd/angular/assets/report_' + this.markerids.data.text.id + '.html';
      iframe.width = '100%';
      iframe.height = '1800';
      testDiv.appendChild(iframe);
      this.countertimelineReportContainer++;

    } else {
      console.error('Div with id Timeline not found.');
    }


  }

  addDynamicReportHtml() {
    
    const testDiv = this.elementRef.nativeElement.querySelector('#report-container');
    if (testDiv) {
      const iframe = document.createElement('iframe');
      iframe.src = '/cybercrowd/angular/assets/report_analytics_0.html';
      iframe.width = '100%';
      iframe.height = '2000';
      testDiv.appendChild(iframe);

    } else {
      console.error('Div with id report-container not found.');
    }


  }

  removeReportContainer() {
    const reportContainer = this.elementRef.nativeElement.querySelector('#report-container');

    if (reportContainer) {
      reportContainer.innerHTML = '';
    } else {
      console.error('Div with id report-container not found.');
    }
  }

  async generateTimelineData() {
    let events: any[] = [];
    if (this.fromButton == 1) {
      if(this.fromBack == 1){
        this.markerids = this.previousMarkerids;
      }
      this.contextMenuReady = 1;
      this.test = 1;
      let res = await this.datacrowdService.finalDisplayTimeline('month', this.markerids.data.text.id);
      events = res;
    } else if (this.fromButton == 2) {
      this.test = 2;
      if(this.fromBack == 1){
        this.markerids = this.previousMarkerids;
      }
      let res = await this.datacrowdService.finalDisplayTimeline('day', this.markerids.data.text.id);
      events = res;
    } else if (this.fromButton == 3) {
      const objDate = this.dataservice.getData();
      let res = await this.datacrowdService.getFilteredData(objDate);

      events = res;
    }  else if (this.fromButton == 4) {
      const objDate = this.dataservice.getData();

      const obj= {"endOperation": objDate.selectedStartDate, "SimulationId": this.markerids.data.text.id};
      let res = await this.datacrowdService.getCommonDevicesTimeline(obj);
      console.log('res>>>',res)

      //const decodedRes = this.decodeByteText(res);
     // console.log('decodedRes----->',decodedRes);

      events = res;

    } else if(this.fromButton == 5){
       let simulationId = this.dataservice.getTimelineSimulID();
       let res = await this.datacrowdService.displayTimelineFromMap(simulationId);
       console.log('res>>>',res)
        events = res;
      }else if(this.fromButton == 7){
        let obj={"simulationName":this.filterDataSimulationName,"deviceId":this.filterDataDeviceId,"simulationType":this.filterDataSimulationType}
     
         let res = await this.datacrowdService.getfilterDataTimeline(obj);
         events = res;
      }else{
      let res = await this.datacrowdService.finalDisplayTimeline('year', "null");
      events = res;
    }
    return events;
  }
  // decodeByteText (events: any[]) {
  //   return events.map((event:any) => {
  //     if ('byteText' in event.text && event.text.byteText!== '') {
  //       const decodedText = decodeURIComponent(event.text.byteText);
  //       return {
  //        ...event,
  //         text: {
  //          ...event.text,
  //           text: decodedText,
  //           byteText: undefined // Optional: remove byteText property after decoding
  //         }
  //       };
  //     }
  //     return event;
  //   });
  // }

  onMarkerDoubleClick() {

    if (markerId != null) {
      if (this.test == 0) {
        this.markerids=markerId;
        this.fromBack = 0;
        this.fromButton = 1;
        this.generateTimeline();
      } else if(this.test == 1){
        this.previousMarkerids=this.markerids;
        this.markerids=markerId;
        this.fromButton = 2;
        this.fromBack=0;
        this.generateTimeline();
      }else if(this.test == 2){
        this.previousMarkerids=this.markerids;
        this.markerids=markerId;
        this.dataservice.setDatePickerFromTimeline(1);
        const dialogRef = this.dialog.open(DatePickerComponent);
        dialogRef.afterClosed().subscribe(result => {
           this.fromButton = 4;
           this.generateTimeline();
        })
      }else{
        }
      markerId = null;

    }
  }
  onMarkerMouseDown(event: MouseEvent) {
    event.preventDefault();
    this.showContextMenu(event.clientX, event.clientY);

  }
  showContextMenu(x: number, y: number) {
    if (markerId) {
      this.contextMenuReady = markerId.data.text.contextMenu
    }

    if (markerId.data.text.contextMenu == 0) {
      const contextMenu = document.createElement('div');
      contextMenu.classList.add('custom-context-menu');
      contextMenu.innerHTML = `
      <ul>
        <li>Map</li>
      </ul>
    `;
      contextMenu.style.position = 'absolute';
      contextMenu.style.left = `${x}px`;
      contextMenu.style.top = `${y}px`;
      document.body.appendChild(contextMenu);

      const closeContextMenu = () => {
        document.body.removeChild(contextMenu);
        window.removeEventListener('click', closeContextMenu);
      };
      window.addEventListener('click', closeContextMenu);
      this.markerids=markerId;
      contextMenu.querySelector('li:nth-child(1)').addEventListener('click', () => {
        this.flagDisplay = 3;
        this.displaySimul();
      });

      markerId = null;
    } else if (markerId.data.text.contextMenu == 1) {
      const contextMenu = document.createElement('div');
      contextMenu.classList.add('custom-context-menu');
      contextMenu.innerHTML = `
      <ul>
        <li>Map</li>
        <li>Back</li>
      </ul>
    `;
    this.markerids=markerId;
      contextMenu.style.position = 'fixed';
      contextMenu.style.left = `${x}px`;
      contextMenu.style.top = `${y}px`;
      document.body.appendChild(contextMenu);

      const closeContextMenu = () => {
        document.body.removeChild(contextMenu);
        window.removeEventListener('click', closeContextMenu);
      };
      window.addEventListener('click', closeContextMenu);

      contextMenu.querySelector('li:nth-child(1)').addEventListener('click', () => {
        this.flagDisplay = 1;
        this.displaySimul();
      });
  
      contextMenu.querySelector('li:nth-child(2)').addEventListener('click', () => {
        this.fromButton = 0;
        this.test = 0;
        this.generateTimeline();
      });
      markerId=null;

    }else if(markerId.data.text.contextMenu == 4){
      const contextMenu = document.createElement('div');
      contextMenu.classList.add('custom-context-menu');
      contextMenu.innerHTML = `
      <ul>
        <li>Map</li>
        <li>Back</li>
        <li>Report</li>
      </ul>
    `;
    this.markerids=markerId;
      contextMenu.style.position = 'fixed';
      contextMenu.style.left = `${x}px`;
      contextMenu.style.top = `${y}px`;
      document.body.appendChild(contextMenu);

      const closeContextMenu = () => {
        document.body.removeChild(contextMenu);
        window.removeEventListener('click', closeContextMenu);
      };
      window.addEventListener('click', closeContextMenu);

      contextMenu.querySelector('li:nth-child(1)').addEventListener('click', () => {
         this.flagDisplay = 4;
         this.displayCoTraveler(this.markerids.data.text.simulationId);
      });
  
      contextMenu.querySelector('li:nth-child(2)').addEventListener('click', () => {
        this.fromButton = 2;
        this.test = 1;
        this.fromBack = 1;
        this.removeReportContainer();
        this.generateTimeline();
      });
      markerId=null;
      contextMenu.querySelector('li:nth-child(3)').addEventListener('click', async () => {
        console.log('id >> ',this.markerids.data);
       await this.datacrowdService.getReportHtml(this.markerids.data.text.id,this.markerids.data.text.simulationId,this.offlineOnlineflag);
        this.addDynamicReportHtml();
      });
    } else {
      this.flagDisplay = 2
      if (markerId != null) {
        
        const contextMenu = document.createElement('div');
        contextMenu.classList.add('custom-context-menu');
          contextMenu.innerHTML = `
        <ul>
            <li>Map</li>
            <li>Timeline</li>
            <li>Knowledge Graph</li>
            <li>Back</li>
        </ul>
    `;
          this.markerids= markerId;
          contextMenu.style.position = 'fixed';
          contextMenu.style.left = `${x}px`;
          contextMenu.style.top = `${y}px`;
          document.body.appendChild(contextMenu);
          const closeContextMenu = () => {
            document.body.removeChild(contextMenu);
            window.removeEventListener('click', closeContextMenu);
          };
          window.addEventListener('click', closeContextMenu);
          contextMenu.querySelector('li:nth-child(1)').addEventListener('click', () => {
            this.displaySimul();
          });
          contextMenu.querySelector('li:nth-child(2)').addEventListener('click', async () => {
            await this.datacrowdService.getDrillDownTimeline(this.markerids.data.text.id).then((response: any) => {
              let id = this.markerids.data.text.id;
              this.addDynamicHtml();
            });
          });
          contextMenu.querySelector('li:nth-child(3)').addEventListener('click', () => {
            $('#Map').css('display','none');
            $('#Timeline').css('display','none');
            $('#Graph').css('display','');
            let report_Id=this.markerids.data.text.id;
            this.dataservice.setFromTimeline(1);
            (window.parent.parent.parent[7] as any).A_ISGraph = true;
            (window.parent.parent.parent[7] as any).executeGraphFromTimeline(report_Id);
            (window.parent.parent.parent[7] as any).refreshSimulation();

          });
          contextMenu.querySelector('li:nth-child(4)').addEventListener('click', () => {
            this.test = 0;
            this.fromButton = 1;
            this.fromBack = 1;
            this.generateTimeline();
          });
          markerId=null;

        
      } else {

      }
    }
  }
  displaySimul() {
    $('#Map').css('display', '');
    $('#Graph').css('display', 'none');
    if (this.flagDisplay == 1) {
      this.datacrowdService.getDailySimulationId(this.markerids.data.text.id).then(Response => {
        const stringSeparated = Response.join(',');
        this.dataservice.setTimelineSimulID(stringSeparated);
        this.markerids = null;
        $('#displayTimelineSimul').click();
      });

    } else if (this.flagDisplay == 2) {
      this.dataservice.setTimelineSimulID(this.markerids.data.text.id);
      this.markerids = null;
      $('#displayTimelineSimul').click();
    } else if (this.flagDisplay == 3) {
      this.datacrowdService.getMonthlySimulationId(this.markerids.data.text.id).then(Response => {
        const stringSeparated = Response.join(',');
        this.dataservice.setTimelineSimulID(stringSeparated);
        this.markerids = null;
        $('#displayTimelineSimul').click();
      });
    } else if (this.flagDisplay == 4) {
      this.markerids.data.text.simulationId=179752;
      this.dataservice.setTimelineSimulID(this.markerids.data.text.simulationId);
      this.markerids = null;
      $('#displayTimelineSimul').click();
    } else { 

    }
  }
  displayCoTraveler(simulId:any) {
    this.openPopup2(simulId);
    
  }
  async openPopup2(simulId:any) {
 await this.datacrowdService.getDeviceCommonLocationHIts(simulId).then((res:any)=>{
  const modifiedData = res.map((deviceId: string) => {
    return { Device_id: deviceId };
  });
  this.rowData=modifiedData;
 });

      const componentfactory =
        this.componentFactoryResolver.resolveComponentFactory(VAgGridComponent );
      const componentref:any =this.viewContainerRef.createComponent(componentfactory);
      const closeButton = document.createElement('button');
      const icon = document.createElement('i');
      icon.classList.add('fas', 'fa-times'); 
      closeButton.appendChild(icon);
      closeButton.style.position = 'absolute';
      closeButton.style.top = '0px'; 
      closeButton.style.right = '10px'; 
      closeButton.style.backgroundColor = '#ffffff'; 
      closeButton.style.border = 'none';
      closeButton.style.borderRadius = '50%'; 
      closeButton.style.padding = '5px'; 
      closeButton.style.cursor = 'pointer'; 
      closeButton.addEventListener('click', () => {
        componentref.destroy();
        let x:any=localStorage.getItem("multiselection");
        if(x.toString()=="[\"\"]"){
        }else{
          this.dataservice.setDeviceCommonLocationHits(x);
          localStorage.removeItem("multiselection")
          this.flagDisplay = 4;
          this.displaySimul();
          $('#displayTimelineCoTraveler').click();
        }
      });
      componentref.location.nativeElement.appendChild(closeButton);
      componentref.instance.rowData = this.rowData;
      componentref.instance.columnDefs = this.columnDefs;
      componentref.instance.headerHeight = 30;
       componentref.instance.selectdevices = true;
      componentref.instance.Title = "";
      componentref.instance.distinct = true;
      componentref.instance.rowGrouping = false;



      componentref.changeDetectorRef.detectChanges();
      const html1 = componentref.location.nativeElement;
      html1.id="modal-content1";

 
      $("#modal-content1").css("min-width", "246px");
      $("#modal-content1").css("right", "200px");
      $("#modal-content1").css("padding", "10px");
      $("#modal-content1").css("top", "50px");
      $("#modal-content1").css("border-radius", "12px");
      $("#modal-content1").css("position", "absolute");
      $("#modal-content1").css("border", "2px solid");
      $("#modal-content1").css("z-index", "99999999999");
      $("#modal-content1").css("background-color", "white");

      $("#modal-content1").draggable({
        axis: "both",
        cursor: "move"
      });
  }
  dateTtoDate(milliseconds: number): string {
    const date = new Date(milliseconds);
    const options: Intl.DateTimeFormatOptions = {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric',
      year: 'numeric',
    };
    const formattedDate = new Intl.DateTimeFormat('en-US', options).format(date);
    return formattedDate;
  }
  
  closemodalservice() {
    if(this.modalRef){
      this.modalRef.close();

    }
  }
  readValues() {
     this.filterDataDeviceId = this.form.get('DeviceId').value;
    this.filterDataSimulationName = this.form.get('Simulation_name').value;
     this.filterDataSimulationType = this.form.get('Type').value;
    this.fromButton = 7;
     this.generateTimeline();
}

onFieldSelectChange(event: Event): void {
   this.selectedField = (event.target as HTMLSelectElement).value;
}


}