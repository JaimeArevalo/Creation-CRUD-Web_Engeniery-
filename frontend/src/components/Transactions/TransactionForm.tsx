import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import {
  Box,
  TextField,
  Button,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Stack,
  FormHelperText,
} from '@mui/material';
import { TransactionFormData } from '../../types';

interface TransactionFormProps {
  initialData?: Partial<TransactionFormData>;
  onSubmit: (data: TransactionFormData) => void;
  isLoading?: boolean;
  cards: { id: number; cardNumber: string }[];
}

export const TransactionForm: React.FC<TransactionFormProps> = ({
  initialData,
  onSubmit,
  isLoading = false,
  cards,
}) => {
  const { control, handleSubmit, formState: { errors } } = useForm<TransactionFormData>({
    defaultValues: {
      cardId: initialData?.cardId || 0,
      amount: initialData?.amount || 0,
      type: initialData?.type || 'PURCHASE',
      description: initialData?.description || '',
    },
  });

  return (
    <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
      <Stack spacing={2}>
        <Controller
          name="cardId"
          control={control}
          rules={{ 
            required: 'La tarjeta es requerida',
            validate: value => value !== 0 || 'Debe seleccionar una tarjeta'
          }}
          render={({ field }) => (
            <FormControl fullWidth error={!!errors.cardId}>
              <InputLabel>Tarjeta</InputLabel>
              <Select {...field} label="Tarjeta">
                <MenuItem value={0} disabled>
                  Seleccione una tarjeta
                </MenuItem>
                {cards.map((card) => (
                  <MenuItem key={card.id} value={card.id}>
                    {card.cardNumber}
                  </MenuItem>
                ))}
              </Select>
              {errors.cardId && (
                <FormHelperText>{errors.cardId.message}</FormHelperText>
              )}
            </FormControl>
          )}
        />

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <Controller
            name="amount"
            control={control}
            rules={{
              required: 'El monto es requerido',
              min: {
                value: 0.01,
                message: 'El monto debe ser mayor a 0',
              },
            }}
            render={({ field }) => (
              <TextField
                {...field}
                label="Monto"
                type="number"
                fullWidth
                error={!!errors.amount}
                helperText={errors.amount?.message}
                inputProps={{
                  step: "0.01",
                  min: "0.01"
                }}
              />
            )}
          />

          <Controller
            name="type"
            control={control}
            rules={{ required: 'El tipo es requerido' }}
            render={({ field }) => (
              <FormControl fullWidth error={!!errors.type}>
                <InputLabel>Tipo</InputLabel>
                <Select {...field} label="Tipo">
                  <MenuItem value="PURCHASE">Compra</MenuItem>
                  <MenuItem value="PAYMENT">Pago</MenuItem>
                </Select>
                {errors.type && (
                  <FormHelperText>{errors.type.message}</FormHelperText>
                )}
              </FormControl>
            )}
          />
        </Stack>

        <Controller
          name="description"
          control={control}
          rules={{
            required: 'La descripción es requerida',
            minLength: {
              value: 3,
              message: 'La descripción debe tener al menos 3 caracteres',
            },
            maxLength: {
              value: 100,
              message: 'La descripción no puede exceder los 100 caracteres',
            },
          }}
          render={({ field }) => (
            <TextField
              {...field}
              label="Descripción"
              fullWidth
              error={!!errors.description}
              helperText={errors.description?.message}
              inputProps={{
                maxLength: 100
              }}
            />
          )}
        />

        <Button
          type="submit"
          variant="contained"
          color="primary"
          fullWidth
          disabled={isLoading}
        >
          {isLoading ? 'Guardando...' : 'Guardar'}
        </Button>
      </Stack>
    </Box>
  );
}; 