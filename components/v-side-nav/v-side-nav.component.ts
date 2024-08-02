import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/Kernel/services/authentication.service';
import { AppComponent } from 'src/app/app.component';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { SignInData } from 'src/app/Kernel/model/signInData';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { LoaderService } from 'src/app/Kernel/services/loader.service';
import { ChartPopupComponent } from 'src/app/Kernel/kernelapp/dashboard/chart-popup/chart-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { KpiRatioPopupComponent } from 'src/app/Kernel/kernelapp/dashboard/kpi-ratio-popup/kpi-ratio-popup.component';
import { GridPopupComponent } from 'src/app/Kernel/kernelapp/dashboard/grid-popup/grid-popup.component';
import { DashboardPopupComponent } from 'src/app/Kernel/kernelapp/dashboard/dashboard-popup/dashboard-popup.component';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component({
  selector: 'v-side-nav',
  templateUrl: './v-side-nav.component.html',
  styleUrls: ['./v-side-nav.component.css'],
})
export class SideNavComponent implements OnInit {

  toggleAppTheme: boolean; // used to toggle app theme

  // Logged In User Information
  public loggedInUsername: string = '';
  public loggedInUserFullName: string = '';
  public loggedInEmail: string = '';
  public loggedInFirstName: string = '';
  public loggedInImage: string = '';
  public isManaged: string = "";
  public menuVariable: String = "";
  public breadCrumbData:any[]=[];
  public userId = this.informationservice.getLogeduserId();
  // public userId = 1;
  public prevParentCode: String = ''; // Used to store the previous parent menu before selecting the child menu
  public appSname: String = '';
  user !: SignInData;
  
  //String to be shown as path
  public customPath: string = '';
  //to show the path of apps created by using screen builder
  public showCustomPath: boolean = false;

  toggleForm = new UntypedFormGroup({
    toggle: new UntypedFormControl('')
  });

  @Input() applicationName: any;
  @ViewChild(AppComponent) child: any;

