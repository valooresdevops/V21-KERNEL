import { Component, ElementRef, Input, OnInit } from '@angular/core';
import * as Highcharts from 'highcharts/highstock';
import StockModule from 'highcharts/modules/stock';

// Initialize the stock module
StockModule(Highcharts);
@Component({
  selector: 'v-highcharts-stocks',
  templateUrl: './v-highcharts-stocks.component.html',
  styleUrls: ['./v-highcharts-stocks.component.css']
})
export class VHighchartsStocksComponent implements OnInit {
  @Input() options: Highcharts.Options;
  @Input() Highcharts: typeof Highcharts = Highcharts;

  chart: Highcharts.Chart;

  constructor(private elementRef: ElementRef) {}

  ngOnInit(): void {

    // Create the chart
    this.chart = this.Highcharts.stockChart(
      this.elementRef.nativeElement.querySelector('.chart-container'),
      this.options
    );

    if (!this.options.credits) {
      this.options.credits = { enabled: false };
    }
  }

  ngOnDestroy(): void {
    // Destroy the chart instance when component is destroyed
    if (this.chart) {
      this.chart.destroy();
    }
  }
}