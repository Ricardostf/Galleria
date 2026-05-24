import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente } from './cliente.service';

export interface ItemPedido {
  id?: number;
  produtoId: number;
  produtoDescricao?: string;
  quantidade: number;
  valorUnitario?: number;
  subtotal?: number;
}

export interface Pedido {
  id?: number;
  numero?: string;
  dataEmissao?: string;
  descricao?: string;
  cliente?: Cliente;
  itens?: ItemPedido[];
  total?: number;
}

export interface PedidoRequest {
  descricao?: string;
  clienteId: number;
  produtosIds: number[];
}

@Injectable({
  providedIn: 'root'
})
export class PedidoService {
  private apiUrl = 'http://localhost:8080/pedidos';

  constructor(private http: HttpClient) {}

  listarTodos(): Observable<Pedido[]> {
    return this.http.get<Pedido[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<Pedido> {
    return this.http.get<Pedido>(`${this.apiUrl}/${id}`);
  }

  criar(pedido: PedidoRequest): Observable<Pedido> {
    return this.http.post<Pedido>(this.apiUrl, pedido);
  }
}
