import { Component, EventEmitter, Input, OnInit, OnDestroy, Output, OnChanges, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { DatacrowdService } from '../../Services/datacrowd.service';
import { DataService } from '../../Services/data.service';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { InformationService } from 'src/app/Kernel/services/information.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy, OnChanges {
  form: UntypedFormGroup;
  selectedType: number;
  IMSI_IDValue: string = '';
  getAllProcAndPack: any;
  openFromKwg:boolean;
  @Input() simulationtype: any[] = [];
  @Input() IsClear: number = 0; 
  @Input() ChangeType: any ; 
  @Input() IsTypechanged: number = 0; 
  public reloadType: boolean =true;


  formKwg=new FormGroup({
    reports: new UntypedFormControl('')
  });


  @Output() onChangeEvent: EventEmitter<any> = new EventEmitter();
  public getExecutedReport = GlobalConstants.getExecutedReports1 ;
  constructor(private datacrowdService: DatacrowdService,private dataservice:DataService,private informationservice:InformationService) {
    this.form = new UntypedFormGroup({
      dateTimeFrom: new UntypedFormControl(''),
      dateTimeTo: new UntypedFormControl(''),
      Device: new UntypedFormControl(''),
      countryValue: new UntypedFormControl(''),
      MeterFrom: new UntypedFormControl(''),
      MeterTo: new UntypedFormControl(''),
      TimeLimit: new UntypedFormControl(''),
      IMSI_ID: new UntypedFormControl(''),
      GROUPS: new UntypedFormControl(''),
      TYPE: new UntypedFormControl(''),
      PHONE_NB: new UntypedFormControl('')
    });
    this.selectedType = 0;
  }

  async ngOnInit(): Promise<void> {
    this.loadFormData();
    const now = new Date();
    now.setHours(now.getHours() + 3); // Adjusting current time +3 hours
    const oneYearAgo = new Date(now);
    oneYearAgo.setFullYear(now.getFullYear() - 1);

    if (!this.form.get('dateTimeFrom')?.value) {
      this.form.get('dateTimeFrom')?.setValue(oneYearAgo.toISOString().slice(0, 16));
    }
    if (!this.form.get('dateTimeTo')?.value) {
      this.form.get('dateTimeTo')?.setValue(now.toISOString().slice(0, 16));
    }

    await this.datacrowdService.getCountryCode().then((res: any) => {
      console.log("ress", res);
      this.getAllProcAndPack = this.convertArray(res);
    });
    console.log("getAllProcAndPack", this.getAllProcAndPack);


   
      this.openFromKwg=this.dataservice.getHeaderFromKwg();
    console.log("this.openFromKwg----",this.openFromKwg);

    console.log("selectedTypeepepe",this.selectedType)
if(this.selectedType != null ){
  this.form.controls["TYPE"].setValue(this.selectedType);
setTimeout(() => {
this.reloadType = false;
}, 100);

setTimeout(() => {
this.reloadType = true;
}, 100);
}else{
  this.selectedType = 1;
  this.form.controls["TYPE"].setValue(1);
setTimeout(() => {
this.reloadType = false;
}, 100);

setTimeout(() => {
this.reloadType = true;
}, 100);
}

  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log("changes", changes);
    if (changes['IsClear']) {
     
      this.reset();
      this.form.controls['TYPE'].setValue(1);
      this.selectedType = 1;

      this.simulationtype = [
        { id: 1, name: "Activity Scan By Hits" },
        { id: 2, name: "Device History" },
        { id: 3, name: "Device Travel Pattern" },
        { id: 6, name: "Device History Pattern" },
        { id: 7, name: "POI" },
        { id: 8, name: "Fixed Element Scan" },
        { id: 9, name: "Fixed Element Activity Scan" },
        { id: 10, name: "Activity Scan By Device" },
        { id: 11, name: "TCD History" },
        { id: 13, name: "Grouping History" },
      ];
    }

    if (changes['IsTypechanged']) {
      let headerType=this.dataservice.getheaderType();
      console.log("headerType ",headerType);
      this.form.controls["TYPE"].setValue(headerType);
      this.selectedType = headerType;
setTimeout(() => {
  this.reloadType = false;
}, 100);

setTimeout(() => {
  this.reloadType = true;
}, 100);
  

    }
    console.log(" typeeeeeeeeeeeeeeeeeeeeee===",this.form.controls["TYPE"]);
  }

  ngOnDestroy(): void {
    this.saveFormData();
  }

  changeType(Type: any) {
    console.log('Type:', Type);
    this.selectedType = Type;
  }

  changeType1(type: any) {
    this.IMSI_IDValue = type;
    console.log('this.IMSI_IDValue:', this.IMSI_IDValue); // Debugging log
  }

  convertArray(input: any[]): { ID: number, NAME: string }[] {
    return input.map(item => ({
      ID: item[0],
      NAME: item[1]
    }));
  }

  setCountry(Type: any) {
    console.log('Type:', Type);
    this.selectedType = Type;
  }

  sendparam() {
    console.log("form", this.form.value);
    this.onChangeEvent.emit(this.form.value);
  }

  saveFormData(): void {
    localStorage.setItem('formData', JSON.stringify(this.form.value));
    localStorage.setItem('selectedType', JSON.stringify(this.selectedType));
  }

  loadFormData(): void {
    let  savedFormData :any= localStorage.getItem('formData');
    let savedSelectedType:any = localStorage.getItem('selectedType');
    console.log("savedFormData", savedFormData);
    console.log("savedSelectedType>>", savedSelectedType);

      this.selectedType = JSON.parse(savedSelectedType);
      console.log("this.selectedType",this.selectedType)
    // }

  }

  reset() {
    this.form.reset();
    const now = new Date();
    now.setHours(now.getHours() + 3); // Adjusting current time +3 hours
    const oneYearAgo = new Date(now);
    oneYearAgo.setFullYear(now.getFullYear() - 1);

    this.form.get('dateTimeFrom')?.setValue(oneYearAgo.toISOString().slice(0, 16));
    this.form.get('dateTimeTo')?.setValue(now.toISOString().slice(0, 16));
  }

  displaykwg(){
    const reportsValue = this.formKwg.get('reports')?.value;
    this.informationservice.setAgGidSelectedNode(reportsValue);
    $('#refreshJspGraph').click();
  }

}
