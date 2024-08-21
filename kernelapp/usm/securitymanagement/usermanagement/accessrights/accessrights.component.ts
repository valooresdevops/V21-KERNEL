import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { Application } from 'src/app/Kernel/ArrayList/Application';
import { AGGridComponent } from 'src/app/Kernel/components/v-grid/v-grid.components';
import {  GridApi } from 'ag-grid-community';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { CheckboxCellRenderer } from 'src/app/Kernel/components/v-grid/components/v-grid-checkbox';
import { CheckboxCellRendererByCell } from 'src/app/Kernel/components/v-grid/components/v-grid-checkbox-cell';

declare let alertify: any;

@Component({
  selector: 'accessrights',
  templateUrl: './accessrights.component.html',
  styleUrls: ['./accessrights.component.css']
})
export class AccessrightsComponent implements OnInit {
  @ViewChild(AGGridComponent) agGrid: any;

  agColumns: AgColumns[] = [];
  public gridApi: GridApi = new GridApi();
  public gridColumnApi: any;
  public agColumnsJson: any;
  public content: any;
  
  public menuPath: any = this.informationservice.getMenuPath();
  public menuPaths: any;
  public id: string = '';
  public showHidefield = false;
  public comboAppType: any;
  public comboAppMenu: any = '';
  public accessRights: any;
  public isShown: boolean = true;
  public isRequired: boolean = true;
  public userLogedId: any = '';
  subsVar: Subscription;
  test = false;
  public disabledApplication: boolean = false;
  public updateToshow: boolean = true;
  public createToShow: boolean;
  public UserId: any;
  public RoleId: any;
  public TestList: Application[] = [];
  frameworkComponents:any;



  public actionType: string = '';
  accessRightsForm = new FormGroup({
    applicationName: new FormControl(''),
    applicationMenu: new FormControl(''),
    subLevel: new FormControl(''),
    inColumn: new FormControl(''),
    inRights: new FormControl(''),
    isZeroAccess: new FormControl(''),

  });

  public previousnodesBAR: any;

  constructor(private router: Router, private commonFunctions: CommonFunctions, private http: HttpClient, private _Activatedroute: ActivatedRoute, private eventEmitterService: EventEmitterService,
    public informationservice: InformationService) {
      
      this.frameworkComponents = {
        checkboxRenderer: CheckboxCellRenderer,
      };
     }

  ngOnInit(): void {

   this.UserId = this.informationservice.getUserId();
   this.comboAppType = GlobalConstants.fetchUSMRolesAppComboApi;
    this._Activatedroute.paramMap.subscribe(params => {
      this.id = params.get('id');
      this.actionType = params.get('actionType');
    });
    if (this.menuPath == 'usm/role') {
     // this.id;
      this.showHidefield = true;
      if (this.actionType == "create") {
        // value of the user id is :
        
        this.RoleId = this.informationservice.getRoleId();
        // this.updateToshow = false;
        this.createToShow = true;
        // this.disabledApplication = false;
      }
      if (this.actionType == "update") {
        this.disabledApplication = true;
        this.accessRightsForm.controls['applicationName'].setValue(this.id);
        this.onAppName(this.id);
        // this.updateToshow = true;
        this.createToShow = false;
      }
    } else {

    
    if (this.menuPath == 'usm/userMgmt') {


      if (this.actionType == "create") {
        // value of the user id is :
        
        this.UserId = this.informationservice.getUserId();
        // this.updateToshow = false;
        this.createToShow = true;
        // this.disabledApplication = false;
      }
      if (this.actionType == "update") {
        this.disabledApplication = true;
        this.accessRightsForm.controls['applicationName'].setValue(this.id);
        this.onAppName(this.id);
        // this.updateToshow = true;
        this.createToShow = false;
      }
    }
  }

    this.userLogedId = this.informationservice.getLogeduserId();
    //this is the column definition
    this.agColumnsJson = [
    { headerName: 'Menu Name', field: 'name', filter: 'agTextColumnFilter' },
    { headerName: 'id', field: 'id', filter: 'agTextColumnFilter', hide: "true" },
    { headerName: 'Display', field: 'isDisplay',      cellRenderer: CheckboxCellRendererByCell,
     },
      { headerName: 'Add', field: 'isAdd',   cellRenderer: CheckboxCellRendererByCell,
       },
      { headerName: 'Modify', field: 'isModify',   cellRenderer: CheckboxCellRendererByCell,
       },
      { headerName: 'Delete', field: 'isDelete',     cellRenderer: CheckboxCellRendererByCell,
       },
      { headerName: 'Print', field: 'isPrint',    cellRenderer: CheckboxCellRendererByCell,
       },
      { headerName: 'Export', field: 'isExport',    cellRenderer: CheckboxCellRendererByCell,
       },
      { headerName: 'Translate', field: 'isTranslate',   cellRenderer: CheckboxCellRendererByCell,
       },
      //{ headerName: 'OrderMenu', field: 'orderCode', hide: "true" }

    ];
    this.agColumns.push(this.agColumnsJson);

    //this is the data list


    //used on updating data in grid
    this.subsVar = this.eventEmitterService.onSaveClick.subscribe((value: { updateList: any; }[]) => {

      if (this.menuPath == 'usm/userMgmt') {
        this.menuPaths = 'userMgmt';

      }
      else {
        this.menuPaths = 'role';
      }


      let val = value[0].updateList;


    });
      // let appValue = this.formatAppValue(this.accessRightsForm.get('applicationName')?.value);

    // console.log("LLLLLLLLLLLLLLLLLLLLLLLLLL>>>>>>>>>>>>>>>>>>>",appValue);

    // this.accessRights = GlobalConstants.getAccessRightsGrid+appValue
    // console.log("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY>>>>>> USER ID", this.UserId);
    // console.log("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ>>>>>> ROLE ID", this.RoleId);
    // console.log("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK>>>>>> TABLE ID", this.id);


    //  /////////////////////////////////////// filling the grid with the previous data
    // setTimeout(() => {
      this.fetchGrid()

      
    // }, 500);

 //////////////////////////////////////////////////////////////////////////////////////////////////////////////

  }

