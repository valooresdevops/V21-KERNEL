import { Component, Input } from '@angular/core';
import { CommonFunctions } from '../../common/CommonFunctions';
import { InformationService } from '../../services/information.service';

@Component({
  selector: 'v-bubble-button',
  templateUrl: './bubble-button.component.html',
  styleUrl: './bubble-button.component.css'
})
export class BubbleButtonComponent {
  @Input() public imgSrc: any;

  constructor(
    public commonFunctions: CommonFunctions,
    public informationservice: InformationService,
  ){}
  ngOnInit(): void
  {
// console.log(this.imgSrc);
  }
  onBubbleClicked(){
    // console.log("selected>>>>>>>>",this.informationservice.getAgGidSelectedNode());
    // this.commonFunctions.navigateToPage(this.menuVariable);
    console.log("Bubble Clicked !!!!!!!!!");
  }
}
