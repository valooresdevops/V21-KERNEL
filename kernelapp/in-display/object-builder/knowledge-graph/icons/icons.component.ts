import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
import { AgColumns } from 'src/app/Kernel/common/AGColumns';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { DataService } from 'src/app/Kernel/services/data.service';
import { Router } from '@angular/router';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { IconsFormComponent } from './icons-form/icons-form.component';
import { from, lastValueFrom } from 'rxjs';
import axios from 'axios';

interface GridRow {
  id: number;
  // Add other fields if needed
}
@Component({
  selector: 'app-icons',
  templateUrl: './icons.component.html',
  styleUrls: ['./icons.component.css']
})
export class IconsComponent implements OnInit {
  
  public agColumns: AgColumns[] = [];
  public agColumnsJson: any;
  public iconData: any=GlobalConstants.getIconData; // To hold the data for the grid
  public reloadGrid: boolean = true;
  public action: any;
  selectedIconId: number | null = null;
  @Output() onGridEventSave = new EventEmitter<any>();
  getUpdatedGridData(): any[] {
    // Implement logic to fetch and return the updated grid data
    return [];
  }
  constructor(
    private router: Router,
    private http: HttpClient,
    public commonFunctions: CommonFunctions,
     private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService,
    
  ){

  }

  ngOnInit() {
    this.reloadGrid=this.informationservice.getreloadGrid();
    this.agColumnsJson = [
      {
        headerName: 'Id',
        field: 'id',
        width: '25%',
        cellRenderer: (params: { value: any; }) => `<div style="margin-top:1px;">${params.value}</div>`
      },
      {
        headerName: 'Name',
        field: 'imageName',
        width: '30%',
        cellRenderer: (params: { value: any; }) => `<div style="margin-top:1px;">${params.value}</div>`

      },
      {
        headerName: 'Icon Image',
        field: 'imageByteStream',
        cellRenderer: this.imageRenderer.bind(this), // Use the custom renderer
        width: '30%',
        
      }
    ];
    this.agColumns.push(this.agColumnsJson);
   
  }

  // initializeGrid() {
  //   console.log("Grid initializing");
  //   // Define your columns here
  //   this.agColumnsJson = [
  //     {
  //       headerName: 'Icon Name',
  //       field: 'imageName',
  //       width: '40%'
  //     },
  //     {
  //       headerName: 'Icon Image',
  //       field: 'imageByteStream',
  //       // cellRenderer: this.imageRenderer.bind(this), // Use the custom renderer
  //       width: '30%'
  //     }
  //     // {
  //     //   headerName: 'Info',
  //     //   field: 'info',
  //     //   width: '30%'
  //     // }
  //   ];
  //   this.agColumns.push(this.agColumnsJson);
  // }

  // async loadIconData() {
  //   try {
  //   //  const url = GlobalConstants.getIconData;

  //     // Using axios to perform a POST request to fetch the data
  //    // const res = await axios.get(url,{});
  //     //const res = await lastValueFrom(getIcons);
  //   //  this.iconData=res.data;


  //     console.log('Icon data loaded:', this.iconData);
  //   } catch (error) {
  //     console.error('Error loading icon data:', error);
  //   }
  // }
  onAddClick() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '43%';
    dialogConfig.height = '30%';
  
    const dialogRef = this.dialog.open(IconsFormComponent, dialogConfig);
  
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
  
  // async onDeleteIcon(iconId: number) {
  //   const apiEndpoint = `${GlobalConstants.deleteIconData}/${iconId}`;
  //   console.log("on delete icon >>>>>>>>>",iconId);
  //   try {
  //     const deleteIconApi = from(axios.post(apiEndpoint));
  //     await lastValueFrom(deleteIconApi);
  //     console.log('Icon deleted successfully');
  //     this.informationservice.setreloadGrid(true); // Trigger a grid refresh
  //   } catch (error) {
  //     console.error('Delete failed', error);
  //   }
  // }

  
  onDeleteClick(): void {
    const selectedRow = this.informationservice.getSelectedNodeVGrid() as GridRow; // Cast to GridRow

    if (!selectedRow || typeof selectedRow.id !== 'number') {
      alert("Please choose a selected row to delete");
      return;
    }
    const selectedRowId = selectedRow.id;

    const text = "Are you sure you want to delete?";

      this.deleteIcon(selectedRowId);
    
    // GlobalConstants.getIconData;
  }

