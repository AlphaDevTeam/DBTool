import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExUserAuth } from 'app/shared/model/ex-user-auth.model';
import { ExUserAuthService } from './ex-user-auth.service';

@Component({
  templateUrl: './ex-user-auth-delete-dialog.component.html'
})
export class ExUserAuthDeleteDialogComponent {
  exUserAuth?: IExUserAuth;

  constructor(
    protected exUserAuthService: ExUserAuthService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.exUserAuthService.delete(id).subscribe(() => {
      this.eventManager.broadcast('exUserAuthListModification');
      this.activeModal.close();
    });
  }
}
