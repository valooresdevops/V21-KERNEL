// file-download.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { url } from 'inspector';
import { DatacrowdService } from './datacrowd.service';

@Injectable({
  providedIn: 'root'
})
export class FileDownloadService {

  constructor(private http: HttpClient,private datacrowdService:DatacrowdService) { }

  downloadFile(fileName: string): void {
    const filePath = `../../repository/${fileName}`;


    this.http.get(filePath, { responseType: 'blob' }).subscribe(blob => {
      saveAs(blob, fileName);
    }, error => {
      console.error('File download error:', error);
    });


  }
  downloadFile2(fileName: string,SimulID:any): void {
    this.datacrowdService.GetHtmlContent(SimulID).then((blob:any) => {
      console.log("blob",blob);
      const blob2 = new Blob([blob], { type: 'text/html;charset=utf-8' });

        saveAs(blob, `${fileName}.html`);
      }, error => {
        console.error('File download error:', error);
      });

  }
  }
