import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClienteService } from '../../../core/services/cliente.service';

import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputMaskModule } from 'primeng/inputmask';

@Component({
  selector: 'app-cliente-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    ToastModule,
    InputMaskModule
  ],
  providers: [MessageService],
  templateUrl: './cliente-form.component.html',
  styleUrls: ['./cliente-form.component.css']
})
export class ClienteFormComponent implements OnInit {
  clienteForm!: FormGroup;
  isEditMode: boolean = false;
  clienteId!: number;
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private clienteService: ClienteService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.clienteForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      cpf: ['', [Validators.required]],
      telefone: ['']
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.clienteId = Number(idParam);
      this.carregarCliente(this.clienteId);
    }
  }

  carregarCliente(id: number): void {
    this.loading = true;
    this.clienteService.buscarPorId(id).subscribe({
      next: (cliente) => {
        this.clienteForm.patchValue(cliente);
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar cliente' });
        this.loading = false;
        this.router.navigate(['/clientes']);
      }
    });
  }

  onSubmit(): void {
    if (this.clienteForm.valid) {
      this.loading = true;
      const formValue = this.clienteForm.value;

      if (this.isEditMode) {
        this.clienteService.atualizar(this.clienteId, formValue).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente atualizado com sucesso' });
            setTimeout(() => this.router.navigate(['/clientes']), 1000);
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao atualizar cliente' });
            this.loading = false;
          }
        });
      } else {
        this.clienteService.criar(formValue).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Cliente criado com sucesso' });
            setTimeout(() => this.router.navigate(['/clientes']), 1000);
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao criar cliente' });
            this.loading = false;
          }
        });
      }
    } else {
      this.clienteForm.markAllAsTouched();
    }
  }
}
