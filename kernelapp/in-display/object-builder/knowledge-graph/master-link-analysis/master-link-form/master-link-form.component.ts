
import { Component, OnInit, ElementRef, ChangeDetectorRef, ViewChild , Renderer2,NgZone    } from '@angular/core';
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
public ready : Boolean = true;
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
  private gridApi: any;
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
  public images: any[] = [];

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
    private renderer: Renderer2,
    private ngZone: NgZone) 
    {
    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
     }
  async ngOnInit(): Promise<void> 
  {
    const getNodesImagesApi = from(axios.post(GlobalConstants.getNodesImages,{}));
    const getNodesImages = await lastValueFrom(getNodesImagesApi);
    this.images=getNodesImages.data;
    this.images = this.images.map(item => item.name);
    console.log("images>>>>>>>>>>",this.images); // ["name1", "name2"]

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
        cellEditor: 'agTextCellEditor',
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
        headerName: 'Image',
        field: 'image',
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.images,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
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
        cellEditor: 'agTextCellEditor',
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
        cellEditor: 'agTextCellEditor',
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
        cellEditor: 'agSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.images,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
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
        cellEditor: 'agTextCellEditor',
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
          cellEditor: 'agTextCellEditor',
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
          cellEditor: 'agTextCellEditor',
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
    console.log("THIS HEADERNAMES>>>>>>>>>>>>>>",this.headerNames);
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
      // this.http.get<any[]>(GlobalConstants.getNodesImages, { headers: GlobalConstants.headers })
      // .subscribe(response => {
      //   if (response && Array.isArray(response)) {
      //     this.images = response.map(header => header.field);
      //     this.updateAgColumnsJson();
      //     this.cdRef.detectChanges(); // trigger change detection
  
      //     // Add a small timeout before triggering grid reload
      //     setTimeout(() => {
      //       const gridReloadEvent = new Event('reloadGrid', { bubbles: true });
      //       document.getElementById('gridReload')?.dispatchEvent(gridReloadEvent);
      //     }, 1000); // Adjust timeout duration as needed
      //     this.isReload = true;
      //   }
      // });
  }
  

  // Method to handle grid reload event if needed
  onGridReload(event: Event): void {
    // Handle any specific actions needed when the grid reloads
    console.log('Grid is reloading...');
  }

  addInfo() 
  {
    this.agColumnsJson3 = [
      {
        headerName: 'Info Name',
        field: 'infoName',
        editable: true,
        cellEditor: 'agTextCellEditor',
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
        cellEditor: 'agTextCellEditor',
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
               const gridEventSaveApi = from(axios.post(GlobalConstants.insertMasterLinkData+this.informationservice.getLogeduserId(), JSON.parse(this.jsonRes)));
               const gridEventSave = await lastValueFrom(gridEventSaveApi);
              this.commonFunctions.alert("alert", "Master Link Created");
          } catch (error) {
              console.log("gridEventSave error >>> ", error);
          }
  }
  async saveMasterLinkData() {
    let check : boolean;
    let eventCounter = 3;
    if(this.isVisible) eventCounter = 5;
    if(!this.isVisible) {
      check = false;
    } else check=true;
    console.log('Save clicked');
    this.countEvent1 = this.countEvent2 = this.countEvent3 = this.countEvent4 = this.countEvent5 =0;
    $('.defaultBtn').each(function(index) {

      if (index >= eventCounter) return false; // Stop iteration after 5 buttons
      $(this).trigger('click');
    });
    
    if(!check) this.isVisible = false;
this.gridEventSave5();
await this.jsonBuilder();
  }
  

  closeDialog() {
    this.dialog.closeAll();
  }
  onGridReady(params: any) {
    params.api.sizeColumnsToFit();
    this.getAllRowsData(params.api);
    this.gridApi = params.api;
  }
  onGridReady2(params: any) {
    params.api.sizeColumnsToFit();
    this.getAllRowsData(params.api);
  }
  onGridReady3(params: any) {
    params.api.sizeColumnsToFit();
    this.getAllRowsData(params.api);
  }
  onGridReady4(params: any) {
    params.api.sizeColumnsToFit();
    this.getAllRowsData(params.api);
  }
  getAllRowsData(api : any) {
    let allRowData : any[]= [];
    api.forEachNode((node: any) => {
      allRowData.push(node.data);
    });
    console.log('All Row Data:', allRowData);
    // Now you can save allRowData
  }
  gridDeselectAll( ){
    this.ready = false;
    this.ready=true;
    this.gridApi.deselectAll();
    console.log("deselected alll");
  }
  async gridEventSave(event: any) {
    
    if (this.countEvent1 == 0) {
      this.countEvent1++;
      console.log("event issssssssssss ::::::: ", event);
      let updatedData: any[] = event[0].addList;
      event[0].deleteList.forEach((deleteItem: any[]) => {
        const rowToDelete = deleteItem[0];
        updatedData = updatedData.filter((addItem: any) => addItem !== rowToDelete);
      });
  
      let json: string = "\"nodes\":[";
      if(updatedData.length==0){ 
        json += "{" +
        "\"" + "nodeName" + "\"" + ":" + "\"" + "\"" + "," +
        "\"" + "field" + "\"" + ":" + "\""  + "\"" + "," +
        "\"" + "image" + "\"" + ":" + "\""  + "\"" + "," +
        "\"" + "height" + "\"" + ":" + "\""  + "\"" + "," +
        "\"" + "width" + "\"" + ":" + "\""  + "\"" + "}";
      }
      console.log("updatedData111111",updatedData);
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


  if (this.countEvent3 == 0) {
    this.countEvent3++;
    console.log("event3 issssssssssss ::::::: ", event);
    let updatedData: any[] = event[0].addList;
    event[0].deleteList.forEach((deleteItem: any[]) => {
      const rowToDelete = deleteItem[0];
      updatedData = updatedData.filter((addItem: any) => addItem !== rowToDelete);
    });

    let json: string = "\"nodeInfo\":[";

    if(updatedData.length==0){
      json += "{" +
      "\"" + "infoName" + "\"" + ":" + "\"" + "\"" + "," +
      "\"" + "field" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "linkedTo" + "\"" + ":" + "\""  + "\"" + "}";
    }
    console.log("updatedData3333",updatedData);
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
    event[0].deleteList.forEach((deleteItem: any[]) => {
      const rowToDelete = deleteItem[0];
      updatedData = updatedData.filter((addItem: any) => addItem !== rowToDelete);
    });
    let json: string = "\"link\":[";

    if(updatedData.length==0){
      json += "{" +
      "\"" + "linkName" + "\"" + ":" + "\"" + "\"" + "," +
      "\"" + "color" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "linkedFrom" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "linkedTo" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "label" + "\"" + ":" + "\""  + "\"" + "}";
    }
    console.log("updatedData22222",updatedData);
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

  if (this.countEvent4 == 0) {
    this.countEvent4++;
    console.log("event4 issssssssssss ::::::: ", event);
    let updatedData: any[] = event[0].addList;
    event[0].deleteList.forEach((deleteItem: any[]) => {
      const rowToDelete = deleteItem[0];
      updatedData = updatedData.filter((addItem: any) => addItem !== rowToDelete);
    });
    let json: string = "\"linkInfo\":[";

    if(updatedData.length == 0 ){
      json += "{" +
      "\"" + "infoName" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "field" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "source" + "\"" + ":" + "\""  + "\"" + "," +
      "\"" + "target" + "\"" + ":" + "\""  + "\"" + "}";
    }
    console.log("updatedData4444",updatedData);
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
    this.jsonMasterLink = JSON.stringify(jsonParams);
    
  console.log("jsonMasterLink :::::::",this.jsonMasterLink);

  }}

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


