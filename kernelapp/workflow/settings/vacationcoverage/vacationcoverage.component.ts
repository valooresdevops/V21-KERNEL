import { Component, OnInit,ViewChild } from '@angular/core';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { HttpClient } from '@angular/common/http';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { from } from 'rxjs';
import axios from 'axios';
import { hide } from '@popperjs/core';

@Component({
  selector: 'app-vacationcoverage',
  templateUrl: './vacationcoverage.component.html',
  styleUrls: ['./vacationcoverage.component.css']
})
export class VacationcoverageComponent implements OnInit {

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public content: any;
  public agGridSelectedNodes: any = '';
  public comboDatasource = [{}];
  subsVar: Subscription;
  // public getData = from(axios.post(GlobalConstants.getVacationCoverageData , {}));
  public getData = GlobalConstants.getVacationCoverageData;

 // Update Form Variables
 // public getUSMUsersApi: any = GlobalConstants.fetchUSMUsersApi;

 @ViewChild('addContent') addContent: any;
 @ViewChild('updateContent') updateContent: any;

  constructor(
    private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
  ) { }

  ngOnInit(): void {
    this.agColumnsJson = [
      { headerName: '',field: '',checkboxSelection: true,width: '25px', headerCheckboxSelection: true },
      { headerName: 'ID',field: 'autoescalationid', filter: 'agTextColumnFilter', hide : true },
      { headerName: 'Escalation Type',field: 'escalationtype', filter: 'agTextColumnFilter' },
      { headerName: 'Escalation Object',field: 'escalationobject', filter: 'agTextColumnFilter' },
      { headerName: 'Original Assigned To',field: 'parentescalatorname', filter: 'agTextColumnFilter' },
      { headerName: 'Escalated To',field: 'childescalatorname', filter: 'agTextColumnFilter' },
      { headerName: 'Begin Date',field: 'begindate', filter: 'agTextColumnFilter' },
      { headerName: 'End Date',field: 'enddate', filter: 'agTextColumnFilter' },
    ];
    this.agColumns.push(this.agColumnsJson);
    
    
  }

}
