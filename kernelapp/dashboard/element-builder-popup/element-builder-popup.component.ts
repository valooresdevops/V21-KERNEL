import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { EditorPreviewComponent } from '../../in-display/object-builder/editor/editor-preview/editor-preview.component';
import { ObjectSizeManagerPopupComponent } from '../object-size-manager-popup/object-size-manager-popup.component';

@Component({
  selector: 'app-element-builder-popup',
  templateUrl: './element-builder-popup.component.html',
  styleUrl: './element-builder-popup.component.css'
})
export class ElementBuilderPopupComponent {
  public getElementData = GlobalConstants.getHtmlElementDataApi;

  public action: any;  public agGridSelectedNodes: any = '';
  public agColumnsJson: any;
  public agColumns: AgColumns[] = [];
  frameworkComponents: any;


  constructor(private http: HttpClient,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    public informationservice: InformationService) {

    this.onRunButtonClick = this.onRunButtonClick.bind(this);

    this.frameworkComponents = {
      buttonRenderer: ButtonRendererComponent,
    };
  }

  ngOnInit(): void {

    this.agColumnsJson = [
      {
        headerName: '',
        field: '',
        checkboxSelection: true,
        width: '25px',
        headerCheckboxSelection: true
      }, {
        headerName: 'Element Id',
        field: 'eId',
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
        field: 'userName',
      },
      {
        headerName: 'Created date',
        field: 'creationDate',

      }

    ];

    this.agColumns.push(this.agColumnsJson);
  }


  onRunButtonClick(e: any) {
    console.log("e>>>>>>>>>", e);
    this.action = 'run';
    this.informationservice.setactionType(this.action);
    let selectedNodeData = e.rowData;
    let editorContent = selectedNodeData.content; // Content from CKEditor
    let editorTitle = selectedNodeData.title; // Title from CKEditor
  
    // Open a dialog with the EditorPreviewComponent
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '50%';
    dialogConfig.height = '50%';
    dialogConfig.data = {
      content: editorContent,
      title: editorTitle
    };
    
    this.dialog.open(EditorPreviewComponent, dialogConfig);
  }

  Insert() {
    //test
    let selectedNodes = this.informationservice.getAgGidSelectedNode();
    const dialogRef = this.dialog.open(ObjectSizeManagerPopupComponent, {
      data: "CkEditor",
      width: '40%',
      height: '33%',
    });
    let allData = {
      objectId: selectedNodes,
      templateId: this.informationservice.getSelectedTabId()
    }
    console.log("ALLDATA>>>>>>>>>>>>",allData);
    this.http.post<any>(GlobalConstants.addDashboardHtmlElement, allData,
      { headers: GlobalConstants.headers }).subscribe({
        next: (res) => {
          console.log(res);

        },
        error: (error) => {
          console.log(error);
        }
      });  
        dialogRef.afterClosed().subscribe(result => {

    this.commonFunctions.reloadPage('/dashboard');
    this.commonFunctions.navigateToPage('/dashboard');
  });
  }
}
