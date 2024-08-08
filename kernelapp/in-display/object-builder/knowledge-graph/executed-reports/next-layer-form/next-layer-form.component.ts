import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { DataService } from 'src/app/Kernel/services/data.service';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { Router } from '@angular/router';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';


@Component({
  selector: 'app-next-layer-form',
  templateUrl: './next-layer-form.component.html',
  styleUrl: './next-layer-form.component.css'
})
export class NextLayerFormComponent {
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
    public dialogRef: MatDialogRef<NextLayerFormComponent>,
    private router: Router,
    private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService

  ) {}


  

  ngOnInit(): void {
    this.informationservice.setAgGidSelectedNode('');
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
        hide:true
      },
      {
        headerName: 'Owner',
        field: 'createdBy',
        width: '20%',
        hide:true
      },
    ];

    this.agColumns.push(this.agColumnsJson);
  }



  async onRunButtonClick(e: any) {
            
  }
  closeDialog(): void {
    this.dialogRef.close();
  }
}
