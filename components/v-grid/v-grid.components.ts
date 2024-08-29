import 'ag-grid-enterprise';
import { Component, Input, OnChanges, OnInit, SimpleChanges, EventEmitter, Output, Renderer2  } from '@angular/core';
import { ColDef, ColumnApi, GridApi, IAfterGuiAttachedParams, RowGroupingDisplayType,GridOptions,  GetMainMenuItemsParams, MenuItemDef,
} from 'ag-grid-community';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { LinkCellRenderer } from './components/v-grid-link';
import { CheckboxCellRenderer } from './components/v-grid-checkbox';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { DataService } from 'src/app/Kernel/services/data.service';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { from, lastValueFrom } from 'rxjs';
import axios from 'axios';
import e from 'cors';
import { Console, table } from 'console';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { read } from 'fs';
import { data } from 'jquery';



@Component({
  selector: 'v-grid',
  templateUrl: './v-grid.components.html',
  styleUrls: ['./v-grid.components.css'],
})

export class AGGridComponent implements OnInit, OnChanges {
  
  @Input() checkInput : Boolean = false; //  hayde kermel search bar input design display none or block
  @Input() checkPagination :boolean;

  public gridApi: GridApi;
  private columnApi: ColumnApi;

  public paginationSizeList = [{ id: 1, name: 10 }, { id: 2, name: 20 }, { id: 3, name: 50 }, { id: 4, name: 100 }];
  public groupDisplayType: RowGroupingDisplayType = 'groupRows';
  public groupRowRendererParams: any = {
    checkbox:true,
    checkboxSelection: true,
    rowSelection: 'multiple',
    groupSelectsChildren: true,
  };
  // autoGroupColumnDef = {

  //   // enabled sorting on Row Group Columns only
  //   sortable: true,
  //   cellRendererParams: {
  //   //   suppressCount: false,
  //     checkbox: true,
  //     checkboxselection: true,
  //     groupSelectsChildren: true,
  //   //   rowSelection: 'multiple',

  //   }
  //   // cellRenderer: CheckboxCellRenderer, // Add this line
  // };
  public autoGroupColumnDef = {
    // enabled sorting on Row Group Columns only
    sortable: true,
    headerName: 'Organisational Hierarchy',
    cellRendererParams: {
      innerRenderer: 'group',
      suppressCount: true,
      expandedByDefault: false
    },

  };
  // public gridColumnApi: any;

  public pivotColumnDefs: ColDef[];
  public pivotMode: boolean ;
  public pivotSuppressAutoColumn: boolean;
  public ColDef: any;
  public statusBar: any;
  public rowModelType: any;
  public rowData: any[] = [];
  public paginationFlag: boolean = false;
  public paginationSize: any;
  public selectedNodes: string = '';
  public gridModifiedRows: any[] = [];
  public gridAddedRows: any[] = [];
  public gridDeletedRows: any[] = [];
  public agGridUpdates: any[] = [];
  public userId: any;
  public isManaged: any;
  public accessRights: any;
  public arrayOfData: any = [];
  public selectedNodesAr: any = "";
  public selectedCellsAr: any = "";
  public selectedCellsArString: any = "";
  public selectedNodesNew: any;
  public isOnGridReady: Boolean = true;
  public rowCount: any = 0;
  public rowContSelected: any = 0;
  public duplicates: number[] = [];
  public preloadedEvent: any;
  public checkedrowData: any[] = [];
  public selectedNodesBAR:any;
  @Input() public Parameter: any;
  @Input() public agStyle: any;

  public isGrouped: boolean = false;
  public commonKeysArray: any = []; // Array to store common keys for all children


  // Used to handle cellRenderer feature in ag-grid
  @Input() public frameworkComponents: any;
  // Used as flag that will size the columns to fit the grid or no
  // @Input() public sizeColumnsToFit: String = "false";
  // Used as flag that will show the right sidebar or no
  @Input() public allowSideBar: any;
  // Used to pass the columns the ag-grid will load
  @Input() public agColumns: any;
  // Used to specify the API that will load data in the grid
  @Input() public dataApi: any;
  // Used to send param for grid fetching if needed
  @Input() public dataApiParam: any;
  // Used to speficy what to show add/modify/delete buttons, a => add, m => modify, d = delete
  @Input() public agToolBar: any = '';
  // Used to specify if the grid shows paggination or no
  @Input() public pagination: boolean = false;
  // Used to specify the pagination size of the grid
  @Input() public paginationPageSize: any;
  // Used to specify which column/columns are used as primary
  @Input() public agPrimaryKey: any = '';
  // Used to specify how the data is fetched if from Client Side or Server Side
  @Input() public agType: any = 'clientSide';
  // Used to add capability to do crud operations on grid level
  @Input() public agOnGridEvents: boolean = false;
  //used to show or hide the submit button under the grid
  @Input() public showOrHideButton: boolean = false;
  //used in masterLinkForm to hide the buttons but the functionality still works
  @Input() public hideBTN: boolean = false;
  // Used to specify the menuvariable of the grid
  @Input() public menuVariable: any;
  // Used as flag to show if the grid will allow grouping or no
  @Input() public rowGrouping: boolean;
  // Used as flag to show the grid's context menu or no
  @Input() public contextmenu: boolean;
  // Used to pass the needed data statically to the grid
  @Input() public staticData: any = -1;
  @Input() public agRowSelection: any  = 'single';
  // Used to turn of search
  @Input() public allowSearch: boolean = true;
  // Used to turn of is advanced search
  @Input() public isAdvancedSearch: any = 0;
  // Allow Pagination
  @Input() public allowPagination: boolean = true;
  // Check if inDisplay or not
  @Input() public isForInDisplay: boolean = false;
  @Input() public isForQueryForm: boolean = false;

  //Check if in USM
  @Input() public isAccessRightsGrid: boolean = false;

  // Lookup parameters
  @Input() public isGridInLookup: boolean = false;
  @Input() public lookupFieldName: String = "";
  @Input() public isReadOnly: boolean;
  @Input() public treeGridHeader: any[] ;
  @Input() public treeGridData: any[];
  @Input() public isUpdateColsDef: number = 0;

  @Input() public isTreeGrid: boolean = false;
  public lookupIds: any;
  public lookupNames: any;
  public lookupAllNames: any;

  public NoneGrid = 'disabled';

  public item1: any;
  public item2: any;
  public value1: any;
  public value2: any;
  // Exchange changed parameters with the container
  @Output() paramsChange = new EventEmitter<any>();

  // Grid crud actions eventEmitter
  @Output() onGridAdd = new EventEmitter<void>();
  @Output() onGridUpdate = new EventEmitter<void>();
  @Output() onGridDelete = new EventEmitter<void>();
  @Output() onGridEventSave = new EventEmitter<any>();
  @Output() onGridDownload = new EventEmitter<void>();

  // gatherAllSelectedRowNodes: any[] = [];
  paginationChange = new UntypedFormGroup({
    value: new UntypedFormControl('')
  })

  selectedCheckboxes: Set<string> = new Set<string>();
  gridOptions: GridOptions

  constructor(
    private eventEmitterService: EventEmitterService,
    private commonFunction: CommonFunctions,
    public dataservice: DataService,
    public informationservice: InformationService,
    private renderer: Renderer2) {
     }
     public groupDefaultExpanded = -1;


  ngOnInit(): void {
    if(this.isTreeGrid){
      this.ColDef=this.agColumns[0];
      this.rowData=this.staticData;
      this.gridOptions = {
        defaultColDef: {
          sortable: true,
          filter: true,
          resizable: true,
        },
        autoGroupColumnDef: {
          headerName: "Hierarchy",
          minWidth: 300,
          cellRendererParams: {
            suppressCount: true,
            checkbox: true
          }
        },
        treeData: true,
        animateRows: true,
        groupDefaultExpanded: -1,
        getDataPath: this.getDataPath,
        getRowId: (params) => params.data.id,
      };
    }
    this.informationservice.setAgGidSelectedNode('');
    if(this.informationservice.getAccessRights()){
      this.accessRights = JSON.parse(this.informationservice.getAccessRights());
    }else{
      this.accessRights = "null";
    }

    this.NoneGrid = '';

    if(this.checkPagination){
      this.hidePagination();
    }else{
      this.showPagination();
    }
   
  } 

  hidePagination(): void {
    setTimeout(() => {
      const paginationPanels = document.querySelectorAll('.ag-paging-panel');
      paginationPanels.forEach(panel => {
        (panel as HTMLElement).style.display = 'none';
      });
    }, 100); // Adjust the delay as needed
  }
  
  showPagination(): void {
    setTimeout(() => {
      const paginationPanels = document.querySelectorAll('.ag-paging-panel');
      paginationPanels.forEach(panel => {
        (panel as HTMLElement).style.display = '';
      });
    }, 100); // Adjust the delay as needed
  }
  

getChildren(parentId: number): any[] {
  const children: any[] = [];

  // Helper function to recursively find all children
  const findChildren = (id: number) => {
    const childNodes = this.rowData.filter(item => item.parentid === id);
    childNodes.forEach(child => {
      children.push(child);
      findChildren(child.ROW_ID); // Recursively find children of this child
    });
  };

  // Start finding children from the given parentId
  findChildren(parentId);

  return children;
}
  onRowSelectedTreeGrid(event: any): void {
    const selectedNode = event.node;
    console.log('event--->', selectedNode);
    if (selectedNode.isSelected()) {
      const selectedData = selectedNode.data;
      const children = this.getChildren(selectedData.id);
      const allIds = [selectedData.ROW_ID, ...children.map(child => child.ROW_ID)];
      let result = allIds.join(',');
      result = result.replaceAll("[","").replaceAll("]","");
      result = "[" + result + "]";
      console.log('Result:', result);
      console.log(event.node.data);
      this.informationservice.setVcisrowData(event.node.data);

      this.informationservice.setAgGidSelectedNode(result);
    } else {
      console.log('Deselected Node:', event.node.data);
    }
  }


  getDataPath = (data: any) => {
    const path = [];
    let current = data;
    while (current) {
        path.unshift(current.id); // Adjust if your path should use a different field
        current = this.rowData.find(item => item.id === current.parentid); // Adjust if needed
    }
    return path;
};

  onColumnRowGroupChanged(event: any) {
    if(this.isGrouped == false)
    {
      this.uncheckAllCheckboxes();

      this.isGrouped = true;
      this.selectedNodesAr = [];
    }
    else
    {
      this.uncheckAllCheckboxes();

      this.isGrouped = false;
      this.selectedNodesAr = [];
    }
  }

