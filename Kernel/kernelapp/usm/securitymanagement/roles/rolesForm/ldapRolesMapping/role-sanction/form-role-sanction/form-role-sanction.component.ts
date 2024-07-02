import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl, UntypedFormBuilder } from '@angular/forms';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';

@Component({
  selector: 'app-form-role-sanction',
  templateUrl: './form-role-sanction.component.html',
  styleUrls: ['./form-role-sanction.component.css']
})
export class FormRoleSanctionComponent implements OnInit {

  public sanctionCombo: any[] = [];
  public sanctionName: any;
  public lookupFieldName: string = "";
  public lookupSelection: string = "";
  public allRoleSanction: any;





  sanctionListForm = new UntypedFormGroup({
    sanctionReferenceCode: new UntypedFormControl(''),
    sanctionAbbreviation: new UntypedFormControl(''),
  });


  constructor(private http: HttpClient,
    private commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    private formBuilder: UntypedFormBuilder) { }

  ngOnInit(): void {
    this.allRoleSanction = GlobalConstants.getAllRoleSanctionList;
  }

}
