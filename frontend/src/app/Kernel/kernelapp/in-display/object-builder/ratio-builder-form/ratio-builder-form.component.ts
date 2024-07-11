import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';

@Component({
  selector: 'app-ratio-builder-form',
  templateUrl: './ratio-builder-form.component.html',
  styleUrls: ['./ratio-builder-form.component.css']
})
export class RatioBuilderFormComponent implements OnInit {
  public getQueryName = GlobalConstants.getQueryNameApi;
  ratioForm = new UntypedFormGroup({
    title: new UntypedFormControl(''),
    query: new UntypedFormControl(''),
    grid: new UntypedFormControl(''),
    chart: new UntypedFormControl(''),
    report: new UntypedFormControl(''),
  
 });

  constructor() { }

  ngOnInit(): void {
  }

}
