import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { BranchDetailComponent } from 'app/entities/branch/branch-detail.component';
import { Branch } from 'app/shared/model/branch.model';

describe('Component Tests', () => {
  describe('Branch Management Detail Component', () => {
    let comp: BranchDetailComponent;
    let fixture: ComponentFixture<BranchDetailComponent>;
    const route = ({ data: of({ branch: new Branch(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [BranchDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BranchDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BranchDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load branch on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.branch).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
