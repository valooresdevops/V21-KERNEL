import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { HttpClient } from '@angular/common/http';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-logsbyuser',
  templateUrl: './logsbyuser.component.html',
  styleUrls: ['./logsbyuser.component.css']
})
export class LogsbyuserComponent implements OnInit {

  public userLogs: any;
  public application: any;
  
  public menuPath: any = this.informationservice.getMenuPath();
  public menuPaths: any ;
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public userLogsGrid: any;
  public id:string = '';
  public isShown: boolean = false ;
   gridData : any[] = [];
  gridValue: any[] = [];

  constructor(private http: HttpClient,
    public datepipe: DatePipe,
    public informationservice: InformationService) {}

  logsUserForm = new UntypedFormGroup({
    userLogs: new UntypedFormControl(''),
    application : new UntypedFormControl(''),
    loginDate : new UntypedFormControl('')
   });
  ngOnInit(): void {
    $("#formRow").addClass("vh-100");

    this.userLogs=GlobalConstants.fetchUserCombo;
    this.application = GlobalConstants.fetchUSMRolesAppComboApi;

    this.agColumnsJson = [
      {
        headerName: 'Application',
        field: 'applicationname',
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
        headerName: 'Operation Description',
        field: 'operationhint',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Operation Date',
        field: 'operationdate',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Login Date',
        field: 'logindate',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Logout Date',
        field: 'logoutdate',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'IP',
        field: 'logip',
        filter: 'agTextColumnFilter',
        sortable: true
      }
    ];
    // this.agColumnsJson = [
    //   {
    //     headerName: 'Log Id',
    //     field: 'logId',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Application Code ',
    //     field: 'applicationCode',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Application Name',
    //     field: 'applicationName',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Operation Type Id',
    //     field: 'operationTypeId',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Operation Type',
    //     field: 'operationType',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Operation Hint',
    //     field: 'operationHint',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Operation Date',
    //     field: 'operationDate',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Emp Id',
    //     field: 'empId',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Log Ip',
    //     field: 'logIp',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Login Date',
    //     field: 'loginDate',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Logout Date',
    //     field: 'logoutDate',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Is Action',
    //     field: 'isAction',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   },
    //   {
    //     headerName: 'Creation Date',
    //     field: 'creationDate',
    //     filter: 'agTextColumnFilter',
    //     sortable: true
    //   }
    // ];
    this.agColumns = [];
    this.agColumns.push(this.agColumnsJson);
  }

  //////////Sigma
  async searchForm(){
    const userLogs = this.logsUserForm.get('userLogs').value ;
    // console.log("typeoffff0000>>>>>>",typeof this.logsUserForm.get('userLogs').value);
    let application = this.logsUserForm.get('application').value == "" ? "-1" : this.logsUserForm.get('application').value;
    // console.log("typeoffff1111>>>>>>",typeof this.logsUserForm.get('application').value);
    const loginDate = this.datepipe.transform(this.logsUserForm.get('loginDate').value, 'MM-dd-yyyy');
    // console.log("loginDate >>> ", typeof loginDate);
    const getLogsByUserApi = from(axios.post(GlobalConstants.getLogsByUserApi+ userLogs+"/"+application + "/"+loginDate,{}));
    const getLogsByUser = await lastValueFrom(getLogsByUserApi);
    this.userLogsGrid = getLogsByUser.data;
   console.log("typeoffffffff", typeof this.userLogsGrid);
   console.log("userLogsGrid>>>>>>>>>>>>  ", this.userLogsGrid);
    if(this.logsUserForm.status != "INVALID")
    {
    $("#formRow").removeClass("vh-100");
    this.isShown = true;

    // console.log("userLogs >>> ", this.logsUserForm.get('userLogs')?.value);
    // console.log("application >>> ", this.logsUserForm.get('application').value);

  }
}
}
