import { IExUser } from 'app/shared/model/ex-user.model';

export interface IExUserAuth {
  id?: number;
  code?: string;
  description?: string;
  isActive?: boolean;
  users?: IExUser[];
}

export class ExUserAuth implements IExUserAuth {
  constructor(public id?: number, public code?: string, public description?: string, public isActive?: boolean, public users?: IExUser[]) {
    this.isActive = this.isActive || false;
  }
}
