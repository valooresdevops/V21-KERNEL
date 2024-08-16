import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { ButtonJsonRelationComponent } from './button-json-relation/button-json-relation.component';

@Component({
  selector: 'app-newbutton',
  templateUrl: './newbutton.component.html',
  styleUrls: ['./newbutton.component.css']
})
export class NewbuttonComponent implements OnInit {

  public buttonPosition: any;
  public objectId: any;
  public AllFieldSet: any;
  public actionType: string = '';
  public buttonId: number;
  public AllAction: any = [{ id: 1, name: 'Form Opening' }, { id: 2, name: 'Call Procedure' },{ id: 3, name: 'Call Api' },{ id: 4, name: 'Close Popup'},{ id: 5, name: 'Generate Report'},{ id: 6, name: 'Next Action'},{ id: 7, name: 'Form Opening No Link'},{ id: 8, name: 'Export'}];
  public FormOpeningSelected: boolean = false;
  Condition: boolean= false;
  ApiSelected: boolean = false;
  ApiSelected1: boolean = false;
  showApiBuilderJson: boolean = false;

  public exportSelected: boolean = false;

  public AllMenus: any;
  public objectButtonId: any;
  public ALLcol : any;
  public buttonActions: number[] = [];
  public requestJsonParams:any[]=[];
  public responseJsonParams:any[]=[];
  public requestSavedParams:any[]=[];
  public responseSavedParams:any[]=[];
  // public getAllTablesCombo: any = GlobalConstants.getAllTablesCombo;
  public getSourceQuery = GlobalConstants.getSourceQueryApi;
  public getApiBuilderListDropDown = GlobalConstants.getApiBuilderListDropDown;

  constructor(private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public lookupData: any,
    private dialogRef: MatDialogRef<NewbuttonComponent>,
    private commonFunctions: CommonFunctions,
    public informationservice: InformationService,
    private dialog: MatDialog) { }

  buttonForm = new UntypedFormGroup({
    buttonId: new UntypedFormControl(''),
    buttonName: new UntypedFormControl(''),
    buttonOrder: new UntypedFormControl(''),
    fieldSet: new UntypedFormControl(''),
    ButtonAction: new UntypedFormControl(''),
    Menus: new UntypedFormControl(''),
    procedureName: new UntypedFormControl(''),
    allColumns: new UntypedFormControl(''),
    condition: new UntypedFormControl(''),
    OtherCondition: new UntypedFormControl(''),
    url : new UntypedFormControl(''),
    alertValue : new UntypedFormControl(''),
    isMainPreview: new UntypedFormControl(''),
    thirdCondition: new UntypedFormControl(''),
    alertMessage : new UntypedFormControl(''),
    Path : new UntypedFormControl(''),
    nbOfAction : new UntypedFormControl(''),
    requestJson : new UntypedFormControl(''),
    responseJson : new UntypedFormControl(''),
  });

  closeDialog(): void {
    this.dialogRef.close();
  }
  
