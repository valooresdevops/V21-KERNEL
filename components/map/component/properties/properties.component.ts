import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { DatacrowdService } from '../../Services/datacrowd.service';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {
  @Input() showForm: boolean;
  @Input() SimulID: any;

  myGroup: FormGroup;
  rowdata:any;
  columndefs:any;
  Data: any;

  constructor(public datacrowdService:DatacrowdService) { }

  async ngOnInit(): Promise<void> {
 console.log("propertiesssssssssssssssssss");
 console.log("jsonData>>>>>",this.Data);
// let jsonArr:any=[["Hits"],["user"],["  Name"],["Countries"],["TimeFrame (with time)"],["Previous Simulation"]]
// let jsonArr: any = ["Hits", "user", "Simulation Name"];


// let jsonArr:any=[
//   {"FieldsetName":"Simulation",
//    "is_grid":false,
//    "fields":[{"Name":"number_of_devices",
//               "Data":2
//         },
//         {"Name":"number_of_hits",
//           "Data":4
//         },
//         {"Name":"number_of_days ",
//           "Data":2
//         },
// {"Name":"start_date ",
//           "Data":"null"
//         },
// {"Name":"end_date ",
//           "Data":"null"
//         },
// {"Name":"execution_date ",
//           "Data":"2024-07-05"
//         },
// {"Name":"number_of_shapes ",
//           "Data":"1"
//         },
// {"Name":"number_of_countries ",
//           "Data":"1"
//         },
// {"Name":"created_by ",
//           "Data":"Souk wakef"
//         },
// {"Name":"number_of_cities ",
//           "Data":"1"
//         },
// {"Name":"list_of_countries ",
//           "Data":"Lebanon"
//         },
// {"Name":"list_of_cities ",
//           "Data":"Baabda"
//         }
//   ]
 
//   },
//   {
//   "FieldsetName":"Device Classification",
//    "is_grid":true,
//    "Columns":['device_id,number_of_hits,number_of_days,first_seen,last_seen,number_of_countries,number_of_cities,list_of_countries,list_of_cities'],
//     "Data":[["3CD0A7B8-B53A-4DF3-A10A-9EB9DF6D7F0C",2,1,1696712400000,1696712400000,1,1,"LB","Baabda"],["a5f497f7-5479-44c7-ade1-77a939555b1b",2,1,1696885200000,1696885200000,1,1,"LB","Baabda"]]
 
 
//   }
//   ];
// this.Data=jsonArr;  
//     console.log("jsonData:", this.Data);
// for(let i=0;i<this.Data.length;i++){
//   if(this.Data[i].is_grid==true){
    
//    this.columndefs= this.generateColumns(this.Data[i].Columns);
//    console.log("columndefs:", this.columndefs);

//    this.rowdata= this.generateRowData(this.Data[i].Columns,this.Data[i].Data);
//    console.log("rowdata:", this.rowdata);

//   }
//   else{
//     this.myGroup = new FormGroup({});
//     this.Data[i].fields.forEach((name:any) => {
//       console.log("name---------",name);
//       this.myGroup.addControl(name.Name, new FormControl(name.Data));
//       console.log("this.myGroup---------",this.myGroup);

//     });
//   }
// }
  
    // let formGroupObj:any = {};
    // this.Data.forEach((name:any) => {
    //   formGroupObj[name] = new FormControl('');
    // console.log("11111111111111111---",name );

    // });
    // this.myGroup = new FormGroup(formGroupObj);
    // console.log("2222222222----",this.myGroup);

//  this.columndefs=this.generateColumns()
     
  }

  async ngOnChanges(changes: any) {
    console.log("changes:",changes.SimulID.currentValue);

  await  this.datacrowdService.getPropertiesObj(changes.SimulID.currentValue).then((res:any)=>{
// res=[ 
// //   {
// //   "FieldsetName" : "Device Information",
// //   "is_grid" : true,
// //   "fields" : [ {
// //     "Name" : "Co-travelers",
// //     "Data" : "{\"data\":[{\"DEVICE2\":\"74ac8e6f-b443-45a5-ab9e-41cb93c6603c\",\"MONTH\":\"10\",\"COMMON_HITS\":1,\"YEAR\":\"2023\",\"DEVICE1\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\"},{\"DEVICE2\":\"819be618-1ed9-47a5-a9c7-07228f46c7ee\",\"MONTH\":\"10\",\"COMMON_HITS\":1,\"YEAR\":\"2023\",\"DEVICE1\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\"},{\"DEVICE2\":\"98db4e3f-c881-42d2-ac5e-89bc51b3301b\",\"MONTH\":\"10\",\"COMMON_HITS\":1,\"YEAR\":\"2023\",\"DEVICE1\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\"},{\"DEVICE2\":\"e0dc7c9b-73bf-4174-a09a-90a7cb7bc889\",\"MONTH\":\"10\",\"COMMON_HITS\":1,\"YEAR\":\"2023\",\"DEVICE1\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\"},{\"DEVICE2\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\",\"MONTH\":\"10\",\"COMMON_HITS\":1,\"YEAR\":\"2023\",\"DEVICE1\":\"819be618-1ed9-47a5-a9c7-07228f46c7ee\"},{\"DEVICE2\":\"cb511c35-c463-4e55-a84f-8b2557da9aad\",\"MONTH\":\"10\",\"COMMON_HITS\":9,\"YEAR\":\"2023\",\"DEVICE1\":\"bc299b12-b831-4e22-a48f-4c7229ad5dda\"},{\"DEVICE2\":\"dedbfbe2-29fb-4a92-adc2-cea9299590d2\",\"MONTH\":\"10\",\"COMMON_HITS\":2,\"YEAR\":\"2023\",\"DEVICE1\":\"bc299b12-b831-4e22-a48f-4c7229ad5dda\"},{\"DEVICE2\":\"bc299b12-b831-4e22-a48f-4c7229ad5dda\",\"MONTH\":\"10\",\"COMMON_HITS\":9,\"YEAR\":\"2023\",\"DEVICE1\":\"cb511c35-c463-4e55-a84f-8b2557da9aad\"}],\"columns\":[\"Device1\",\"Device2\",\"Year\",\"Month\",\"Common Hits\"]}"
// //   }, {
// //     "Name" : "Device Statistics",
// //     "Data" : "Error deserializing data: expected zero arguments for construction of ClassDict (for pandas._libs.tslibs.timestamps._unpickle_timestamp). This happens when an unsupported/unregistered class is being unpickled that requires construction arguments. Fix it by registering a custom IObjectConstructor for this class."
// //   }, {
// //     "Name" : "Significant AOIs",
// //     "Data" : "{\"data\":[{\"SIGNIFICANCE_LEVEL\":\"low\",\"HITS\":10,\"PERCENTAGE_VISITS_PER_GRID\":26.923076923076923,\"WEEKDAY\":3,\"VISITS\":7,\"TIME_SPENT\":37243,\"GRID\":\"10.62863, -4.74533\",\"HOME_HOURS\":2,\"WEEKEND\":0,\"PERCENTAGE_OVERNIGHT_STAY\":14.285714285714285,\"OVERNIGHT_STAY\":1,\"DEVICE_ID\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\",\"WORK_HOURS\":0,\"UNKNOWN_HOURS\":3},{\"SIGNIFICANCE_LEVEL\":\"low\",\"HITS\":3,\"PERCENTAGE_VISITS_PER_GRID\":7.692307692307692,\"WEEKDAY\":1,\"VISITS\":2,\"TIME_SPENT\":29819,\"GRID\":\"10.63044, -4.74443\",\"HOME_HOURS\":2,\"WEEKEND\":0,\"PERCENTAGE_OVERNIGHT_STAY\":0.0,\"OVERNIGHT_STAY\":0,\"DEVICE_ID\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\",\"WORK_HOURS\":0,\"UNKNOWN_HOURS\":0},{\"SIGNIFICANCE_LEVEL\":\"low\",\"HITS\":7,\"PERCENTAGE_VISITS_PER_GRID\":23.076923076923077,\"WEEKDAY\":1,\"VISITS\":6,\"TIME_SPENT\":30843,\"GRID\":\"10.63225, -4.74892\",\"HOME_HOURS\":2,\"WEEKEND\":2,\"PERCENTAGE_OVERNIGHT_STAY\":0.0,\"OVERNIGHT_STAY\":0,\"DEVICE_ID\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\",\"WORK_HOURS\":0,\"UNKNOWN_HOURS\":2},{\"SIGNIFICANCE_LEVEL\":\"low\",\"HITS\":37,\"PERCENTAGE_VISITS_PER_GRID\":62.5,\"WEEKDAY\":4,\"VISITS\":15,\"TIME_SPENT\":358270,\"GRID\":\"10.62682, -4.74713\",\"HOME_HOURS\":9,\"WEEKEND\":2,\"PERCENTAGE_OVERNIGHT_STAY\":6.666666666666667,\"OVERNIGHT_STAY\":1,\"DEVICE_ID\":\"bc299b12-b831-4e22-a48f-4c7229ad5dda\",\"WORK_HOURS\":0,\"UNKNOWN_HOURS\":1},{\"SIGNIFICANCE_LEVEL\":\"low\",\"HITS\":11,\"PERCENTAGE_VISITS_PER_GRID\":60.0,\"WEEKDAY\":3,\"VISITS\":6,\"TIME_SPENT\":179712,\"GRID\":\"10.62682, -4.74713\",\"HOME_HOURS\":5,\"WEEKEND\":1,\"PERCENTAGE_OVERNIGHT_STAY\":0.0,\"OVERNIGHT_STAY\":0,\"DEVICE_ID\":\"cb511c35-c463-4e55-a84f-8b2557da9aad\",\"WORK_HOURS\":0,\"UNKNOWN_HOURS\":0}],\"columns\":[\"Device ID\",\"Grid\",\"Overnight Stay\",\"Time Spent\",\"Hits\",\"Visits\",\"Weekday\",\"Weekend\",\"Work Hours\",\"Home Hours\",\"Unknown Hours\",\"Percentage Overnight Stay\",\"Percentage Visits Per Grid\",\"Significance Level\"]}"
// //   } ]
// // } ,
//  {
//   "FieldsetName" : "Simulation Information",
//   "is_grid" : false,
//   "fields" : [ {
//     "Name" : "Cities",
//     "Data" : "\"Banfora\""
//   }, {
//     "Name" : "Countries",
//     "Data" : "\"Burkina Faso\""
//   }, {
//     "Name" : "End Date",
//     "Data" : "1723582800000"
//   }, {
//     "Name" : "Execution Date",
//     "Data" : "1723582800000"
//   }, {
//     "Name" : "Number of Cities",
//     "Data" : "1"
//   }, {
//     "Name" : "Number of Countries",
//     "Data" : "1"
//   }, {
//     "Name" : "Number of Devices",
//     "Data" : "4"
//   }, {
//     "Name" : "Number of Hits",
//     "Data" : "13"
//   }, {
//     "Name" : "Recorded Days",
//     "Data" : "4"
//   }, {
//     "Name" : "Scanned Areas",
//     "Data" : "1"
//   }, {
//     "Name" : "Simulation ID",
//     "Data" : "173195"
//   }, {
//     "Name" : "Simulation Name",
//     "Data" : "\"No Name\""
//   }, {
//     "Name" : "Simulation Type",
//     "Data" : "\"Activity Scan By Hits\""
//   }, {
//     "Name" : "Start Date",
//     "Data" : "1691960400000"
//   } ]
// },
// //  {
// //   "FieldsetName" : "Simulation Information",
// //   "is_grid" : true,
// //   "fields" : [ {
// //     "Name" : "Cross Country Devices",
// //     "Data" : "{\"data\":[{\"DEVICE_ID\":\"495ce394-9ea5-49d2-a5b9-09774f83b9ee\",\"COUNTRY_NAME\":\"Cote Ivoire, Burkina Faso\"}],\"columns\":[\"Device ID\",\"Country Name\"]}"
// //   }, {
// //     "Name" : "First Seen Devices",
// //     "Data" : "Error deserializing data: expected zero arguments for construction of ClassDict (for pandas._libs.tslibs.timestamps._unpickle_timestamp). This happens when an unsupported/unregistered class is being unpickled that requires construction arguments. Fix it by registering a custom IObjectConstructor for this class."
// //   }, {
// //     "Name" : "Last Seen Devices",
// //     "Data" : "Error deserializing data: expected zero arguments for construction of ClassDict (for pandas._libs.tslibs.timestamps._unpickle_timestamp). This happens when an unsupported/unregistered class is being unpickled that requires construction arguments. Fix it by registering a custom IObjectConstructor for this class."
// //   } ]
// // } ,
// //  {
// //   "FieldsetName" : "User Information",
// //   "is_grid" : false,
// //   "fields" : [ {
// //     "Name" : "Number of Primary Cases",
// //     "Data" : "106"
// //   }, {
// //     "Name" : "Number of Related Cases",
// //     "Data" : "127"
// //   }, {
// //     "Name" : "Number of Suspicious Areas",
// //     "Data" : "0"
// //   }, {
// //     "Name" : "User ID",
// //     "Data" : "8158"
// //   }, {
// //     "Name" : "User Name",
// //     "Data" : "\"ymtmtb\""
// //   } ]
// // }, 
// // {
// //   "FieldsetName" : "User Information",
// //   "is_grid" : true,
// //   "fields" : [ {
// //     "Name" : "Countries Classification",
// //     "Data" : "{\"data\":[{\"SUSPICIOUS_TO\":null,\"SUS_SCORE\":100,\"CASE_ID\":null,\"SUSPICIOUS_FROM\":null,\"COUNTRY_NAME\":\"Burkina Faso\",\"SIMULATION_ID\":null,\"USER_PROFILE_ID\":0,\"COUNTRY_CODE\":854},{\"SUSPICIOUS_TO\":null,\"SUS_SCORE\":25,\"CASE_ID\":null,\"SUSPICIOUS_FROM\":null,\"COUNTRY_NAME\":\"Cote Ivoire\",\"SIMULATION_ID\":null,\"USER_PROFILE_ID\":0,\"COUNTRY_CODE\":384}],\"columns\":[\"Country Code\",\"User Profile ID\",\"Case ID\",\"Country Name\",\"Simulation ID\",\"Sus Score\",\"Suspicious From\",\"Suspicious To\"]}"
// //   } ]
// // } 
// ]


       this.Data=res;
  
      console.log("res>>>>>",res);
      console.log("jsonData:", this.Data);
      for(let i=0;i<this.Data.length;i++){
        if(this.Data[i].is_grid==true){
          
         this.columndefs= this.generateColumns(this.Data[i].Columns);
         console.log("columndefs:", this.columndefs);
      
         this.rowdata= this.generateRowData(this.Data[i].Columns,this.Data[i].Data);
         console.log("rowdata:", this.rowdata);
      
        }
        else{
          this.myGroup = new FormGroup({});
          this.Data[i].fields.forEach((name:any) => {
            console.log("name---------",name);
            this.myGroup.addControl(name.Name, new FormControl(name.Data));
            console.log("this.myGroup---------",this.myGroup);
      
          });
        }
      }
    })

  }

  receiveData(){

  }
  generateColumns(csv: any) {
    console.log("generateColumns>>>>", csv);
    return csv.toString().split(',').map((header:any) => ({
      headerName: header,
      field: header,
      sortable: true,
      filter: "agSetColumnFilter",
      autoSizeColumns: true,
      padding: 0,
      sort: 'desc'
    }));
  }

  generateRowData(csvString: any, data: any[]) {
    console.log("csvString",csvString);
    console.log("data",data);

    let rowdata:any=[];
    const keys = csvString.toString().split(',');

    data.forEach((item) => {
      let jsonaggrid:any = {};
      keys.forEach((key:any, index:any) => {
        jsonaggrid[key] = item[index];
      });
      rowdata.push(jsonaggrid);
    });
    return rowdata;
  }
}
