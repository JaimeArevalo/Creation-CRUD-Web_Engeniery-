import api from './api';
import { Card, CardFormData, PaginatedResponse } from '../types';

export const cardService = {
  async getAll(page = 0, size = 100): Promise<PaginatedResponse<Card>> {
    const response = await api.get(`/cards?page=${page}&size=${size}`);
    return response.data;
  },

  async getById(id: number): Promise<Card> {
    const response = await api.get(`/cards/${id}`);
    return response.data;
  },

  async create(card: CardFormData): Promise<Card> {
    const response = await api.post('/cards', card);
    return response.data;
  },

  async update(id: number, card: CardFormData): Promise<Card> {
    const response = await api.put(`/cards/${id}`, card);
    return response.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/cards/${id}`);
  },

  async getByNumber(cardNumber: string): Promise<Card> {
    const response = await api.get(`/cards/number/${cardNumber}`);
    return response.data;
  },
}; 