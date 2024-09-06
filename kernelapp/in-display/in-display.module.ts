import { DragDropModule } from '@angular/cdk/drag-drop';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';
import { AgGridModule } from 'ag-grid-angular';
import { CommonModules } from '../../custom-modules/common.module';
import { ComponentsModule } from './../../components/components.module';
import { APIBuilderForm } from './api-builder/api-builder-form/api-builder-form.component';
import { APIBuilderComponent } from './api-builder/api-builder.component';
import { ColumnModifierComponent } from './form-builder/column-modifier/column-modifier.component';
import { CustomFieldComponent } from './form-builder/custom-field/custom-field.component';
import { FieldsetconfigurationComponent } from './form-builder/fieldsetconfiguration/fieldsetconfiguration.component';
import { FormBuilderComponent } from './form-builder/form-builder.component';
import { InputComponent } from './form-builder/input/input.component';
import { AdvancedSearchConfigurationFormComponent } from './form-builder/new-tab/advanced-search-configuration/advanced-search-configuration-form/advanced-search-configuration-form.component';
import { AdvancedSearchConfigurationComponent } from './form-builder/new-tab/advanced-search-configuration/advanced-search-configuration.component';
import { NewTabComponent } from './form-builder/new-tab/new-tab.component';
import { ButtonJsonRelationComponent } from './form-builder/newbutton/button-json-relation/button-json-relation.component';
import { NewbuttonComponent } from './form-builder/newbutton/newbutton.component';
import { OrderManagementComponent } from './form-builder/order-management/order-management.component';
import { AmPreviewFormComponent } from './form-builder/preview-form/am-preview-form/am-preview-form.component';
import { PreviewFormComponent } from './form-builder/preview-form/main-preview-form.component';
import { FormUpdateComponent } from './form/form-update/form-update.component';
import { FormComponent } from './form/form.component';
import { InDisplayRoutingModule } from './in-display-routing.module';
import { AlertBuilderFormComponent } from './object-builder/alert-builder/alert-builder-form/alert-builder-form.component';
import { AlertBuilderComponent } from './object-builder/alert-builder/alert-builder.component';
import { AlertPreviewComponent } from './object-builder/alert-builder/alert-preview/alert-preview.component';
import { ChartBuilderFormComponent } from './object-builder/chart-builder-form/chart-builder-form.component';
import { ChartBuilderTypeComponent } from './object-builder/chart-builder-type/chart-builder-type.component';
import { ChartBuilderComponent } from './object-builder/chart-builder/chart-builder.component';
import { ChartFromKpiBuilderComponent } from './object-builder/chart-from-kpi-builder/chart-from-kpi-builder.component';
import { EditorFormComponent } from './object-builder/editor/editor-form/editor-form.component';
import { EditorPreviewComponent } from './object-builder/editor/editor-preview/editor-preview.component';
import { EditorComponent } from './object-builder/editor/editor.component';
import { GridBuilderFormComponent } from './object-builder/grid-builder-form/grid-builder-form.component';
import { GridBuilderPreviewComponent } from './object-builder/grid-builder-preview/grid-builder-preview.component';
import { GridBuilderComponent } from './object-builder/grid-builder/grid-builder.component';
import { ExecutedReportsComponent } from './object-builder/knowledge-graph/executed-reports/executed-reports.component';
import { KwgCytoscapeComponent } from './object-builder/knowledge-graph/executed-reports/kwg-cytoscape/kwg-cytoscape.component';
import { NextLayerFormComponent } from './object-builder/knowledge-graph/executed-reports/next-layer-form/next-layer-form.component';
import { IconsFormComponent } from './object-builder/knowledge-graph/icons/icons-form/icons-form.component';
import { IconsComponent } from './object-builder/knowledge-graph/icons/icons.component';
import { CreateExecutedReportComponent } from './object-builder/knowledge-graph/master-link-analysis/create-executed-report/create-executed-report.component';
import { MasterLinkAnalysisComponent } from './object-builder/knowledge-graph/master-link-analysis/master-link-analysis.component';
import { MasterLinkFormComponent } from './object-builder/knowledge-graph/master-link-analysis/master-link-form/master-link-form.component';
import { PopupComponent } from './object-builder/knowledge-graph/popup/popup.component';
import { KpiBuilderFormComponent } from './object-builder/kpi-builder-form/kpi-builder-form.component';
import { KpiBuilderPreviewComponent } from './object-builder/kpi-builder-preview/kpi-builder-preview.component';
import { KpiBuilderComponent } from './object-builder/kpi-builder/kpi-builder.component';
import { ObjectBuilderComponent } from './object-builder/object-builder.component';
import { RatioBuilderFormComponent } from './object-builder/ratio-builder-form/ratio-builder-form.component';
import { RatioBuilderComponent } from './object-builder/ratio-builder/ratio-builder.component';
import { DynamicScreenComponent } from './screen-builder/dynamic-screen/dynamic-screen.component';
import { ApplicationListComponent } from './screen-builder/screen-builder-tool/application-list/application-list.component';
import { CellRenderer } from './screen-builder/screen-builder-tool/cellRenderer';
import { ParentMenuListComponent } from './screen-builder/screen-builder-tool/parent-menu-list/parent-menu-list.component';
import { PreviewScreenComponent } from './screen-builder/screen-builder-tool/preview-screen/preview-screen.component';
import { ScreenBuilderToolComponent } from './screen-builder/screen-builder-tool/screen-builder-tool.component';
import { ScreenBuilderComponent } from './screen-builder/screen-builder.component';
import { SeparatorDirective } from 'src/assets/assets/seprator.directive';


@NgModule({
    declarations: [
        FormBuilderComponent,
        ScreenBuilderComponent,
        APIBuilderComponent,
        APIBuilderForm,
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
        ButtonJsonRelationComponent,
        AdvancedSearchConfigurationComponent,
        AdvancedSearchConfigurationFormComponent,
        EditorComponent,
        EditorFormComponent,
        EditorPreviewComponent,
        IconsFormComponent,
        AlertBuilderComponent,
        AlertBuilderFormComponent,
        IconsComponent,
        AlertPreviewComponent,
        SeparatorDirective
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
