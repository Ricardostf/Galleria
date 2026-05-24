import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UsuarioService } from '../../../core/services/usuario.service';

import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-usuario-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    CardModule,
    InputTextModule,
    PasswordModule,
    ButtonModule,
    ToastModule
  ],
  providers: [MessageService],
  templateUrl: './usuario-form.component.html',
  styleUrls: ['./usuario-form.component.css']
})
export class UsuarioFormComponent implements OnInit {
  usuarioForm!: FormGroup;
  isEditMode: boolean = false;
  usuarioId!: number;
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.usuarioForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      login: ['', Validators.required],
      senha: ['']
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.usuarioId = Number(idParam);
      this.carregarUsuario(this.usuarioId);
    } else {
      this.usuarioForm.get('senha')?.setValidators([Validators.required]);
      this.usuarioForm.get('senha')?.updateValueAndValidity();
    }
  }

  carregarUsuario(id: number): void {
    this.loading = true;
    this.usuarioService.buscarPorId(id).subscribe({
      next: (usuario) => {
        this.usuarioForm.patchValue({
          nome: usuario.nome,
          login: usuario.login
        });
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar usuário' });
        this.loading = false;
        this.router.navigate(['/usuarios']);
      }
    });
  }

  onSubmit(): void {
    if (this.usuarioForm.valid) {
      this.loading = true;
      const formValue = this.usuarioForm.value;

      if (this.isEditMode) {
        this.usuarioService.atualizar(this.usuarioId, formValue).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuário atualizado com sucesso' });
            setTimeout(() => this.router.navigate(['/usuarios']), 1000);
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao atualizar usuário' });
            this.loading = false;
          }
        });
      } else {
        this.usuarioService.criar(formValue).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Usuário criado com sucesso' });
            setTimeout(() => this.router.navigate(['/usuarios']), 1000);
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao criar usuário' });
            this.loading = false;
          }
        });
      }
    } else {
      this.usuarioForm.markAllAsTouched();
    }
  }
}
