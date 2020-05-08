import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsiSharedModule } from 'app/shared/shared.module';
import { PartidaComponent } from './partida.component';
import { PartidaDetailComponent } from './partida-detail.component';
import { PartidaUpdateComponent } from './partida-update.component';
import { PartidaDeleteDialogComponent } from './partida-delete-dialog.component';
import { partidaRoute } from './partida.route';

@NgModule({
  imports: [PsiSharedModule, RouterModule.forChild(partidaRoute)],
  declarations: [PartidaComponent, PartidaDetailComponent, PartidaUpdateComponent, PartidaDeleteDialogComponent],
  entryComponents: [PartidaDeleteDialogComponent]
})
export class PsiPartidaModule {}
