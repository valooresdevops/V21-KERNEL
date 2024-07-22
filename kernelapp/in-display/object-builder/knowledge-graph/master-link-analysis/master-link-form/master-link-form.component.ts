
import { Component, OnInit, ElementRef, ChangeDetectorRef, ViewChild , Renderer2   } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, ReactiveFormsModule, FormsModule  } from '@angular/forms';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { HttpClient } from '@angular/common/http';
import { DataService } from 'src/app/Kernel/services/data.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { Router } from '@angular/router';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { numberFormat } from 'highcharts';
import { ICellRendererParams, IAfterGuiAttachedParams } from 'ag-grid-community';
import { VLookupComponent } from 'src/app/Kernel/components/v-input/v-lookup/v-lookup.component';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import * as $ from 'jquery';



@Component({
  selector: 'app-master-link-form',
  templateUrl: './master-link-form.component.html',
  styleUrl: './master-link-form.component.css'
})
export class MasterLinkFormComponent implements OnInit {

  @ViewChild('titleInput', { static: false }) titleInput: ElementRef<HTMLInputElement>;
  titleValue: string;
 
   public actionType: string = 'add';
  public gridId: any;
  public isUpdate: boolean = false;
  public getQueryName = GlobalConstants.getQueryNameApi;
  public getRowDate = GlobalConstants.rowSourceApi + localStorage.getItem("agGidSelectedLookup_(query)_id");
  gridForm = new UntypedFormGroup({
    title: new UntypedFormControl(''),
    query: new UntypedFormControl(''),

  });

  public isReload : Boolean = true;
  public isVisible : Boolean = false;
  public selectedLookupId: Number;

  public allColumnsAr: any[] = [{ id: '', name: '' }];

  //grid builder variables
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public agColumns2: AgColumns[] = [];
  public agColumnsJson2: any;
  public agColumns3: AgColumns[] = [];
  public agColumnsJson3: any;
  public agColumns4: AgColumns[] = [];
  public agColumnsJson4: any;
  public agGridSelectedNodes: any = '';
  public getGridData = GlobalConstants.getGridDataApi;
  public action: any;
  frameworkComponents: any;
  public getQbeQueryApi  = GlobalConstants.fetchQbeMappingApi;

  public isQueryexecute: boolean;
  headerNames: any[] = [];
  public test = '';
  public shouldReloadGrid: boolean = false;

  //json data variables 
  public jsonNodes : string = "\"nodes\":[" +
                "{" + "\"" + "nodeName" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "field" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "image" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "height" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "width" + "\"" + ":" + "\"" + "" + "\"" + "}" + "]";
  public jsonLink : string = "\"link\":[" +
                "{" + "\"" + "linkName" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "color" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "linkedFrom" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "linkedTo" + "\"" + ":" + "\"" + "" + "\"" + "," +
                "\"" + "label" + "\"" + ":" + "\"" + "" + "\"" + "}" + "]";
  public jsonNodeInfo : string = "\"nodeInfo\":[" +
    "{" + "\"" + "infoName" + "\"" + ":" + "\"" + "" + "\"" + "," +
    "\"" + "field" + "\"" + ":" + "\"" + ""+ "\"" + "," +
    "\"" + "linkedTo" + "\"" + ":" + "\"" + "" + "\"" + "}" + "]";
  public jsonLinkInfo : string = "\"linkInfo\":[" + 
    "{" + "\"" + "infoName" + "\"" + ":" + "\"" + "" + "\"" + "," +
    "\"" + "field" + "\"" + ":" + "\"" + "" + "\"" + "," +
    "\"" + "source" + "\"" + ":" + "\"" + "" + "\"" + "," +
    "\"" + "target" + "\"" + ":" + "\"" + "" + "\"" + "}" + "]";
  public jsonRes : string = '';
  public jsonMasterLink: string;

  //Counters for data displaying
  public countEvent1 : number = 0;
  public countEvent2 : number = 0;
  public countEvent3 : number = 0;
  public countEvent4 : number = 0;
  public countEvent5 : number = 0;

