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
import { CardForm } from '../components/Cards/CardForm';
import { CardList } from '../components/Cards/CardList';
import { cardService } from '../services/cardService';
import { Card, CardFormData } from '../types';

export const CardsPage: React.FC = () => {
  const [page, setPage] = useState(1);
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedCard, setSelectedCard] = useState<Card | null>(null);
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

  const { data, isLoading, error } = useQuery({
    queryKey: ['cards', page],
    queryFn: () => cardService.getAll(page - 1),
  });

  const createMutation = useMutation({
    mutationFn: cardService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cards'] });
      setOpenDialog(false);
      setSnackbar({
        open: true,
        message: 'Tarjeta creada exitosamente',
        severity: 'success',
      });
    },
    onError: () => {
      setSnackbar({
        open: true,
        message: 'Error al crear la tarjeta',
        severity: 'error',
      });
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: CardFormData }) =>
      cardService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cards'] });
      setOpenDialog(false);
      setSnackbar({
        open: true,
        message: 'Tarjeta actualizada exitosamente',
        severity: 'success',
      });
    },
    onError: () => {
      setSnackbar({
        open: true,
        message: 'Error al actualizar la tarjeta',
        severity: 'error',
      });
    },
  });

  const deleteMutation = useMutation({
    mutationFn: cardService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cards'] });
      setSnackbar({
        open: true,
        message: 'Tarjeta eliminada exitosamente',
        severity: 'success',
      });
    },
    onError: () => {
      setSnackbar({
        open: true,
        message: 'Error al eliminar la tarjeta',
        severity: 'error',
      });
    },
  });

  const handleOpenDialog = (card?: Card) => {
    setSelectedCard(card || null);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedCard(null);
  };

  const handleSubmit = (formData: CardFormData) => {
    if (selectedCard) {
      updateMutation.mutate({ id: selectedCard.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const handleDelete = (id: number) => {
    if (window.confirm('¿Está seguro de eliminar esta tarjeta?')) {
      deleteMutation.mutate(id);
    }
  };

  if (error) {
    return (
      <Alert severity="error">
        Error al cargar las tarjetas. Por favor, intente nuevamente.
      </Alert>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h4">Tarjetas</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
        >
          Nueva Tarjeta
        </Button>
      </Box>

      {isLoading ? (
        <Typography>Cargando...</Typography>
      ) : (
        <>
          <CardList
            cards={data?.content || []}
            onEdit={handleOpenDialog}
            onDelete={handleDelete}
          />
          {data && data.totalPages > 1 && (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
              <Pagination
                count={data.totalPages}
                page={page}
                onChange={(_, value) => setPage(value)}
                color="primary"
              />
            </Box>
          )}
        </>
      )}

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {selectedCard ? 'Editar Tarjeta' : 'Nueva Tarjeta'}
        </DialogTitle>
        <DialogContent>
          <CardForm
            initialData={selectedCard || undefined}
            onSubmit={handleSubmit}
            isLoading={createMutation.isPending || updateMutation.isPending}
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