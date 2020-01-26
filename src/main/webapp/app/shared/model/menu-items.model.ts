import { IUserPermissions } from 'app/shared/model/user-permissions.model';

export interface IMenuItems {
  id?: number;
  menuName?: string;
  menuURL?: string;
  isFavorite?: boolean;
  isActive?: boolean;
  userPermissions?: IUserPermissions[];
}

export class MenuItems implements IMenuItems {
  constructor(
    public id?: number,
    public menuName?: string,
    public menuURL?: string,
    public isFavorite?: boolean,
    public isActive?: boolean,
    public userPermissions?: IUserPermissions[]
  ) {
    this.isFavorite = this.isFavorite || false;
    this.isActive = this.isActive || false;
  }
}
