import { NgModule } from '@angular/core';
import { MaterialModule } from '../Kernel/custom-modules/material.module'
import { CommonModules } from '../Kernel/custom-modules/common.module'
import { ComponentsModule } from '../Kernel/components/components.module';
import { GlobalConstants } from '../Kernel/common/GlobalConstants';
import { MapExplorerComponent } from './mapexplorer/mapexplorer.component';
import { VCISComponent } from './vcis.component';
// import { MapComponent } from '../Kernel/components/map/map/map.component';
// import { mapModule } from '../Kernel/components/map/map.module';


@NgModule({
  declarations: [ 
    VCISComponent,
    // MapExplorerComponent,
    //  MapComponent
  ],
  imports: [
    ComponentsModule,
    MaterialModule,
    CommonModules
  ],
  exports:[

  ],
  providers:[GlobalConstants],
  bootstrap:[],
})
export class VCISModule { }
