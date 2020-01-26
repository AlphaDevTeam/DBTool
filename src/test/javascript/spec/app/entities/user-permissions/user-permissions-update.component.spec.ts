import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { UserPermissionsUpdateComponent } from 'app/entities/user-permissions/user-permissions-update.component';
import { UserPermissionsService } from 'app/entities/user-permissions/user-permissions.service';
import { UserPermissions } from 'app/shared/model/user-permissions.model';

describe('Component Tests', () => {
  describe('UserPermissions Management Update Component', () => {
    let comp: UserPermissionsUpdateComponent;
    let fixture: ComponentFixture<UserPermissionsUpdateComponent>;
    let service: UserPermissionsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [UserPermissionsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserPermissionsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserPermissionsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserPermissionsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserPermissions(123);
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
        const entity = new UserPermissions();
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
