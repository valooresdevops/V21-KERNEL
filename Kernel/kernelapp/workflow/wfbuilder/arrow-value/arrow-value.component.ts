import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-arrow-value',
  templateUrl: './arrow-value.component.html',
  styleUrls: ['./arrow-value.component.css']
})
export class ArrowValueComponent implements OnInit {
  public values = [
    { id: 1, name: 'true' }, 
    { id: 0, name: 'false' },
  ];

  arrowValue = new FormGroup({
    valueTrueFalse: new FormControl(''),
  });

  constructor(@Inject(MAT_DIALOG_DATA) 
              public data: any, 
              public dialogRef: MatDialogRef<ArrowValueComponent>,) {}

  ngOnInit(): void {
    //taking the true or false value from the past component
    //converting it to the value to assign and place in the dropdown 
    if(this.data.value == 'True'){
      this.arrowValue.controls['valueTrueFalse'].setValue('1');
    }else if(this.data.value == 'False'){
      this.arrowValue.controls['valueTrueFalse'].setValue('0');
    }
  }

  //close without saving anything
  closeDialog(){
    this.dialogRef.close();
  }

  save(): void{
    //if there is no selection OR it's false meaning 0, save
    //sending back a 1 for true, a 0 for false OR an empty value to empty the text value of the arrow
    if(this.arrowValue.get('valueTrueFalse')?.value != undefined && this.arrowValue.get('valueTrueFalse')?.value != '' || this.arrowValue.get('valueTrueFalse')?.value == '0'){
      this.data.value = this.arrowValue.get('valueTrueFalse')?.value;
      this.dialogRef.close(this.data);
    }else{
      this.data.value = '';
      this.dialogRef.close(this.data);
    }
  }
}
