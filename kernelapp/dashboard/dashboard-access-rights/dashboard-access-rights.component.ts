import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { CommonFunctions } from 'src/app/Kernel/common/CommonFunctions';
import { GlobalConstants } from 'src/app/Kernel/common/GlobalConstants';
import { DataService } from 'src/app/Kernel/services/data.service';
import { InformationService } from 'src/app/Kernel/services/information.service';

@Component
(
  {
    selector: 'app-dashboard-access-rights',
    templateUrl: './dashboard-access-rights.component.html',
    styleUrl: './dashboard-access-rights.component.css'
  }
)
export class DashboardAccessRightsComponent implements OnInit
{
  public getAllUsersData = GlobalConstants.getAllUsersData;
  public logedInUserId = this.informationservice.getLogeduserId();
  public usersDropdownValueRes: any;
  public templateCreatedBy: any;

  usersForm = new UntypedFormGroup
  (
    {
      dropdownUsers: new UntypedFormControl(''),
    }
  );


  constructor(
    private http: HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: any,
    public commonFunctions: CommonFunctions,
    private dialog: MatDialog,
    private dataservice: DataService,
    public informationservice: InformationService
  ) { }

  ngOnInit(): void
  {
    // get the id of the user that created the selected tab
    this.http.get<any>(GlobalConstants.getDashboardTemplateCreatedByForAccess + this.informationservice.getSelectedTabId(), { headers: GlobalConstants.headers }).subscribe(
      (res: any) =>
      {
        this.templateCreatedBy = res[0].createdBy;
      });
  }

  submit()
  {
    // make sure that at least 1 user is selected
    if (this.usersForm.get("dropdownUsers").value != '')
    {
      // Initialize an array to store the IDs of the selected users
      let numericIds: number[] = [];
  
      // Get the value of the 'dropdownUsers' field from the form
      const dropdownUsersValue = this.usersForm.get("dropdownUsers").value;
  
      // Check if the dropdown value is an array and if it contains at least one element
      if (Array.isArray(dropdownUsersValue) && dropdownUsersValue.length > 0)
      {
        // Clear the array (optional since it's already initialized above)
        numericIds = [];
  
        // Iterate through each selected item in the dropdown
        for (const item of dropdownUsersValue)
        {
          // Check if the item is an object and has an 'id' property
          if (typeof item === 'object' && item.hasOwnProperty('id'))
          {
            // Add the id to the numericIds array
            numericIds.push(item.id);
          }
          else
          {
            // If the item is not an object or doesn't have an 'id', add it directly
            numericIds.push(item);
          }
        }
        // Convert the numericIds array to a comma-separated string and assign it to usersDropdownValueRes
        this.usersDropdownValueRes = numericIds.join(',');
      }
      else
      {
        numericIds = []; // Clear the array if no valid items are found
        this.usersDropdownValueRes = numericIds; // Assign an empty array
      }
      
      // Store the final list of user IDs
      let userIds = this.usersDropdownValueRes;
      
      // Check if the template creator's ID (this.templateCreatedBy) is already in the list of userIds
      if (userIds.includes(this.templateCreatedBy))
      {
        // If the creator's ID is already included, do nothing
      }
      else
      {
        // If not, append the creator's ID to the list of userIds
        userIds += "," + this.templateCreatedBy;
      }
      
      // Send an HTTP GET request to update the dashboard template with the selected user IDs
      this.http.get<any>(GlobalConstants.updateDashboardTemplateUsersIds + this.informationservice.getSelectedTabId() + "/" + userIds, { headers: GlobalConstants.headers }).subscribe(
        (res: any) =>
        {
          // Handle the response if needed (currently empty)
        });

        // Close all dialogs (presumably related to this action)
        this.closedialogall();
    }
    else
    {
      // If no users were selected, reset the 'dropdownUsers' field and display an alert
      this.usersForm.get("dropdownUsers").setValue('');
      alert("Please select at least one user!");
    }
  }

  closedialogall()
  {
    this.dialog.closeAll();
  }
}