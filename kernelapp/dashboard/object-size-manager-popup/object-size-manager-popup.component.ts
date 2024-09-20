import { Component, OnInit, Inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { MatDialog } from '@angular/material/dialog';
import { InformationService } from 'src/app/Kernel/services/information.service';

import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-object-size-manager-popup',
  // standalone: true,
  templateUrl: './object-size-manager-popup.component.html',
  styleUrl: './object-size-manager-popup.component.css'
})
export class ObjectSizeManagerPopupComponent implements OnInit
{
  public objectWidthInDashboard: String;

  constructor(private http: HttpClient,
    public commonFunctions: CommonFunctions,
    public dialog: MatDialog,
   public informationService: InformationService,
   public dialogRef: MatDialogRef<ObjectSizeManagerPopupComponent>,
   @Inject(MAT_DIALOG_DATA) public data: any){}

  ngOnInit(): void
  {
    
  }

  saveOne()
  {
    this.objectWidthInDashboard = "1";
    this.saveDataToDatabase(this.objectWidthInDashboard);
  }

  saveTwo()
  {
    this.objectWidthInDashboard = "2";
    this.saveDataToDatabase(this.objectWidthInDashboard);
  }

  saveThree()
  {
    this.objectWidthInDashboard = "3";
    this.saveDataToDatabase(this.objectWidthInDashboard);
  }



  saveDataToDatabase(objectWidthInDashboard: String)
  {
    if(this.data == "chart")
    {
      this.http.post<any>(GlobalConstants.editChartSize+this.informationService.getAgGidSelectedNode()+'/'+objectWidthInDashboard, { headers: GlobalConstants.headers }).subscribe({})
    }
    
    if(this.data == "grid")
    {
      this.http.post<any>(GlobalConstants.editGridSize+this.informationService.getAgGidSelectedNode()+'/'+objectWidthInDashboard, { headers: GlobalConstants.headers }).subscribe({})
    }
    
    if(this.data == "kpi")
    {
      this.http.post<any>(GlobalConstants.editKpiSize+this.informationService.getAgGidSelectedNode()+'/'+objectWidthInDashboard, { headers: GlobalConstants.headers }).subscribe({})
    }

    if(this.data == "CkEditor")
      {
        this.http.post<any>(GlobalConstants.editCkEditorSize+this.informationService.getAgGidSelectedNode()+'/'+objectWidthInDashboard, { headers: GlobalConstants.headers }).subscribe({})
      }

    this.dialog.closeAll();
  }
}
