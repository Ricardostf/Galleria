import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ProdutoService } from '../../../core/services/produto.service';

import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    CardModule,
    InputTextModule,
    InputNumberModule,
    ButtonModule,
    ToastModule
  ],
  providers: [MessageService],
  templateUrl: './produto-form.component.html',
  styleUrls: ['./produto-form.component.css']
})
export class ProdutoFormComponent implements OnInit {
  produtoForm!: FormGroup;
  isEditMode: boolean = false;
  produtoId!: number;
  loading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private produtoService: ProdutoService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.produtoForm = this.fb.group({
      descricao: ['', [Validators.required]],
      valor: [null, [Validators.required, Validators.min(0.01)]]
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.produtoId = Number(idParam);
      this.carregarProduto(this.produtoId);
    }
  }

  carregarProduto(id: number): void {
    this.loading = true;
    this.produtoService.buscarPorId(id).subscribe({
      next: (produto) => {
        this.produtoForm.patchValue(produto);
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar produto' });
        this.loading = false;
        this.router.navigate(['/produtos']);
      }
    });
  }

  onSubmit(): void {
    if (this.produtoForm.valid) {
      this.loading = true;
      const formValue = this.produtoForm.value;

      if (this.isEditMode) {
        this.produtoService.atualizar(this.produtoId, formValue).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto atualizado com sucesso' });
            setTimeout(() => this.router.navigate(['/produtos']), 1000);
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao atualizar produto' });
            this.loading = false;
          }
        });
      } else {
        this.produtoService.criar(formValue).subscribe({
          next: () => {
            this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Produto criado com sucesso' });
            setTimeout(() => this.router.navigate(['/produtos']), 1000);
          },
          error: (err) => {
            this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao criar produto' });
            this.loading = false;
          }
        });
      }
    } else {
      this.produtoForm.markAllAsTouched();
    }
  }
}
