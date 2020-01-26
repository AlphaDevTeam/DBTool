import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DbToolTestModule } from '../../../test.module';
import { CoreConfigurationItemsDetailComponent } from 'app/entities/core-configuration-items/core-configuration-items-detail.component';
import { CoreConfigurationItems } from 'app/shared/model/core-configuration-items.model';

describe('Component Tests', () => {
  describe('CoreConfigurationItems Management Detail Component', () => {
    let comp: CoreConfigurationItemsDetailComponent;
    let fixture: ComponentFixture<CoreConfigurationItemsDetailComponent>;
    const route = ({ data: of({ coreConfigurationItems: new CoreConfigurationItems(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [DbToolTestModule],
        declarations: [CoreConfigurationItemsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(CoreConfigurationItemsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CoreConfigurationItemsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load coreConfigurationItems on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.coreConfigurationItems).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
