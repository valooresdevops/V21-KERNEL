import { HttpClient } from '@angular/common/http';
import { AfterViewInit, Component, Inject, Input, OnInit, ViewChild, enableProdMode } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AgEditorComponent, AgGridAngular } from 'ag-grid-angular';
import { ICellRendererParams, IAfterGuiAttachedParams } from 'ag-grid-community';
import axios from 'axios';
import { Console } from 'console';
import { from, lastValueFrom } from 'rxjs';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { RefreshDialogService } from 'src/app/Kernel/services/refresh-dialog.service';
import { AgRendererComponent } from 'ag-grid-angular';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { GridOptions } from 'ag-grid-community';

@Component({
  selector: 'advanced-form',
  templateUrl: './advanced-form.component.html',
  styleUrls: ['./advanced-form.component.css']
})
export class AdvancedFormComponent implements OnInit {

  gridOptions: GridOptions;
  rowData: any[] = [
    { make: "Toyota", model: "Celica", price: 35000 },
    { make: "Ford", model: "Mondeo", price: 32000 },
    { make: "Porsche", model: "Boxster", price: 72000 }
  ];

  @ViewChild('agGrid', { static: true }) agGrid!: AgGridAngular;

  constructor(@Inject(MAT_DIALOG_DATA) public lookupData: any,
    private dialogRef: MatDialogRef<AdvancedFormComponent>,
    private refreshService: RefreshDialogService,
    private http: HttpClient,
    public informationservice: InformationService,
  ) { this.gridOptions = {
    rowData: this.rowData,
    // Define columns here if needed
  };}
    

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public test = '';
  public dataApiParam: any = [];
  public addList = '';
  public allColumnsAr: any[] = [{ id: '', name: '' }];
  public AdvancedRule: any = [];
  public GridWithValue: any;
  public lastAdvancedRuleData : any;
  // public staticData: any[] = null;
  public showGrid = false;
  public closeAndOpenGridData: any;
  public list : any = [];

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

  public operators = [
    { id: '', name: '' },
    { id: 1, name: 'And' },
    { id: 2, name: 'Or' },
    { id: 3, name: 'No Operator' }
    //test
  ];

  async ngOnInit(): Promise<void> {

    let FirstTimeOfLoadingGrid = this.informationservice.getFirstOpenGrid()

    if(FirstTimeOfLoadingGrid == '0'){
    this.informationservice.getDataToSet();
    this.dataApiParam = JSON.parse(this.closeAndOpenGridData);
    this.test = GlobalConstants.inDispGatewat + 'api/getAdvancedResult';
    this.CreateGridSaveNew(); 

    }else{
      const getColumnsApiUrl = from(axios.get(GlobalConstants.getColumnsApi + this.lookupData[0].objectId));
      const getColumnsApi = await lastValueFrom(getColumnsApiUrl);
      for (let i = 0; i < getColumnsApi.data.length; i++) {
        this.allColumnsAr.push({ id: getColumnsApi.data[i].id, name: getColumnsApi.data[i].name });
      }
      
      let list = this.lookupData[0].advancedRuleData;
      for (let i = 0; i < this.lookupData[0].advancedRuleData.length; i++) {
        if (this.lookupData[0].advancedRuleData[i].step == 0) {
          this.AdvancedRule = this.lookupData[0].advancedRuleData[i].data;
        }
      }
  
      for (let i = 0; i < list.length; i++) {
        if (list[i].step == 0) {
          this.lastAdvancedRuleData = list[i].data;
        }
      }
  
      if (this.lastAdvancedRuleData != undefined) {
        this.GridWithValue = 1;
        const jsonObject = JSON.parse(this.AdvancedRule);
        const addList = jsonObject.addList;
        this.addList = JSON.stringify(addList);
        let Object = JSON.parse(this.addList);
  
        for (let i = 0; i < Object.length; i++) {
          let beginCondition = '';
          let field = '';
          let condition = '';
          let value = '';
          let endCondition = '';
          let operator = '';
  
          if (Object[i].beginCondition == undefined) {
            beginCondition = '';
          }
          if (Object[i].beginCondition != undefined) {
            beginCondition = Object[i].beginCondition.name;
          }
          if (Object[i].field == undefined) {
            field = '';
          }
          if (Object[i].field != undefined) {
            field = Object[i].field.name;
          }
  
          if (Object[i].condition == undefined) {
            condition = '';
          }
          if (Object[i].condition != undefined) {
            condition = Object[i].condition.name;
          }
  
          if (Object[i].value == undefined) {
            value = '';
          }
          if (Object[i].value != undefined) {
            value = Object[i].value;
          }
  
          if (Object[i].endCondition == undefined) {
            endCondition = '';
          }
          if (Object[i].endCondition != undefined) {
            endCondition = Object[i].endCondition.name;
          }
  
          if (Object[i].operator == undefined) {
            operator = '';
          }
          if (Object[i].operator != undefined) {
            operator = Object[i].operator.name;
          }
  
          let item = {
            "beginCondition": beginCondition,
            "field": field,
            "condition": condition,
            "value": value,
            "endCondition": endCondition,
            "operator": operator
          }
          this.dataApiParam.push(item);
          this.test = GlobalConstants.inDispGatewat + 'api/getAdvancedResult';
        }
        this.CreateGridSaveNew();
      } else {
        this.GridWithValue = 0;
        this.test = '';
        this.dataApiParam = [];
        this.CreateGridSaveNew();
        
      }
    }
  }


