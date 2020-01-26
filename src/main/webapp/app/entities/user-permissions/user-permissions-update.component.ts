import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IUserPermissions, UserPermissions } from 'app/shared/model/user-permissions.model';
import { UserPermissionsService } from './user-permissions.service';
import { IMenuItems } from 'app/shared/model/menu-items.model';
import { MenuItemsService } from 'app/entities/menu-items/menu-items.service';

@Component({
  selector: 'jhi-user-permissions-update',
  templateUrl: './user-permissions-update.component.html'
})
export class UserPermissionsUpdateComponent implements OnInit {
  isSaving = false;

  menuitems: IMenuItems[] = [];

  editForm = this.fb.group({
    id: [],
    userPermKey: [],
    userPermDescription: [],
    isActive: [],
    menuItems: [null, Validators.required]
  });

  constructor(
    protected userPermissionsService: UserPermissionsService,
    protected menuItemsService: MenuItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userPermissions }) => {
      this.updateForm(userPermissions);

      this.menuItemsService
        .query()
        .pipe(
          map((res: HttpResponse<IMenuItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IMenuItems[]) => (this.menuitems = resBody));
    });
  }

  updateForm(userPermissions: IUserPermissions): void {
    this.editForm.patchValue({
      id: userPermissions.id,
      userPermKey: userPermissions.userPermKey,
      userPermDescription: userPermissions.userPermDescription,
      isActive: userPermissions.isActive,
      menuItems: userPermissions.menuItems
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userPermissions = this.createFromForm();
    if (userPermissions.id !== undefined) {
      this.subscribeToSaveResponse(this.userPermissionsService.update(userPermissions));
    } else {
      this.subscribeToSaveResponse(this.userPermissionsService.create(userPermissions));
    }
  }

  private createFromForm(): IUserPermissions {
    return {
      ...new UserPermissions(),
      id: this.editForm.get(['id'])!.value,
      userPermKey: this.editForm.get(['userPermKey'])!.value,
      userPermDescription: this.editForm.get(['userPermDescription'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      menuItems: this.editForm.get(['menuItems'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPermissions>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IMenuItems): any {
    return item.id;
  }

  getSelected(selectedVals: IMenuItems[], option: IMenuItems): IMenuItems {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
