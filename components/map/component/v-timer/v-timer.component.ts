import { Component, EventEmitter, Input, OnInit, Output,DoCheck,OnChanges, SimpleChange  } from '@angular/core';
 
  @Component({
    selector: 'app-v-timer',
    templateUrl: './v-timer.component.html',
    styleUrls: ['./v-timer.component.css']
  })
  export class VTimerComponent implements OnInit {
   @Input() timelineItems:{label:string,action:()=>void}[]=[];
   @Output() sentSpeed=new EventEmitter<number>();
   speedTime=1;

    constructor() {}

    ngOnInit(): void {
    }

  handleClick(action:()=>void){
    if(action){
        action();
    }
  }
changebar(){

    this.sentSpeed.emit(this.speedTime);
}

  } 


