import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IExUserAuth, ExUserAuth } from 'app/shared/model/ex-user-auth.model';
import { ExUserAuthService } from './ex-user-auth.service';

@Component({
  selector: 'jhi-ex-user-auth-update',
  templateUrl: './ex-user-auth-update.component.html'
})
export class ExUserAuthUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    description: [null, [Validators.required]],
    isActive: []
  });

  constructor(protected exUserAuthService: ExUserAuthService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exUserAuth }) => {
      this.updateForm(exUserAuth);
    });
  }

  updateForm(exUserAuth: IExUserAuth): void {
    this.editForm.patchValue({
      id: exUserAuth.id,
      code: exUserAuth.code,
      description: exUserAuth.description,
      isActive: exUserAuth.isActive
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exUserAuth = this.createFromForm();
    if (exUserAuth.id !== undefined) {
      this.subscribeToSaveResponse(this.exUserAuthService.update(exUserAuth));
    } else {
      this.subscribeToSaveResponse(this.exUserAuthService.create(exUserAuth));
    }
  }

  private createFromForm(): IExUserAuth {
    return {
      ...new ExUserAuth(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      description: this.editForm.get(['description'])!.value,
      isActive: this.editForm.get(['isActive'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExUserAuth>>): void {
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
}
