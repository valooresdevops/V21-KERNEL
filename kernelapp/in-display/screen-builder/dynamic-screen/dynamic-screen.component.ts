import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';

@Component({
  selector: 'app-dynamic-screen',
  templateUrl: './dynamic-screen.component.html',
  styleUrls: ['./dynamic-screen.component.css']
})
export class DynamicScreenComponent implements OnInit {

  public menuVariable: any;

  public objectId:any;

  constructor(private _Activatedroute: ActivatedRoute,
    private http: HttpClient) { }

  ngOnInit(): void {
    this._Activatedroute.paramMap.subscribe((params:any) => {
      this.menuVariable = params.get('menuVariable');
    });
    console.log("menu variable>>>>>>>>>",this.menuVariable);

    const numericPattern = /^[0-9]+$/;

    // Check if myString consists only of numeric characters
    if (numericPattern.test(this.menuVariable)) {
      console.log('String contains only numeric characters');
    } else {
      console.log('String does not contain only numeric characters');
    }


    this.http.get<any>(GlobalConstants.getScreenPreviewData+this.menuVariable, { headers: GlobalConstants.headers }).subscribe(
      (res: any) => { 
    });    
  }

}
