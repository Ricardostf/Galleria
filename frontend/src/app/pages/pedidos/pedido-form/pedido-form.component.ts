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
import { FormsModule } from '@angular/forms';

export interface ItemSelecionado {
  produto: Produto;
  quantidade: number;
}

@Component({
  selector: 'app-pedido-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
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

  produtoSelecionado: Produto | null = null;
  itensSelecionados: ItemSelecionado[] = [];

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
      clienteId: [null, [Validators.required]]
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

  adicionarProduto(): void {
    if (!this.produtoSelecionado) return;
    
    const index = this.itensSelecionados.findIndex(i => i.produto.id === this.produtoSelecionado!.id);
    
    if (index !== -1) {
      this.itensSelecionados[index].quantidade++;
    } else {
      this.itensSelecionados.push({ produto: this.produtoSelecionado, quantidade: 1 });
    }
    
    this.produtoSelecionado = null;
  }

  alterarQuantidade(item: ItemSelecionado, delta: number): void {
    const novaQuantidade = item.quantidade + delta;
    if (novaQuantidade > 0) {
      item.quantidade = novaQuantidade;
    }
  }

  removerProduto(item: ItemSelecionado): void {
    this.itensSelecionados = this.itensSelecionados.filter(i => i.produto.id !== item.produto.id);
  }

  getTotalParcial(): number {
    return this.itensSelecionados.reduce((total, item) => total + (item.produto.valor * item.quantidade), 0);
  }

  onSubmit(): void {
    if (this.pedidoForm.valid && !this.isViewMode) {
      if (this.itensSelecionados.length === 0) {
        this.messageService.add({ severity: 'warn', summary: 'Aviso', detail: 'Adicione pelo menos um produto ao pedido.' });
        return;
      }

      this.loading = true;
      const formValue = this.pedidoForm.value;
      
      // Expande a lista de itens para um array de IDs repetidos (como o backend espera)
      const produtosIds: number[] = [];
      this.itensSelecionados.forEach(item => {
        for (let i = 0; i < item.quantidade; i++) {
          produtosIds.push(item.produto.id!);
        }
      });

      const payload = {
        ...formValue,
        produtosIds
      };

      this.pedidoService.criar(payload).subscribe({
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
