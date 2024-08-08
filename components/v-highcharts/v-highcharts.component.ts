import { Component, ElementRef, Input, OnInit, Renderer2 } from '@angular/core';
import * as Highcharts from 'highcharts';
import HighchartsMore from 'highcharts/highcharts-more';

import HighchartsSolidGauge from 'highcharts/modules/solid-gauge';

HighchartsMore(Highcharts);
HighchartsSolidGauge(Highcharts);


@Component({
  selector: 'v-highcharts',
  templateUrl: './v-highcharts.component.html',
  styleUrls: ['./v-highcharts.component.css']
})
export class VHighChartsComponent implements OnInit {
  @Input() options: Highcharts.Options;
  @Input() Highcharts: typeof Highcharts;
  @Input() sizeCheck : Boolean; // used in chart builder form component
  constructor(private elementRef: ElementRef, 
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    // Disable credits in the chart options
    if (!this.options.credits) {
      this.options.credits = { enabled: false };
    }
    if(this.sizeCheck){
      this.applySizeCheckStyles();
    }
    this.Highcharts.chart(this.elementRef.nativeElement.querySelector('.chart-container'), this.options);
  }
  applySizeCheckStyles() {
    const elements = document.querySelectorAll('.chart-container');
    elements.forEach(element => {
      this.renderer.setStyle(element, 'width', '100%');
      this.renderer.setStyle(element, 'min-width', '40%');
      this.renderer.setStyle(element, 'height', '100%');
      this.renderer.setStyle(element, 'margin', '0 auto');
    });
  }
}
