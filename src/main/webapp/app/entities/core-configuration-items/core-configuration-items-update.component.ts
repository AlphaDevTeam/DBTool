import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICoreConfigurationItems, CoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';
import { CoreConfigurationItemsService } from './core-configuration-items.service';

@Component({
  selector: 'jhi-core-configuration-items-update',
  templateUrl: './core-configuration-items-update.component.html'
})
export class CoreConfigurationItemsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    configCode: [null, [Validators.required]],
    configDescription: [null, [Validators.required]],
    isActive: []
  });

  constructor(
    protected coreConfigurationItemsService: CoreConfigurationItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coreConfigurationItems }) => {
      this.updateForm(coreConfigurationItems);
    });
  }

  updateForm(coreConfigurationItems: ICoreConfigurationItems): void {
    this.editForm.patchValue({
      id: coreConfigurationItems.id,
      configCode: coreConfigurationItems.configCode,
      configDescription: coreConfigurationItems.configDescription,
      isActive: coreConfigurationItems.isActive
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coreConfigurationItems = this.createFromForm();
    if (coreConfigurationItems.id !== undefined) {
      this.subscribeToSaveResponse(this.coreConfigurationItemsService.update(coreConfigurationItems));
    } else {
      this.subscribeToSaveResponse(this.coreConfigurationItemsService.create(coreConfigurationItems));
    }
  }

  private createFromForm(): ICoreConfigurationItems {
    return {
      ...new CoreConfigurationItems(),
      id: this.editForm.get(['id'])!.value,
      configCode: this.editForm.get(['configCode'])!.value,
      configDescription: this.editForm.get(['configDescription'])!.value,
      isActive: this.editForm.get(['isActive'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoreConfigurationItems>>): void {
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
