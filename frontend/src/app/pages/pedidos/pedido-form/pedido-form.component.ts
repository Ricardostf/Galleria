import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Pedido, PedidoService } from '../../../core/services/pedido.service';
import { Cliente, ClienteService } from '../../../core/services/cliente.service';
import { Produto, ProdutoService } from '../../../core/services/produto.service';

import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { InputTextareaModule } from 'primeng/inputtextarea';

@Component({
  selector: 'app-pedido-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    CardModule,
    InputTextModule,
    DropdownModule,
    MultiSelectModule,
    ButtonModule,
    TableModule,
    ToastModule,
    InputTextareaModule
  ],
  providers: [MessageService],
  templateUrl: './pedido-form.component.html',
  styleUrls: ['./pedido-form.component.css']
})
export class PedidoFormComponent implements OnInit {
  pedidoForm!: FormGroup;
  isViewMode: boolean = false;
  pedidoId!: number;
  loading: boolean = false;
  
  clientes: Cliente[] = [];
  produtos: Produto[] = [];
  pedidoVisualizacao?: Pedido;

  constructor(
    private fb: FormBuilder,
    private pedidoService: PedidoService,
    private clienteService: ClienteService,
    private produtoService: ProdutoService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isViewMode = true;
      this.pedidoId = Number(idParam);
      this.carregarPedido(this.pedidoId);
    } else {
      this.initForm();
      this.carregarDados();
    }
  }

  initForm(): void {
    this.pedidoForm = this.fb.group({
      descricao: [''],
      clienteId: [null, [Validators.required]],
      produtosIds: [[], [Validators.required, Validators.minLength(1)]]
    });
  }

  carregarDados(): void {
    this.clienteService.listarTodos().subscribe(data => this.clientes = data);
    this.produtoService.listarTodos().subscribe(data => this.produtos = data);
  }

  carregarPedido(id: number): void {
    this.loading = true;
    this.pedidoService.buscarPorId(id).subscribe({
      next: (pedido) => {
        this.pedidoVisualizacao = pedido;
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar pedido' });
        this.loading = false;
        this.router.navigate(['/pedidos']);
      }
    });
  }

  onSubmit(): void {
    if (this.pedidoForm.valid && !this.isViewMode) {
      this.loading = true;
      const formValue = this.pedidoForm.value;

      this.pedidoService.criar(formValue).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Sucesso', detail: 'Pedido criado com sucesso' });
          setTimeout(() => this.router.navigate(['/pedidos']), 1000);
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Erro', detail: err.error?.message || 'Erro ao criar pedido' });
          this.loading = false;
        }
      });
    } else {
      this.pedidoForm?.markAllAsTouched();
    }
  }
}
