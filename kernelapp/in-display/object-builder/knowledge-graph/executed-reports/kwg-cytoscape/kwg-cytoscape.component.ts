import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, ComponentFactoryResolver, ElementRef, Input, NgZone, ViewChild, ViewContainerRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GridOptions } from 'ag-grid-community';
import cytoscape, { NodeDataDefinition } from 'cytoscape';
import cyContextMenus from 'cytoscape-context-menus';
import dagre from 'cytoscape-dagre';
import fcose from 'cytoscape-fcose';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import $ from 'jquery';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { NextLayerFormComponent } from '../next-layer-form/next-layer-form.component';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-kwg-cytoscape',
  templateUrl: './kwg-cytoscape.component.html',
  styleUrl: './kwg-cytoscape.component.css'
})
export class KwgCytoscapeComponent {


  // constructor(private http: HttpClient,public commonFunctions: CommonFunctions,
  //   private eventEmitterService: EventEmitterService,
  //   @Inject(MAT_DIALOG_DATA) public data:any,
  //   private dialog: MatDialog, private snackBar: MatSnackBar,
  //   private _Activatedroute: ActivatedRoute,
  //   public datepipe: DatePipe,
  //   private route: Router,
  //   public informationservice: InformationService) { }


    @ViewChild('popup') popup: any;
    @ViewChild('createNodePopup') createpopup: any;
    @ViewChild('gridPopup') gridpopup: any;
    @ViewChild('searchNode', { static: true }) searchNode!: ElementRef<HTMLInputElement>; // Add this line
    rowData: any
    graphData: any;
    columnDefs: any;
    fromDatemillis: any;
    toDatemillis: any;
    callingNo: any;
    linkType: any;
    directIndirect: any;
    layer: any;
    storeName: any;
    itemName: any;
    masterId: any;
    reportID: any;
    dialog: any;
    map: any;
    firstNodeId:any = null;
    edgeLink:any = null;
    darkmode:any = false;
    inputValue: number | any;
    linkCountInput: number | any;
    legendItems = [
      { imgSrc: 'assets/Profile-PNG-File.png', title: 'Title 1' },
      { imgSrc: 'assets/Profile-PNG-File.png', title: 'Title 2' },
      { imgSrc: 'assets/Profile-PNG-File.png', title: 'Title 3' },
      { imgSrc: 'assets/Profile-PNG-File.png', title: 'Title 4' },
    ];
  
    @Input() graph!: any[]; // Input property to receive graph data from   parent component
    processedGraphData: any[] = []; // Initialize an empty array for graphData
    gridOptions: GridOptions = {}; // Initialize gridOptions
    filteredGraphData: any[] = [];
    searchText: string = '';
  
    generateRandomName() {
      const firstNames = ['John', 'Jane', 'Michael', 'David', 'Jennifer', 'Lisa', 'Sarah', 'Emily', 'Brian', 'Andrew', 'James', 'Linda', 'Matthew', 'Susan', 'Christopher', 'Karen'];
      const lastNames = ['Smith', 'Johnson', 'Williams', 'Brown', 'Jones', 'Miller', 'Davis', 'Garcia', 'Wilson', 'Moore', 'Taylor', 'Anderson', 'Thomas', 'Jackson', 'White', 'Harris'];
  
      const randomFirstName = firstNames[Math.floor(Math.random() * firstNames.length)];
      const randomLastName = lastNames[Math.floor(Math.random() * lastNames.length)];
  
      return `${randomFirstName} ${randomLastName}`;
    }
  
  
    constructor(
      // private popup: PopupComponent,
      private changeDetectorRef: ChangeDetectorRef,
      private ngZone: NgZone,
      private viewContainerRef : ViewContainerRef ,
      private componentFactoryResolver:ComponentFactoryResolver,
      private datePipe: DatePipe,
      private route: ActivatedRoute,
      private elementRef: ElementRef,
      private http: HttpClient,
      public informationservice: InformationService,
      private dialogNext: MatDialog,
      public dialogRef: MatDialogRef<KwgCytoscapeComponent>,
    )
      {}
    private cy: any;
  
    private spacingF = 6;
    popupContent: string = 'My App';
    devices :any;
  
    ipAddress: any  = GlobalConstants.ipAddress;
    ipAddressKYG: any  = GlobalConstants.ipAddressKYG;
    hiddenNodes!: Set<cytoscape.NodeSingular>;
  
  
  