  async ngOnInit(): Promise<void> {
    this.FormOpeningSelected = false;
    this.Condition = false;
     this.ApiSelected = false;
    this.ApiSelected1 = false;
    let ButtonAction : any;

    this.objectId = this.lookupData[0].objectId;
    this.actionType = this.lookupData[0].actionType;
    this.AllMenus = GlobalConstants.getMenusButton;
    this.buttonPosition = GlobalConstants.getButtonPosition;
    this.AllFieldSet = GlobalConstants.getAllFieldSets + this.objectId;
    let isMain = this.buttonForm.controls['isMainPreview']?.value;

    // this.ALLcol = GlobalConstants.getColumnsApi + this.objectId;

    const allColuumsUrl = from(axios.get(GlobalConstants.getColumnsApi + this.objectId));
    const allColuums = await lastValueFrom(allColuumsUrl);
    this.ALLcol = allColuums.data;
    
    if (this.actionType == "update") {
      this.buttonId = this.lookupData[0].formBtnId;

      const buttonDataUrl = from(axios.get(GlobalConstants.getButtonDataApi + this.buttonId));
      const buttonData = await lastValueFrom(buttonDataUrl);

           let data = buttonData.data;
          const base64EncodedString = data.blobFile;
          const decodedString = atob(base64EncodedString);
          let nbOfAction = decodedString.split("~N~")[1];
          const parts = decodedString.split('~N~');
          let relevantPart = parts[2];
          console.log("RELEVANT PART>>>>>>>>>>>>>>",relevantPart);
          this.buttonForm.controls["nbOfAction"].setValue(nbOfAction);
          this.onNbOfActionInput(); // This will create the form controls based on nbOfAction

          //let actionsArray = relevantPart.split('|');
          let splitedString = relevantPart.split("|");
          splitedString.forEach(async (part, index) => {         
          let menuId = part.split("~A~")[0];
          ButtonAction = part.split("~A~")[1];
          let isMainPreview: any = part.split("~A~")[4];
          this.buttonForm.controls[`ButtonAction${index-1}`]?.setValue(ButtonAction);
          this.buttonForm.controls["buttonName"].setValue(data.columnName);
          this.buttonForm.controls["buttonOrder"].setValue(data.orderNo);
          this.buttonForm.controls["fieldSet"].setValue(data.groupId);
          this.buttonForm.controls["buttonId"].setValue(data.columnId);
          this.buttonForm.controls["Menus"].setValue(menuId);
          this.buttonForm.controls["ButtonAction"].setValue(ButtonAction);
          if(isMainPreview == "false"){
            this.buttonForm.controls["isMainPreview"].setValue(false);
          }
          else if(isMainPreview == "true"){
            this.buttonForm.controls["isMainPreview"].setValue(true);
          }
       
          if (ButtonAction == 1) {
            this.FormOpeningSelected = true;
            this.Condition = true;
          }

          if(ButtonAction == 2) {
            this.buttonForm.controls["procedureName"].setValue(menuId);
            this.buttonForm.controls["allColumns"].setValue(part.split("~A~")[2]);

            this.ApiSelected = true;
            this.ApiSelected1 = true;
            this.Condition = true;
          }
          
          if(ButtonAction == 3) {
            this.buttonForm.controls["url"].setValue(part.split("~A~")[3]);
            this.buttonForm.controls["allColumns"].setValue(part.split("~A~")[2])
            this.ApiSelected1 = true;
            this.FormOpeningSelected = false;
          }

          if(ButtonAction == 4){
          }

          if(ButtonAction == 5) {
            this.buttonForm.controls["url"].setValue(part.split("~A~")[3]);
            this.buttonForm.controls["allColumns"].setValue(part.split("~A~")[2])

            this.ApiSelected1 = true;
          }
          if (ButtonAction == 7) {
            this.FormOpeningSelected = true;
            this.Condition = true;
          } 

          if (ButtonAction == 8) {
            this.exportSelected=true;

          } 
      
        });
      
    }
  }

//   submitForm() {
//     if (this.buttonForm.status != 'INVALID') {
//       if (this.actionType == 'saveNew') {
//         let buttonName = this.buttonForm.controls['buttonName']?.value;
//         let buttonOrder = this.buttonForm.controls['buttonOrder']?.value;
//         let buttonAction = this.buttonForm.controls['ButtonAction']?.value;
//         let fieldSet = this.buttonForm.controls['fieldSet']?.value;
//         let objectButtonId;
//         let selectedCols;
//         let URL;
//         let isMainPreview;
//         let condition;
//         let OtherCondition;
//         let alertValue;
//         let thirdCondition;
//         let alertMessage;
//         let PATH;

//         if(buttonAction == 1){
//           objectButtonId = this.buttonForm.controls['Menus']?.value;
//           selectedCols = '';
//           URL = '';
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }else if(buttonAction == 2){
//           objectButtonId = this.buttonForm.controls['procedureName']?.value;
//           selectedCols = this.buttonForm.controls['allColumns']?.value;
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;

//         }
//         else if(buttonAction == 3){
//           selectedCols = this.buttonForm.controls['allColumns']?.value;
//           URL = this.buttonForm.controls['url']?.value;
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;

//         }
//         else if(buttonAction == 4){
//           selectedCols = '';
//           URL = '';
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = null;
//           OtherCondition = null;
//           alertValue = null;
//           thirdCondition = null;
//           alertMessage = null;
//         }
//         else if(buttonAction == 5){
//           selectedCols = this.buttonForm.controls['allColumns']?.value;
//           URL = this.buttonForm.controls['url']?.value;
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;

//         }if(buttonAction == 7){
//           objectButtonId = this.buttonForm.controls['Menus']?.value;
//           selectedCols = '';
//           URL = '';
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }
//         if(buttonAction == 8){
//           PATH= this.buttonForm.controls['Path']?.value;
//           console.log("pathh>>",PATH);
//           if(PATH==""){
            