  fetchGrid() {
    
    let appValue = this.formatAppValue(this.accessRightsForm.get('applicationName')?.value);
    this.http.post<any>(GlobalConstants.fetchPreviousSelectedNodesBAR + this.UserId+ "/" + appValue, { headers: GlobalConstants.headers }).subscribe(
    (res: any) => {
      console.log("ressssssssssss>>>",res);
 // Transform the response to the desired format
 this.previousnodesBAR = res.map((node: {
   menu_name: string; menu_code: string; access_code: string; 
}) => {
  if (node.access_code && typeof node.access_code === 'string') {
    return {
      'id': node.menu_code,
      'name':node.menu_name,
      'isDisplay': node.access_code.charAt(0) ,
      'isAdd': node.access_code.charAt(1) ,
      'isModify': node.access_code.charAt(2) ,
      'isDelete': node.access_code.charAt(3),
      'isPrint': node.access_code.charAt(4),
      'isExport': node.access_code.charAt(5),
      'isTranslate': node.access_code.charAt(6) 
    };
  } else {
    return {
      'id': node.menu_code,
      'name': node.menu_name,
      'isDisplay': '',
      'isAdd': '',
      'isModify': '',
      'isDelete': '',
      'isPrint': '',
      'isExport': '',
      'isTranslate': ''
    };
  
  }
});
console.log("PREVIOUS SELECTED NODES>>>>>>>>>>>>>>", this.previousnodesBAR);
this.agGrid.rowData = this.previousnodesBAR;

    

})

  }
  Destroy() {
    if (this.subsVar) {
      this.subsVar.unsubscribe()
    }
  }
  //it is called after the all afterGuiAttached in the same row
  //it might not be needed
  checkNameLevel(params: any) {
    console.log("params.data.orderCode>>>>>>>>>>>>>>>>>>>>>>>>", params.value);
    let value = '';
    
    value = params.value;

    return value;
  }
 
  //to return the id of the selected cell
  getSelectedCells() {
    const selectedNodes = this.gridApi.getSelectedNodes();
    const selectedData = selectedNodes.map(node => node.data);
    const selectedIds = selectedData.map(data => data.id);
    return selectedIds;

  }

  onAppMenuChange() {
    // this.isShown = false;
    console.log("onAppMenuChange >>> ");
  }

  onAppName(value: any) {
    // this.isShown = false;
    // this.comboAppMenu = GlobalConstants.fetchUSMAppMenuComboApi + value;
  }

  saveApplication(){
    let applicationName = this.formatAppValue(this.accessRightsForm.get('applicationName')?.value);


///////////////////////////////////////////////////////////////////////////////////
let selectedCells = this.informationservice.getAgGidSelectedCell().replace(/,(\s*[\]}])/g, '$1');
let selectedNodesJson = JSON.parse(selectedCells);
let uniqueNodesMap = new Map();

selectedNodesJson.forEach((node: { [x: string]: any; }) => {
  let id = node['id'];
  let accessCode = `${node['isDisplay']}${node['isAdd']}${node['isModify']}${node['isDelete']}${node['isPrint']}${node['isExport']}${node['isTranslate']}`;
  uniqueNodesMap.set(id, { "id": id, "access_code": accessCode });
});

let selectedNodesBAR = Array.from(uniqueNodesMap.values());
let selectedNodesBAR_string = JSON.stringify(selectedNodesBAR)

console.log("SELECTED NODES BAR:",selectedNodesBAR_string);
///////////////////////////////////////////////////////////////////////////////////////////////


