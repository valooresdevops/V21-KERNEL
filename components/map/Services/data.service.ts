import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private data: any;
  private startdate:any;
  private endDate:any;
  private BeginTime:any;
  private EndTime:any;
  private ok:boolean
  private cancel:boolean
  private DHselected:any;
  private TimelineSimulID:any;
  private senarioID:any;
  private timelineFromMap: number = 0;
  private fromTimeline:number = 0;
  private routedevices:any;
  private datePickerFromTimeline:number = 0;
  private deviceCommonLocationHits: number = 0;
  getSimulationData: any;
  private shapeIdDate:any;
  private valuedate:any;
  private mapType:any;
  private datacrowd:boolean=false;
  private headerFromKwg:boolean=false;
  private headerType:any;
  
  constructor() { }

  getFromTimeline() {
    return this.fromTimeline;
  }
  setFromTimeline(value: number) {
    this.fromTimeline = value;
  }
  getTimelineFromMap() {
    return this.timelineFromMap;
  }
  setTimelineFromMap(value: number) {
    this.timelineFromMap = value;
  }
  setData(data: any) {
    this.data = data;
  }

  getData() {
    return this.data;
  }

  setStartDate(startdate: any) {
    this.startdate = startdate;
  }

  getstartdate() {
    return this.startdate;
  }

  setendDate(endDate: any) {
    this.endDate = endDate;
  }

  getendDate() {
    return this.endDate;
  }
  setOK(ok: boolean) {
    this.ok = ok;
  }

  getOk() {
    return this.ok;
  }
  setCancel(cancel: boolean) {
    this.cancel = cancel;
  }

  getCancel() {
    return this.cancel;
  }
  setDHselectedDevice(DHselected: any) {
    this.DHselected = DHselected;
  }
  getDHselectedDevice() {
    return this.DHselected;
  }

  setTimelineSimulID(TimelineSimulID: any) {
    this.TimelineSimulID = TimelineSimulID;
  }
  getTimelineSimulID() {
    return this.TimelineSimulID;
  }

  setEndTime(EndTime: any) {
    this.EndTime = EndTime;
  }
  getEndTime() {
    return this.EndTime;
  }

  setBeginTime(BeginTime: any) {
    this.BeginTime = BeginTime;
  }
  getBeginTime() {
    return this.BeginTime;
  }
  setsenarioID(senarioID: any) {
    this.senarioID = senarioID;
  }
  getsenarioID() {
    return this.senarioID;
  }
  setroutedevices(routedevices: any) {
    this.routedevices = routedevices;
  }
  getroutedevices() {
    return this.routedevices;
  }
  getDatePickerFromTimeline(){
    return this.datePickerFromTimeline;
  }
  setDatePickerFromTimeline(datePickerFromTimeline: any) {
    this.datePickerFromTimeline = datePickerFromTimeline;
  }
  getDeviceCommonLocationHits(){
    return this.deviceCommonLocationHits;
  }
  setDeviceCommonLocationHits(deviceCommonLocationHits: any) {
    this.deviceCommonLocationHits = deviceCommonLocationHits;
  }
  setshapeIdDate(shapeIdDate:any){
    this.shapeIdDate=shapeIdDate;
  }
  getshapeIdDate(){
    return this.shapeIdDate;
  }
  setvaluedate(valuedate:any){
    console.log("settttttttttttttttttt value in service:",valuedate);
    this.valuedate=valuedate;
  }
  getvaluedate(){
    console.log("value in service:",this.valuedate);
    return this.valuedate;
  }
  setmapType(mapType:any){
    this.mapType=mapType;
  }
  getmapType(){
    return this.mapType;
  }
  setdatacrowd(datacrowd:boolean){
    this.datacrowd=datacrowd;
  }
  getdatacrowd(){
    return this.datacrowd;
  }
  setHeaderFromKwg(headerFromKwg:boolean){
    this.headerFromKwg=headerFromKwg;
  }
  getHeaderFromKwg(){
    return this.headerFromKwg;
  }
  setheaderType(headerType:any){
    this.headerType=headerType;
  }
  getheaderType(){
    return this.headerType;
  }
}