    ngOnInit(): void {
      this.gridOptions = {
        columnDefs: [
          {
            checkboxSelection: true, // Enable checkboxes for row selection
            headerCheckboxSelection: true, // Enable header checkbox for selecting all rows
            headerCheckboxSelectionFilteredOnly: true, // Only select filtered rows when using header checkbox
            suppressSizeToFit: true,
            maxWidth: 50,
          },
          { headerName: 'Node Name', field: 'nodeName', sortable: true, filter: 'agTextColumnFilter', suppressSizeToFit: true  },
          { headerName: 'Edge Count', field: 'edgeCount', sortable: true, filter: 'agNumberColumnFilter', suppressSizeToFit: true  }
        ],
        quickFilterText: this.searchText,
        rowData: [],
        paginationPageSize: 10, // Set the number of rows per page to 10
        pagination: true, // Enable pagination
        domLayout: 'autoHeight'
      };
      //this.switchmode();
      console.log("infromation service>>>>>>>>>>>>",this.informationservice.getAgGidSelectedNode());
      //this.getGraph(this.informationservice.getAgGidSelectedNode());
      this.refreshsimualtionGraph();
    }
  
  
    generateRandomLebaneseMobileNumber() {
      const prefixes = ['03', '70', '71', '76', '78', '79'];
      const selectedPrefix = prefixes[Math.floor(Math.random() * prefixes.length)];
      const remainingDigits = Math.floor(Math.random() * 10000000).toString().padStart(7, '0');
      return selectedPrefix + remainingDigits;
    }
  //  async  getdatabaseType(){
  //   let response =this.httpClient.get("https://10.1.8.136:8088/api/getdatabaseType", {responseType: 'text'}).toPromise();
  //   return response;
  // }
  
