import { Component, EventEmitter, Input, OnInit, OnDestroy, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { DatacrowdService } from '../../Services/datacrowd.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {
  form: FormGroup;
  selectedType: number;
  IMSI_IDValue: string = '';
  getAllProcAndPack: any;
  @Input() simulationtype: any[] = [];

  @Output() onChangeEvent: EventEmitter<any> = new EventEmitter();

  constructor(private datacrowdService: DatacrowdService) {
    this.form = new FormGroup({
      dateTimeFrom: new FormControl(''),
      dateTimeTo: new FormControl(''),
      Device: new FormControl(''),
      countryValue: new FormControl(''),
      MeterFrom: new FormControl(''),
      MeterTo: new FormControl(''),
      TimeLimit: new FormControl(''),
      IMSI_ID: new FormControl(''),
      GROUPS: new FormControl(''),
      TYPE: new FormControl('')
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
    const savedFormData = localStorage.getItem('formData');
    const savedSelectedType = localStorage.getItem('selectedType');

    if (savedFormData) {
      this.form.setValue(JSON.parse(savedFormData));
    }

    if (savedSelectedType) {
      this.selectedType = JSON.parse(savedSelectedType);
    }
  }

  reset(){
    this.form.reset();
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
    // this.form.get('MeterFrom')?.setValue(200);
  }
}
