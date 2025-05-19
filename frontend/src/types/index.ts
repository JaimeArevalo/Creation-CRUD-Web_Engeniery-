export interface Card {
  id: number;
  cardNumber: string;
  cardHolder: string;
  expirationDate: string;
  cvv: string;
  creditLimit: number;
  balance: number;
  status: 'ACTIVE' | 'BLOCKED' | 'EXPIRED';
  createdAt: string;
  updatedAt: string;
  isActive: boolean;
}

export interface Transaction {
  id: number;
  cardId: number;
  amount: number;
  type: 'PURCHASE' | 'PAYMENT';
  description: string;
  transactionDate: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
}

export interface CardFormData {
  cardNumber?: string;
  cardHolder: string;
  expirationDate: string;
  cvv: string;
  creditLimit: number;
  balance?: number;
  status?: 'ACTIVE' | 'BLOCKED' | 'EXPIRED';
}

export interface TransactionFormData {
  cardId: number;
  amount: number;
  type: 'PURCHASE' | 'PAYMENT';
  description: string;
} 