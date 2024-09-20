import { Component, ElementRef, EventEmitter, HostListener, Input, Output, ViewChild } from '@angular/core';
import { GlobalConstants } from '../../common/GlobalConstants';
import { catchError, from, lastValueFrom, map, of, switchMap } from 'rxjs';
import axios from 'axios';
import { log } from 'console';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrl: './alert.component.css'
})
export class AlertComponent {
  showDropdownMenu = false;
  dropdownPosition = { top: '0px', left: '0px' };
  constructor(private http: HttpClient) {}
  @Input() public color: any;
  @Input() public content: any;
  @Input() public title: any;
  @Input() public id: any;
  @Input() public functionality: String; // Add this input for functionality

  @Output() rightClick = new EventEmitter<{ event: MouseEvent, functionality: String }>();
 
  @ViewChild('dropdownTrigger') dropdownTrigger!: ElementRef;
  
  getGeneratedReport = GlobalConstants.getGeneratedReport;

  public buttonColor: any; 
  formattedContentParts: { beforeCount: string; count: string; afterCount: string } = { 
    beforeCount: '', 
    count: '', 
    afterCount: '' 
  };
  
  ngOnInit(): void {
    console.log('ID>>>>>>>>>>>>>', this.id);
    console.log('content>>>>>>>>>',this.content);
    console.log('color>>>>>>>>>',this.color);
    console.log('title>>>>>>>>>',this.title);
    console.log("functionlityyyy>>>>",this.functionality)
   

    this.buttonColor = this.color || '#000000'; // Default color if not provided
    
    
// Assuming `content` holds your query result string, e.g., "20 devices detected..."
const originalContent = this.content; 

// Regular expression to find the number in the text
const match = originalContent.match(/(\d+)/);

if (match) {
  // Split the content into three parts: before, count, after
  const index = match.index!;
  this.formattedContentParts.beforeCount = originalContent.slice(0, index);
  this.formattedContentParts.count = match[0];
  this.formattedContentParts.afterCount = originalContent.slice(index + match[0].length);
} else {
  // Fallback if no number is found
  this.formattedContentParts.beforeCount = originalContent;
}  }

  // adjustColorLightness(color: string, factor: number): string {
  //   let [r, g, b] = [0, 0, 0];
    
  //   // Parse the color string
  //   if (color.length === 7) {
  //     [r, g, b] = [parseInt(color.slice(1, 3), 16), parseInt(color.slice(3, 5), 16), parseInt(color.slice(5, 7), 16)];
  //   }
  
  //   // Calculate luminance (a measure of brightness)
  //   const luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
  
  //   // Adjust factor if luminance is too high
  //   const adjustedFactor = luminance > 150 ? factor / 1.5 : factor;
  
  //   // Calculate new color values
  //   r = Math.min(255, Math.floor(r * adjustedFactor));
  //   g = Math.min(255, Math.floor(g * adjustedFactor));
  //   b = Math.min(255, Math.floor(b * adjustedFactor));
  
  //   return `rgb(${r}, ${g}, ${b})`;
  // }

  // async buttonAction() {
  //   const alertId =(this.id);
  //   const api= `${GlobalConstants.getAlertsFunctionality}/${alertId}`;
  //   const resp = await lastValueFrom(from(axios.post(api)));
  //   const responseFunc = resp.data;
  //   console.log(">>>>>>>>>>>>>>>>>>>>>>>>>",responseFunc)
  //   if (!alertId) {
  //     console.error('Alert ID not found');
  //     return;
  //   }
  //   if (responseFunc === 1) {
  //     // If functionality is 1, open the report
  //     this.generateReport(alertId);
  //   } else if (responseFunc === 2) {
  //     // If functionality is 2, decode the query
  //     this.decodeQuery(alertId);
  //   } else {
  //     console.error('Unexpected functionality value');
  //   }

  //   const apiUrl = `${GlobalConstants.decodeAlertQuery}${alertId}`;
  //   const response = await lastValueFrom(from(axios.post(apiUrl)));
  //   const responseData = response.data;
  //   console.log("Response Data:", responseData);

  //   const originalContent = responseData[0]?.content || '';
  //   const match = originalContent.match(/(\d+)/);

