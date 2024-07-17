import { Component, EventEmitter, Input, OnInit, Output,DoCheck,OnChanges, SimpleChange  } from '@angular/core';
  import { ActivatedRoute, Router } from "@angular/router";
  import { ChangeDetectorRef } from '@angular/core';
  import { map } from 'rxjs/operators';
  import { NgZone } from '@angular/core';
  @Component({
    selector: 'app-v-table',
    templateUrl: './v-table.component.html',
    styleUrls: ['./v-table.component.css']
  })
  export class VTableComponent implements OnInit {
    @Input() tableColumns: string[]=[] ;
    @Input() tablerow: Number ;
    @Input()tableData: any[] =[];
    @Input() typeofdata: any ;
    @Input()routeDevices: any[] =[];
    @Output() clickedDeviceId = new EventEmitter<{ row: any, type: string }>(); // Assuming typeofdata is a string

    @Output() clickedRow = new EventEmitter<{row:any}>();
    isDropdownOpen :boolean= false;

    config: any= {
      currentPage: 1,
      itemsPerPage: 10
    };
    filteredData: any[] = this.tableData;
    searchTerm: string = '';
    sortColumn: string = '';
    sortDirection: string = 'desc';

    pageSize!: number; // Number of items per page
    currentPage: number = 1; // Current page number
    totalPages: number = 0; // Total number of pages

    dataSource: any;
    searchText = '';
    constructor(private route: ActivatedRoute, private router: Router,private cdr: ChangeDetectorRef,
      private ngZone: NgZone) {
    

    }



    ngOnInit(): void {
  
      this.config = {
        currentPage: 1,
        itemsPerPage: 10
      };
    
    //  this.tableData.unshift(this.tablerow);
    

    for(let i=0;i<this.tableData.length;i++){
      // this.firstpage();
      this.filteredData.unshift(this.tableData[i]);
      //console.log("this.tableData[i]---------",this.tableData[i]);
    }
      // this.filteredData=this.tableData;
      // //console.log("tablerow111---------",this.tablerow);
      //console.log("tableData111111---------",this.tableData);
      // Update pagination when the tableData changes
      this.pageSize = this.tableData.length;
      this.updatePagination();
    }

      ngOnChanges(changes: any) {
      // if (changes.tableData && changes.tableData.currentValue) {
        // Update pagination when the tableData changes
    // this.pageSize= this.config.totalItems = this.tableData.length;
      //console.log("this.pageSize--------",this.pageSize)
      // console.log("changes.tableData-----------",changes.tableData);
      // this.tableData=changes.tableData.currentValue;
      this.updatePagination();
      // }
    }

  

    updatePagination() {
      const startIndex = (this.currentPage - 1) * this.config.itemsPerPage;
      let endIndex = startIndex + this.config.itemsPerPage;


      
      if (endIndex > this.tableData.length) {
        endIndex = this.tableData.length;
      }
    
      this.filteredData = this.tableData.slice(startIndex, endIndex);
      this.totalPages = Math.ceil(this.tableData.length / this.config.itemsPerPage);
      //console.log("endIndex--------",endIndex);
      //console.log("endIndex--------",endIndex);
     }
    

    applySearch() {
  //  this.tableData.pop();
  //  //console.log("tableData----------",this.tableData);
      this.filteredData = this.tableData.filter((item) => {
        for (const column of this.tableColumns) {
          //console.log("search--------",this.searchTerm.toLowerCase());
          //console.log("item[column].toString()--------",item[column].toString());
          if (item[column].toString().toLowerCase().includes(this.searchTerm.toLowerCase())) {
          

            return true;
          }
        }
        return false;
      });
      if(this.searchTerm !=''){
        this.totalPages = Math.ceil(this.filteredData.length / this.pageSize);
      }
    }




    applySort(columnName: string) {
      if (columnName === this.sortColumn) {
        this.sortDirection = this.sortDirection === 'desc' ? 'asc' : 'desc';
      } else {
        this.sortColumn = columnName;
        this.sortDirection = 'desc';
      }

      this.filteredData = this.filteredData.slice().sort((a, b) => {
        const direction = this.sortDirection === 'desc' ? 1 : -1;
        return (a[this.sortColumn] < b[this.sortColumn] ? -1 : 1) * direction;
      });
    }

    rowClicked(row:any){
      //console.log('row',row);
      this.clickedRow.emit({row});

    }
    pageChange(newPage: any) {
      //console.log("newpageeee----------",newPage);
      this.currentPage=newPage;
          // this.router.navigate([""], { queryParams: { page: newPage } });
          
      
          this.updatePagination();
        }
    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      return this.dataSource.filter = filterValue.trim().toLowerCase();
    }

    firstpage(){
  
      //console.log("newpageeee----------",this.currentPage);
      this.currentPage=1;
          // this.router.navigate([""], { queryParams: { page: this.currentPage } });
      
          this.updatePagination();
    }

    lastpage(){
      this.currentPage=(this.tableData.length)/10;
      this.currentPage= Math.ceil(this.currentPage);

          // this.router.navigate([""], { queryParams: { page: this.currentPage } });
      
          this.updatePagination();
    }

    previouspage(){
      this.currentPage=this.currentPage-1;
      //console.log("newpageeee----------",this.currentPage);

          // this.router.navigate([""], { queryParams: { page: this.currentPage } });
      
          this.updatePagination();
    }


    nextpage() {
      this.currentPage=this.currentPage+1;
      //console.log("newpageeee----------",this.currentPage);

          // this.router.navigate([""], { queryParams: { page: this.currentPage } });
      
          this.updatePagination();
        }
        toggleDropdown() {
          this.isDropdownOpen = !this.isDropdownOpen;
        }

        DeviceIdclicked(row:any){
          
          this.clickedDeviceId.emit({row, type: this.typeofdata});
          this.isDropdownOpen=false;
        }

        getColumnStyle(column: string) {
          // Example condition: style column 'Age' differently
          if (column === 'Lng'|| column === 'lat') {
           return { 'display': 'none' };
         }
         return {};
       }
  
  } 