  uncheckAllCheckboxes(): void {
    // Get all rows
    this.gridApi.forEachNode(node => {
      // Uncheck if the row is selected
      if (node.isSelected()) {
        node.setSelected(false);
      }
    });
  }

  resetGrid() {
    this.onGridReady(this);
  }

  ngOnChanges(changes: SimpleChanges) {
    if(this.informationservice.getAccessRights()){
      this.accessRights = JSON.parse(this.informationservice.getAccessRights());
    }else{
      this.accessRights = "null";
    }

    if ('dataApi' in changes || 'Parameter' in changes || 'staticData' in changes) {
      this.fetchGridData();
    }
    // if(changes['isUpdateColsDef']){
    //   alert(this.isUpdateColsDef);
    //   this.updateColsdef();
    // }
    if(this.isUpdateColsDef != 0){
      alert(6)
          this.updateColsdef();

    }

  }

  onAddClick() {
    if (this.agOnGridEvents == true) {
      this.addNewAgRow();
    }
    if (this.agOnGridEvents == false) {
      this.onGridAdd.emit();
    }
  }

  onUpdateClick() {
    let selectedRow = this.informationservice.getAgGidSelectedNode();
    if (selectedRow == "") {
      this.commonFunction.alert("alert", "Please choose a selected row");
    } else {
      this.onGridUpdate.emit();
    }
  }

  onDeleteClick() {
    let selectedRowId = this.informationservice.getAgGidSelectedNode();
    if (selectedRowId == "") {
      this.commonFunction.alert("alert", "Please choose a selected row to delete");
    } else {
      let text = "Are you sure you want to delete ?";
      if (confirm(text) == true) {
        if (this.agOnGridEvents == true) {
          this.deleteAgRow();
        }

        if (this.agOnGridEvents == false) {
          this.onGridDelete.emit();
        }
      } else {
        return;
      }
    }
  }

  // autoSizeAllColumns()
  // {
  //   const allColumnIds = this.ColDef.map((column: { field: any; }) => column.field);
  //   this.columnApi.autoSizeColumns(allColumnIds);
  // }


