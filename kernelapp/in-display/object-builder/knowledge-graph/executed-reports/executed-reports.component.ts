
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { GridBuilderFormComponent } from '../../grid-builder-form/grid-builder-form.component';
import { DataService } from 'src/app/Kernel/services/data.service';
import { ChartBuilderFormComponent } from '../../chart-builder-form/chart-builder-form.component';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { GridBuilderPreviewComponent } from '../../grid-builder-preview/grid-builder-preview.component';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { Router } from '@angular/router';
import { KwgCytoscapeComponent } from './kwg-cytoscape/kwg-cytoscape.component';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
// import { MasterLinkFormComponent } from './master-link-form/master-link-form.component';

@Component({
  selector: 'app-executed-reports',
  templateUrl: './executed-reports.component.html',
  styleUrl: './executed-reports.component.css'
})
export class ExecutedReportsComponent {
  
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public agGridSelectedNodes: any = '';
  public getExecutedReports = GlobalConstants.getExecutedReports;
  public action: any;
  frameworkComponents: any;

  public isQueryexecute: boolean;
  constructor(
    private router: Router,
    private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService

  ) {

    this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
  }

  ngOnInit(): void {

    this.agColumnsJson = [
      {
        headerName: 'Executed Report ID',
        field: 'executedReportId',
        width: '25%',
      },
      {
        headerName: 'Executed Report Name',
        field: 'executedReportName',
        width: '30%',
      },
      {
        headerName: 'Master Name',
        field: 'executedreportMaster',
        width: '20%',
      },
      {
        headerName: 'Owner',
        field: 'createdBy',
        width: '25%',
      },
      {
        headerName: 'Creation Date',
        field: 'creationDate',
        width: '30%',
      },
      {
        headerName: 'Display',
        field: '',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClick.bind(this),
          label: 'Click 1'
        }
      }, 

    ];

    this.agColumns.push(this.agColumnsJson);
  }
  



  async onRunButtonClick(e: any) {

    // const getExecutedReportDataApi = from(axios.get(GlobalConstants.getExecutedReportData+this.informationservice.getAgGidSelectedNode()));
    // const getExecutedReportData = await lastValueFrom(getExecutedReportDataApi);

    //console.log("EXECUTED REPORT DATA>>>>>>>>",getExecutedReportData.data);

    // const displayGraphApi = from(axios.get(GlobalConstants.displayGraph+this.informationservice.getAgGidSelectedNode()));
    // const displayGraph = await lastValueFrom(displayGraphApi);
    // console.log("DISPLAY GRAPH DATA>>>>>>>>",displayGraph.data);

    let info = {};

    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {

    }else{
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '700px';
      dialogConfig.height = '700px';
  
      const dialogRef = this.dialog.open(KwgCytoscapeComponent, {
         data: info,
        width: '90%',
        height: '90%',
      });
    }
  }




  onDeleteClick() {
    
  //   this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
  //   let selectedNodes = this.agGridSelectedNodes;
  //   if (selectedNodes.length == 0) {
  //   } else {
  //     if (selectedNodes.indexOf(",") != -1) {
  //       selectedNodes = selectedNodes.split(',');
  //       for (let i = 0; i < selectedNodes.length; i++) {
  //         this.http.delete<any>(GlobalConstants.deleteGridApi + selectedNodes[i],
  //           { headers: GlobalConstants.headers }).subscribe({
  //             next: (res) => {
  //               this.commonFunctions.reloadPage('/dsp/gridBuilder');
  //             },
  //             error: (error) => {
  //               console.log(error);
  //             }
  //           });
  //       }
  //     } else {
  //       this.http.delete<any>(GlobalConstants.deleteGridApi + selectedNodes,
  //         { headers: GlobalConstants.headers }).subscribe({
  //           next: (res) => {
  //             this.commonFunctions.reloadPage('/dsp/gridBuilder');
  //           },
  //           error: (error) => {
  //             console.log(error);
  //           }
  //         });
  //     }
  //   }
  }

}