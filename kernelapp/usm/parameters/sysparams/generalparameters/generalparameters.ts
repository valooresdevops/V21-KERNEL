import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';

@Component({
  selector: 'app-generalparameters',
  templateUrl: './generalparameters.html',
  styleUrls: ['./generalparameters.css']
})
export class Generalparameters {

  public getGroupRoleMappingBehavior: any ;
  public getGroupRoleMappingType: any ;


  generalParameterssForm = new UntypedFormGroup({
    UDINATAID: new UntypedFormControl(''),
    MNOIL: new UntypedFormControl(''),
    UsersLogsByManager: new UntypedFormControl(''),
    DualFactorAuthenticationFlag: new UntypedFormControl(''),
    AGRMB: new UntypedFormControl(''),
    AGRMT: new UntypedFormControl('')
  });

  constructor(private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,) { }

  ngOnInit(): void {
  }

}
