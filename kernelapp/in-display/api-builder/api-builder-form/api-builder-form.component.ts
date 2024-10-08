import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'api-builder-form',
  templateUrl: './api-builder-form.component.html',
  styleUrls: ['./api-builder-form.component.css'],
})
export class APIBuilderForm implements OnInit {

  public apiMethodId: string = '';
  public actionType: string = '';
  public menuPath: any = this.informationservice.getMenuPath();
  public showToken: boolean =false;

  constructor(
    private _Activatedroute: ActivatedRoute,
    public datepipe: DatePipe,
    private commonFunctions: CommonFunctions,
    private route: Router,
    public informationservice: InformationService
  ) { }

  apiBuilderForm = new UntypedFormGroup({
    methodId: new UntypedFormControl(''),
    methodName: new UntypedFormControl(''),
    url: new UntypedFormControl(''),
    token: new UntypedFormControl(''),
    header: new UntypedFormControl(''),
    flagID: new UntypedFormControl(''),
    methodType: new UntypedFormControl(''),
    tokeFlag: new UntypedFormControl(''),
    responseParameter: new UntypedFormControl(''),
    requestParameter: new UntypedFormControl(''),
    paramType:new UntypedFormControl(''),
  });


  ngOnInit(): void {
    this._Activatedroute.paramMap.subscribe((params) => {

      this.apiMethodId = params.get('id');
      this.informationservice.setUserId(this.apiMethodId);
      this.actionType = params.get('actionType');

    });


    if (this.actionType == 'create') {

    }else{
      this.fetchApiFormData();
    }
  }

  async fetchApiFormData() {

    const fetchUSMUser = from(axios.post(GlobalConstants.getAPIMethodFormResults + this.apiMethodId));
    const fetchUSMUserUrl = await lastValueFrom(fetchUSMUser);
    let res = fetchUSMUserUrl.data;
console.log(res)
    // to fetch regular input
    this.apiBuilderForm.controls['methodId'].setValue(res[0].method_id);
    this.apiBuilderForm.controls['methodName'].setValue(res[0].method_name);
    this.apiBuilderForm.controls['url'].setValue(res[0].url);
    this.apiBuilderForm.controls['header'].setValue(res[0].header);
    this.apiBuilderForm.controls['flagID'].setValue(res[0].flag_id);
    this.apiBuilderForm.controls['methodType'].setValue(res[0].method_type);
    this.apiBuilderForm.controls['header'].setValue(res[0].header);
    this.apiBuilderForm.controls['requestParameter'].setValue(res[0].request_parameter);
    this.apiBuilderForm.controls['responseParameter'].setValue(res[0].response_parameter);
    this.apiBuilderForm.controls['tokeFlag'].setValue(res[0].token_flag);
    this.apiBuilderForm.controls['paramType'].setValue(res[0].param_type);
    this.apiBuilderForm.controls['token'].setValue(res[0].token);


    // to fetch the toggle
    if (res[0].token_flag == "0") {
      this.apiBuilderForm.controls['tokeFlag'].setValue(false);
      this.showToken=false;
    } else {
      this.apiBuilderForm.controls['tokeFlag'].setValue(true);
      this.showToken = true;
    }

  }

  async submitForm() {
    var methodId = this.apiBuilderForm.controls['methodId']?.value;
    var methodName = this.apiBuilderForm.controls['methodName']?.value;
    var url = this.apiBuilderForm.controls['url']?.value;
    var token = this.apiBuilderForm.controls['token']?.value;
    var header = this.apiBuilderForm.controls['header']?.value;
    var flagId = this.apiBuilderForm.controls['flagID']?.value;
    var methodType = this.apiBuilderForm.controls['methodType']?.value;
    var requestParameter = this.apiBuilderForm.controls['requestParameter']?.value;
    var responseParameter = this.apiBuilderForm.controls['responseParameter']?.value;
    var tokeFlag = this.apiBuilderForm.controls['tokeFlag']?.value;
    var paramType = this.apiBuilderForm.controls['paramType']?.value;

    const formData = new FormData();
    formData.append('method_id', methodId);
    formData.append('method_name', methodName);
    formData.append('url',url);
    formData.append('token', token);
    formData.append('header', header);
    formData.append('flag_id',flagId);
    formData.append('method_type', methodType);
    formData.append('request_parameter', requestParameter);
    formData.append('response_parameter',responseParameter);
    formData.append('token_flag', tokeFlag);
    formData.append('param_type', paramType);
    if(this.apiBuilderForm.controls['tokeFlag']?.value == true){
      tokeFlag=1;
    }else{
      tokeFlag=0;
    }
let json={
  'method_id':methodId,
  'method_name':methodName,
  'url':url,
  'token':token,
  'header':header,
  'flag_id':flagId,
  'method_type':methodType,
  'request_parameter':requestParameter,
  'response_parameter':responseParameter,
  'token_flag':tokeFlag,
  'param_type':paramType
}
      if (this.apiBuilderForm.status != 'INVALID') {
        if (this.actionType == 'create') {
            const insertApiDataApi = from(axios.post(GlobalConstants.insertApiData,json));
            const insertApiData = await lastValueFrom(insertApiDataApi);
            this.commonFunctions.alert("alert", 'Created Successfully');

            this.commonFunctions.navigateToPage("/dsp/apiBuilder");
    } else{
      const updateApiDataApi = from(axios.post(GlobalConstants.updateApiData+methodId,json));
      const updateApiData = await lastValueFrom(updateApiDataApi);
      this.commonFunctions.alert("alert", 'Updated Successfully');

      this.commonFunctions.navigateToPage("/dsp/apiBuilder");


    }
    }
}
tokeFlagChange(){

  if(this.apiBuilderForm.get('tokeFlag').value == true){
this.showToken = true;
  }else{
this.showToken = false;
this.apiBuilderForm.controls['token'].setValue('');
  }
}
}
