import { IBank } from 'app/shared/model/bank.model';
import { IExUser } from 'app/shared/model/ex-user.model';

export interface IBranch {
  id?: number;
  branchCode?: string;
  branchName?: string;
  isActive?: boolean;
  bank?: IBank;
  users?: IExUser[];
}

export class Branch implements IBranch {
  constructor(
    public id?: number,
    public branchCode?: string,
    public branchName?: string,
    public isActive?: boolean,
    public bank?: IBank,
    public users?: IExUser[]
  ) {
    this.isActive = this.isActive || false;
  }
}
