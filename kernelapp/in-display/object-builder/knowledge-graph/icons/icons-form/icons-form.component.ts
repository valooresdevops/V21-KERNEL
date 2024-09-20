import { Component, OnInit, Inject, Input } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
import axios from 'axios';
import { VFileComponent } from 'src/app/Kernel/components/v-file/v-file.component';
import { from, lastValueFrom } from 'rxjs';
import { NgZone } from '@angular/core';


@Component({
  selector: 'app-icons-form',
  templateUrl: './icons-form.component.html',
  styleUrls: ['./icons-form.component.css']
})

export class IconsFormComponent implements OnInit {
  @Input() existingImageData: string;
  public imagePreview: string = '';
public previewUrl: string = '';
public imageUrl : string = '';
 
  uploadForm=new UntypedFormGroup({
    imageName: new UntypedFormControl('', Validators.required),
    imageByteStream:new UntypedFormControl(''),
  });
  isUpdate: boolean = false;
  // zone: any;
  // existingImageData: string;
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<IconsFormComponent>,
     @Inject(MAT_DIALOG_DATA) public dataa: any,
    private http: HttpClient,
    public informationService: InformationService,
    private zone: NgZone
  ) {
    this.uploadForm.get('imageByteStream')?.valueChanges.subscribe(value => {
      console.log('File changed, new value:', value);
      this.imageUrl = value;
      // Handle the new value (base64 or z`form data) here
    });
  }
  

  ngOnInit(): void {
    
    if (this.dataa) {
      this.isUpdate = true;
      this.populateFormWithData(this.dataa);
    }
    if (this.existingImageData) {
      this.previewUrl = this.extractDataUrl(this.existingImageData);
    }
    // Conditionally add validators based on whether it's an update or insert
    if (!this.isUpdate) {
      this.uploadForm.get('imageByteStream')?.setValidators(Validators.required);
    }
    // Update validity after changing validators
    this.uploadForm.get('imageByteStream')?.updateValueAndValidity();
  }


  extractDataUrl(fullString: string): string {
    const startIndex = fullString.indexOf('data:image');
    if (startIndex !== -1) {
      return fullString.substring(startIndex);
    }
    return '';
  }
  populateFormWithData(data: any): void {
    this.uploadForm.patchValue({
      imageName: data.imageName,
      imageByteStream: '' // Start with an empty string; this field will be populated only if a new image is selected
    });
    this.existingImageData = data.imageByteStream; // Store existing image data
  }

  onFileChange(event: any): void { 
    console.log("<<<<<<<File input changed>>>>>>");

    // Use FileReader to handle file input change
    const reader = new FileReader();
    reader.onload = () => {
      this.zone.run(() => {
        this.previewUrl = reader.result as string;
        this.imagePreview = this.extractDataUrl(reader.result as string);
      });
    };

    // Check if the event has files and read the first one
    if (event.target && event.target.files && event.target.files.length > 0) {
      reader.readAsDataURL(event.target.files[0]);

      // Clear existing image data
      this.existingImageData = null;
    }
  }
  async onSubmit() {
  if (this.uploadForm.valid) {
    const formData = this.uploadForm.value;
    console.log("JJJJJJJJJJJJJJJJJJ>>>>>>>>>>>>>>>",this.uploadForm.get('imageByteStream').value.toString());

let json: { [key: string]: any } = {
  imageName: this.uploadForm.get('imageName')?.value,
};

// Include imageData only if it's present
const imageData = this.uploadForm.get('imageByteStream')?.value;
if (imageData) {
  json['imageData'] = imageData.toString();
} else if (this.isUpdate && this.existingImageData) {
  json['imageData'] = this.existingImageData; // Reuse existing image data
}
try {
  const apiEndpoint = this.isUpdate ? `${GlobalConstants.updateIconData}${this.dataa.iconId}` : GlobalConstants.insertIconData;
        const insertOrUpdateIconDataApi = from(axios.post(apiEndpoint, json));
        const insertOrUpdateIconData = await lastValueFrom(insertOrUpdateIconDataApi);
        const responseData = insertOrUpdateIconData.data;
        console.log("Response Data:", responseData);
  // const responseData = insertIconData.data;
  // console.log( "responseData>>>>>>>>>>>>>>",responseData );

  // Close the form dialog on successful submission
  this.dialogRef.close({ success: true });
  

} catch (error) {
  console.error('Upload failed', error);
  this.dialogRef.close({ success: false });
}
  }
}
  onCancel() {
    this.dialogRef.close();
  }
}
