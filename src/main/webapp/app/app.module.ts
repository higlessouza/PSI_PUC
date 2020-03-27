import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { PsiSharedModule } from 'app/shared/shared.module';
import { PsiCoreModule } from 'app/core/core.module';
import { PsiAppRoutingModule } from './app-routing.module';
import { PsiHomeModule } from './home/home.module';
import { PsiEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    PsiSharedModule,
    PsiCoreModule,
    PsiHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PsiEntityModule,
    PsiAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class PsiAppModule {}
