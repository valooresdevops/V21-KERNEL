import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { ButtonRendererComponent } from 'src/app/Kernel/kernelapp/qbe/query-builder/buttonRenderer.component';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { InDisplayLogsDetailsComponent } from './in-display-logs-details/in-display-logs-details.component';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';

@Component({
  selector: 'app-in-display-logs',
  templateUrl: './in-display-logs.component.html',
  styleUrls: ['./in-display-logs.component.css']
})
export class InDisplayLogsComponent implements OnInit {

  public getInDisplayLogs = GlobalConstants.getInDisplayLogs;
  frameworkComponents:any;
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  
  constructor(private http: HttpClient,public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService, private dialog: MatDialog,
    public informationservice: InformationService) {
      this.onRunButtonClick = this.onRunButtonClick.bind(this);

      this.frameworkComponents = {
        buttonRenderer: ButtonRendererComponent,
      };
    }

  ngOnInit(): void {
    this.agColumnsJson = [
      {
        headerName: 'Log Id',
        field: 'logId',
        hide:true
      },
      {
        headerName: 'Log',
        field: 'tableName',
      },
      {
        headerName: 'Type',
        field: 'actionType',
      },
      {
        headerName: 'Action',
        field: 'actionText',
      },
      {
        headerName: 'User',
        field: 'loggedBy',
      },
      {
        headerName: 'Date',
        field: 'logDate',
      },
      {
        headerName: 'Details',
        field: 'changes',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClick.bind(this),
          label: ''
        }
      },
    ];

    this.agColumns.push(this.agColumnsJson);
  }


  async onRunButtonClick(){
    const getInDisplayLogsDetailsApi = from(axios.post(GlobalConstants.getInDisplayLogsDetails+this.informationservice.getAgGidSelectedNode()));
    const getInDisplayLogsDetails = await lastValueFrom(getInDisplayLogsDetailsApi);
    const dialogConfig = new MatDialogConfig();
    // dialogConfig.width = '700px';
    // dialogConfig.height = '700px';
    
    const dialogRef = this.dialog.open(InDisplayLogsDetailsComponent, {
      data: JSON.stringify(getInDisplayLogsDetails.data).replace(/,/g, ",\n"),
      width: '50%',
      height: '50%',
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
