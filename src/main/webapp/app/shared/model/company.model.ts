import { Moment } from 'moment';
import { ILicenseType } from 'app/shared/model/license-type.model';
import { IConfigurationItems } from 'app/shared/model/configuration-items.model';

export interface ICompany {
  id?: number;
  companyCode?: string;
  companyName?: string;
  companyRegNumber?: string;
  email?: string;
  rating?: number;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  isActive?: boolean;
  apiKey?: string;
  expireOn?: Moment;
  licenseType?: ILicenseType;
  configitems?: IConfigurationItems[];
}

export class Company implements ICompany {
  constructor(
    public id?: number,
    public companyCode?: string,
    public companyName?: string,
    public companyRegNumber?: string,
    public email?: string,
    public rating?: number,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public isActive?: boolean,
    public apiKey?: string,
    public expireOn?: Moment,
    public licenseType?: ILicenseType,
    public configitems?: IConfigurationItems[]
  ) {
    this.isActive = this.isActive || false;
  }
}
