import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { ExUserAuthUpdateComponent } from 'app/entities/ex-user-auth/ex-user-auth-update.component';
import { ExUserAuthService } from 'app/entities/ex-user-auth/ex-user-auth.service';
import { ExUserAuth } from 'app/shared/model/ex-user-auth.model';

describe('Component Tests', () => {
  describe('ExUserAuth Management Update Component', () => {
    let comp: ExUserAuthUpdateComponent;
    let fixture: ComponentFixture<ExUserAuthUpdateComponent>;
    let service: ExUserAuthService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [ExUserAuthUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ExUserAuthUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExUserAuthUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExUserAuthService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExUserAuth(123);
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
        const entity = new ExUserAuth();
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
