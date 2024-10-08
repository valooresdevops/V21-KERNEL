import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormArray, UntypedFormBuilder, FormControl, UntypedFormGroup, Validators } from '@angular/forms';
import axios from 'axios';
import { get } from 'jquery';
import { Observable, Subscription, from, lastValueFrom } from 'rxjs';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { CommonFunctions } from '../../common/CommonFunctions';
import { InformationService } from '../../services/information.service';
import { Router } from '@angular/router';

@Component({
  selector: 'v-dynamic-search',
  templateUrl: './v-dynamic-search.component.html',
  styleUrls: ['./v-dynamic-search.component.css']
})
export class VDynamicSearchComponent implements OnInit {

  public clearAll:boolean=false;
   public getSearchType :any = '';
   public selectedValue: any; 
   public query :any;
   public getDynamicSearch :any = [];
   public fieldsCombo: any[] = [];
   public firstCombo: any[] = [];
   public thirdCombo: any[] = [];
    public fieldsComboStoredData:any[]=[];
   public formElem: any[] = [];
   public isDropdownStatus: boolean[] = [];
   public isDate: boolean[] = [];
   public isNumber: boolean[] = [];
   public isText: boolean[] = [];
   public jsonEmpty: any[] = [];
   public isDropdown = false;
   public formElemNum: number = 0;
   public dynamicSearchForm = new UntypedFormGroup({});
   public getWhereCond:any = '-1';
  //  public checkIfFIeldExists:any = 'exists';
  public getDynamicSearchMainDropDown:any='';
  public allDynamicSearchData:any[]=[];
  public specialSearchData:any[]=[];
   @Input() public objectId: any;
   @Input() public sourceQuery: any;
   @Input() public isForForm: any;
  public showDynamicSearch:any=true;
   @Output() public onSearchSubmit: EventEmitter<any> = new EventEmitter();
  rowData: any;
  gridOptions: any;
  searchService: any;
  public searchButtonId:any;
   
  constructor(private formBuilder: UntypedFormBuilder,
     private http: HttpClient,private commonFunctions: CommonFunctions,
     public informationservice: InformationService,
     private cdr: ChangeDetectorRef,
     private router: Router,
    ) {}

  async ngOnInit(): Promise<void> {
    this.searchButtonId=this.objectId+"_"+this.isForForm;
    //this.getDynamicSearchMainDropDown=GlobalConstants.getDynamicSearchMainDropDown+this.objectId;
if(this.sourceQuery == null){

  this.sourceQuery = "0";

}else{
 
  this.sourceQuery = "1";

}
    console.log("IS FOR FORM>>>>>>>>>>>>>>>>",this.isForForm);
    const getDynamicSearchMainDropDownApi = from(axios.get(GlobalConstants.getDynamicSearchMainDropDown + this.objectId+"/"+this.isForForm));
    const getDynamicSearchMainDropDown = await lastValueFrom(getDynamicSearchMainDropDownApi);

    this.allDynamicSearchData=getDynamicSearchMainDropDown.data;
    console.log("getDynamicSearchMainDropDown.data>>>>>>>",getDynamicSearchMainDropDown.data);

    // const getDynamicSearchUrl = from(axios.get(GlobalConstants.getDynamicSearch + this.objectId +"/"+ this.sourceQuery ));
    // const getDynamicSearch = await lastValueFrom(getDynamicSearchUrl);
    const getSearchTypeUrl = from(axios.get(GlobalConstants.getSearchType));
    const getSearchType = await lastValueFrom(getSearchTypeUrl);

    this.dynamicSearchForm = this.formBuilder.group({
      conditions: this.formBuilder.array([]),
      dynamicDropdownControl: [null],
      textControl: [null, Validators.required],
    });
    
    for (let i = 0; i < getDynamicSearchMainDropDown.data.length; i++) {
      this.fieldsCombo.push({
        id: getDynamicSearchMainDropDown.data[i].id,
        name: getDynamicSearchMainDropDown.data[i].name,
        colType: getDynamicSearchMainDropDown.data[i].colType,
        cmbSQL: getDynamicSearchMainDropDown.data[i].cmbSQL,
        isMandatory: getDynamicSearchMainDropDown.data[i].isMandatory,
        isDefault: getDynamicSearchMainDropDown.data[i].isDefault,
        defaultValues: getDynamicSearchMainDropDown.data[i].defaultValues        
      });
      console.log("getDynamicSearchMainDropDown.data>>>>>>>",getDynamicSearchMainDropDown.data);
      this.isDropdownStatus.push(false);
      this.isDate.push(false);
      console.log("11111111111111111111>>>>>>>>>>>>>>>>>>",this.dynamicSearchForm);
      if(getDynamicSearchMainDropDown.data[i].isMandatory=="1"){
        this.specialSearchData.push({
        id: getDynamicSearchMainDropDown.data[i].id,
        name: getDynamicSearchMainDropDown.data[i].name,
        colType: getDynamicSearchMainDropDown.data[i].colType,
        cmbSQL: getDynamicSearchMainDropDown.data[i].cmbSQL,
        isMandatory: getDynamicSearchMainDropDown.data[i].isMandatory,
        isDefault: getDynamicSearchMainDropDown.data[i].isDefault,
        defaultValues: getDynamicSearchMainDropDown.data[i].defaultValues   
        })

      }
    }

    for(let i = 0; i < getSearchType.data.length; i ++) {
      this.firstCombo.push({
        id: getSearchType.data[i].id, 
        name: getSearchType.data[i].name
      });
    }
    console.log("SPECIAL SEARCH DATA>>>>>>>>>>>>>>>>>>>>>",this.specialSearchData);
    setTimeout(() => {
      if(this.specialSearchData.length==0){
        console.log("NO GOOD>");
        this.addCondition();
      }else{
        for(let i=0;i<this.specialSearchData.length;i++){
          this.addConditionOnLoad(this.specialSearchData[i]);
        }
      }

      // if(getDynamicSearchMainDropDown.data[i].isMandatory=="1"){
      // this.dynamicSearchForm.controls[this.formElem[0].typeDropdown].setValue(getDynamicSearchMainDropDown.data[i].id);
      // }


    }, 500);

  }

