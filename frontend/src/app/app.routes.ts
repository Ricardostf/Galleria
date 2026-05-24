import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { LayoutComponent } from './core/layout/layout.component';
import { UsuarioListComponent } from './pages/usuarios/usuario-list/usuario-list.component';
import { UsuarioFormComponent } from './pages/usuarios/usuario-form/usuario-form.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { 
        path: '', 
        component: LayoutComponent, 
        canActivate: [authGuard],
        children: [
            { path: '', redirectTo: 'usuarios', pathMatch: 'full' },
            { path: 'usuarios', component: UsuarioListComponent },
            { path: 'usuarios/novo', component: UsuarioFormComponent },
            { path: 'usuarios/editar/:id', component: UsuarioFormComponent }
        ]
    }
];
