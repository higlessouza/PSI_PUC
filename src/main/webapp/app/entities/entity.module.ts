import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'time',
        loadChildren: () => import('./time/time.module').then(m => m.PsiTimeModule)
      },
      {
        path: 'partida',
        loadChildren: () => import('./partida/partida.module').then(m => m.PsiPartidaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PsiEntityModule {}
