import { Component, OnDestroy, OnInit } from '@angular/core';
import { AuthenticationService } from './Kernel/services/authentication.service';
import { Subscription } from 'rxjs';
import { Title } from "@angular/platform-browser";
import { CommonFunctions } from './Kernel/common/CommonFunctions';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit, OnDestroy {

  isAuthenticated = false;
  public userSub: Subscription | undefined;
  public fetchMenuApi: any = '/api/menu/';


  constructor(private authenticationService: AuthenticationService, 
              private titleService: Title,
              private commonFunctions: CommonFunctions) {
                window.onload = () => {
                  // Perform initialization logic here
                  for (let i = 0; i < localStorage.length; i++) {
                    const key = localStorage.key(i);
                    if (key && key.startsWith("formData_")) {
                        localStorage.removeItem(key);
                    }
                }
                };
              }

  ngOnInit() {
    this.userSub = this.authenticationService.user.subscribe((user) => {
      // Check if user is authenticated or no
      this.isAuthenticated = !!user;

      // Once application is refreshed, redirect to dashboard
      if(this.isAuthenticated) {
        this.commonFunctions.navigateToPage('./dashboard');
      }
    });

    // Relogin user on refresh
    this.authenticationService.autoLogin();

    // Set browser tab application name
    this.titleService.setTitle("Valoores Home Page");
  }

  ngOnDestroy() {
    this.userSub?.unsubscribe();
  }

}
