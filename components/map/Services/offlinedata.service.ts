import { Injectable, EventEmitter } from '@angular/core';    
import { Subscription } from 'rxjs/internal/Subscription';   
import { BehaviorSubject ,Subject} from 'rxjs';
import { delay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class OfflinedataService {
  private shapename:any;
  cancel = new EventEmitter();  
  ok = new EventEmitter();    
  okk = false;
  public _checked: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public  checked$ = this._checked.asObservable().pipe(delay(1)); 
    subsVar!: Subscription; 
  CheckValue: boolean = false;

  private okButtonClickSource = new Subject<void>();

  // Observable for the OK button click event
  okButtonClick$ = this.okButtonClickSource.asObservable();
  constructor() { }

    setShapename(shapename:any){
    this.shapename=shapename;
    console.log("shapename111111111",shapename);
    }

    getShapename(){
      return this.shapename;

    }
    cancelfct() {    
      this.cancel.emit();    
    }    

    okfct() {    
      this.ok.emit();
      // this.okButtonClickSource.next();
    } 

    setCheckValue(CheckValue:any){
      this.CheckValue=CheckValue;
      console.log("CheckValue111111111",CheckValue);
      }
      
    getCheckValue(){
      return this.CheckValue;
    }
 
}