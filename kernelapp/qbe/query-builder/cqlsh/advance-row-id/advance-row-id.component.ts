import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { stringify } from 'querystring';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { AdvancedFormComponent } from 'src/app/Kernel/components/v-dynamic-rule-builder/form/advanced-form/advanced-form.component';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { RefreshDialogService } from 'src/app/Kernel/services/refresh-dialog.service';

@Component({
  selector: 'app-advance-row-id',
  templateUrl: './advance-row-id.component.html',
  styleUrl: './advance-row-id.component.css'
})
export class AdvanceRowIdComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public lookupData: any,
  private dialogRef: MatDialogRef<AdvancedFormComponent>,
  private refreshService: RefreshDialogService,
  private http: HttpClient,public commonFunctions: CommonFunctions,
  public informationservice: InformationService) { }

  public agColumns: AgColumns[] = [];
  public allColumnsAr: any[] = [{ id: '', name: '' }];
  public agColumnsJson: any;
  public queryID = JSON.parse(this.informationservice.getAgGidSelectedNode())[0].QBE_ID;
  public addList:any;
  public list:any;

  public dataApi:any;




  public type = [
    { id: '', name: '' },
    { id: 1, name: 'Form' },
    { id: 2, name: 'Grid' },
    { id: 3, name: 'Button' },
    { id: 4, name: 'Tab' },
    { id: 5, name: 'Combo' },
    { id: 6, name: 'GridLink'}
  ];

  async ngOnInit(): Promise<void> {
    this.dataApi=GlobalConstants.fetchAdvancedRowId +this.queryID;
    this.http.get<any>(GlobalConstants.cqlFetchDynamicHeaderData+"execHeads_"+sessionStorage.getItem("session_serial")+"/"+this.queryID).subscribe(
      (res: any) => {
        if (res.status == 'Fail') {
          this.commonFunctions.alert("alert", res.description);
        } else {
          for(let i = 0; i < res.length; i++) {
            this.allColumnsAr.push({id: res[i].headerName, name: res[i].field});
          }
        }
  
      });

      console.log("allColumnsAr----------->",this.allColumnsAr);
      this.CreateGrid();

    }
  async gridEventSave(event: any) {
    const addList = event[0].addList;
    this.addList = JSON.stringify(addList);
  //  const result: { type: any; colName: any; colValue: string; }[] = [];

    // event.forEach((item:any) => {
    //   item.addList.forEach((addItem:any) => {
    //     const transformedItem = {
    //       type: addItem.type.name.toLowerCase(),
    //       colName: addItem.field.name,
    //       colValue: `' ||${addItem.field.id}|| '`
    //     };
    //     result.push(transformedItem);
    //   });
    // });
// console.log(result);
console.log('queryId-----> : ',this.queryID);
let allData={
  queryId:this.queryID,
  advanceRowId: this.addList 
};
console.log(allData);
this.http.post<any>(GlobalConstants.updateAdvancedRowId, allData, { headers: GlobalConstants.headers }).subscribe(
  (res: any) => {
    console.log(res)
  })
    return ;
  }
  CreateGrid(){
    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        maxWidth: '50',
        headerCheckboxSelection: true
      },{
        headerName: 'Field',
        field: 'field',
        minwidth: '300',
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
        headerName: 'Type',
        field: 'type',
        width: '300px',
        cellEditor: 'agRichSelectCellEditor',
        cellRenderer: DropdownCellRenderer,
        keyCreator: (params: any) => {
          return params.value.name;
        },
        cellEditorParams: {
          cellRenderer: DropdownCellRenderer,
          values: this.type,
        },
        editable: true,
        cellDataType: true
      },
    
    ]
    this.agColumns.push(this.agColumnsJson);
  }
  
}
























class DropdownCellRenderer {
  setValues(filteredValues: any) {
    throw new Error('Method not implemented.');
  }
  eGui: any;

  init(params: any) {
    if (params.value != undefined) {
      if (params.value.name != undefined) {
        this.eGui = document.createElement('div');
        this.eGui.innerHTML = `<span style="display:block; text-overflow: ellipsis">${params.value.name}</span>`;
      } else {
        this.eGui = document.createElement('div');
        this.eGui.innerHTML = `<span style="overflow: hidden;position:fixed;top:0; text-overflow: ellipsis">${params.value}</span>`;
      }
    }
  }
  getGui() {
    return this.eGui;
  }
  refresh(params: any) {
    return false;
  }
}
