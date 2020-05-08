import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartida } from 'app/shared/model/partida.model';

import { ITime } from 'app/shared/model/time.model';
import { TimeService } from '../time/time.service';

@Component({
  selector: 'jhi-partida-detail',
  templateUrl: './partida-detail.component.html'
})
export class PartidaDetailComponent implements OnInit {
  partida: IPartida | null = null;
  times?: ITime[];

  constructor(protected activatedRoute: ActivatedRoute, protected timeSerice: TimeService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partida }) => (this.partida = partida));
    this.timeSerice.query().subscribe((res: HttpResponse<ITime[]>) => (this.times = res.body || []));
  }

  getTime(id: number): string {
    const thisTime = this.times?.find(time => time.id === id);
    const teamName: string = thisTime!.nome!;

    return teamName;
  }

  previousState(): void {
    window.history.back();
  }
}
