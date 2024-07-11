import { NgModule, CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA, ErrorHandler} from '@angular/core';
import { AppComponent } from './app.component';
import { ComponentsModule } from './Kernel/components/components.module';
import { MaterialModule } from './Kernel/custom-modules/material.module';
import { AppRoutingModule } from './app-routing.module';
import { UsmModule } from './Kernel/kernelapp/usm/usm.module';
import { CdsModule } from './Kernel/kernelapp/cds/cds.module';
import { QbeModule } from './Kernel/kernelapp/qbe/qbe.module';
import { Login } from './Kernel/kernelapp/login/login';
import { DashboardComponent } from './Kernel/kernelapp/dashboard/dashboard.component';
import { Http_Interceptor } from './Kernel/services/http-interceptor.service';

import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { EventEmitterService } from './Kernel/services/event-emitter.service';
import { WorkflowModule } from './Kernel/kernelapp/workflow/workflow.module';
import { InDisplayModule } from './Kernel/kernelapp/in-display/in-display.module';
//import { NgChartsModule } from 'ng2-charts';
import { KycModule } from './kyc/kyc.module';
import { NmsModule } from './nms/nms.module';
import { DashboardPopupComponent } from './Kernel/kernelapp/dashboard/dashboard-popup/dashboard-popup.component';
import { KpiRatioPopupComponent } from './Kernel/kernelapp/dashboard/kpi-ratio-popup/kpi-ratio-popup.component';
import { GridPopupComponent } from './Kernel/kernelapp/dashboard/grid-popup/grid-popup.component';
import { ChartPopupComponent } from './Kernel/kernelapp/dashboard/chart-popup/chart-popup.component';
import { DashboardModifyPopupComponent } from './Kernel/kernelapp/dashboard/dashboard-modify-popup/dashboard-modify-popup.component';
import { PreviewScreenComponent } from './Kernel/kernelapp/in-display/screen-builder/screen-builder-tool/preview-screen/preview-screen.component';
import { AmlModule } from './aml/aml.module';
import { VCISModule } from './vcis/vcis.module';
// import { mapModule } from './Kernel/components/map/map.module';
import { PropertiesDialogComponent } from './Kernel/kernelapp/workflow/wfbuilder/properties-dialog/properties-dialog.component';
import { VReportGenerationComponent } from './Kernel/components/v-report-generation/v-report-generation.component';
import { ApplicationEventComponent } from './Kernel/kernelapp/workflow/wfbuilder/application-event/application-event.component';
import { ExternalEventComponent } from './Kernel/kernelapp/workflow/wfbuilder/external-event/external-event.component';
import { ScheduleEventComponent } from './Kernel/kernelapp/workflow/wfbuilder/schedule-event/schedule-event.component';
import { DynamicReportSaveComponent } from './Kernel/kernelapp/in-display/form-builder/preview-form/dynamic-report-save/dynamic-report-save.component';
import { DynamicReportResultComponent } from './Kernel/components/v-dynamic-report/dynamic-report-result/dynamic-report-result.component';

import { ArrowValueComponent } from './Kernel/kernelapp/workflow/wfbuilder/arrow-value/arrow-value.component';
@NgModule({
  declarations: [AppComponent, Login, DashboardComponent, DashboardPopupComponent, KpiRatioPopupComponent,
     GridPopupComponent, ChartPopupComponent, DashboardModifyPopupComponent, PropertiesDialogComponent,
     VReportGenerationComponent, DynamicReportSaveComponent, DynamicReportResultComponent,ApplicationEventComponent,
      ExternalEventComponent, ScheduleEventComponent, ArrowValueComponent],

   imports: [
    VCISModule,
    ComponentsModule,
    MaterialModule,
    // mapModule,
    AppRoutingModule,
    KycModule,
    NmsModule,
    AmlModule,
    UsmModule,
    CdsModule,
    QbeModule,
    // QbeModule,
    WorkflowModule,
    InDisplayModule,
    //NgChartsModule
  ],
  providers: [
     {
       provide: HTTP_INTERCEPTORS,
       useClass: Http_Interceptor,
       multi: true
     },
     
    EventEmitterService
  ],
  bootstrap: [AppComponent],

  schemas: [
    CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA
  ],
})
export class AppModule {}