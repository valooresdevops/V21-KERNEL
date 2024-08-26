import { Component, Input } from '@angular/core';
import { CommonFunctions } from '../../common/CommonFunctions';
import { InformationService } from '../../services/information.service';
import { Router } from '@angular/router';
import { GlobalConstants } from '../../common/GlobalConstants';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'v-bubble-button',
  templateUrl: './bubble-button.component.html',
  styleUrl: './bubble-button.component.css'
})
export class BubbleButtonComponent {
  @Input() public imgSrc: any;

  public isClicked: boolean = false;
  public bubbleMenus: any;
  public bubbleChildMenus: any;
  public allMenus: any;





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
public customPath: any;
// public userId = 1;
public prevParentCode: String = ''; // Used to store the previous parent menu before selecting the child menu
public appSname: String = '';
public a: boolean = false;
public counter: Number=0;
public previousMenuCode:String='';
public isSideNavActive = false; // Manage the state of the side nav










  constructor(
    private router: Router,
    public commonFunctions: CommonFunctions,
    public informationservice: InformationService,
    private http: HttpClient,

  ){}
  ngOnInit(): void
  {
this.bubbleMenus = this.informationservice.getBubbleButton();
console.log('bubbleMenus>>> :',this.bubbleMenus);
this.allMenus = this.bubbleMenus;
// console.log(this.imgSrc);
  }
  onBubbleClicked(){
    // console.log("selected>>>>>>>>",this.informationservice.getAgGidSelectedNode());
    // this.commonFunctions.navigateToPage(this.menuVariable);
    console.log("Bubble Clicked !!!!!!!!!");
    this.isClicked == true;
  }
   // Function used to navigate to a page by routing
   onSelect(menuVariable: any, childMenus: any, name: any) {
    let menuPath = this.appSname + '/dynamicScreen/' + menuVariable;
    menuPath = menuPath.replace('/' + this.appSname + '/', '/');
    if(childMenus == 0){
      this.commonFunctions.navigateToPage(menuPath);
    }
  }
    // Toggle the state of the side nav
    toggleSideNav() {
      this.isSideNavActive = !this.isSideNavActive;
    }
  toggleActiveMenu(menuCode: String, appAbrv: String, childMenusCount: String,menuName:String) {
    console.log('menuCode>>>>>>>>>>',menuCode);
    console.log('appAbrv>>>>>>>>>',appAbrv);
    console.log('childMenusCount>>>>>>',childMenusCount);
    this.menuName = menuName;

    if(menuName != 'Back'){      
      if(this.informationservice.getNavBreadCrumb().length !=0){
    
        let siblingBreadCrumb = false;
        for(let i = 0; i < this.informationservice.getNavBreadCrumb().length; i++){
    
          for(let j = 0; j < this.bubbleMenus.length; j++){
    
            if(this.informationservice.getNavBreadCrumb()[i].name == this.bubbleMenus[j].name){
              this.informationservice.setNavBreadCrumb(JSON.parse(JSON.stringify(this.informationservice.getNavBreadCrumb()).replace(this.informationservice.getNavBreadCrumb()[i].name.toString(), menuName.toString()).replace(this.informationservice.getNavBreadCrumb()[i].route, this.router.url)));
              siblingBreadCrumb = true;
            }
          }
        }
        if(!siblingBreadCrumb){
          let newPath = JSON.parse('{"name":"' + menuName + '","route":"' + this.router.url + '"}');
          this.informationservice.getNavBreadCrumb().push(newPath); 
        }
      } else {
        let newPath = JSON.parse('[{"name":"' + menuName + '","route":"' + this.router.url + '"}]');
        this.informationservice.setNavBreadCrumb(newPath);
      }
    
    } else {
      if(this.informationservice.getNavBreadCrumb().length!=0){
      this.informationservice.getNavBreadCrumb().pop(); 
      } 
    }
    this.breadCrumbData=this.informationservice.getNavBreadCrumb();
      if (appAbrv == null) {
        appAbrv = '';
      }
      console.log('childMenusCount>>>>>>>',childMenusCount);
      if (childMenusCount != '0') {
        this.fetchChildMenus(menuCode);
      }
      if (appAbrv != '') {
        this.appSname = appAbrv;
      }
    
  }



  fetchChildMenus(menuCode: any) {
    // If 'a' is true and menuCode is the same as previousMenuCode
    if (this.a && menuCode === this.previousMenuCode) {
        // Only set 'a' to false if menuCode is the same
        this.a = false;
        return; // Exit the function
    }

    // Check if it's the first click (i.e., previousMenuCode is not set)
    if (this.previousMenuCode === null || this.previousMenuCode === '') {
        // Perform the fetch as usual
        this.previousMenuCode = menuCode;
        this.http.get(GlobalConstants.fetchMenuApi + menuCode + "/" + this.userId, { headers: GlobalConstants.headers })
            .subscribe(
                (data: any) => {
                    const menus: any = data;
                    let bubbleMenus = new Array();

                    for (let i = 0; i < menus.length; i++) {
                        if (menus[i].menuManaged == '1') {
                            if (menus[i].menuType == '10') {
                                bubbleMenus.push({
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
                        }
                    }

                    // Update bubbleChildMenus with new data
                    this.bubbleChildMenus = bubbleMenus;
                    this.a = true; // Ensure 'a' remains true after fetching new data
                }
            );
    } else {
        // If menuCode is different from previousMenuCode, fetch new data
        if (menuCode !== this.previousMenuCode) {
            this.previousMenuCode = menuCode;
            this.http.get(GlobalConstants.fetchMenuApi + menuCode + "/" + this.userId, { headers: GlobalConstants.headers })
                .subscribe(
                    (data: any) => {
                        const menus: any = data;
                        let bubbleMenus = new Array();

                        for (let i = 0; i < menus.length; i++) {
                            if (menus[i].menuManaged == '1') {
                                if (menus[i].menuType == '10') {
                                    bubbleMenus.push({
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
                            }
                        }

                        // Update bubbleChildMenus with new data
                        this.bubbleChildMenus = bubbleMenus;
                        this.a = true; // Ensure 'a' remains true after fetching new data
                    }
                );
        } else {
            // If 'a' is false, set it to true to allow future fetches
            this.a = true;
        }
    }
}
}
