import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, FormBuilder, FormArray } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription, from, lastValueFrom, map } from 'rxjs';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { AmPreviewFormComponent } from './am-preview-form/am-preview-form.component';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DataService } from 'src/app/Kernel/services/data.service';
import axios from 'axios';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { RefreshDialogService } from 'src/app/Kernel/services/refresh-dialog.service';
import { FileDownloadService } from 'src/app/Kernel/components/map/Services/FileDownloadService.service';
import { LoaderService } from 'src/app/Kernel/components/map/Services/loader.service';

@Component({
  selector: 'main-preview-form',
  templateUrl: './main-preview-form.component.html',
  styleUrls: ['./main-preview-form.component.css']
})



export class PreviewFormComponent implements OnInit {
  public isGridOptions:any;
  public isAMLoad:any;

  @Input() fromScreenBuilder: String = "0";
  @Input() screenBuilderObjId: any;
  public newVariableTitle: string = '';
  public mainPreviewDataFromMain:any[]=[];
  public subsVar: Subscription;
  public AllTabs: any = [];
  public tableOptions1: any[] = [];
  public objectId: any;
  public isAdvancedSearch: any;
  public test: any = [];
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public isGrid: any | undefined;
  public hasMultipleSelection: any | undefined;
  public isQueryForm: any | undefined;
  public isDynamicReport: any | undefined;
  public previewGridApi: string = '';
  public previewGridApiParam: any;
  public columnId: any;
  public test_1: string = '0';
  public actionType: any;
  public agGridSelectedNodes: any;
  public objectD: any;
  public hasSourceQuery: number = 0;
  public jsonQbe: any[] = [];
  public jsonQbeArr: any[] = [];
  public jsonEmpty: any[] = [];
  public gridStaticData: any;
  public canAdd: string = "";
  public canUpdate: string = "";
  public canDelete: string = "";
  public sourceQuery: string = "-1";
  public toolBar: string = "";
  public objectMain: any;
  public testLinks: any[] = [];
  public isLink: any;
  public queryType: any;
  public hiddenColumns: any[] = [];
  public datatobesent:any[]=[];
  public selectionType:any="single";
  hiddenForm = new UntypedFormGroup({});
  menuForm = new UntypedFormGroup({});
  public userId: number = Number(this.informationservice.getLogeduserId());
  public isTreeGrid: boolean =false;
  public isAdvancedHidden: boolean =false;
  public columnTypeCode: Number;
  public dataReceivedFromDynnamicApi:any[]=[];
  // Variables added by Nadine
  public buttonObjectId: any;
  public amInfo: any;
  public dialogArray: any[] = [];
  public getWhereCond: any = '-1';
  // public getWhereCond: any[] = [];
  public jp:boolean =true;
  isUpdateColsDef: number = 0;
  public isDialog: boolean = false;


  constructor(private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private _Activatedroute: ActivatedRoute,
    private eventEmitterService: EventEmitterService,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService,
    private refreshService: RefreshDialogService,
    public loaderService: LoaderService,
    private fileDownloadService:FileDownloadService
  ) { }