  addCondition() {
   
    const rowIndex = this.formElemNum;
    const typeDropdownKey = "typeDropdown_" + rowIndex;
    const searchTypeKey = "searchType_" + rowIndex;
    const textKey = "text_" + rowIndex;
    const thirdDropdownKey = "thirdDropdown_" + rowIndex;
    const secondDropdownKey = "secondDropdown_" + rowIndex;

    const newConditionGroup = this.formBuilder.group({
      [typeDropdownKey]: [''],
      [searchTypeKey]: [[]],
      [textKey]: [''],
      [secondDropdownKey]: [[]],
      [thirdDropdownKey]: [[]]
    });

    this.dynamicSearchForm.addControl(typeDropdownKey, newConditionGroup.controls[typeDropdownKey]);
    this.dynamicSearchForm.addControl(searchTypeKey, newConditionGroup.controls[searchTypeKey]);
    this.dynamicSearchForm.addControl(textKey, newConditionGroup.controls[textKey]);
    this.dynamicSearchForm.addControl(thirdDropdownKey, newConditionGroup.controls[thirdDropdownKey]);
  
    
    this.formElem.push({
      typeDropdown: typeDropdownKey,
      searchType: searchTypeKey,
      text: textKey,
      thirdDropdown: thirdDropdownKey,
      secondDropdown: secondDropdownKey,
      thirdDropdownOptions: [] ,
      secondDropdownOptions: []
    });

    this.dynamicSearchForm.get(typeDropdownKey)?.valueChanges.subscribe(selectedValue => {
      this.onDropdownChange(selectedValue, this.formElem[rowIndex], thirdDropdownKey, rowIndex);
    });

    this.dynamicSearchForm.get(typeDropdownKey)?.valueChanges.subscribe(selectedValue => {
      this.onDropdownChange(selectedValue, this.formElem[rowIndex], secondDropdownKey, rowIndex);
    });
  
    this.formElemNum++;
  }

