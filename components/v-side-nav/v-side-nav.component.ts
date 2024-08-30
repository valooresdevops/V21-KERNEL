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
import { from, lastValueFrom } from 'rxjs';
import axios from 'axios';
import { DashboardAccessRightsComponent } from '../../kernelapp/dashboard/dashboard-access-rights/dashboard-access-rights.component';

@Component({
  selector: 'v-side-nav',
  templateUrl: './v-side-nav.component.html',
  styleUrls: ['./v-side-nav.component.css'],
})
export class SideNavComponent implements OnInit {

  toggleAppTheme: boolean; // used to toggle app theme

  public themeColorsChanged: boolean = false;
  public isBob: boolean = false;
  
  backGroundColor: string;
  layoutForegroundColor: string;
  appBtnHoverColor: string;
  appBtnBgColor: string;
  sideNavColor: string;
  appNavigationBackgroundColor: string;
  appBtnMainColor: string;
  whiteColor: string;
  blackColor: string;
  blueColor: string;

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
  public menuName : String = "";
  // public userId = 1;
  public prevParentCode: String = ''; // Used to store the previous parent menu before selecting the child menu
  public appSname: String = '';
   a: boolean =false;
  user !: SignInData;
  public goBackSrc: String = "assets/img/goBack.png"
  jp:boolean=false;
  
public parentMenu : Boolean = true; // used to check if the menu selected is the first parent menu ; to insert a new usmdba.usm_user_log logId value


  //String to be shown as path
  public customPath: string = '';
  //to show the path of apps created by using screen builder
  public showCustomPath: boolean = false;

  toggleForm = new UntypedFormGroup({
    toggle: new UntypedFormControl('')
  });

  @Input() applicationName: any;
  @ViewChild(AppComponent) child: any;
  isCollapsed: boolean = true; // Initial state of the side navigation, adjust as needed

  checkParent(){
  }



  ///////Sigma
   // Function to insert selected tabs into the database 
   insertTab( menuId: String , menuName : String){
    // alert(menuId);


    if(menuId.length ==3){
      this.parentMenu=true;
    }else{
      this.parentMenu=false;
    }

  setTimeout(() => {
    
  }, 100);
    const loggedUserId = localStorage.getItem('LogeduserId');
    // console.log("userId>>>>>>", loggedUserId);
    const ipAddress = window.location.hostname;
    // console.log("ip>>>>>",window.location.hostname);
    console.log("this.parentMenu>>>>", this.parentMenu);
    const saveToDB = from(axios.post(GlobalConstants.insertSelectedTab + menuId + "/"+loggedUserId + "/"+ipAddress +"/"+menuName + "/" + this.parentMenu));
    // this.from(GlobalConstants.insertSelectedTab + menuId + "/"+loggedUserId + "/"+ipAddress, { headers: GlobalConstants.headers });
  saveToDB;
   }

