import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPartida, Partida } from 'app/shared/model/partida.model';
import { PartidaService } from './partida.service';

@Component({
  selector: 'jhi-partida-update',
  templateUrl: './partida-update.component.html'
})
export class PartidaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    mandanteID: [],
    visitanteID: [],
    mandanteGols: [],
    visitanteGols: [],
    local: [],
    campeonato: []
  });

  constructor(protected partidaService: PartidaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partida }) => {
      this.updateForm(partida);
    });
  }

  updateForm(partida: IPartida): void {
    this.editForm.patchValue({
      id: partida.id,
      mandanteID: partida.mandanteID,
      visitanteID: partida.visitanteID,
      mandanteGols: partida.mandanteGols,
      visitanteGols: partida.visitanteGols,
      local: partida.local,
      campeonato: partida.campeonato
    });
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
      mandanteGols: this.editForm.get(['mandanteGols'])!.value,
      visitanteGols: this.editForm.get(['visitanteGols'])!.value,
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