  addConditionOnLoad(loadedSearchData:any) {
    console.log("SPECIAL SEARCH DATA>>>>>>>>>",loadedSearchData);

    const rowIndex = this.formElemNum;
    const typeDropdownKey = "typeDropdown_" + rowIndex;
    const searchTypeKey = "searchType_" + rowIndex;
    const textKey = "text_" + rowIndex;
    const thirdDropdownKey = "thirdDropdown_" + rowIndex;
    const secondDropdownKey = "secondDropdown_" + rowIndex;

    const newConditionGroup = this.formBuilder.group({
      [typeDropdownKey]: [''],
      [searchTypeKey]: [[]],
      [textKey]: [''],
      [secondDropdownKey]: [[]],
      [thirdDropdownKey]: [[]]
    });


    this.dynamicSearchForm.addControl(typeDropdownKey, newConditionGroup.controls[typeDropdownKey]);
    this.dynamicSearchForm.addControl(searchTypeKey, newConditionGroup.controls[searchTypeKey]);
    this.dynamicSearchForm.addControl(textKey, newConditionGroup.controls[textKey]);
    this.dynamicSearchForm.addControl(thirdDropdownKey, newConditionGroup.controls[thirdDropdownKey]);

    //this.dynamicSearchForm.controls[typeDropdownKey].setValue(loadedSearchData.id);


    this.formElem.push({
      typeDropdown: typeDropdownKey,
      searchType: searchTypeKey,
      text: textKey,
      thirdDropdown: thirdDropdownKey,
      secondDropdown: secondDropdownKey,
      thirdDropdownOptions: [] ,
      secondDropdownOptions: [],
      isMandatory:loadedSearchData.isMandatory
    });

    this.dynamicSearchForm.get(typeDropdownKey)?.valueChanges.subscribe(selectedValue => {
      this.onDropdownChange(selectedValue, this.formElem[rowIndex], thirdDropdownKey, rowIndex);
    });

    this.dynamicSearchForm.get(typeDropdownKey)?.valueChanges.subscribe(selectedValue => {
      this.onDropdownChange(selectedValue, this.formElem[rowIndex], secondDropdownKey, rowIndex);
    });
    console.log("FORM ELEM<<<<<<<<<<<<",this.formElem);
    this.dynamicSearchForm.controls[typeDropdownKey].setValue(loadedSearchData.id);
    this.dynamicSearchForm.controls[searchTypeKey].setValue('1');

  console.log("this.fieldCombo>>>>>>>>>>>",this.fieldsCombo);
  console.log("typeDropdownKey>>>>>>>>>>>>",typeDropdownKey);
  
  console.log("this.dynamicSearchForm BEFORE>>>>>>>>>>>>>>>>>>",this.dynamicSearchForm);

   //this.dynamicSearchForm.controls[searchTypeKey].setValue('2');

  console.log("FETET 11111");
  setTimeout(() => {
    if(loadedSearchData.isDefault=="1" && loadedSearchData.colType!="Combo" && loadedSearchData.colType!="COMBO"){
      console.log("FETET 22222");
       this.dynamicSearchForm.controls[textKey].setValue(loadedSearchData.defaultValues);
    }
  }, 200);

  setTimeout(() => {
   // this.cdr.detectChanges();

      this.dynamicSearchForm.markAsDirty();
      this.dynamicSearchForm.updateValueAndValidity();
  }, 300);
  // this.registerTouchedField(typeDropdownKey);

    this.formElemNum++;
  }

