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
