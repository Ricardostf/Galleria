import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { Pedido, PedidoService } from '../../../core/services/pedido.service';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-pedido-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    TableModule,
    ButtonModule,
    CardModule,
    ToastModule
  ],
  providers: [MessageService],
  templateUrl: './pedido-list.component.html',
  styleUrls: ['./pedido-list.component.css']
})
export class PedidoListComponent implements OnInit {
  pedidos: Pedido[] = [];
  loading: boolean = true;

  constructor(
    private pedidoService: PedidoService,
    private router: Router,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.carregarPedidos();
  }

  carregarPedidos(): void {
    this.loading = true;
    this.pedidoService.listarTodos().subscribe({
      next: (data) => {
        this.pedidos = data;
        this.loading = false;
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Erro', detail: 'Falha ao carregar pedidos' });
        this.loading = false;
      }
    });
  }

  visualizar(id: number): void {
    this.router.navigate(['/pedidos/visualizar', id]);
  }
}