  //   if (match) {
  //     const index = match.index!;
  //     this.formattedContentParts.beforeCount = originalContent.slice(0, index);
  //     this.formattedContentParts.count = match[0];
  //     this.formattedContentParts.afterCount = originalContent.slice(index + match[0].length);
  //   } else {
  //     this.formattedContentParts.beforeCount = originalContent;
  //   }
  // }
//   @HostListener('contextmenu', ['$event'])
//   onRightClick(event: MouseEvent) {
//     console.log("ON RIGHT CLICK>>>>>")
//     event.preventDefault(); 
//     console.log("DEFAULT PREVENTED ")
//     this.showDropdownMenu = true;
// console.log("DROP DOWN SET TO TRUE ");
//     this.dropdownPosition = {
//       top: `${event.clientY}px`,
//       left: `${event.clientX}px`
//     };
//   }

//   onDropdownChange(event: any) {
//     console.log('Dropdown selection changed:', event);
//     this.showDropdownMenu = false; // Hide dropdown after selection
//   }
//   onReportChange(event: any) {
//     // Handle dropdown change event
//     console.log('Dropdown selection changed:', event);
//   }
// @Output() rightClick = new EventEmitter<MouseEvent>();

// onRightClick(event: MouseEvent) {
//   event.preventDefault();
//   // this.rightClick.emit(event); // Emit event to parent component
// }
onRightClick(event: MouseEvent) {
  event.preventDefault();
  console.log("Emitting right-click event with functionality:>>>>>", this.functionality);
  this.rightClick.emit({ event, functionality: this.functionality });
}

  async buttonAction() {
    const alertId = this.id;
    const api = `${GlobalConstants.getAlertsFunctionality}/${alertId}`;
    const resp = await lastValueFrom(from(axios.post(api)));
    const responseFunc = resp.data;

    console.log("Functionality Response:", responseFunc);

    if (!alertId) {
      console.error('Alert ID not found');
      return;
    }

    // Decide which action to perform based on functionality
    switch (responseFunc) {
      case 1:
          this.generateReport();
          break;
      case 2:
          this.decodeQuery(alertId);
          break;
      default:
          console.error('Unsupported functionality value');
  }
}

generateReport() {
    console.log("Fetching report...");
    this.http.post(GlobalConstants.LatestReports, Headers, { responseType: 'text' }).pipe(
      map((htmlContent: string) => {
        console.log("HTML content received:", htmlContent);

        // Create a Blob from the HTML content
        let blob = new Blob([htmlContent], { type: 'text/html' });

        // Create a link element and trigger the download
        var link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.target = '_blank';
        link.click();

        // Clean up the URL object
        window.URL.revokeObjectURL(link.href);
      })
    ).subscribe();
}

// fetchLatestReportId() {
//   console.log("Fetching latest report ID...");
//   return this.http.get<any[]>(GlobalConstants.LatestReports).pipe(
//     map(response => {
//       console.log("Response from LatestReports:", response);
//       // Ensure response is correctly parsed and contains the report ID
//       if (Array.isArray(response) && response.length > 0) {
//         return response[0].id; // Extract the ID from the latest report
//       } else {
//         throw new Error("No latest report found.");
//       }
//     }),
//     catchError(error => {
//       console.error("Error fetching latest report ID:", error);
//       return of(null); // or EMPTY if you do not want further handling
//     })
//   );
// }


// const headers = new HttpHeaders({
//   'Content-Type': 'application/json', // Adjust this if necessary
//   // Add any other necessary headers
// });

// this.http.post(`${GlobalConstants.getGeneratedReportById}/${selectedReportId}`, null, { headers: headers, responseType: 'text' }).pipe(
//   map((htmlContent: string) => {
//     console.log("HTML content received:", htmlContent);

//     // Create a Blob from the HTML content
//     let blob = new Blob([htmlContent], { type: 'text/html' });

//     // Create a link element and trigger the download
//     const link = document.createElement('a');
//     link.href = window.URL.createObjectURL(blob);
//     link.target = '_blank';
//     link.click();

//     // Clean up the URL object
//     window.URL.revokeObjectURL(link.href);
//   }),
//   catchError((error: any) => {
//     console.error('Error fetching report:', error);
//     // Optionally, display a user-friendly message or handle error further
//     return of(null); // or EMPTY if you do not want further handling
//   })
// ).subscribe();
// }

// generateReport() {
//   console.log("Fetching latest report ID...");
//   this.fetchLatestReportId().pipe(
//     switchMap((id: number) => {
//       if (id === null) {
//         throw new Error("No valid ID found.");
//       }
//       console.log("Fetching report for ID:", id);
//       return this.http.post(GlobalConstants.getGeneratedReportBlob + "/" + id, null, { responseType: 'text' });
//     }),
//     map((htmlContent: string) => {
//       console.log("HTML content received:", htmlContent);

//       // Create a Blob from the HTML content
//       let blob = new Blob([htmlContent], { type: 'text/html' });

//       // Create a link element and trigger the download
//       const link = document.createElement('a');
//       link.href = window.URL.createObjectURL(blob);
//       link.target = '_blank';
//       link.click();

//       // Clean up the URL object
//       window.URL.revokeObjectURL(link.href);
//     }),
//     catchError(error => {
//       console.error("Error fetching report:", error);
//       return of(null); // or EMPTY if you do not want further handling
//     })
//   ).subscribe();
// }





decodeQuery(id: number) {
    const apiUrl = `${GlobalConstants.decodeAlertQuery}${id}`;
    this.http.post(apiUrl,Headers).pipe(
      map((responseData: any) => {
        console.log("Decoded Query Data:", responseData);
        // Handle your decoded data here
      })
    ).subscribe();
}

}