  onGridReady(params: any) {
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

  CreateGridSaveNew() {
    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        maxWidth: '50',
        headerCheckboxSelection: true
      },
      {
        headerName: 'Begin Condition',
        field: 'beginCondition',
        width: 110,
        cellEditor: 'agRichSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: [
            { id: '', name: '' },
            { id: '1', name: '{' },
          ],
        },
        editable: true,
        cellDataType: true
      },
      {
        headerName: 'Field',
        field: 'field',
        width: 110,
        cellEditor: 'agRichSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.allColumnsAr,
          isSearchable: true,
        },
        editable: true,
        cellDataType: true
      },
      {
        headerName: 'Condition',
        field: 'condition',
        width: 110,
        cellEditor: 'agRichSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.conditions,
        },
        editable: true,
        cellDataType: true,
        cellClass: 'ag-rich-select-list, ag-rich-select-list-item, ag-rich-select-list-item:hover'
      },
      
      {
        headerName: 'Value',
        field: 'value',
        cellEditor: 'agTextCellEditor',
        editable: true
      },
      {
        headerName: 'Operator',
        field: 'operator',
        width: 110,
        cellEditor: 'agRichSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.operators,
        },
        editable: true,
        cellDataType: true
      },
      {
        headerName: 'End Condition',
        field: 'endCondition',
        cellEditor: 'agRichSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: [
            { id: '', name: '' },
            { id: '2', name: '}' },
          ],
        },
        editable: true,
        cellDataType: true
      },
    ]
    this.agColumns.push(this.agColumnsJson);
    this.showGrid = true;  
  }

  closeDialog(data: any): void {
    this.dialogRef.close(data);
    this.refreshService.notifyOther({ refresh: true });
  }

  async gridEventSave(event: any) {
  console.log("event >>>>>>> ",event);     
      const addList = event[0].addList;
      this.addList = JSON.stringify(addList);
      let Object = JSON.parse(this.addList);
      console.log("adlisttttttttttt:",addList);
      for (let i = 0; i < Object.length; i++) {
        let beginCondition = '';
        let field = '';
        let condition = '';
        let value = '';
        let endCondition = '';
        let operator = '';

        if (Object[i].beginCondition == undefined) {
          beginCondition = '';
        }
        if (Object[i].beginCondition != undefined) {
          beginCondition = Object[i].beginCondition.name;
        }
        if (Object[i].field == undefined) {
          field = '';
        }
        if (Object[i].field != undefined) {
          field = Object[i].field.name;
        }

        if (Object[i].condition == undefined) {
          condition = '';
        }
        if (Object[i].condition != undefined) {
          condition = Object[i].condition.name;
        }

        if (Object[i].value == undefined) {
          value = '';
        }
        if (Object[i].value != undefined) {
          value = Object[i].value;
        }

        if (Object[i].endCondition == undefined) {
          endCondition = '';
        }
        if (Object[i].endCondition != undefined) {
          endCondition = Object[i].endCondition.name;
        }

        if (Object[i].operator == undefined) {
          operator = '';
        }
        if (Object[i].operator != undefined) {
          operator = Object[i].operator.name;
        }

        let item = {
          "beginCondition": beginCondition,
          "field": field,
          "condition": condition,
          "value": value,
          "endCondition": endCondition,
          "operator": operator
        }
        this.list.push(item);
      }
    this.informationservice.setDataToSet(JSON.stringify(this.list));

    this.informationservice.setFirstOpenGrid('0');

    this.closeDialog(event[0]);
  }

  

}

// class DropdownCellRenderer {
//   setValues(filteredValues: any) {
//     throw new Error('Method not implemented.');
//   }
//   eGui: any;

//   init(params: any) {
//     if (params.value != undefined) {
//       if (params.value.name != undefined) {
//         this.eGui = document.createElement('div');
//         this.eGui.innerHTML = `<span style="position:fixed;background-color: yellow; text-overflow: ellipsis; z-index: 9999999999999999999 !important;">${params.value.name}</span>`;
//       } else {
//         this.eGui = document.createElement('div');
//         this.eGui.innerHTML = `<span style="overflow: hidden;background-color: yellow;position:fixed;top:0; text-overflow: ellipsis; z-index: 9999999999999999999 !important;">${params.value}</span>`;
//       }
//     }
//   }
//   getGui() {
//     return this.eGui;
//   }
//   refresh(params: any) {
//     return false;
//   }
// }
class DropdownCellRenderer {
  eGui: HTMLElement | null = null;

  init(params: any) {
    if (params.value !== undefined) {
      const span = document.createElement('span');
      span.style.cssText = `
        
        
        background-color: #f0f0f0 !important; /* Green background */
        color: #333 !important; /* Dark text color */
        border-radius: 5px !important; /* Rounded corners */
        box-shadow: 0 2px 4px rgba(0,0,0,0.1) !important; /* Subtle shadow for depth */
        text-overflow: ellipsis !important;
        white-space: nowrap !important;
        overflow: hidden !important; /* Ensure content is visible outside */
        z-index: 2147483646 !important; /* Ensure it's on top */
        position: absolute !important; /* Adjusted to ensure visibility */
        top:100 !important;
        margin-bottom:10% !important;
      `;

      span.textContent = params.value.name !== undefined ? params.value.name : params.value;

      this.eGui = document.createElement('div');
      this.eGui.style.cssText = `
        position: absolute; /* Ensure absolute positioning */
        top: 0; /* Adjust positioning as needed */
        left: 0; /* Adjust positioning as needed */
        bottom:20;
      `;
      this.eGui.appendChild(span);
    }
  }

  getGui() {
    return this.eGui!;
  }

  refresh(params: any) {
    return false;
  }
}
