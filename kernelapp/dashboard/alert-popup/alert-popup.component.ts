import { Component, OnInit } from '@angular/core';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { HttpClient } from '@angular/common/http';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { MatDialog } from '@angular/material/dialog';
import { DataService } from 'src/app/Kernel/services/data.service';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { AlertPreviewComponent } from '../../in-display/object-builder/alert-builder/alert-preview/alert-preview.component';
import { ObjectSizeManagerPopupComponent } from '../object-size-manager-popup/object-size-manager-popup.component';
import { from, lastValueFrom } from 'rxjs';
import axios from 'axios';

@Component({
  selector: 'app-alert-popup',
  templateUrl: './alert-popup.component.html',
  styleUrl: './alert-popup.component.css'
})
export class AlertPopupComponent implements OnInit{
  public getAlertData = GlobalConstants.getAlertsDataApi;
  public reloadGrid: boolean = true;
  public action: any;
  public agGridSelectedNodes: any = '';
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  frameworkComponents: any;
  constructor(private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService) {

    this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
  }
  ngOnInit(): void {
    this.reloadGrid=this.informationservice.getreloadGrid();
console.log("ALERT POPOUP INIT>>>>>>>>>>>>>>>>>>>>>")
this.agColumnsJson = [
  {
    headerName: '',
    field: '',
    checkboxSelection: true,
    width: '25px',
    headerCheckboxSelection: true
  }, {
    headerName: 'Alert Id',
    field: 'id',
    width: '25px',
  },
  {
    headerName: 'Title',
    field: 'title',
    width: '25px',
  },
  {
    headerName: 'Preview',
    field: '',
    cellRenderer: ButtonRendererComponent,
    cellRendererParams: {
      onClick: this.onRunButtonClick.bind(this),
      label: 'Click 1'
    }
  },
  {
    headerName: 'Created by',
    field: 'username',
  },
  {
    headerName: 'Created date',
    field: 'creationDate',
  }
];

this.agColumns.push(this.agColumnsJson);
  }
  onRunButtonClick(params: any) {
    const rowID=params.rowData.id;
    const rowTitle = params.rowData.title; 
    const rowColor=params.rowData.color;
    // Open the dialog when the button is clicked
    this.dialog.open(AlertPreviewComponent, {
      data: {
        alertId:rowID,
        title:rowTitle,
        color:rowColor,
        rowData: params.rowData // Pass the selected row data to the dialog
      },
      width: '35%', // Adjust dialog size as needed
      height: '35%'
    });
    console.log("TITLE>>>>>>",rowTitle);
    console.log("TITLE>>>>>>",rowColor);
  }
  InsertAlert() {
    // Check if any alert is selected in the grid
    if (this.informationservice.getAgGidSelectedNode() === '' || this.informationservice.getAgGidSelectedNode() === null) {
      alert("Please select an alert first");
    } else {
      // Retrieve selected nodes (alerts) from the grid
      this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
      let selectedNodes = this.agGridSelectedNodes;
  
      let alertId = selectedNodes;
      let templateId = this.informationservice.getSelectedTabId();
  
      // Open dialog to manage object size
      const dialogRef = this.dialog.open(ObjectSizeManagerPopupComponent, {
        data: "alert", // Pass 'alert' to indicate this is for alert management
        width: '40%',
        height: '33%',
      });
  
      dialogRef.afterClosed().subscribe(result => {
        // Prepare data to send to the backend
        let allData = {
          alertId: alertId,  // Use 'alertId' instead of 'chartId'
          templateId: templateId
        };
  
        // Send HTTP POST request to add the alert to the dashboard
        this.http.post<any>(GlobalConstants.addDashboardAlert, allData, { headers: GlobalConstants.headers }).subscribe({
          next: (res) => {
            console.log("Alert added successfully", res);
          },
          error: (error) => {
            console.log("Error adding alert:", error);
          }
        });
  
        // Refresh the page and navigate back to the dashboard
        this.commonFunctions.reloadPage('/dashboard');
        this.commonFunctions.navigateToPage('/dashboard');
      });
    }
  }
  Insert() {

    console.log("insert entereddddd>>>>>>>>>>>>>>>>>>>>>>>");
    // Check if any alert is selected in the grid
    if (this.informationservice.getAgGidSelectedNode() === '' || this.informationservice.getAgGidSelectedNode() === null) {
      alert("Please select an alert first");
    } else {
      console.log("ELSE entereddddd>>>>>>>>>>>>>>>>>>>>>>>");
      // Retrieve selected nodes (alerts) from the grid
      this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
      let selectedNodes = this.agGridSelectedNodes;
  
      let alertId = selectedNodes;
      let templateId = this.informationservice.getSelectedTabId();
  
      console.log("DIALOG REF OPEN>>>>>>>>");
      // Open dialog to manage object size
      const dialogRef = this.dialog.open(ObjectSizeManagerPopupComponent, {
        data: "alert", // Pass 'alert' to indicate this is for alert management
        width: '40%',
        height: '33%',
      });
  
      dialogRef.afterClosed().subscribe(async result => {
        // Prepare data to send to the backend
        let allData = {
          alertId: alertId,  // Use 'alertId' instead of 'chartId'
          templateId: templateId
        };
        console.log("ALL DATA >>>>>>>>",allData);
  
        // Send HTTP POST request to add the alert to the dashboard
        const apiEndpoint = GlobalConstants.addDashboardAlert;
      
      // Convert axios POST request to Observable using `from`
      const addAlertApi = from(axios.post(apiEndpoint, allData));
      
      try {
        // Await the completion of the observable
        const addAlertData = await lastValueFrom(addAlertApi);
        console.log("Alert added successfully", addAlertData.data);

        // Refresh the page and navigate back to the dashboard
        this.commonFunctions.reloadPage('/dashboard');
        this.commonFunctions.navigateToPage('/dashboard');

      } catch (error) {
        console.error("Error adding alert:", error);
      }
  
        // Refresh the page and navigate back to the dashboard
        // this.commonFunctions.reloadPage('/dashboard');
        // this.commonFunctions.navigateToPage('/dashboard');
      });
    }
  }
    

}
