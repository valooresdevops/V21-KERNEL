import { Component, OnInit,ViewChild } from '@angular/core';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { HttpClient } from '@angular/common/http';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { Subscription } from 'rxjs/internal/Subscription';
@Component({
  selector: 'app-internalsettings',
  templateUrl: './internalsettings.component.html',
  styleUrls: ['./internalsettings.component.css']
})
export class InternalsettingsComponent implements OnInit {

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public content: any;
  public agGridSelectedNodes: any = '';
  public comboDatasource = [{}];
  subsVar: Subscription;

 // Update Form Variables
 // public getUSMUsersApi: any = GlobalConstants.fetchUSMUsersApi;

 @ViewChild('addContent') addContent: any;
 @ViewChild('updateContent') updateContent: any;

  constructor(
    private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
  ) { }

  ngOnInit(): void {
    this.agColumnsJson = [
      { headerName: '',field: '',checkboxSelection: true,width: '25px', headerCheckboxSelection: true },
      { headerName: 'Id',field: 'id', filter: 'agTextColumnFilter' },
      { headerName: 'Name',field: 'name', filter: 'agTextColumnFilter' },
      { headerName: 'Active',field: 'active', filter: 'agTextColumnFilter' },
    ];
    this.agColumns.push(this.agColumnsJson);
    
    
  }

}
