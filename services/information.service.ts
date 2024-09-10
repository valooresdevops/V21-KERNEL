import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InformationService {
  public checkExisting: String = '';
  public openLikeForm: String = '';
  public formToOpen: String = '';
  public ROW_ID: any;
  public mainJsonForDataWhenSkip: string = '';
  public statusOfCheckExisting: String = '';
  public selectedTabId: string = '';
  public tabToBeSelectedOnValidate: String = '';
  public selectedTabName: string = '';
  public previousTab: String = '';
  [key: string]: any;
  public previousMainTab: string = '';
  public mainTab: string = '';
  public logeduserId: string = '';
  public twiceClose: String = '';
  public userRoleId: string = '';
  public agGidSelectedNode: string = '';
  public agGidSelectedCell: string = '';
  public popupBreadcrumb: any;
  public previousMainTab2: string = '';
  public previousTabIdSelected: string = '';
  public UserId: String = '';
  public MyId: String = '';
  public agGidSelectedNodeRule: string = '';
  public menuPath: any;
  public agGridSelectedNode: String = '';
  public userRoleName: String = '';
  public RoleId: String = '';
  public clickedItemDetails: string = '';
  public isFromMain: string = '';
  public logSerial: string = '';
  public FirstOpenGrid: string = '';
  public isManaged: string = '';
  public DataToSet: string = '';
  public clickedTab: string = '';
  public tabPath: string = '';
  public isGrid: string = '';
  public dynamicRuleBuilderReloadUrl: string = '';
  public accessRights: string = '';
  public choosenTab: string = '';
  public listOfData : any;
  public dynamicReportId: string = '';
  public Skip : string = '';
  public reopen : string = '';
  public isDynamicReport:boolean=false;
  public sameSession : string = '' ; 
  public selectDay : string = '';
  public selectMonths : any = '';
  public customScreenBuilderPath : string = '';
  public lookUpSubmitValue : any = '';
  public ISExport : boolean =false;
  public isRowGroup : any = '';
  public selectedChart : any = '';
  public navBreadCrumb : any[] =[];
  public vcisrowData : any ;
  public checkParentMenu : number=0;
  public checkParentMenuFirst : String = "";
  public bubbleButton: any;
  public selectedGridNode : any;
  public advancedSearchFunctionData:any='';
  public dashboardContentSize :number;
  public dynamicSearchApiData :any;
  private actionType:any;
  public agGridNodeData : Object;
  public reloadGrid : boolean = true;
  public selectedNodeVGrid : object;
  public selectedColumnFormOpening : any;
  public selectedColumnValue : boolean = false;
  public mainPreviewScreenType : boolean = false;
  public advancedSearchShowGrid : boolean = false;
  public advancedSearchShowGridMain:boolean=true;
  public isDisplayAll:boolean=false;
  public breakLiveDataBar: any;
  public breakLiveDataLine: any;
  public breakLiveDataArea: any;
  public breakLiveDataColumn: any;
  public formRecentData : any[] =[];
  public isFromMainPreviewForm: any;

  constructor() { }

  private bubbleMenusSubject = new BehaviorSubject<any[]>([]);
  bubbleMenus$ = this.bubbleMenusSubject.asObservable();

  setBubbleMenus(menus: any[]) {
    this.bubbleMenusSubject.next(menus);
  }
  //authentication still has a localstorage
  setLogeduserId(logeduserIdVal: string) {
    this.logeduserId = logeduserIdVal;
  }
  
  getBubbleButton(){
    return this.bubbleButton;
  }
  setBubbleButton(bubbleButton:any){
    this.bubbleButton = bubbleButton;
  }

  getSelectedChart(){
    return this.selectedChart;
  }
  setSelectedChart(selectedChart : any){
    this.selectedChart = selectedChart;
  }
  getLogeduserId() {
    return this.logeduserId;
  }

  setNavBreadCrumb(navBreadCrumb: any[]) {
    this.navBreadCrumb = navBreadCrumb;
  }
  
  getNavBreadCrumb() {
    return this.navBreadCrumb;
  }

  setIsRowGroup(isRowGroup: String) {
    this.isRowGroup = isRowGroup;
  }
  
  getIsRowGroup() {
    return this.isRowGroup;
  }
  setLookUpSubmitValue(lookUpSubmitValue: string) {
    this.lookUpSubmitValue = lookUpSubmitValue;
  }
  
  getLookUpSubmitValue() {
    return this.lookUpSubmitValue;
  }
  
  setCheckExisting(checkExistingVal: String) {
    this.checkExisting = checkExistingVal;
  }
  
  getCheckExisting() {
    return this.checkExisting;
  }
  
  setOpenLikeForm(openLikeFormVal: String) {
    this.openLikeForm = openLikeFormVal;
  }

  getOpenLikeForm() {
    return this.openLikeForm;
  }

  removeOpenLikeForm(){
    this.openLikeForm = '';
  }
  
  setFormToOpen(formToOpenVal: String) {
    this.formToOpen = formToOpenVal;
  }
  
  getFormToOpen() {
    return this.formToOpen;
  }
  //doesn't have a get
  setROW_ID(ROW_ID_Val: any){
    this.ROW_ID = ROW_ID_Val;
  }
  
  getROW_ID(){
    return this.ROW_ID;
  }
  
  setMainJsonForDataWhenSkip(MainJsonForDataWhenSkipVal: string){
    this.mainJsonForDataWhenSkip = MainJsonForDataWhenSkipVal;
  }
  
  getMainJsonForDataWhenSkip(){
    return this.mainJsonForDataWhenSkip;
  }
  
  setStatusOfCheckExisting(statusOfCheckExistingVal: String){
    this.statusOfCheckExisting = statusOfCheckExistingVal;
  }
  
  getStatusOfCheckExisting(){
    return this.statusOfCheckExisting;
  }
  
  
  setPreviousTabIdSelected(previousTabIdSelectedVal: string){
    this.previousTabIdSelected = previousTabIdSelectedVal;
  }
  
  getPreviousTabIdSelected(){
    return this.previousTabIdSelected;
  }
  
  setAgGidSelectedNode(agGidSelectedNodeVal: string) {
    this.agGidSelectedNode = agGidSelectedNodeVal;
  }
  
  getAgGidSelectedNode() {
    return this.agGidSelectedNode;
  }

  setAgGidSelectedCell(agGidSelectedCellVal :string){
    this.agGidSelectedCell = agGidSelectedCellVal;
  }
  getAgGidSelectedCell(){
    return this.agGidSelectedCell;
  }

  setSelectedTabId(selectedTabIdVal: string){
    this.selectedTabId = selectedTabIdVal;
  }
  
  getSelectedTabId(){
    return this.selectedTabId;
  }

  setTabToBeSelectedOnValidate(tabToBeSelectedOnValidateVal: String){
    this.tabToBeSelectedOnValidate = tabToBeSelectedOnValidateVal;
  }
  
  getTabToBeSelectedOnValidate(){
    return this.tabToBeSelectedOnValidate;
  }
  
  setSelectedTabName(selectedTabNameVal: string){
    this.selectedTabName = selectedTabNameVal;
  }
  
  getSelectedTabName(){
    return this.selectedTabName;
  }
  removeSelectedTabName(){
    this.selectedTabName = '';
  }
  
  setPreviousTab(previousTabVal: String){
    this.previousTab = previousTabVal;
  }
  
  getPreviousTab(){
    return this.previousTab;
  }
  removePreviousTab(){
    this.previousTab = '';
  }
  
  setPreviousMainTab(previousMainTabVal: string) {
    this.previousMainTab = previousMainTabVal;
  }
  
  getPreviousMainTab() {
    return this.previousMainTab;
  }
  removePreviousMainTab(){
    this.previousMainTab = '';
  }
  
  setMainTab(mainTabVal: string) {
    this.mainTab = mainTabVal;
  }
  
  getMainTab() {
    return this.mainTab;
  }
  removeMainTab(){
    this.mainTab = '';
  }
  
  setTwiceClose(twiceCloseVal: String) {
    this.twiceClose = twiceCloseVal;
  }
  
  getTwiceClose() {
    return this.twiceClose;
  }
  
  setUserRoleId(userRoleIdVal: string) {
    this.userRoleId = userRoleIdVal;
  }
  
  getUserRoleId() {
    return this.userRoleId;
  }
  
  setPopupBreadcrumb(popupBreadcrumbVal: any) {
    this.popupBreadcrumb = popupBreadcrumbVal;
  }
  
  getPopupBreadcrumb() {
    return this.popupBreadcrumb;
  }
  removePopupBreadcrumb(){
    this.popupBreadcrumb = '';
  }
  
  setPreviousMainTab2(previousMainTab2Val: string) {
    this.previousMainTab2 = previousMainTab2Val;
  }
  
  getPreviousMainTab2() {
    return this.previousMainTab2;
  }
  
  setDynamicService(dynamicServiceName:any , value : any){
    this[dynamicServiceName] = value;
  }
  
  getDynamicService(dynamicServiceName:any){
    return this[dynamicServiceName]
  }
  
  setUserId(userIdVal: String) {
    this.UserId = userIdVal;
  }
  
  getUserId() {
    return this.UserId;
  }
  
  setMyId(MyIdVal: string) {
    this.MyId = MyIdVal;
  }
  
  getMyId() {
    return this.MyId;
  }
  
  setAgGidSelectedNodeRule(agGidSelectedNodeRuleVal: string) {
    this.agGidSelectedNodeRule = agGidSelectedNodeRuleVal;
  }
  
  getAgGidSelectedNodeRule() {
    return this.agGidSelectedNodeRule;
  }
  
  setMenuPath(menuPathVal: string) {
    this.menuPath = menuPathVal;
  }
  
  getMenuPath() {
    return this.menuPath;
  }

  setAgGridSelectedNode(agGridSelectedNodeVal: string) {
    this.agGridSelectedNode = agGridSelectedNodeVal;
  }
  
  getAgGridSelectedNode() {
    return this.agGridSelectedNode;
  }
  removeAgGridSelectedNode(){
    this.agGridSelectedNode = '';
  }

  setUserRoleName(userRoleNameVal: string) {
    this.userRoleName = userRoleNameVal;
  }
  
  getUserRoleName() {
    return this.userRoleName;
  }

  setRoleId(RoleIdVal: string) {
    this.RoleId = RoleIdVal;
  }
  
  getRoleId() {
    return this.RoleId;
  }

  setClickedItemDetails(clickedItemDetailsVal: string) {
    this.clickedItemDetails = clickedItemDetailsVal;
  }
  
  getClickedItemDetails() {
    return this.clickedItemDetails;
  }

  setIsFromMain(isFromMainVal: string) {
    this.isFromMain = isFromMainVal;
  }
  
  getIsFromMain() {
    return this.isFromMain;
  }

  setLogSerial(logSerialVal: string) {
    this.logSerial = logSerialVal;
  }
  
  getLogSerial() {
    return this.logSerial;
  }
  
  setFirstOpenGrid(firstOpenGridVal: string) {
    this.FirstOpenGrid = firstOpenGridVal;
  }
  
  getFirstOpenGrid() {
    return this.FirstOpenGrid;
  }

  setIsManaged(isManagedVal: string) {
    this.isManaged = isManagedVal;
  }
  
  getIsManaged() {
    return this.isManaged;
  }
  
  setDataToSet(dataToSetVal: string) {
    this.DataToSet = dataToSetVal;
  }
  
  getDataToSet() {
    return this.DataToSet;
  }
  
  setClickedTab(clickedTabVal: string) {
    this.clickedTab = clickedTabVal;
  }
  
  getClickedTab() {
    return this.clickedTab;
  }
  
  
  setTabpath(tabpathVal: string) {
    this.tabPath = tabpathVal;
  }
  
  getTabpath() {
    return this.tabPath;
  }
  removeTabpath(){
    this.tabPath = '';
  }
  
  
  setIsGrid(isGridVal: string) {
    this.isGrid = isGridVal;
  }
  
  getIsGrid() {
    return this.isGrid;
  }
  
  setDynamicRuleBuilderReloadUrl(dynamicRuleBuilderReloadUrlVal: string) {
    this.dynamicRuleBuilderReloadUrl = dynamicRuleBuilderReloadUrlVal;
  }
  
  getDynamicRuleBuilderReloadUrl() {
    return this.dynamicRuleBuilderReloadUrl;
  }
  
  setAccessRights(accessRightsVal: string) {
    this.accessRights = accessRightsVal;
    return accessRightsVal;
  }
  
  getAccessRights() {
    return this.accessRights;
  }
  
  setChoosenTab(choosenTabVal: string) {
    this.choosenTab = choosenTabVal;
  }
  
  getChoosenTab() {
    return this.choosenTab;
    
  }
  removeChoosenTab(){
    this.choosenTab = '';
  }
  
  setDynamicReportId(dynamicReportId: string) {
    this.dynamicReportId = dynamicReportId;
  }
  
  getDynamicReportId() {
    return this.dynamicReportId;
    
  }
  removeDynamicReportId(){
    this.dynamicReportId = '';
  }
  
  setListOFData(listOfData : any){
    this.listOfData = listOfData;
  }

  getListOfData (){
    return this.listOfData;
  }

  removeListOfData(){
    this.listOfData = '';
  }

  getSkip(){
    return this.Skip;
  }

  setSkip(skipValue : string){
    this.Skip = skipValue;
  }

  setReopen( reopenValue : string){
    this.reopen = reopenValue;
  }

  getReopen(){
      return this.reopen;
  }


  setIsDynamicReport(isDynamicReport: boolean) {
    this.isDynamicReport = isDynamicReport;
  }
  
  getIsDynamicReport() {
    return this.isDynamicReport;
    
  }
  removeIsDynamicReport(){
    this.isDynamicReport = false;
  }



  setSelectDay( selectDayValue : string){
    this.selectDay = selectDayValue;
  }

  getSelectDay(){
      return this.selectDay;
  }
  removeSelectDay(){
    this.selectDay = '';
  }

  setSelectMonths(selectMonthsValue: any) {
      this.selectMonths = selectMonthsValue;
  }

  getSelectMonths(){
      return this.selectMonths;
  }
  removeSelectMonths(){
    this.selectMonths = '';
  }
  addSelectMonths( selectMonthsValue : any){
    //ensuring taht selectMonths is an array that can be pushed inside
    if (!Array.isArray(this.selectMonths)) {
      this.selectMonths = [];
    }
    this.selectMonths.push(selectMonthsValue);
  }

  getSameSession(){
    return this.sameSession ; 
  }
  
  setSameSession(samesession : any){
    this.sameSession = samesession;
  }

  setCustomScreenBuilderPath(customScreenBuilderPathValue: any) {
    this.customScreenBuilderPath = customScreenBuilderPathValue;
  }

  getCustomScreenBuilderPath(){
      return this.customScreenBuilderPath;
  }

  setISExport(ISExport: any) {
    this.ISExport = ISExport;
  }

  getISExport(){
      return this.ISExport;
  }

  setVcisrowData(vcisrowData: any) {
    this.vcisrowData = vcisrowData;
  }

  getVcisrowData(){
      return this.vcisrowData;
  }

getSelectedGridNode(){
  return this.selectedGridNode;
}
setSelectedGridNode(Node : any){
  this.selectedGridNode = Node;
}

setAdvancedSearchFunctionData(advancedSearchFunctionData: any) {
  this.advancedSearchFunctionData = advancedSearchFunctionData;
}

getAdvancedSearchFunctionData() {
  return this.advancedSearchFunctionData;
}
getreloadGrid(){
  return this.reloadGrid;
}
setreloadGrid(check : boolean){
  this.reloadGrid = check;
}
getSelectedNodeVGrid(){
  return this.selectedNodeVGrid;
}
setSelectedNodeVGrid(node : object){
this.selectedNodeVGrid = node;
}

setDashboardContentSize(dashboardContentSize: any) {
  this.dashboardContentSize = dashboardContentSize;
}

getDashboardContentSize() {
  return this.dashboardContentSize;
}

getDynamicSearchApiData(){
  return this.dynamicSearchApiData;
}
setDynamicSearchApiData(dynamicSearchApiData:any){
  this.dynamicSearchApiData = dynamicSearchApiData;
}
setactionType(actionType: any) {
  this.actionType = actionType;
}

getactionType() {
  return this.actionType;
}

getAgGridNodeData(){
  return this.agGridNodeData ;
  }

setAgGridNodeData(data : object){
this.agGridNodeData = data;
}



getSelectedColumnValue(){
  return this.selectedColumnValue;
}
setSelectedColumnValue(selectedColumnValue:any){
  this.selectedColumnValue = selectedColumnValue;
}



getSelectedColumnFormOpening(){
  return this.selectedColumnFormOpening;
}
setSelectedColumnFormOpening(selectedColumnFormOpening:any){
  this.selectedColumnFormOpening = selectedColumnFormOpening;
}
removeSelectedColumnFormOpening(){
  this.selectedColumnFormOpening = '';
}


getMainPreviewScreenType(){
  return this.mainPreviewScreenType;
}
setMainPreviewScreenType(mainPreviewScreenType:any){
  this.mainPreviewScreenType = mainPreviewScreenType;
}
getAdvancedSearchShowGrid(){
  return this.advancedSearchShowGrid;
}
setAdvancedSearchShowGrid(advancedSearchShowGrid:any){
  this.advancedSearchShowGrid = advancedSearchShowGrid;
}
getAdvancedSearchShowGridMain(){
  return this.advancedSearchShowGridMain;
}
setAdvancedSearchShowGridMain(advancedSearchShowGridMain:any){
  this.advancedSearchShowGridMain = advancedSearchShowGridMain;
}
setIsDisplayALL(DisplayALL: boolean) {
  this.isDisplayAll = DisplayALL;
}

getIsDisplayALL() {
  return this.isDisplayAll;
}

getBreakLiveDataBar(){
  return this.breakLiveDataBar;
}
setBreakLiveDataBar(breakLiveDataBar:any){
  this.breakLiveDataBar = breakLiveDataBar;
}

getBreakLiveDataLine(){
  return this.breakLiveDataLine;
}
setBreakLiveDataLine(breakLiveDataLine:any){
  this.breakLiveDataLine = breakLiveDataLine;
}

getBreakLiveDataArea(){
  return this.breakLiveDataArea;
}
setBreakLiveDataArea(breakLiveDataArea:any){
  this.breakLiveDataArea = breakLiveDataArea;
}

getBreakLiveDataColumn(){
  return this.breakLiveDataColumn;
}
setBreakLiveDataColumn(breakLiveDataColumn:any){
  this.breakLiveDataColumn = breakLiveDataColumn;
}

setFormRecentData(formRecentData: any[]) {
  this.formRecentData = formRecentData;
}

getFormRecentData() {
  return this.formRecentData;
}
setIsFromMainPreviewForm(isFromMainPreviewForm: any) {
  this.isFromMainPreviewForm = isFromMainPreviewForm;
}

getIsFromMainPreviewForm() {
  return this.isFromMainPreviewForm;
}
}
