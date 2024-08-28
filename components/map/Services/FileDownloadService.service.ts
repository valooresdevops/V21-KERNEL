// file-download.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class FileDownloadService {

  constructor(private http: HttpClient) { }

  downloadFile(fileName: string): void {
    const filePath = `assets/repository/${fileName}`;
    this.http.get(filePath, { responseType: 'blob' }).subscribe(blob => {
      saveAs(blob, fileName);
    }, error => {
      console.error('File download error:', error);
    });
  }
}
