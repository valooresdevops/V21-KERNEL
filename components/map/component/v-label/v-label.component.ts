import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';

@Component({
  selector: 'v-label',
  templateUrl: './v-label.component.html',
  styleUrls: ['./v-label.component.css']
})
export class LabelComponent implements OnInit {

  @Input() public text: any;
  @Input() public required: any = "false";
  @Input() public parentForm?: UntypedFormGroup;
  @Input() public fieldName: string = '';

  constructor() {}

  get formField():UntypedFormControl {
    if(this.fieldName != '') {
      return this.parentForm?.get( this.fieldName ) as UntypedFormControl;
    }
  }

  ngOnInit(): void {
  }

}
