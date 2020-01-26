import { IUser } from 'app/core/user/user.model';
import { ICompany } from 'app/shared/model/company.model';
import { IBranch } from 'app/shared/model/branch.model';
import { IUserGroup } from 'app/shared/model/user-group.model';
import { IUserPermissions } from 'app/shared/model/user-permissions.model';
import { IExUserAuth } from 'app/shared/model/ex-user-auth.model';

export interface IExUser {
  id?: number;
  userKey?: string;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  isActive?: boolean;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  country?: string;
  imageContentType?: string;
  image?: any;
  userLimit?: number;
  creditScore?: number;
  relatedUser?: IUser;
  company?: ICompany;
  branches?: IBranch[];
  userGroups?: IUserGroup[];
  userPermissions?: IUserPermissions[];
  userAuths?: IExUserAuth[];
}

export class ExUser implements IExUser {
  constructor(
    public id?: number,
    public userKey?: string,
    public login?: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public isActive?: boolean,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public country?: string,
    public imageContentType?: string,
    public image?: any,
    public userLimit?: number,
    public creditScore?: number,
    public relatedUser?: IUser,
    public company?: ICompany,
    public branches?: IBranch[],
    public userGroups?: IUserGroup[],
    public userPermissions?: IUserPermissions[],
    public userAuths?: IExUserAuth[]
  ) {
    this.isActive = this.isActive || false;
  }
}
