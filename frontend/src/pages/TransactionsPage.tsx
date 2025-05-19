import React, { useState } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  Typography,
  Pagination,
  Alert,
  Snackbar,
} from '@mui/material';
import { Add as AddIcon } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { TransactionForm } from '../components/Transactions/TransactionForm';
import { TransactionList } from '../components/Transactions/TransactionList';
import { transactionService } from '../services/transactionService';
import { cardService } from '../services/cardService';
import { Transaction, TransactionFormData } from '../types';

export const TransactionsPage: React.FC = () => {
  const [page, setPage] = useState(1);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState<Transaction | null>(null);
  const [snackbar, setSnackbar] = useState<{
    open: boolean;
    message: string;
    severity: 'success' | 'error';
  }>({
    open: false,
    message: '',
    severity: 'success',
  });

  const queryClient = useQueryClient();

  const { data: transactionsData, isLoading: isLoadingTransactions, error: transactionsError } = useQuery({
    queryKey: ['transactions', page],
    queryFn: () => transactionService.getAll(page - 1),
  });

  const { data: cardsData, isLoading: isLoadingCards, error: cardsError } = useQuery({
    queryKey: ['cards'],
    queryFn: () => cardService.getAll(0, 100),
  });

  const createMutation = useMutation({
    mutationFn: transactionService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      setOpenDialog(false);
      setSnackbar({
        open: true,
        message: 'Transacción creada exitosamente',
        severity: 'success',
      });
    },
    onError: (error: Error) => {
      setSnackbar({
        open: true,
        message: error.message || 'Error al crear la transacción',
        severity: 'error',
      });
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: TransactionFormData }) =>
      transactionService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      setOpenDialog(false);
      setSnackbar({
        open: true,
        message: 'Transacción actualizada exitosamente',
        severity: 'success',
      });
    },
    onError: (error: Error) => {
      setSnackbar({
        open: true,
        message: error.message || 'Error al actualizar la transacción',
        severity: 'error',
      });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: transactionService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] });
      setSnackbar({
        open: true,
        message: 'Transacción eliminada exitosamente',
        severity: 'success',
      });
    },
    onError: (error: Error) => {
      setSnackbar({
        open: true,
        message: error.message || 'Error al eliminar la transacción',
        severity: 'error',
      });
    },
  });

  const handleOpenDialog = (transaction?: Transaction) => {
    setSelectedTransaction(transaction || null);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedTransaction(null);
  };

  const handleSubmit = (formData: TransactionFormData) => {
    if (selectedTransaction) {
      updateMutation.mutate({ id: selectedTransaction.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleDelete = (id: number) => {
    if (window.confirm('¿Está seguro de eliminar esta transacción?')) {
      deleteMutation.mutate(id);
    }
  };

  if (transactionsError || cardsError) {
    return (
      <Alert severity="error">
        {transactionsError ? 'Error al cargar las transacciones' : 'Error al cargar las tarjetas'}. 
        Por favor, intente nuevamente.
      </Alert>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h4">Transacciones</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
          disabled={isLoadingCards || !cardsData?.content?.length}
        >
          Nueva Transacción
        </Button>
      </Box>

      {isLoadingTransactions || isLoadingCards ? (
        <Typography>Cargando...</Typography>
      ) : (
        <>
          {!cardsData?.content?.length ? (
            <Alert severity="warning">
              No hay tarjetas disponibles. Por favor, cree una tarjeta antes de crear una transacción.
            </Alert>
          ) : (
            <>
              <TransactionList
                transactions={transactionsData?.content || []}
                onEdit={handleOpenDialog}
                onDelete={handleDelete}
              />
              {transactionsData && transactionsData.totalPages > 1 && (
                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
                  <Pagination
                    count={transactionsData.totalPages}
                    page={page}
                    onChange={(_, value) => setPage(value)}
                    color="primary"
                  />
                </Box>
              )}
            </>
          )}
        </>
      )}

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {selectedTransaction ? 'Editar Transacción' : 'Nueva Transacción'}
        </DialogTitle>
        <DialogContent>
          <TransactionForm
            initialData={selectedTransaction || undefined}
            onSubmit={handleSubmit}
            isLoading={createMutation.isPending || updateMutation.isPending}
            cards={cardsData?.content || []}
          />
        </DialogContent>
      </Dialog>

      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert
          onClose={() => setSnackbar({ ...snackbar, open: false })}
          severity={snackbar.severity}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
}; 