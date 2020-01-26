import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { BranchUpdateComponent } from 'app/entities/branch/branch-update.component';
import { BranchService } from 'app/entities/branch/branch.service';
import { Branch } from 'app/shared/model/branch.model';

describe('Component Tests', () => {
  describe('Branch Management Update Component', () => {
    let comp: BranchUpdateComponent;
    let fixture: ComponentFixture<BranchUpdateComponent>;
    let service: BranchService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [BranchUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BranchUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BranchUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BranchService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Branch(123);
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
        const entity = new Branch();
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
