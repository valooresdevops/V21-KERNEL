import { HttpClient } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { FormBuilder, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, from, lastValueFrom } from 'rxjs';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { FieldsetconfigurationComponent } from '../fieldsetconfiguration/fieldsetconfiguration.component';
import { NewbuttonComponent } from '../newbutton/newbutton.component';
import { ColumnModifierComponent } from '../column-modifier/column-modifier.component';
import { DataService } from 'src/app/Kernel/services/data.service';
import { LoaderService } from 'src/app/Kernel/services/loader.service';
import axios from 'axios';
import { OrderManagementComponent } from '../order-management/order-management.component';
import { CustomFieldComponent } from '../custom-field/custom-field.component';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-input',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.css']
})
export class InputComponent {

  @Input() fromScreenBuilder: String = "0";
  @Input() screenBuilderObjId: any;

  isStrikethroughCondition: boolean = true;

  isGrid: any;
  isQueryForm: any;
  hasMultipleSelection: any;
  isDynamicReport:any;
  test: any = [];
  AllTabs: any = [];
  public objectId: any;
  public objectPId: any;
  public columnDescription: any;
  public columnId: any;
  public isLink: any;
  public TabName: any;
  subsVar: Subscription;
  public columnsName: any[] = [];
  public isEditable: boolean = true;
  public canReadOnly: any;
  readOnly: boolean = false;
  public jsonEmpty: any[] = [];
  public getAllFieldSets: string;
  public fieldsetData: any[] = [];
  public test_1: string = '0';
  public fieldSetId: any;
  public selectedTabName: any;

  tableOptions1: any[] = []; // Initialize an empty array to hold the data for the tabs

  menuForm = new UntypedFormGroup({
    menuName: new UntypedFormControl('')
  });

  elemForm = new UntypedFormGroup({});
  hiddenForm = new UntypedFormGroup({});
  receivedismultiple: boolean;
  dialogRef: any;

  constructor(public commonFunctions: CommonFunctions,
    private _Activatedroute: ActivatedRoute,
    private eventEmitterService: EventEmitterService,
    private dialog: MatDialog,
    private dataService: DataService,
    public loaderService: LoaderService,
    public informationservice: InformationService,
    private route: ActivatedRoute,
    private router: Router) { }

  async getAllColums() {
    // Used to show loading animation
    this.loaderService.isLoading.next(true);

    this.test = [];
    let counter = 0;
    this.selectedTabName = this.informationservice.getSelectedTabName();
    if (this.informationservice.getSelectedTabName() != "-1") {
      for (let i = 0; i < this.AllTabs.length; i++) {
        if (this.AllTabs[i].menuName == this.informationservice.getSelectedTabName()) {
          this.objectId = this.AllTabs[i].objectId;

          const getAllFieldSetsApiURL = from(axios.get(GlobalConstants.getAllFieldSetsApi + this.objectId));
          const getAllFieldSetsApi = await lastValueFrom(getAllFieldSetsApiURL);
          this.fieldsetData = getAllFieldSetsApi.data;
          console.log("fieldsetData DATA API>>>>>>>>>>>",getAllFieldSetsApi.data);

          const getColumnsApiURL = from(axios.get(GlobalConstants.getColumnsApi + this.objectId));
          const getColumnsApi = await lastValueFrom(getColumnsApiURL);
          let getColumnsApiData = getColumnsApi.data;

          console.log("COLUMN DATA API>>>>>>>>>>>",getColumnsApi.data);
          
          for (let g = 0; g < this.fieldsetData.length; g++) {
            let dataa = getColumnsApiData.filter((el: any) => {
              return Number(el.groupId) === Number(this.fieldsetData[g].id);
            });

            this.test.push({ row_id: counter, fieldSetId: this.fieldsetData[g].id, fieldSetName: this.fieldsetData[g].name });
            for (let i = 0; i < dataa.length; i++) {
              dataa[i].row_id = counter;

              this.elemForm.addControl(dataa[i].name, new UntypedFormControl(''));
              this.isLink = dataa[i].isLink;
              if (dataa[i].defaultValue != undefined) {
                dataa[i].defaultValue = dataa[i].defaultValue + "_DV";
              }

              if (dataa[i].qbeReadOnly != undefined) {
                const getQbeIdApiURL = from(axios.post(GlobalConstants.getQbeIdApi + dataa[i].qbeReadOnly + "/0", this.jsonEmpty));
                const getQbeIdApi = await lastValueFrom(getQbeIdApiURL);
                let res = getQbeIdApi.data[0] == 1 ? true : false;
                dataa[i].qbeReadOnly = res;
              } else {
                dataa[i].qbeReadOnly = false;
              }

              if (dataa[i].query != undefined) {

                const getQbeIdApiURL = from(axios.post(GlobalConstants.getQbeIdApi + dataa[i].query + "/2", this.jsonEmpty));
                const getQbeIdApi = await lastValueFrom(getQbeIdApiURL);
                dataa[i].query = getQbeIdApi.data;
              }

              if (dataa[i].isSuspended == 1) {
                dataa[i].isSuspended = true;
              } else {
                dataa[i].isSuspended = false;
              }

              if (dataa[i].isMandatory == 1 || dataa[i].isMandatory == 2) {
                dataa[i].isMandatory = true;
              } else {
                dataa[i].isMandatory = false;
              }

              dataa[i].fieldSetId = '';
              dataa[i].fieldSetName = '';
              this.test.push(dataa[i]);
              counter += 1;
            }
          }
        }
      }
      console.log("TESTTTT>>>>>>>>>",this.test);
      // Used to hide loading animation
      this.loaderService.isLoading.next(false);
    }
  }

