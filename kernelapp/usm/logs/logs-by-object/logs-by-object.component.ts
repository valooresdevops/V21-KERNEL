import { DatePipe } from '@angular/common';
import { Component, OnInit , ChangeDetectorRef } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
//working exple on swager app:5 app,menu_code=00529 or Skip row selection on context menu, login date=10-10-2017, logoutdate=-1


@Component({
  selector: 'app-logs-by-object',
  templateUrl: './logs-by-object.component.html',
  styleUrls: ['./logs-by-object.component.css']
})
export class LogsByObjectComponent implements OnInit {

  public getApplicationName: any;
  public getMenuName: any;
  public menuNameBody: any;
  public menuIsRequired = false;
  public menuIsDisabled = false;
  public isShown: boolean = false;
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public objectLogsGrid: any;



  ObjectForm = new UntypedFormGroup({
    application:new UntypedFormControl({value:'',disabled: false}),
    loginDate: new UntypedFormControl({value: '', disabled: false}, ),
    logoutDate: new UntypedFormControl({value: '', disabled: false},),
    menuName:new UntypedFormControl({value: '', disabled: false},)
  });
  constructor(public datepipe: DatePipe,
              private commonFunctions: CommonFunctions,
              private cdr : ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    $("#dropdownStyle").addClass('vh-100');
    this.getApplicationName = GlobalConstants.fetchApplicationCombo;

    this.agColumnsJson = [
      {
        headerName: 'Employee Name',
        field: 'empname',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Menu',
        field: 'menu',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Operation Type',
        field: 'operationtype',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Hint',
        field: 'operationhint',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Operation Date',
        field: 'operationdate',
        filter: 'agTextColumnFilter',
        sortable: true
      }
    ];
    this.agColumns.push(this.agColumnsJson);
  }
  ngOnChanges(): void {this.submitForm();}


  ////////Sigma
  async submitForm() {
    if (this.ObjectForm.status != "INVALID") {
      $("#dropdownStyle").removeClass('vh-100');


      // const loginDate = this.ObjectForm.get('loginDate').value == "" ? "-1" : this.datepipe.transform(this.ObjectForm.get('loginDate').value, 'YYYY-MM-dd');

      // const logoutDate = this.ObjectForm.get('logoutDate').value == "" ? "-1" : this.datepipe.transform(this.ObjectForm.get('logoutDate').value, 'YYYY-MM-dd');

      const loginDate = this.datepipe.transform(this.ObjectForm.get('loginDate').value, 'MM-dd-yyyy');
      const logoutDate = this.datepipe.transform(this.ObjectForm.get('logoutDate').value, 'MM-dd-yyyy');


      let application = this.ObjectForm.get('application').value == "" ? "-1" : this.ObjectForm.get('application').value;
      application = application.toString();
if (application.length === 1) {
  application ='00' + application;
      } else {
        application ='0' + application;
      }
      //parseInt to eliminate leading zeros
      let menuName = (this.ObjectForm.get('menuName').value == "" ? "-1" : this.ObjectForm.get('menuName').value);
      console.log("menu name >>>>", menuName);
      console.log("menu name type>>>>", typeof menuName);
      // menuName = parseInt(menuName);

      //console.log('logoutdate',logoutDate);

      if (loginDate != null && logoutDate != null && logoutDate != "-1" && loginDate != "-1") {
        const loginDateTimeStamp = new Date(loginDate).getTime();
        const logoutDateTimeStamp = new Date(logoutDate).getTime();

        if (loginDateTimeStamp > logoutDateTimeStamp) {
          this.commonFunctions.alert("alert", 'Login Date cannot be later than Logout Date');
          return;
        }
      }


      //  console.log('logindate:',loginDate);
      //  console.log('logoutdate:',logoutDate);
      //  console.log('application:',application);
      //  console.log('menuName:',menuName);
      const getLogsByObjApi = from(axios.get(GlobalConstants.getObjectLogsApi + application + "/" + menuName + "/" + loginDate + "/" + logoutDate,{}));
      const getLogsByObj = await lastValueFrom(getLogsByObjApi);
      this.objectLogsGrid = getLogsByObj.data;
      console.log("typeoffffffff", typeof this.objectLogsGrid);
   console.log("objectLogsGrid>>>>>>>>>>>>  ", this.objectLogsGrid);
  // this.objectLogsGrid = GlobalConstants.getObjectLogsApi + application + "/" + menuName + "/" + loginDate + "/" + logoutDate;

//console.log ("objectLogsGrid",this.objectLogsGrid);
this.isShown = true;
  }
}

onChangeApplicationName(value: any) {
  value = value.toString();
  if (value !== '') {
      this.menuIsDisabled = false;
      this.menuIsRequired = true;
      const defaultRoleJson = { 'id': value };

      // Check the length of the value and format accordingly
      console.log("value lengthhh>>>",value.length);
      if (value.length === 1) {
          value ='00' + value;
      } else {
          value ='0' + value;
      }

      this.getMenuName = GlobalConstants.getMenuNameByPCode + value;
      this.cdr.detectChanges();
      //alert("getMenuName"+this.getMenuName);
      this.menuNameBody = defaultRoleJson;
      console.log("this.menuNameBody", this.menuNameBody);
      console.log("app valuee>>>>>",value);
  } else {
      this.getMenuName = "";
      this.menuIsRequired = false;
      this.menuIsDisabled = true;
  }

  // Set menuIsDisabled flag based on menuIsRequired flag
  this.menuIsDisabled = !this.menuIsRequired;
}

}


