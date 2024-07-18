import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { FormUpdateComponent } from '../../form/form-update/form-update.component';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';


import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-new-tab',
  templateUrl: './new-tab.component.html',
  styleUrls: ['./new-tab.component.css']
})
export class NewTabComponent implements OnInit {

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public AllTableName: any = [];
  public objectId: any;
  public objectPId: any;
  public groupingGrid = GlobalConstants.getTableGroupingApi;
  public inDisplayGrid = GlobalConstants.fetchInDispTablesApi;
  public getTabTables: string = "";
  public getSourceQuery = GlobalConstants.getSourceQueryApi;
  public isGrid: any;
  public isQueryForm: any;
  public hasMultipleSelection: any;
  public isDynamicReport: any;
  public actionType: any;
  public menuName: any;
  public isMainTab: any;
  public test: any;
  dialogRef: any;
  showAddSearchProcedure:any;
  showQueryFormButtonCombo:any;
  public getAllProcAndPack: string = '';
  showCallApi:any;
  showisTitle:any;
  public getAllColumns: any;
  queryFormpossibleButtons:any[]=[{id:'1',name:'Save'},{id:'2',name:'Execute'},{id:'3',name:'Rule'}];

  // public reloadUrl: string = '';

  newTabForm = new UntypedFormGroup({
    tabName: new UntypedFormControl(''),
    tableName: new UntypedFormControl(''),
    orderField: new UntypedFormControl(''),
    isGrid: new UntypedFormControl(''),
    isQueryForm: new UntypedFormControl(''),
    hasMultipleSelection: new UntypedFormControl(''),
    isDynamicReport: new UntypedFormControl(''),
    readOnlyQbeId: new UntypedFormControl(''),
    isAdvancedSearch: new UntypedFormControl(''),
    sourceQuery: new UntypedFormControl(''),
    canAdd: new UntypedFormControl(''),
    canDelete: new UntypedFormControl(''),
    canModify: new UntypedFormControl(''),
    condition: new UntypedFormControl(''),
    isSave: new UntypedFormControl(''),
    isQueryFormSelectedButtons: new UntypedFormControl(''),
    addSearchProcedure:new UntypedFormControl(''),
    callRestApi:new UntypedFormControl(''),
    isDynamicTitleEnabled:new UntypedFormControl(''),
    dynamicTitleName:new UntypedFormControl(''),
    canImport: new UntypedFormControl(''),

    callRestApi2:new UntypedFormControl(''),
  });
  agPrimaryKey: any;

  constructor(private dialog: MatDialog, private http: HttpClient, private _Activatedroute: ActivatedRoute, private commonFunctions: CommonFunctions,
    
    public informationservice: InformationService ) { }

  ngOnInit(): void {
    this.getAllProcAndPack = GlobalConstants.getAllProcAndPack;
    this._Activatedroute.paramMap.subscribe((params) => {
      this.objectId = params.get('childId');
      this.objectPId = params.get('parentId');
      this.actionType = params.get('actionType');

      // this.reloadUrl = "/dsp/augmentedConfig/form/update/" + this.objectPId + "/" + this.objectId + "/tabConfiguration";

      let url = "/dsp/augmentedConfig/form/update/" + this.objectPId + "/" + this.objectId + "/tabConfiguration";

      this.informationservice.setDynamicRuleBuilderReloadUrl(url);

      this.agColumnsJson = [
        {
          headerName: '',
          field: '',
          width: '25px',
          checkboxSelection: true
        },
        {
          headerName: 'ID',
          field: 'id',
          defaultMinWidth: '180',
          filter: 'agTextColumnFilter',
          hide: true,
          suppressColumnsToolPanel: true
        },
        {
          headerName: 'Table Name',
          field: 'tableName',
          defaultMinWidth: '180',
          filter: 'agTextColumnFilter',
          sortable: true,
          enableRowGroup: true
        },
        {
          headerName: 'Order Number',
          field: 'orderNo',
          filter: 'agTextColumnFilter',
          sortable: true
        }
      ];

      this.agColumns.push(this.agColumnsJson);

      this.getTabTables = GlobalConstants.getTabTables + "/" + this.objectId;

      if (this.actionType == "update") {
        this.fetchTabData();
        this.getAllColums();
      }

      if (this.actionType == "saveNew") {
        this.getAllColums();
      }
    });
    this.getAllColumns= GlobalConstants.getAllColumnsTitle +this.objectId;
    console.log('getAllColumns>>>>>>>',this.getAllColumns)
  }
  onAddClick() {
    let data = [{ objectId: this.objectId, actionType: 'saveNew', objectPId: this.objectPId }];

    const dialogRef = this.dialog.open(FormUpdateComponent, {
      width: "800px",
      height: "650px",
      data: data
    });
    dialogRef.disableClose = true;

    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);
      