  refreshsimualtionGraph(){
    //console.log("directindirect", (window.parent.parent.parent[7] as any).directIndirect);
  
      //this.devices = this.route.snapshot.paramMap.get("devices");
    //   this.devices =(window.parent.parent.parent[7] as any).A_allDevices;
    //   if(this.devices == "null")
    //   {
    //     this.devices = "";
    //   }
    //   //this.callingNo = this.route.snapshot.paramMap.get("callingNo");
    //   this.callingNo = (window.parent.parent.parent[7] as any).A_phone;
  
    //   if(this.callingNo == "null")
    //   {
    //     this.callingNo = "";
    //   }
    //  // this.linkType = this.route.snapshot.paramMap.get("linkType");
    //  this.linkType =(window.parent.parent.parent[7] as any).A_linkType;
    //   if(this.linkType == "null")
    //   {
    //     this.linkType = "";
    //   }
  
    //   //this.directIndirect = this.route.snapshot.paramMap.get("directIndirect");
    //   this.directIndirect =(window.parent.parent.parent[7] as any).A_directIndirect;
    //   if(this.directIndirect == "null")
    //   {
    //     this.directIndirect = "";
    //   }
    //   //this.layer = this.route.snapshot.paramMap.get("layer");
  
  
    //   this.layer =(window.parent.parent.parent[7] as any).A_layers;
    //   if(this.layer == "null")
    //   {
    //     this.layer = "";
    //   }
    //   // this.fromDatemillis = this.datePipe.transform(
    //   //   this.route.snapshot.paramMap.get("fromDatemillis"),
    //   //   "MM/dd/yyyy"
    //   // );
  
    //   this.fromDatemillis = this.datePipe.transform((window.parent.parent.parent[7] as any).A_DateTimeFrom);
    //   // this.toDatemillis = this.datePipe.transform(
    //   //   this.route.snapshot.paramMap.get("toDatemillis"),
    //   //   "MM/dd/yyyy"
    //   // );
    //   this.toDatemillis =this.datePipe.transform((window.parent.parent.parent[7] as any).A_DateTimeTo);
    //  // alert(this.devices);
    // //  this.storeName =(window.parent.parent.parent[7] as any).A_StoreName;
    // //  this.itemName =(window.parent.parent.parent[7] as any).A_ItemName;
    // //  this.masterId =(window.parent.parent.parent[7] as any).A_MasterId;
    this.reportID=this.informationservice.getAgGidSelectedNode();
    //this.reportID =(window.parent.parent.parent[7] as any).A_ReportId;
  
      this.getGraph(this.reportID)
  
      // const darkModeToggle :any = document.querySelector("#dark-mode-toggle");
  
      // darkModeToggle.addEventListener("click", () => {
      //   var cy:any  = document.getElementById("cy");
      //   // Toggle the "dark-mode" class on the body element
      //   console.log("cy",cy);
      //   cy.classList.toggle("dark-mode");
      //   // Update the text of the button
      //   if (cy.classList.contains("dark-mode")) {
      //     darkModeToggle.textContent = "🔆";
      //     this.darkmode = true;
      //     this.cy.edges().style('color', 'white');
      //     this.cy.nodes().style('color', 'white');
      //     $('.legend-container').css('background', '#35363a');
      //     $('.legend-container').css('color', 'white');
      //   } else {
      //     darkModeToggle.textContent = "🌙";
      //     this.darkmode = false;
  
      //     this.cy.edges().style('color', '#35363a');
      //     this.cy.nodes().style('color', '#35363a');
      //     $('.legend-container').css('background', 'white');
      //     $('.legend-container').css('color', '#35363a');
  
      //   }
      // });
  
      // const addNode :any = document.querySelector("#addNode");
  // addNode.addEventListener("click", () => {
    // this.nodeCreationForm();
  // });
      cytoscape.use(fcose);
      cytoscape.use(dagre);
      cytoscape.use(cyContextMenus);
      this.cy = cytoscape({
    container: document.getElementById('cy'),
    elements:  [{
      "data": {
          "size": "50",
          "id": "tempNode",
          "info": [],
          "addedOn": 1679919692469
      }
  }],
    style: [
      {
        selector: 'node',
        style: {
          'label': 'data(label)',
          'color': 'black',
          'font-size': '60px'
        }
      },
  
      {
        selector: 'edge',
        style: {
          'width': 8,
          'target-arrow-shape': 'triangle',
          'line-color': '#9dbaea',
          'target-arrow-color': '#9dbaea',
          'curve-style': 'bezier',
          'label': 'data(label  )'
        }
      }
    ],
    layout: {
      name: 'fcose',
      spacingFactor: this.spacingF
    } as any,
  });
  console.log('this.cy.elements')
  // console.log(this.generateGraph(10000))
      this.cy.contextMenus({
        menuItems: [
          {
            id: 'myMenuItem',
            content: 'Get next layer',
            // tooltipText: 'This is a tooltip',
            selector: 'node',
            onClickFunction: (event: any) => {
              console.log('Clicked node:', event.target.id());

              const dialogConfig = new MatDialogConfig();
              dialogConfig.width = '700px';
              dialogConfig.height = '700px';
          
              const dialogRef = this.dialogNext.open(NextLayerFormComponent, {
               //data: info,
                width: '50%',
                height: '50%',
              });

              dialogRef.afterClosed().subscribe(result => {
                setTimeout(() => {
                  console.log("RUN NEXT LAYER EVENT ID>>>>>>>>>>>>",event.target.id());
                  this.runNextLayer(event.target.id());
                }, 100);
               
               });


            },
            hasTrailingDivider: true,
          },
          {
            id: 'moreInfo',
            content: 'More info',
            // tooltipText: 'This is a tooltip',
            selector: 'node, edge',
            onClickFunction: async (event: any) => {
              console.log('Clicked node:', event.target._private.data);
              var target = event.target._private.data;
              this.graphData = event.target._private.data.info;
              // this.popup.click()
              this.popup.open()
              // this.popup.appendData(html1)
              // this.popupContent = html1.toString();
              // this.updatePopupContent(html1.toString())
            },
            hasTrailingDivider: true,
          },
          // {
          //   id: 'excel',
          //   title: 'export nodes',
          //   content: 'excel',
          //   selector: 'node, edge',
          //   onClickFunction: async (event: any) => {
          //     this.exportNodesToExcel()
          //   },
          //   hasTrailingDivider: true,
          // },
          {
            id: 'createlINK',
            title: 'generate edge',
            content: 'new link',
            selector: 'node',
            onClickFunction: async (event: any) => {
              this.edgeLink = true;
              this.firstNodeId = event.target._private.data.id;
  
            },
            hasTrailingDivider: true,
          }
        ],
      });
  
      $('.cy-context-menus-cxt-menu').children().addClass('ctxtMenuItems');
      $('.ctxtMenuItems').css('width','200px');
      $('.ctxtMenuItems').css('border','solid 1px lightgray');
      // this.cy.ready(() => {
      //   console.log(this.cy);
      // });
        // Customize node styles
        // this.cy.nodes().forEach((node: cytoscape.NodeSingular) => {
        //   node.style('background-color', '#' + Math.floor(Math.random() * 16777215).toString(16)); // Random color
        //   node.style('width', (Math.floor(Math.random() * 20) + 10) + 'px'); // Random width between 10px and 30px
        //   node.style('height', (Math.floor(Math.random() * 20) + 10) + 'px'); // Random height between 10px and 30px
        // });
  
        // // Customize edge styles
        // this.cy.edges().forEach((edge: any) => {
        //   edge.style('line-color', '#' + Math.floor(Math.random() * 16777215).toString(16)); // Random color
        //   edge.style('width', (Math.floor(Math.random() * 8) + 1) + 'px'); // Random width between 1px and 3px
        // });
        const originalOpacity = 1;
        this.cy.nodes().style('opacity', originalOpacity);
        var layers = 1;
        this.cy.on('click', 'node', (event: any) => {
          var node = event.target;
          if(this.edgeLink)
          {
            var nodeId = node._private.data.id
            this.createEdgeLink(nodeId);
          }
          else
          {
            this.cy.nodes().style('opacity', originalOpacity);
            this.cy.edges().style('opacity', originalOpacity);
            for (let i = 1; i < layers; i++) {
              node = node.neighborhood();
            }
  
            this.cy.elements().not(node).difference(node.neighborhood()).animate({
              style: { 'opacity': 0.1 },
              duration: 500
            });
          }
  
        });
        this.cy.on('click', (event: any) => {
          if (event.target === this.cy) {
            this.cy.nodes().style('opacity', originalOpacity);
            this.cy.edges().style('opacity', originalOpacity);
          }
        });
  
        this.cy.on('click', 'node', (event: any) => {
          const node = event.target;
  
          console.log(node);
          // Your code here
        });
  
  // console.log($('.cy-context-menus-cxt-menu'))
  this.hiddenNodes = new Set<cytoscape.NodeSingular>();
  // this.createFilter();
  // this.attachFilterEventListener();
  // this.filterEdgesByLabelNumber();
  }
  
  
  getStyle(nodeData: NodeDataDefinition): cytoscape.Css.Node {
    const nodeStyle: cytoscape.Css.Node = {
      height: nodeData['type'].height,
      width: nodeData['type'].width,
      'background-color': nodeData['type']['background-color'],
      shape: nodeData['type'].shape,
      'border-color': nodeData['type']['border-color'],
    };
  
    return nodeStyle
  }
  
