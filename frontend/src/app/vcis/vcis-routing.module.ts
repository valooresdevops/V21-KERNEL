import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VCISComponent } from './vcis.component';
import { MapExplorerComponent } from './mapexplorer/mapexplorer.component';


const routes: Routes = [
   {
      path: 'vcis',
      component: VCISComponent,
      // children:[
      //    {
      //       path: 'mapExplorer',
      //       data: { breadcrumb: 'mapExplorer'},
      //       children: [
      //          {
      //             path: '',
      //             component: MapExplorerComponent
      //          }
      //         ]
      //    }]
      
      }];


   @NgModule({
      imports: [RouterModule.forChild(routes)],
      exports: [RouterModule],
      declarations: []
   })
   export class VcisRoutingModule { }
