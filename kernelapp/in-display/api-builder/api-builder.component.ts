import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { EventEmitterService } from 'src/app/Kernel/services/event-emitter.service';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'app-api-builder',
  templateUrl: './api-builder.component.html',
  styleUrls: ['./api-builder.component.css']
})
export class APIBuilderComponent implements OnInit {

  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public content: any;
  public agGridSelectedNodes: any = '';
  public comboDatasource = [{}];
  subsVar: Subscription;

  // Update Form Variables
  public getAPIMethodResults: any = GlobalConstants.getAPIMethodResults;

  @ViewChild('addContent') addContent: any;
  @ViewChild('updateContent') updateContent: any;

  constructor(
    private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private eventEmitterService: EventEmitterService,
    public informationservice: InformationService,
  ) {}

  ngOnInit() {

    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        width: '25px',
        headerCheckboxSelection: true
      },
      {
        headerName: 'Method Id',
        field: 'ID',
        filter: 'agTextColumnFilter',
        sortable: true
      },
      {
        headerName: 'Method Name',
        field: 'NAME',
        filter: 'agTextColumnFilter',
        sortable: true,
        isLink: true,
        linkType:'field',
        link: "/dsp/apiBuilder/form/update/",
        linkParameters: "method_id"
      },

    ];
    this.agColumns.push(this.agColumnsJson);
  }


  onAddClick() {
    this.commonFunctions.navigateToPage("/dsp/apiBuilder/form/create/-1");
  }

  onUpdateClick() {

    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    console.log("select row record --- ",this.agGridSelectedNodes);
    if(this.agGridSelectedNodes == '') {
      this.commonFunctions.alert("alert", "Please select a row");
    } else {
      this.commonFunctions.navigateToPage("/dsp/apiBuilder/form/update/"+this.informationservice.getAgGidSelectedNode());
    }
  }

  ngOnDestroy() {
    if (this.subsVar) {
       this.subsVar.unsubscribe()
     }
  }


  onDeleteClick() {

    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (selectedNodes.length == 0) {
      this.commonFunctions.alert("alert", 'Please select a row to delete');
    } else {
      if(selectedNodes.indexOf(",") != -1) {
      selectedNodes = selectedNodes.split(',');
      for (let i = 0; i < selectedNodes.length; i++) {
        this.http.delete<any>(GlobalConstants.deleteApiData + selectedNodes[i],
          {headers: GlobalConstants.headers}).subscribe({
          next:(res) => {
            console.log(res);
            this.commonFunctions.alert("alert", 'Deleted Successfully');
            this.commonFunctions.reloadPage('/dsp/apiBuilder');
          },
          error:(error) => {
            console.log(error);
          }
         } );
      }
    } else {
      console.log("selectedNodes>>>>>>>>>",selectedNodes);
      this.http.delete<any>(GlobalConstants.deleteApiData + selectedNodes, {headers: GlobalConstants.headers}).subscribe({
        next:(res) => {
          this.commonFunctions.alert("alert", 'Deleted Successfully');
          this.commonFunctions.reloadPage('/dsp/apiBuilder');
        },
       error:(error) => {
          console.log(error);
        }
    });
    }
  }
}


}