  // Function used to navigate to child menus based on clicked parent
  toggleActiveMenu(menuCode: String, appAbrv: String, childMenusCount: String,menuName:String) {
    console.log("SELECTED MENU NAME>>>>>>>>>",menuName);
    console.log("ROUTER URL>>>>>>>>>>>>",this.router.url);
    console.log("ALL APPLICATIONS>>>>>",this.applicationName);
//      let  breadCrumbList: any[] = []; 

    /////////////////////////elie new Breadcrumb/////////////////////////////
    if(menuName != 'Back'){
    //  let storedBreadCrumbs = this.informationservice.getNavBreadCrumb();
      
      if(this.informationservice.getNavBreadCrumb().length !=0){
      //  breadCrumbList = storedBreadCrumbs; 
        console.log("breadCrumbList>>>>>", this.informationservice.getNavBreadCrumb());
    
        let siblingBreadCrumb = false;
        for(let i = 0; i < this.informationservice.getNavBreadCrumb().length; i++){
          console.log("BREADCRUMB>>>>>>", this.informationservice.getNavBreadCrumb()[i].name);
    
          for(let j = 0; j < this.applicationName.length; j++){
            console.log("application Name>>>>", this.applicationName[j].name);
    
            if(this.informationservice.getNavBreadCrumb()[i].name == this.applicationName[j].name){
              this.informationservice.setNavBreadCrumb(JSON.parse(JSON.stringify(this.informationservice.getNavBreadCrumb()).replace(this.informationservice.getNavBreadCrumb()[i].name.toString(), menuName.toString()).replace(this.informationservice.getNavBreadCrumb()[i].route, this.router.url)));
              siblingBreadCrumb = true;
            }
          }
        }
    
        if(!siblingBreadCrumb){
          let newPath = JSON.parse('{"name":"' + menuName + '","route":"' + this.router.url + '"}');
          this.informationservice.getNavBreadCrumb().push(newPath); 
        //  this.informationservice.setNavBreadCrumb(breadCrumbList);
        }
    
      } else {
        let newPath = JSON.parse('[{"name":"' + menuName + '","route":"' + this.router.url + '"}]');
        this.informationservice.setNavBreadCrumb(newPath);
      }
    
    } else {
     // let storedBreadCrumbs = this.informationservice.getNavBreadCrumb();
      console.log("STOED BREADCRUMBS>>>>>>>>>>",this.informationservice.getNavBreadCrumb());
      if(this.informationservice.getNavBreadCrumb().length!=0){
    //  let breadCrumbList = storedBreadCrumbs;
      this.informationservice.getNavBreadCrumb().pop(); 
      }
      //console.log("BREAD CRUMB LIST 2>>>>>>>>>>",breadCrumbList);
      
       // this.informationservice.setNavBreadCrumb(breadCrumbList);
      
    }
    this.breadCrumbData=this.informationservice.getNavBreadCrumb();
    // if(menuName!='Back'){
    //   if(this.informationservice.getNavBreadCrumb()!=''){
    //     breadCrumbList=JSON.parse(this.informationservice.getNavBreadCrumb());

    //     //let breadCrumbList=this.informationservice.getNavBreadCrumb().split("/");
    //     console.log("breadCrumbList>>>>>",breadCrumbList);

    //     let siblingBreadCrumb:boolean=false;
    //     for(let i=0;i<breadCrumbList.length;i++){
    //       console.log("BREADCRUMB>>>>>>",breadCrumbList[i].name);

    //       for(let j=0;j<this.applicationName.length;j++){
    //         console.log("application Name>>>>",this.applicationName[j].name);

    //           if(breadCrumbList[i].name==this.applicationName[j].name){
    //              // this.informationservice.setNavBreadCrumb(this.informationservice.getNavBreadCrumb().replace(breadCrumbList[i],menuName));
    //               this.informationservice.setNavBreadCrumb(this.informationservice.getNavBreadCrumb().replace(breadCrumbList[i].name,menuName).replace(breadCrumbList[i].route,this.router.url));
    //               siblingBreadCrumb=true;
    //           }
    //       }
    //     }  
    //     if(siblingBreadCrumb==false){
    //       let newPath=JSON.parse("{\"name\":\""+menuName+"\",\"route\":\""+this.router.url+"\"}");
    //       breadCrumbList.push(newPath)
    //       this.informationservice.setNavBreadCrumb(JSON.stringify(breadCrumbList));
    //     }

    //   }else{
    //     let newPath=JSON.parse("[{\"name\":\""+menuName+"\",\"route\":\""+this.router.url+"\"}]");

    //     this.informationservice.setNavBreadCrumb(JSON.stringify(newPath));

    //   }

    // }else{
      
    //  // let breadCrumbList=this.informationservice.getNavBreadCrumb().split("/");
    //  breadCrumbList=JSON.parse(this.informationservice.getNavBreadCrumb()); 
    // // let newBreadCrumbList='';
    // let newBreadCrumbList=breadCrumbList.pop();
    //   console.log("NEW BREAD CRUMB LIST AFTER BACK>>>>>>>>",newBreadCrumbList);
    //   // for(let i=0;i<breadCrumbList.length-1;i++){
    //   //   if(i==breadCrumbList.length-2){
    //   //   newBreadCrumbList+=breadCrumbList[i];
    //   //   }else{
    //   //     newBreadCrumbList+=breadCrumbList[i]+"/";
    //   //   }
    //   // }

    // this.informationservice.setNavBreadCrumb(JSON.stringify(newBreadCrumbList));
    //   console.log("New BreadCrumb List>>>>>>>>>",this.informationservice.getNavBreadCrumb());

    // }

///////////////////////////////////////////////////////////////////////////

    if ($('#side-nav').attr('class')!.indexOf("active") != -1) {
      if (appAbrv == null) {
        appAbrv = '';
      }
      if (childMenusCount != '0') {
        this.fetchChildMenus(menuCode);
      }
      if (appAbrv != '') {
        this.appSname = appAbrv;
      }
    }
  }

