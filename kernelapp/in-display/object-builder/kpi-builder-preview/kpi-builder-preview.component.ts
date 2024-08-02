import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { GridBuilderPreviewComponent } from '../grid-builder-preview/grid-builder-preview.component';
import { ChartFromKpiBuilderComponent } from '../chart-from-kpi-builder/chart-from-kpi-builder.component';
import { ChartBuilderFormComponent } from '../chart-builder-form/chart-builder-form.component';


@Component({
  selector: 'app-kpi-builder-preview',
  templateUrl: './kpi-builder-preview.component.html',
  styleUrls: ['./kpi-builder-preview.component.css']
})
export class KpiBuilderPreviewComponent implements OnInit {

  public info: any;
  public chartData: any;
  public gridData: any;
  constructor(private http: HttpClient, public commonFunctions: CommonFunctions,
    @Inject(MAT_DIALOG_DATA) public data: any, private dialog: MatDialog,) { }

    // valueFromSecondObject: string;

  ngOnInit(): void {
    this.http.post<any>(GlobalConstants.getKpiQueryData + this.data.kpiId, { headers: GlobalConstants.headers }).subscribe(
      (res: any) => {
        this.info = res;
        console.log("this.info = ", JSON.stringify(this.info));

        // Ensure the array has at least two objects
        // if (this.info.length > 1)
        // {
        //   const firstObject = this.info[0]; // Get the first object
        //   const secondObject = this.info[1]; // Get the second object

        //   // Get the keys of the first object
        //   const firstObjectKeys = Object.keys(firstObject);

        //   // Assuming you want the value from the second object corresponding to the first object's keys
        //   // Here, we assume you want the value of the first field in the first object from the second object
        //   const firstFieldName = firstObjectKeys[0];
        //   this.valueFromSecondObject = secondObject[firstFieldName];

        //   console.log("Value from the second object for the first field of the first object:", this.valueFromSecondObject);
        // }
        // else
        // {
        //   console.log("The array does not have enough elements.");
        // }
      });
  }

  openChart() {
    this.http.post<any>(GlobalConstants.selectChartRelatedToKpi + this.data.kpiId, { headers: GlobalConstants.headers }).subscribe(
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
  openGrid() {

    this.http.post<any>(GlobalConstants.selectGridRelatedToKpi + this.data.kpiId, { headers: GlobalConstants.headers }).subscribe(
      (res: any) => {
        this.gridData = {data:res,
          flag:1}
        setTimeout(() => {
          const dialogConfig = new MatDialogConfig();
          dialogConfig.width = '700px';
          dialogConfig.height = '700px';

          const dialogRef = this.dialog.open(GridBuilderPreviewComponent, {
            data: this.gridData,
            width: '50%',
            height: '60%',
          });
        }, 1000);
      });
  }

}
