import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
 import { DatacrowdService } from '../../Services/datacrowd.service';
import { AmPreviewFormComponent } from 'src/app/Kernel/kernelapp/in-display/form-builder/preview-form/am-preview-form/am-preview-form.component';
import { PreviewFormComponent } from 'src/app/Kernel/kernelapp/in-display/form-builder/preview-form/main-preview-form.component';
import { DynamicScreenComponent } from 'src/app/Kernel/kernelapp/in-display/screen-builder/dynamic-screen/dynamic-screen.component';

@Component({
  selector: 'app-textmenu',
  templateUrl: './textmenu.component.html',
  styleUrls: ['./textmenu.component.css']
})
export class TextmenuComponent implements OnInit {
  @Input() nextActionMenuList: any[]=[] ;
simulationtype:any;
objid:number;
  constructor( private dialog: MatDialog,
           private datacrowdservice:DatacrowdService
  ) { }

  ngOnInit(): void {
  console.log("nextActionMenuList in child comp:",this.nextActionMenuList);
   }
  async CallFunctions(menucode:any){
    console.log("menucode====",menucode);
//    await this.datacrowdservice.getObjectIdNextMenu(menucode).then((objid:number)=>{
//       console.log("objid====",objid);
// this.objid=objid;
//     });
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '700px';
      dialogConfig.height = '700px';

      let data = [{
        menucode: menucode,
      }];
      const dialogRef = this.dialog.open(DynamicScreenComponent, {
     data: menucode,
      width: '58%',
      height: '60%',
    });


    dialogRef.afterClosed().subscribe((res)=>{
      $("#DisplayFromSenario").click();

    });
   }
}