  async deleteIcon(iconId: number): Promise<void> {
    try {
      const apiEndpoint = `${GlobalConstants.deleteIconData}${iconId}`;
      const deleteIconApi = from(axios.post(apiEndpoint));
      const deleteIconData = await lastValueFrom(deleteIconApi);
      const responseData = deleteIconData.data;
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


  onUpdateClick(icon: any) {
    console.log("On update");
    this.action = 'update';
   const data : any=this.informationservice.getSelectedNodeVGrid();
  //  console.log("dataVGrid>>>>>", data);
  //  console.log("dataVGrid>>>>>", typeof data);
    const selectedNode = this.informationservice.getAgGidSelectedNode();
    // console.log("Selected node>>>>>>>>>frtend",selectedNode);
    if (selectedNode === "") {
      // console.log('No valid selection for update');
      return;
    }
  
    // this.dataservice.setactionType(this.action);
  
    const dialogConfig = new MatDialogConfig();
    dialogConfig.width = '43%';
    dialogConfig.height = '30%';
    dialogConfig.data = {
      iconId: data.id,              // Pass the ID of the icon to update
      imageName: data.imageName,    // Pass the current image name
      imageByteStream: data.imageByteStream  // Pass the current image data
    };
  //   console.log("before dialog open");
  // console.log("data:>>>>>>>>>>>",dialogConfig.data);
    const dialogRef = this.dialog.open(IconsFormComponent, dialogConfig);
  
    dialogRef.afterClosed().subscribe(result => {
      if (result && result.success) {
        // this.reloadGrid = this.informationservice.getreloadGrid();
        this.reloadGrid = false;
      setTimeout(() => {
        this.reloadGrid = true;
      }, 100);
        // Handle successful update (e.g., refresh the grid)
      }
    });
  }
  ngOnChanges() {

     this.informationservice.getAgGidSelectedNode();
     this.agColumnsJson ;
  }

  // Renderer function for the image column
  // imageRenderer(params: any) {
  //   console.log("Entering imageRenderer()");

  //   if (params.value) {
  //       // console.log("Entering If statement");
  //       let base64Data = params.value;

  //       // Correct the prefix if necessary
  //       if (!base64Data.startsWith('data:image/')) {
  //           if (base64Data.includes('jpegbase64')) {
  //               base64Data = 'data:image/jpeg;base64,' + base64Data.split('jpegbase64')[1];
  //               console.log("JPG>>>>>>>>",base64Data)
  //           } else if (base64Data.includes('webpbase')) {
  //             // console.log("WEBP IMAGE before>>>>>>>>",base64Data);
  //               base64Data = 'data:image/webp;base64,' + base64Data.split('webpbase64')[1];
  //               console.log("WEBP IMAGE>>>>>>>>",base64Data);
  //           } else if (base64Data.includes('pngbase64')) {
  //                    base64Data = 'data:image/png;base64,' + base64Data.split('pngbase64')[1];
  //                    console.log("PNG>>>>>>>>",base64Data);
  //           }
  //           else {
  //               // Default to PNG if no recognizable format is found
  //               base64Data = 'data:image/png;base64,' + base64Data;
  //           }
  //       }
  //         return `<img src="${base64Data}" alt="Icon" onerror="this.onerror=null;this.src='../../../../../assets/assets/images/download (2).jpg';" style="width: 50px; height: 50px;">`;
  //   } else {
  //       console.log("Entering else statement");
  //       return '<img src="../assets/images/searchIcon.png" alt="No Image" style="width: 50px; height: 50px;">';
  //   }
  //     }

  imageRenderer(params: any) {
    console.log("Entering imageRenderer()");

    if (params.value) {
      let base64Data = params.value;
  
      // Extract the full path after the first comma
      let base64Image = base64Data.substring(base64Data.indexOf(',')+ 1);
  
      console.log("SPLIT>>>>>", base64Image);
  
        // If no Base64 image data is found after the split, default to PNG
        if (!base64Image) {
            base64Image = base64Data; // Fallback if splitting fails
        }
        // Return the image element with the correctly formatted src
        return `<img src="${base64Image}" alt="Icon"  onerror="this.onerror=null;this.src='../../../../../assets/assets/images/download (2).jpg';" style="width: 50px; height: 50px; border-radius: 50%; object-fit: cover;">`;
    } else {
        console.log("No image data available");
        return `<img src="../assets/images/searchIcon.png" alt="No Image" style="width: 50px; height: 50px;">`;
    }
}
}