let jsonParams = {};
if (this.menuPath == 'usm/userMgmt') {
  this.commonFunctions.alert("alert", "value of the user id is >> "+this.UserId);
  
let userRoleId=this.informationservice.getUserRoleId();
if(this.UserId!=null){

  jsonParams = {
    userId: this.UserId,
    userRoleId:userRoleId,
    menuCode: applicationName,
    createdBy: this.informationservice.getLogeduserId(),
    accessCode: selectedNodesBAR_string

  };
}else{
  jsonParams = {
    userId: this.UserId,
    userRoleId:"-1",
    menuCode: applicationName,
    createdBy: this.informationservice.getLogeduserId(),
    accessCode: selectedNodesBAR_string

 };

}
   } else if (this.menuPath == 'usm/role') {
    this.commonFunctions.alert("alert", "value of the role id is >> "+this.RoleId);
    jsonParams = {
      roleId: this.RoleId,
      menuCode: applicationName,
      createdBy: this.informationservice.getLogeduserId(),
      accessCode: selectedNodesBAR_string

   };

      }
      this.http.post<any>(GlobalConstants.AddApplicationInAccessRight, jsonParams,
        { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          if (res.status == 'Fail') {
            this.commonFunctions.alert("alert", res.description);
          } else {
           console.log("save success !!")
          }
        });
//if(this.menuPath=='/usm/userMgmt'){
  
this.router.navigateByUrl("/usm/userMgmt/form/update/"+this.informationservice.getAgGidSelectedNode());
// }else{
  
//   this.commonFunctions.reloadPage('/usm/role/form/update/'+this.informationservice.getUserRoleId());
// }
//console.log("SUCCESSFUL SAVE FINALLY GOOD JOB!!!!!!!!!!!!!!!!!")

  }

  onRowSelected(event : any){

    // console.log("event is :",event);
    
    // console.log("value of the id selected is >>> ",this.informationservice.getAgGidSelectedNode());

  }

  //adedd by mira to handle the updatedParams received from the aggrid to be sent to the api
  //updatedParams has the same form as the json received from the api but with updated values according to the checkboxes-------------
  onParamsChange(updatedParams: any[]): void {
    if (updatedParams && updatedParams.length > 0) {
      if (this.accessRightsForm.status != "INVALID") {
        this.onAppMenuChange();
        // this.isShown = true;
        if (this.menuPath == 'usm/userMgmt') {
          this.menuPaths = 'userMgmt';
          $("#formRow").removeClass('vh-100');
          
          let id = this.informationservice.getUserId();
          
          let userRole=this.informationservice.getUserRoleId();
          if (userRole!=null){

            // console.log("GlobalConstants.headers before post >>>>>>>>>>>>>>>>>",GlobalConstants.headers);
            
             this.http.post<any>('http://10.1.8.94:8088/api/updateAccessRightsApi/'+this.informationservice.getUserId()+ "/" +userRole+"/" + this.menuPaths + "/" + this.accessRightsForm.get('applicationName')?.value
             + "/" + this.accessRightsForm.get('applicationMenu')?.value
             + "/" + this.accessRightsForm.get('subLevel')?.value
             + "/" + this.accessRightsForm.get('inColumn')?.value,updatedParams).subscribe({
               next:(res: any) => {
                 this.commonFunctions.alert("alert", res.description);
               },
               error:(error) => {
                 console.log(error);
               }
           });
          } else{
            // console.log("GlobalConstants.headers before post >>>>>>>>>>>>>>>>>",GlobalConstants.headers);
            
            this.http.post<any>('http://10.1.8.94:8088/api/updateAccessRightsApi/'+this.informationservice.getUserId()+ "/-1/" + this.menuPaths + "/" + this.accessRightsForm.get('applicationName')?.value
            + "/" + this.accessRightsForm.get('applicationMenu')?.value
            + "/" + this.accessRightsForm.get('subLevel')?.value
            + "/" + this.accessRightsForm.get('inColumn')?.value,updatedParams).subscribe({
              next:(res: any) => {
                this.commonFunctions.alert("alert", res.description);
              },
              error:(error) => {
                console.log(error);
              }
          });
          }
        } else if (this.menuPath == 'usm/role'){
          this.menuPaths = 'role';
          $("#formRow").removeClass('vh-100');
          
          let id = this.informationservice.getRoleId();
         // console.log("GlobalConstants.headers before post >>>>>>>>>>>>>>>>>",GlobalConstants.headers);
         
          this.http.post<any>('http://10.1.8.57:8088/api/updateAccessRightsApi/'+this.informationservice.getRoleId()+ "/-1/" + this.menuPaths + "/" + this.accessRightsForm.get('applicationName')?.value
          + "/" + this.accessRightsForm.get('applicationMenu')?.value
          + "/" + this.accessRightsForm.get('subLevel')?.value
          + "/" + this.accessRightsForm.get('inColumn')?.value,updatedParams).subscribe({
            next:(res: any) => {
              this.commonFunctions.alert("alert", res.description);
            },
            error:(error) => {
              console.log(error);
            }
        });
        }


      }
    }
  }
  formatAppValue(appValue: string): string {
    if (appValue.length === 1) {
      return "00" + appValue;
    } else if (appValue.length === 2) {
      return "0" + appValue;
    }
    return appValue;
  }

}