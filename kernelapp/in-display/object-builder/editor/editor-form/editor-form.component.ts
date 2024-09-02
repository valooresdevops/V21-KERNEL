import { Component, Inject } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import axios from 'axios';
import { from } from 'rxjs';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
@Component({
  selector: 'app-editor-form',
  templateUrl: './editor-form.component.html',
  styleUrls: ['./editor-form.component.css']
})
export class EditorFormComponent {
  public editor = ClassicEditor;
  public content: string = '';
  public userId : String; 
  public title : String;
  public nodeId : String;  

  form = new UntypedFormGroup({
    title: new UntypedFormControl(''),
  });

  constructor(
    public informationService: InformationService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<EditorFormComponent>,
    public commonFunctions : CommonFunctions,
  ) {
    // Check if the action type is not 'add'
    if (this.informationService.getactionType() !== 'add' ) {
      this.content = data?.content || '';
      this.title = data?.title || ''; // Initialize the title with the passed data

      // Set the title in the form control
      this.form.controls['title'].setValue(this.title);
    }
  }


  //on update do : exp : form.component.ts
  //    this.form.controls["title"].setValue(res[0].title);

  public editorConfig = {
    toolbar: [
      'heading',
      '|',
      'bold',
      'italic',
      // 'link',
      'bulletedList',
      'numberedList',
      '|',
      // 'imageUpload',
      'blockQuote',
      'insertTable',
      // 'mediaEmbed',
      'undo',
      'redo'
    ],
    // image: {
    //   toolbar: [
    //     'imageTextAlternative',
    //     'imageStyle:alignLeft',
    //     'imageStyle:alignCenter',
    //     'imageStyle:alignRight',
    //     'imageStyle:side',
    //     'linkImage'
    //   ]
    // },
    table: {
      contentToolbar: [
        'tableColumn',
        'tableRow',
        'mergeTableCells'
      ]
    },
    language: 'en'
  };

  public onSave() {

    this.userId = localStorage.getItem("logeduserId");
    // const localDate: Date = new Date();
    // const year: number = localDate.getFullYear();
    // const month: string = String(localDate.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    // const day: string = String(localDate.getDate()).padStart(2, '0');
    
    // const hours: string = String(localDate.getHours()).padStart(2, '0');
    // const minutes: string = String(localDate.getMinutes()).padStart(2, '0');
    // const seconds: string = String(localDate.getSeconds()).padStart(2, '0');
    
    // const formattedDate: string = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    // console.log("formattedDate>>>>>", formattedDate);
    
    
    this.title = this.form.controls['title']?.value;
    console.log("title>>>>>",this.title);
    console.log('HTML Content:', this.content);



    this.content = this.content.replace(/<br\s*\/?>/gi, '').trim();
    console.log("Cleaned Content:", this.content);

    if(this.informationService.getactionType() == 'add'){
      let jsonData={  
        userId:this.userId,
        content:this.content,
        title : this.title
        // date: formattedDate
      }
      console.log("ALL FORM DATA>>>>>>",jsonData);
    from(axios.post(GlobalConstants.insertHtmlElementDataApi, jsonData,{}));

  }
  else if(this.informationService.getactionType()=='update'){
    this.nodeId = this.informationService.getAgGidSelectedNode();
    let jsonData={  
      userId:this.userId,
      content:this.content,
      title : this.title,
      nodeId : this.nodeId
      // date: formattedDate
    }
    console.log("ALL FORM DATA>>>>>>",jsonData);
      from(axios.post(GlobalConstants.updateHtmlElementData, jsonData,{}));
      this.commonFunctions.reloadPage('/dsp/editor');
    }
    this.dialogRef.close();
  }
}