  onSearchSubmit(getWhereCond: any) {
   // this.getWhereCond = getWhereCond.data;

    this.getWhereCond = getWhereCond.result;
    if(getWhereCond.reloadGrid == true){
      this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectMain + '/-1/previewForm/');

    }
    this.getAllColums();
  }

  ngOnDestroy() {
    if (this.subsVar) {
      this.subsVar.unsubscribe()
    }
  }
  formatWhereCond(whereCond:any) {
    // Check if whereCond contains underscores
    if (whereCond.indexOf('_') !== -1) {
        // Replace underscores with spaces
        whereCond = whereCond.replace("_", " ");
    }
    // Add double quotes around the string
    whereCond = '"' + whereCond + '"';
    // Return the formatted whereCond
    return whereCond;
}

  getAllColums() {
    // Filter array on needed data
    let getMainTab = this.AllTabs.filter(function (el: any) {
      return el.isMain == 1;
    });
// this.AllTabs = getMenuNameApi.data;
//     // if (this.AllTabs.length == 1) {
//     //   $(".nav-tabs").hide();
//     // }

//     // localStorage.setItem("allTabs",this.AllTabs[0]);
//     // for(let i=1;i<this.AllTabs.length;i++){
//     // localStorage.setItem("allTabs",localStorage.getItem("allTabs")+~+this.AllTabs[i]);
//     // }
    // this.selectedTabName = this.informationservice.getSelectedTabName();


//     this.AllTabs = this.AllTabs.filter((el: any) => {
//       return el.menuName == this.selectedTabName;
//     });

    // if (this.AllTabs[0] == undefined)
    //   return;









    // for (let i = 0; i < this.AllTabs.length; i++) {


    this.objectId = getMainTab[0].objectId;
    let x = this.tableOptions1[0].canAdd == null ? -1 : this.tableOptions1[0].canAdd;
    let y = this.tableOptions1[0].canDelete == null ? -1 : this.tableOptions1[0].canDelete;
    let z = this.tableOptions1[0].canModify == null ? -1 : this.tableOptions1[0].canModify;
    let w = this.tableOptions1[0].sourceQuery == null ? -1 : this.tableOptions1[0].sourceQuery;
    this.jsonQbe = [];

    if (this.getWhereCond == undefined) {
      this.getWhereCond = ' ';

    } else {
    //  this.getWhereCond = this.formatWhereCond(this.getWhereCond);
    this.getWhereCond =this.getWhereCond ;
    }

    let jsonQbe_canAdd: any[] = [];
    jsonQbe_canAdd.push(
      {
        queryId: x,
        parameters: [
          {
            paramName: 'actionType',
            paramValue: '' + this.actionType + ''
          },
          {
            paramName: 'userId',
            paramValue: this.userId
          }
        ],
        link: [],
        isHidden: [],
        whereCond: this.getWhereCond
      }
    )

    this.http.post<any>(GlobalConstants.getQbeIdApi + x + "/0", jsonQbe_canAdd, { headers: GlobalConstants.headers }).subscribe((data: any) => {
      this.canAdd = data[0];

      let jsonQbe_canDelete: any[] = [];
      jsonQbe_canDelete.push(
        {
          queryId: y,
          parameters: [
            {
              paramName: 'actionType',
              paramValue: '' + this.actionType + ''
            },
            {
              paramName: 'userId',
              paramValue: this.userId
            }
          ],
          link: [],
          isHidden: [],
          whereCond: this.getWhereCond

        }
      )

      this.http.post<any>(GlobalConstants.getQbeIdApi + y + "/0", jsonQbe_canDelete, { headers: GlobalConstants.headers }).subscribe((data: any) => {
        this.canDelete = data[0];

        let jsonQbe_canUpdate: any[] = [];
        jsonQbe_canUpdate.push(
          {
            queryId: y,
            parameters: [
              {
                paramName: 'actionType',
                paramValue: '' + this.actionType + ''
              },
              {
                paramName: 'userId',
                paramValue: this.userId
              }
            ],
            link: [],
            isHidden: [],
            whereCond: this.getWhereCond
          }
        )

        this.http.post<any>(GlobalConstants.getQbeIdApi + z + "/0", jsonQbe_canUpdate, { headers: GlobalConstants.headers }).subscribe(async (data: any) => {
          this.canUpdate = data[0];

          if (this.canAdd == "1") {
            this.toolBar += "a";
          }

          if (this.canUpdate == "1") {
            this.toolBar += "m";
          }

          if (this.canDelete == "1") {
            this.toolBar += "d";
          }

          // this.http.get<any>(GlobalConstants.getParamsNameApi + w, { headers: GlobalConstants.headers }).subscribe((data0: any) => {
          //   if (data0 != null && data0.length != 0) {
          //     let colName = data0[0].paramName;
          //     let colVal = this.informationservice.getAgGidSelectedNode();
          //     colName = data0[0].paramName;
          //     if (colName == "userId") {
          //       this.jsonQbe = [
          //         {
          //           queryId: w,
          //           parameters: [
          //             {
          //               paramName: colName,
          //               paramValue: this.informationservice.getLogeduserId()
          //             }
          //           ],
          //           link: []
          //         }
          //       ]
          //     } else {
          //       this.jsonQbe = [
          //         {
          //           queryId: w,
          //           parameters: [
          //             {
          //               paramName: colName,
          //               paramValue: colVal
          //             }
          //           ],
          //           link: []
          //         }
          //       ]
          //     }
          //   } else {
          //     this.jsonQbe = [
          //       {
          //         queryId: w,
          //         parameters: [
          //           {
          //             paramName: '',
          //             paramValue: ''
          //           }
          //         ],
          //         link: []
          //       }
          //     ]
          //   }

          const getColumnsApiUrl0 = from(axios.get(GlobalConstants.getColumnsApi + this.objectId));
          const getColumns0Api = await lastValueFrom(getColumnsApiUrl0);
          let linkColumn: any = getColumns0Api.data.filter((el: any) => {
            return el.isLink === "1";
          });
          let hiddenColumn: any = getColumns0Api.data.filter((el: any) => {
            return el.isGridHidden === "1";
          });


          //elie///////////////////////////////////////////
          if(w!='' || w!=null){
          const getColumnsQueryApi = from(axios.get(GlobalConstants.getColumnsQuery + w));
          const getColumnsQuery = await lastValueFrom(getColumnsQueryApi);

          if(getColumnsQuery.data.length!=0){
          let linkQueryColumn: any = getColumnsQuery.data.filter((el: any) => {
            return el.isLink === "1";
          });
          for (let f = 0; f < linkQueryColumn.length; f++) {
            let item = {
              colName: linkQueryColumn[f].name,
              isLink: linkQueryColumn[f].isLink,
              menuName: linkQueryColumn[f].menus,
              colDesc:linkQueryColumn[f].columnDescription
            }
            this.testLinks.push(item);
          }
        }
      }
          /////////////////////////////////////////////////

          for (let f = 0; f < linkColumn.length; f++) {
            let item = {
              colName: linkColumn[f].name,
              isLink: linkColumn[f].isLink,
              menuName: linkColumn[f].menus,
              colDesc:linkColumn[f].columnDescription
            }
            this.testLinks.push(item);

          }
          for (let f = 0; f < hiddenColumn.length; f++) {
            let item = {
              colName: hiddenColumn[f].name,
              isGridHidden: hiddenColumn[f].isGridHidden,
              menuName: hiddenColumn[f].menus,
              colDesc:hiddenColumn[f].columnDescription
            }
            this.hiddenColumns.push(item);

          }

          let jsonQbe_sourceQuery: any[] = [];
          jsonQbe_sourceQuery.push(
            {
              queryId: w,
              parameters: [
                {
                  paramName: 'actionType',
                  paramValue: '' + this.actionType + ''
                },
                {
                  paramName: 'userId',
                  paramValue: this.userId
                }
              ],
              link: [],
              isHidden: [],
              objectId: this.objectId,
              whereCond: this.getWhereCond
            }
          )

          jsonQbe_sourceQuery.push(
            {
              queryId: w,
              parameters: [
                {
                  paramName: '',
                  paramValue: ''
                }
              ],
              link: this.testLinks,
              isHidden:[],
              objectId: this.objectId,
              whereCond: this.getWhereCond

            }
          );

          jsonQbe_sourceQuery.push(
            {
              queryId: w,
              parameters: [
                {
                  paramName: '',
                  paramValue: ''
                }
              ],
              link:[],
              isHidden: this.hiddenColumns,
              objectId: this.objectId,
              whereCond: this.getWhereCond

            }
          );
         this.http.post<any>(GlobalConstants.getQueryTypeApi + w, { headers: GlobalConstants.headers }).subscribe((data: any) => {
            if(data == 2){
              this.queryType =3;
            }else{
              this.queryType =1;
            }

          this.http.post<any>(GlobalConstants.getQbeIdApi + w + "/"+this.queryType, jsonQbe_sourceQuery, { headers: GlobalConstants.headers }).subscribe((data: any) => {
            this.sourceQuery = data;
            if (this.sourceQuery != "-1") {
              this.http.get<any>(GlobalConstants.getColumnsApi + this.objectId).subscribe((dataa: any) => {
                // let tableName = dataa[0].tableName;

                let gridHeaders = data[0].headers[0];
                let gridResults = data[0].result[0];
                this.agColumns = [];
                this.columnId = "ROW_ID";
                this.agColumnsJson = gridHeaders;
                this.agColumns.push(this.agColumnsJson);
                this.gridStaticData = gridResults;
                this.test_1 = '1';
                this.hasSourceQuery = 1;
              });
            } else {

              let jsonArr: string = "[";

              this.http.get<any>(GlobalConstants.getColumnsApi + this.objectId).subscribe((data: any) => {

                let dataa = data.filter((el: any) => {
                  return el.columnTypeCode != 14;
                });
                let uniqueArray = dataa.filter(
                  (item: any, index: any, self: any) =>
                    index === self.findIndex((t: any) => t.tableName === item.tableName)
                );

                setTimeout(() => {

                  let tblName: any;
                  let tableName = '';
                  let count = 0;
                  for (let k = 0; k < uniqueArray.length; k++) {
                    jsonArr += "{\"" + "columns" + "\" : [";
                    tableName = uniqueArray[k].tableName
                    for (let i = 0; i < dataa.length; i++) {
                      count++;
                      if (tableName == dataa[i].tableName) {
                        // tblName = tableName;
                        let colName = dataa[i].name;
                        jsonArr += "{";
                        if (i == dataa.length - 1) {
                          jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + colName + "\"";
                          jsonArr += "}";
                        } else {
                          jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + colName + "\"";
                          jsonArr += "},";
                        }
                      }
                    }

                    if (k == uniqueArray.length - 1) {
                      jsonArr += "],";
                      jsonArr += "\"" + "tableName" + "\"" + ":" + "\"" + tableName + "\"";
                      jsonArr += "}]";
                    }
                    else {
                      jsonArr += "],";
                      jsonArr += "\"" + "tableName" + "\"" + ":" + "\"" + tableName + "\"";
                      jsonArr += "},";
                    }
                    jsonArr = jsonArr.replace("},]", "}]");
                  }

                  let jsonData = JSON.parse(jsonArr);
                  let tableNames: string = '';
                  for (let u = 0; u < jsonData.length; u++) {
                    tableNames = tableNames + "," + jsonData[u].tableName;
                  }
                  tableNames = tableNames.substring(1);
                  this.http.post<any>(GlobalConstants.getColumnIdApi, tableNames).subscribe((data: any) => {
                    this.columnId = data.description;
                    this.informationservice.setROW_ID(data.description);

                    let jsonVal = [{
                      objectId: this.objectId,
                      selectedRowId: -1,
                      primaryColumn: this.columnId,
                      dynamicTable: JSON.parse(jsonArr)
                    }];

                    this.http.post<any>(GlobalConstants.getDynamicGridHeaders, jsonVal, { headers: GlobalConstants.headers }).subscribe((data: any) => {
                      this.agColumnsJson = data;
                      this.agColumns.push(this.agColumnsJson);
                      this.previewGridApi = GlobalConstants.getDynamicGridData;
                      this.previewGridApiParam = jsonVal;
                      this.test_1 = '1';
                      // this.getAllColums();
                    });
                  });
                }, 1000);
              });
            }
          });
        });
          // });
          // }
        });
      });
    });
    //   }
    // }
    this.http.get<any>(GlobalConstants.getColumnsApi + this.objectId).subscribe(async (data: any) => {
      for (let i = 0; i < data.length; i++) {
        this.menuForm.addControl(data[i].name, new UntypedFormControl(''));
        // nadine
        if (data[i].columnTypeCode == 14) {

          const getButtonDataUrl = from(axios.get(GlobalConstants.getButtonDataApi + data[i].id));
          const getButtonData = await lastValueFrom(getButtonDataUrl);

          let data1 = getButtonData.data;
          console.log("DATA BUTTON>>>>>>",data1);

          if (data1.blobFile != null && data1.blobFile != undefined) {
            const base64EncodedString = data1.blobFile;
            const decodedString = atob(base64EncodedString);
            console.log("DECODED STRING",decodedString);
            let n = decodedString.split("~A~");
            // let lastPart = n.pop();
            let lastPart = decodedString.split("~A~")[4];
            console.log("LAST PART>>>>>>>>>",lastPart);
            if (lastPart == "false") {
              data[i].mainAndPreview = false;
            } else {
              data[i].mainAndPreview = true;
            }
          }
        }
      }
      this.test = data;
     // console.log("ALL DATA>>>>>>>>>>>>>>>>>>>>>>>>",this.test);
    });
  }

  ngOnInit(): void {
    this.loaderService._loading.next(true);
    ///////////elie//////////////////
    this.informationservice.setDynamicReportId('');

     for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i);
      if (key && key.startsWith('~') && key!="~previousMainTab~") {
        localStorage.removeItem(key);
      }
    }
    ///////////////////      
    this.informationservice.removePopupBreadcrumb();
    this.informationservice.removeTabpath();

    this.tableOptions1 = [];
    this._Activatedroute.paramMap.subscribe(async (params) => {
      this.objectId = this.fromScreenBuilder == "1" ? this.screenBuilderObjId : params.get('parentId');
  await  this.http.get<any>(GlobalConstants.getAllTabs + this.objectId, { headers: GlobalConstants.headers }).subscribe((res: any) => {
        for (let i = 0; i < res.length; i++) {
          this.tableOptions1.push({
            "tableName": res[i].menuName, "canAdd": res[i].canAdd, "canDelete": res[i].canDelete,
            "canModify": res[i].canModify,
            "sourceQuery": res[i].sourceQuery,
            "isAdvancedSearch": res[i].isAdvancedSearch,
            "isGrid": res[i].isGrid,
            "isTreeGrid":res[i].isTreeGrid,
            "isAMLoad":res[i].isAMLoad,
            "isAdvancedHidden":res[i].isAdvancedHidden,
            "hasMultipleSelection": res[i].hasMultipleSelection,
            "isQueryForm": res[i].isQueryForm,
            "isDynamicReport": res[i].isDynamicReport,
            "isReadOnly": res[i].isReadOnly,
            "objectId": res[i].objectId,
            "isMain": res[i].isMain,
            "isRowGrouping": res[i].isRowGroup
          });

          ///////// elie  am preview form in main preview form/////////////
          this.mainPreviewDataFromMain= [{
            objectId: this.objectId,
            actionType: this.actionType,
            isFromGridClick: 1,
            primaryColumn: this.columnId,
            previousTab: "-1"
          }];
          console.log("DATA TO SEND ELIE>>>>>>>>>>",this.mainPreviewDataFromMain);
          ///////////////////////////////////////////////

          if(res[i].isTreeGrid == 1){
            this.isTreeGrid = true;
          }
          if (res[i].isMain == 1) {
            this.objectMain = res[i].objectId;
          }
          if(res[i].isAdvancedHidden == 1){
            this.isAdvancedHidden = true;
            this.informationservice.setAdvancedSearchShowGridMain(false);
          }
          if (res[i].isMain == "1") {

            let previousTab = this.informationservice.getPreviousMainTab();

            if (this.informationservice.getMainTab() != null && this.informationservice.getMainTab() != "")
            {
              this.isDialog = true;
              this.informationservice.setPreviousMainTab(this.informationservice.getMainTab())
            }
            if (previousTab) {

              this.informationservice.setMainTab(previousTab);
            }
            if (!previousTab) {
              this.informationservice.setMainTab(res[i].menuName);
            }
          }
        }
        
        
        if(this.tableOptions1[0].isDynamicReport=='1'){
            this.informationservice.setIsDynamicReport(true);
        }else{
          this.informationservice.setIsDynamicReport(false);
        }
        if(this.tableOptions1[0].hasMultipleSelection=='1'){
        this.selectionType="multiple";
      }else{
        this.selectionType="single";
      }
        this.tableOptions1 = this.tableOptions1.filter(value => value.isMain == '1');
        this.http.get<any>(GlobalConstants.getMenuNameApi + this.objectId).subscribe((data: any) => {
          this.AllTabs = data;
          let variableTitle = JSON.stringify(data[0].menuName);
          this.newVariableTitle = variableTitle.replace(/"/g, '');
          this.isGridOptions=this.tableOptions1[0].isGrid;
          this.isAMLoad=this.tableOptions1[0].isAMLoad;
          console.log("IS GRID OPTIONS>>>>>>>>>>>",this.isGridOptions);
          console.log("IS AM LOAD>>>>>>>>>>>",this.isAMLoad);

          this.getAllColums();
        });
      });
    });
    console.log("MAIN PREVIEW FORM TABLE OPTIONS>>>>>>>>>",this.tableOptions1);
    setTimeout(() => {
   
    }, 1000);


    this.loaderService._loading.next(false);
  }

  onAddClick() {

    this.informationservice.removePreviousTab();
    this.informationservice.removePreviousMainTab();
    this.informationservice.removeMainTab();
    this.informationservice.removeSelectedTabName();

    this.actionType = "saveNew";
    let data = [{
      objectId: this.objectMain,
      actionType: this.actionType,
      isFromGridClick: 1,
      primaryColumn: this.columnId,
      previousTab: "-1"
    }];
    console.log("DATA SENT TO AM PREVIEW FORM>>>>>>>>",data);
    this.informationservice.setIsFromMainPreviewForm(true);
    const dialogRef = this.dialog.open(AmPreviewFormComponent, {
      width: "80%",
      height: "80%",
      data: data
    });
    this.dataservice.PushdialogArray(dialogRef);
    this.dataservice.PushOpenLikeForm(this.informationservice.getFormToOpen());
    dialogRef.disableClose = true;


    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setIsFromMainPreviewForm(false);

      this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectMain + '/-1/previewForm/');
    });
  }

  onUpdateClick() {
    this.informationservice.removePreviousTab();
    this.informationservice.removePreviousMainTab();
    this.informationservice.removeMainTab();
    this.informationservice.removeSelectedTabName();

    this.actionType = "update";
    let data = [{
      objectId: this.objectMain,
      selectedRowId: this.informationservice.getAgGidSelectedNode(),
      isFromGridClick: 1,
      actionType: this.actionType,
      primaryColumn: this.columnId,
      previousTab: "-1"
    }];

    const dialogRef = this.dialog.open(AmPreviewFormComponent, {
      width: "80%",
      height: "80%",
      data: data
    });

    this.dataservice.PushdialogArray(dialogRef);
    this.dataservice.PushOpenLikeForm(this.informationservice.getFormToOpen());
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
    this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectMain + '/-1/previewForm/');
    });
  }



  dynamicFormQuering(data: any, actionType: string): string {
    if (actionType == 'select') {

      let jsonArr: string = "[";
      let uniqueArray = data.filter(
        (item: any, index: any, self: any) =>
          index === self.findIndex((t: any) => t.tableName === item.tableName)
      );

      let tableName = '';
      let count = 0;
      // for (let k = 0; k < uniqueArray.length; k++) {
      //   jsonArr += "{\"" + "columns" + "\" : [";
      //   tableName = uniqueArray[k].tableName
      //   for (let i = 0; i < data.length; i++) {
      //     count++;
      //     if (tableName == data[i].tableName) {
      //       let colName = data[i].name;
      //       let colType = data[i].columnType;
      //       orderNo = data[i].orderNo;
      //       jsonArr += "{";
      //       if (i == data.length - 1) {
      //         jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + colName + "\"" + "," + "\"" + "colType" + "\"" + ":" + "\"" + colType + "\"";
      //         jsonArr += "}";
      //       } else {
      //         jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + colName + "\"" + "," + "\"" + "colType" + "\"" + ":" + "\"" + colType + "\"";
      //         jsonArr += "},";
      //       }
      //     }
      //   }
      //   if (k == uniqueArray.length - 1) {
      //     jsonArr += "],";
      //     jsonArr += "\"" + "tableName" + "\"" + ":" + "\"" + tableName + "\"" + "," + "\"" + "orderNo" + "\"" + ":" + "\"" + orderNo + "\"";
      //     jsonArr += "}]";

      //     jsonArr = jsonArr.replace("},]", "}]");
      //   } else {
      //     jsonArr += "],";
      //     jsonArr += "\"" + "tableName" + "\"" + ":" + "\"" + tableName + "\"" + "," + "\"" + "orderNo" + "\"" + ":" + "\"" + orderNo + "\"";
      //     jsonArr += "},";
      //   }
      //   jsonArr = jsonArr.replace("},],", "}],");

      // }
      for (let k = 0; k < uniqueArray.length; k++) {
        jsonArr += "{\"" + "columns" + "\" : [";
        tableName = uniqueArray[k].tableName;
        let orderNo;
        for (let i = 0; i < data.length; i++) {
          count++;
          if (tableName == data[i].tableName) {
            let colId = data[i].colId;
            let colName = data[i].name;
            let colType = data[i].columnType;
            let isLink = data[i].isLink;
            orderNo = data[i].orderNo;
            let linkedObjectId = isLink != null ? data[i].menus : -1;
            jsonArr += "{";

            if (isLink == "1") {
              jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + colName + "\"" + "," + "\"" + "colType" + "\"" + ":" + "\"" + colType + "\"" + "," + "\"" + "isLink" + "\"" + ":" + "\"" + isLink + "\"" + "," + "\"" + "linkedObjectId" + "\"" + ":" + "\"" + linkedObjectId + "\"";
            } else {
              jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + colName + "\"" + "," + "\"" + "colType" + "\"" + ":" + "\"" + colType + "\"";
            }
            if (i == data.length - 1) {
              jsonArr += "}";
            } else {
              jsonArr += "},";
            }
          }
        }
        if (k == uniqueArray.length - 1) {
          jsonArr += "],";
          jsonArr += "\"" + "tableName" + "\"" + ":" + "\"" + tableName + "\""+ "," + "\"" + "orderNo" + "\"" + ":" + "\"" + orderNo + "\"";
          jsonArr += "}]";
          jsonArr = jsonArr.replace("},]", "}]");
        } else {
          jsonArr += "],";
          jsonArr += "\"" + "tableName" + "\"" + ":" + "\"" + tableName + "\"" + "," + "\"" + "orderNo" + "\"" + ":" + "\"" + orderNo + "\"";
          jsonArr += "},";
          jsonArr = jsonArr.replace("},]", "}]");
        }
      }
      return jsonArr;
    }
  }


  onDeleteClick() {

    let jsonArr = "[{";
    jsonArr += " \"" + "columns" + "\" : [";
    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    this.http.get<any>(GlobalConstants.getColumnsApi + this.objectId).subscribe((data: any) => {
      let jsonData = JSON.parse(this.dynamicFormQuering(data, 'select'));
      let dataa = jsonData.sort((b:any, a:any) => {
        return parseInt(a.orderNo) - parseInt(b.orderNo);
    });


      let jsonVal = [{
        objectId: this.objectId,
        selectedRowId: selectedNodes,
        primaryColumn: this.columnId,
        dynamicTable: dataa
      }];
      this.http.post<any>(GlobalConstants.dynamicDeleteFormBuilderApi, jsonVal,
        { headers: GlobalConstants.headers }).subscribe({
          next: (res) => {
            this.commonFunctions.alert("alert", 'Deleted Successfully');
            this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectMain + '/-1/previewForm/');
          },
          error: (error) => {
            console.log(error);
          }
        });
    });
  }

  async onShowButtonForm(buttonId: number,fromButtonOrSearch:string) {
    // let mainTab = this.informationservice.getMainTab();
    // const getButtonDataUrl = from(axios.get(GlobalConstants.getButtonDataApi + buttonId));
    // const getButtonData = await lastValueFrom(getButtonDataUrl);
    // let data1 = getButtonData.data;

    // if (data1.blobFile != null && data1.blobFile != undefined) {
    //   const base64EncodedString = data1.blobFile;
    //   const decodedString = atob(base64EncodedString);
    //   let GridToOpenID = decodedString.split("~A~")[0];
    //   let buttonAction: any = decodedString.split("~A~")[1];
    //   let url = decodedString.split("~A~")[3];
    let buttonMandatory='';
    let buttonPassed=true;
    console.log("BUTTON TRIGGERED!!!!!!!!!!!!!");
    this.informationservice.setPreviousTab(this.informationservice.getSelectedTabName());
    let mainTab = this.informationservice.getMainTab();
    let data1:any;
    let decodedString='';
    console.log("fromButtonOrSearch>>>>>>>>>>>>>>>",fromButtonOrSearch);
    if(fromButtonOrSearch=="dynamicButton"){

        const getButtonDataUrl = from(axios.get(GlobalConstants.getButtonDataApi + buttonId));
        const getButtonData = await lastValueFrom(getButtonDataUrl);
        data1 = getButtonData.data;
        this.columnTypeCode = data1.columnType;
        const base64EncodedString = data1.blobFile;
        decodedString = atob(base64EncodedString);

        console.log("ALL BUTTON DATA>>>>>>>>>>>>>",getButtonData.data);

    }else{

        const getSearchButtonFunctionDataApi = from(axios.get(GlobalConstants.getSearchButtonFunctionData + this.objectId));
        const getSearchButtonFunctionData = await lastValueFrom(getSearchButtonFunctionDataApi);
        data1 = getSearchButtonFunctionData.data[0];
        this.columnTypeCode = 14;
        decodedString=getSearchButtonFunctionData.data[0].blobFile;
    }
    console.log("DATA111111>>>>>>>>>>>>>",data1);

    console.log("buttonMandatory>>>>>>>>",data1.buttonMandatory);
    //console.log("form status>>>>>>>>",this.dynamicForm.status);

    //  if(data1.buttonMandatory=="1"){
    //   // console.log("AALOOOOOOOOOOO");
    //   // if (this.dynamicForm.status == 'INVALID'){
    //   //   console.log("FETET LA HONE BUTTONNNNN");
    //   //   this.submitForm();
    //   // }else{
    //   //   buttonPassed=true;

    //   // }
    // }else{
    //   buttonPassed=true;
    // }
    console.log("BUTTON PASSED>>>>>>>>>",buttonPassed);
    if(buttonPassed){
    if (data1.blobFile != null && data1.blobFile != undefined && data1.blobFile != "") {
      
      let splitedString = decodedString.split("|");
      console.log("SPLITED STRING>>>>>>>>>>>>>>",splitedString);
      splitedString.forEach(async (part, index) => {

      let procedureName = part.split("~A~")[0];
      let buttonAction: any = part.split("~A~")[1];
      let isMainPreview: any = part.split("~A~")[4];
      let params = part.split("~A~")[2];
      let url = part.split("~A~")[3];
      let otherCondition = part.split("~A~")[6];
      let alertValue = part.split("~A~")[7];
      let thirdCondition = part.split("~A~")[8];

      let alertMessage = part.split("~A~")[9];
      let jsonRequest = part.split("~A~")[10];
      let jsonResponse = part.split("~A~")[11];
      let selectedColsFormOpening = part.split("~A~")[12];



      this.buttonObjectId = procedureName;
        console.log("procedureName>>>>>>>>>",procedureName);
        console.log("buttonAction>>>>>>>>>",buttonAction);
        console.log("isMainPreview>>>>>>>>>",isMainPreview);
        console.log("params>>>>>>>>>",params);
        console.log("url>>>>>>>>>",url);
        console.log("otherCondition>>>>>>>>>",otherCondition);
        console.log("alertValue>>>>>>>>>",alertValue);
        console.log("thirdCondition>>>>>>>>>",thirdCondition);
        console.log("alertMessage>>>>>>>>>",alertMessage);
        console.log("jsonRequest>>>>>>>>>",jsonRequest);
        console.log("jsonResponse>>>>>>>>>",jsonResponse);
        console.log("selectedColsFormOpening>>>>>>>>>",selectedColsFormOpening);

      if (buttonAction == "5") {
        let customerId = -1;
        let userId = this.informationservice.getLogeduserId();
        let params = this.informationservice.getAgGidSelectedNode();
        customerId = JSON.parse(params)[0].COLVALUE;
        let customerType=JSON.parse(params)[2].COLVALUE;
        let jsonArr: string = "{";
        jsonArr += "\"" + "columns" + "\" : [";
        jsonArr += "{";
        jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + "kycId" + "\"" + "," + "\"" + "colVal" + "\"" + ":" + "\"" + customerId + "\"";
        jsonArr += "}"
        jsonArr += "]," + "\"" + "customerId" + "\"" + ":" + "\"" + customerId + "\"" + "," + "\"" + "userId" + "\"" + ":" + "\"" + userId + "\"" + "," + "\"" + "nearBy" + "\"" + ":" + "\"" + 0 + "\"" + "}";
        //  const callingApi = from(axios.post(url,JSON.parse(jsonArr)));
        //  const ResultOfCallingApi = await lastValueFrom(callingApi);
        let headerOptions = new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/pdf'
          //   'Accept': 'application/octet-stream', // for excel file
        });
        let requestOptions = { headers: headerOptions, responseType: 'blob' as 'blob' };

        if(customerType==='7'){
          url=url.replace("31860","31849");
        }
        // url is : executeReportwithOneParam/31849
        let ApiUrl = "http://" + GlobalConstants.endPointAddress + ":7001/api/" + url;

        this.http.post(ApiUrl, JSON.parse(jsonArr), requestOptions).pipe(map((data: any) => {
          let blob = new Blob([data], {
            type: 'application/pdf' // must match the Accept type
          });
          var link = document.createElement('a');
          link.href = window.URL.createObjectURL(blob);
          // link.download = 'samplePDFFile.pdf';
          link.target = '_blank';
          link.click();
          window.URL.revokeObjectURL(link.href);
        })).subscribe((result: any) => {
        });
      }
      else if(buttonAction == "3") {
        console.log("HERE FIRST");
        
        console.log("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        let dataa:any=[];
                // let formData = this.dynamicForm.value;
                // if (formData) {
                //   this.listOfData = JSON.parse(JSON.stringify(formData));
                //   this.listOfDataFormOpening = JSON.parse(JSON.stringify(formData));
                // }
                console.log("Button Data>>>>>>>",part);
           //     console.log("button action 1>>>>>>",formData);
                console.log("11111>>>>>>",part.split("~A~")[0]);
                console.log("22222>>>>>>",part.split("~A~")[1]);
                console.log("33333>>>>>>",part.split("~A~")[2]);
                console.log("44444>>>>>>",part.split("~A~")[3]);
                console.log("55555>>>>>>",part.split("~A~")[5]);
                console.log("66666>>>>>>",part.split("~A~")[6]);
                console.log("77777>>>>>>",part.split("~A~")[7]);
                console.log("88888>>>>>>",part.split("~A~")[8]);
                console.log("99999>>>>>>",part.split("~A~")[9]);
                console.log("10101010>>>>>>",part.split("~A~")[10]);
                console.log("11111111>>>>>>",part.split("~A~")[11]);
                console.log("SHOW API>>>>>>>>>>>>>>>>>>>>",url);
                const getApiJsonsApi = from(axios.get(GlobalConstants.getApiJsons + url));
                const getApiJsons = await lastValueFrom(getApiJsonsApi);
        
                let requestJsonString=getApiJsons.data[0].requestJson;
                for(let i=0;i<JSON.parse(jsonRequest).length;i++){
               //   for(let j=0;j<Object.keys(this.dynamicForm.value).length;j++){
                //    if(Object.keys(this.dynamicForm.value)[j]==JSON.parse(jsonRequest)[i].fieldName){
                //      requestJsonString=requestJsonString.replaceAll("#"+JSON.parse(jsonRequest)[i].jsonParameter+"#",this.dynamicForm.get(JSON.parse(jsonRequest)[i].fieldName).value);
                    }
              //    }
            //    }
        
                let responseJsonString=getApiJsons.data[0].responseJson;
                // console.log("jspnReponse>>>>>>>>>>>",JSON.parse(jsonResponse));
                // for(let i=0;i<JSON.parse(jsonResponse).length;i++){
                //   responseJsonString=responseJsonString.replaceAll("#"+JSON.parse(jsonResponse)[i].jsonParameter+"#",JSON.parse(jsonResponse)[i].fieldName);
                // }
                // console.log("method id>>>>>>>>>>>>>>",url);
                
                if(fromButtonOrSearch!='dynamicButton'){
                  requestJsonString=this.informationservice.getDynamicSearchApiData();
                }
        
                // let params: any;
                // if (this.amInfo.selectedRowId == undefined) {
                //   params = -1;
                // } else {
                //   params = typeof (this.amInfo.selectedRowId) == "string" ? JSON.parse(this.amInfo.selectedRowId) : this.amInfo.selectedRowId;
                // }
               // console.log("PARAMSSSSSSSSSS>>>>>>>>>>>>",params);
               // console.log("requestJsonString>>>>>>>>>>>>>>",requestJsonString);
               // console.log("responseJsonString>>>>>>>>>>>>>",responseJsonString);
               let json={};
               if(this.informationservice.agGidSelectedNode!=''){
                json={"requestJson":requestJsonString,
                          "responseJson":responseJsonString,
                          "rowid" : this.informationservice.getAgGidSelectedNode()
                }
              }else{
                json={"requestJson":requestJsonString,
                  "responseJson":responseJsonString
        }
              }
              console.log("JSON TO BE SENT>>>>>>>>>>>>>>>",json);
                   const runDynamicBuiltApi = from(axios.post(GlobalConstants.runDynamicBuiltApi+url,json));
                   const runDynamicBuilt = await lastValueFrom(runDynamicBuiltApi);
                console.log("RETURN DATA API>>>>>>>>>>",runDynamicBuilt.data);
             //   console.log("this.listOfHeaders>>>>>>>>>>",this.listOfHeaders);
        //jp1111111111111111111111111111
        
              //    dataa=runDynamicBuilt.data;
                this.dataReceivedFromDynnamicApi=runDynamicBuilt.data;
                console.log("DATAAAA>>>>>>>>>>>",this.dataReceivedFromDynnamicApi);

        }
                
        
        
        
        // let customerId = -1;
        // let userId = this.informationservice.getLogeduserId();
        // let params = this.informationservice.getAgGidSelectedNode();
        // customerId = JSON.parse(params)[0].COLVALUE;
        // let customerType=JSON.parse(params)[2].COLVALUE;
        // let jsonArr: string = "{";
        // jsonArr += "\"" + "columns" + "\" : [";
        // jsonArr += "{";
        // jsonArr += "\"" + "colName" + "\"" + ":" + "\"" + "kycId" + "\"" + "," + "\"" + "colVal" + "\"" + ":" + "\"" + customerId + "\"";
        // jsonArr += "}"
        // jsonArr += "]," + "\"" + "customerId" + "\"" + ":" + "\"" + customerId + "\"" + "," + "\"" + "userId" + "\"" + ":" + "\"" + userId + "\"" + "," + "\"" + "nearBy" + "\"" + ":" + "\"" + 0 + "\"" + "}";
        // //  const callingApi = from(axios.post(url,JSON.parse(jsonArr)));
        // //  const ResultOfCallingApi = await lastValueFrom(callingApi);
        // let headerOptions = new HttpHeaders({
        //   'Content-Type': 'application/json',
        //   'Accept': 'application/pdf'
        //   //   'Accept': 'application/octet-stream', // for excel file
        // });
        // let requestOptions = { headers: headerOptions, responseType: 'blob' as 'blob' };
        
        // // url is : executeReportwithOneParam/31849
        // let ApiUrl = "http://" + GlobalConstants.endPointAddress + ":7004/api/" + url;
        // this.http.post(ApiUrl, JSON.parse(params), requestOptions).pipe(map((data: any) => {
        // })).subscribe((result: any) => {
        // });
      

      else if (buttonAction == "1") {
        console.log("MENU OBJECT ID>>>>>>>>>>>",part.split("~A~")[0]);

        setTimeout(() => {
        console.log("HERE SECOND");

        let data:any;
        if(JSON.stringify(this.dataReceivedFromDynnamicApi)=="[]"){
          
        let selectedRowIdd = this.informationservice.getAgGidSelectedNode();
        let nodeData = JSON.parse(selectedRowIdd);
        let newSelectedRowIdd = nodeData.filter((el: any) => {
          return el.TYPE === "BUTTON";
        });
        console.log("DATA button222>>>",nodeData);
        console.log("DATA button3333>>>",newSelectedRowIdd);


        let params = '';
        console.log(" this.handleSelectedRowIds(newSelectedRowIdd,>>>>>>>>>>>>>>>",this.handleSelectedRowIds(newSelectedRowIdd, "button"));
        console.log("GRID DATA");

        data = [{
          actionType:'update',
          objectId: part.split("~A~")[0],
          isFromGridClick: 0,
          primaryColumn: this.columnId,
          buttonClick: 14,
          selectedRowId: this.handleSelectedRowIds(newSelectedRowIdd, "button")
        }];
      }else{
        console.log("API DATA");
        data = [{
          actionType:'update',
          objectId: part.split("~A~")[0],
          isFromGridClick: 0,
          primaryColumn: this.columnId,
          buttonClick: 14,
          selectedRowId: this.dataReceivedFromDynnamicApi
        }];
      }
      console.log("DATA TO BE SENT FROM MAIN PREVIEW FORM TO AM PREVIEW FORM EITHER BY GRID OR BY API>>>>>>>>>>>",data);
        
        let dialogRef = this.dialog.open(AmPreviewFormComponent, {
          width: "80%",
          height: "80%",
          data: data
        });
        this.dataservice.PushdialogArray(dialogRef);
        this.dataservice.PushOpenLikeForm(this.informationservice.getFormToOpen());
        this.dialogArray.push(dialogRef);
        console.log("objectId>>>>>>>>>>>",this.objectId);
        console.log("objectMain>>>>>>>>>>>",data[0].objectId);
        dialogRef.afterClosed().subscribe(result =>
        {
          this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectId + '/-1/previewForm/');

          $(".nav-tabs").show();
          $(".nav-tabs").css({ "flex-direction": "row" });

          setTimeout(() =>
          {
            this.refreshService.notifyOther({ refresh: true });
            let x = this.dataservice.getdialogArray();
            dialogRef = x[x.length - 1];
            x.pop();
            this.dataservice.SetdialogArray(x);

            let tabs = $(".tab-title");

            for (let i = 0; i < tabs.length; i++)
            {
              if (tabs[i].className.indexOf("active") != -1)
              {
                $("#" + tabs[i].offsetParent.id + " .tab .tab-title")[0].click();
              }
            }
          }, 1000);

          this.informationservice.setSelectedTabName(mainTab);

          for (let i = 0; i < JSON.parse(this.informationservice.getTabpath()).length; i++)
          {
            if (mainTab == JSON.parse(this.informationservice.getTabpath())[i].tabTitle)
            {
              this.amInfo = JSON.parse(this.informationservice.getTabpath())[i].tabData;
              //this.loadAM(true);
            }
          }
        });
      }, 2000);

      }else if(buttonAction == "6"){
        this.informationservice.setISExport(false);
        this.informationservice.setIsDisplayALL(false);

        this.dialog.closeAll();
      
      }
      else if(buttonAction == "8"){
        this.informationservice.setIsDisplayALL(false);

let dataselected:any=JSON.parse(this.informationservice.getAgGidSelectedNode());
for(let i=0;i<dataselected.length;i++)
  {

    this.downloadHtmlFile(dataselected[i].COLVALUE);

  }
// this.dialog.closeAll();
this.informationservice.setISExport(true);

      }
      else if(buttonAction == "10"){

        
        this.informationservice.setIsDisplayALL(true);

        this.dialog.closeAll();
                }
              }
              )}
          }
            }


  handleSelectedRowIds(data: any, compare: string) {
    let newDD: any;
    try {
      let dd: any;
      console.log("DATA RECEIVED>>>>>>>>>",data);
      if (typeof (data) == "string") {
        let d = data.toLowerCase();
        dd = JSON.parse(d);
        console.log("DATA RECEIVED22222222222>>>>>>>>>",dd);

      } else {
        let d = JSON.stringify(data);
        dd = JSON.parse(d.toLowerCase());
        console.log("DATA RECEIVED3333333>>>>>>>>>",dd);

      }


      if (compare.indexOf(",") != -1) {
        newDD = dd.filter((el: any) => {
          console.log("NEW DDDDDDDD1111111");

          return el.type.toLowerCase() === compare.split(",")[0] || el.type.toLowerCase() === compare.split(",")[1];
        });
      } else {
        newDD = dd.filter((el: any) => {
          console.log("NEW DDDDDDD2222222");

          return el.type.toLowerCase() === compare.toLowerCase();
        });
      }
    } catch (error) {
      newDD = -1;
    }
    console.log("NEW DDDDDDDD");
    return newDD;
  }



  downloadHtmlFile(ID:any): void {
    this.fileDownloadService.downloadFile2('SimulationReport_'+ID+'.html',ID);
  }
 
  closeDialog()
  {
    this.dialog.closeAll();
  }

}