    createGraphFromJson(data: any) {
      // const data = JSON.parse(jsonString);
      this.cy.remove('#tempNode');
      if (!data || !data.nodes || !data.edges) {
        console.error('Invalid input JSON:', data);
        return;
      }
  
      // this.cy = cytoscape({
      //   container: document.getElementById('cy'),
      //   elements: data,
      //   layout: {
      //     name: 'fcose',
      //     spacingFactor: this.spacingF
      //   }as any,
      // });
  
      this.cy.add(data['nodes']);
      this.cy.add(data['edges']);
      var layout = this.cy.layout({
        name: 'fcose',
    spacingFactor: 8,
        animate: true,
          animationDuration: 1000
      });
  
      const switchLayoutBtn = document.querySelector('#switch-layout-btn');
      this.cy.layoutname = 'fcose';
  if (switchLayoutBtn !== null) {
  switchLayoutBtn.addEventListener('click', () => {
    // your code to switch layouts goes here
    if (this.cy.layoutname === 'fcose') {
      this.cy.layout({ name: 'dagre', animate: true, spacingFactor: this.spacingF / 3 }).run();
      this.cy.layoutname = 'dagre';
    } else {
      this.cy.layout({ name: 'fcose', animate: true, spacingFactor: this.spacingF }).run();
      this.cy.layoutname = 'fcose';
    }
  });
  
  }
      // alert(1)
      // console.log('this.cy >>>>>>>>>',this.cy)
      setTimeout(layout.run(), 2000)
  
      // this.cy.ready(() => {
      //   console.log(this.cy);
      // });
        // // Customize node styles
        // this.cy.nodes().forEach((node: cytoscape.NodeSingular) => {
        //   node.style('background-color', '#' + Math.floor(Math.random() * 16777215).toString(16)); // Random color
        //   node.style('width', (Math.floor(Math.random() * 20) + 10) + 'px'); // Random width between 10px and 30px
        //   node.style('height', (Math.floor(Math.random() * 20) + 10) + 'px'); // Random height between 10px and 30px
        // });
  
        // // Customize edge styles
        // this.cy.edges().forEach((edge: any) => {
        //   edge.style('line-color', '#' + Math.floor(Math.random() * 16777215).toString(16)); // Random color
        //   edge.style('width', (Math.floor(Math.random() * 3) + 1) + 'px'); // Random width between 1px and 3px
        // });
  
  
    }
  
  
  
    generateGraph(numNodes: number): any {
      const redStyle = {'height':250,'width':250 , 'background-color':'#FF0000','shape':'circle',"border-color": this.darkenColor('#FF0000')};
      const greenStyle = {'height':150,'width':250, 'background-color':'#00FF00','shape':'circle'}
      const yellowStyle = {'height':150,'width':250, 'background-color':'#fda707','shape':'round-rectangle'}
      const blueStyle = {'height':150,'width':250, 'background-color':'#97c2fc','shape':'round-rectangle'}
      const iconStyle1 = {'height':250,'width':350,'background-image': 'url(./ass)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle2 = {'height':150,'width':250,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/1830/1830351.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle3 = {'height':350,'width':450,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/3281/3281289.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle4 = {'height':250,'width':350,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/553/553416.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle5 = {'height':350,'width':450,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/2736/2736906.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle6 = {'height':450,'width':750,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/3001/3001758.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle7 = {'height':450,'width':750,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/476/476863.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const iconStyle8 = {'height':250,'width':350,'background-image': 'url(https://cdn-icons-png.flaticon.com/512/5585/5585856.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const typeArray = ['Bank','Restaurant','Work','Home','Car','Person','Group of people','Call'];
      const stylesArray = [
        iconStyle1,
        iconStyle2,
        iconStyle3,
        iconStyle4,
        iconStyle5,
        iconStyle6,
        iconStyle7,
        iconStyle8
      ]
      const nodes = [];
      const edges = [];
  
      // Add nodes
      for (let i = 0; i < numNodes; i++) {
        var randNmbr = Math.floor(Math.random() * (7 + 1));
        var nodestyle = stylesArray[randNmbr];
        var nodeType = typeArray[randNmbr];
        const node = {
          data: {
            id: `node${i}`,
            label: `${nodeType} ${Math.floor(Math.random() * (100 - 20 + 1) + 20)}`,
            name: 'tile',
            type:`${nodeType}`,
            date: `${this.getRandomDateAsString()}`
          },
          style :nodestyle
  
        };
        nodes.push(node);
      }
      // Add edges
          var edgeStyle = {
        'curve-style': 'bezier',
        'target-arrow-shape': 'triangle',
        'target-arrow-color': '#000000',
        'line-color': '#000000',
        'width': 4
      };
      const colors = ['red','green','blue','yellow'];
      for (let i = 0; i < numNodes - 1; i++) {
        const edge = {
          data: {
            id: `edge${i}`,
            source: `node${i}`,
            target: `node${Math.floor(Math.random() * (130 - 100 + 1) + 100)}`,
            color: colors[Math.floor(Math.random() * (1 + 1))],
            label: i,
          },
  
        };
        edges.push(edge);
        // const edge1 = {
        //   data: {
        //     id: `edge${i}`,
        //     source: `node${i}`,
        //     target: `node${Math.floor(Math.random() * (500 - 1 + 1) + 1)}`
        //   },
        // };
        // edges.push(edge1);
      }
      console.log({ nodes, edges })
      return { nodes, edges };
    }
  
  getRandomDateInMay2023() {
    const startOfMay = new Date("2023-05-01");
    const endOfMay = new Date("2023-05-31");
  
    const randomDate = new Date(startOfMay.getTime() + Math.random() * (endOfMay.getTime() - startOfMay.getTime()));
  
    return randomDate;
  }
    getRandomDateAsString(): string {
      const start = new Date(1970, 0, 1); // Start date
      const end = new Date(); // Today's date
      const randomDate = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime())); // Random date between start and end
      return randomDate.toISOString().substr(0, 10); // Return date as string in format "YYYY-MM-DD"
    }
    darkenColor(color: string): any {
      let r = parseInt(color.substring(1, 3), 16);
      let g = parseInt(color.substring(3, 5), 16);
      let b = parseInt(color.substring(5, 7), 16);
  
      r = Math.floor(r * 0.8);
      g = Math.floor(g * 0.8);
      b = Math.floor(b * 0.8);
      return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
    }
  
