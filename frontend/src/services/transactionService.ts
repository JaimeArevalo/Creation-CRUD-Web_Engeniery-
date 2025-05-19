import api from './api';
import { Transaction, TransactionFormData, PaginatedResponse } from '../types';

export const transactionService = {
  async getAll(page = 0, size = 100): Promise<PaginatedResponse<Transaction>> {
    const response = await api.get(`/transactions?page=${page}&size=${size}`);
    return response.data;
  },

  async getById(id: number): Promise<Transaction> {
    const response = await api.get(`/transactions/${id}`);
    return response.data;
  },

  async create(transaction: TransactionFormData): Promise<Transaction> {
    try {
      const response = await api.post('/transactions', transaction);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Error al crear la transacción');
    }
  },

  async update(id: number, transaction: TransactionFormData): Promise<Transaction> {
    try {
      const response = await api.put(`/transactions/${id}`, transaction);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Error al actualizar la transacción');
    }
  },

  async delete(id: number): Promise<void> {
    try {
      await api.delete(`/transactions/${id}`);
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Error al eliminar la transacción');
    }
  },

  async getByCardId(cardId: number): Promise<Transaction[]> {
    try {
      const response = await api.get(`/transactions/card/${cardId}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Error al obtener las transacciones de la tarjeta');
    }
  },

  async getReport(): Promise<any> {
    try {
      const response = await api.get('/transactions/report');
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Error al obtener el reporte');
    }
  },
}; 