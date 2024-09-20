// path: src/app/components/CheckboxCellRendererByCell.ts

import { Component, ViewChild, EventEmitter, Output } from '@angular/core';
import { AgRendererComponent } from 'ag-grid-angular';
import { IAfterGuiAttachedParams } from 'ag-grid-enterprise';
import { AGGridComponent } from '../v-grid.components';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  template: `
    <input type="checkbox"
           [id]="fieldId"
           [checked]="isChecked"
           [disabled]="isDisabled"
           (change)="onCheckboxChange($event)">
  `
})
export class CheckboxCellRendererByCell implements AgRendererComponent {
  params: any;
  isChecked: boolean = false;
  isDisabled: boolean = false;
  fieldId: string;

  @ViewChild(AGGridComponent) agGrid: any;
  @Output() cellClicked = new EventEmitter<any>(); // Event emitter to notify cell click

  constructor(private eventEmitterService: EventEmitterService,
              public informationservice: InformationService) {}

  agInit(params: any): void {
    this.params = params;
    this.fieldId = `chk_${params.data.id}_${params.colDef.field}`;
    this.isChecked = params.value === '1';
    this.isDisabled = params.isCheckBoxDisabled || false;
  }

  afterGuiAttached(params?: IAfterGuiAttachedParams): void {}

  onCheckboxChange(event: any): void {
    const newValue = event.target.checked ? '1' : '0';
    if (newValue !== this.params.value) {
      this.params.node.setDataValue(this.params.colDef.field, newValue);
      this.cellClicked.emit({
        rowId: this.params.data.id,
        colId: this.params.colDef.field,
        newValue: newValue
      });
      console.log("Checkbox clicked:", {
        rowId: this.params.data.id,
        colId: this.params.colDef.field,
        newValue: newValue,
        rowData: this.params.data // Logging the entire row data
      });
    }
  }

  refresh(params: any): boolean {
    this.params = params;
    this.isChecked = params.value === '1';
    return true; // Refresh the cell with new params
  }
}
