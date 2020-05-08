import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { PsiTestModule } from '../../../test.module';
import { PartidaComponent } from 'app/entities/partida/partida.component';
import { PartidaService } from 'app/entities/partida/partida.service';
import { Partida } from 'app/shared/model/partida.model';

describe('Component Tests', () => {
  describe('Partida Management Component', () => {
    let comp: PartidaComponent;
    let fixture: ComponentFixture<PartidaComponent>;
    let service: PartidaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PsiTestModule],
        declarations: [PartidaComponent]
      })
        .overrideTemplate(PartidaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartidaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PartidaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Partida(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.partidas && comp.partidas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
