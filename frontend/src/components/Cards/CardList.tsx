import React from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Chip,
  Box,
  Typography,
} from '@mui/material';
import { Edit as EditIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { Card } from '../../types';

interface CardListProps {
  cards: Card[];
  onEdit: (card: Card) => void;
  onDelete: (id: number) => void;
}

export const CardList: React.FC<CardListProps> = ({ cards, onEdit, onDelete }) => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'BLOCKED':
        return 'error';
      case 'EXPIRED':
        return 'warning';
      default:
        return 'default';
    }
  };

  const formatCardNumber = (number: string) => {
    return number.replace(/(\d{4})/g, '$1 ').trim();
  };

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Número de Tarjeta</TableCell>
            <TableCell>Titular</TableCell>
            <TableCell>Fecha de Expiración</TableCell>
            <TableCell>Límite de Crédito</TableCell>
            <TableCell>Saldo</TableCell>
            <TableCell>Estado</TableCell>
            <TableCell>Acciones</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {cards.map((card) => (
            <TableRow key={card.id}>
              <TableCell>
                <Typography variant="body2" fontFamily="monospace">
                  {formatCardNumber(card.cardNumber)}
                </Typography>
              </TableCell>
              <TableCell>{card.cardHolder}</TableCell>
              <TableCell>{card.expirationDate}</TableCell>
              <TableCell>${card.creditLimit.toFixed(2)}</TableCell>
              <TableCell>${card.balance.toFixed(2)}</TableCell>
              <TableCell>
                <Chip
                  label={card.status}
                  color={getStatusColor(card.status) as any}
                  size="small"
                />
              </TableCell>
              <TableCell>
                <Box>
                  <IconButton
                    size="small"
                    onClick={() => onEdit(card)}
                    color="primary"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    size="small"
                    onClick={() => onDelete(card.id)}
                    color="error"
                  >
                    <DeleteIcon />
                  </IconButton>
                </Box>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}; 