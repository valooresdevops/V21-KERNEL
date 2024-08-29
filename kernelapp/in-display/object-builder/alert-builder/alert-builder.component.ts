import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { DataService } from 'src/app/Kernel/services/data.service';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { AlertBuilderFormComponent } from './alert-builder-form/alert-builder-form.component';
import { from, lastValueFrom } from 'rxjs';
import axios from 'axios';
import { AlertPreviewComponent } from './alert-preview/alert-preview.component';
import { Title } from '@angular/platform-browser';

interface GridRow {
  id: number;
  // Add other fields if needed
}
@Component({
  selector: 'app-alert-builder',
  templateUrl: './alert-builder.component.html',
  styleUrl: './alert-builder.component.css'
})
export class AlertBuilderComponent {
  public getAlertData = GlobalConstants.getAlertsDataApi;
  public reloadGrid: boolean = true;
  public action: any;
  public agGridSelectedNodes: any = '';
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  frameworkComponents: any;

  chartForm = new UntypedFormGroup({
    title: new UntypedFormControl(''),
    query: new UntypedFormControl(''),

  });
  public isQueryexecute: boolean;
  @Output() onGridEventSave = new EventEmitter<any>();
  selectedIconId: number | null = null;
  getUpdatedGridData(): any[] {
    // Implement logic to fetch and return the updated grid data
    return [];
  }
  constructor(private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService) {

    this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
  }

  ngOnInit(): void {
    this.reloadGrid=this.informationservice.getreloadGrid();

    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        width: '25px',
        headerCheckboxSelection: true
      }, {
        headerName: 'Alert Id',
        field: 'id',
        width: '25px',
      },
      {
        headerName: 'Title',
        field: 'title',
        width: '25px',
      },
      {
        headerName: 'Preview',
        field: '',
        cellRenderer: ButtonRendererComponent,
        cellRendererParams: {
          onClick: this.onRunButtonClick.bind(this),
          label: 'Click 1'
        }
      },
      {
        headerName: 'Created by',
        field: 'username',
      },
      {
        headerName: 'Created date',
        field: 'creationDate',
      }
    ];

    this.agColumns.push(this.agColumnsJson);
  }
  onAddClick() {
    
    
    this.action = 'add';
    this.dataservice.setactionType(this.action);
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '43%';
    dialogConfig.height = '35%';
    const dialogRef = this.dialog.open(AlertBuilderFormComponent, dialogConfig);
    dialogRef.afterClosed().subscribe(result => {
      if (result && result.success) {
        // this.reloadGrid=this.informationservice.getreloadGrid();
        this.reloadGrid = false;
      setTimeout(() => {
        this.reloadGrid = true;
      }, 100);
        // Handle successful upload (e.g., refresh the grid)
      }
    });

  }

  onRunButtonClick(params: any) {
    const rowID=params.rowData.id;
    const rowTitle = params.rowData.title; 
    const rowColor=params.rowData.color;
    // Open the dialog when the button is clicked
    this.dialog.open(AlertPreviewComponent, {
      data: {
        alertId:rowID,
        title:rowTitle,
        color:rowColor,
        rowData: params.rowData // Pass the selected row data to the dialog
      },
      width: '35%', // Adjust dialog size as needed
      height: '31%'
    });
    console.log("TITLE>>>>>>",rowTitle);
    console.log("TITLE>>>>>>",rowColor);
  }
  




    onUpdateClick() {
      console.log("On update");
      this.action = 'update';
      const data: any = this.informationservice.getSelectedNodeVGrid();
      const selectedNode = this.informationservice.getAgGidSelectedNode();
      if (selectedNode === "") {
        console.log('No valid selection for update');
        return;
      }
      const dialogConfig = new MatDialogConfig();
      dialogConfig.width = '43%';
      dialogConfig.height = '35%';
      dialogConfig.data = {
        alertId: data.id,              // Pass the ID of the alert to update
        title: data.title,             // Pass the current alert title
        color: data.color,             // Pass the current alert color
        functionality: data.functionality  // Pass the current functionality data
      };

        console.log("data:>>>>>>>>>>>",dialogConfig.data);
      const dialogRef = this.dialog.open(AlertBuilderFormComponent, dialogConfig);
      dialogRef.afterClosed().subscribe(result => {
        if (result && result.success) {
          this.reloadGrid = false;
          setTimeout(() => {
            this.reloadGrid = true;
          }, 100);
        }
      });
    }
  
  
    onDeleteClick(): void {
      const selectedRow = this.informationservice.getSelectedNodeVGrid() as GridRow; // Cast to GridRow
  
      if (!selectedRow || typeof selectedRow.id !== 'number') {
        alert("Please choose a selected row to delete");
        return;
      }
      const selectedRowId = selectedRow.id;
  
      const text = "Are you sure you want to delete?";
  
        this.deleteAlert(selectedRowId);
      
      // GlobalConstants.getIconData;
    }
    async deleteAlert(iconId: number): Promise<void> {
      try {
        const apiEndpoint = `${GlobalConstants.deleteAlertsDataApi}${iconId}`;
        const deleteAlertApi = from(axios.post(apiEndpoint));
        const deleteAlertData = await lastValueFrom(deleteAlertApi);
        const responseData = deleteAlertData.data;
        //console.log("Delete Response Data:", responseData);
  
        // Perform any additional actions, such as updating the UI
        //console.log("before event emitter");
        this.emitGridEventSave(); 
        //console.log("after event emitter");
  
        this.reloadGrid = false;
        setTimeout(() => {
          this.reloadGrid = true;
        }, 100);
  
      } catch (error) {
        console.error('Delete failed', error);
      }
    }
    emitGridEventSave() {
      const updatedData: any[] = this.getUpdatedGridData(); // Get updated grid data
      const agGridUpdates = {
        deleteList: [{ id: this.selectedIconId }],
        updatedData: updatedData // Include the updated data in the event
      };
      this.onGridEventSave.emit(agGridUpdates); // Emit the event with updated data
    }
  }

