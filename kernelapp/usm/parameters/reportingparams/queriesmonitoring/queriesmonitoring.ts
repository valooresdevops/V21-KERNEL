import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';

@Component({
  selector: 'app-queriesmonitoring',
  templateUrl: './queriesmonitoring.html',
  styleUrls: ['./queriesmonitoring.css']
})
export class QueriesmonitoringComponent implements OnInit {

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public getQueriesMonitoring: any ;
  public agGridSelectedNodes: any = '';

  queriesMonitoringForm = new UntypedFormGroup({
    totalQueries: new UntypedFormControl(''),
    completedQueries: new UntypedFormControl(''),
    runningQueries: new UntypedFormControl(''),
    failedQueries: new UntypedFormControl(''),
    qbeQueries: new UntypedFormControl(''),
    wfmQueries: new UntypedFormControl(''),
    cdsQueries: new UntypedFormControl('')
  });


  SelectedQueryInformationForm = new UntypedFormGroup({
    queryId: new UntypedFormControl(''),
    queryName: new UntypedFormControl(''),
    performer: new UntypedFormControl(''),
    statuss: new UntypedFormControl(''),
    application: new UntypedFormControl(''),
    beginTime: new UntypedFormControl(''),
    endTime: new UntypedFormControl(''),
    executionTime: new UntypedFormControl('')

  });

  constructor(private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    private _Activatedroute: ActivatedRoute,) { }

  ngOnInit(): void {
    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        width: '25px',
        headerCheckboxSelection: true,
      },
      {
        headerName: 'Query Id',
        field: 'queryId',
        filter: 'agTextColumnFilter',
        sortable: true,

      },
      {
        headerName: 'Query Name',
        field: 'queryName',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'User',
        field: 'user',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'Application',
        field: 'application',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'Performer',
        field: 'performer',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'Status',
        field: 'status',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'Begin Time',
        field: 'beginTime',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'End Time',
        field: 'endTime',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
      {
        headerName: 'Execution Time',
        field: 'executionTime',
        filter: 'agTextColumnFilter',
        sortable: true,
      },
    ];
    this.agColumns.push(this.agColumnsJson);

  }

  }