  registerTouchedField(fieldName: string) {
    const control = this.dynamicSearchForm.get(fieldName);
    if (control) {
      control.markAsTouched();
    }
  }
  async onDropdownChange(selectedValue: any, fields: any, DropDownChange: any, index: number): Promise<void> {
    console.log("selectedValue>>>>>>>>>>>",selectedValue);
    console.log("fields>>>>>>>>>>>",fields);
    console.log("DropDownChange>>>>>>>>>>>",DropDownChange);
    console.log("index>>>>>>>>>>>",index);
    console.log("FIELDSSSS COMBOOO>>>>>>>>>>>>>>>",this.fieldsCombo);
    fields.selectedValue = selectedValue;
    let keyArray = Object.keys(this.fieldsCombo);
  
    this.dynamicSearchForm.get(fields.searchType)?.setValue(null);
    this.dynamicSearchForm.get(fields.text)?.setValue(null);
    
    let fieldIndexSelected=null;
    for(let i=0;i<this.fieldsCombo.length;i++){
      if(selectedValue==this.fieldsCombo[i].id){
        fieldIndexSelected=i;
        break;
      }
    }

    this.query = "";
    console.log("this.fieldsCombo[index]",this.fieldsCombo[index]);
    if (this.fieldsCombo[fieldIndexSelected]) {
      
      for (let i = 0; i < keyArray.length; i++) {
        console.log("this.fieldsCombo[i].colType>>>>>>>>>>>>>>>>",this.fieldsCombo[fieldIndexSelected].colType);

        if (selectedValue == this.fieldsCombo[fieldIndexSelected].id) {

          if (this.fieldsCombo[fieldIndexSelected].colType == 'Combo' || this.fieldsCombo[fieldIndexSelected].colType == 'COMBO') {

            this.isDropdownStatus[index] = true;
            this.isDate[index] = false;
            this.isNumber[index] = false;
            this.isText[index] = false;
            const selectedValues: string[] = ['1', '2'];
            this.thirdCombo = []; 

            this.formElem[index].secondDropdownOptions = this.firstCombo.filter(item => selectedValues.includes(item.id));
            this.dynamicSearchForm.get(DropDownChange)?.setValue(null);


            const getComboQueryDataApi = from(axios.get(GlobalConstants.getComboQueryData+this.fieldsCombo[fieldIndexSelected].cmbSQL));
            const getComboQueryData = await lastValueFrom(getComboQueryDataApi);   
              for(let y = 0; y <getComboQueryData.data.length; y++) {

              this.thirdCombo.push({
                id: getComboQueryData.data[y].id, 
                name: getComboQueryData.data[y].name
              });

            // const getThirdDropDownUrl = from(axios.post( GlobalConstants.getThirdDropDown + this.fieldsCombo[i].id +"/"+localStorage.getItem('LogeduserId'))  );
            // const getThirdDropDown = await lastValueFrom(getThirdDropDownUrl);   
            //   for(let y = 0; y <getThirdDropDown.data.length; y ++) {

            //   this.thirdCombo.push({
            //     id: getThirdDropDown.data[y][0], 
            //     name: getThirdDropDown.data[y][1]
            //   });
           
          }

        fields.thirdDropdownOptions=this.thirdCombo ;

          console.log("111111111111111111111111111111111111");
          } else if (this.fieldsCombo[fieldIndexSelected].colType == 'DATE' || this.fieldsCombo[fieldIndexSelected].colType == 'Date') {
            console.log("22222222222222222222222222222222222222");

              this.isDate[index] = true;
              this.isDropdownStatus[index] = false;
              this.isNumber[index] = false;
              this.isText[index] = false;
  
              const selectedValues: string[] = ['1', '2', '3', '4', '5','8'];

              this.formElem[index].secondDropdownOptions = this.firstCombo.filter(item => selectedValues.includes(item.id));
              this.dynamicSearchForm.get(DropDownChange)?.setValue(null);
            console.log("SECOND DROPDOWN optoins for date>>>>>>>>",this.formElem[index].secondDropdownOptions);
              // this.formElem[index].secondDropdownOptions = this.firstCombo.filter(item => selectedValues.includes(item.id));;
              // this.dynamicSearchForm.get(DropDownChange)?.setValue(null);

          } else if (this.fieldsCombo[fieldIndexSelected].colType == 'Text' || this.fieldsCombo[fieldIndexSelected].colType == 'TEXT' ) {
              this.isDate[index] = false;
              this.isDropdownStatus[index] = false;
              this.isNumber[index] = false;
              this.isText[index] = true;
              const selectedValues: string[] = ['7', '8', '1', '2'];
              this.formElem[index].secondDropdownOptions = this.firstCombo.filter(item => selectedValues.includes(item.id ));;
              this.dynamicSearchForm.get(DropDownChange)?.setValue(null);

  
          } else if (this.fieldsCombo[fieldIndexSelected].colType == 'NUMBER' || this.fieldsCombo[fieldIndexSelected].colType == 'Number') {
              this.isNumber[index] = true;
              this.isDate[index] = false;
              this.isDropdownStatus[index] = false;
              this.isText[index] = false;
  
              const selectedValues: string[] = ['1', '2', '5', '6', '3', '4','8'];

              this.formElem[index].secondDropdownOptions = this.firstCombo.filter(item => selectedValues.includes(item.id));;
              this.dynamicSearchForm.get(DropDownChange)?.setValue(null);
            }
        }
      }
    }
    console.log("THIS DYNAMIC FORM ON CHANGE>>>>>>>>>>>",this.dynamicSearchForm);
  }

  getRowNumber(){
    return this.formElem.length -1 ;
  }