  constructor(
    public commonFunctions: CommonFunctions,
    private router: Router,
    private dataservice: DataService,
    private http: HttpClient,
    private dialog: MatDialog,
    public informationservice: InformationService,
    private elementRef: ElementRef,
    private cdRef: ChangeDetectorRef, 
    private renderer: Renderer2) 
    {
      this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
     }
    //  public conditions = [
    //   { id: '', name: '' },
    //   { id: 1, name: '=' },
    //   { id: 2, name: '<' },
    //   { id: 3, name: '>' },
    //   { id: 4, name: '<=' },
    //   { id: 5, name: '>=' },
    //   { id: 6, name: '< sysdate' },
    //   { id: 7, name: '> sysdate' },
    //   { id: 8, name: '!=' },
    //   { id: 9, name: 'included' },
    //   { id: 10, name: '!included' },
    //   // { id: 8, name: 'Get sysdate (Year)' },
    //   // { id: 8, name: 'Get sysdate (Month)' },
    //   // { id: 8, name: 'Get sysdate (Day)' },
  
    // ];

  ngOnInit(): void 
  {
    this.actionType = this.dataservice.getactionType();
    if (this.actionType == 'update') {
      this.gridId = this.informationservice.getAgGidSelectedNode();
      this.isUpdate = true;

      this.http.get<any>(GlobalConstants.selectGridApi + this.gridId, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          if (res.status == 'Fail') {
          } else {
            this.gridForm.get("title").setValue(res[0].gridName)
            this.gridForm.get("query").setValue(res[0].query)
          }
        })
    }
    
    this.agColumnsJson = [
      {
        headerName: 'Node Name',
        field: 'nodeName',
        editable: true,
        // width: '20px',
      },
      // {
      //   headerName: 'Condition',
      //   field: 'condition',
      //   width: 110,
      //   cellEditor: 'agRichSelectCellEditor',
      //   cellRenderer: DropdownCellRenderer,
      //   keyCreator: (params: any) => {
      //     console.log("watchhhhhhhhhhhhhhh",params.value.name);
      //     return params.value.name;
          
      //   },
      //   cellEditorParams: {
      //     cellRenderer: DropdownCellRenderer,
      //         values: this.headerNames,
      //     isSearchable: true,
      //   },
      //   editable: true,
      //   cellDataType: true
      // },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      // {
      //   headerName: 'Field',
      //   field: 'field',
      //   width: 110,
      //   cellEditor: 'agRichSelectCellEditor',
      //   cellRenderer: DropdownCellRenderer,
      //   keyCreator: (params: any) => {
      //     return params.value.name;
      //   },
      //   cellEditorParams: {
      //     cellRenderer: DropdownCellRenderer,
      //     values: this.allColumnsAr,
      //     isSearchable: true,
      //   },
      //   editable: true,
      //   cellDataType: true
      // },
      {
        headerName: 'Image',
        field: 'image',
        editable: true,
         cellEditor: 'agSelectCellEditor'
        // width: '15px',
      },
      {
        headerName: 'Height',
        field: 'height',
        editable: true,
        cellEditor: "agNumberCellEditor",
      },

      {
        headerName: 'Width',
        field: 'width',
        editable: true,
        cellEditor: "agNumberCellEditor",
        // width: '20px'
      }, 

    ];

