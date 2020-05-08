import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPartida, Partida } from 'app/shared/model/partida.model';
import { PartidaService } from './partida.service';

import { ITime } from 'app/shared/model/time.model';
import { TimeService } from '../time/time.service';

@Component({
  selector: 'jhi-partida-update',
  templateUrl: './partida-update.component.html',
  styleUrls: ['partida-update.component.scss']
})
export class PartidaUpdateComponent implements OnInit {
  isSaving = false;
  times?: ITime[];
  mandante = [];
  visitante = [];
  mandanteNome = '';
  mandanteID = null;
  visitanteNome = '';
  visitanteID = null;

  editForm = this.fb.group({
    id: [],
    mandanteID: [],
    mandanteNome: [],
    visitanteID: [],
    visitanteNome: [],
    golsVisitante: [],
    golsMandante: [],
    local: [],
    campeonato: []
  });

  constructor(
    protected partidaService: PartidaService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected timeSerice: TimeService
  ) {}

  ngOnInit(): void {
    this.timeSerice.query().subscribe((res: HttpResponse<ITime[]>) => (this.times = res.body || []));
    this.activatedRoute.data.subscribe(({ partida }) => {
      this.updateForm(partida);
    });
  }

  buscaTime(event: KeyboardEvent, type: string): void {
    const query = event.target!.value!;
    const resultArray = query ? this.times?.filter(time => time.nome?.toLowerCase().includes(query.toLowerCase())) : [];

    type === 'mandante' ? (this.mandante = resultArray) : (this.visitante = resultArray);
  }

  selecionaTime(event: MouseEvent, type: string): void {
    const timeNome = event.target!.textContent;
    const timeId = event.target!.getAttribute('id');

    if (type === 'mandante') {
      this.editForm.patchValue({
        mandanteID: timeId,
        mandanteNome: timeNome
      });
    } else {
      this.editForm.patchValue({
        visitanteID: timeId,
        visitanteNome: timeNome
      });
    }

    this.mandante = [];
    this.visitante = [];
  }

  updateForm(partida: IPartida): void {
    this.editForm.patchValue({
      id: partida.id,
      mandanteID: partida.mandanteID,
      visitanteID: partida.visitanteID,
      golsVisitante: partida.golsVisitante,
      golsMandante: partida.golsMandante,
      local: partida.local,
      campeonato: partida.campeonato
    });

    window.setTimeout(() => {
      const mandante = this.times.find(time => time.id === partida.mandanteID);
      const visitante = this.times.find(time => time.id === partida.visitanteID);

      this.editForm.patchValue({
        mandanteNome: mandante.nome,
        visitanteNome: visitante.nome
      });
    }, 500);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partida = this.createFromForm();
    if (partida.id !== undefined) {
      this.subscribeToSaveResponse(this.partidaService.update(partida));
    } else {
      this.subscribeToSaveResponse(this.partidaService.create(partida));
    }
  }

  private createFromForm(): IPartida {
    return {
      ...new Partida(),
      id: this.editForm.get(['id'])!.value,
      mandanteID: this.editForm.get(['mandanteID'])!.value,
      visitanteID: this.editForm.get(['visitanteID'])!.value,
      golsVisitante: this.editForm.get(['golsVisitante'])!.value,
      golsMandante: this.editForm.get(['golsMandante'])!.value,
      local: this.editForm.get(['local'])!.value,
      campeonato: this.editForm.get(['campeonato'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartida>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
