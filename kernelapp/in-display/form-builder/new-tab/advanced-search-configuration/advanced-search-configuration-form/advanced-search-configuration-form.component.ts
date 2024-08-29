import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';

@Component({
  selector: 'app-advanced-search-configuration-form',
  templateUrl: './advanced-search-configuration-form.component.html',
  styleUrl: './advanced-search-configuration-form.component.css'
})
export class AdvancedSearchConfigurationFormComponent {
  public getColumnType:any=GlobalConstants.getColumnsSearchType;
  public comboQueryIsValid:boolean=false;
  public isCombo:boolean=false;
  public hasDefaultValue:boolean=false;
  public getColumnNames:any='';
  public defaultValuesData:any[]=[];
  searchConfigForm = new UntypedFormGroup({
    colName: new UntypedFormControl(''),
    colType:new UntypedFormControl(''),
    comboSql: new UntypedFormControl(''),
    isForForm: new UntypedFormControl(''),
    isMandatory: new UntypedFormControl(''),
    isDefault: new UntypedFormControl(''),
    defaultValues: new UntypedFormControl(''),
  });

  constructor(private dialog: MatDialog, private http: HttpClient, private _Activatedroute: ActivatedRoute, private commonFunctions: CommonFunctions,public router:Router,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<AdvancedSearchConfigurationFormComponent>
    ){}
 
  ngOnInit(): void {
    console.log("this.data>>>>>>>>>>>>",this.data);
    //this.getColumnNames=GlobalConstants.getColumnsForDynamicSearch+this.data.objectId
    this.getColumnNames=GlobalConstants.getQueryHeadersForAdvancedSearch+this.data.objectId


  }

  fieldHasDefault(){
    if(!this.hasDefaultValue){
      this.hasDefaultValue=true;
    }else{
      this.hasDefaultValue=false;
      this.searchConfigForm.controls['defaultValues'].setValue('');

    }
  }
 
  closeDialog(): void {
    this.dialogRef.close();
  }
  closeDialogWithData(data:any){
    this.dialogRef.close(data);
  }

  checkIfCombo(){
    let typeSelected=this.searchConfigForm.get('colType').value;
    console.log("TYPE SELECTED >>>>>>>>>>>>>>",typeSelected);
    if(typeSelected=="Combo"){
      this.isCombo=true;
    }else{
      this.isCombo=false;
    }
  }

  async validateComboQuery(){
    const validateComboQueryApi = from(axios.get(GlobalConstants.validateComboQuery+this.searchConfigForm.get('comboSql').value.toString()));
    const validateComboQuery = await lastValueFrom(validateComboQueryApi);
    console.log("validateComboQuery>>>>>>>>>>>>>>>",validateComboQuery.data);
    if(validateComboQuery.data.length!=0 && validateComboQuery.data.length!=null){
      this.comboQueryIsValid=true;
      this.defaultValuesData=validateComboQuery.data;
    }else{
      this.comboQueryIsValid=false;
    }
  }

  requireRevalidation(){
    this.comboQueryIsValid=false;

  }

  setUpJsonAndSubmit(){
    if(this.searchConfigForm.get('colName').value!='' && this.searchConfigForm.get('colType').value!=''){
    let newConfigurationObject={
      colName:this.searchConfigForm.get('colName').value,
      colType:this.searchConfigForm.get('colType').value,
      comboSql:this.searchConfigForm.get('comboSql').value,
      isMandatory:this.searchConfigForm.get('isMandatory').value,
      isForForm:this.searchConfigForm.get('isForForm').value,
      isDefault:this.searchConfigForm.get('isDefault').value,
      defaultValues:this.searchConfigForm.get('defaultValues').value,
    }
    this.closeDialogWithData(newConfigurationObject);
  }else{
    this.commonFunctions.alert("alert","Fill the required fields!");
  }
  }

  switchMainDropDownValues(){
    if(this.searchConfigForm.get('isForForm').value==true){
      this.getColumnNames=GlobalConstants.getColumnsForDynamicSearch+this.data.objectId
    }else{
      this.getColumnNames=GlobalConstants.getQueryHeadersForAdvancedSearch+this.data.objectId
    }

  }
}