  removeCondition(index: number) {
    const removedRow = this.formElem[index];
    console.log("Condtion to REmove>>>>>>>>>>>>",this.formElem[index]);
    if(removedRow.isMandatory!='1'){
    this.formElem.splice(index, 1);
    this.isDropdownStatus.splice(index, 1);
    // this.isDate.splice(index, 1);
    this.dynamicSearchForm.removeControl(removedRow.typeDropdown);
    this.dynamicSearchForm.removeControl(removedRow.searchType);
    this.dynamicSearchForm.removeControl(removedRow.text);
    this.dynamicSearchForm.removeControl(removedRow.secondDropdown);
    this.dynamicSearchForm.removeControl(removedRow.thirdDropdown);
  }
}

  
  async onSubmit() {

    let keyArray = Object.keys(this.fieldsCombo);
    for (let i = 0; i < keyArray.length; i++) {
    }
    let MyList = [];
    let elementSize = 0 ;
    let obj:any;

    console.log("FORM ELEM>>>>>>>>>>>>>",this.formElem);
    for (let i = 0; i < this.formElem.length; i++) {

      let searchType = this.dynamicSearchForm.controls[this.formElem[i].searchType].value;
      let text       = this.dynamicSearchForm.controls[this.formElem[i].text]?.value;
      let dropdown   = this.dynamicSearchForm.controls[this.formElem[i].typeDropdown]?.value;
      let dropdown2  = this.dynamicSearchForm.controls[this.formElem[i].secondDropdown]?.value;
      let dropdown3  = this.dynamicSearchForm.controls[this.formElem[i].thirdDropdown]?.value;
      let sourceQuery = this.sourceQuery ;
      console.log("searchType>>>>>>>>>",searchType);
      console.log("text>>>>>>>>>",text);
      console.log("dropdown>>>>>>>>>",dropdown);
      console.log("dropdown2>>>>>>>>>",dropdown2);
      console.log("dropdown3>>>>>>>>>",dropdown3);
      console.log("sourceQuery>>>>>>>>>",sourceQuery);
     // console.log("searchType>>>>>>>>>",searchType);

      let item1 ;

     
      if( dropdown3 != null && text == null){
          item1 = {
          "searchType": searchType,
          "text": '',
          "dropdown": dropdown,
          "dropdown2": dropdown2,
          "dropdown3": dropdown3,
          "objectId": this.objectId,
          "sourceQuery": sourceQuery,
        
      }

      }else{
 
        item1 = {
          "searchType": searchType,
          "text": text,
          "dropdown": dropdown,
          "dropdown2": dropdown2,
          "dropdown3": 0,
          "objectId": this.objectId,
          "sourceQuery": sourceQuery
      }
      }
  
      MyList.push(item1);
    }
    
    console.log("MY LIST>>>>>>>>>>>>>",MyList);

    const buildSearchJsonForApiApi = from(axios.post( GlobalConstants.buildSearchJsonForApi,MyList)  );
    const buildSearchJsonForApi = await lastValueFrom(buildSearchJsonForApiApi);
    
    console.log("buildSearchJsonForApi DATA>>>>>>>>>>>>>>>>>>",buildSearchJsonForApi.data);
    this.informationservice.setDynamicSearchApiData(buildSearchJsonForApi.data);
    if(this.formElemNum <=1 ){
      elementSize = 0;
    }else{
      elementSize = 1;
    }
    const getWhereCondUrl = from(axios.post( GlobalConstants.getWhereCondition + "/" +  elementSize   ,  MyList  )  );
    let getWhereCond1 = await lastValueFrom(getWhereCondUrl);
    let result: any;
    let getWhereCondNew: any = "-1";
  
    if (getWhereCond1 !== undefined) {
      this.getWhereCond = getWhereCond1.data;
      getWhereCondNew = this.getWhereCond;
    }
  
  console.log("getWhereCondNew ::: ",getWhereCondNew);
    let functionName: string | any;
    let condition: string | any;
  
    if (getWhereCondNew.indexOf('Function Name:') !== -1 && getWhereCondNew.indexOf('Condition:') !== -1) {
      const functionNameMatch = getWhereCondNew.match(/Function Name:\s*([^,]*)/);
      const conditionMatch = getWhereCondNew.match(/Condition:\s*(.*)/);
  
      if (functionNameMatch && functionNameMatch[1]) {
        functionName = functionNameMatch[1].trim();
      }
  
      if (conditionMatch && conditionMatch[1]) {
        condition = conditionMatch[1].trim();
      }
    }
  
    if (condition && condition.indexOf(' ') !== -1) {
      result = await this.transformCondition(condition);
    }
    if (functionName) {
      const callApi =  from(axios.post( GlobalConstants.callApi +functionName ,result )  );  
      let apiReturn =await lastValueFrom(callApi);      
       obj = {
        result :"",
        reloadGrid : true
      }
    }else{
      obj = {
        result :result,
        reloadGrid : false
      }
    }
    const checkIfAdvancedSearchHasFunctionApi = from(axios.get(GlobalConstants.checkIfAdvancedSearchHasFunction+this.objectId));
    const checkIfAdvancedSearchHasFunction = await lastValueFrom(checkIfAdvancedSearchHasFunctionApi);
    console.log("checkIfAdvancedSearchHasFunction>>>>>>>>",checkIfAdvancedSearchHasFunction.data);
    
    if(checkIfAdvancedSearchHasFunction.data!=0){
      $("#searchFunctionButton_"+this.objectId)[0].click();

      // setTimeout(() => {
      //   $("#searchFunctionButton_"+this.objectId)[0].click();

      // }, 1500);
      
    }
    this.informationservice.setAdvancedSearchShowGridMain(true);
    this.onSearchSubmit.emit(obj);
  }







