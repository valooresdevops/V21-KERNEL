import { NgModule } from '@angular/core';
import { CommonModules } from '../../custom-modules/common.module'
import { ComponentsModule } from './../../components/components.module';
import { InDisplayRoutingModule } from './in-display-routing.module';
import { BrowserModule } from '@angular/platform-browser';
import { FormBuilderComponent } from './form-builder/form-builder.component';
import { ScreenBuilderComponent } from './screen-builder/screen-builder.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FormComponent } from './form/form.component';
import { InputComponent } from './form-builder/input/input.component';
import { ColumnModifierComponent } from './form-builder/column-modifier/column-modifier.component';
import { PreviewFormComponent } from './form-builder/preview-form/main-preview-form.component';
import { NewTabComponent } from './form-builder/new-tab/new-tab.component';
import { AgGridModule } from 'ag-grid-angular';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ScreenBuilderToolComponent } from './screen-builder/screen-builder-tool/screen-builder-tool.component';
import { CKEditorModule } from 'ckeditor4-angular';
import { CellRenderer } from './screen-builder/screen-builder-tool/cellRenderer';
import { ApplicationListComponent } from './screen-builder/screen-builder-tool/application-list/application-list.component';
import { ParentMenuListComponent } from './screen-builder/screen-builder-tool/parent-menu-list/parent-menu-list.component';
import { DynamicScreenComponent } from './screen-builder/dynamic-screen/dynamic-screen.component';
import { PreviewScreenComponent } from './screen-builder/screen-builder-tool/preview-screen/preview-screen.component';
import { AmPreviewFormComponent } from './form-builder/preview-form/am-preview-form/am-preview-form.component';
import { NewbuttonComponent } from './form-builder/newbutton/newbutton.component';
import { FieldsetconfigurationComponent } from './form-builder/fieldsetconfiguration/fieldsetconfiguration.component';
import { ObjectBuilderComponent } from './object-builder/object-builder.component';
import { KpiBuilderComponent } from './object-builder/kpi-builder/kpi-builder.component';
import { ChartBuilderComponent } from './object-builder/chart-builder/chart-builder.component';
import { RatioBuilderComponent } from './object-builder/ratio-builder/ratio-builder.component';
import { ChartBuilderFormComponent } from './object-builder/chart-builder-form/chart-builder-form.component';
import { KpiBuilderFormComponent } from './object-builder/kpi-builder-form/kpi-builder-form.component';
import { RatioBuilderFormComponent } from './object-builder/ratio-builder-form/ratio-builder-form.component';
import { ChartBuilderTypeComponent } from './object-builder/chart-builder-type/chart-builder-type.component';
import { FormUpdateComponent } from './form/form-update/form-update.component';
import { GridBuilderFormComponent } from './object-builder/grid-builder-form/grid-builder-form.component';
import { GridBuilderPreviewComponent } from './object-builder/grid-builder-preview/grid-builder-preview.component';
import { GridBuilderComponent } from './object-builder/grid-builder/grid-builder.component';
import { KpiBuilderPreviewComponent } from './object-builder/kpi-builder-preview/kpi-builder-preview.component';
import { OrderManagementComponent } from './form-builder/order-management/order-management.component';
import { ChartFromKpiBuilderComponent } from './object-builder/chart-from-kpi-builder/chart-from-kpi-builder.component';
import { CustomFieldComponent } from './form-builder/custom-field/custom-field.component';
import { MasterLinkAnalysisComponent } from './object-builder/knowledge-graph/master-link-analysis/master-link-analysis.component';
import { MasterLinkFormComponent } from './object-builder/knowledge-graph/master-link-analysis/master-link-form/master-link-form.component';
import { ExecutedReportsComponent } from './object-builder/knowledge-graph/executed-reports/executed-reports.component';
import { CreateExecutedReportComponent } from './object-builder/knowledge-graph/master-link-analysis/create-executed-report/create-executed-report.component';
import { KwgCytoscapeComponent } from './object-builder/knowledge-graph/executed-reports/kwg-cytoscape/kwg-cytoscape.component';
import { PopupComponent } from './object-builder/knowledge-graph/popup/popup.component';
import { NextLayerFormComponent } from './object-builder/knowledge-graph/executed-reports/next-layer-form/next-layer-form.component';
import { ButtonJsonRelationComponent } from './form-builder/newbutton/button-json-relation/button-json-relation.component';

@NgModule({
    declarations: [
        FormBuilderComponent,
        ScreenBuilderComponent,
        FormComponent,
        InputComponent,
        ColumnModifierComponent,
        PreviewFormComponent,
        NewTabComponent,
        ScreenBuilderToolComponent,
        CellRenderer,
        ApplicationListComponent,
        ParentMenuListComponent,
        DynamicScreenComponent,
        PreviewScreenComponent,
        ParentMenuListComponent,
        AmPreviewFormComponent,
        NewbuttonComponent,
        FieldsetconfigurationComponent,
        ObjectBuilderComponent,
        KpiBuilderComponent,
        ChartBuilderComponent,
        RatioBuilderComponent,
        ChartBuilderFormComponent,
        KpiBuilderFormComponent,
        RatioBuilderFormComponent,
        ChartBuilderTypeComponent,
        FormUpdateComponent,
        ChartBuilderTypeComponent,
        GridBuilderComponent,
        GridBuilderFormComponent,
        GridBuilderPreviewComponent,
        KpiBuilderPreviewComponent,
        OrderManagementComponent,
        ChartFromKpiBuilderComponent,
        CustomFieldComponent,
        MasterLinkAnalysisComponent, 
        MasterLinkFormComponent,
        ExecutedReportsComponent,
        CreateExecutedReportComponent,
        KwgCytoscapeComponent,
        PopupComponent,
        NextLayerFormComponent,
        ButtonJsonRelationComponent
    ],
    imports: [
        InDisplayRoutingModule,
        ComponentsModule,
        ReactiveFormsModule,
        FormsModule,
        DragDropModule,
        CKEditorModule,
        CommonModules,
        // AgGridModule.withComponents([]),
        AgGridModule,
        BrowserModule,
        AgGridModule , 
    ] ,
    exports: [MasterLinkFormComponent, 
    ],
})

export class InDisplayModule { }
