import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-in-display-logs-details',
  templateUrl: './in-display-logs-details.component.html',
  styleUrls: ['./in-display-logs-details.component.css']
})
export class InDisplayLogsDetailsComponent implements OnInit {

  public details:any;

  constructor(private dialogRef: MatDialogRef<InDisplayLogsDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public informationservice: InformationService) { }

  async ngOnInit(): Promise<void> {
    // const getInDisplayLogsDetailsApi = from(axios.post(GlobalConstants.getInDisplayLogsDetails+this.informationservice.getAgGidSelectedNode()));
    // const getInDisplayLogsDetails = await lastValueFrom(getInDisplayLogsDetailsApi);
    // console.log("CHANGES DETAILS>>>>>>",getInDisplayLogsDetails.data);
    // setTimeout(() => {
    //   this.details=JSON.stringify(getInDisplayLogsDetails.data);
    // }, 500);
    }

    closeDialog(): void {
      this.informationservice.setAgGidSelectedNode('');

      this.dialogRef.close();
    }
  

}
