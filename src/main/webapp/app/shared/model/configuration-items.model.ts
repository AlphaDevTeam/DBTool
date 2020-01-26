import { ICoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IConfigurationItems {
  id?: number;
  configEnabled?: boolean;
  configTypeCode?: string;
  configParameter?: string;
  coreConfig?: ICoreConfigurationItems;
  locations?: ICompany[];
}

export class ConfigurationItems implements IConfigurationItems {
  constructor(
    public id?: number,
    public configEnabled?: boolean,
    public configTypeCode?: string,
    public configParameter?: string,
    public coreConfig?: ICoreConfigurationItems,
    public locations?: ICompany[]
  ) {
    this.configEnabled = this.configEnabled || false;
  }
}