  ngOnDestroy() {
    if (this.subsVar) {
      this.subsVar.unsubscribe()
    }
  }

  async fetchMenuName() {
    const getNewMenuNameApiURL = from(axios.get(GlobalConstants.getNewMenuNameApi + this.objectPId));
    const getNewMenuNameApi = await lastValueFrom(getNewMenuNameApiURL);
    this.menuForm.controls['menuName'].setValue(getNewMenuNameApi.data.menuName);
  }

  async getAllTabs() {

    const getMenuNameApiURL = from(axios.get(GlobalConstants.getMenuNameApi + this.objectPId));
    const getMenuNameApi = await lastValueFrom(getMenuNameApiURL);
    this.AllTabs = getMenuNameApi.data;

    const getAllTabsApiURL = from(axios.get(GlobalConstants.getAllTabs + this.objectPId));
    const getAllTabsApi = await lastValueFrom(getAllTabsApiURL);

    for (let i = 0; i < getAllTabsApi.data.length; i++) {
      this.hiddenForm.addControl("hidden_" + getAllTabsApi.data[i].menuName + "_objId", new UntypedFormControl(''));
      this.hiddenForm.controls["hidden_" + getAllTabsApi.data[i].menuName + "_objId"].setValue(getAllTabsApi.data[i].objectId);

      var isDelete = getAllTabsApi.data[i].isMain == 1 ? 0 : 1;
      this.tableOptions1.push({
        "tableName": getAllTabsApi.data[i].menuName,
        "canAdd": getAllTabsApi.data[i].canAdd,
        "canDelete": getAllTabsApi.data[i].canDelete,
        "canModify": getAllTabsApi.data[i].canModify,
        "condition": getAllTabsApi.data[i].condition,
        "isSave": getAllTabsApi.data[i].isSave,
        "sourceQuery": getAllTabsApi.data[i].sourceQuery,
        "isAdvancedSearch": getAllTabsApi.data[i].isAdvancedSearch,
        "isGrid": getAllTabsApi.data[i].isGrid,
        "hasMultipleSelection": getAllTabsApi.data[i].hasMultipleSelection,
        "isQueryForm": getAllTabsApi.data[i].isQueryForm,
        "isDynamicReport": getAllTabsApi.data[i].isDynamicReport,
        "isReadOnly": getAllTabsApi.data[i].isReadOnly,
        "objectId": getAllTabsApi.data[i].objectId,
        "isMain": getAllTabsApi.data[i].isMain,
        "isDelete": isDelete,
        "tabId": "tabId_" + getAllTabsApi.data[i].objectId
      })
    }
    this.getAllColums();
  }