    getGraph(reportId: any){
  
  //if(masterId != "null" || masterId != "" ){}
  if(reportId != null  ){
    const headers = { 'Content-Type': 'application/json' };
    this.http.post(GlobalConstants.displayGraph+reportId, { headers }).subscribe(data => {
      console.log('datadatadatadatadatadata1',data);
      this.createGraphFromJson(data);
      console.log("data",data);
  
    });
  
  }else{
      const body = {"reportId":reportId};
      const headers = { 'Content-Type': 'application/json' };
      console.log( 'json',body);
      this.http.post(this.ipAddress+'/api/getGraphNodes', body, { headers }).subscribe(data => {
        console.log('datadatadatadatadatadata2',data);
        this.createGraphFromJson(data);
        console.log("data",data);
      });
  
    }
    }
  
    createLegend() {
      const legendContainer = document.createElement('div');
      legendContainer.classList.add('legend-container');
      legendContainer.style.position = 'absolute';
      legendContainer.style.bottom = '10px';
      legendContainer.style.left = '10px';
      legendContainer.style.backgroundColor = 'white';
      legendContainer.style.padding = '10px';
      legendContainer.style.border = '1px solid #ccc';
      legendContainer.style.borderRadius = '4px';
      this.cy.container().appendChild(legendContainer);
  
      const legendItems = [
        { type: 'line', color: 'red', label: 'Missed Call', title: 'missedcall' },
        { type: 'line', color: 'green', label: 'Call', title: 'call' },
        { type: 'line', color: 'blue', label: 'Text Message', title: 'message' },
        { type: 'line', color: 'yellow', label: 'Multiway', title: 'multiway' },
        { type: 'icon', iconUrl: '/assets/assets/KwgImages/mobile.png', label: 'Mobile Phone', title: 'mobile' }
      ];
  
      legendItems.forEach(item => {
        const legendItem = document.createElement('div');
        legendItem.classList.add('legend-item');
  
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.checked = true;
        checkbox.addEventListener('change', () => {
          this.filterEdgesByTitle(item.title, checkbox.checked);
        });
  
        legendItem.appendChild(checkbox);
  
        if (item.type === 'line') {
          const legendColor:any  = document.createElement('div');
          legendColor.classList.add('legend-color');
          legendColor.style.backgroundColor = item.color;
          legendColor.style.width = '20px';
          legendColor.style.height = '2px';
          legendColor.style.marginRight = '5px';
          legendColor.style.display = 'inline-block';
          legendColor.style.verticalAlign = 'middle';
  
          legendItem.appendChild(legendColor);
        } else if (item.type === 'icon') {
          const legendIcon:any  = document.createElement('img');
          legendIcon.classList.add('legend-icon');
          legendIcon.src = item.iconUrl;
          legendIcon.style.width = '20px';
          legendIcon.style.height = '20px';
          legendIcon.style.marginRight = '5px';
          legendIcon.style.display = 'inline-block';
          legendIcon.style.verticalAlign = 'middle';
  
          legendItem.appendChild(legendIcon);
        }
  
        const legendLabel = document.createElement('div');
        legendLabel.classList.add('legend-label');
        legendLabel.style.display = 'inline-block';
        legendLabel.style.verticalAlign = 'middle';
        legendLabel.textContent = item.label;
  
        legendItem.appendChild(legendLabel);
        legendContainer.appendChild(legendItem);
      });
    }
  