  // Function used to navigate to a page by routing
  onSelect(menuVariable: any, childMenus: any, name: any) {
    //building the path for the custom path used for the apps made by the screen
    if (this.customPath != undefined && this.customPath != '') {
      //adding names to the path
      const newPathSegment = " / " + name;
      this.customPath = this.customPath + newPathSegment;
    } else {
      //initialising path with the first name
      this.customPath = name;
    }

    if (childMenus != 0) {
      return;
    } else {
      if ($('#side-nav').attr('class')!.indexOf("active") != -1) {
        //turns off the custom path, using the original one unless it's a 
        //SB , it'll remain off and original on
        this.showCustomPath = false;
        //what will be shown in the path
        this.informationservice.setCustomScreenBuilderPath(this.customPath);
        //taking out the last portion of the path for if someone switched the last option without backing
        //or picked the same name multiple times
        let lastSlashIndex = this.customPath.lastIndexOf('/');
        this.customPath = this.customPath.substring(0, lastSlashIndex);
        //giving time to process that showCustomPath is false now, or it'll stay hidden
        setTimeout(() => {
          this.menuVariable = menuVariable;
          this.fetchUserAccessRights(this.menuVariable, 'menu',this.menuVariable,false);
  
          menuVariable = this.appSname + "/" + menuVariable;
          
          this.informationservice.setMenuPath(menuVariable);
          
          if (this.menuVariable.indexOf("SB") != -1) {
            //shows the custom path for screens created by screen builder
            //turning off the original one
            this.showCustomPath = true;
            let menuPath = this.appSname + '/dynamicScreen/' + menuVariable;
            menuPath = menuPath.replace('/' + this.appSname + '/', '/');
            this.commonFunctions.navigateToPage(menuPath);
          } else if (this.appSname != '') {
            this.commonFunctions.navigateToPage(menuVariable);
          } else {
            this.commonFunctions.navigateToPage(menuVariable);
          }
          this.toggleSwitch();
        }, 0);
      }
    }
  }

  //taking out the last element in the custom path each time the back button is pressed
  shortenCustomPath(){
    const lastSlashIndex = this.customPath.lastIndexOf('/');
    if (lastSlashIndex === -1) {
      return;
    }
    this.customPath = this.customPath.substring(0, lastSlashIndex);
  }

  fetchUserAccessRights(routeObj: any, sourceOfAccess: any,menuName:any,isFromBreadCrumb:boolean) {

    console.log("ROUTE OBJECT>>>>",routeObj);
    console.log("SOURCE OF ACCESS>>>>",sourceOfAccess);
    let isBreadCrumbParentClick:boolean;
    let menuVar: any;

    // if (sourceOfAccess == "breadcrumb") {
    //   menuVar = routeObj.url;
    // }

    // if (sourceOfAccess == "menu") {
    //   menuVar = routeObj;
    // }
    if(!isFromBreadCrumb){
    setTimeout(() => {
      this.informationservice.getNavBreadCrumb()[this.informationservice.getNavBreadCrumb().length-1].route=this.router.url
      console.log(this.informationservice.getNavBreadCrumb()[this.informationservice.getNavBreadCrumb().length-1].route);
      console.log("elieeee>>>>>>>>>>>>>",this.informationservice.getNavBreadCrumb());
    }, 500);
  }else{

    if(routeObj=='/dashboard'){
      this.breadCrumbData=[];
      this.informationservice.setNavBreadCrumb([]);
    }

    for(let i=0;i<this.informationservice.getNavBreadCrumb().length;i++){
      if(this.informationservice.getNavBreadCrumb()[i].name==menuName){
        isBreadCrumbParentClick=true;
        this.informationservice.getNavBreadCrumb().pop();
        continue;
      }
      if(isBreadCrumbParentClick){
        this.informationservice.getNavBreadCrumb().pop();
      }
    

    }
      

    let uniqueItems = this.informationservice.getNavBreadCrumb().reduce((acc, current) => {
      const x = acc.find((item: { name: any; }) => item.name === current.name);
      if (!x) {
        return acc.concat([current]);
      } else {
        return acc;
      }
    }, []);
    
    console.log("UNIQUE ARRAY>>>>>>>>>>",uniqueItems);
    this.informationservice.setNavBreadCrumb(uniqueItems);
  }
  
  
  console.log("Routing to navigate towards>>>>>>>>",routeObj);

  this.commonFunctions.navigateToPage(routeObj);

    // this.http.get<any>(GlobalConstants.checkIsMenuManaged + menuVar).subscribe(
    //   (res: String) => {
    //     this.isManaged = res.toString();
    //     this.informationservice.setIsManaged(this.isManaged);

    //     if (this.isManaged == "1") {
    //       this.http.get<any>(GlobalConstants.checkAccessRight + menuVar + "/" + this.userId).subscribe(
    //         (res: any) => {
    //           this.informationservice.setAccessRights(JSON.stringify(res));
              
    //           this.commonFunctions.navigateToPage(routeObj.url);
    //         },
    //         (error) => {
    //           console.log(error);
    //         }
    //       );
    //     }
    //   },
    //   (error) => {
    //     console.log(error);
    //   }
    // );
  }