  ngOnInit(): void {
    this._Activatedroute.paramMap.subscribe((params) => {
      // this.objectId = params.get('childId');
      if (this.fromScreenBuilder == "1") {
        this.objectPId = this.screenBuilderObjId;
      } else {
        this.objectPId = params.get('parentId');
      }
      // localStorage.setItem("ObjectId",this.objectId);
      this.isGrid = params.get('isGrid');
      console.log("TABLE OPTIONS1>>>>>>>>>>>",this.tableOptions1);
      this.informationservice.setIsGrid(this.isGrid);

    });

/////breadcrumb////////////
    console.log("ROUTE URL>>>>>>>>>>>>",this.router.url);
    let breadCrumbData=this.informationservice.getNavBreadCrumb();
    breadCrumbData.push(JSON.parse('{"name":"' + "Form Configuration" + '","route":"' + this.router.url + '"}'))
    this.informationservice.setNavBreadCrumb(breadCrumbData);

    console.log("information srevice>>>>>>>>>>>>",this.informationservice.getNavBreadCrumb());

    let uniqueItems = this.informationservice.getNavBreadCrumb().reduce((acc, current) => {
      const x = acc.find((item: { name: any; }) => item.name === current.name);
      if (!x) {
        return acc.concat([current]);
      } else {
        return acc;
      }
    }, []);
    
    console.log("UNIQUE ARRAY>>>>>>>>>>",uniqueItems);
    this.informationservice.setNavBreadCrumb(uniqueItems);
    
    /////////////////////////
    this.getAllTabs();
    this.fetchMenuName();

    this.subsVar = this.eventEmitterService.onTabActionClick.subscribe(() => {
      setTimeout(() => {
        this.getAllColums();
      }, 1200);
    });

    this.subsVar = this.eventEmitterService.onTabSettingBtnClick.subscribe(() => {
      this.onTabModifyClick();
    });

    this.subsVar = this.eventEmitterService.onTabDeleteBtnClick.subscribe(() => {
      this.onTabDeleteClick();
    });

    this.receivedismultiple = this.dataService.getMessage();

    document.addEventListener("click", (event) => {
      $(".context-menu").addClass("hidden");
    });

  }



  onModifierClick(objectId: any, columnId: any, isLink: any) {
    // let data = [{ objectId: objectId, actionType: 'update', columnId: columnId, isLink: isLink }];
    // const dialogRef = this.dialog.open(ColumnModifierComponent, {
    //   width: "95%",
    //   height: "400x",
    //   data: data
    // });
    // dialogRef.disableClose = true;

    // dialogRef.afterClosed().subscribe(result => {
    //   localStorage.setItem("choosenTab", objectId);
    //   this.getAllColums();
    //   localStorage.removeItem("choosenTab");
    // });

    // localStorage.setItem("choosenTab", this.objectId);
    let url = "/dsp/augmentedConfig/form/update/"+this.objectPId+"/"+objectId+"/"+columnId+"/"+isLink+"/columnConfig";

    this.informationservice.setDynamicRuleBuilderReloadUrl(url);

    this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/update/"+this.objectPId+"/"+objectId+"/"+columnId+"/"+isLink+"/columnConfig");
    // localStorage.removeItem("choosenTab");
  }

  onPreviewForm() {
    this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/update/" + this.objectPId + "/-1/previewForm/");
  }

  onAddTabClick() {
    this.informationservice.setChoosenTab(this.objectId);

    this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/saveNew/" + this.objectPId + "/" + this.objectId + "/tabConfiguration");
    // localStorage.removeItem("choosenTab");
    this.informationservice.removeChoosenTab();
  }

  onAddButtonForm() {
    let data = [{ objectId: this.objectId, actionType: 'saveNew' ,isForAdvancedSearch:false}];
    const dialogRef = this.dialog.open(NewbuttonComponent, {
      width: "700px",
      height: "500px",
      data: data
    });
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);

