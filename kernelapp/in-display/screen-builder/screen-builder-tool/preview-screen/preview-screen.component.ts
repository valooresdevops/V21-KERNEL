import { HttpClient } from '@angular/common/http';
import { Component, Inject, Input, OnInit, Optional } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { ChartFromKpiBuilderComponent } from 'src/app/Kernel/kernelapp/in-display/object-builder/chart-from-kpi-builder/chart-from-kpi-builder.component';
import { GridBuilderPreviewComponent } from 'src/app/Kernel/kernelapp/in-display/object-builder/grid-builder-preview/grid-builder-preview.component';
import Highcharts from 'highcharts';
import Highcharts3D from 'highcharts/highcharts-3d';
Highcharts3D(Highcharts);

import HighchartsMore from 'highcharts/highcharts-more';
import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);
@Component({
  selector: 'app-preview-screen',
  templateUrl: './preview-screen.component.html',
  styleUrls: ['./preview-screen.component.css']
})
export class PreviewScreenComponent implements OnInit {
  Highcharts: typeof Highcharts = Highcharts;

  public itemsData: any[] = [];
  public chartData: any[] = [];

  public menuVariable: any;
  public gridData: any;

  constructor(@Optional() @Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog,
    private sanitizer: DomSanitizer,
    @Optional() public dialogRef: MatDialogRef<PreviewScreenComponent>,
    private _Activatedroute: ActivatedRoute,
    private http: HttpClient) { }

    async ngOnInit() {
      this.itemsData = [];
  
      if (this.data == null || this.data == "undefined") {
        this._Activatedroute.paramMap.subscribe(async (params:any) => {
          this.menuVariable = params.get('menuVariable');
          console.log("this.menuVariable-----",this.menuVariable)
          const itemsDataUrl = from(axios.get(GlobalConstants.getScreenPreviewData + this.menuVariable));
          const itemsDataa = await lastValueFrom(itemsDataUrl);
          console.log("itemsDataa===",itemsDataa);
          let data = itemsDataa.data;
          this.itemsData = JSON.parse(data[0].screenData);
        });
      } else {
        this.menuVariable = this.data;
        console.log("this.menuVariable===",this.menuVariable);
  
        const jsonString = JSON.stringify(this.menuVariable);
  
        console.log(jsonString);
  
  
  
  
  
        const response = await axios.get(GlobalConstants.getScreenPreviewData + this.menuVariable);
        console.log("Response:", response);
        const responseaxios = response.data;
        let data =responseaxios[0].screenData;
        this.itemsData = JSON.parse(data);
        console.log("this.itemsData===",this.itemsData);
      }
  console.log("this.itemdata------->",this.itemsData)
    }
  
    openChart(data: any) {
      this.http.post<any>(GlobalConstants.selectChartRelatedToKpi + data.value, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          this.chartData = res
  
          setTimeout(() => {
            const dialogConfig = new MatDialogConfig();
            dialogConfig.width = '700px';
            dialogConfig.height = '700px';
  
            const dialogRef = this.dialog.open(ChartFromKpiBuilderComponent, {
              data: this.chartData,
              width: '50%',
              height: '60%',
            });
          }, 1000);
        });
    }
  
    openGrid(data: any) {
      console.log(data);
      console.log("@##", data.value)
      this.http.post<any>(GlobalConstants.selectGridRelatedToKpi + data.value, { headers: GlobalConstants.headers }).subscribe(
        (res: any) => {
          this.gridData = {
            data: res,
            flag: 1
          }
          console.log(res);
          console.log("><><><<>", this.gridData);
          setTimeout(() => {
            const dialogConfig = new MatDialogConfig();
            dialogConfig.width = '500px';
            dialogConfig.height = '600px';
  
            const dialogRef = this.dialog.open(GridBuilderPreviewComponent, {
              data: this.gridData,
              width: '50%',
              height: '60%',
            });
          }, 1000);
        });
    }
  
  
    sanitizeHtml(html: string): SafeHtml {
      return this.sanitizer.bypassSecurityTrustHtml(html);
    }
  
  }
  
  