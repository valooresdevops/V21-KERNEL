// Used to handle AG-Grid Columns in any application
export class AgColumns {
  [x: string]: any;
  public parameters: string
  constructor(parameters:string) {
    this.parameters = parameters;
  }
}