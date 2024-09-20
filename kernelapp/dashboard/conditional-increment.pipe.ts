import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'conditionalIncrement'
})
export class ConditionalIncrementPipe implements PipeTransform {

  transform(index: number, gridIndexes: number[] , counter: number): number {
    if(gridIndexes.includes(index) )
      {
        return counter -1;
      }
      else
        if(gridIndexes.includes(index) )
        {
          return index;
          // this.counter += 1;
          // return index - this.counter;
        }
  }
}