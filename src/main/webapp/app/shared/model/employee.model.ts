import { IBranch } from 'app/shared/model/branch.model';
import { IEmployeeType } from 'app/shared/model/employee-type.model';

export interface IEmployee {
  id?: number;
  empNumber?: string;
  firstName?: string;
  lastName?: string;
  knownName?: string;
  email?: string;
  isActive?: boolean;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageURL?: string;
  imageContentType?: string;
  image?: any;
  salary?: number;
  designation?: string;
  branch?: IBranch;
  empType?: IEmployeeType;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public empNumber?: string,
    public firstName?: string,
    public lastName?: string,
    public knownName?: string,
    public email?: string,
    public isActive?: boolean,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageURL?: string,
    public imageContentType?: string,
    public image?: any,
    public salary?: number,
    public designation?: string,
    public branch?: IBranch,
    public empType?: IEmployeeType
  ) {
    this.isActive = this.isActive || false;
  }
}