  filterData(data: any[], searchCriteria: any[]): any[] {

    return data.filter(item => {
      return searchCriteria.every(criteria => {
         if (criteria.dropdown && item[criteria.searchType] !== criteria.dropdown) {
          return false;
        }
        if (criteria.text && !item[criteria.searchType].includes(criteria.text)) {
          return false;
        }  
        return true;
      });
    });
  }


  transformCondition(getWhereCondNew: any) {
    console.log("getWhereCondNew ::: ::: ::: ", getWhereCondNew);
    // Split the string by the logical operator 'and'
    let conditions = getWhereCondNew.split(" and ");

    // Function to process each individual condition
    const processCondition = (condition: any) => {
        if (condition.includes(' = ')) {
            const parts = condition.split(' = ');
            const fieldName = `"${parts[0].trim()}"`;
            const fieldValue = parts[1].trim();
            return `${fieldName} = ${fieldValue}`;
        } else if (condition.includes(' != ')) {
            const parts = condition.split(' != ');
            const fieldName = `"${parts[0].trim()}"`;
            const fieldValue = parts[1].trim();
            return `${fieldName} != ${fieldValue}`;
        } else if (condition.includes(' not like ')) {
            const parts = condition.split(' not like ');
            let fieldName = parts[0].trim();
            const modifiedFieldName = `upper("${fieldName.substring(fieldName.indexOf('(') + 1, fieldName.lastIndexOf(')'))}")`;
            const modifiedValue = parts[1].trim();
            return `${modifiedFieldName} not like ${modifiedValue}`;
        } else if (condition.includes(' like ')) {
            const parts = condition.split(' like ');
            let fieldName = parts[0].trim();
            const modifiedFieldName = `upper("${fieldName.substring(fieldName.indexOf('(') + 1, fieldName.lastIndexOf(')'))}")`;
            const modifiedValue = parts[1].trim();
            return `${modifiedFieldName} like ${modifiedValue}`;
        } else if (condition.includes(' < ')) {
            const parts = condition.split(' < ');
            const fieldName = `"${parts[0].trim()}"`;
            const fieldValue = parts[1].trim();
            return `${fieldName} < ${fieldValue}`;
        } else if (condition.includes(' > ')) {
            const parts = condition.split(' > ');
            const fieldName = `"${parts[0].trim()}"`;
            const fieldValue = parts[1].trim();
            return `${fieldName} > ${fieldValue}`;
        } else if (condition.includes(' <= ')) {
            const parts = condition.split(' <= ');
            const fieldName = `"${parts[0].trim()}"`;
            const fieldValue = parts[1].trim();
            return `${fieldName} <= ${fieldValue}`;
        } else if (condition.includes(' >= ')) {
            const parts = condition.split(' >= ');
            const fieldName = `"${parts[0].trim()}"`;
            const fieldValue = parts[1].trim();
            return `${fieldName} >= ${fieldValue}`;
        }
        return condition; // return the condition as is if no known operator is found
    };

    // Process each condition and join them back with 'and'
    let transformedConditions = conditions.map(processCondition);
    return transformedConditions.join(' and ');
}
  onReset(){

    this.dynamicSearchForm.reset();
    console.log("this router url>>>>>>>>>>>>",this.router.url);
    console.log("isForForm dynamic search>>>>>>>>>>>",this.isForForm);

    if(this.isForForm){
        this.commonFunctions.reloadPage(this.router.url);
    }
    //this.commonFunctions.reloadPage(this.router.url);

    setTimeout(() => {
      this.clearAll=true;
    }, 20);

    setTimeout(() => {
      this.clearAll=false;
    }, 50);
  
   let obj = {
      result :"",
      reloadGrid : true
    }
    this.onSearchSubmit.emit(obj);
    
  }



  
}