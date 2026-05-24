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
            { path: 'usuarios/editar/:id', component: UsuarioFormComponent },
            { path: 'clientes', loadComponent: () => import('./pages/clientes/cliente-list/cliente-list.component').then(m => m.ClienteListComponent) },
            { path: 'clientes/novo', loadComponent: () => import('./pages/clientes/cliente-form/cliente-form.component').then(m => m.ClienteFormComponent) },
            { path: 'clientes/editar/:id', loadComponent: () => import('./pages/clientes/cliente-form/cliente-form.component').then(m => m.ClienteFormComponent) },
            { path: 'produtos', loadComponent: () => import('./pages/produtos/produto-list/produto-list.component').then(m => m.ProdutoListComponent) },
            { path: 'produtos/novo', loadComponent: () => import('./pages/produtos/produto-form/produto-form.component').then(m => m.ProdutoFormComponent) },
            { path: 'produtos/editar/:id', loadComponent: () => import('./pages/produtos/produto-form/produto-form.component').then(m => m.ProdutoFormComponent) }
        ]
    }
];
