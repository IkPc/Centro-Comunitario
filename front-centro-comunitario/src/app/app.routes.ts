import { Routes } from '@angular/router';
import { ListaCentrosComponent } from './features/lista-centros/lista-centros.component';
import { IntercambioFormComponent } from './features/intercambio-form/intercambio-form.component';
import { RelatorioComponent } from './features/relatorio/relatorio.component';
import { HomeComponent } from './features/home/home.component';

export const routes: Routes = [
    { path: '',  redirectTo: 'homepage', pathMatch: 'full' },
    { path: 'homepage',  component: HomeComponent },
    { path: 'centros', component: ListaCentrosComponent },
    { path: 'intercambios', component: IntercambioFormComponent },
    { path: 'relatorios', component: RelatorioComponent },
];