  // Function used to fetch child menus related to a parent menu code
  fetchChildMenus(menuCode: any) {
    var dynamicMenus = new Array()
    this.http.get(GlobalConstants.fetchMenuApi + menuCode + "/" + this.userId, { headers: GlobalConstants.headers }).subscribe(
      (data) => {
        console.log("FETCH MENUUUU>>>>>>>>>>>",data);
        const menus: any = data;
        for (let i = 0; i < menus.length; i++) {
          this.menuVariable = menus[i].menuVariable;
          // fetchUserAccessRights();
          if (menus[i].menuManaged == '1') {
            dynamicMenus.push({
              id: menus[i].menuCode,
              appAbrv: menus[i].appSname,
              name: menus[i].menuName,
              icon: menus[i].menuIcon,
              parentCode: menus[i].prevParentCode,
              menuVariable: menus[i].menuVariable,
              childMenus: menus[i].childMenus,
              menuManaged: menus[i].menuManaged,
              display: menus[i].isDisplay
            });
          }
          this.prevParentCode = menus[i].prevParentCode.toString();
          if(this.prevParentCode === '-1'){
            this.customPath = '';
          }
        }
      }
    );
    this.applicationName = dynamicMenus;
  }

  // Function used to show arrow on menu containing child menus
  toggleChildNavigation(menuId: any) {
    if ($(".side-nav").attr("class")?.indexOf("active") != -1) {
      $(".navigateToChildArrow").css("display", "none");
      $("#childNav_" + menuId).toggle();
    }
  }

  // Function used to toggle side-nav menu
  toggleSwitch() {
    $(".navigateToChildArrow").css("display", "none");
    if ($(".side-nav").attr("class")?.indexOf("active") != -1) {
      $(".side-nav .body-section").css({ "overflow-y": "auto", "scrollbar-width": "" });
      $(".side-nav-body-content").removeClass("active");
      $(".side-nav").removeClass("active");
      $(".top-section-1 img").addClass("active");
      $(".app-logo").addClass("collapsed");
      $(".top-section").addClass("collapsed");
      $(".goBackDiv").addClass("collapsed");
      $(".side-nav-icon-img").addClass("collapsed");
      $(".title").addClass("collapsed");
      $(".list").addClass("collapsed");
    } else {
      $(".side-nav .body-section").css({ "overflow-y": "hidden", "scrollbar-width": "none" });
      $(".side-nav-body-content").addClass("active");
      $(".side-nav").addClass("active");
      $(".top-section-1 img").removeClass("active");
      $(".app-logo").removeClass("collapsed");
      $(".top-section").removeClass("collapsed");
      $(".goBackDiv").removeClass("collapsed");
      $(".side-nav-icon-img").removeClass("collapsed");
      $(".list").removeClass("collapsed");
      setTimeout(() => { $(".title").removeClass("collapsed") }, 500);
    }
  }

  // Function used to logout from application
  logoutApplication() {
    this.authenticationService.logout();
  }

