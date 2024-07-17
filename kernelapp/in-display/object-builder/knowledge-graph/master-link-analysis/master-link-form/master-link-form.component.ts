
import { Component, OnInit, ElementRef, ChangeDetectorRef, NgZone   } from '@angular/core';
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


@Component({
  selector: 'app-master-link-form',
  templateUrl: './master-link-form.component.html',
  styleUrl: './master-link-form.component.css'
})
export class MasterLinkFormComponent implements OnInit {
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
  

  public allColumnsAr: any[] = [{ id: '', name: '' }];

  //grid builder variables
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public agColumns2: AgColumns[] = [];
  public agColumnsJson2: any;
  public agGridSelectedNodes: any = '';
  public getGridData = GlobalConstants.getGridDataApi;
  public action: any;
  frameworkComponents: any;
  public getQbeQueryApi  = GlobalConstants.fetchQbeMappingApi;

  public isQueryexecute: boolean;
  headerNames: any[] = [];
  public test = '';
  public shouldReloadGrid: boolean = false;

  constructor(
    public commonFunctions: CommonFunctions,
    private router: Router,
    private dataservice: DataService,
    private http: HttpClient,
    private dialog: MatDialog,
    public informationservice: InformationService,
    private elementRef: ElementRef,
    private cdRef: ChangeDetectorRef,
    private ngZone: NgZone) 
    {
      this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
     }
     public conditions = [
      { id: '', name: '' },
      { id: 1, name: '=' },
      { id: 2, name: '<' },
      { id: 3, name: '>' },
      { id: 4, name: '<=' },
      { id: 5, name: '>=' },
      { id: 6, name: '< sysdate' },
      { id: 7, name: '> sysdate' },
      { id: 8, name: '!=' },
      { id: 9, name: 'included' },
      { id: 10, name: '!included' },
      // { id: 8, name: 'Get sysdate (Year)' },
      // { id: 8, name: 'Get sysdate (Month)' },
      // { id: 8, name: 'Get sysdate (Day)' },
  
    ];

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
        cellEditor: 'agRichSelectCellEditor',
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
        editable: true,
        cellEditor: 'agSelectCellEditor'
        // width: '15px',
      },
      {
        headerName: 'Linked To',
        field: 'linkedTo',
        editable: true,
        cellEditor: 'agSelectCellEditor'
        // width: '20px',
      },

      {
        headerName: 'Label',
        field: 'label',
        editable: true,
        cellEditor: 'agSelectCellEditor'
        // width: '20px'
      }, 

    ];
    this.agColumns2.push(this.agColumnsJson2);
    
  }
  updateAgColumnsJson(): void {
    

    console.log("we entereddddd the update function");  
    console.log("headerrrrrr  names are eeeeeeeee" ,this.headerNames);
      // Create a new configuration object for 'Field'
  const updatedFieldColumn = {
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
  };

  // Update agColumnsJson at index 1 with the new configuration
  this.agColumnsJson[1] = updatedFieldColumn;

  // Update agColumns with the modified agColumnsJson
  this.agColumns=[];
  this.agColumns.push(this.agColumnsJson); // Create a new array to trigger change detection
  setTimeout(() => { 
  this.ngZone.run(() => {
    this.cdRef.detectChanges();
  });
}, 1000);
    console.log("this is agcolums1",this.agColumns);
    console.log("this is agcolums2",this.agColumns2);
    this.isReload=true;
  }

  onDropdownChange(event: any): void {
    this.isReload=false;
    this.test = GlobalConstants.inDispGatewat + 'api/getAllQueriesHeaderList';
    
    this.http.get<any[]>(GlobalConstants.getAllQueriesHeaderList + event, { headers: GlobalConstants.headers })
      .subscribe(response => {
        if (response && Array.isArray(response)) {
          this.headerNames = response.map(header => header.field);
          this.updateAgColumnsJson();
          // trigger change detection
  
          // Add a small timeout before triggering grid reload
          // setTimeout(() => { 
          //   this.cdRef.detectChanges();
          //   // const gridReloadEvent = new Event('reloadGrid', { bubbles: true });
          //   // document.getElementById('gridReload')?.dispatchEvent(gridReloadEvent);
          // }, 1000); // Adjust timeout duration as needed
          // this.isReload = true;
        }
      });
  }
  

  // Method to handle grid reload event if needed
  onGridReload(event: Event): void {
    // Handle any specific actions needed when the grid reloads
    console.log('Grid is reloading...');
  }
  

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
    // grid builder
  }
  save() 
  {
    // grid builder
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

}{

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


