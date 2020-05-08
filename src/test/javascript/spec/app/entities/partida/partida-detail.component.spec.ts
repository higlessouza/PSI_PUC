import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PsiTestModule } from '../../../test.module';
import { PartidaDetailComponent } from 'app/entities/partida/partida-detail.component';
import { Partida } from 'app/shared/model/partida.model';

describe('Component Tests', () => {
  describe('Partida Management Detail Component', () => {
    let comp: PartidaDetailComponent;
    let fixture: ComponentFixture<PartidaDetailComponent>;
    const route = ({ data: of({ partida: new Partida(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsiTestModule],
        declarations: [PartidaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PartidaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartidaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load partida on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.partida).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
