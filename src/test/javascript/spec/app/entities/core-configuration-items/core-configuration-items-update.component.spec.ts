import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { CoreConfigurationItemsUpdateComponent } from 'app/entities/core-configuration-items/core-configuration-items-update.component';
import { CoreConfigurationItemsService } from 'app/entities/core-configuration-items/core-configuration-items.service';
import { CoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';

describe('Component Tests', () => {
  describe('CoreConfigurationItems Management Update Component', () => {
    let comp: CoreConfigurationItemsUpdateComponent;
    let fixture: ComponentFixture<CoreConfigurationItemsUpdateComponent>;
    let service: CoreConfigurationItemsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [CoreConfigurationItemsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(CoreConfigurationItemsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CoreConfigurationItemsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CoreConfigurationItemsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new CoreConfigurationItems(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new CoreConfigurationItems();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
