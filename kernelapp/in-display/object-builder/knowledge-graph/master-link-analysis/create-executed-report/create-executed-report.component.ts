import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-create-executed-report',
  templateUrl: './create-executed-report.component.html',
  styleUrl: './create-executed-report.component.css'
})
export class CreateExecutedReportComponent {

  public parameters: any;
  public showPopup: boolean = false;
  public agGridSelectedNodes: any = '';
  public fetchDynamicHeaderData: string='';
  public fetchDynamicData: string='';
  public getParamSession: string='';

  public formExists:boolean=false;
  public showInput :boolean=false;
  public showExecGrid:boolean=false;
  public formValues:any=[];
  public currentQuery:any;
  public subQuery:any;
  paramValForm = new UntypedFormGroup({});
  reportNameForm = new UntypedFormGroup({
    reportName: new UntypedFormControl('')
    });


  constructor(private http: HttpClient,public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    @Inject(MAT_DIALOG_DATA) public data:any,
    private dialog: MatDialog, private snackBar: MatSnackBar,
    private _Activatedroute: ActivatedRoute,
    public datepipe: DatePipe,
    private route: Router,
    public informationservice: InformationService) { }
    public queryParameters:any[]=[];

    public agColumnsJson: any = [];
  
  
    async ngOnInit(): Promise<void> {

      this.queryParameters=JSON.parse(this.data.info.queryParams)
      this.currentQuery=this.data.info.query;

     for(let i = 0; i < this.queryParameters.length; i++) {
       console.log("PARAM>>>>>>>>",this.queryParameters[i]);
     if(this.queryParameters[i].paramType=="query"){
      // const getSubQueriesApi=from(axios.get(GlobalConstants.getSubQueries+sessionStorage.getItem("session_serial")+"/"+this.data.queryParams[i].paramDefault+"/"+this.informationservice.getLogeduserId()))
      // const getSubQueries=await lastValueFrom(getSubQueriesApi);
      // this.subQuery=getSubQueries.data[0].query;

      //  this.data.query=this.data.query.replaceAll("["+this.data.queryParams[i].paramName+"]",this.subQuery);

     }else{
     this.paramValForm.addControl(this.queryParameters[i].paramName, new UntypedFormControl(''));
     }
  }
  
  setTimeout(() => {
    this.parameters=this.queryParameters;

    this.showInput=true;
  }, 700);


    }
    
    async execution(){
      let kwgProcedureParams={};
      console.log("CUURENT QUERY>>>>>>>",this.currentQuery);

      
      for(let i=0;i<this.parameters.length;i++){
       // console.log("PENETRATION>>>>>>>>",this.paramValForm.get(this.parameters[i].paramName)?.value);

        if(this.paramValForm.get(this.parameters[i].paramName)?.value.includes(",")){
          let multiParam=this.paramValForm.get(this.parameters[i].paramName)?.value.replaceAll(",","','");
          this.currentQuery=this.currentQuery.replaceAll("["+this.parameters[i].paramName+"]","'"+multiParam+"'");
        }else{
          this.currentQuery=this.currentQuery.replaceAll("["+this.parameters[i].paramName+"]","'"+this.paramValForm.get(this.parameters[i].paramName)?.value+"'");
        }
      }

      console.log("The current Query is>>>>>>>>",this.currentQuery);

      kwgProcedureParams={
        query:this.currentQuery,
        executedReportName:this.reportNameForm.get('reportName')?.value,
        userId:this.informationservice.getLogeduserId(),
        masterId:this.data.selectedData[0].masterId
      }
      console.log("KWG DATA>>>>>>>>",kwgProcedureParams);

      const executeMasterLinkApi = from(axios.post(GlobalConstants.executeMasterLink,kwgProcedureParams));
      const executeMasterLink = await lastValueFrom(executeMasterLinkApi);

      console.log("EXECUTEMASTERLINK DATA>>>>>>>>>>>>>>>",executeMasterLink.data);
      if(executeMasterLink.data==0){
        this.commonFunctions.alert("alert","Report Executed Successfully");
      }else{
        this.commonFunctions.alert("alert","Failed to Execute Report");
      }
    }
  
  }
