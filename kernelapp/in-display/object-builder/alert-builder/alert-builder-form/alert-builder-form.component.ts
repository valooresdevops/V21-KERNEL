import { Component, Inject, Input, NgZone, OnInit } from '@angular/core';
import { FormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { IconsFormComponent } from '../../knowledge-graph/icons/icons-form/icons-form.component';
import { HttpClient } from '@angular/common/http';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { Title } from '@angular/platform-browser';



@Component({
  selector: 'app-alert-builder-form',
  templateUrl: './alert-builder-form.component.html',
  styleUrl: './alert-builder-form.component.css'
})
export class AlertBuilderFormComponent implements OnInit {
  public getqueryData = GlobalConstants.getSourceQueryApi;
  public showFunctionality: boolean=false;

  @Input() existingImageData: string;
  // public functionalities: string[] = ['Functionality 1', 'Functionality 2', 'Functionality 3'];
  public userId = this.informationService.getLogeduserId();
  public qbe_id=this.informationService.getLookUpSubmitValue().name;
  public functionality = [{ id: 1, name: 'Generate Report'},{id: 2, name: 'Query'}];
  public  getGeneratedReport =GlobalConstants.getGeneratedReport;
  public selectedItems: any[] = [];
  public multiple: boolean = true; // Adjust based on your requirement
  public selectedValues: any[] = [];

  public generatedReports: any[] = []; 
  // public selectedItems: any[] = [];
  //public formField: UntypedFormControl = new UntypedFormControl();
  // uploadForm = new UntypedFormGroup({
   
  //   title: new UntypedFormControl('', Validators.required),
  //   color: new UntypedFormControl('', Validators.required), 
  //   functionality: new UntypedFormControl('', Validators.required), 
  //   qbe_id: new UntypedFormControl('', Validators.required),
  //   report_id: new UntypedFormControl('')
  // });
  uploadForm: UntypedFormGroup;
  

  isUpdate: boolean = false;
  report_id: any;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<IconsFormComponent>,
    @Inject(MAT_DIALOG_DATA) public dataa: any,
    private http: HttpClient,
    public informationService: InformationService,
    private zone: NgZone
  ) {
    // this.uploadForm = this.fb.group({
    //   title: ['', Validators.required],
    //   color: ['', Validators.required],
    //   functionality: ['', Validators.required],
    //   qbe_id: ['',Validators.required]
    // });
    if (dataa && dataa.alertId) {
      this.isUpdate = true;
    }

    // Check if data is passed for update
    
  }

  // alertForm = new UntypedFormGroup({
  //   buttonId: new UntypedFormControl(''),
  //   buttonName: new UntypedFormControl(''),
  // })

  ngOnInit(): void {
    this.uploadForm = this.fb.group({
      title: [this.dataa?.title || '', Validators.required],
      color: [this.dataa?.color || '#FFFFFF', Validators.required],
      functionality: [this.dataa?.functionality || '', Validators.required],
      qbe_id: [this.qbe_id || '',],
      report_id: [null]
    });
}


onReportChange(event: any): void {
  console.log("ON REPORT CHANGE event:", event);
  
  const selectedReportId = event?.id; 
  if (selectedReportId) {
    this.uploadForm.get('report_id')?.setValue(selectedReportId);
    console.log('Selected Report ID:', selectedReportId);
  }
}

onItemSelect(items: any[]) {
  if (!items) {
    console.error("No items selected or items is undefined.");
    return;
  }

  this.selectedItems = items;
  console.log("Selected Items:", this.selectedItems);

  if (this.selectedItems.length > 0) {
    if (this.multiple) {
      this.selectedValues = this.selectedItems.map(item => item.id);
    } else {
      this.selectedValues = [this.selectedItems[0].id];
    }

    console.log("Selected Values:", this.selectedValues);
  }
}


  // populateForm(dataa: any): void {
  //   this.uploadForm.patchValue({
  //     alertTitle: dataa.title, // Set alert name
  //     alertColor: this.dataa.color, // Set alert color
  //     alertFunctionality: this.dataa.functionality, // Set selected functionality 
  //   });
  // }

  // onFileChange(event: any): void {
  //   const reader = new FileReader();
  //   reader.onload = () => {
  //     this.zone.run(() => {
  //       this.previewUrl = reader.result as string;
  //       this.imagePreview = this.extractDataUrl(reader.result as string);
  //     });
  //   };
  //   if (event.target && event.target.files && event.target.files.length > 0) {
  //     reader.readAsDataURL(event.target.files[0]);
  //     this.existingImageData = null;
  //   }
  // }

  async onSubmit() {

    console.log("qbe_id>>>>>>>>>>>>>>>>>",this.qbe_id);
    console.log("report_id>>>>>>>>>>>>>>>>",this.report_id);
    if (this.uploadForm.valid) {
      const currentDateISO = new Date().toISOString();
    // Convert to desired format: '2024-08-28 14:30:00'
    const currentDate = currentDateISO.split('T')[0] + ' ' + currentDateISO.split('T')[1].split('.')[0];
      const formData = this.uploadForm.value;
      let json: { [key: string]: any } = {
        title: this.uploadForm.get('title')?.value, // Add alert name
        color: this.uploadForm.get('color')?.value, // Add alert color
        functionality: this.uploadForm.get('functionality')?.value, 
        qbe_id: localStorage.getItem("agGidSelectedLookup_(query)_id"),// Add functionality selection
        report_id: this.uploadForm.get('report_id')?.value,
        username:this.userId,
        creation_date:currentDate
      };
      console.log(">>>>>><<<><><><><><>",localStorage.getItem("agGidSelectedLookup_(query)_id"));

      try {
        const apiEndpoint = this.isUpdate 
          ? `${GlobalConstants.updateAlertsDataApi}${this.dataa.alertId}` 
          : GlobalConstants.insertAlertsDataApi;
        const insertOrUpdateAlertDataApi = from(axios.post(apiEndpoint, json));
        const insertOrUpdateAlertData = await lastValueFrom(insertOrUpdateAlertDataApi);
        const responseData = insertOrUpdateAlertData.data;
        console.log("Response Data:", responseData);
        this.dialogRef.close({ success: true });
      } catch (error) {
        console.error('Upload failed', error);
        this.dialogRef.close({ success: false });
      }
    }
  }
  onQueryChange(event: any) {
    console.log("ON QUERY CHANGE >>>>>>>>>>>>>>>>>>>>>>>>>>>")
    // Assuming the event carries the value we need; otherwise, adapt as needed
    const selectedId = localStorage.getItem(`agGidSelectedLookup_(${this.uploadForm.get('qbe_id')?.value})_id`);
    
    // Update the form control with the selected ID
    this.uploadForm.get('qbe_id')?.setValue(selectedId);
  
    console.log('Selected ID:', selectedId);
  }
  onCancel() {
    this.dialogRef.close();
  }

  // onReportChange(event: any): void {
  //   console.log("ON REPORT CHANGE>>>>");
  //   console.log("EVENT>>>>>>>>>>",event);
  //   // Assuming the event contains the selected report object with an 'id' property
  //   const selectedReportId = event?.id; // Adjust this based on your event structure
  //   if (selectedReportId) {
  //     this.uploadForm.get('report_id')?.setValue(selectedReportId); // Update the form control
  //     console.log('Selected Report ID:', selectedReportId); // Debug log
  //   }
  // }
  showFunctionalityJsons(){
         let functionality = this.uploadForm.controls['functionality']?.value;
         switch (functionality) {
          case 1:
          this.showFunctionality = true;
          break;
          case 2:
this.showFunctionality=false;
          break;
         }
  }
  
  onSelectionChange() {
    // Handle selection change logic here
  }
}