    filterEdgesByTitle(title: string, show: boolean) {
      const edges = this.cy.edges();
      edges.forEach((edge: { data: (arg0: string) => any; show: () => void; connectedNodes: () => any; hide: () => void; }) => {
        const type = edge.data('type');
        if (type === title) {
          if (show) {
            edge.show();
            const connectedNodes = edge.connectedNodes();
            connectedNodes.forEach((node: { data: (arg0: string) => boolean; }) => {
              if (node.data('hidden') === true) {
                this.showDevice(node);
              }
            });
          } else {
            edge.hide();
            const connectedNodes = edge.connectedNodes();
            connectedNodes.forEach((node: { connectedEdges: (arg0: string) => { (): any; new(): any; length: number; }; }) => {
              if (node.connectedEdges(':visible').length === 0) {
                this.hideDevice(node);
              }
            });
          }
        }
      });
  
      const nodes = this.cy.nodes();
      nodes.forEach((node: { data: (arg0: string) => any; }) => {
        const nodeType = node.data('type');
        if (nodeType === title) {
          if (show) {
            if (node.data('hidden') === true) {
              this.showDevice(node);
            }
          } else {
            this.hideDevice(node);
          }
        }
      });
    }
  
    hideDevice(deviceNode: any) {
      deviceNode.style('display', 'none');
      deviceNode.data('hidden', true);
    }
  
    showDevice(deviceNode: any) {
      deviceNode.style('display', 'element');
      deviceNode.data('hidden', false);
    }
  
  
    createFilter() {
      const filterContainer:any  = document.getElementById('filter-container');
      if(document.getElementById('label-filter'))
      {
        var active = 1;
      }
      else
      {
        const labelFilterLabel :any = document.createElement('label');
        labelFilterLabel.setAttribute('for', 'label-filter');
        labelFilterLabel.textContent = 'Label Filter:';
  
        const labelFilterInput:any  = document.createElement('input');
        labelFilterInput.setAttribute('type', 'number');
        labelFilterInput.setAttribute('id', 'label-filter');
        labelFilterInput.setAttribute('min', '0');
        labelFilterInput.setAttribute('step', '1');
  
        filterContainer.appendChild(labelFilterLabel);
        filterContainer.appendChild(labelFilterInput);
      }
  
    }
  
    attachFilterEventListener() {
      const labelFilterInput :any = document.getElementById('label-filter');
  
      labelFilterInput.addEventListener('input', () => {
        this.filterEdgesByLabelNumber();
      });
    }
  
    filterEdgesByLabelNumber() {
      const edges = this.cy.edges();
      const nodes = this.cy.nodes();
      // const labelFilterInput = <HTMLInputElement>document.getElementById('label-filter');
      const labelFilterValue = this.inputValue;
      this.hiddenNodes.clear(); // Clear the set of hidden nodes
      // edges.show();
      console.log(labelFilterValue)
      edges.forEach((edge: any) => {
        const label = edge.data('label');
        edge.show();
        if (!isNaN(Number(label))) {
          if (label < labelFilterValue) {
            edge.hide();
            this.hiddenNodes.add(edge.source());
            this.hiddenNodes.add(edge.target());
          } else {
            edge.show();
            console.log('show')
          }
        }
      });
  
      nodes.forEach((node: any) => {
  
            node.show(); // Show nodes that were previously hidden
  
  
      });
  
      nodes.forEach((node: any) => {
        if(node._private.data.type != 'City')
        {
          if (this.hiddenNodes.has(node)) {
            const connectedEdges = node.connectedEdges();
            const hiddenEdges = connectedEdges.filter((edge: any) => edge.hidden());
  
            if (hiddenEdges.length === connectedEdges.length) {
              node.hide();
            } else {
              node.show();
            }
          } else {
            node.show(); // Show nodes that were previously hidden
          }
        }
  
      });
    }
  
    filterEdgesByConnectionsNumber() {
      const nodes = this.cy.nodes();
      const hiddenNodes = new Set();
      const minLinks = this.linkCountInput;
      nodes.forEach((node: any) => {
        const connectedEdges = node.connectedEdges();
        const connectedEdgesCount = connectedEdges.length;
  
        // If the node is not linked to at least minLinks edges, hide it
        if (connectedEdgesCount < minLinks) {
          node.hide();
          hiddenNodes.add(node);
        } else {
          node.show();
        }
      });
  
      // Iterate again to handle connected edges visibility
      nodes.forEach((node: any) => {
        if (!hiddenNodes.has(node)) {
          const connectedEdges = node.connectedEdges();
          const hiddenEdges = connectedEdges.filter((edge: any) => edge.hidden());
  
          // If all connected edges are hidden, hide the node
          if (hiddenEdges.length === connectedEdges.length) {
            node.hide();
          }
        }
      });
    }
  