//           }
//         }
//         let object = {
//           "buttonName": buttonName,
//           "position": 0,
//           "order": buttonOrder,
//           "fieldSet": fieldSet,
//           "createdBy": this.informationservice.getLogeduserId(),
//           "objectButtonId": objectButtonId + "~A~" + buttonAction + "~A~" + selectedCols + "~A~"+ URL + "~A~" +isMainPreview + "~A~" +condition + "~A~" + OtherCondition + "~A~" + alertValue + "~A~" + thirdCondition + "~A~" + alertMessage
//         }

// console.log("object ===",object);        

//         this.http.post<any>(GlobalConstants.createFormButton + this.objectId, object, { headers: GlobalConstants.headers }).subscribe(
//           (res: any) => {
//             if (res.status == 'Fail') {
//               this.commonFunctions.alert("alert", res.description);
//             } else {
//               this.commonFunctions.alert("alert", res.description);
//             }
//           });
//       }

//       if (this.actionType == 'update') {
//         let buttonId = this.buttonForm.controls['buttonId']?.value;
//         let buttonName = this.buttonForm.controls['buttonName']?.value;
//         let buttonOrder = this.buttonForm.controls['buttonOrder']?.value;
//         let fieldSet = this.buttonForm.controls['fieldSet']?.value;
//         let buttonAction = this.buttonForm.controls['ButtonAction']?.value;
        
