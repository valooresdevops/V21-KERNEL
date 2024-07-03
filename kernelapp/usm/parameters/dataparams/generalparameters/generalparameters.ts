import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';

@Component({
  selector: 'app-generalparameters',
  templateUrl: './generalparameters.html',
  styleUrls: ['./generalparameters.css']
})
export class GeneralparametersComponent implements OnInit {

  public getPaysInstitutionnel: any;
  public getDefaultCurrency: any;
  public getDefaultLanguage: any;
  public getElementStructureAppearance: any;

  generalParametersForm = new UntypedFormGroup({
    paysInstitutionnel: new UntypedFormControl(''),
    mO: new UntypedFormControl(''),
    defaultCurrency: new UntypedFormControl(''),
    defaultLanguagev: new UntypedFormControl(''),
    elementStructureAppearance: new UntypedFormControl(''),
    FUMISL: new UntypedFormControl(''),
    FUMIECU: new UntypedFormControl(''),
    PSU: new UntypedFormControl(''),
    FUMIRU: new UntypedFormControl(''),
    FUMIBDL: new UntypedFormControl(''),
    FUMINDT: new UntypedFormControl('')
  });


  constructor(private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,) { }

  ngOnInit(): void {
  }

}