    exportNodesToExcel() {
      this.processedGraphData = this.generateGraphData(this.cy);
      this.filteredGraphData = this.processedGraphData;

      //this.gridOptions.api!.setRowData(this.processedGraphData);
      (this.gridOptions as any).api!.setRowData(this.processedGraphData);

      console.log("processedGraphData",this.processedGraphData);
      this.gridpopup.open();
      // const nodes = this.cy.nodes(); // Get all nodes in the graph
  
      // // Create the headers for the table
      // const headers = ['FROM', 'CONNECTION', 'TO'];
  
      // // Extract node labels, edge types, and connected node labels as an array
      // const nodeData = [headers]; // Initialize with headers
      // nodes.forEach((node: { data: (arg0: string) => any; connectedEdges: () => any; }) => {
      //   const label = node.data('label');
      //   const connectedEdges = node.connectedEdges();
      //   connectedEdges.forEach((edge: { source: () => any; target: () => any; data: (arg0: string) => any; }) => {
      //     const edgeType = edge.data('info');
      //     console.log('hereererererererererererer>>>>>>>>>>>>>>>>>>>>> ',edgeType)
      //     var edgeinfo = "";
      //     edgeType.forEach((info: any) => {
      //       edgeinfo += info.title + " : " + info.name + "  ";
      //     })
      //       const sourceLabel = edge.source().data('label');
      //       const targetLabel = edge.target().data('label');
      //       const connectedNodeLabel = sourceLabel !== label ? sourceLabel : targetLabel;
      //       nodeData.push([label, edgeinfo, connectedNodeLabel]);
  
      //   });
      //   if (connectedEdges.length === 0) {
      //     nodeData.push([label, '', '']);
      //   }
      // });
  
      // // Create a new workbook and worksheet
      // const workbook = XLSX.utils.book_new();
      // const worksheet = XLSX.utils.aoa_to_sheet(nodeData);
  
      // // Add the worksheet to the workbook
      // XLSX.utils.book_append_sheet(workbook, worksheet, 'Node Data');
  
      // // Define the range for the table
      // const range = XLSX.utils.sheet_to_json(worksheet, { header: 1, raw: false }).map((row: any) => row.filter((cell: any) => cell !== undefined));
  
      // // Add the filter to the table
      // const filter = XLSX.utils.encode_range({ s: { r: 0, c: 0 }, e: { r: range.length - 1, c: headers.length - 1 } });
      // (worksheet as any)['!autofilter'] = { ref: filter };
  
      // // Export the workbook to an Excel file
      // const workbookOutput = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
      // const blob = new Blob([workbookOutput], { type: 'application/octet-stream' });
      // saveAs(blob, 'cnn.xlsx');
    }
  
    nodeCreationForm()
    {
      this.createpopup.open()
    }
  
    createNode() {
      // alert('Create')
      // const node = {
      //   group: 'nodes',
      //   data: {
      //     id: label,
      //     label: label,
      //   },
      // };
  
      // this.cy.add(node);
  
  
  
      var nlabel = $('#node-name').val();
      var ntype = $('#node-type').val();
      const iconCall = {'height':350,'width':350,'background-image': 'url(/../assets/assets/kwgImages/'+ ntype +'.png)',
      'background-fit': 'contain',
      'background-clip': 'none',
      'background-opacity': 0};
      const node = {
            data: {
              id: `${nlabel}`,
              label: `${nlabel}`,
              name: 'tile',
              type:`${ntype}`,
              date: ``,
              info:[{'title':'name', 'name':`${nlabel}`}],
              size:'240px',
  
            },
            style :iconCall
  
          };
          this.cy.add(node);
          this.createpopup.close();
    }
  
  
  
    createEdgeLink(nodeId:any){
      if (this.firstNodeId !== null && this.firstNodeId !== nodeId) {
        // Create an edge between the first selected node and the current clicked node
        const edgeId = `edge_${this.firstNodeId}_${nodeId}`;
        const edgeData = {
          id: edgeId,
          target: this.firstNodeId,
          source: nodeId,
          color:'purple'
        };
        this.cy.add({ data: edgeData });
  
        // Reset the firstNodeId variable
        this.firstNodeId = null;
        this.edgeLink = false;
  
      }
    }
  
    getUpdGraph(nextLayerReportId:any)
    {
     // var localid = sessionStorage.getItem("newReport");
      console.log("getUPdGraph Report Id>>>>>>>" + nextLayerReportId);
      this.getGraph(nextLayerReportId)
    }
  