  onFirstDataRendered(params: any): void {
if(this.isGridInLookup){

setTimeout(() => {


    let rowdata2: any[] = [];
    rowdata2 = this.rowData;
    let tablesselected: any;

    if (!localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id")) {
      params.api.sizeColumnsToFit();
    } else {

      if((!localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes("[") || !localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes("{")) && localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes(",")){
        tablesselected=localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").split(",")
       let i=0;
        params.api.forEachNode((rowNode: any, index: any) => {


          if ((rowNode.data.id == undefined ||rowNode.data.id=="undefined") && rowNode.data.ID == tablesselected[i]) {

            rowNode.setSelected(true);
            i++;

          }else if ((rowNode.data.ID == undefined ||rowNode.data.ID=="undefined") && rowNode.data.id == tablesselected[i]) {

            rowNode.setSelected(true);
            i++;
          }
        });

      }else{
        tablesselected = JSON.parse(localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id"));


      params.api.forEachNode((rowNode: any, index: any) => {

        if (rowNode.data.id == tablesselected || rowNode.data.ID==tablesselected) {

          rowNode.setSelected(true);
        }
      });
      }



    }
  }, 1500);

  }else if(this.isForQueryForm){
    let ids = JSON.parse('[' + this.informationservice.getDynamicService('agGidSelectedQueryForm_('+this.lookupFieldName+')') + ']');
    let stringArray: string[] = ids.map((num:any) => num.toString());

    params.api.forEachNode((rowNode:any, index:any) => {
       rowNode.setSelected(stringArray.includes(rowNode.data.TRANSACTION_GROUPING_ID.toString()))
      });
  }else{


      let rowdata2: any[] = [];
      rowdata2 = this.rowData;
      let tablesselected: any;

      if (!localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id")) {
        params.api.sizeColumnsToFit();
      } else {

        if((!localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes("[") || !localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes("{")) && localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes(",")){
          tablesselected=localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").split(",")
          console.log("TABLESELECTED>>>>>>",tablesselected);
         let i=0;
          params.api.forEachNode((rowNode: any, index: any) => {


            if ((rowNode.data.id == undefined ||rowNode.data.id=="undefined") && rowNode.data.ID == tablesselected[i]) {
              console.log("SINGLE ROW NODEE>>>>",rowNode);

              rowNode.setSelected(true);
              i++;

            }else if ((rowNode.data.ID == undefined ||rowNode.data.ID=="undefined") && rowNode.data.id == tablesselected[i]) {
              console.log("SINGLE ROW NODEE>>>>",rowNode);

              rowNode.setSelected(true);
              i++;
            }
          });

        }else{
          tablesselected = JSON.parse(localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id"));

      //    console.log("SINGLE RENDERED TABLE SELECTION >>>>>>>",tablesselected);

        params.api.forEachNode((rowNode: any, index: any) => {
        //  console.log("SINGLE RENDERED ROW NODE>>>>",rowNode.data);

          if (rowNode.data.id == tablesselected || rowNode.data.ID==tablesselected) {

            rowNode.setSelected(true);
          }
        });
        }



      }



  }

//this.autoSizeAllColumns();
// this.paginationPageSize=15;

  }

  onTreeGridReady(params: { api: GridApi, columnApi: ColumnApi }): void {
    this.gridApi = params.api;
    this.columnApi = params.columnApi;
    if (this.gridApi) {
      this.gridApi.setRowData(this.treeGridData);
    }
  }
  onGridReady(params: any) {
    // this.informationservice.setAgGidSelectedNode('');
    this.gridApi = params.api;
    this.columnApi = params.columnApi;


    console.log("GRID>>>>>>>>>>",this.gridApi);
    this.preloadedEvent = params;



    if (this.agColumns.length > 0) {
      this.fetchGridData();
      if (this.allowSideBar == 'true') {
        params.api.setSideBar('columns');
        params.api.setSideBarVisible('true');





        this.statusBar = {
          statusPanels: [
            { statusPanel: 'agTotalRowCountComponent', align: 'left' },
            { statusPanel: 'agFilteredRowCountComponent' },
            { statusPanel: 'agSelectedRowCountComponent' },
            { statusPanel: 'agAggregationComponent' },
          ],
        };
        params.api.closeToolPanel();
      }
      params.api.setColumnDefs(this.ColDef);
      params.api.refreshHeader();
      this.gridApi.setDomLayout('autoHeight');


    }
    //     $('#autoSize')[0].click();
    // console.log('on grid ready>>>>>>>>>>>>>');


    //to resize the columns to the longest data input in their cells
    params.api.sizeColumnsToFit();
    //needed to give time for the grid to load


    //   setTimeout(() => {
    //   this.autoSizeAllColumns();
    // }, 500);


  }


//   handleAggridJSONRowSelection(primaryKey: any, event: any, selectionType: any)
//   {

//     let nbOfPrimaryKeys = this.commonFunction.countNbOfOccuranceStr(primaryKey, ",");
//     nbOfPrimaryKeys = Number(nbOfPrimaryKeys + 1);

//     let columnVal;
//     this.selectedNodesAr += ",{";
//     if (selectionType != "unselected")
//     {
//       if(this.isGrouped == true || this.informationservice.getIsRowGroup() =='1')
//         {
//           for(let q = 0; q < event.node.allLeafChildren.length; q++)
//           {
//             for (let i = 0; i < nbOfPrimaryKeys; i++)
//             {
//               let columnName: String = primaryKey.split(",")[i];

//                 if(this.isGrouped == true  || this.informationservice.getIsRowGroup() =='1')
//                   {
//                       columnVal = event.node.allLeafChildren[q].data[primaryKey.split(",")[i]] || "0";
//                       this.selectedNodesAr += "\"" + columnName + "\"" + ":" + "\"" + columnVal + "\"" + ", ";
//                   }

//               console.log("this.selectedNodesAr = ", this.selectedNodesAr);

//               if ((i + 1) == nbOfPrimaryKeys)
//               {
//                 if (this.rowContSelected <= this.rowCount)
//                 {
//                   this.selectedNodesAr += "\"rowSlectedStatus\":\"update\"";
//                   this.selectedNodesAr += "}";
//                 }
//                 else
//                 {
//                   this.selectedNodesAr += "}";
//                 }
//               }
//             }
//           }
//         }
//         else
//         {
//           for (let i = 0; i < nbOfPrimaryKeys; i++)
//           {
//             let columnName: String = primaryKey.split(",")[i];
//             columnVal = event.data[primaryKey.split(",")[i]] || "0";
//             this.selectedNodesAr += "\"" + columnName + "\"" + ":" + "\"" + columnVal + "\"" + ", ";

//             if ((i + 1) == nbOfPrimaryKeys)
//               {
//                 if (this.rowContSelected <= this.rowCount)
//                 {
//                   this.selectedNodesAr += "\"rowSlectedStatus\":\"update\"";
//                   this.selectedNodesAr += "}";
//                 }
//                 else
//                 {
//                   this.selectedNodesAr += "}";
//                 }
//               }
//           }
//         }

//     if (this.selectedNodesAr.endsWith(", }]"))
//     {
//       this.selectedNodesAr = this.selectedNodesAr.replace(", }]", "}]");
//     }
//     this.selectedNodesAr = this.selectedNodesAr.replace("[", "")
//     this.selectedNodesAr = this.selectedNodesAr.replace("]", "");
//     this.selectedNodesAr = "[" + this.selectedNodesAr + "]";
//     this.selectedNodesAr = this.selectedNodesAr.replace("undefined,", "0");
//     this.selectedNodesAr = this.selectedNodesAr.replace("undefined", "0");
//     this.selectedNodesAr = this.selectedNodesAr.replace("},]", "}]");
//     this.selectedNodesAr = this.selectedNodesAr.replace("[,{", "[{");

//     if(this.isGrouped == true  || this.informationservice.getIsRowGroup() =='1')
//     {
//       for (let i = 0; i < event.node.allLeafChildren.length; i++)
//       {
//         if(i < event.node.allLeafChildren.length - 1)
//         {
//           this.selectedNodesAr = this.selectedNodesAr.replace(", }", "},{");
//         }
//         else
//         {
//           this.selectedNodesAr = this.selectedNodesAr.replace(", }", "}");
//         }
//       }
//     }
//     else
//     {
//       for(let i = 0; i < this.selectedNodesAr.length; i++)
//       {
//         this.selectedNodesAr = this.selectedNodesAr.replace(", ,", "},");
//       }
//     }
//   }
//     //note that here, the comma is followed by a space for the cases that the json would have ended in ", }]"
//     if (this.selectedNodesAr.endsWith(", }]"))
//       {
//         this.selectedNodesAr = this.selectedNodesAr.replace(", }]", "}]");
//       }

//     if (selectionType == "unselected") {
//       this.selectedNodesAr=this.selectedNodesAr.replace(/\s/g,"");

//       console.log("EVENT>>>>>>>>>>>>>",event.data);
//       console.log("selectedNodesAr ==== ", this.selectedNodesAr);
//       if (this.selectedNodesAr.endsWith(",{"))
//       {
//         this.selectedNodesAr = this.selectedNodesAr.slice(0, -2);
//       }

//       console.log("OLD ARRAY>>>>>",this.selectedNodesAr);

//       let selectedNodesJson = JSON.parse(this.selectedNodesAr);
//       this.informationservice.getAgGidSelectedNode();
//      let commonKeys: { [x: string]: any; };

//     if (this.isGrouped == true  || this.informationservice.getIsRowGroup() =='1') {
//       if (event.node.data == '' || event.node.data == null || event.node.data == undefined) {

//         for (let q = 0; q < event.node.allLeafChildren.length; q++) {
//           let commonKeys = Object.keys(event.node.allLeafChildren[q].data).reduce((result: any, key) => {
//             if (this.agPrimaryKey.includes(key)) {
//               result[key] = event.node.allLeafChildren[q].data[key];
//             }
//             return result;
//           }, {});
//           this.commonKeysArray.push(commonKeys); // Push the result of each iteration to the array
//         }

//         // Apply replacement operations on each element of commonKeysArray
//         this.commonKeysArray = this.commonKeysArray.map((commonKeys: any) => {
//           let selectedNodesAr = JSON.stringify(commonKeys);
//           selectedNodesAr = selectedNodesAr.replace("[", "")
//                                            .replace("]", "")
//                                            .replace("undefined,", "0")
//                                            .replace("undefined", "0")
//                                            .replace("},]", "}]")
//                                            .replace("[,{", "[{")
//                                            .replace(",,", ",")
//                                            .replace(",]", "]")
//                                            .replace("[,", "[");
//           return JSON.parse(selectedNodesAr);
//         });
//       }
//     }

//     else
//     {
//       console.log("event.node.data");
//       commonKeys = Object.keys(event.node.data).reduce((result:any, key) =>
//       {
//         console.log("key == ", key)
//         if (this.agPrimaryKey.includes(key))
//         {
//           result[key] = event.node.data[key];
//           console.log("event.node.data[key] = ", event.node.data[key])
//         }
//       return result;
//      }, {});
//     }

//       this.selectedNodesAr = JSON.stringify(selectedNodesJson);
//       if (this.isGrouped == true  || this.informationservice.getIsRowGroup() =='1')
//       {
//         this.selectedNodesAr=this.selectedNodesAr.replaceAll(JSON.stringify(this.commonKeysArray),"");

//         let valuesOfPrimaryKeys = primaryKey.split(",");
//         this.value1 = valuesOfPrimaryKeys[0];
//         this.value2 = valuesOfPrimaryKeys[1];

//         selectedNodesJson = selectedNodesJson.filter((item1: { [key: string]: string }) =>
//           !this.commonKeysArray.some((item2: { [key: string]: string }) =>
//               item2[this.value1] === item1[this.value1] && item2[this.value2] === item1[this.value2]
//           )
//       );

//         console.log("selectedNodesJson == ", selectedNodesJson);
//         console.log("valuesOfPrimaryKeys === ", valuesOfPrimaryKeys);
//         console.log("1 this.selectedNodesJson === ", this.selectedNodesAr);
//         // this.selectedNodesAr = selectedNodesJson;
//         this.selectedNodesAr = JSON.stringify(selectedNodesJson);
//         console.log("2 this.selectedNodesAr === ", this.selectedNodesAr);

//       }
//       else
//       {
//         this.selectedNodesAr=this.selectedNodesAr.replaceAll(JSON.stringify(commonKeys),"");
//         console.log("commonKeys = ", commonKeys)
//       }

//       this.selectedNodesAr = this.selectedNodesAr.replace("[", "")
//       this.selectedNodesAr = this.selectedNodesAr.replace("]", "");
//       this.selectedNodesAr = "[" + this.selectedNodesAr + "]";
//       this.selectedNodesAr = this.selectedNodesAr.replace("undefined,", "0");
//       this.selectedNodesAr = this.selectedNodesAr.replace("undefined", "0");
//       this.selectedNodesAr = this.selectedNodesAr.replace("},]", "}]");
//       this.selectedNodesAr = this.selectedNodesAr.replace("[,{", "[{");
//       this.selectedNodesAr = this.selectedNodesAr.replace(",,", ",");
//       this.selectedNodesAr = this.selectedNodesAr.replace(",]", "]");
//       this.selectedNodesAr = this.selectedNodesAr.replace("[,", "[");

//       console.log("NEW ARRAY>>>>>",this.selectedNodesAr);

//       if (this.isGrouped == true  || this.informationservice.getIsRowGroup() =='1')
//       {
//         this.commonKeysArray = [];
//       }

//     }
//  //   console.log("V-GRID LOOKUP GRID SELECTION>>>>>>>>",this.agRowSelection);
// console.log("SELECTED AR NODES BEFORE", this.selectedNodesAr);


//  //////////////////////////////////////////////////////////////HAYA START Access Rights (USM)
//  if (this.isAccessRightsGrid){
//   // Remove duplicates based on first primary key only in USM
//   let primaryKeys = primaryKey.split(",");

//   let firstPrimaryKey = primaryKey.split(",")[0];
//   let selectedNodesJson = JSON.parse(this.selectedNodesAr);
//   let uniqueNodesMap = new Map();
//   selectedNodesJson.forEach((node: { [x: string]: any; }) => {
//     uniqueNodesMap.set(node[firstPrimaryKey], node);
// });

// this.selectedNodesAr = JSON.stringify(Array.from(uniqueNodesMap.values()));

// this.selectedNodesAr = this.selectedNodesAr.replace("undefined", "0");
// this.selectedNodesAr = this.selectedNodesAr.replace("undefined,", "0");

// console.log("SELECTED NODES AR>>>>>>>>>>>>>>>>>>>>>>>>>>>>", this.selectedNodesAr);


// }
// /////////////////////////////////////////////////////////////////////////////////////////////////////HAYA END
//   }




handleAggridJSONRowSelection(primaryKey: any, event: any, selectionType: any)
  {
    let wasGrouped=0;
    if(event.node.allLeafChildren == undefined){
      console.log("yessssssssssssssssssssssssssss",this.informationservice.getIsRowGroup())
      if(this.informationservice.getIsRowGroup() =='1'){
        console.log('#@!#@!#!#@!#@!#!@!#@!#@!#@!#@!#@!')
        wasGrouped=1;
        this.isGrouped = false;

      }
      this.informationservice.setIsRowGroup('0');

    }
    let nbOfPrimaryKeys = this.commonFunction.countNbOfOccuranceStr(primaryKey, ",");
    nbOfPrimaryKeys = Number(nbOfPrimaryKeys + 1);

    let columnVal;
    this.selectedNodesAr += ",{";
      console.log(' this.informationservice.getIsRowGroup() ---->',this.informationservice.getIsRowGroup())
      if(this.isGrouped == true || this.informationservice.getIsRowGroup() =='1')
        {
          console.log('event.node---->',event);
          if(event.node.allLeafChildren != undefined){
            for(let q = 0; q < event.node.allLeafChildren.length; q++)
              {
                for (let i = 0; i < nbOfPrimaryKeys; i++)
                {
                  let columnName: String = primaryKey.split(",")[i];

                    if(this.isGrouped == true  || this.informationservice.getIsRowGroup() =='1')
                      {
                          columnVal = event.node.allLeafChildren[q].data[primaryKey.split(",")[i]] || "0";
                          console.log('columnVal>>>>>>>>>>>>>>>>>',columnVal)
                          this.selectedNodesAr += "\"" + columnName + "\"" + ":" + "\"" + columnVal + "\"" + ", ";
                      }

                  console.log("this.selectedNodesAr = ", this.selectedNodesAr);

                  if ((i + 1) == nbOfPrimaryKeys)
                  {
                      columnVal = event.node.allLeafChildren[q].data[primaryKey.split(",")[i]] || "0".replace("[","");
                      columnVal = columnVal.replace("]","");
                      this.selectedNodesAr += "\"" + columnName + "\"" + ":" + "\"" + columnVal + "\"" + ", ";
                  }
                }
              }


        }
        else
        {
          for (let i = 0; i < nbOfPrimaryKeys; i++)
          {
            let columnName: String = primaryKey.split(",")[i];
            columnVal = event.data[primaryKey.split(",")[i]] || "0".replace("[","");
            columnVal = columnVal.replace("]","");
            this.selectedNodesAr += "\"" + columnName + "\"" + ":" + "\"" + columnVal + "\"" + ", ";

            if ((i + 1) == nbOfPrimaryKeys)
              {
                if (this.rowContSelected <= this.rowCount)
                {
                  this.selectedNodesAr += "\"rowSlectedStatus\":\"update\"";
                  this.selectedNodesAr += "}";
                }
                else
                {
                  this.selectedNodesAr += "}";
                }
              }
          }
        }

    if (this.selectedNodesAr.endsWith(", }]"))
    {
      console.log('selectedNodesAr---111--->',this.selectedNodesAr);
      this.selectedNodesAr = this.selectedNodesAr.replace(", }]", "}]");
      console.log('selectedNodesAr---111--->',this.selectedNodesAr);

    }
  console.log('selectedNodesAr---2221--->',this.selectedNodesAr);
// if(this.isGrouped == true || this.informationservice.getIsRowGroup() =='1' || wasGrouped == 1){

// }
// else{
//     this.selectedNodesAr = this.selectedNodesAr.replace("[", "");
// }
    // if(this.isGrouped == true || this.informationservice.getIsRowGroup() =='1' || wasGrouped == 1){
    //   this.selectedNodesAr = this.selectedNodesAr.replaceAll("\"[", "[");
    //   this.selectedNodesAr = this.selectedNodesAr.replaceAll("]\"", "]");

    // }
    // else{
    //   this.selectedNodesAr = this.selectedNodesAr.replace("]", "");
    // }
    this.selectedNodesAr = "[" + this.selectedNodesAr + "]";

    this.selectedNodesAr = this.selectedNodesAr.replace("undefined,", "0");
    console.log('selectedNodesAr---123222--->',this.selectedNodesAr);

    this.selectedNodesAr = this.selectedNodesAr.replace("undefined", "0");
    console.log('selectedNodesAr---123333--->',this.selectedNodesAr);

    this.selectedNodesAr = this.selectedNodesAr.replace("},]", "}]");
    console.log('selectedNodesAr---123444--->',this.selectedNodesAr);

    this.selectedNodesAr = this.selectedNodesAr.replace("[,{", "[{");
    console.log('selectedNodesAr---1235555--->',this.selectedNodesAr);

    if(this.isGrouped == true)
    {

      for (let i = 0; i < event.node.allLeafChildren.length; i++)
      {
        if(i < event.node.allLeafChildren.length - 1)
        {
          this.selectedNodesAr = this.selectedNodesAr.replace(", }", "},{");
        }
        else
        {
          this.selectedNodesAr = this.selectedNodesAr.replace(", }", "}");
        }
      }
      console.log('selectedNodesAr---444--->',this.selectedNodesAr);

    }
    else
    {
      for(let i = 0; i < this.selectedNodesAr.length; i++)
      {
        this.selectedNodesAr = this.selectedNodesAr.replace(", ,", "},");
      }
    }
  }
    //note that here, the comma is followed by a space for the cases that the json would have ended in ", }]"
    if (this.selectedNodesAr.endsWith(", }]"))
      {
        this.selectedNodesAr = this.selectedNodesAr.replace(", }]", "}]");
        console.log('selectedNodesAr---555--->',this.selectedNodesAr);

      }
      this.selectedNodesAr = this.selectedNodesAr.replaceAll("\"[", "[");
      this.selectedNodesAr = this.selectedNodesAr.replaceAll("]\"", "]");
    if (selectionType == "unselected") {
      this.selectedNodesAr=this.selectedNodesAr.replace(/\s/g,"");

      //console.log("EVENT>>>>>>>>>>>>>",event.data);
      console.log("selectedNodesAr ==== ", this.selectedNodesAr);
      if (this.selectedNodesAr.endsWith(",{"))
      {
        this.selectedNodesAr = this.selectedNodesAr.slice(0, -2);
      }

      //console.log("OLD ARRAY>>>>>",this.selectedNodesAr);

      let selectedNodesJson = JSON.parse(this.selectedNodesAr);
      this.informationservice.getAgGidSelectedNode();
     let commonKeys: { [x: string]: any; };

    if (this.isGrouped == true) {
      if (event.node.data == '' || event.node.data == null || event.node.data == undefined) {

        for (let q = 0; q < event.node.allLeafChildren.length; q++) {
          let commonKeys = Object.keys(event.node.allLeafChildren[q].data).reduce((result: any, key) => {
            if (this.agPrimaryKey.includes(key)) {
              result[key] = event.node.allLeafChildren[q].data[key];
            }
            return result;
          }, {});
          this.commonKeysArray.push(commonKeys); // Push the result of each iteration to the array
        }

        // Apply replacement operations on each element of commonKeysArray
        this.commonKeysArray = this.commonKeysArray.map((commonKeys: any) => {
          let selectedNodesAr = JSON.stringify(commonKeys);
          selectedNodesAr = selectedNodesAr.replace("[", "")
                                           .replace("]", "")
                                           .replace("undefined,", "0")
                                           .replace("undefined", "0")
                                           .replace("},]", "}]")
                                           .replace("[,{", "[{")
                                           .replace(",,", ",")
                                           .replace(",]", "]")
                                           .replace("[,", "[");
          return JSON.parse(selectedNodesAr);
        });
      }
    }

    else
    {
      //console.log("event.node.data");
      commonKeys = Object.keys(event.node.data).reduce((result:any, key) =>
      {
        //console.log("key == ", key)
        if (this.agPrimaryKey.includes(key))
        {
          result[key] = event.node.data[key];
          //console.log("event.node.data[key] = ", event.node.data[key])
        }
      return result;
     }, {});
    }

    console.log("selectedNodesJson = ", selectedNodesJson)
    console.log("this.isGrouped = ", this.isGrouped)
      this.selectedNodesAr = JSON.stringify(selectedNodesJson);
      if (this.isGrouped == true)
      {
        this.selectedNodesAr=this.selectedNodesAr.replaceAll(JSON.stringify(this.commonKeysArray),"");
        console.log("this.commonKeysArray = ", this.commonKeysArray)
        console.log("this.selectedNodesAr = ", this.selectedNodesAr)

        let valuesOfPrimaryKeys = primaryKey.split(",");
        this.value1 = valuesOfPrimaryKeys[0];
        this.value2 = valuesOfPrimaryKeys[1];

        selectedNodesJson = selectedNodesJson.filter((item1: { [key: string]: string }) =>
          !this.commonKeysArray.some((item2: { [key: string]: string }) =>
              item2[this.value1] === item1[this.value1] && item2[this.value2] === item1[this.value2]
          )
      );

        //console.log("selectedNodesJson == ", selectedNodesJson);
        //console.log("valuesOfPrimaryKeys === ", valuesOfPrimaryKeys);
        //console.log("1 this.selectedNodesJson === ", this.selectedNodesAr);
        // this.selectedNodesAr = selectedNodesJson;
        this.selectedNodesAr = JSON.stringify(selectedNodesJson);
        //console.log("2 this.selectedNodesAr === ", this.selectedNodesAr);

      }
      else
      {
        this.selectedNodesAr=this.selectedNodesAr.replaceAll(JSON.stringify(commonKeys),"");
        //console.log("commonKeys = ", commonKeys)
      }

      this.selectedNodesAr = this.selectedNodesAr.replace("[", "")
      this.selectedNodesAr = this.selectedNodesAr.replace("]", "");
      this.selectedNodesAr = "[" + this.selectedNodesAr + "]";
      this.selectedNodesAr = this.selectedNodesAr.replace("undefined,", "0");
      this.selectedNodesAr = this.selectedNodesAr.replace("undefined", "0");
      this.selectedNodesAr = this.selectedNodesAr.replace("},]", "}]");
      this.selectedNodesAr = this.selectedNodesAr.replace("[,{", "[{");
      this.selectedNodesAr = this.selectedNodesAr.replace(",,", ",");
      this.selectedNodesAr = this.selectedNodesAr.replace(",]", "]");
      this.selectedNodesAr = this.selectedNodesAr.replace("[,", "[");

      //console.log("NEW ARRAY>>>>>",this.selectedNodesAr);

      if (this.isGrouped == true)
      {
        this.commonKeysArray = [];
        this.selectedNodesAr=this.selectedNodesAr.replace("[","");
        this.selectedNodesAr=this.selectedNodesAr.replace("]","");
        this.selectedNodesAr="["+this.selectedNodesAr+"]";

      }
      this.selectedNodesAr=this.selectedNodesAr.replace("[","");
      this.selectedNodesAr=this.selectedNodesAr.replace("]","");
      this.selectedNodesAr="["+this.selectedNodesAr+"]";
    }
 //   //console.log("V-GRID LOOKUP GRID SELECTION>>>>>>>>",this.agRowSelection);
//console.log("SELECTED AR NODES BEFORE", this.selectedNodesAr);

 //////////////////////////////////////////////////////////////HAYA START Access Rights (USM)
//  if (this.isAccessRightsGrid){
//   // Remove duplicates based on first primary key only in USM
//   let primaryKeys = primaryKey.split(",");

//   let firstPrimaryKey = primaryKey.split(",")[0];
//   let selectedNodesJson = JSON.parse(this.selectedNodesAr);
//   let uniqueNodesMap = new Map();
//   selectedNodesJson.forEach((node: { [x: string]: any; }) => {
//     uniqueNodesMap.set(node[firstPrimaryKey], node);
// });

// this.selectedNodesAr = JSON.stringify(Array.from(uniqueNodesMap.values()));

// this.selectedNodesAr = this.selectedNodesAr.replace("undefined", "0");
// this.selectedNodesAr = this.selectedNodesAr.replace("undefined,", "0");

// //console.log("SELECTED NODES AR>>>>>>>>>>>>>>>>>>>>>>>>>>>>", this.selectedNodesAr);


// }
/////////////////////////////////////////////////////////////////////////////////////////////////////HAYA END

this.informationservice.setAgGidSelectedNode(this.selectedNodesAr);
    this.selectedNodesAr ="";
  }

  onCellClicked(cellParams: any): void {
    if(this.isAccessRightsGrid){
      console.log("cellParamsssssssssss>>>", cellParams);
      console.log("cellParamsssssssssssagcolumns>>>", cellParams.colDef.field);
  
      this.handleAggridJSONCellSelection(cellParams.colDef.field, cellParams, cellParams.value === '1' ? 'selected' : 'unselected');
  
      const commonKeys = Object.keys(cellParams.data).reduce((result: any, key) => {
        if (this.agPrimaryKey.includes(key)) {
          result[key] = cellParams.data[key];
        }
        return result;
      }, {});
  
      console.log("COMMON KEYS>>>>>>>>>>>>>>", commonKeys);
  
      // Retrieve the existing data
      let existingData = this.informationservice.getAgGidSelectedCell();
      let jsonData: any[] = [];
  
      if (existingData) {
        try {
          jsonData = JSON.parse(existingData);
        } catch (e) {
          console.error("Error parsing existing JSON data", e);
        }
      }
  
      // Update or add the new entry
      const id = commonKeys.id;
      let found = false;
  
      jsonData = jsonData.map((item: any) => {
        if (item.id === id) {
          found = true;
          return commonKeys;
        }
        return item;
      });
  
      if (!found) {
        jsonData.push(commonKeys);
      }
  
      // Save the updated data
      this.informationservice.setAgGidSelectedCell(JSON.stringify(jsonData));
      console.log("this.informationservice.getAgGidSelectedCell", this.informationservice.getAgGidSelectedCell());    }
  }

  handleAggridJSONCellSelection(primaryKey: any, cellParams: any, selectionType: any) {
    let nbOfPrimaryKeys = this.commonFunction.countNbOfOccuranceStr(primaryKey, ",");
    nbOfPrimaryKeys = Number(nbOfPrimaryKeys + 1);

    const rowId = cellParams.data.id;
    const colId = cellParams.colDef.field;
    const cellValue = cellParams.value;

    // Initialize the cell data structure if not already present
    if (!this.selectedCellsAr) {
        this.selectedCellsAr = {};
    }

    if (!this.selectedCellsAr[rowId]) {
        this.selectedCellsAr[rowId] = {};
    }

    if (selectionType === "selected") {
        this.selectedCellsAr[rowId][colId] = cellValue;
    } else {
        delete this.selectedCellsAr[rowId][colId];
        if (Object.keys(this.selectedCellsAr[rowId]).length === 0) {
            delete this.selectedCellsAr[rowId];
        }
    }

    // Convert to JSON format
    let selectedCellsJson: { [key: string]: any }[] = [];
    for (let row in this.selectedCellsAr) {
        let rowData: { [key: string]: any } = { id: row };
        for (let col in this.selectedCellsAr[row]) {
            rowData[col] = this.selectedCellsAr[row][col];
        }
        selectedCellsJson.push(rowData);
    }

    // Convert the JSON array to string
    this.selectedCellsArString = JSON.stringify(selectedCellsJson);

    // Remove duplicates based on the first primary key if in USM
    
        let firstPrimaryKey = primaryKey.split(",")[0];
        let uniqueCellsMap = new Map();

        selectedCellsJson.forEach((cell: { [x: string]: any }) => {
            uniqueCellsMap.set(cell[firstPrimaryKey], cell);
        });

        this.selectedCellsArString = JSON.stringify(Array.from(uniqueCellsMap.values()));
        this.selectedCellsArString = this.selectedCellsArString.replace("undefined", "0");
        this.selectedCellsArString = this.selectedCellsArString.replace("undefined,", "0");
    

    console.log("SELECTED CELLS AR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", this.selectedCellsArString);
    // alert("Cell selection updated");
  }

  onRowSelected(event: any) {
    if(!this.isAccessRightsGrid){
    setTimeout(() => {
      if(this.informationservice.getIsDynamicReport()==true){
        console.log("FETET");
        this.informationservice.setDynamicReportId(this.informationservice.getAgGidSelectedNode());
      }
    }, 1000);

    //console.log("INFORMATION DYNAMIC REPORT ID>>>>>>>>",this.informationservice.getDynamicReportId());
    this.rowContSelected = this.rowContSelected + 1;
    // If primary key is not available, throw exception Primary key is required
    if (this.agPrimaryKey != '') {
      //   throw new Error('Primary Key is required');
      // } else {
      let primaryKey = this.agPrimaryKey;
      if (event.node.selected) {

        // this.gatherAllSelectedRowNodes.push(event.node);
        if (primaryKey != "" && primaryKey != undefined) {
          if (primaryKey.indexOf(",") != -1 && !this.isGridInLookup) {
            this.handleAggridJSONRowSelection(primaryKey, event, 'selected');

            ///elie///////////////
            if(this.agRowSelection=='single' && this.agPrimaryKey.includes(",")){
                const commonKeys = Object.keys(event.node.data).reduce((result:any, key) => {
                if (this.agPrimaryKey.includes(key)) {
                  result[key] = event.node.data[key];
                }
                return result;
              }, {});

              console.log("COMMON KEYS>>>>>>>>>>>>>>",commonKeys);



              this.informationservice.setAgGidSelectedNode("["+JSON.stringify(commonKeys)+"]");


            }else{
          ////////////////////////////////////////////////////
            this.informationservice.setAgGidSelectedNode(this.selectedNodesAr);

          }

            console.log("this.informationservice.setAgGidSelectedNode on row selected :", this.informationservice.getAgGidSelectedNode())
          } else if (primaryKey.indexOf(",") == -1 && this.isGridInLookup) {
            //console.log("EVEnTTT>>>>>>",event);

            if(!event.data["ID"]) {
              this.lookupIds = this.lookupIds + "," + event.data["id"];
              this.lookupNames = this.lookupNames + "," + event.data["name"];
              this.lookupAllNames = this.lookupAllNames + "," + event.data["name"];

            } else {
              this.lookupIds = this.lookupIds + "," + event.data["ID"];
              this.lookupNames = this.lookupNames + "," + event.data["NAME"];
              this.lookupAllNames = this.lookupAllNames + "," + event.data["NAME"];

            }

            this.lookupIds = this.lookupIds.replace("undefined,", "");
            this.lookupNames = this.lookupNames.replace("undefined,", "");
            this.lookupAllNames = this.lookupAllNames.replace("undefined,", "");

            let countSelections = this.commonFunction.countNbOfOccuranceStr(this.lookupIds, ",");
            countSelections = Number(countSelections) + 1;
            if(countSelections!=1){
              this.lookupNames = "Selected (" + countSelections + ")";
            }

            localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id", this.lookupIds);
            localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name", this.lookupNames);
          } else if (primaryKey.indexOf(",") != -1 && this.isGridInLookup) {
            console.log("LESH FETTE LA HON");
            if(!event.data["ID"]) {
              this.lookupIds = this.lookupIds + "," + event.data["id"];
              this.lookupNames = this.lookupNames + "," + event.data["name"];
              this.lookupAllNames = this.lookupAllNames + "," + event.data["name"];

            } else {
              this.lookupIds = this.lookupIds + "," + event.data["ID"];
              this.lookupNames = this.lookupNames + "," + event.data["NAME"];
              this.lookupAllNames = this.lookupAllNames + "," + event.data["NAME"];

            }

            this.lookupIds = this.lookupIds.replace("undefined,", "");
            this.lookupNames = this.lookupNames.replace("undefined,", "");
            this.lookupAllNames = this.lookupAllNames.replace("undefined,", "");

            let countSelections = this.commonFunction.countNbOfOccuranceStr(this.lookupIds, ",");
            countSelections = Number(countSelections) + 1;
            if(countSelections!=1){
              this.lookupNames = "Selected (" + countSelections + ")";
             }
            localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id", this.lookupIds);
            localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name", this.lookupNames);
          } else {
            console.log("FETET LA HON");
            console.log("event-->",event);
            this.informationservice.setSelectedNodeVGrid(event.data);
            if(this.isGrouped == true || this.informationservice.getIsRowGroup() =='1')
            {
              // this.selectedNodes = '';
              this.selectedNodes = this.selectedNodes + "," + event.node.key;
              this.handleAggridJSONRowSelection(primaryKey, event, 'selected');

              console.log("this.informationservice.setAgGidSelectedNode group :", this.informationservice.getAgGidSelectedNode())

            }
            else
            {
              this.selectedNodes = this.selectedNodes + "," + event.data[primaryKey];
            }


            if (this.selectedNodes.charAt(0) === ',') {
              this.selectedNodes = this.selectedNodes.substring(1);
            }

            if (this.selectedNodes == "undefined") {

              this.informationservice.setAgGidSelectedNode(event.data.ROW_ID);
              console.log("this.informationservice.setAgGidSelectedNode undefined :", this.informationservice.getAgGidSelectedNode())

            } else {
              console.log('isGrouped---->',this.isGrouped);
              console.log('isRowGroup---->: ',this.informationservice.getIsRowGroup());
              if(this.informationservice.getIsRowGroup() == 0){
                console.log("!!!!!!!!!!");
              this.informationservice.setAgGidSelectedNode(this.selectedNodes);
              }
              console.log("this.informationservice.setAgGidSelectedNode else :", this.informationservice.getAgGidSelectedNode())


            }
          }
        }
      } else {
        if (primaryKey != "" && primaryKey != undefined) {
          if (primaryKey.indexOf(",") != -1 && !this.isGridInLookup) {
            this.handleAggridJSONRowSelection(primaryKey, event, 'unselected');

             ///elie///////////////
             if(this.agRowSelection=='single' && this.agPrimaryKey.includes(",")){


                }else{


            this.informationservice.setAgGidSelectedNode(this.selectedNodesAr);
            console.log("this.informationservice.setAgGidSelectedNode unselected :", this.informationservice.getAgGidSelectedNode())
 }
          } else if (primaryKey.indexOf(",") && this.isGridInLookup) {

            console.log("EVEnTTT>>>>>>",event.node.selected);

          //  this.lookupIds = this.lookupIds.replace(event.data["id"] + ",", "");
          //  this.lookupIds = this.lookupIds.replace("," + event.data["id"], "");



            if(!event.data["ID"]) {
              this.lookupIds = this.lookupIds + "," + event.data["id"];
              this.lookupNames = this.lookupNames + "," + event.data["name"];
              this.lookupAllNames = this.lookupAllNames + "," + event.data["name"];

            } else {
              if(event.node.selected!=false){
              this.lookupIds = this.lookupIds + "," + event.data["ID"];
              this.lookupNames = this.lookupNames + "," + event.data["NAME"];
              this.lookupAllNames = this.lookupAllNames + "," + event.data["NAME"];
              console.log("NAMEEEE>>>>>>>",this.lookupAllNames);
              }else{
                console.log("LOOKUP ID>>>>",this.lookupIds);
                console.log("LOOKUP NAME>>>>",this.lookupAllNames);
                console.log("EVENTTTTT>>>>",event.data["ID"]);


                this.lookupIds=this.lookupIds.replace(","+event.data["ID"],"");
                this.lookupIds=this.lookupIds.replace(event.data["ID"]+",","");
                this.lookupIds=this.lookupIds.replace(event.data["ID"],"");
                if(this.agRowSelection!='single'){
                this.lookupAllNames=this.lookupAllNames.replace(","+event.data["NAME"],"");
                this.lookupAllNames=this.lookupAllNames.replace(event.data["NAME"]+",","");
                this.lookupAllNames=this.lookupAllNames.replace(event.data["NAME"],"");
                this.lookupAllNames=this.lookupAllNames.replace("undefined,","");
                localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id",this.lookupIds);

                let countLookUpNames=this.lookupAllNames.split(",").length;
                  for(let b=0;b<countLookUpNames;b++){
                    if(countLookUpNames!=1){
                      this.lookupNames = "Selected (" + countLookUpNames + ")";
                     }else
                    if(countLookUpNames==1){
                      this.lookupNames=this.lookupAllNames;

                    }else
                     if(countLookUpNames==0){
                      this.lookupNames='';
                     }
                     localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name",this.lookupNames);

                  }

                }
              }

            }
              // this.lookupIds = this.lookupIds.replace("", "," + event.data["id"]);
            // this.lookupNames = this.lookupNames.replace(event.data["name"] + ",", "");
            // this.lookupNames = this.lookupNames.replace("," + event.data["name"], "");

            let countSelections = this.commonFunction.countNbOfOccuranceStr(this.lookupIds, ",");
            countSelections = Number(countSelections) + 1;

            console.log("COUNT SELECTIONS>>>>>>",countSelections);
            if(this.agRowSelection=='single'){
              console.log("this.agRowSelection : ",this.agRowSelection);
              let lid=localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id");
              let llid=lid.split(",");
              let lenid=llid.length;

              let lname=this.lookupAllNames;
              let llname=lname.split(",");
              let lenname=llname.length;

              console.log("CURRENT>>>",lid);
              console.log("ARRAY>>>",llid);
              console.log("LENGTH>>>>",lenid);
              console.log("TESTT>>>>>",llid[lenid]);
              localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id",llid[lenid-1])

              console.log("CURRENT NAME>>>",lname);
              console.log("ARRAY NAME>>>",llname);
              console.log("LENGTH NAME>>>>",lenname);
              console.log("TESTT NAME>>>>>",llname[lenname-1]);
              console.log("LocalStorage>>>",localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id"));

             console.log("EVENT DATA>>>>>>>",event.data);

             if(!event.data["ID"]){
              console.log("EEEEEEEEEEEEEEEEEEEEEE");
              localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name","Selected (1)");
             }else{
              localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name",llname[lenname-1]);

             }
              // if(!event.data["ID"]){
              // localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name",llname[lenname-2]);
              // }else{
              // localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name",llname[lenname-1]);
              // }
            }else{
            if(countSelections!=1){
              this.lookupNames = "Selected (" + countSelections + ")";
             }
            if(countSelections==1){

              this.lookupNames=this.lookupAllNames;

            }
             if(countSelections==0){
              this.lookupNames='';
             }
            }
          //  localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id", this.lookupIds);
           // localStorage.setItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_name", this.lookupNames);
          } else {

            if(this.isGrouped == true)
              {

                this.selectedNodes = this.selectedNodes.replace("," + event.node.key, "");
                this.selectedNodes = this.selectedNodes.replace(event.node.key + ",", "");
                this.selectedNodes = this.selectedNodes.replace(event.node.key, "");
              }
              else
              {
                this.selectedNodes = this.selectedNodes.replace("," + event.data[primaryKey], "");
                this.selectedNodes = this.selectedNodes.replace(event.data[primaryKey] + ",", "");
                this.selectedNodes = this.selectedNodes.replace(event.data[primaryKey], "");
              }

            // this.selectedNodes = this.selectedNodes.replace("," + event.data[primaryKey], "");
            // this.selectedNodes = this.selectedNodes.replace(event.data[primaryKey] + ",", "");
            // this.selectedNodes = this.selectedNodes.replace(event.data[primaryKey], "");

            if (this.selectedNodes.charAt(0) === ',') {
              this.selectedNodes = this.selectedNodes.substring(1);
            }


            this.informationservice.setAgGidSelectedNode(this.selectedNodes);
            console.log("this.informationservice.setAgGidSelectedNode , :", this.informationservice.getAgGidSelectedNode())

          }
        }
      }
    }
/////////FOR QUERY FORM/////////////////////////////
    if(this.isForQueryForm==true){
      this.informationservice.setDynamicService('agGidSelectedQueryForm_('+this.lookupFieldName+')',this.informationservice.getAgGidSelectedNode());
    }else if(this.isForQueryForm==false && this.isGridInLookup==false){
      if(this.informationservice.getIsRowGroup() == 0){
      this.informationservice.setAgGidSelectedNodeRule(this.selectedNodes);
      }
      console.log("this.selectedNodes :", this.selectedNodes);
      console.log("this.informationservice.setAgGidSelectedNode false :", this.informationservice.getAgGidSelectedNode());
      console.log('agPrimaryKey------->',this.agPrimaryKey)
      if(this.agRowSelection!='single' && this.agPrimaryKey=='ROW_ID'){
      let tempArray=this.informationservice.getAgGidSelectedNode().replaceAll("[","").replaceAll("]","");
     tempArray="["+tempArray+"]";
     console.log("tempArray--->"+tempArray)
      this.informationservice.setAgGidSelectedNode(tempArray);
      }
    }
////////////////////////////////////////////////////
  }}
onCellEditingStopped(event: any) {
  let primaryKey = this.agPrimaryKey.toLowerCase();
  let rowPrimaryKey = event.data[primaryKey];
  let jsonRow = {...event.data }; // create a copy of the original object

  if (typeof (rowPrimaryKey) === "undefined") {
    rowPrimaryKey = "";
  }

  for (let i = 0; i < this.gridModifiedRows.length; i++) {
    if (rowPrimaryKey === this.gridModifiedRows[i][primaryKey]) {
      this.gridModifiedRows[i]["modeType"] = "~toBeRemoved~";
    }
  }

  jsonRow.modeType = "~updateRow~"; // add the new property

  if (rowPrimaryKey!= "") {
    // Condition used to only take into consideration the Updated rows and not the added ones as well
    this.gridModifiedRows.push(jsonRow);
  }
}
  // onCellEditingStopped(event: any) {

  //   let primaryKey = this.agPrimaryKey.toLowerCase();
  //   let rowPrimaryKey = event.data[primaryKey];
  //   let jsonRow: any = JSON.stringify(event.data).slice(0, -1);

  //   if (typeof (rowPrimaryKey) === "undefined") {
  //     rowPrimaryKey = "";
  //   }

  //   for (let i = 0; i < this.gridModifiedRows.length; i++) {
  //     if (rowPrimaryKey === this.gridModifiedRows[i][primaryKey]) {
  //       this.gridModifiedRows[i]["modeType"] = "~toBeRemoved~";
  //     }
  //   }

  //   jsonRow = jsonRow + ',' + '"modeType"' + ":" + '"~updateRow~"' + "}";
  //   jsonRow = JSON.parse(jsonRow);
  //   if (rowPrimaryKey != "") {
  //     // Condition used to only take into consideration the Updated rows and not the added ones as well
  //     this.gridModifiedRows.push(jsonRow);
  //   }
  // }

  addNewAgRow() {
    // Used to add a new line to the grid
    this.gridApi.paginationGoToLastPage();
    const res = this.gridApi.applyTransaction({ add: [{}] })!;
    this.gridAddedRows.push(res.add[0].data);
  }

  deleteAgRow() {
    // Used to delete selected row from the grid
    const selectedData = this.gridApi.getSelectedRows();
    if (selectedData.length > 1) {
      for (let i = 0; i < selectedData.length; i++) {
        this.gridDeletedRows.push(selectedData[i]);
      }
    } else {
      this.gridDeletedRows.push(selectedData);
    }
    this.gridApi.applyTransaction({ remove: selectedData })!;
  }

  onGridEventSaveFn() {
    //fired only on submit
    const updatedData: any[] = this.getGridData();
    this.agGridUpdates.push({ "addList": this.gridAddedRows, "updateList": this.gridModifiedRows, "deleteList": this.gridDeletedRows});
    this.onGridEventSave.emit(this.agGridUpdates);
  }

  agQuickFilter(event: any) {
    const value = (<HTMLInputElement>event.target).value
    if (this.agType == 'clientSide') {
      this.gridApi.setQuickFilter(value);
    }

    if (this.agType == 'serverSide') {

    }
  }

  async fetchGridData(): Promise<any> {


    this.staticData = this.staticData == undefined ? -1 : this.staticData;


    //console.log("STATIC DATA>>>>>>>>>>>",this.staticData);

    if (this.gridApi != undefined) {
      // const { api } = params;
      let agGridRenderedColumns = [];
      if (this.agColumns.length > 0) {

        for (let kk = 0; kk < this.agColumns[0].length; kk++) {
          let definition: ColDef = {

            headerName: this.agColumns[0][kk].headerName,
            field: this.agColumns[0][kk].field,
            // editable: this.agColumns[0][kk].editable,
            editable: this.agColumns[0][kk].editable == null ? false : this.agColumns[0][kk].editable,
            cellRenderer: this.agColumns[0][kk].cellRenderer,
            hide: this.agColumns[0][kk].hide,
            colId: this.agColumns[0][kk].colId,
            type: this.agColumns[0][kk].type,
            valueGetter: this.agColumns[0][kk].valueGetter,
            valueFormatter: this.agColumns[0][kk].valueFormatter,
            refData: this.agColumns[0][kk].refData,
            keyCreator: this.agColumns[0][kk].keyCreator,
            equals: this.agColumns[0][kk].equals,
            checkboxSelection: this.agColumns[0][kk].checkboxSelection,
            toolPanelClass: this.agColumns[0][kk].hidtoolPanelClasse,
            suppressColumnsToolPanel: false,
            columnGroupShow: this.agColumns[0][kk].columnGroupShow,
            icons: this.agColumns[0][kk].icons,
            suppressNavigable: this.agColumns[0][kk].suppressNavigable,
            suppressKeyboardEvent: this.agColumns[0][kk].suppressKeyboardEvent,
            suppressPaste: this.agColumns[0][kk].suppressPaste,
            suppressFillHandle: this.agColumns[0][kk].suppressFillHandle,
            initialHide: this.agColumns[0][kk].initialHide,
            lockVisible: this.agColumns[0][kk].lockVisible,
            lockPosition: this.agColumns[0][kk].lockPosition,
            suppressMovable: this.agColumns[0][kk].suppressMovable,
            valueSetter: this.agColumns[0][kk].valueSetter,
            valueParser: this.agColumns[0][kk].valueParser,
            cellEditor: this.agColumns[0][kk].cellEditor,
            cellEditorParams: this.agColumns[0][kk].cellEditorParams,
            cellEditorSelector: this.agColumns[0][kk].cellEditorSelector,
            cellEditorPopup: this.agColumns[0][kk].cellEditorPopup,
            cellEditorPopupPosition: this.agColumns[0][kk].cellEditorPopupPosition,
            singleClickEdit: this.agColumns[0][kk].singleClickEdit,
            onCellValueChanged: this.agColumns[0][kk].onCellValueChanged,
            onCellClicked: this.agColumns[0][kk].onCellClicked,
            onCellDoubleClicked: this.agColumns[0][kk].onCellDoubleClicked,
            onCellContextMenu: this.agColumns[0][kk].onCellContextMenu,
            // filter: this.agColumns[0][kk].filter,
            filter: this.agColumns[0][kk].filter == null ? true : this.agColumns[0][kk].filter,
            filterParams: this.agColumns[0][kk].filterParams,
            filterValueGetter: this.agColumns[0][kk].filterValueGetter,
            getQuickFilterText: this.agColumns[0][kk].getQuickFilterText,
            floatingFilter: this.agColumns[0][kk].floatingFilter,
            floatingFilterComponent: this.agColumns[0][kk].floatingFilterComponent,
            floatingFilterComponentParams: this.agColumns[0][kk].floatingFilterComponentParams,
            suppressFiltersToolPanel: this.agColumns[0][kk].suppressFiltersToolPanel,
            headerValueGetter: this.agColumns[0][kk].headerValueGetter,
            headerTooltip: this.agColumns[0][kk].headerTooltip,
            headerClass: this.agColumns[0][kk].headerClass,
            headerComponent: this.agColumns[0][kk].headerComponent,
            headerComponentParams: this.agColumns[0][kk].headerComponentParams,
            menuTabs: this.agColumns[0][kk].menuTabs,
            columnsMenuParams: this.agColumns[0][kk].columnsMenuParams,
            suppressMenu: this.agColumns[0][kk].suppressMenu,
            suppressHeaderKeyboardEvent: this.agColumns[0][kk].suppressHeaderKeyboardEvent,
            headerCheckboxSelection: this.agColumns[0][kk].headerCheckboxSelection,
            headerCheckboxSelectionFilteredOnly: this.agColumns[0][kk].headerCheckboxSelectionFilteredOnly,
            chartDataType: this.agColumns[0][kk].chartDataType,
            pinned: this.agColumns[0][kk].pinned,
            initialPinned: this.agColumns[0][kk].initialPinned,
            lockPinned: this.agColumns[0][kk].lockPinned,
            pivot: true,
            initialPivot: this.agColumns[0][kk].initialPivot,
            pivotIndex: this.agColumns[0][kk].pivotIndex,
            initialPivotIndex: this.agColumns[0][kk].initialPivotIndex,
            pivotComparator: this.agColumns[0][kk].pivotComparator,
            enablePivot: true,
            // cellStyle: this.agColumns[0][kk].cellStyle,
            cellStyle: {fontSize: '14px'},
            cellClass: this.agColumns[0][kk].cellClass,
            cellClassRules: this.agColumns[0][kk].cellClassRules,
            cellRendererParams: this.agColumns[0][kk].cellRendererParams,
            cellRendererSelector: this.agColumns[0][kk].cellRendererSelector,
            autoHeight: this.agColumns[0][kk].autoHeight == null ? true : this.agColumns[0][kk].autoHeight,
            wrapText: this.agColumns[0][kk].wrapText,
            enableCellChangeFlash: this.agColumns[0][kk].enableCellChangeFlash,
            suppressCellFlash: this.agColumns[0][kk].suppressCellFlash,
            rowDrag: this.agColumns[0][kk].rowDrag,
            rowDragText: this.agColumns[0][kk].rowDragText,
            dndSource: this.agColumns[0][kk].dndSource,
            dndSourceOnRowDrag: this.agColumns[0][kk].dndSourceOnRowDrag,
            rowGroup: this.agColumns[0][kk].rowGroup,
            initialRowGroup: this.agColumns[0][kk].initialRowGroup,
            rowGroupIndex: this.agColumns[0][kk].rowGroupIndex,
            initialRowGroupIndex: this.agColumns[0][kk].initialRowGroupIndex,
            enableRowGroup: true,
            showRowGroup: this.agColumns[0][kk].showRowGroup,
            enableValue: this.agColumns[0][kk].enableValue,
            aggFunc: this.agColumns[0][kk].aggFunc,
            initialAggFunc: this.agColumns[0][kk].initialAggFunc,
            allowedAggFuncs: this.agColumns[0][kk].allowedAggFuncs,
            // sortable: this.agColumns[0][kk].sortable,
            sortable: this.agColumns[0][kk].sortable == null ? true : this.agColumns[0][kk].sortable,
            sort: this.agColumns[0][kk].sort,
            initialSort: this.agColumns[0][kk].initialSort,
            sortIndex: this.agColumns[0][kk].sortIndex,
            initialSortIndex: this.agColumns[0][kk].initialSortIndex,
            sortingOrder: this.agColumns[0][kk].sortingOrder,
            comparator: this.agColumns[0][kk].comparator,
            unSortIcon: this.agColumns[0][kk].unSortIcon,
            colSpan: this.agColumns[0][kk].colSpan,
            rowSpan: this.agColumns[0][kk].rowSpan,
            tooltipField: this.agColumns[0][kk].tooltipField,
            tooltipValueGetter: this.agColumns[0][kk].tooltipValueGetter,
            tooltipComponent: this.agColumns[0][kk].hitooltipComponentde,
            tooltipComponentParams: this.agColumns[0][kk].tooltipComponentParams,
             width: this.agColumns[0][kk].field == '' ? "25px" : this.agColumns[0][kk].width,
            // initialWidth: this.agColumns[0][kk].initialWidth,
            //  width: this.agColumns[0][kk].width,
            //minimum width of all v-grid elements
              minWidth: this.agColumns[0][kk].minWidth == null ? 100 : this.agColumns[0][kk].minWidth,
              maxWidth: this.agColumns[0][kk].maxWidth,
            // flex: this.agColumns[0][kk].flex,
            flex: this.agColumns[0][kk].flex == null ? 1 : this.agColumns[0][kk].flex,
            initialFlex: this.agColumns[0][kk].initialFlex,
            // resizable: this.agColumns[0][kk].resizable,
            resizable: this.agColumns[0][kk].resizable == null ? true : this.agColumns[0][kk].resizable,
            suppressSizeToFit: true,
            suppressAutoSize: true,

          };
          if (this.agColumns[0][kk].isLink) {
            definition.cellRenderer = LinkCellRenderer;
            definition.cellRendererParams = {
              link: this.agColumns[0][kk].link,
              linkValue: this.agColumns[0][kk].linkValue,
              linkType: this.agColumns[0][kk].linkType,
              linkParameters: this.agColumns[0][kk].linkParameters,
              // accessRightParam: this.accessRights.display
              accessRightParam: 1

            }
          }


          if (this.agColumns[0][kk].isCheckBox) {

            definition.cellRenderer = CheckboxCellRenderer;
            definition.cellRendererParams = {
              isCheckBoxDisabled: this.agColumns[0][kk].isCheckBoxDisabled,
              col_name: this.agColumns[0][kk].field,
              pinned: true,

            }

          }
          agGridRenderedColumns.push(definition);

        }

        this.ColDef = agGridRenderedColumns;
        this.groupDisplayType = 'multipleColumns'
        this.rowModelType = this.agType; // Specify how the data is fetched if from Client Side or Server Side
        this.paginationFlag = this.pagination; // enables pagination in the grid
        this.paginationSize = this.paginationPageSize; // sets 10 rows per page (default is 100)


        let gridParam = this.dataApiParam == '' ? null : this.dataApiParam;

        if (this.staticData == -1) {

          try {
            console.log('dataApi----->',this.dataApi);
            console.log("gridParam---->",gridParam)
            const gridDataApi = gridParam === ""
  ? from(axios.get(this.dataApi))
  : from(axios.post(this.dataApi, gridParam));
console.log('gridDataApi--------->',gridDataApi);
gridDataApi.subscribe(
  response => {
    console.log('Data fetched successfully:', response.data);
  },
  error => {
    console.error('Error fetching data:', error);
    if (error.response) {
      console.error('Status:', error.response.status);
      console.error('Data:', error.response.data);
    }
  }
);
            // console.log("data apiiiiiiiiiiiiiiiii isssssssss::::",axios.get(this.dataApi));
            // const gridDataApi = gridParam == "" ? from(axios.get(this.dataApi)) : from(axios.post(this.dataApi, gridParam));
            // // const gridDataApi = from(axios.post(this.dataApi, gridParam));
            const gridData = await lastValueFrom(gridDataApi);

            if (this.rowModelType == 'clientSide') {

              if (localStorage.getItem('agGidSelectedLookup_(' + this.lookupFieldName + ')_id') === null || localStorage.getItem('agGidSelectedLookup_(' + this.lookupFieldName + ')_id') === '') {

              } else {
                let rowdata2: any[] = [];
                let tablesselected: any;
                rowdata2 = gridData.data;
                tablesselected = JSON.parse(localStorage.getItem('agGidSelectedLookup_(' + this.lookupFieldName + ')_id'));

               // console.log("TABLE ROW SELECTED>>>>>>>>>>",tablesselected);

                gridData.data.forEach((data: any) => {
                  //checking if the id's number and type values are the same before putting it on top
                  //after submission
                  if (Number(data.id) === Number(tablesselected)) {
                    this.checkedrowData.push(data);

                 // console.log("CHECKED ROW DATA>>>>>>>>>>",this.checkedrowData);

                    let x = rowdata2.filter((a: any) => a !== data);
                    rowdata2 = x;

                  }
                });


                gridData.data = [...this.checkedrowData, ...rowdata2];
              }
            //  console.log("AAAAAA>>>>>>>>",gridData.data);
              this.rowData = gridData.data;

            }
          } catch (error) {
          }
        } else {
          this.rowData = this.staticData;
        }
        if (localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id") != null || this.informationservice.getAgGidSelectedNode() != "") {
          let agGidSelectedNodesData = "";
          if (this.isGridInLookup) {
            agGidSelectedNodesData = localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id");

          }

          if (!this.isGridInLookup) {
            if (this.informationservice.getAgGidSelectedNode() != "" && this.informationservice.getAgGidSelectedNode() != "undefined") {
              let val = this.informationservice.getAgGidSelectedNode();
              val = val.replace(" = ", "=");
              val = val.replace("= ", "=");
              val = val.replace("=", "=");
              agGidSelectedNodesData = val;

              // agGidSelectedNodesData = JSON.parse(val);
              // this.informationservice.setAgGidSelectedNode('');
            }
          }

          if (agGidSelectedNodesData != "") {

            let res1: any[] = [];

            // console.log("PRIMARY >>>>>>>>>>>>>>>",this.agPrimaryKey);

            if (this.agPrimaryKey != "" && this.agPrimaryKey != undefined) {

              if (this.agPrimaryKey.indexOf(",") != -1) {
                const agPrimaryKeyAr = this.agPrimaryKey.split(",");
                for (let i = 0; i < this.rowData.length; i++) {
                  let checkX = 0;
                  for (let x = 0; x < agPrimaryKeyAr.length; x++) {
                    for (let j = 0; j < agGidSelectedNodesData!.length; j++) {
                      if (this.rowData[i][agPrimaryKeyAr[x]] == agGidSelectedNodesData![j][agPrimaryKeyAr[x]]) {
                        checkX += 1;
                        if (Number(checkX) == Number(this.commonFunction.countNbOfOccuranceStr(this.agPrimaryKey, ",") + 1)) {
                          let dataa = { id: i, data: this.rowData[i] };
                          res1.push(dataa);
                        }
                      }
                    }
                  }

                  if (i == this.rowData.length - 1) {
                    for (let u = 0; u < res1.length; u++) {
                      for (let uu = 0; uu < agGidSelectedNodesData.length; uu++) {
                        if (JSON.stringify(res1[u].data) == JSON.stringify(agGidSelectedNodesData[uu])) {
                          this.rowCount = this.rowCount + 1;
                          console.log("ROW NODE111>>>>>>>",this.gridApi.getRowNode(res1[u].id));
                          this.gridApi.getRowNode(res1[u].id).setSelected(true);
                        }
                      }
                    }
                  }
                }
              } else {

                if (this.isGridInLookup) {

                  for (let i = 0; i < this.rowData.length; i++) {

                    if (agGidSelectedNodesData.indexOf(",") != -1) {
                      console.log("ROW SELECTED GRID111111>>>>>>>",agGidSelectedNodesData);
                      let checkX = 0;

                      let lookupIds = agGidSelectedNodesData.split(",");

                      for (let j = 0; j < lookupIds.length; j++) {
                        // console.log("LOOKUP IDS>>>>>>",lookupIds);
                        // console.log("LOOKUP IDS ROW DATA>>>>>>",this.rowData[i]);
                        // console.log("AG PRIMARY KEY>>>>>>",this.agPrimaryKey);

                        // if(this.rowData[i].id!=undefined ||this.rowData[i].id!=''){

                        //   if (this.rowData[i][this.agPrimaryKey] == lookupIds[j][this.agPrimaryKey]) {

                        //     console.log("FETETTTTTT");

                        //     checkX += 1;
                        //     if (Number(checkX) == Number(this.commonFunction.countNbOfOccuranceStr(this.agPrimaryKey, ",") + 1)) {
                        //       let dataa = { id: i, data: this.rowData[i] };
                        //       res1.push(dataa);
                        //     }

                        //   }

                        // }else {
                        //  console.log("IDDD>>>>>>>>>",this.rowData[i].id);
                        //  console.log("LOOKUP IDDD>>>>>>>>>",lookupIds[j]);


                          if (this.rowData[i].id == lookupIds[j]) {
                          // console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", agGidSelectedNodesData)
                           // console.log("FETETTTTTT");

                            checkX += 1;
                            if (Number(checkX) == Number(this.commonFunction.countNbOfOccuranceStr(this.agPrimaryKey, ",") + 1)) {
                              let dataa = { id: i, data: this.rowData[i] };
                              res1.push(dataa);
                            }

                      //    }



                      let rowdata2: any[] = [];
                      let tablesselected: any;
                      rowdata2 = this.staticData;



                      if((!localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes("[") || !localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes("{")) && localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").includes(",")){
                        tablesselected=localStorage.getItem("agGidSelectedLookup_(" + this.lookupFieldName + ")_id").split(",")

                       // console.log("MULTIPLE RENDERED TABLE SELECTION >>>>>>>",tablesselected);
                        // params.api.forEachNode((rowNode: any, index: any) => {
                        // //  console.log("MULTIPLE RENDERED ROW NODE>>>>",rowNode.data);

                        //   if (rowNode.data.id == tablesselected[i] || rowNode.data.ID==tablesselected[i]) {

                        //    // console.log("MULTIPLE ROW NODEE 222>>>>",rowNode);
                        //     rowNode.setSelected(true);
                        //   }
                      //  });

                      //tablesselected = JSON.parse(localStorage.getItem('agGidSelectedLookup_(' + this.lookupFieldName + ')_id'));
                   //   console.log("TABLE ROW SELECTED>>>>>>>>>>",tablesselected);


                    for(let i=0;i<tablesselected.length;i++){



                  setTimeout(() => {


                  this.staticData.forEach((data: any) => {
                        if (data.ID == tablesselected[i]) {
                          this.checkedrowData.push(data);
                          let x = rowdata2.filter((a: any) => a !== data);
                          rowdata2 = x;
                          console.log("CHECKED ROW DATA>>>>>>>>>>",data);




                        }
                      });

                    }, 500);

                    }

                   setTimeout(() => {



                    this.staticData='';

                    const uniqueArray = this.removeDuplicates(this.checkedrowData, 'id');

                      this.staticData = [...uniqueArray, ...rowdata2];
                      // console.log("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                     // this.staticData = this.checkedrowData.concat(rowdata2);


                          const transaction = {
                            add: this.staticData
                          };

                          this.gridApi.applyTransaction(transaction);

                          this.rowData=this.staticData;
                          console.log("SSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                      this.rowCount = this.rowCount + 1;
                    }, 700);



                      }
                        }

                      }
                    } else {
                      if (this.rowData[i][this.agPrimaryKey] == agGidSelectedNodesData) {

                        this.staticData=this.rowData;
                        let rowdata2: any[] = [];
                        let tablesselected: any;
                        rowdata2 = this.staticData;
                        tablesselected = JSON.parse(localStorage.getItem('agGidSelectedLookup_(' + this.lookupFieldName + ')_id'));
                        console.log("TABLE ROW SELECTED>>>>>>>>>>", tablesselected);
                        console.log("ROW DATA BEFORE FILTERING>>>>>>>>>>", this.staticData); //


                        this.staticData.forEach((data: any) => {
                          console.log("CHECKING DATA.ID>>>>>>>>>>", data.id);  //
                          if (data.id == tablesselected || data.ID == tablesselected) {
                            this.checkedrowData.push(data);


                            let x = rowdata2.filter((a: any) => a !== data);
                            rowdata2 = x;



                          }

                        });

                        if (this.isReadOnly) {
                          this.staticData = this.rowData
                          tablesselected = JSON.parse(localStorage.getItem('agGidSelectedLookup_(' + this.lookupFieldName + ')_id'));

                          // console.log(this.checkedrowData, "REAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADDDDDDDDDDDDD")
                          this.staticData = this.checkedrowData
                          // console.log("OOOOOOOOOOOOOOOOOOOOONNNNNNNNNNLLLLLLLLLYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY")
                        } else {

                        this.staticData = [...this.checkedrowData, ...rowdata2];
                        }
                        // console.log("STATIC DATA 2>>>>>>>>>>",this.staticData);

                            const transaction = {
                              add: this.staticData
                            };

                            this.gridApi.applyTransaction(transaction);

                            this.rowData=this.staticData;

                        this.rowCount = this.rowCount + 1;

                       // console.log("ROW SELECTED GRID22222>>>>>>>",agGidSelectedNodesData);
                       // console.log("GRID APIIIIIII1111>>>>>>>",this.gridApi);
                        //console.log("GRID APIIIIIII22222>>>>>>>",this.gridApi.getRowNode(i.toString()));

                       // this.gridApi.getRowNode(i.toString()).setSelected(true);

                      }
                    }
                    }
                  }
                }
                if (this.rowModelType != 'serverSide' && this.rowModelType != 'clientSide') {
                  throw new Error("Invalid rowModelType, it must be 'serverSide' or 'clientSide'");
                }
                if (this.rowModelType != 'serverSide' && this.rowModelType != 'clientSide') {
                  throw new Error("Invalid rowModelType, it must be 'serverSide' or 'clientSide'");
                }
              }
            }

        }
}
    }
  }

  afterGuiAttached(params?: IAfterGuiAttachedParams): void { }
  getGridData(): any[] {
    const displayedRowCount = this.gridApi.getDisplayedRowCount();
    const gridData: any[] = [];

    for (let i = 0; i < displayedRowCount; i++) {
      const rowNode = this.gridApi.getDisplayedRowAtIndex(i);
      gridData.push(rowNode.data);
    }

    this.paramsChange.emit(gridData);
    // Emit the updated params back to the sender//container
    return gridData;
  }

  changeGridPagination(): any {
    let value = this.paginationChange.controls["value"]?.value;
    if (value != undefined && value != null && value != '') {
      this.paginationPageSize = this.commonFunction.filterArrayById(this.paginationSizeList, value)[0].name;
    } else {
      this.pagination=true;
      this.paginationPageSize = 20;
    }
    this.fetchGridData();
  }
  customSort() {
    // Sort the rowData based on checkbox selections.
    this.rowData.sort((a: { selected: any; }, b: { selected: any; }) => {
      if (a.selected && !b.selected) return 1;
      if (!a.selected && b.selected) return -1;
      return 0;
    });
  }

  onDownloadClick() {
    this.onGridDownload.emit();
  }


   getMainMenuItems(
    params: GetMainMenuItemsParams
  ): (string | MenuItemDef)[] {
    // you don't need to switch, we switch below to just demonstrate some different options
    // you have on how to build up the menu to return
    const MenuItems: (
      | MenuItemDef
      | string
    )[] = params.defaultItems.slice(0);
    MenuItems.push({
      name: 'Set RowGroup',
      action: () => {
            // Row grouping logic
      const column = params.column;
      if (column) {
        const colId = column.getColId();
        const isAlreadyGrouped = params.columnApi.getRowGroupColumns().some((groupedColumn: { getColId: () => any; }) => groupedColumn.getColId() === colId);
        if (!isAlreadyGrouped) {
          const columnToGroup = params.columnApi.getColumns().find((col: { getColId: () => any; }) => col.getColId() === colId);
          if (columnToGroup) {
            params.columnApi.addRowGroupColumns([columnToGroup]);
            // params.columnApi.setColumnVisible(colId, false);
          }
        }
      }
      },
    });
    return MenuItems;
  }

  removeDuplicates(array: any[], property: string): any[] {
    return array.filter((obj, index, self) =>
      index === self.findIndex((o) => o[property] === obj[property])
    );
  }


  onRowDoubleClicked(event: any) {
    if((this.isForQueryForm || this.isGridInLookup) && this.agRowSelection!='multiple'){
        $("#lookupSubmitButton")[0].click();
      }
  }

  updateColsdef(){
    this.fetchGridData();
    console.log("this.colDef<<>>",this.rowData);
    
    this.gridApi.setRowData(this.rowData);
  }

}


