import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-execute-query',
  templateUrl: './execute-cqlquery.component.html',
  styleUrls: ['./execute-cqlquery.component.css']
})
export class ExecuteCqlQueryComponent implements OnInit {
  public agColumns: AgColumns[] = [];
  // public agColumnsJson:any = [];
  
  public parameters: any;
  public showPopup: boolean = false;
  public agGridSelectedNodes: any = '';
  public queryParam :any=this.data.query;
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

  constructor(private http: HttpClient,public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    @Inject(MAT_DIALOG_DATA) public data:any,
    private dialog: MatDialog, private snackBar: MatSnackBar,
    private _Activatedroute: ActivatedRoute,
    public datepipe: DatePipe,
    private route: Router,
    public informationservice: InformationService) { }
    public queryID:any; 


    public agColumnsJson: any = [];

  ngOnInit(): void {
    if(this.informationservice.getAgGidSelectedNode() !=''){
      this.queryID = JSON.parse(this.informationservice.getAgGidSelectedNode())[0].QBE_ID;
    }else{
      this.queryID = 0;
    }
    this.http.post<any>(GlobalConstants.getParamSession+"paramAdd_"+sessionStorage.getItem("session_serial"),{}).subscribe(
      async (res: any) => {
        if (res.status == 'Fail') {
        } else {

          for(let i = 0; i < res.length; i++) {
            console.log("PARAM>>>>>>>>",res[i]);
          this.paramValForm.addControl(res[i].paramName, new UntypedFormControl(''));
          }
        this.parameters = res;
        console.log("THIS PARAMETERS>>>>>>>>>>",this.parameters);
        console.log("THIS PARAMETERS FORM>>>>>>>>>>",this.paramValForm);

          setTimeout(() => {
            console.log("ELIE1")
            this.showInput=true;
          }, 700);

          if(res!=null || res!='' || res!='[]'){
            console.log("ELIE2")

            this.formExists=true;
          }

        }
      });
  }
  async execution(){
    this.agColumnsJson=[];
    this.agColumns=[];
    this.showExecGrid=true;

    this.currentQuery=this.data.query;
    console.log("this.currentQuery------->",this.currentQuery);


    if(this.formExists==true){

      console.log("CUURENT QUERY>>>>>>>",this.currentQuery);

      
      for(let i=0;i<this.parameters.length;i++){
        console.log("PENETRATION>>>>>>>>",this.paramValForm.get(this.parameters[i].paramName)?.value);

        if(this.paramValForm.get(this.parameters[i].paramName)?.value.includes(",")){
          let multiParam=this.paramValForm.get(this.parameters[i].paramName)?.value.replaceAll(",","','");
          this.currentQuery=this.currentQuery.replaceAll("["+this.parameters[i].paramName+"]","'"+multiParam+"'");
        }else{
          //jpppppp
          console.log('------>',this.parameters[i]);
          if(this.parameters[i].paramType == 'text'){
            this.currentQuery=this.currentQuery.replaceAll("["+this.parameters[i].paramName+"]","'"+this.paramValForm.get(this.parameters[i].paramName)?.value+"'");

          }else{
            this.currentQuery=this.currentQuery.replaceAll("["+this.parameters[i].paramName+"]",this.paramValForm.get(this.parameters[i].paramName)?.value);

          }
        }
      }

      console.log("The current Query is>>>>>>>>",this.currentQuery);

      // console.log("The encoded Query is>>>>>>",btoa(this.currentQuery));

    }
console.log('jp----------------------------->',this.informationservice.getAgGidSelectedNode())
    if(this.informationservice.getAgGidSelectedNode() !=''){
      this.queryID = JSON.parse(this.informationservice.getAgGidSelectedNode())[0].QBE_ID;
    }else{
      this.queryID = 0;
    }

    // if(this.queryID == undefined || this.queryID == '' || this.queryID == null){
    //   this.queryID = 0;
    // }
      
            this.http.get<any>(GlobalConstants.cqlFetchDynamicHeaderData+"execHeads_"+sessionStorage.getItem("session_serial")+"/"+this.queryID).subscribe(
      (res: any) => {
        if (res.status == 'Fail') {
          this.commonFunctions.alert("alert", res.description);
        } else {
          for(let i = 0; i < res.length; i++) {
            this.agColumnsJson.push({"headerName": res[i].headerName, "field": res[i].field});
          }

          this.agColumns.push(this.agColumnsJson);
          // this.currentQuery=btoa(this.currentQuery);
          this.fetchDynamicData=GlobalConstants.cqlFetchDynamicData +this.currentQuery+"/"+this.queryID;
      
        }

      });
     
      this.agColumns.push(this.agColumnsJson);

        setTimeout(() => {
          this.showExecGrid=false;

        }, 100);
        setTimeout(() => {
          this.showExecGrid=true;
          console.log("DYNAMIC DATA>>>>>>>>>",this.fetchDynamicData);
        }, 300);
          
  }


}
