import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import axios from 'axios';
import { link } from 'fs';
import { from, lastValueFrom } from 'rxjs';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { FiltersDataComponent } from './filters-data/filters-data.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { InformationService } from 'src/app/Kernel/services/information.service';
//import {AGGridComponent} from 'src/app/Kernel/components/v-grid/v-grid.components';
@Component({
  selector: 'app-filters',
  templateUrl: './filters.html',
  styleUrls: ['./filters.css']
})
export class FiltersComponent implements OnInit {

  public getUSMApplicationComboApi: any;
  public getMenuApi: any;
  public getApplicationEvent: any;
  public agColumnsJson: any;
  agColumns: AgColumns[] = [];
  agLogsByUsers: AgColumns[] = [];
  isShown: boolean=false;
  applicationForm = new UntypedFormGroup({
    application:new UntypedFormControl({value:'',disabled: false}),
    loginDate: new UntypedFormControl({value: '', disabled: false}, ),
    logoutDate: new UntypedFormControl({value: '', disabled: false},),
  });


  constructor(private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    public datepipe: DatePipe,
    public dialog: MatDialog,
    private informationService: InformationService) { }


  ngOnInit(): void {

    this.getUSMApplicationComboApi = GlobalConstants.getUSMApplicationComboApi;
    this.agColumnsJson = [
      { headerName: 'Emp Name',field: 'empName', filter: 'agTextColumnFilter' },
      { headerName: 'Login Date',field: 'loginDate', filter: 'agTextColumnFilter', sortable: true },
      { headerName: 'Logout Date',field: 'logoutDate', filter: 'agTextColumnFilter' },
      { headerName: 'Logout Incidence',field: 'logoutIncidence', filter: 'agTextColumnFilter' },
      { headerName: 'Connected IP Address',field: 'logIp', filter: 'agTextColumnFilter' },
      {
        headerName: 'Actions',
        field: 'actionsCount',
        filter: 'agTextColumnFilter',
        // isLink: true,
        cellRenderer: (params: any) => {
          const renderer = new MatDialogCellRenderer(this.dialog, this.informationService);
          renderer.init(params);
          return renderer.getGui();
        },
        keyCreator: (params: any) => {
          return params.value;
        },
        cellEditorParams: {
          cellRenderer: MatDialogCellRenderer,
          isSearchable: true,
        },
        // linkValue: 'R',
        // linkType: 'linkPopUp',
      },
      { headerName: 'Exceptions',field: 'exceptionsCount', filter: 'agTextColumnFilter' },
    ];
    this.agColumns.push(this.agColumnsJson);

  }
  ngOnChanges(): void {
    this.isShown = false;

    this.gridShow();}

  async gridShow(){

    if (this.applicationForm.status != "INVALID") {
      const loginDate = this.applicationForm.get('loginDate').value == "" ? "-1" : this.datepipe.transform(this.applicationForm.get('loginDate').value, 'dd-MM-YYYY');
      const logoutDate = this.applicationForm.get('logoutDate').value == "" ? "-1" : this.datepipe.transform(this.applicationForm.get('logoutDate').value, 'dd-MM-YYYY');
      let application = this.applicationForm.get('application').value == "" ? "-1" : this.applicationForm.get('application').value;

       if(application.toString().length == 1){
        application = '00'+application;
              }
       if(application.toString().length == 2){
       application = '0'+application;
                      }
                      console.log('logoutdate',logoutDate);
                      if (loginDate != '' && logoutDate != '' && logoutDate != "-1" && loginDate != "-1") {
                        const loginDateTimeStamp = new Date(loginDate).getTime();
                        const logoutDateTimeStamp = new Date(logoutDate).getTime();

                        if (loginDateTimeStamp > logoutDateTimeStamp) {
                          this.commonFunctions.alert("alert", 'Login Date cannot be later than Logout Date');
                          return;
                        }
                      }
console.log("logoutdate",logoutDate);
const getLogsByEventApi = from(axios.post(GlobalConstants.getUSMApplicationEvent + application + "/"  + loginDate + "/" + logoutDate,{}));
      const getLogsByEvent = await lastValueFrom(getLogsByEventApi);
      this.getApplicationEvent = getLogsByEvent.data;
    //   this.getApplicationEvent = GlobalConstants.getUSMApplicationEvent + application + "/"  + loginDate + "/" + logoutDate;
     console.log (this.getApplicationEvent);

     this.isShown = true;
    }
  }

}
///////Sigma
class MatDialogCellRenderer {
  eGui: HTMLElement | null = null;

  constructor(
    public dialog: MatDialog,
    public informationService: InformationService
  ) {

  }

  init(params: any) {
    console.log("MatDialogCellRenderer params:", params);

    // Create the anchor element
    const anchor = document.createElement('a');
    anchor.textContent = params.value;
    anchor.className = 'ag-row-hyperlink';
    anchor.style.cursor = 'pointer';
    anchor.style.color = '#007bff'; // Bootstrap-like hyperlink color
    anchor.style.textDecoration = 'underline';

    // Attach the click event to the anchor
    anchor.addEventListener('click', () => this.openLinkPop(params.data.logId));

    // Create the container div and append the anchor
    this.eGui = document.createElement('div');
    this.eGui.style.display = 'flex';
    this.eGui.style.alignItems = 'center';
    this.eGui.style.justifyContent = 'center';
    this.eGui.appendChild(anchor);
  }

  openLinkPop(logId: String) {
    console.log('Popup link clicked', typeof logId);
    this.informationService.setSelectedGridNode(logId);

    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '65%';
    dialogConfig.height = '50%';
    dialogConfig.data = { logId: logId };

    this.dialog.open(FiltersDataComponent, dialogConfig);
  }

  getGui() {
    return this.eGui!;
  }

  refresh(params: any) {
    return false;
  }
}


