export interface IBank {
  id?: number;
  bankCode?: string;
  bankName?: string;
  isActive?: boolean;
}

export class Bank implements IBank {
  constructor(public id?: number, public bankCode?: string, public bankName?: string, public isActive?: boolean) {
    this.isActive = this.isActive || false;
  }
}