//         let objectButtonId;
//         let selectedCols;
//         let URL;
//         let isMainPreview;
//         let condition;
//         let OtherCondition;
//         let alertValue;
//         let thirdCondition;
//         let alertMessage;
//         if(buttonAction == 1){
//           objectButtonId = this.buttonForm.controls['Menus']?.value;
//           selectedCols = '';
//           URL = '';
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }else if(buttonAction == 2){
//           objectButtonId = this.buttonForm.controls['procedureName']?.value;
//           selectedCols = this.buttonForm.controls['allColumns']?.value;
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }
//         else if(buttonAction == 3){
//           selectedCols = this.buttonForm.controls['allColumns']?.value;
//           URL = this.buttonForm.controls['url']?.value;
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }
//         else if(buttonAction == 4){
//           selectedCols = '';
//           URL = '';
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = null;
//           OtherCondition = null;
//           thirdCondition = null;
//           alertValue = null;
//         }
//         ///////elie///////////////
//         else if(buttonAction == 5){
//           selectedCols = this.buttonForm.controls['allColumns']?.value;
//           URL = this.buttonForm.controls['url']?.value;
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }else if(buttonAction == 7){
//           objectButtonId = this.buttonForm.controls['Menus']?.value;
//           selectedCols = '';
//           URL = '';
//           isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
//           condition = this.buttonForm.controls['condition']?.value;
//           OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
//           alertValue = this.buttonForm.controls['alertValue']?.value;
//           thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
//           alertMessage = this.buttonForm.controls['alertMessage']?.value;
//         }
//         //////////////////////////////
//         let object = {
//           "buttonName": buttonName,
//           "position": 0,
//           "order": buttonOrder,
//           "fieldSet": fieldSet,
//           "updatedBy": this.informationservice.getLogeduserId(),
//           "buttonId": buttonId,
//           "objectButtonId": objectButtonId + "~A~" + buttonAction + "~A~" + selectedCols + "~A~"+ URL + "~A~" + isMainPreview + "~A~" +condition + "~A~" + OtherCondition + "~A~" + alertValue + "~A~" + thirdCondition + "~A~" + alertMessage,
//         }
// console.log("object>>>>>>>>>>",object);
//         this.http.post<any>(GlobalConstants.updateFormButton + this.objectId, object, { headers: GlobalConstants.headers }).subscribe(
//           (res: any) => {
//             if (res.status == 'Fail') {
//               this.commonFunctions.alert("alert", res.description);
//             } else {
//               this.commonFunctions.alert("alert", res.description);
//             }
//           });
//       }
//     }
//   }




submitForm() {
  if (this.buttonForm.status !== 'INVALID') {
   if (this.actionType === 'saveNew') {
      let buttonName = this.buttonForm.controls['buttonName']?.value;
      let buttonOrder = this.buttonForm.controls['buttonOrder']?.value;
      let fieldSet = this.buttonForm.controls['fieldSet']?.value;
      let isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
      let buttonId = null;

      let objectButtonIds = [];

      for (let i = 0; i < this.buttonActions.length; i++) {
        let buttonAction = this.buttonForm.controls[`ButtonAction${i}`]?.value;

        let objectButtonId = '';
        let selectedCols = '';
        let URL = '';
        let condition = '';
        let OtherCondition = '';
        let alertValue = '';
        let thirdCondition = '';
        let alertMessage = '';
        let PATH = '';
        let jsonResponse='';
        let jsonRequest='';
        switch (buttonAction) {
          case 1:
            objectButtonId = this.buttonForm.controls['Menus']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            break;
          case 2:
            objectButtonId = this.buttonForm.controls['procedureName']?.value;
            selectedCols = this.buttonForm.controls['allColumns']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            break;
          case 3:
            selectedCols = this.buttonForm.controls['allColumns']?.value;
            URL = this.buttonForm.controls['url']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            jsonRequest=JSON.stringify(this.requestSavedParams);
            jsonResponse=JSON.stringify(this.responseSavedParams);
            break;
          case 5:
            selectedCols = this.buttonForm.controls['allColumns']?.value;
            URL = this.buttonForm.controls['url']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            break;
          case 4:
            condition = null;
            OtherCondition = null;
            alertValue = null;
            thirdCondition = null;
            alertMessage = null;
            break;
          case 8:
            PATH = this.buttonForm.controls['Path']?.value;
            if (PATH === "") {
              // handle empty path if needed
            }
            break;
          case 7:
            objectButtonId = this.buttonForm.controls['Menus']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            break;
        }

        let objectButtonIdString = `${objectButtonId}~A~${buttonAction}~A~${selectedCols}~A~${URL}~A~${isMainPreview}~A~${condition}~A~${OtherCondition}~A~${alertValue}~A~${thirdCondition}~A~${alertMessage}~A~${jsonRequest}~A~${jsonResponse}`;
        objectButtonIds.push(objectButtonIdString);
      }
      console.log("JAVA METHOD FILE>>>>>>>>>>>>>",objectButtonIds);
      let object = {
        "buttonName": buttonName,
        "position": 0,
        "order": buttonOrder,
        "fieldSet": fieldSet,
        "createdBy": this.informationservice.getLogeduserId(),
        "objectButtonId": `~N~${this.buttonActions.length}~N~|${objectButtonIds.join('|')}`
    };
      console.log("BUTTON DATA OBJECT>>>>>>>>>>>>>>>", object);

      this.http.post<any>(GlobalConstants.createFormButton + this.objectId, object, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          if (res.status === 'Fail') {
            this.commonFunctions.alert("alert", res.description);
          } else {
            this.commonFunctions.alert("alert", res.description);
          }
        });
    }

    if (this.actionType === 'update') {
      let buttonName = this.buttonForm.controls['buttonName']?.value;
      let buttonOrder = this.buttonForm.controls['buttonOrder']?.value;
      let fieldSet = this.buttonForm.controls['fieldSet']?.value;
      let isMainPreview = this.buttonForm.controls['isMainPreview']?.value;
      let buttonId = this.buttonForm.controls['buttonId']?.value ;
      let jsonResponse='';
      let jsonRequest='';
      let objectButtonIds = [];

      for (let i = 0; i < this.buttonActions.length; i++) {
        let buttonAction = this.buttonForm.controls[`ButtonAction${i}`]?.value;

        let objectButtonId = '';
        let selectedCols = '';
        let URL = '';
        let condition = '';
        let OtherCondition = '';
        let alertValue = '';
        let thirdCondition = '';
        let alertMessage = '';
        let PATH = '';

        switch (buttonAction) {
          case 1:
            objectButtonId = this.buttonForm.controls[`Menus`]?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            break;
          case 2:
            objectButtonId = this.buttonForm.controls['procedureName']?.value;
            selectedCols = this.buttonForm.controls['allColumns']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            objectButtonId = this.buttonForm.controls[`Menus`]?.value;

            break;
          case 3:
            objectButtonId = this.buttonForm.controls[`Menus`]?.value;
            selectedCols = this.buttonForm.controls['allColumns']?.value;
            URL = this.buttonForm.controls['url']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            jsonRequest=JSON.stringify(this.requestSavedParams);
            jsonResponse=JSON.stringify(this.responseSavedParams);
            break;
          case 5:
            selectedCols = this.buttonForm.controls['allColumns']?.value;
            URL = this.buttonForm.controls['url']?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            objectButtonId = this.buttonForm.controls[`Menus`]?.value;
            break;
          case 4:
            condition = null;
            OtherCondition = null;
            alertValue = null;
            thirdCondition = null;
            alertMessage = null;
            break;
          case 8:
            PATH = this.buttonForm.controls['Path']?.value;
            objectButtonId = this.buttonForm.controls[`Menus`]?.value;

            if (PATH === "") {
            }
            break;
          case 7:
            objectButtonId = this.buttonForm.controls[`Menus`]?.value;
            condition = this.buttonForm.controls['condition']?.value;
            OtherCondition = this.buttonForm.controls['OtherCondition']?.value;
            alertValue = this.buttonForm.controls['alertValue']?.value;
            thirdCondition = this.buttonForm.controls['thirdCondition']?.value;
            alertMessage = this.buttonForm.controls['alertMessage']?.value;
            break;
        }

        let objectButtonIdString = `${objectButtonId}~A~${buttonAction}~A~${selectedCols}~A~${URL}~A~${isMainPreview}~A~${condition}~A~${OtherCondition}~A~${alertValue}~A~${thirdCondition}~A~${alertMessage}~A~${jsonRequest}~A~${jsonResponse}`;
        objectButtonIds.push(objectButtonIdString);
      }

      let object = {
        "buttonName": buttonName,
        "position": 0,
        "order": buttonOrder,
        "fieldSet": fieldSet,
        "updatedBy": this.informationservice.getLogeduserId(),
        "buttonId": buttonId,
        "objectButtonId": `~N~${this.buttonActions.length}~N~|${objectButtonIds.join('|')}`
    };

      this.http.post<any>(GlobalConstants.updateFormButton + this.objectId, object, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          if (res.status === 'Fail') {
            this.commonFunctions.alert("alert", res.description);
          } else {
            this.commonFunctions.alert("alert", res.description);
          }
        });
    }
  }
}










   onActionChange() {
  //   let ButtonAction = this.buttonForm.controls['ButtonAction']?.value;
  //   let isMain = this.buttonForm.controls['isMainPreview']?.value;
  //   //hide the condition
  //   this.Condition = false;
  //   if (ButtonAction == 1) {
  //     this.FormOpeningSelected = true;
  //     this.ApiSelected = false;
  //     this.ApiSelected1 = false;
  //   }
  //   if (ButtonAction == 2) {
  //     this.ApiSelected = true;
  //     this.ApiSelected1 = true;
  //     this.FormOpeningSelected = false;
  //   }
  //   if(ButtonAction == 3){
  //     this.ApiSelected = false;
  //     this.ApiSelected1 = true;
  //     this.FormOpeningSelected = false;
  //   }
  //   if(ButtonAction == 4){
  //     this.ApiSelected = false;
  //     this.ApiSelected1 = false;
  //     this.FormOpeningSelected = false;
  //   }
  //   if(ButtonAction == 8){
  //     this.ApiSelected = false;
  //     this.ApiSelected1 = false;
  //     this.exportSelected=true;
  //     this.FormOpeningSelected = false;
  //   }
   }
   showCondition(){
  //   this.Condition = true;
  // }
  // onNbOfActionInput(): void {
  //   const nbOfAction = this.buttonForm.get('nbOfAction').value;
  //   this.buttonActions = Array.from({ length: nbOfAction }, (_, i) => i);

  //   // Add or remove controls based on the nbOfAction value
  //   for (let i = 0; i < nbOfAction; i++) {
  //     if (!this.buttonForm.get(`ButtonAction${i}`)) {
  //       this.buttonForm.addControl(`ButtonAction${i}`, new UntypedFormControl(''));
  //       this.buttonForm.addControl(`Menus${i}`, new UntypedFormControl(''));
  //     }
  //   }

  //   // Remove extra controls if the number of actions is reduced
  //   const formControls = Object.keys(this.buttonForm.controls);
  //   formControls.forEach(control => {
  //     if (control.startsWith('ButtonAction') && parseInt(control.replace('ButtonAction', ''), 10) >= nbOfAction) {
  //       this.buttonForm.removeControl(control);
  //     }
  //   });
  }

  onNbOfActionInput(): void {
    const nbOfAction = this.buttonForm.get('nbOfAction').value;
    this.buttonActions = Array.from({ length: nbOfAction }, (_, i) => i);
    this.Condition = false;
    this.ApiSelected = false;
    this.ApiSelected1 = false;

    // Add or remove controls based on the nbOfAction value
    for (let i = 0; i < nbOfAction; i++) {
      if (!this.buttonForm.get(`ButtonAction${i}`)) {
        const control = new UntypedFormControl('');
        this.buttonForm.addControl(`ButtonAction${i}`, control);
        this.buttonForm.addControl(`Menus${i}`, new UntypedFormControl(''));
        control.valueChanges.subscribe(value => this.onButtonActionChange(value, i));
      }
    }

    // Remove extra controls if the number of actions is reduced
    const formControls = Object.keys(this.buttonForm.controls);
    formControls.forEach(control => {
      const controlIndex = parseInt(control.replace(/\D/g, ''), 10);
      if (control.startsWith('ButtonAction') && controlIndex >= nbOfAction) {
        this.buttonForm.removeControl(control);
      }
    });
  }

  onButtonActionChange(value: any, index: number): void {
    this.resetConditions(index);
    if (value === 1) {
      this.FormOpeningSelected = true;
    } else if (value === 2) {
      this.ApiSelected = true;
      this.ApiSelected1 = true;
    }else if (value === 3) {
      this.ApiSelected1 = true;

    }else if (value === 5) {
      this.ApiSelected1 = true;
    }else if (value === 7) {
      this.FormOpeningSelected = true;
      this.Condition = true;

    }else if (value === 8) {
      this.exportSelected=true;
    }
    // Add more conditions as needed
  }

  resetConditions(index: number): void {
    this.FormOpeningSelected = false;
    this.Condition = false;
    this.ApiSelected = false;
    this.ApiSelected1 = false;
    this.exportSelected= false;
  }

  async showApiJsons(){
    this.showApiBuilderJson=true;

    const getApiJsonsApi = from(axios.get(GlobalConstants.getApiJsons + this.buttonForm.get('url').value));
    const getApiJsons = await lastValueFrom(getApiJsonsApi);
    this.buttonForm.controls['requestJson'].setValue(getApiJsons.data[0].requestJson);
    this.buttonForm.controls['responseJson'].setValue(getApiJsons.data[0].responseJson);
    
    const hashTagRegex = /#([^#]+)#/g;

    let matchesRequest = getApiJsons.data[0].requestJson.match(hashTagRegex);
    let matchesResponse = getApiJsons.data[0].responseJson.match(hashTagRegex);
    // let requestJsonParams=[];
    // let responseJsonParams=[];
    
    if (matchesRequest) {
      this.requestJsonParams = matchesRequest.map((match: string | any[]) => match.slice(1, -1)); 
      } 

    if (matchesResponse) {
      this.responseJsonParams = matchesResponse.map((match: string | any[]) => match.slice(1, -1)); 
      } 

      console.log("requestJsonParams>>>>>>>>>>>",this.requestJsonParams); 
      console.log("responseJsonParams>>>>>>>>>",this.responseJsonParams); 

  }
  
  openRelationRequestPopup(){
    let info={
    objectId:this.objectId,
    jsonData:this.requestJsonParams
    }


    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '700px';
    dialogConfig.height = '700px';
  
    const dialogRef = this.dialog.open(ButtonJsonRelationComponent, {
      data: info,
      width: '50%',
      height: '60%',
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if(result==undefined){

      }else{
        console.log("close json relation data>>>>>>>>>>>>>>",result);
        this.requestSavedParams=result;
      }

    });
  }
  
  openRelationResponsePopup(){
    let info={
    objectId:this.objectId,
    jsonData:this.responseJsonParams
    }


    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '700px';
    dialogConfig.height = '700px';
  
    const dialogRef = this.dialog.open(ButtonJsonRelationComponent, {
      data: info,
      width: '50%',
      height: '60%',
    });
  
    dialogRef.afterClosed().subscribe(result => {
  if(result==undefined){

      }else{
        console.log("close json relation data>>>>>>>>>>>>>>",result);
        this.responseSavedParams=result;
      }    
    });
  }
}
