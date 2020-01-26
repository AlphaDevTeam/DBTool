export interface ICoreConfigurationItems {
  id?: number;
  configCode?: string;
  configDescription?: string;
  isActive?: boolean;
}

export class CoreConfigurationItems implements ICoreConfigurationItems {
  constructor(public id?: number, public configCode?: string, public configDescription?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
