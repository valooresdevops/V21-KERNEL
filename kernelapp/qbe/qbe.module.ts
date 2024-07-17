import { NgModule } from '@angular/core';
import { CommonModules } from '../../../Kernel/custom-modules/common.module'
import { ComponentsModule } from './../../../Kernel/components/components.module';

import { RouterModule } from '@angular/router';
import { MaterialModule } from '../../../Kernel/custom-modules/material.module'
import { GlobalConstants } from "../../../Kernel/common/GlobalConstants";
import { QbeComponent } from './qbe.component';
import { ReportdesignerComponent } from './reportdesigner/reportdesigner.component';
import { QueryBuilderComponent } from './query-builder/query-builder.component';
import { StaticQueryBuilderComponent } from './query-builder/static-query-builder/static-query-builder.component';
import { ParameterBuilderComponent } from './query-builder/static-query-builder/parameter-builder/parameter-builder.component';
import { SavequeryComponent } from './query-builder/static-query-builder/savequery/savequery.component';
import { ExecuteQueryComponent } from './query-builder/static-query-builder/execute-query/execute-query.component';
import { ButtonRendererComponent } from './query-builder/buttonRenderer.component';
import { LinkQueryComponent } from './query-builder/static-query-builder/link-query/link-query.component';
import { SecurityQueryComponent } from './query-builder/static-query-builder/security-query/security-query.component';
import { TypeQueryComponent } from './query-builder/static-query-builder/type-query/type-query.component';
import { LinkProcedureComponent } from './query-builder/static-query-builder/link-procedure/link-procedure.component';
import { HeadersQueryComponent } from './query-builder/static-query-builder/headers-query/headers-query.component';
import { AddParamsComponent } from './query-builder/static-query-builder/parameter-builder/add-params/add-params.component';
import { QbeToolComponent } from './query-builder/qbe-tool/qbe-tool.component';
import { DataFilterComponent } from './query-builder/qbe-tool/data-filter/data-filter.component';
import { ShowGeneratedQueryComponent } from './query-builder/qbe-tool/show-generated-query/show-generated-query.component';
import { CqlshComponent } from './query-builder/cqlsh/cqlsh.component';
import { AddReportComponent } from './reportdesigner/add-report/add-report.component';
import { RunReportParametersComponent } from './reportdesigner/run-report-parameters/run-report-parameters.component';
import { ImportQueryComponent } from './query-builder/import-query/import-query.component';
import { SecurityUserQueryComponent } from './query-builder/static-query-builder/security-query/security-user-query/security-user-query.component';
import { ExecuteCqlQueryComponent } from './query-builder/cqlsh/execute-cqlquery/execute-cqlquery.component';
import { AdvanceRowIdComponent } from './query-builder/cqlsh/advance-row-id/advance-row-id.component';
@NgModule({
  declarations: [
    QbeComponent,
    ReportdesignerComponent,
    QueryBuilderComponent,
    StaticQueryBuilderComponent,
    ParameterBuilderComponent,
    SavequeryComponent,
    ExecuteQueryComponent,
    ExecuteCqlQueryComponent,
    ButtonRendererComponent,
    LinkQueryComponent,
    SecurityQueryComponent,
    TypeQueryComponent,
    LinkProcedureComponent,
    HeadersQueryComponent,
    AddParamsComponent,
    QbeToolComponent,
    DataFilterComponent,
    ShowGeneratedQueryComponent,
    CqlshComponent,
    AdvanceRowIdComponent,
    AddReportComponent,
    RunReportParametersComponent,
    ImportQueryComponent,
    SecurityUserQueryComponent   
  ],
  imports: [
    ComponentsModule,
    RouterModule,
    MaterialModule,
    CommonModules
  ],
  exports:[],
  providers:[GlobalConstants],
  bootstrap:[],
})
export class QbeModule { }
