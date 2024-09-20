import { Component, Input } from '@angular/core';
import { OfflinedataService } from '../../Services/offlinedata.service';

@Component({
  selector: 'app-popup-name-shape',
  templateUrl: './popup-name-shape.component.html',
  styleUrls: ['./popup-name-shape.component.css']
})
export class PopupNameShapeComponent {
  @Input() Title:any;
  @Input() value!: any;
  nameshape: string = '';
  nameshapevalue: any;
  okvalue:any;
  cancelvalue:any;
  isChecked: boolean = false;
 

  constructor(private offlinedata: OfflinedataService) {}
 async ngOnInit() {

  this.nameshape="";
  //  $("#Shapename").val(this.value);

    this.nameshape=this.value;
  


   }
  saveName() {
    const checkValue = this.isChecked;
    console.log("nameshapevalue: " + this.nameshape);
    console.log("Shapename: " +$("#Shapename").val());
    this.offlinedata.setShapename($("#Shapename").val());
    console.log("Shapename: ",this.offlinedata.getShapename());
    if (this.isChecked) {
      this.offlinedata.setCheckValue(true);
    }else{
      this.offlinedata.setCheckValue(false);
      console.log("Checkbox is not checked")
    }
    this.offlinedata.okfct();
    
  }
  cancel(){    


    this.offlinedata.cancelfct();    
  }
  

}
