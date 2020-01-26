import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { DbToolTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { CoreConfigurationItemsDeleteDialogComponent } from 'app/entities/core-configuration-items/core-configuration-items-delete-dialog.component';
import { CoreConfigurationItemsService } from 'app/entities/core-configuration-items/core-configuration-items.service';

describe('Component Tests', () => {
  describe('CoreConfigurationItems Management Delete Component', () => {
    let comp: CoreConfigurationItemsDeleteDialogComponent;
    let fixture: ComponentFixture<CoreConfigurationItemsDeleteDialogComponent>;
    let service: CoreConfigurationItemsService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [CoreConfigurationItemsDeleteDialogComponent]
      })
        .overrideTemplate(CoreConfigurationItemsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CoreConfigurationItemsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CoreConfigurationItemsService);
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
