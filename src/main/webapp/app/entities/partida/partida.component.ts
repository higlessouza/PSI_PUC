import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartida } from 'app/shared/model/partida.model';
import { PartidaService } from './partida.service';
import { PartidaDeleteDialogComponent } from './partida-delete-dialog.component';

@Component({
  selector: 'jhi-partida',
  templateUrl: './partida.component.html'
})
export class PartidaComponent implements OnInit, OnDestroy {
  partidas?: IPartida[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected partidaService: PartidaService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.partidaService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IPartida[]>) => (this.partidas = res.body || []));
      return;
    }

    this.partidaService.query().subscribe((res: HttpResponse<IPartida[]>) => (this.partidas = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPartidas();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPartida): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPartidas(): void {
    this.eventSubscriber = this.eventManager.subscribe('partidaListModification', () => this.loadAll());
  }

  delete(partida: IPartida): void {
    const modalRef = this.modalService.open(PartidaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partida = partida;
  }
}
