
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
import { MasterLinkFormComponent } from './master-link-form/master-link-form.component';
import { CreateExecutedReportComponent } from './create-executed-report/create-executed-report.component';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';


@Component({
  selector: 'app-master-link-analysis',
  templateUrl: './master-link-analysis.component.html',
  styleUrl: './master-link-analysis.component.css'
})
export class MasterLinkAnalysisComponent implements OnInit {
  public execHeads:any;
  public parameters: any;
  public query:any;
  public queryId:any;
  public queryName:any;
  public showInput :boolean=false;

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public agGridSelectedNodes: any = '';
  public getGridData = GlobalConstants.getGridDataApi;
  public action: any;
  frameworkComponents: any;
  public getQbeQueryApi  = GlobalConstants.fetchQbeMappingApi;
  public getMasterLinks  = GlobalConstants.getMasterLinks;
  executedReportForm = new UntypedFormGroup({});

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
        headerName: 'Master Link ID',
        field: 'masterId',
        width: '25%',
      },
      {
        headerName: 'Master Link Name',
        field: 'masterName',
        width: '30%',
      },
      {
        headerName: 'Query Id',
        field: 'masterQueryId',
        width: '20%',
      },
      {
        headerName: 'Owner',
        field: 'createdBy',
        width: '20%',
      },
      {
        headerName: 'Creation Date',
        field: 'creationDate',
        width: '25%',
      },

      {
        headerName: 'Execute',
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
  onAddClick() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '700px';
    dialogConfig.height = '700px';

    const dialogRef = this.dialog.open(MasterLinkFormComponent, {
      // data: info,
      width: '70%',
      height: '70%',
    });
  }


  async onRunButtonClick(e: any) {
            let info = {};
    
            this.executedReportForm= new UntypedFormGroup({});
            this.executedReportForm.updateValueAndValidity();

            this.execHeads=[];
            this.agColumnsJson=[];
            this.agColumns=[];
    console.log("JSON MASTERLINK>>>>>>>>>",JSON.parse(this.informationservice.getAgGidSelectedNode())[0].masterQueryId);
          const getQueryParamsApi=from(axios.get(GlobalConstants.getQueryParams+JSON.parse(JSON.parse(this.informationservice.getAgGidSelectedNode())[0].masterQueryId)));
          const getQueryParams=await lastValueFrom(getQueryParamsApi);
          info=getQueryParams.data[0];
          this.query=getQueryParams.data[0].query;
          this.parameters=JSON.parse(getQueryParams.data[0].queryParams);
          this.queryId=getQueryParams.data[0].queryId;
          this.queryName=getQueryParams.data[0].queryName;
          this.execHeads=JSON.parse(getQueryParams.data[0].execHeads);
          for(let i=0;i<this.parameters.length;i++){
          this.executedReportForm.addControl(this.parameters[i].paramName, new UntypedFormControl(''));
          }
        console.log("PARAMETERS>>>>>>>>",this.parameters);
          setTimeout(() => {
            this.showInput=true;
          }, 500);



      setTimeout(() => {
        let allData={
          info:info,
          selectedData:JSON.parse(this.informationservice.getAgGidSelectedNode())
        }
        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '700px';
        dialogConfig.height = '700px';

        const dialogRef = this.dialog.open(CreateExecutedReportComponent, {
          data: allData,
          width: '50%',
          height: '60%',
        });
      }, 1000);


  //  }
  }


  onUpdateClick() {

    this.action = 'update';
    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {

    } else {
      this.dataservice.setactionType(this.action);
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '700px';
      dialogConfig.height = '700px';

      const dialogRef = this.dialog.open(MasterLinkFormComponent, {
        // data: info,
        width: '70%',
        height: '70%',
      });
    }
  }





  onDeleteClick() {
    
    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (selectedNodes.length == 0) {
    } else {
      if (selectedNodes.indexOf(",") != -1) {
        selectedNodes = selectedNodes.split(',');
        for (let i = 0; i < selectedNodes.length; i++) {
          this.http.delete<any>(GlobalConstants.deleteGridApi + selectedNodes[i],
            { headers: GlobalConstants.headers }).subscribe({
              next: (res) => {
                this.commonFunctions.reloadPage('/dsp/knowledgeGraph');
              },
              error: (error) => {
                console.log(error);
              }
            });
        }
      } else {
        this.http.delete<any>(GlobalConstants.deleteGridApi + selectedNodes,
          { headers: GlobalConstants.headers }).subscribe({
            next: (res) => {
              this.commonFunctions.reloadPage('/dsp/knowledgeGraph');
            },
            error: (error) => {
              console.log(error);
            }
          });
      }
    }
  }

}