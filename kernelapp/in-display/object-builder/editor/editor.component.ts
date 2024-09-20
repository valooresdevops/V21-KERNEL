import { Component } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { ButtonRendererComponent } from './buttonRenderer.component';
import { HttpClient } from '@angular/common/http';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { EditorFormComponent } from './editor-form/editor-form.component';
import { from } from 'rxjs';
import axios from 'axios';
import { EditorPreviewComponent } from './editor-preview/editor-preview.component';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css'
})
export class EditorComponent {
  public getElementData = GlobalConstants.getHtmlElementDataApi;

  public action: any;
  public agGridSelectedNodes: any = '';
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
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
  onAddClick() {
    this.action = 'add';
    this.informationservice.setactionType(this.action);
    const dialogConfig = new MatDialogConfig();
    const dialogRef = this.dialog.open(EditorFormComponent, {
      // data: info,
      width: '80%',
      height: '70%',
    });
    dialogRef.afterClosed().subscribe((res)=>{
      this.commonFunctions.reloadPage('/dsp/editor');
    });

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
  



  onUpdateClick(e: any) {
    this.action = 'update';
    let selectedNodeData = this.informationservice.getAgGridNodeData() as { data: { content: string, title: string } };
    let editorContent = selectedNodeData.data.content; // Assuming you want to display the 'title' in CKEditor
    let editorTitle = selectedNodeData.data.title; // Assuming you want to display the 'title' in CKEditor
    console.log("e>>>>>>>>>>>>>",selectedNodeData);
    console.log("selectedNodeData>>>>>>"+editorContent);

    if (this.informationservice.getAgGidSelectedNode().includes(",") || this.informationservice.getAgGidSelectedNode() == "") {

    } else {
      this.informationservice.setactionType(this.action);
      const dialogRef = this.dialog.open(EditorFormComponent, {
        data: {
          content: editorContent,
          title: editorTitle
        },
        width: '80%',
        height: '70%',
      });
      // dialogRef.afterClosed().subscribe((res)=>{
      //   this.commonFunctions.reloadPage('/dsp/editor');
      // });
  
    }
  }
  onDeleteClick() {
    
    this.agGridSelectedNodes = this.informationservice.getAgGidSelectedNode();
    let selectedNodes = this.agGridSelectedNodes;
    if (selectedNodes.length == 0) {
      console.log('Please select a row to delete');
    } else {
    from(axios.post(GlobalConstants.deleteHtmlElementDataById + "/" + selectedNodes,{}));
    this.commonFunctions.reloadPage('/dsp/editor');
    }
  }
}

