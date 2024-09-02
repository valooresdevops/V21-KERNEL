import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { AdvancedSearchConfigurationFormComponent } from './advanced-search-configuration-form/advanced-search-configuration-form.component';
import { InformationService } from 'src/app/Kernel/services/information.service';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-advanced-search-configuration',
  templateUrl: './advanced-search-configuration.component.html',
  styleUrl: './advanced-search-configuration.component.css'
})
export class AdvancedSearchConfigurationComponent {
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  //public getDynamicSearchFields:any=GlobalConstants.getDynamicSearchFields;
  public hasFunctions:boolean=false;
  public allGridData:any[]=[];
  public showGrid:boolean=true;
  advancedSearchConfigurationForm = new UntypedFormGroup({
    hasFunctionalities: new UntypedFormControl(''),
    columnGridData:new UntypedFormControl('')
  });

  constructor(private dialog: MatDialog, private http: HttpClient, private _Activatedroute: ActivatedRoute, private commonFunctions: CommonFunctions,public router:Router,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<AdvancedSearchConfigurationComponent>,
    public informationservice: InformationService
    ){}

  
  async ngOnInit(): Promise<void> {
    if(this.data.actionType=="update"){

    const getAdvancedSearchGridDataApi=from(axios.post(GlobalConstants.getAdvancedSearchGridData+this.data.objectId))
    const getAdvancedSearchGridData=await lastValueFrom(getAdvancedSearchGridDataApi);
    
    this.allGridData=getAdvancedSearchGridData.data;
    }
   
    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        width: '25px',
        headerCheckboxSelection: true
      },
      {
        headerName: 'Id',
        field: 'dynSrchId',
        width: '25px',
        hide:true
      },
      {
        headerName: 'Column Name',
        field: 'colName',
      },  
      {
        headerName: 'Type',
        field: 'colType',
      },
      {
        headerName: 'Display Name',
        field: 'searchDescription',
      },
      {
        headerName: 'Query',
        field: 'comboSql',
      },
      {
        headerName: 'Is Mandatory',
        field: 'isMandatory',
      },
      {
        headerName: 'Is Default',
        field: 'isDefault',
      },
      {
        headerName: 'Default Values',
        field: 'defaultValues',
      },
      {
        headerName: 'Creation Date',
        field: 'creationDate',
        hide:true
      }
    ];

    this.agColumns.push(this.agColumnsJson);

  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  onAddClick(){
    let data={
      objectId:this.data.objectId,
    }
    const dialogRef = this.dialog.open(AdvancedSearchConfigurationFormComponent, {
      width: "90%",
      height: "90%",
      data: data
    });
    dialogRef.disableClose = true;

    dialogRef.afterClosed().subscribe(result => {
      if(result!=null){
      console.log("DATA RETURNED OF FORM CLOSEEEE>>>>>>>>>>>>>",result);
      this.allGridData.push(result);

      console.log("ALL GRID DATAAAAAAA>>>>>>>>>>>>>",this.allGridData);
      this.showGrid=false;
      setTimeout(() => {
          this.showGrid=true;
      }, 200);
    }
    });
    
  }

  onDeleteClick(){  
    let selectedColumn=this.informationservice.getAgGidSelectedNode();
    console.log("ALL GRID DATA ON DELETE>>>>>>>>",this.allGridData);
    if(this.allGridData.length==1){
      this.allGridData=[];
    }
    this.allGridData = this.allGridData.filter(item => item.colName !== selectedColumn);
    this.informationservice.setAgGidSelectedNode('');
  }

  onUpdateClick(){

  }

  searchHasFunctions(){

    if(this.hasFunctions){
    
      this.hasFunctions=false;
    
    }else{
    
      this.hasFunctions=true;
    
    }

  }


  async submitForm(){

    
    $("#buttonSubmitForm")[0].click();

    console.log("ACTION TYPE>>>>>>>",this.data.actionType);
    let jsonData={  
      objectId:this.data.objectId,
      allGridData:this.allGridData,
      functionData:this.informationservice.getAdvancedSearchFunctionData()
    }
    console.log("ALL FORM DATA>>>>>>",jsonData);

    const insertAdvancedSearchAndFunctionsApi=from(axios.post(GlobalConstants.insertAdvancedSearchAndFunctions,jsonData))
    const insertAdvancedSearchAndFunctions=await lastValueFrom(insertAdvancedSearchAndFunctionsApi);
    console.log("insertAdvancedSearchAndFunctions>>>>>>>>>>>",insertAdvancedSearchAndFunctions.data);
    if(insertAdvancedSearchAndFunctions.data){
      this.commonFunctions.alert("alert","Advanced Search Configuration Saved");
      this.closeDialog();
    }else{
      this.commonFunctions.alert("alert","Failed to save Advanced Search Configuration");
    }
  }
  
}
