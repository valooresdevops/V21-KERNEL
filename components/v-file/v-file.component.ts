import { Component, EventEmitter, forwardRef, Input, Output, Renderer2 } from '@angular/core';
import { ControlValueAccessor, UntypedFormControl, UntypedFormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';

interface HTMLInputEvent extends Event
{
  target: HTMLInputElement & EventTarget;
}

@Component
(
  {
    selector: 'v-file',
    templateUrl: './v-file.component.html',
    styleUrls: ['./v-file.component.css'],
    providers:
    [
      {
        provide: NG_VALUE_ACCESSOR,
        useExisting: forwardRef(() => VFileComponent),
        multi: true,
      },
    ]
  }
)

export class VFileComponent implements ControlValueAccessor
{
  // public value!: string;
  // public changed!: (value: string) => void;

  public value: any;
  public changed!: (value: any) => void;
  public touched!: () => void;
  public isDisabled!: boolean;
  filename :any='';
  public selectedFile!: any;

  @Input() public parentForm?: UntypedFormGroup;
  @Input() public fieldName: any;
  @Input() public required: any;
  @Input() public acceptedFileTypes: any;
  @Input() public label: any;
  @Input() public allowLabel: boolean = true;
  @Input() public isForIndisplay: boolean = false;
  @Input() public hasSuspendedStyle: boolean = false;
  @Input() public readonly: any;
  @Input() public fileClass : any = -1;
  @Input() public maxLength: number=10000000000 ; 

  @Output() fileSelected = new EventEmitter<File>();

  // Get the form field associated with the component
  get formField(): UntypedFormControl
  {
    return this.parentForm?.get(this.fieldName) as UntypedFormControl;
  }

  constructor(private renderer: Renderer2) { if(this.required){
    //alert(111111);
  }else{console.log("nooo")}
  }

  @Input() maxFileSize: number; // Pass this from the dynamic form component

  onFileSelected(event: HTMLInputEvent): void {
    const file = event.target.files[0]; // Assuming single file upload
    
    if (file) {
      const actualLength = file.size;
  
      // Dynamically set maxLength to actual file length
      this.maxLength = actualLength;
  
      // Check file size against the maximum length
      if (this.maxFileSize && file.size > this.maxFileSize) {
        // Set an error if the file size exceeds maxFileSize
        this.parentForm.get(this.fieldName).setErrors({ maxFileSizeExceeded: true });
      } else {
        // Proceed with file processing and update maxLength dynamically
        const reader = new FileReader();
        reader.onload = (e) => {
          let base64Data = e.target.result as string;
          base64Data = file.name + "," + base64Data;
  
          const totalLength = base64Data.length;
          this.maxLength = totalLength;
  
          this.filename = file.name;
          const formData = new FormData();
          formData.append(this.fieldName, file);
  
          this.changedFile({ formData, base64Data });
  
          this.parentForm.get(this.fieldName).setErrors(null);
          this.parentForm.get(this.fieldName).updateValueAndValidity();
        };
  
        reader.readAsDataURL(file);
      }
    }
  }
  

  // onFileSelected(event: HTMLInputEvent): void {
  //   const file = event.target.files[0]; // Assuming single file upload
  
  //   if (file) {
  //     // Check file size against the maximum length
  //     if (this.maxFileSize && file.size > this.maxFileSize) {
  //       // Set an error if the file size exceeds maxFileSize
  //       this.parentForm.get(this.fieldName).setErrors({ maxFileSizeExceeded: true });
  //     } else {
  //       // Proceed with file processing
  //       const reader = new FileReader();
  //       reader.onload = (e) => {
  //         let base64Data = e.target.result as string;
  //         base64Data = file.name + "," + base64Data; // Concatenate filename and base64 data
  //         const totalLength = base64Data.length;
          
  //         // Dynamically adjust maxLength to actual length
  //         this.maxLength = totalLength;
  //         this.filename = file.name;
  
  //         const formData = new FormData();
  //         formData.append(this.fieldName, file);
  
  //         // Handle file change
  //         this.changedFile({ formData, base64Data });
  
  //         // Clear errors if file is valid
  //         this.parentForm.get(this.fieldName).setErrors(null); // Clear previous errors
  //         this.parentForm.get(this.fieldName).updateValueAndValidity();
  //       };
  
  //       reader.readAsDataURL(file); // Start reading the file
  //     }
  //   }
  // }
  

  /*  public onChange(event: HTMLInputEvent): void
  {
    console.log("Change Was Done");

    const formData = new FormData();

    formData.append(this.fieldName, event.target.files[0]);

    const reader = new FileReader();

    reader.onload = (e) =>
    {
      let base64Data = e.target.result as string;
      base64Data=event.target.files[0].name+","+base64Data;
      this.changedFile({formData, base64Data});
      this.filename=event.target.files[0].name;
      console.log("FILENAME>>>>>>>>>>",this.filename);
    };

    reader.readAsDataURL(event.target.files[0]);
  } */


  // Handle changed file data
  changedFile({ formData, base64Data }: { formData: FormData, base64Data: string })
  {
    //in case file component for inDisplay return base64 encoded image else return Multipart image structure
    // Emit base64 or form data based on component type
    if(this.isForIndisplay)
    {
      this.changed(base64Data);
    }
    else
    {
     // this.changed(base64Data);
       this.changed(formData);

    }
  }
  // Write value to the file input
  public writeValue(value: string): void
  {
    //Filter browse file based on acceptedFileTypes
    // Code to handle file type filtering and filename extraction
    if (typeof (this.acceptedFileTypes) != 'undefined')
    {
      if (this.acceptedFileTypes.toLowerCase() == 'images')
      {
        this.acceptedFileTypes = 'image/*';
      }
      else
        if (this.acceptedFileTypes.toLowerCase() == 'documents')
        {
          this.acceptedFileTypes = '.xlsx,.csv,.xls,.doc,.docx,.ppt,.pptx,.txt,.pdf';
        }
        else
          if (this.acceptedFileTypes.toLowerCase() == 'all')
          {
            this.acceptedFileTypes = '.xlsx,.xls,.csv,.doc,image/*,.docx,.ppt,.pptx,.txt,.pdf';
          }
          else
          {
            this.acceptedFileTypes = "." + this.acceptedFileTypes;
          }

      console.log("FFFFFFFFF>>>>>>>>>>",this.parentForm?.get(this.fieldName).value);
      console.log("ddddddddd>>>>>>>>>>",this.parentForm?.get(this.fieldName).value);
      
      if(this.parentForm?.get(this.fieldName).value!=null)
      {
        this.filename =this.parentForm?.get(this.fieldName).value.split(",")[0];
      }
    }
  }

  // Register change function
  public registerOnChange(fn: any): void
  {
    this.changed = fn;
  }

  // Register touch function
  public registerOnTouched(fn: any): void
  {
    this.touched = fn;
  }

  // Set disabled state
  public setDisabledState(isDisabled: boolean): void
  {
    this.isDisabled = isDisabled;
  }

  // Trigger file upload
  uploadExec()
  {
    console.log("ZZZZZZZZZZZZZZZZZZZZZZZ");
    
    this.parentForm.get(this.fieldName).markAsTouched();
    $("#uploadBtn_"+this.fieldName).trigger("click");
  }


///////////////MIKE///////////////////
  // Adding an ID property
  public inputId: string = 'myInputId';

  clearInputValue()
  {    
    // Clear both filename and input value if filename is not empty
    this.filename = ''; // Clear filename
    const inputElement = this.renderer.selectRootElement('#' + this.inputId);
    this.renderer.setProperty(inputElement, 'value', ''); // Clear input value

    // Call removeFromStorage to remove the document and clear the form field value
    this.removeFromStorage();
  }


  removeFromStorage()
  {
    try
    {
      // Step 1: Retrieve Filename or Identifier
      const filenameOrIdentifier = this.parentForm?.get(this.fieldName)?.value;

      if (filenameOrIdentifier)
      {
        // Step 2: Remove File from localStorage
        const storedData = localStorage.getItem(filenameOrIdentifier);

        if (storedData)
        {
          // File exists, remove it from storage
          localStorage.removeItem(filenameOrIdentifier);
          //console.log(`Document '${filenameOrIdentifier}' removed from storage.`);
        }
        else
        {
          // File not found in storage
          // console.warn(`Document '${filenameOrIdentifier}' not found in storage.`);
        }
      }
      else
      {
        // Handle case where filename or identifier is empty or undefined
        // console.warn('Filename or identifier not provided.');
      }

      // Step 3: Clear the form field value
      this.parentForm?.get(this.fieldName)?.reset(); // Resets the form field value
      // console.log('Form field value cleared.');
    }
    catch (error)
    {
      // Handle any errors
      console.error('Error removing document from storage or clearing form field:', error);
    }
  }
  
/////////////////////////////

  // Trigger file download
  downloadExec()
  {
    // Code to handle file download
    let newElement=this.parentForm?.get(this.fieldName).value.split(",")[2];
    
    const fileType = this.parentForm?.get(this.fieldName).value.split(",")[1].split(":")[1].split(";")[0];
    const fileName =this.parentForm?.get(this.fieldName).value.split(",")[0];
    const byteCharacters = atob(newElement);
    const byteNumbers = new Array(byteCharacters.length);
    
    for (let i = 0; i < byteCharacters.length; i++)
    {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
     
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: fileType });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    
    link.href = url;
    link.download = fileName;
    link.click();

    // Release the object URL
    URL.revokeObjectURL(url);
  }
}