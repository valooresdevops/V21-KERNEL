import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';

@Component({
  selector: 'app-serverparameters',
  templateUrl: './serverparameters.html',
  styleUrls: ['./serverparameters.css']
})
export class ServerParametersComponent implements OnInit {

  Parameters = new UntypedFormGroup({
    currentServerIp: new UntypedFormControl(''),
    currentServerName: new UntypedFormControl(''),
    wfmServerIp: new UntypedFormControl(''),
    wfmServerPort: new UntypedFormControl(''),
    infoLinkServerIp: new UntypedFormControl(''),
    infoLinkServerPort: new UntypedFormControl(''),
    wfmEngineActive:new UntypedFormControl(''),
    StatusEngineActive : new UntypedFormControl('')

  });


  constructor(private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,) { }

  ngOnInit(): void {
  }

}
