import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import axios from 'axios';
import { from, lastValueFrom } from 'rxjs';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';

@Component({
  selector: 'app-alert-preview',
  templateUrl: './alert-preview.component.html',
  styleUrl: './alert-preview.component.css'
})
export class AlertPreviewComponent implements OnInit{
  buttonColor: string ;
  lighterBackgroundColor: string = '#f5f5f5';
  result: any = null;
  queryResults: any;
  constructor( public dialogRef: MatDialogRef<AlertPreviewComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any){
   
      this.buttonColor = this.data?.color || this.buttonColor;
    this.lighterBackgroundColor = this.adjustColorLightness(this.buttonColor, 1.5); // Adjust lightness
  }
  
    ngOnInit(): void {
      this.buttonColor = this.data?.color || '#000000'; // Default color if not provided
      console.log('Button Color>>>>>>>>>>>>>>', this.buttonColor);
      this.lighterBackgroundColor = this.adjustColorLightness(this.buttonColor, 2);
      this.executeQuery(); // Execute query on dialog open
  }

  

  adjustColorLightness(color: string, factor: number): string {
    let [r, g, b] = [0, 0, 0];
    
    // Parse the color string
    if (color.length === 7) {
      [r, g, b] = [parseInt(color.slice(1, 3), 16), parseInt(color.slice(3, 5), 16), parseInt(color.slice(5, 7), 16)];
    }
  
    // Calculate luminance (a measure of brightness)
    const luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
  
    // Adjust factor if luminance is too high
    const adjustedFactor = luminance > 150 ? factor / 1.5 : factor;
  
    // Calculate new color values
    r = Math.min(255, Math.floor(r * adjustedFactor));
    g = Math.min(255, Math.floor(g * adjustedFactor));
    b = Math.min(255, Math.floor(b * adjustedFactor));
  
    return `rgb(${r}, ${g}, ${b})`;
  }
  

  async executeQuery() {
    const Id=this.data.alertId
    const apiUrl = `${GlobalConstants.decodeAlertQuery}${Id}`;

    const insertOrUpdateAlertDataApi = from(axios.post(apiUrl));
    const insertOrUpdateAlertData = await lastValueFrom(insertOrUpdateAlertDataApi);
    const responseData = insertOrUpdateAlertData.data;
    console.log("Response Data:", responseData);
    this.result=responseData;
    console.log("jason>>>>>>>>>>",this.result[0].content);
  }
  onClose(): void {
    this.dialogRef.close();
  }



}