    this.agColumns.push(this.agColumnsJson);
// this.updateAgColumnsJson;
    this.agColumnsJson2 = [
      {
        headerName: 'Link Name',
        field: 'linkName',
        editable: true,
        // width: '20px',
      },
      {
        headerName: 'Color',
        field: 'color',
        cellEditor: 'agSelectCellEditor',
        editable: true,
        // width: '25px',
      },
      {
        headerName: 'Linked From',
        field: 'linkedFrom',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      
      // 
      {
        headerName: 'Linked To',
        field: 'linkedTo',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },

      {
        headerName: 'Label',
        field: 'label',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },

    ];
    this.agColumns2.push(this.agColumnsJson2);
    
    // hawne mn balesh grid3 
    this.agColumnsJson3 = [
      {
        headerName: 'Info Name',
        field: 'infoName',
        editable: true,
        // width: '20px',
      },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      {
        headerName: 'Linked To',
        field: 'linkedTo',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      }
    ];
    this.agColumns3.push(this.agColumnsJson3);
    

    // hawne mn balesh grid4
    this.agColumnsJson4 = [
      {
        headerName: 'Info Name',
        field: 'infoName',
        editable: true,
        // width: '20px',
      },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      {
        headerName: 'Source',
        field: 'source',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      
      // 
      {
        headerName: 'Target',
        field: 'target',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      }
    ];
    this.agColumns4.push(this.agColumnsJson4);

    // this.hideButtons();
  }
  // hideButtons() {
  //   $('.defaultBtn').each(function() {
  //     // Set display to none
  //     $(this).css('display', 'none');
  //   });
  // }
  updateAgColumnsJson(): void {

    console.log("we entereddddd the update function");  
    console.log("headerrrrrr  names are eeeeeeeee" ,this.headerNames);
    this.agColumnsJson = [
      {
        headerName: 'Node Name',
        field: 'nodeName',
        editable: true,
      },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      {
        headerName: 'Image',
        field: 'image',
        editable: true,
        cellEditor: 'agSelectCellEditor'
      },
      {
        headerName: 'Height',
        field: 'height',
        editable: true,
        cellEditor: "agNumberCellEditor",
      },
      {
        headerName: 'Width',
        field: 'width',
        editable: true,
        cellEditor: "agNumberCellEditor",
      }
    ];
  
    this.agColumns = []; // Update agColumns with the new configuration
    this.agColumns.push(this.agColumnsJson);


    this.agColumnsJson2 = [
      {
        headerName: 'Link Name',
        field: 'linkName',
        editable: true,
        // width: '20px',
      },
      {
        headerName: 'Color',
        field: 'color',
        cellEditor: 'agSelectCellEditor',
        editable: true,
        // width: '25px',
      },
      {
        headerName: 'Linked From',
        field: 'linkedFrom',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      
      // 
      {
        headerName: 'Linked To',
        field: 'linkedTo',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },

      {
        headerName: 'Label',
        field: 'label',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },

    ];
    this.agColumns2 = [];
    this.agColumns2.push(this.agColumnsJson2);

    if(this.isVisible){
      this.agColumnsJson3 = [
        {
          headerName: 'Info Name',
          field: 'infoName',
          editable: true,
          // width: '20px',
        },
        {
          headerName: 'Field',
          field: 'field',
          width: 110,
          cellEditor: 'agSelectCellEditor',
          cellRenderer: DropdownCellRenderer,
          keyCreator: (params: any) => {
            return params.value;
          },
          cellEditorParams: {
            cellRenderer: DropdownCellRenderer,
            values: this.headerNames,
            isSearchable: true,
          },
          editable: true,
          cellDataType: true,
          cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
        },
        {
          headerName: 'Linked To',
          field: 'linkedTo',
          width: 110,
          cellEditor: 'agSelectCellEditor',
          cellRenderer: DropdownCellRenderer,
          keyCreator: (params: any) => {
            return params.value;
          },
          cellEditorParams: {
            cellRenderer: DropdownCellRenderer,
            values: this.headerNames,
            isSearchable: true,
          },
          editable: true,
          cellDataType: true,
          cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
        }
      ];
      this.agColumns3 = [];
      this.agColumns3.push(this.agColumnsJson3);
      
  
      // hawne mn balesh grid4
      this.agColumnsJson4 = [
        {
          headerName: 'Info Name',
          field: 'infoName',
          editable: true,
          // width: '20px',
        },
        {
          headerName: 'Field',
          field: 'field',
          width: 110,
          cellEditor: 'agSelectCellEditor',
          cellRenderer: DropdownCellRenderer,
          keyCreator: (params: any) => {
            return params.value;
          },
          cellEditorParams: {
            cellRenderer: DropdownCellRenderer,
            values: this.headerNames,
            isSearchable: true,
          },
          editable: true,
          cellDataType: true,
          cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
        },
        {
          headerName: 'Source',
          field: 'source',
          cellEditor: 'agSelectCellEditor',
          cellRenderer: DropdownCellRenderer,
          keyCreator: (params: any) => {
            return params.value.name;
          },
          cellEditorParams: {
            cellRenderer: DropdownCellRenderer,
            values: this.headerNames,
            isSearchable: true,
          },
          editable: true,
          cellDataType: true,
          cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
        },
        
        // 
        {
          headerName: 'Target',
          field: 'target',
          cellEditor: 'agSelectCellEditor',
          cellRenderer: DropdownCellRenderer,
          keyCreator: (params: any) => {
            return params.value.name;
          },
          cellEditorParams: {
            cellRenderer: DropdownCellRenderer,
            values: this.headerNames,
            isSearchable: true,
          },
          editable: true,
          cellDataType: true,
          cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
        }
      ];
      this.agColumns4 = [];
      this.agColumns4.push(this.agColumnsJson4);
    }

    // hawne mn balesh grid3 
   

    console.log("this is agcolums1",this.agColumns);
    console.log("this is agcolums2",this.agColumns2);

  }

  onDropdownChange(event: any): void {
    
      this.isReload=false;
    
    this.test = GlobalConstants.inDispGatewat + 'api/getAllQueriesHeaderList';
    
    this.http.get<any[]>(GlobalConstants.getAllQueriesHeaderList + event, { headers: GlobalConstants.headers })
      .subscribe(response => {
        if (response && Array.isArray(response)) {
          this.headerNames = response.map(header => header.field);
          this.updateAgColumnsJson();
          this.cdRef.detectChanges(); // trigger change detection
  
          // Add a small timeout before triggering grid reload
          setTimeout(() => {
            const gridReloadEvent = new Event('reloadGrid', { bubbles: true });
            document.getElementById('gridReload')?.dispatchEvent(gridReloadEvent);
          }, 1000); // Adjust timeout duration as needed
          this.isReload = true;
        }
      });
      // this.selectedLookupId = this.getLastAccessedValue();
  }
  

  // Method to handle grid reload event if needed
  onGridReload(event: Event): void {
    // Handle any specific actions needed when the grid reloads
    console.log('Grid is reloading...');
  }
  // getLastAccessedValue(): Number {
  //   // Logic to find and return the last accessed value
  //   // For example, searching for a key pattern like 'agGidSelectedLookup_..._id'
  //   const keys = Object.keys(localStorage);
  //   let lastValue = null;

  //   keys.forEach(key => {
  //     if (key.startsWith('agGidSelectedLookup_') && key.endsWith('_id')) {
  //       // Assuming the value is a string, parse or use as needed
  //       lastValue = localStorage.getItem(key);
  //     }
  //   });

  //   return lastValue;
  // }

  // onSubmit(): void {
  //   alert(1111111);
  //   const queryControl = this.gridForm.get('query');
  //   if (queryControl && queryControl.value) {
  //     const selectedValue = queryControl.value; // get the selected value from the v-lookup
  //    console.log("selected Value---->",selectedValue)
  //     this.http.get<any>(GlobalConstants.getAllQueriesHeaderList + selectedValue, { headers: GlobalConstants.headers })
  //       .subscribe(response => {
  //         console.log(`We submitted: ${response}`);
  //         // do something with the response data
  //       });
  //   }
  // }
  // onSubmit(): void {
  //   const queryControl = this.gridForm.get('query');
  //   if (queryControl && queryControl.value) {
  //     const id = queryControl.value; // assuming the selected value is the ID
  //     this.http.get<any>(GlobalConstants.getAllQueriesHeaderList + id, { headers: GlobalConstants.headers })
  //      .subscribe(response => {
  //         console.log(`We submitteddddddddd: ${response}`);
  //         this.responseData = response;
  //       });
  //   }
  // }

  // openLookupDialog(): void {
  //   const dialogRef = this.dialog.open(VLookupComponent, {
  //     width: '700px',
  //     data: [
  //       {
  //         label: 'Source Query',
  //         lookupFieldName: 'query',
  //         lookupDataId: 'getAllQueriesHeaderList',
  //         lookupStaticData: '-1',
  //         lookupSelection: 'ingle',
  //         readonly: false
  //       }
  //     ]
  //   });

  //   dialogRef.afterClosed().subscribe(result => {
  //     if (result === 'closed') {
  //       this.onSubmit(); // Call the onSubmit function here
  //     }
  //   });
  // }
  
  // responseData: any;
  onAddClickNodes() {
    // const dialogConfig = new MatDialogConfig();
    // dialogConfig.width = '700px';
    // dialogConfig.height = '700px';

    // const dialogRef = this.dialog.open(MasterLinkFormComponent, {
    //   // data: info,
    //   width: '70%',
    //   height: '70%',
    // });
  }
  onRunButtonClick(e: any) 
  {
    //  GRID BUILDER
  }

  onUpdateClickNodes() 
  {
    // grid builder
  }
  onDeleteClickNodes() 
  {
    // grid builder
  }
  addInfo() 
  {
    this.agColumnsJson3 = [
      {
        headerName: 'Info Name',
        field: 'infoName',
        editable: true,
        // width: '20px',
      },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      {
        headerName: 'Linked To',
        field: 'linkedTo',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      }
    ];
    this.agColumns3 = [];
    this.agColumns3.push(this.agColumnsJson3);
    

    // hawne mn balesh grid4
    this.agColumnsJson4 = [
      {
        headerName: 'Info Name',
        field: 'infoName',
        editable: true,
        // width: '20px',
      },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      {
        headerName: 'Source',
        field: 'source',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      
      // 
      {
        headerName: 'Target',
        field: 'target',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.headerNames,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      }
    ];
    this.agColumns4 = [];
    this.agColumns4.push(this.agColumnsJson4);
    console.log("this is agcolums3",this.agColumns3);
    console.log("this is agcolums4",this.agColumns4);
    this.isVisible=true;
  }
  async jsonBuilder(){
    this.jsonRes = "";
    this.jsonRes +="{\"masterLink\":"+ this.jsonMasterLink + "," + this.jsonNodes + "," + this.jsonLink + "," + this.jsonNodeInfo + "," + this.jsonLinkInfo + "}"
    console.log("this is final jsonResssssssss ::::::::", this.jsonRes);
    try {
              const gridEventSaveApi = from(axios.post(GlobalConstants.masterLinkData, JSON.parse(this.jsonRes)));
              const gridEventSave = await lastValueFrom(gridEventSaveApi);
              this.commonFunctions.alert("alert", gridEventSave.data.description);
          } catch (error) {
              console.log("gridEventSave error >>> ", error);
          }
  }
  async save() {
    let check : boolean;
    if(!this.isVisible) {
      check = false;
    } else check=true;
    console.log('Save clicked');
    this.countEvent1 = this.countEvent2 = this.countEvent3 = this.countEvent4 = this.countEvent5 =0;
    $('.defaultBtn').each(function() {
      $(this).trigger('click');
    });
    if(!check) this.isVisible = false;
this.gridEventSave5();
await this.jsonBuilder();
  }
  onAddClickLinks()
  {

  }
  onUpdateClickLinks()
  {

  }
  onDeleteClickLinks()
  {

  }
  // submit() {
  //   if (this.actionType == 'update') {
  //     let allData = {
  //       gridId: this.informationservice.getAgGidSelectedNode(),
  //       userId: this.informationservice.getLogeduserId(),
  //       gridName: this.gridForm.get("title").value,
  //       query: localStorage.getItem('agGidSelectedLookup_(query)_id'),
  //     }

  //     this.http.post<any>(GlobalConstants.updateGrid, allData, { headers: GlobalConstants.headers }).subscribe(
  //       (res: any) => {
  //         this.commonFunctions.reloadPage("/dsp/gridBuilder");
  //         this.commonFunctions.navigateToPage("/dsp/gridBuilder");
  //       })
  //   } else {
  //     let allData = {
  //       gridName: this.gridForm.get("title").value,
  //       query: localStorage.getItem("agGidSelectedLookup_(query)_id"),
  //       userId: this.informationservice.getLogeduserId(),
  //     }

  //     this.http.post<any>(GlobalConstants.addGridApi, allData, { headers: GlobalConstants.headers }).subscribe(
  //       (res: any) => {
  //         this.commonFunctions.reloadPage("/dsp/gridBuilder");
  //         this.commonFunctions.navigateToPage("/dsp/gridBuilder");
  //       })
  //   }
  //   this.closeDialog();
  // }
  closeDialog() {
    this.dialog.closeAll();
  }

  async gridEventSave(event: any) {
    if (this.countEvent1 == 0) {
      this.countEvent1++;
      console.log("event issssssssssss ::::::: ", event);
      let updatedData: any[] = event[0].addList;
      console.log("updatedListtttttttttt", event[0].addList);
  
      let json: string = "\"nodes\":[";
  
      for (let i = 0; i < updatedData.length; i++) {
        if (updatedData[i].nodeName === undefined) {
          updatedData[i].nodeName = '';
        }
        if (updatedData[i].field === undefined) {
          updatedData[i].field = '';
        }
        if (updatedData[i].image === undefined) {
          updatedData[i].image = '';
        }
        if (updatedData[i].height === undefined) {
          updatedData[i].height = '';
        }
        if (updatedData[i].width === undefined) {
          updatedData[i].width = '';
        }
  
        json += "{" +
          "\"" + "nodeName" + "\"" + ":" + "\"" + updatedData[i].nodeName + "\"" + "," +
          "\"" + "field" + "\"" + ":" + "\"" + updatedData[i].field + "\"" + "," +
          "\"" + "image" + "\"" + ":" + "\"" + updatedData[i].image + "\"" + "," +
          "\"" + "height" + "\"" + ":" + "\"" + updatedData[i].height + "\"" + "," +
          "\"" + "width" + "\"" + ":" + "\"" + updatedData[i].width + "\"" + "}";
  
        if (i < updatedData.length - 1) {
          json += ",";
        }
      }
  
      json += "]";
      this.jsonNodes = json;
      console.log("this is jsonNodes data on save::::::::", this.jsonNodes);
    }
  }
  
async gridEventSave3(event: any) {
  if (!this.isVisible) {
    let json: string = "\"nodeInfo\":[" +
      "{" + "\"" + "infoName" + "\"" + ":" + "\"" + "" + "\"" + "," +
      "\"" + "field" + "\"" + ":" + "\"" + "" + "\"" + "," +
      "\"" + "linkedTo" + "\"" + ":" + "\"" + "" + "\"" + "}]";
    this.jsonNodeInfo = json;
  }

  if (this.countEvent3 == 0) {
    this.countEvent3++;
    console.log("event3 issssssssssss ::::::: ", event);
    let updatedData: any[] = event[0].addList;
    console.log("updatedListtttttttttt", event[0].addList);
    let json: string = "\"nodeInfo\":[";

    for (let i = 0; i < updatedData.length; i++) {
      if (updatedData[i].infoName === undefined) {
        updatedData[i].infoName = '';
      }
      if (updatedData[i].field === undefined) {
        updatedData[i].field = '';
      }
      if (updatedData[i].linkedTo === undefined) {
        updatedData[i].linkedTo = '';
      }

      json += "{" +
        "\"" + "infoName" + "\"" + ":" + "\"" + updatedData[i].infoName + "\"" + "," +
        "\"" + "field" + "\"" + ":" + "\"" + updatedData[i].field + "\"" + "," +
        "\"" + "linkedTo" + "\"" + ":" + "\"" + updatedData[i].linkedTo + "\"" + "}";

      if (i < updatedData.length - 1) {
        json += ",";
      }
    }

    json += "]";
    this.jsonNodeInfo = json;
    console.log("this is jsonNodeInfo data on save::::::::", this.jsonNodeInfo);
  }
}

async gridEventSave2(event: any) {
  if (this.countEvent2 == 0) {
    this.countEvent2++;
    console.log("event2 issssssssssss ::::::: ", event);
    let updatedData: any[] = event[0].addList;
    console.log("updatedListtttttttttt", event[0].addList);
    let json: string = "\"link\":[";

    for (let i = 0; i < updatedData.length; i++) {
      if (updatedData[i].linkName === undefined) {
        updatedData[i].linkName = '';
      }
      if (updatedData[i].color === undefined) {
        updatedData[i].color = '';
      }
      if (updatedData[i].linkedFrom === undefined) {
        updatedData[i].linkedFrom = '';
      }
      if (updatedData[i].linkedTo === undefined) {
        updatedData[i].linkedTo = '';
      }
      if (updatedData[i].label === undefined) {
        updatedData[i].label = '';
      }

      json += "{" +
        "\"" + "linkName" + "\"" + ":" + "\"" + updatedData[i].linkName + "\"" + "," +
        "\"" + "color" + "\"" + ":" + "\"" + updatedData[i].color + "\"" + "," +
        "\"" + "linkedFrom" + "\"" + ":" + "\"" + updatedData[i].linkedFrom + "\"" + "," +
        "\"" + "linkedTo" + "\"" + ":" + "\"" + updatedData[i].linkedTo + "\"" + "," +
        "\"" + "label" + "\"" + ":" + "\"" + updatedData[i].label + "\"" + "}";

      if (i < updatedData.length - 1) {
        json += ",";
      }
    }

    json += "]";

    if (updatedData.length > 0) {
      this.jsonLink = json;
    }

    console.log("this is jsonLink data on save::::::::", this.jsonLink);
  }
}

async gridEventSave4(event: any) {
  if (!this.isVisible) {
    let json: string = '';
    json += "\"linkInfo\":[" +
      "{" + "\"" + "infoName" + "\"" + ":" + "\"" + "" + "\"" + "," +
      "\"" + "field" + "\"" + ":" + "\"" + "" + "\"" + "," +
      "\"" + "source" + "\"" + ":" + "\"" + "" + "\"" + "," +
      "\"" + "target" + "\"" + ":" + "\"" + "" + "\"" + "}" + "]";
    this.jsonLinkInfo = json;
  }

  if (this.countEvent4 == 0) {
    this.countEvent4++;
    console.log("event4 issssssssssss ::::::: ", event);
    let updatedData: any[] = event[0].addList;
    console.log("updatedListtttttttttt", event[0].addList);
    let json: string = "\"linkInfo\":[";

    for (let i = 0; i < updatedData.length; i++) {
      if (updatedData[i].infoName === undefined) {
        updatedData[i].infoName = '';
      }
      if (updatedData[i].field === undefined) {
        updatedData[i].field = '';
      }
      if (updatedData[i].source === undefined) {
        updatedData[i].source = '';
      }
      if (updatedData[i].target === undefined) {
        updatedData[i].target = '';
      }

      json += "{" +
        "\"" + "infoName" + "\"" + ":" + "\"" + updatedData[i].infoName + "\"" + "," +
        "\"" + "field" + "\"" + ":" + "\"" + updatedData[i].field + "\"" + "," +
        "\"" + "source" + "\"" + ":" + "\"" + updatedData[i].source + "\"" + "," +
        "\"" + "target" + "\"" + ":" + "\"" + updatedData[i].target + "\"" + "}";

      if (i < updatedData.length - 1) {
        json += ",";
      }
    }

    json += "]";
    this.jsonLinkInfo = json;
    console.log("this is jsonLinkInfo data on save::::::::", this.jsonLinkInfo);
  }
}

async gridEventSave5() {
  if(this.countEvent5==0){
    this.countEvent5++;
  let jsonParams = {};

  jsonParams = {
  title: this.gridForm.get('title')?.value,
      query:this.gridForm.get('query')?.value,
    };
  // if (this.titleInput.nativeElement) {
  //   console.log('Value of #title input:', this.titleInput.nativeElement.value);
  //   // Assign to a variable if needed
  //   this.titleValue = this.titleInput.nativeElement.value;
  // }
  // else{this.titleValue="";}
    this.jsonMasterLink = JSON.stringify(jsonParams);
    
  console.log("jsonMasterLink :::::::",this.jsonMasterLink);

      // if (updatedData.length > 0) {
    //     try {
    //         const gridEventSaveApi = from(axios.post(GlobalConstants.masterLinkData, JSON.parse(json)));
    //         const gridEventSave = await lastValueFrom(gridEventSaveApi);
    //         this.commonFunctions.alert("alert", gridEventSave.data.description);
    //     } catch (error) {
    //         console.log("gridEventSave error >>> ", error);
    //     }
    // } else {
    //     this.commonFunctions.alert("alert", "No changes found");
    //     return;
    // }

  }}

//   async gridEventSave(event: any) {
//     console.log("event issssssssssss ::::::: ", event);
//     let updatedData: any[] = event[0].addList;
//     console.log("updatedListtttttttttt", event[0].addList);
//     let json: string = '[';
//     for (let i = 0; i < updatedData.length; i++) {
//         if (updatedData[i].nodeName == undefined) {
//             updatedData[i].nodeName = '';
//         }
//         if (updatedData[i].field == undefined) {
//             updatedData[i].field = '';
//         }
//         if (updatedData[i].image == undefined) {
//             updatedData[i].image = '';
//         }
//         if (updatedData[i].height == undefined) {
//             updatedData[i].height = '';
//         }
//         if (updatedData[i].width == undefined) {
//             updatedData[i].width = '';
//         }
//         if (i == updatedData.length - 1) {
//             json += "{" + "\"" + "nodeName" + "\"" + ":" + "\"" + updatedData[i].nodeName + "\"" + "," +
//                 "\"" + "field" + "\"" + ":" + "\"" + updatedData[i].field + "\"" + "," +
//                 "\"" + "image" + "\"" + ":" + "\"" + updatedData[i].image + "\"" + "," +
//                 "\"" + "height" + "\"" + ":" + "\"" + updatedData[i].height + "\"" + "," +
//                 "\"" + "width" + "\"" + ":" + "\"" + updatedData[i].width + "\"" + "}";
//             json += "]";
//         } else {
//             json += "{" + "\"" + "nodeName" + "\"" + ":" + "\"" + updatedData[i].nodeName + "\"" + "," +
//                 "\"" + "field" + "\"" + ":" + "\"" + updatedData[i].field + "\"" + "," +
//                 "\"" + "image" + "\"" + ":" + "\"" + updatedData[i].image + "\"" + "," +
//                 "\"" + "height" + "\"" + ":" + "\"" + updatedData[i].height + "\"" + "," +
//                 "\"" + "width" + "\"" + ":" + "\"" + updatedData[i].width + "\"" + "}" + ",";
//         }
//     }
//     console.log("this is json data on save::::::::", json);
//     if (updatedData.length > 0) {
//         try {
//             const gridEventSaveApi = from(axios.post(GlobalConstants.masterLinkData, JSON.parse(json)));
//             const gridEventSave = await lastValueFrom(gridEventSaveApi);
//             this.commonFunctions.alert("alert", gridEventSave.data.description);
//         } catch (error) {
//             console.log("gridEventSave error >>> ", error);
//         }
//     } else {
//         this.commonFunctions.alert("alert", "No changes found");
//         return;
//     }
// }


}
class DropdownCellRenderer {
  eGui: HTMLElement | null = null;
  selectedValue: string = '';

  init(params: any) {
    this.updateValue(params);

    const span = document.createElement('span');
    span.textContent = this.selectedValue;
    span.style.cssText = `
      display: flex !important; /* Use flexbox for centering */
      align-items: center !important; /* Center vertically */
      justify-content: center !important; /* Center horizontally */
      padding: 10px !important;
      background-color: #f0f0f0 !important;
      color: #333 !important;
      border-radius: 5px !important;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1) !important;
      text-overflow: ellipsis !important;
      white-space: nowrap !important;
      overflow: hidden !important;
      z-index: 2147483646 !important;
      position: relative !important;
      cursor: pointer !important;
      width: 100%; /* Ensure span takes full width */
      height: 100%; /* Ensure span takes full height */
    `;
    span.addEventListener('click', this.toggleDropdown.bind(this));

    this.eGui = document.createElement('div');
    this.eGui.style.cssText = `
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      display: flex;
      align-items: center;
      justify-content: center;
      overflow: hidden; /* Hide overflow to prevent text from spilling */
      text-overflow: ellipsis; /* Ensure text overflow is handled with ellipsis */
      white-space: nowrap; /* Prevent text wrapping */
    `;
    this.eGui.appendChild(span);
  }

  updateValue(params: any) {
    if (params.value !== undefined) {
      this.selectedValue = params.value.name !== undefined ? params.value.name : params.value;
    }
  }

  toggleDropdown() {
    const dropdown = this.eGui!.querySelector('.ag-rich-select-list') as HTMLElement;
    const span = this.eGui!.querySelector('span') as HTMLElement;

    if (dropdown) {
      dropdown.classList.toggle('ag-rich-select-list-active');

      if (dropdown.classList.contains('ag-rich-select-list-active')) {
        dropdown.style.display = 'block';
      } else {
        dropdown.style.display = 'none';
        if (span) {
          span.style.width = this.eGui!.clientWidth + 'px';
          span.style.height = this.eGui!.clientHeight + 'px';
        }
      }
    }
  }
  
  

  getGui() {
    return this.eGui!;
  }

  refresh(params: any) {
    return false;
  }
}


