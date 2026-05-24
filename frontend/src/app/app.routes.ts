import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
    // Outras rotas serão adicionadas aqui posteriormente, protegidas pelo authGuard
];
