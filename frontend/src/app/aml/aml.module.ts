import { NgModule } from '@angular/core';
import { MaterialModule } from '../Kernel/custom-modules/material.module'
import { CommonModules } from '../Kernel/custom-modules/common.module'
import { ComponentsModule } from '../Kernel/components/components.module';
import { GlobalConstants } from "../Kernel/common/GlobalConstants";
import { DatePipe } from '@angular/common';
import { AmlComponent } from './aml.component';

@NgModule({
  declarations: [
    AmlComponent,
  ],
  imports: [
    ComponentsModule,
    MaterialModule,
    CommonModules
  ],
  exports:[],
  providers:[GlobalConstants,DatePipe],
  bootstrap:[],
})
export class AmlModule { }
