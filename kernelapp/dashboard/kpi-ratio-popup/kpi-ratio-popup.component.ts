import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { HttpClient } from '@angular/common/http';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { KpiBuilderPreviewComponent } from 'src/app/Kernel/kernelapp/in-display/object-builder/kpi-builder-preview/kpi-builder-preview.component';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { ObjectSizeManagerPopupComponent } from '../object-size-manager-popup/object-size-manager-popup.component';
@Component({
  selector: 'app-kpi-ratio-popup',
  templateUrl: './kpi-ratio-popup.component.html',
  styleUrls: ['./kpi-ratio-popup.component.css']
})
export class KpiRatioPopupComponent implements OnInit {
  public getDashboardKpiData = GlobalConstants.getDashboardKpiData;
  public agColumnsJson: any;
  public agColumns: AgColumns[] = [];
  public agGridSelectedNodes: any = '';
  frameworkComponents: any;
  constructor(private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    public informationService: InformationService,) {

    this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
  }

  ngOnInit(): void {
    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        headerCheckboxSelection: true
      }, {
        headerName: 'Id',
        field: 'id',
      },
      {
        headerName: 'Name',
        field: 'name',
      }, {
        headerName: 'Preview',
        field: '',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClick.bind(this),
          label: 'Click 1'
        }
      },
      {
        headerName: 'Type',
        field: 'type',
      }

    ];

    this.agColumns.push(this.agColumnsJson);
  }

  onRunButtonClick(e: any) {
    let info = {};
    this.agGridSelectedNodes = this.informationService.getAgGidSelectedNode();
    
    let selectedNodes = this.agGridSelectedNodes;

    // if (localStorage.getItem("agGidSelectedNode").includes(",") || localStorage.getItem("agGidSelectedNode") == "") {

    // }
    const agGidSelectedNode = localStorage.getItem("agGidSelectedNode");

    if (agGidSelectedNode && (agGidSelectedNode.includes(",") || agGidSelectedNode === ""))
    {}
    else
    {
      this.http.post<any>(GlobalConstants.decodeKpiQuery + selectedNodes, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          info = {
            data: res,
            kpiId: this.informationService.getAgGidSelectedNode(),
          };
        });
        
      setTimeout(() => {

        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '700px';
        dialogConfig.height = '700px';

        const dialogRef = this.dialog.open(KpiBuilderPreviewComponent, {
          data: info,
          width: '30%',
          height: '30%',
        });


      }, 1000);


    }

  }
  Insert() {
    
    // if(this.informationService.getAgGidSelectedNode() == '' || this.informationService.getAgGidSelectedNode() == null)
    //   {
    //     alert("Plese select a kpi first")
    //   }
    //   else
    //   {
    //     const dialogRef = this.dialog.open(ObjectSizeManagerPopupComponent, {
    //     data: "kpi",
    //     width: '50%',
    //     height: '40%',
    //   });
  
    //   dialogRef.afterClosed().subscribe(result => {
  
    //     let allData = {
    //       kpiId: this.informationService.getAgGidSelectedNode(),
    //       templateId: this.informationService.getSelectedTabId(),
    //     }
      
    //     this.http.post<any>(GlobalConstants.addDashboardKpi, allData,
    //   { headers: GlobalConstants.headers }).subscribe({
    //     next: (res) => {
    //       console.log(res);
  
    //     },
    //     error: (error) => {
    //       console.log(error);
    //     }
    //   });
    // this.commonFunctions.reloadPage('/dashboard');
    // this.commonFunctions.navigateToPage('/dashboard');
  
    //   });
    //   }









    // this.agGridSelectedNodes = this.informationService.getAgGidSelectedNode();
    // let selectedNodes = this.agGridSelectedNodes;

    
    // let allData = {
    //   kpiId: selectedNodes,
    //   templateId: this.informationService.getSelectedTabId(),
    // }

    // this.http.post<any>(GlobalConstants.addDashboardKpi, allData,
    //   { headers: GlobalConstants.headers }).subscribe({
    //     next: (res) => {
    //     },
    //     error: (error) => {
    //     }
    //   });

    // this.commonFunctions.reloadPage('/dashboard');
    // this.commonFunctions.navigateToPage('/dashboard');

  }
}