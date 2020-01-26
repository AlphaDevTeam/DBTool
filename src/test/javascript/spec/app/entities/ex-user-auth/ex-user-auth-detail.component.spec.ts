import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { ExUserAuthDetailComponent } from 'app/entities/ex-user-auth/ex-user-auth-detail.component';
import { ExUserAuth } from 'app/shared/model/ex-user-auth.model';

describe('Component Tests', () => {
  describe('ExUserAuth Management Detail Component', () => {
    let comp: ExUserAuthDetailComponent;
    let fixture: ComponentFixture<ExUserAuthDetailComponent>;
    const route = ({ data: of({ exUserAuth: new ExUserAuth(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [ExUserAuthDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ExUserAuthDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExUserAuthDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load exUserAuth on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exUserAuth).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
