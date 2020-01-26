import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DbToolTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { ExUserAuthDeleteDialogComponent } from 'app/entities/ex-user-auth/ex-user-auth-delete-dialog.component';
import { ExUserAuthService } from 'app/entities/ex-user-auth/ex-user-auth.service';

describe('Component Tests', () => {
  describe('ExUserAuth Management Delete Component', () => {
    let comp: ExUserAuthDeleteDialogComponent;
    let fixture: ComponentFixture<ExUserAuthDeleteDialogComponent>;
    let service: ExUserAuthService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [ExUserAuthDeleteDialogComponent]
      })
        .overrideTemplate(ExUserAuthDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExUserAuthDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExUserAuthService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.clear();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
