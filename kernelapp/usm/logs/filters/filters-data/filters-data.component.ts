import { Component ,OnInit} from '@angular/core';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import axios from 'axios';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { from, lastValueFrom } from 'rxjs';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-filters-data',
  templateUrl: './filters-data.component.html',
  styleUrl: './filters-data.component.css'
})
export class FiltersDataComponent implements OnInit{
  public agColumnsJson: any;
  agColumns: AgColumns[] = [];
  isShown: boolean=true;
  public getApplicationEvent: any;
  logId : any;

  constructor(private informationService : InformationService){

  }

  async ngOnInit(): Promise<void> {
    this.agColumnsJson = [
    { headerName: 'Menu',field: 'processname', filter: 'agTextColumnFilter' },
    { headerName: 'Operation Type',field: 'operationtype', filter: 'agTextColumnFilter', sortable: true },
    { headerName: 'Hint',field: 'operationhint', filter: 'agTextColumnFilter' },
    { headerName: 'Operation Date',field: 'operationdate', filter: 'agTextColumnFilter' }
  ];
  this.agColumns.push(this.agColumnsJson);
  
  this.logId = this.informationService.getSelectedGridNode();
  const getLogsByEventApi = from(axios.post(GlobalConstants.getUSMApplicationEventPopup + this.logId,{}));
  const getLogsByEvent = await lastValueFrom(getLogsByEventApi);
  this.getApplicationEvent = getLogsByEvent.data;
//   this.getApplicationEvent = GlobalConstants.getUSMApplicationEvent + application + "/"  + loginDate + "/" + logoutDate;
 console.log (this.getApplicationEvent);

}
}
