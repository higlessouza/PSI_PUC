import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsiSharedModule } from 'app/shared/shared.module';
import { TimeComponent } from './time.component';
import { TimeDetailComponent } from './time-detail.component';
import { TimeUpdateComponent } from './time-update.component';
import { TimeDeleteDialogComponent } from './time-delete-dialog.component';
import { timeRoute } from './time.route';

@NgModule({
  imports: [PsiSharedModule, RouterModule.forChild(timeRoute)],
  declarations: [TimeComponent, TimeDetailComponent, TimeUpdateComponent, TimeDeleteDialogComponent],
  entryComponents: [TimeDeleteDialogComponent]
})
export class PsiTimeModule {}