      this.commonFunctions.reloadPage("/dsp/augmentedConfig/form/update/" + this.objectPId + "/" + this.objectId + "/tabConfiguration");

    });

  }
  onUpdateClick() {

    let data = [{ objectId: this.objectId, actionType: 'update', objectPId: this.objectPId }];
console.log('data--------->',data)
    const dialogRef = this.dialog.open(FormUpdateComponent, {
      width: "800px",
      height: "650px",
      data: data
    });
    dialogRef.disableClose = true;

    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);
      this.informationservice.setChoosenTab(this.objectId);



      this.commonFunctions.reloadPage("/dsp/augmentedConfig/form/update/" + this.objectPId + "/" + this.objectId + "/tabConfiguration");

    });

  }
  onDeleteClick() {
    let NewArrayUpdateJson: any[] = []
    
    let selectedRow = JSON.parse(this.informationservice.getAgGidSelectedNode());
    // let allColumns :any =  GlobalConstants.GetColVal+"/"+selectedRow[0].tableName;
    let tableId :any;
    // let tableName :any;
    // let ownerName :any;
    // let orderNo :any;

    for(let i=0;i < selectedRow.length ; i++)
    {
      // if(selectedRow[i].rowSlectedStatus == "insert")
      // {
        tableId=selectedRow[i].id;
        // tableName=selectedRow[i].tableName;
        // ownerName=selectedRow[i].ownerName;
        // orderNo=selectedRow[i].orderNo;

     // }
    }

    this.http.post<any>(GlobalConstants.getTabTablesFormRecords + this.objectId+"/"+tableId, { headers: GlobalConstants.headers, }).subscribe(
      (res: any) => {
        let arrayOfObject : any;

        // for (var i = 0; i < res.length; i++) {
          // columns.push(res[i].columnName );

          // console.log("data[i].name >> ",data[i].name)

          let deleteJson: string = '';
          for (let i = 0; i < res.length; i++)
          {
            if (i == res.length - 1)
            {
              deleteJson += "{" + "\"" + "columnName" + "\"" + ":" + "\"" + res[i].columnName + "\"" + "}";
              deleteJson = "[" + deleteJson + "]";
              arrayOfObject = 
              {
                "tableOwner":  res[i].tableOwner,
                "tableName":  res[i].tableName,
                "columns": JSON.parse(deleteJson),
                "rowSlectedStatus": "deleted",
                "orderNo":  res[i].orderNo,
                "updatedBy":this.informationservice.getLogeduserId()
              };
              NewArrayUpdateJson.push(arrayOfObject);
            } else
            {
              deleteJson += "{" + "\"" + "columnName" + "\"" + ":" + "\"" + res[i].columnName  + "\"" + "}" + ",";
            }
          }

          // NewArrayUpdateJson.push(object);

        // }

        // for (let i = 0; i < data.length; i++)
        // {

        //   console.log("data[i].name >> ",data[i].name)
        //   let object=
        //   {"ownerName":selectedRow[0].tableName.split('.')[0],
        //   "tableName":selectedRow[0].tableName.split('.')[1],
        //   "columnName":data[i].NAME, "rowSlectedStatus":"deleted"};
        //   NewArrayUpdateJson.push(object);

        // }

        this.http.post<any>(GlobalConstants.updateGridApi + this.objectId, NewArrayUpdateJson, { headers: GlobalConstants.headers }).subscribe(
          (res: any) => {
            if (res.status == 'Fail') {
              this.commonFunctions.alert("alert", res.description);
            } else {
              this.commonFunctions.alert("alert", res.description);
              this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/update/" + this.objectPId);
            }
          });


      });
  }

  submitForm() {
    if (this.newTabForm.status != "INVALID") {
      let sourceQuery = this.newTabForm.controls['sourceQuery']?.value;
        let canAdd = this.newTabForm.controls['canAdd']?.value;
        let canDelete = this.newTabForm.controls['canDelete']?.value;
        let canModify = this.newTabForm.controls['canModify']?.value;
        let readOnlyQbeId = this.newTabForm.controls['readOnlyQbeId']?.value;
        let condition =  this.newTabForm.controls['condition']?.value;
        let isReadOnly = this.newTabForm.controls['isReadOnly']?.value;
        let isAdvencedSearchProcedure = this.newTabForm.controls['addSearchProcedure']?.value;
        let isDynamicTitleEnabled =  this.newTabForm.controls['isDynamicTitleEnabled']?.value;
        let dynamicTitleName =  this.newTabForm.controls['dynamicTitleName']?.value;
        let canImport = this.newTabForm.controls['canImport']?.value;

        let callRestApi =  this.newTabForm.controls['callRestApi']?.value;
        let callRestApi2 =  this.newTabForm.controls['callRestApi2']?.value;
        if(condition == undefined){
          condition = 0;
        }
        if(isReadOnly == undefined){
          isReadOnly = 0;
        }
        if(canAdd == undefined || canAdd == ""){
          canAdd = 0;
        }
        if(canDelete == undefined){
          canDelete = 0;
        }
        if(canModify == undefined){
          canModify = 0;
        }
        if(sourceQuery == ""){
          sourceQuery = null;
        }
        if(readOnlyQbeId == undefined){
          readOnlyQbeId = 0;
        }
        if(canImport == undefined || canImport == ""){
          canImport = 0;
        }
      if (this.actionType == 'saveNew') {
        let List = [];
        // let NewArray = this.informationservice.getAgGidSelectedNode().split(',');
        // console.log("NewArray for aggrid selected node is <<<<<<<<<<<< ", NewArray);
        // for (let i = 0; i < NewArray.length; i++) {
        const jsonParams = {
          // ownerName: NewArray[i].split(".")[0],
          // tableName: NewArray[i].split(".")[1],
          menuName: this.newTabForm.controls['tabName']?.value,
          objectPId: this.objectPId,
          orderNo: this.newTabForm.controls['orderField']?.value,
          isGrid: this.newTabForm.controls['isGrid']?.value,
          hasMultipleSelection: this.newTabForm.controls['hasMultipleSelection']?.value,
          isQueryForm: this.newTabForm.controls['isQueryForm']?.value,
          isDynamicReport: this.newTabForm.controls['isDynamicReport']?.value,
          isQueryFormSelectedButtons:JSON.stringify(this.newTabForm.controls['isQueryFormSelectedButtons']?.value),
          //isHidden: this.newTabForm.controls['isHidden']?.value,
          isReadOnly: isReadOnly,
          isAdvancedSearch: this.newTabForm.controls['isAdvancedSearch']?.value,
          sourceQuery: sourceQuery,
          canAdd: canAdd,
          canDelete: canDelete,
          canModify: canModify,
          condition: condition,
          advancedSearchProcedureName:isAdvencedSearchProcedure,
          userId: this.informationservice.getLogeduserId(),
          isSave: this.newTabForm.controls['isSave']?.value,
          callRestApi: false,
          isDynamicTitleEnabled: this.newTabForm.controls['isDynamicTitleEnabled']?.value,
          dynamicTitleName: dynamicTitleName,
          canImport: canImport,
          

        };
        List.push(jsonParams);
        this.isGrid = this.newTabForm.controls['isGrid']?.value;
        this.hasMultipleSelection = this.newTabForm.controls['hasMultipleSelection']?.value;
        this.isQueryForm = this.newTabForm.controls['isQueryForm']?.value;
        this.isDynamicReport = this.newTabForm.controls['isDynamicReport']?.value;
        
        // }

        this.http.post<any>(GlobalConstants.createTabApi, List, { headers: GlobalConstants.headers }).subscribe(
          (res: any) => {
            if (res.status == 'Fail') {
              this.commonFunctions.alert("alert", res.description);
            } else {
              this.commonFunctions.alert("alert", res.description);
              
              // const insertQueryFormButton=from(axios.get(GlobalConstants.insertQueryFormButton))
              // const getQueryParams=await lastValueFrom(getQueryParamsApi);

              this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/update/" + this.objectPId);
            }
          });

      }

      if (this.actionType == 'update') {
        let updateList = [];
        
        const jsonParams = {
          menuName: this.newTabForm.controls['tabName']?.value,
          objectPId: this.objectPId,
          orderNo: this.newTabForm.controls['orderField']?.value,
          isGrid: this.newTabForm.controls['isGrid']?.value,
          isQueryForm: this.newTabForm.controls['isQueryForm']?.value,
          hasMultipleSelection: this.newTabForm.controls['hasMultipleSelection']?.value,
          isDynamicReport: this.newTabForm.controls['isDynamicReport']?.value,
          isQueryFormSelectedButtons:JSON.stringify(this.newTabForm.controls['isQueryFormSelectedButtons']?.value),
          isMain: this.isMainTab,
          //isHidden: this.newTabForm.controls['isHidden']?.value,
          isReadOnly: isReadOnly,
          isAdvancedSearch: this.newTabForm.controls['isAdvancedSearch']?.value,
          sourceQuery: sourceQuery,
          canAdd: canAdd,
          canDelete: canDelete,
          canModify: canModify,
          condition: condition,
          advancedSearchProcedureName:isAdvencedSearchProcedure,
          canImport: canImport,
          callRestApi:this.newTabForm.controls['callRestApi']?.value,
          callRestApi2:callRestApi2,
          isDynamicTitleEnabled: isDynamicTitleEnabled,
          dynamicTitleName: dynamicTitleName,
          userId: this.informationservice.getLogeduserId(),
          isSave: this.newTabForm.controls['isSave']?.value,
          readOnlyQbeId: readOnlyQbeId
        };
        updateList.push(jsonParams);

        this.http.post<any>(GlobalConstants.updateTabApi + this.objectId, updateList, { headers: GlobalConstants.headers }).subscribe(
          (res: any) => {
            if (res.status == 'Fail') {
              this.commonFunctions.alert("alert", res.description);
            } else {
              this.commonFunctions.alert("alert", res.description);
              this.informationservice.setChoosenTab(this.objectId);
              
              this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/update/" + this.objectPId);
            }
          });
      }
    }
  }

  getAllColums() {
    
    let tableName = this.informationservice.getSelectedTabName();
    this.http.get<any>(GlobalConstants.getColumnsApi + this.objectId).subscribe((data: any) => {
      for (let i = 0; i < data.length; i++) {
      }
      this.test = data;
    });
  }


  fetchTabData() {

    this.http.get<any>(GlobalConstants.getTabConfigurationApi + this.objectId, { headers: GlobalConstants.headers, }).subscribe(
      async (res: any) => {
        this.newTabForm.controls['tabName'].setValue(res[0].menuName);
        this.newTabForm.controls['orderField'].setValue(res[0].orderNo);
        this.newTabForm.controls['sourceQuery'].setValue(res[0].sourceQuery);
        this.newTabForm.controls['canAdd'].setValue(res[0].canAdd);
        this.newTabForm.controls['canDelete'].setValue(res[0].canDelete);
        this.newTabForm.controls['canModify'].setValue(res[0].canModify);
        this.newTabForm.controls['condition'].setValue(res[0].condition);
        this.newTabForm.controls['isSave'].setValue(res[0].isSave);
        this.newTabForm.controls['readOnlyQbeId'].setValue(res[0].readOnlyQbeId);
        this.newTabForm.controls['addSearchProcedure'].setValue(res[0].advancedSearchProcedureName);
        this.newTabForm.controls['isDynamicTitleEnabled'].setValue(res[0].isDynamicTitleEnabled);
        this.newTabForm.controls['canImport'].setValue(res[0].canImport);
        this.newTabForm.controls['callRestApi2'].setValue(res[0].apiFunctionName);

        if (res[0].isGrid == "0") {
          this.newTabForm.controls['isGrid'].setValue(false);
        } else {
          this.newTabForm.controls['isGrid'].setValue(true);
        }
        if (res[0].isApiEnabled == "0") {
          this.newTabForm.controls['callRestApi'].setValue(false);

        } else {
          this.newTabForm.controls['callRestApi'].setValue(true);
          this.showCallApi=true;
        }
        if (res[0].isDynamicReport == "0") {
          this.newTabForm.controls['isDynamicReport'].setValue(false);
        } else {
          this.newTabForm.controls['isDynamicReport'].setValue(true);
        }

        if (res[0].hasMultipleSelection == "0") {
          this.newTabForm.controls['hasMultipleSelection'].setValue(false);
        } else {
          this.newTabForm.controls['hasMultipleSelection'].setValue(true);
        }

        if (res[0].isQueryForm == "0") {
          this.newTabForm.controls['isQueryForm'].setValue(false);
          this.showQueryFormButtonCombo=false;
          this.newTabForm.controls['isQueryFormSelectedButtons'].setValue('');

        } else {
          this.newTabForm.controls['isQueryForm'].setValue(true);
          this.showQueryFormButtonCombo=true;
           const getQueryFormButtonApi=from(axios.get(GlobalConstants.getQueryFormButton+this.objectId));
           const getQueryFormButton=await lastValueFrom(getQueryFormButtonApi);

          this.newTabForm.controls['isQueryFormSelectedButtons'].setValue(getQueryFormButton.data);



        }
        // if (res[0].isReadOnly == "0") {
        //   this.newTabForm.controls['isReadOnly'].setValue(false);
        // } else {
        //   this.newTabForm.controls['isReadOnly'].setValue(true);
        // }

        // if (res[0].isSave == "0") {
        //   this.newTabForm.controls['isSave'].setValue(false);
        // } else {
        //   this.newTabForm.controls['isSave'].setValue(true);
        // }

        if (res[0].isAdvancedSearch == "0") {
          this.showAddSearchProcedure = false;
          this.newTabForm.controls['isAdvancedSearch'].setValue(false);
        } else {
          this.showAddSearchProcedure = true;
          this.newTabForm.controls['isAdvancedSearch'].setValue(true);
        }
        if (res[0].isDynamicTitleEnabled == "0") {
          this.showisTitle = false;
          this.newTabForm.controls['isDynamicTitleEnabled'].setValue(false);
        } else {
          this.showisTitle = true;

          this.newTabForm.controls['isDynamicTitleEnabled'].setValue(true);
        }
        this.isMainTab = res[0].isMain;

        this.commonFunctions.handleLookupElem("canAdd", this.newTabForm);
        this.commonFunctions.handleLookupElem("canModify", this.newTabForm);
        this.commonFunctions.handleLookupElem("condition", this.newTabForm);
        this.commonFunctions.handleLookupElem("isSave", this.newTabForm);
        this.commonFunctions.handleLookupElem("canDelete", this.newTabForm);
        this.commonFunctions.handleLookupElem("sourceQuery", this.newTabForm);
        this.commonFunctions.handleLookupElem("readOnlyQbeId", this.newTabForm);
        this.commonFunctions.handleLookupElem("canImport", this.newTabForm);

        res.objectId = this.objectId;

      });


    this.http.get<any>(GlobalConstants.getSelectedRowsApi + this.objectId, { headers: GlobalConstants.headers }).subscribe(
      (res: any) => {
        
        this.informationservice.setAgGidSelectedNode(JSON.stringify(res));
      });
  }

  isQueryFormChange(){
    if(this.newTabForm.get('isQueryForm').value==true){
      this.showQueryFormButtonCombo=true;
      //this.newTabForm.controls['isQueryFormSelectedButtons'].setValue('');
    }else{
      this.showQueryFormButtonCombo=false;
    }
    
  }


  isAdvancedSearchChange(){
    if(this.newTabForm.get('isAdvancedSearch').value==true){
      this.showAddSearchProcedure=true;
    }else{
      this.showAddSearchProcedure=false;
      // this.newTabForm.controls['addSearchProcedure'].setValue('');    
    }
  }


  isTitleChange(){
    if(this.newTabForm.get('isDynamicTitleEnabled').value==true){
      this.showisTitle=true;
    }else{
      this.showisTitle=false;
      // this.newTabForm.controls['isTitleField'].setValue('');    
    }
  }

  isDynamicReportChange(){
    
    
  }

  isCallApi(){
    if(this.newTabForm.get('callRestApi').value==true){
      this.showCallApi=true;
    }else{
      this.showCallApi=false;
    }
  }
  isTitleFieldChange(){
    console.log('<><><><> ',this.newTabForm.controls['isTitleField']?.value)
  }

}
