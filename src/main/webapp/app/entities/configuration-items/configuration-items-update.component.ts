import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IConfigurationItems, ConfigurationItems } from 'app/shared/model/configuration-items.model';
import { ConfigurationItemsService } from './configuration-items.service';
import { ICoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';
import { CoreConfigurationItemsService } from 'app/entities/core-configuration-items/core-configuration-items.service';

@Component({
  selector: 'jhi-configuration-items-update',
  templateUrl: './configuration-items-update.component.html'
})
export class ConfigurationItemsUpdateComponent implements OnInit {
  isSaving = false;

  coreconfigurationitems: ICoreConfigurationItems[] = [];

  editForm = this.fb.group({
    id: [],
    configEnabled: [],
    configTypeCode: [],
    configParameter: [],
    coreConfig: [null, Validators.required]
  });

  constructor(
    protected configurationItemsService: ConfigurationItemsService,
    protected coreConfigurationItemsService: CoreConfigurationItemsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configurationItems }) => {
      this.updateForm(configurationItems);

      this.coreConfigurationItemsService
        .query()
        .pipe(
          map((res: HttpResponse<ICoreConfigurationItems[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: ICoreConfigurationItems[]) => (this.coreconfigurationitems = resBody));
    });
  }

  updateForm(configurationItems: IConfigurationItems): void {
    this.editForm.patchValue({
      id: configurationItems.id,
      configEnabled: configurationItems.configEnabled,
      configTypeCode: configurationItems.configTypeCode,
      configParameter: configurationItems.configParameter,
      coreConfig: configurationItems.coreConfig
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configurationItems = this.createFromForm();
    if (configurationItems.id !== undefined) {
      this.subscribeToSaveResponse(this.configurationItemsService.update(configurationItems));
    } else {
      this.subscribeToSaveResponse(this.configurationItemsService.create(configurationItems));
    }
  }

  private createFromForm(): IConfigurationItems {
    return {
      ...new ConfigurationItems(),
      id: this.editForm.get(['id'])!.value,
      configEnabled: this.editForm.get(['configEnabled'])!.value,
      configTypeCode: this.editForm.get(['configTypeCode'])!.value,
      configParameter: this.editForm.get(['configParameter'])!.value,
      coreConfig: this.editForm.get(['coreConfig'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfigurationItems>>): void {
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

  trackById(index: number, item: ICoreConfigurationItems): any {
    return item.id;
  }
}
