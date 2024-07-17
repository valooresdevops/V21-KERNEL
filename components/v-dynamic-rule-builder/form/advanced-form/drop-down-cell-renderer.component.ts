import { Component } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';

interface ICellRendererParamsWithCustom extends ICellRendererParams {
  customData: any; // Example of additional custom data needed
}

@Component({
  selector: 'app-drop-down-cell-renderer',
  template: `<div>{{ displayValue }}</div>`
})
export class DropDownCellRendererComponent implements ICellRendererAngularComp {
  displayValue: string = '';
  params: ICellRendererParamsWithCustom;

  agInit(params: ICellRendererParamsWithCustom): void {
    this.params = params;
    this.updateDisplayValue();
  }

  refresh(params: ICellRendererParamsWithCustom): boolean {
    this.params = params;
    this.updateDisplayValue();
    return true;
  }

  private updateDisplayValue() {
    if (this.params.value != undefined) {
      if (this.params.value.name != undefined) {
        this.displayValue = this.params.value.name;
      } else {
        this.displayValue = String(this.params.value);
      }
    }
  }

  // Example of custom method that can be called from ag-Grid
  // Customize this as per your specific requirements
  customMethod() {
    console.log('Custom method called for row data:', this.params.node.data);
    // Implement your custom logic here
  }
}