  // Function used to navigate to child menus based on clicked parent
  toggleActiveMenu(menuCode: String, appAbrv: String, childMenusCount: String,menuName:String) {
    
    // if(this.a == true){

      
   
      // this.jp = true;

    // }
    console.log("SELECTED MENU NAME>>>>>>>>>",menuName);
    console.log("Child Menu Count>>>>>>>>>",childMenusCount);
    // console.log("SELECTED MENU NAME>>>>>>>>>",menuName);
    // console.log("Child Menu Count>>>>>>>>>",childMenusCount);

    // console.log("ROUTER URL>>>>>>>>>>>>",this.router.url);
    // console.log("ALL APPLICATIONS>>>>>",this.applicationName);
    this.menuName = menuName;
//      let  breadCrumbList: any[] = []; 

    /////////////////////////elie new Breadcrumb/////////////////////////////
    if(menuName != 'Back'){
    //  let storedBreadCrumbs = this.informationservice.getNavBreadCrumb();
      
      if(this.informationservice.getNavBreadCrumb().length !=0){
      //  breadCrumbList = storedBreadCrumbs; 
        // console.log("breadCrumbList>>>>>", this.informationservice.getNavBreadCrumb());
    
        let siblingBreadCrumb = false;
        for(let i = 0; i < this.informationservice.getNavBreadCrumb().length; i++){
          // console.log("BREADCRUMB>>>>>>", this.informationservice.getNavBreadCrumb()[i].name);
    
          for(let j = 0; j < this.applicationName.length; j++){
            // console.log("application Name>>>>", this.applicationName[j].name);
    
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
      // console.log("STOED BREADCRUMBS>>>>>>>>>>",this.informationservice.getNavBreadCrumb());
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
    // console.log("menuName>>>>>>>",name);
    // console.log("menuVariable>>>>>",menuVariable);
    // console.log("menuChildrens>>>>>>",childMenus);
    // //building the path for the custom path used for the apps made by the screen
    // console.log("menuVariable>>>>>>>>>>>>>",menuVariable);
    // console.log("childMenus>>>>>>>>>>>>>",childMenus);
    // console.log("name>>>>>>>>>>>>>",name);
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

    // console.log("ROUTE OBJECT>>>>",routeObj);
    // console.log("SOURCE OF ACCESS>>>>",sourceOfAccess);
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
      // console.log(this.informationservice.getNavBreadCrumb()[this.informationservice.getNavBreadCrumb().length-1].route);
      // console.log("elieeee>>>>>>>>>>>>>",this.informationservice.getNavBreadCrumb());
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
    
    // console.log("UNIQUE ARRAY>>>>>>>>>>",uniqueItems);
    this.informationservice.setNavBreadCrumb(uniqueItems);
  }
  
  
  // console.log("Routing to navigate towards>>>>>>>>",routeObj);

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

  // fetchChildMenus(menuCode: any) {
  //   this.http.get(GlobalConstants.fetchMenuApi + menuCode + "/" + this.userId, { headers: GlobalConstants.headers })
  //     .subscribe((data) => {
  //       console.log("FETCH MENU DATA >>>>>>>", data);
  //       const menus: any = data;
  
  //       // Step 1: Build a map of menus by their ID
  //       const menuMap: { [key: string]: any } = {};
  //       menus.forEach((menu:any) => {
  //         menu.childMenus = []; // Initialize childMenus as an empty array
  //         menuMap[menu.id] = menu;
  //       });
  
  //       // Step 2: Build the hierarchy by setting childMenus
  //       const rootMenus: any[] = [];
  //       menus.forEach((menu:any) => {
  //         if (menu.prevParentCode === '-1') {
  //           rootMenus.push(menu); // Root menu (no parent)
  //         } else {
  //           const parentMenu = menuMap[menu.prevParentCode];
  //           if (parentMenu) {
  //             parentMenu.childMenus.push(menu); // Add to parent's childMenus
  //           }
  //         }
  //       });
  
  //       // Step 3: Separate bubbleMenus and dynamicMenus
  //       const dynamicMenus: any[] = [];
  //       const bubbleMenus: any[] = [];
  //       rootMenus.forEach(menu => {
  //         if (menu.menuManaged === '1') {
  //           if (menu.menuType !== '10') {
  //             dynamicMenus.push(menu);
  //           } else {
  //             this.a=true;
  //             bubbleMenus.push(menu);
  //             // Fetch child menus for bubbleMenus items if they exist
  //             if (menu.childMenus.length > 0) {
  //               this.fetchChildMenusForBubble(menu);
  //             }
  //           }
  //         }
  //       });
  
  //       // Step 4: Update state and navigate if needed
  //       if (this.a) {
  //         this.applicationName = bubbleMenus;
  //         console.log('applicationName >>>>>>>>>: ', this.applicationName);
  //         this.informationservice.setBubbleMenus(this.applicationName);
  //         this.router.navigate(['/bubble']);
  //       } else {
  //         this.applicationName = dynamicMenus;
  //       }
  //     });
  // }
  
  // // Helper function to fetch child menus for bubble menu items
  // private fetchChildMenusForBubble(parentMenu: any) {
  //   this.http.get(GlobalConstants.fetchMenuApi + parentMenu.id + "/" + this.userId, { headers: GlobalConstants.headers })
  //     .subscribe((data) => {
  //       const childMenus: any = data;
  //       childMenus.forEach((childMenu:any) => {
  //         if (childMenu.menuManaged === '1' && childMenu.menuType === '10') {
  //           parentMenu.childMenus.push(childMenu);
  //         }
  //       });
  //     });
  // }
  private fetchAndAssociateChildMenus(parentMenu: any) {
    this.http.get(GlobalConstants.fetchMenuApi + parentMenu.id + "/" + this.userId, { headers: GlobalConstants.headers })
      .subscribe((data) => {
        const childMenus: any= data;
        childMenus.forEach((childMenu:any) => {
          if (childMenu.menuManaged === '1') {
            parentMenu.childMenus.push(childMenu);
          }
        });
      });
    }

  fetchChildMenus(menuCode: any) {
    var dynamicMenus = new Array()
    var bubbleMenus = new Array()
    this.http.get(GlobalConstants.fetchMenuApi + menuCode + "/" + this.userId, { headers: GlobalConstants.headers }).subscribe(
      async (data) => {
        // console.log("FETCH MENUUUU>>>>>>>>>>>",data);
        const menus: any = data;
        for (let i = 0; i < menus.length; i++) {
          this.menuVariable = menus[i].menuVariable;
          // fetchUserAccessRights();
          if (menus[i].menuManaged == '1') {
            if(menus[i].menuType !='10'){
              this.a=false;
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
          }else if(menus[i].menuType == '10'){
            this.a=true;
            bubbleMenus.push({
              id: menus[i].menuCode,
              appAbrv: menus[i].appSname,
              name: menus[i].menuName,
              icon: menus[i].menuIcon,
              parentCode: menus[i].prevParentCode,
              menuVariable: menus[i].menuVariable,
              childMenus: menus[i].childMenus,
              menuManaged: menus[i].menuManaged,
              display: menus[i].isDisplay,
          //    parentMenuCode: '0'
            });
            console.log('children<>>>>>>>>>>: ',menus[i].childMenus)
            // if((menus[i].childMenus != 0)){
            //   const childMenu = from(axios.get(GlobalConstants.fetchMenuApi + menus[i].menuCode + "/" + this.userId));
            //   const childMenuData = await lastValueFrom(childMenu);
            //       console.log("childMenuData>>>>>>",childMenuData)
            //       const childmenus: any = childMenuData.data;
            //       for (let j = 0; j < childmenus.length; j++) {
            //       if (childmenus[j].menuManaged == '1') {
            //       if(childmenus[j].menuType == '10'){
            //         bubbleMenus.push({
            //           id: childmenus[j].menuCode,
            //           appAbrv: childmenus[j].appSname,
            //           name: childmenus[j].menuName,
            //           icon: childmenus[j].menuIcon,
            //           parentCode: childmenus[j].prevParentCode,
            //           menuVariable: childmenus[j].menuVariable,
            //           childMenus: childmenus[j].childMenus,
            //           menuManaged: childmenus[j].menuManaged,
            //           display: childmenus[j].isDisplay,
            //           parentMenuCode: menus[i].menuCode
            //         });
            //       }
            //       }
            //     }
          
            // }
          }
          }
          this.prevParentCode = menus[i].prevParentCode.toString();
          if(this.prevParentCode === '-1'){
            this.customPath = '';
          }
        }
      
      if(this.a == true){
        this.applicationName = bubbleMenus;
        console.log('applicationName>>>>>>>>>>>>>>: ',this.applicationName)
        this.informationservice.setBubbleButton(this.applicationName);
        this.router.navigate(['/bubble']);

      }else{
        this.applicationName = dynamicMenus;
  
      }
    }
    );
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
    this.isCollapsed = !this.isCollapsed;

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

    const root = document.documentElement;

    root.style.setProperty('--layout-foreground-color', localStorage.getItem('layoutForegroundColor'));
    root.style.setProperty('--side-nav-color', localStorage.getItem('sideNavColor'));
    root.style.setProperty('--app-navigation-background-color', localStorage.getItem('appNavigationBackgroundColor'));
    root.style.setProperty('--app-btn-main-color', localStorage.getItem('appBtnMainColor'));
    root.style.setProperty('--app-btn-bg-color', localStorage.getItem('appBtnBgColor'));
    root.style.setProperty('--app-btn-hover-color', localStorage.getItem('appBtnHoverColor'));
    root.style.setProperty('--white-color', localStorage.getItem('whiteColor'));
    root.style.setProperty('--app-background-color', localStorage.getItem('backGroundColor'));
    root.style.setProperty('--blue-color', localStorage.getItem('blueColor'));



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
        // console.log("FETCHED DATAAAAAAAAAAA>>>>>>>>>>>",data);
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

    // console.log("malekkk>>><<", $("#openLoginUser"))
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
  openAlertPopup(){
    
  }

  openDashboardAccessSettings() {
    const dialogRef = this.dialog.open(DashboardAccessRightsComponent, {
      // data:
      width: '70%',
      height: '70%',
    });
  }

  changeThemeToDefault()
  {
    this.themeColorsChanged = true;

    const root = document.documentElement;
        
    this.layoutForegroundColor = 'rgb(0, 0, 0)';
    root.style.setProperty('--layout-foreground-color', this.layoutForegroundColor);

    this.sideNavColor = 'rgb(52, 58, 64)';
    root.style.setProperty('--side-nav-color', this.sideNavColor);

    this.appNavigationBackgroundColor = 'rgb(243, 242, 239)';
    root.style.setProperty('--lightgrey-color', this.appNavigationBackgroundColor);
    
    this.appBtnMainColor = 'rgb(46, 140, 200)';
    root.style.setProperty('--app-btn-main-color', this.appBtnMainColor);

    this.appBtnBgColor = 'rgb(255, 255, 255)';
    root.style.setProperty('--app-btn-bg-color', this.appBtnBgColor);

    this.appBtnHoverColor = 'rgb(225, 225, 225)';
    root.style.setProperty('--app-btn-hover-color', this.appBtnHoverColor);

    this.whiteColor = 'rgb(255, 255, 255)';
    root.style.setProperty('--white-color', this.whiteColor);

    this.blackColor = 'rgb(52, 58, 64)';
    root.style.setProperty('--black-color', this.blackColor);

    this.backGroundColor = 'rgb(255 255, 255)';
    root.style.setProperty('--app-background-color', this.backGroundColor);

    this.blueColor = 'rgb(46, 140, 200)';
    root.style.setProperty('--blue-color', this.blueColor);


    // Save the theme colors to local storage
    localStorage.setItem('themeColorsChanged', JSON.stringify(this.themeColorsChanged));
    localStorage.setItem('layoutForegroundColor', this.layoutForegroundColor);
    localStorage.setItem('sideNavColor', this.sideNavColor);
    localStorage.setItem('appNavigationBackgroundColor', this.appNavigationBackgroundColor);
    localStorage.setItem('appBtnMainColor', this.appBtnMainColor);
    localStorage.setItem('appBtnBgColor', this.appBtnBgColor);
    localStorage.setItem('appBtnHoverColor', this.appBtnHoverColor);
    localStorage.setItem('whiteColor', this.whiteColor);
    localStorage.setItem('blackColor', this.blackColor);
    localStorage.setItem('backGroundColor', this.backGroundColor);
    localStorage.setItem('blueColor', this.blueColor);
  }

  changeThemeToWyb()
  {
    const root = document.documentElement;

    this.themeColorsChanged = true;

    this.layoutForegroundColor = 'rgb(255, 255, 255)';
    root.style.setProperty('--layout-foreground-color', this.layoutForegroundColor);

    this.sideNavColor = 'rgb(255, 255, 255)';
    root.style.setProperty('--side-nav-color', this.sideNavColor);

    this.appNavigationBackgroundColor = 'rgb(0, 0, 0)';
    root.style.setProperty('--app-navigation-background-color', this.appNavigationBackgroundColor);
    
    this.appBtnMainColor = 'rgb(225, 224, 26)';
    root.style.setProperty('--app-btn-main-color', this.appBtnMainColor);

    this.appBtnBgColor = 'rgb(0, 0, 0)';
    root.style.setProperty('--app-btn-bg-color', this.appBtnBgColor);

    this.appBtnHoverColor = 'rgb(0, 0, 0)';
    root.style.setProperty('--app-btn-hover-color', this.appBtnHoverColor);

    this.whiteColor = 'rgb(225, 224, 26)';
    root.style.setProperty('--white-color', this.whiteColor);

    this.blackColor = 'rgb(255 255, 255)';
    root.style.setProperty('--black-color', this.blackColor);

    this.backGroundColor = 'rgb(255 254, 26)';
    root.style.setProperty('--app-background-color', this.backGroundColor);

    this.blueColor = 'rgb(0, 0, 0)';
    root.style.setProperty('--blue-color', this.blueColor);
    


    // Save the theme colors to local storage
    localStorage.setItem('themeColorsChanged', JSON.stringify(this.themeColorsChanged));
    localStorage.setItem('layoutForegroundColor', this.layoutForegroundColor);
    localStorage.setItem('sideNavColor', this.sideNavColor);
    localStorage.setItem('appNavigationBackgroundColor', this.appNavigationBackgroundColor);
    localStorage.setItem('appBtnMainColor', this.appBtnMainColor);
    localStorage.setItem('appBtnBgColor', this.appBtnBgColor);
    localStorage.setItem('appBtnHoverColor', this.appBtnHoverColor);
    localStorage.setItem('whiteColor', this.whiteColor);
    localStorage.setItem('blackColor', this.blackColor);
    localStorage.setItem('backGroundColor', this.backGroundColor);
    localStorage.setItem('blueColor', this.blueColor);
  }
}
