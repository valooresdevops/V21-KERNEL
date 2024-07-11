import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { KYCComponent } from './kyc.component';
import { DynamicScreenComponent } from '../Kernel/kernelapp/in-display/screen-builder/dynamic-screen/dynamic-screen.component';


   const routes: Routes = [
   {
      path: '',
      component: KYCComponent,
      children:[
         {
            path: 'dynamicScreen/:menuVariable',
            data: { breadcrumb: 'Screen' },
            children: [
               {
                  path: '',
                  component: DynamicScreenComponent
               }
            ]
         }
        
      ],
   }];

   @NgModule({
      imports: [RouterModule.forChild(routes)],
      exports: [RouterModule],
      declarations: []
   })
   export class KycRoutingModule { }