      this.getAllColums();
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    });
  }

  onUpdateButtonForm(buttonId: number) {
    let data = [{ objectId: this.objectId, formBtnId: buttonId, actionType: 'update',isForAdvancedSearch:false }];
    const dialogRef = this.dialog.open(NewbuttonComponent, {
      width: "700px",
      height: "500px",
      data: data
    });
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);

      this.getAllColums();
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    });
  }

  async onDeleteButtonForm(buttonId: number) {
    try {
      const deleteButtonFormApi = from(axios.delete(GlobalConstants.deleteButton + buttonId+"/"+this.objectId+"/"+this.informationservice.getLogeduserId()));
      const deleteButtonForm = await lastValueFrom(deleteButtonFormApi);
      this.informationservice.setChoosenTab(this.objectId);

      this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectId);
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    } catch (error) {
      console.log("onDeleteButtonForm error >>>> ", error)
    }

    // this.http.delete(GlobalConstants.deleteButton + buttonId).subscribe((data: any) => {
    //   console.log("result of delete >> ", data);
    //   localStorage.setItem("choosenTab", this.objectId);
    //   this.commonFunctions.reloadPage('/dsp/augmentedConfig/form/update/' + this.objectId);
    // });
  }

  onTabModifyClick() {
    const childObjectId = this.hiddenForm.get("hidden_" + this.informationservice.getSelectedTabName() + "_objId").value;
    this.informationservice.setChoosenTab(this.objectId);

    this.commonFunctions.navigateToPage("/dsp/augmentedConfig/form/update/" + this.objectPId + "/" + childObjectId + "/tabConfiguration");
    // localStorage.removeItem("choosenTab");
    this.informationservice.removeChoosenTab();
  }

  async onTabDeleteClick() {
    const childObjectId = this.hiddenForm.get("hidden_" + this.informationservice.getSelectedTabName() + "_objId").value;
    try {
      const tabDeleteApi = from(axios.delete(GlobalConstants.deleteTabApi + childObjectId+"/"+this.informationservice.getLogeduserId()));
      const tabDelete = await lastValueFrom(tabDeleteApi);
      this.commonFunctions.reloadPage("/dsp/augmentedConfig/form/update/" + this.objectPId);
    } catch (error) {
      console.log("onTabDeleteClick error >>>> ", error)
    }
  }

  onFieldSetModifyClick(fieldsetId: number) {
    let data = [{ objectId: this.objectId, fieldsetId: fieldsetId, actionType: 'update' }];
    const dialogRef = this.dialog.open(FieldsetconfigurationComponent, {
      width: "500px",
      height: "400px",
      data: data
    });
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);

      this.getAllColums();
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    });
  }

  onFieldSetAddClick() {
    let data = [{ objectId: this.objectId, fieldsetId: -1, actionType: 'saveNew' }];
    const dialogRef = this.dialog.open(FieldsetconfigurationComponent, {
      width: "500px",
      height: "400px",
      data: data
    });
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);

      this.informationservice.setChoosenTab(this.objectId);

      this.getAllColums();
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    });
  }

  async onFieldSetBtndeleteClick(fieldsetId: number) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      content: 'Are you sure you want to delete ?',
    };

    let text = "Are you sure you want to delete ?";
    if (confirm(text) == true) {
      try {
        const onFieldSetBtndeleteURL = from(axios.delete(GlobalConstants.deleteFieldSetApi + fieldsetId));
        const onFieldSetBtndelete = await lastValueFrom(onFieldSetBtndeleteURL);
        this.informationservice.setChoosenTab(this.objectId);
        
        this.getAllColums();
        // localStorage.removeItem("choosenTab");
        this.informationservice.removeChoosenTab();
      } catch (error) {
        console.log("onFieldSetBtndeleteClick error >>>>>>>>>>>>> ", error);
      }
    } else {
      return;
    }
  }

  openOrderManagement() {
    let data = [{ objectId: this.objectId }];
    const dialogRef = this.dialog.open(OrderManagementComponent, {
      width: "800px",
      height: "600px",
      data: data
    });
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);

      this.getAllColums();
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    });
  }
  openNewCustomField() {
    let data = [{ objectId: this.objectId }];
    const dialogRef = this.dialog.open(CustomFieldComponent, {
      width: "600px",
      height: "450px",
      data: data
    });
    dialogRef.disableClose = true;
    dialogRef.afterClosed().subscribe(result => {
      this.informationservice.setChoosenTab(this.objectId);

      this.getAllColums();
      // localStorage.removeItem("choosenTab");
      this.informationservice.removeChoosenTab();
    });
  }

}


