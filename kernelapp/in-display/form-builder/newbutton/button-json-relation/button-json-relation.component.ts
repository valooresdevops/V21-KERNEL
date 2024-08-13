import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, ElementRef, Inject, NgZone, Renderer2 } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { ButtonRendererComponent } from 'src/app/Kernel/components/buttonRenderer.component';
import { DataService } from 'src/app/Kernel/services/data.service';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-button-json-relation',
  templateUrl: './button-json-relation.component.html',
  styleUrl: './button-json-relation.component.css'
})
export class ButtonJsonRelationComponent {
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
  
  constructor(public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    private dialog: MatDialog, private snackBar: MatSnackBar,
    private _Activatedroute: ActivatedRoute,
    public datepipe: DatePipe,
    private route: Router,
    private dialogRef: MatDialogRef<ButtonJsonRelationComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private router: Router,
    private dataservice: DataService,
    private http: HttpClient,
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
  
  ngOnInit(): void {
  
    this.isReload=false;
    
    //const allColuumsUrl = from(axios.get(GlobalConstants.getColumnsApi + this.objectId));
    //const allColuums = await lastValueFrom(allColuumsUrl);

    this.http.get<any[]>(GlobalConstants.getColumnsApi + this.data.objectId, { headers: GlobalConstants.headers })
      .subscribe(response => {
        console.log("RESPONSE<<<<<<<<<<>>>>>>>>>>>>",response);
        if (response && Array.isArray(response)) {
          this.headerNames = response.map(header => header.name);
          this.updateAgColumnsJson();
          this.cdRef.detectChanges(); 
            setTimeout(() => {
            const gridReloadEvent = new Event('reloadGrid', { bubbles: true });
            document.getElementById('gridReload')?.dispatchEvent(gridReloadEvent);
          }, 1000); 
          this.isReload = true;
        }
      });
    
      console.log("THIS HEADERNAMES>>>>>>>>>>>>>>",this.headerNames);


  }

  updateAgColumnsJson(): void {

    this.agColumnsJson = [
      {
        headerName: 'Field Name',
        field: 'name',
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
        headerName: 'Json Parameter',
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
    ];
  
    this.agColumns = []; // Update agColumns with the new configuration
    this.agColumns.push(this.agColumnsJson);


    console.log("this is agcolums1",this.agColumns);
    console.log("this is agcolums2",this.agColumns2);

  }

  onGridReady(params: any) {
    params.api.sizeColumnsToFit();
    this.getAllRowsData(params.api);
    this.gridApi = params.api;
  }
  getAllRowsData(api : any) {
    let allRowData : any[]= [];
    api.forEachNode((node: any) => {
      allRowData.push(node.data);
    });
    console.log('All Row Data:', allRowData);
    // Now you can save allRowData
  }

  async gridEventSave(event: any) {
    console.log("ON GRID EVENT SAVE!");
  }

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
      overflow-y:auto !important

    `;
    this.eGui.appendChild(span);
  }

  updateValue(params: any) {
    console.log("PARAM VALUE>>>>>>>",params.value);
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
