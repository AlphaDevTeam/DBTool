import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';
import { CoreConfigurationItemsService } from './core-configuration-items.service';

@Component({
  templateUrl: './core-configuration-items-delete-dialog.component.html'
})
export class CoreConfigurationItemsDeleteDialogComponent {
  coreConfigurationItems?: ICoreConfigurationItems;

  constructor(
    protected coreConfigurationItemsService: CoreConfigurationItemsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coreConfigurationItemsService.delete(id).subscribe(() => {
      this.eventManager.broadcast('coreConfigurationItemsListModification');
      this.activeModal.close();
    });
  }
}
