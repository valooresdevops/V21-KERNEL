import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PageNotFoundComponent } from './Kernel/components/page-not-found/page-not-found.component';
import { Login } from './Kernel/kernelapp/login/login';
import { AuthGuard } from './Kernel/guards/auth.guard';
import { LoginGuard } from './Kernel/guards/login.guards';
import { DashboardComponent } from './Kernel/kernelapp/dashboard/dashboard.component';
import { AddressbookComponent } from './Kernel/kernelapp/workflow/settings/addressbook/addressbook.component';
import { DynamicScreenComponent } from './Kernel/kernelapp/in-display/screen-builder/dynamic-screen/dynamic-screen.component';

const routes: Routes = [
  { path: '',  redirectTo: '/dashboard' , pathMatch: 'full'},
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  { path: 'usm', loadChildren: () => import('./Kernel/kernelapp/usm/usm-routing.module').then((m) => m.UsmRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'USM'} },
  { path: 'kyc', loadChildren: () => import('./kyc/kyc-routing.module').then((m) => m.KycRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'KYC'} },
  { path: 'aml', loadChildren: () => import('./aml/aml-routing.module').then((m) => m.AmlRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'AML'} },
  { path: 'nms', loadChildren: () => import('./nms/nms-routing.module').then((m) => m.NmsRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'NMS'} },
  { path: 'cds', loadChildren: () => import('./Kernel/kernelapp/cds/cds-routing.module').then((m) => m.CdsRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'CDS'} },
  { path: 'qbe', loadChildren: () => import('./Kernel/kernelapp/qbe/qbe-routing.module').then((m) => m.QBERoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'QBE'} },
  { path: 'dsp', loadChildren: () => import('./Kernel/kernelapp/in-display/in-display-routing.module').then((m) => m.InDisplayRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'DSP'} },
  { path: 'wfm', loadChildren: () => import('./Kernel/kernelapp/workflow/workflow-routing.module').then((m) => m.WorkflowRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'WORKFLOW'} },
  { path: 'vcis',loadChildren: () => import('./vcis/vcis-routing.module').then((m) => m.VcisRoutingModule), canActivate: [AuthGuard], data: { breadcrumb: 'VCIS'} },
  { path: 'login', component: Login, canActivate: [LoginGuard] },
  { path: 'page-not-found', component: PageNotFoundComponent, canActivate: [AuthGuard]},
  { path: '**', redirectTo: '/page-not-found'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [],
  declarations: [],
})
export class AppRoutingModule {}