  constructor(public authenticationService: AuthenticationService,
    private router: Router,
    private http: HttpClient,
    private dialog: MatDialog,
    private commonFunctions: CommonFunctions,
    public loaderService: LoaderService,
    public informationservice: InformationService) { }

  ngOnInit(): void {
    //this.informationservice.setNavBreadCrumb('');
    
    // In case of application related reset choosenTab control that's found in v-tabs
    // localStorage.removeItem("choosenTab");
    this.informationservice.removeChoosenTab();

    // Start application with root menus
    this.fetchChildMenus(0);
    this.prevParentCode = '0';
    setTimeout(() => { this.handleOnLoadDesign() }, 600);

    // Fetch logged in User Info
    this.fetchUserData();
  }

  fetchUserData() {
    this.http.get<any>(GlobalConstants.fetchUSMUserApi + this.userId, { headers: GlobalConstants.headers }).subscribe(
      (data) => {
        console.log("FETCHED DATAAAAAAAAAAA>>>>>>>>>>>",data);
        if (data.email == null) {
          this.loggedInEmail = 'No Email';
        } else {
          this.loggedInEmail = data.email;
        }
        if (data.firstName == null && data.lastName == null) {
          this.loggedInUserFullName = 'No Name'
        } else {
          this.loggedInUserFullName = data.firstName + " " + data.lastName;
        }
        if (data.userImage == null) {
          this.loggedInImage = "assets/img/Profile-PNG-File.png";
        } else {
          this.loggedInImage = "data:image/png;base64," + data.userImage;
        }
        this.loggedInUsername = data.username;
        this.loggedInFirstName = data.firstName;
      }
    );
  }


  handleOnLoadDesign() {
    $(".app-logo").addClass("collapsed");
    $(".top-section").addClass("collapsed");
    $(".goBackDiv").addClass("collapsed");
    $(".side-nav-icon-img").addClass("collapsed");
    $(".title").addClass("collapsed");
    $(".list").addClass("collapsed");
    $(".body-section").removeClass("hideElement");
    $(".body-section").addClass("showElement");
  }

  toggleUserProfileDropdown() {
    $("#myDropdown0").removeClass("show");
    $("#myDropdown1").toggleClass("show");

    console.log("malekkk>>><<", $("#openLoginUser"))
    $("#openLoginUser").click();
  }
  toggleSettingsDropdown() {
    $("#myDropdown0").removeClass("show");
    $("#myDropdown2").toggleClass("show");
  }
  toggleAppDrawerDropdown() {
    $("#myDropdown1").removeClass("show");
    $("#myDropdown0").toggleClass("show");
  }

  openAppPreferences() {
    $("#myDropdown1 .section-1").hide();
    $("#myDropdown1 .section-1-hr").hide();
    $("#myDropdown1 .section-2").hide();
    $("#myDropdown1 .section-2-hr").hide();
    $("#myDropdown1 .section-3").hide();
    $("#myDropdown1 .section-2-app-pref").show();
  }

  backToUserProfilefromAppPref() {
    $("#myDropdown1 .section-1").show();
    $("#myDropdown1 .section-1-hr").show();
    $("#myDropdown1 .section-2").show();
    $("#myDropdown1 .section-2-hr").show();
    $("#myDropdown1 .section-3").show();
    $("#myDropdown1 .section-2-app-pref").hide();
  }
  openChartPopup() {
    const dialogRef = this.dialog.open(ChartPopupComponent, {
      // data:
      width: '70%',
      height: '70%',
    });
  }
  openKpiPopup() {
    const dialogRef = this.dialog.open(KpiRatioPopupComponent, {
      // data:
      width: '70%',
      height: '70%',
    });
  }
  openGridPopup() {
    const dialogRef = this.dialog.open(GridPopupComponent, {
      // data:
      width: '70%',
      height: '70%',
    });
  }
  openDashboardPopup() {
    const dialogRef = this.dialog.open(DashboardPopupComponent, {
      // data:
      width: '70%',
      height: '70%',
    });
  }
}