    switchmode(){
      const darkModeToggle : any = document.querySelector("#dark-mode-toggle");
  
      var cy : any = document.getElementById("cy");
      // Toggle the "dark-mode" class on the body element
      console.log("cy",cy);
      cy.classList.toggle("dark-mode");
      // Update the text of the button
      if (!this.darkmode) {
        this.darkmode = true;
        $('.menuicons').addClass('darkicons');
        this.cy.edges().style('color', 'white');
        this.cy.nodes().style('color', 'white');
        // $('#menu ul li a span').css('color', 'white');
        $('#darkmodeimage img').removeAttr('src');
        $('#darkmodeimage img').attr('src', 'assets/graphicons/lightbulb-2.svg');
        $('#darkmodeimage span').html('Light');
        document.documentElement.style.setProperty('--primary-color', '#2a2a2a');
        document.documentElement.style.setProperty('--text-color', 'white');
  
  
      } else {
        this.darkmode = false;
        $('.menuicons').removeClass('darkicons');
        this.cy.edges().style('color', '#35363a');
        this.cy.nodes().style('color', '#35363a');
        // $('#menu ul li a span').css('color', 'black');
        $('#darkmodeimage img').removeAttr('src');
        $('#darkmodeimage img').attr('src', 'assets/graphicons/lightbulb-1.svg');
        $('#darkmodeimage span').html('Dark');
        document.documentElement.style.setProperty('--primary-color', '#fff4f4');
        document.documentElement.style.setProperty('--text-color', 'black');
  
  
      }
    }
    searchNodes(elem: any) {
      const query = elem.target.value;
      console.log("searchNodes",query);
      // Reset the style for all nodes before applying the search results
      this.cy.nodes().forEach((node: { style: (arg0: string, arg1: string) => any; }) => node.style('display', 'element'));
  
      if (query.trim() === '') {
        return; // If the search query is empty, show all nodes
      }
  
      const matchingNodes = this.cy.nodes().filter((node: { data: (arg0: string) => any; }) => {
        const label = node.data('label');
        console.log("label", node);
        return label.toLowerCase().includes(query.toLowerCase());
      });
  
      // Hide nodes that don't match the search query
      this.cy.nodes().difference(matchingNodes).forEach((node: { style: (arg0: string, arg1: string) => any; }) => node.style('display', 'none'));
    }
  
    private generateGraphData(graph: any[]): any[] {
      const processedData: any[] = [];
  
      this.cy.nodes().forEach((node: { data: (arg0: string) => any; connectedEdges: () => { (): any; new(): any; length: any; }; }) => {
        const nodeName = node.data('label'); // Assuming 'label' is the property containing node names
        const edgeCount = node.connectedEdges().length; // Counting the number of connected edges
  
        processedData.push({
          nodeName: nodeName,
          edgeCount: edgeCount
        });
      });
  
      return processedData;
    }
  
    gridButton(): void {
      // const selectedRows = this.gridOptions.api!.getSelectedRows();
      // const selectedNodeIds = selectedRows.map(row => row.nodeName);
      const selectedRows = (this.gridOptions.api as any).getSelectedRows() as { nodeName: string }[] | null | undefined;
      const selectedNodeIds = selectedRows.map(row => row.nodeName);

      // if (selectedRows) {
      //     const selectedNodeIds = selectedRows.map(row => row.nodeName);
      //     // Further operations with selectedNodeIds
      // } else {
      //     console.error('Selected rows are null or undefined.');
      //     // Handle this case as per your application logic
      // }
    // Hide all nodes and edges in the Cytoscape graph
    this.cy.nodes().hide();
    this.cy.edges().hide();
  
    console.log('nodeId',selectedNodeIds)
    // Show selected nodes and their directly connected nodes and edges
    selectedNodeIds.forEach(nodeId => {
      console.log('nodeId',nodeId)
      const selectedNode = this.cy.$(`node[id="${nodeId}"]`);
      this.showConnectedNodes(selectedNode);
    });
  
    // Layout the graph after updating the visibility
    this.cy.layout({ name: 'fcose', animate: true, spacingFactor: this.spacingF }).run();
  
    }
    private showConnectedNodes(node: any): void {
      console.log(node)
    // Show the current node
    node.show();
  
    // Show connected edges
    node.connectedEdges().show();
  
    // Show directly connected nodes
    node.neighborhood().nodes().forEach((neighbor: { show: () => void; }) => {
      neighbor.show();
    });
    }

    closeDialog(): void {
      this.dialogRef.close();
    }


    async runNextLayer(id:any){

      const getQueryParamsApi=from(axios.get(GlobalConstants.getQueryParams+JSON.parse(JSON.parse(this.informationservice.getAgGidSelectedNode())[0].masterQueryId)));
      const getQueryParams=await lastValueFrom(getQueryParamsApi);
      // info=getQueryParams.data[0];
      // this.query=getQueryParams.data[0].query;
      // this.parameters=JSON.parse(getQueryParams.data[0].queryParams);
      // this.queryId=getQueryParams.data[0].queryId;
      // this.queryName=getQueryParams.data[0].queryName;
      // this.execHeads=JSON.parse(getQueryParams.data[0].execHeads);
      const runNextLayerProcessApi = from(axios.post(GlobalConstants.runNextLayerProcess+JSON.parse(this.informationservice.getAgGidSelectedNode())[0].masterId+"/"+id+"/"+this.informationservice.getLogeduserId(),getQueryParams.data));
      const runNextLayerProcess = await lastValueFrom(runNextLayerProcessApi);
      console.log("RUN NEXT LAYER RETURN VALUE>>>>>>>>>>",runNextLayerProcess.data);

      this.getUpdGraph(runNextLayerProcess.data);
    }

  }
  
  

