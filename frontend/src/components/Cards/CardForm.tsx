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
} from '@mui/material';
import { CardFormData } from '../../types';

interface CardFormProps {
  initialData?: Partial<CardFormData>;
  onSubmit: (data: CardFormData) => void;
  isLoading?: boolean;
}

export const CardForm: React.FC<CardFormProps> = ({
  initialData,
  onSubmit,
  isLoading = false,
}) => {
  const { control, handleSubmit, formState: { errors } } = useForm<CardFormData>({
    defaultValues: {
      cardHolder: '',
      expirationDate: '',
      cvv: '',
      creditLimit: 0,
      status: 'ACTIVE',
      ...initialData,
    },
  });

  return (
    <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
      <Stack spacing={2}>
        <Controller
          name="cardHolder"
          control={control}
          rules={{
            required: 'El nombre del titular es requerido',
            pattern: {
              value: /^[a-zA-Z\s]{2,50}$/,
              message: 'El nombre debe contener solo letras y espacios (2-50 caracteres)',
            },
          }}
          render={({ field }) => (
            <TextField
              {...field}
              label="Nombre del Titular"
              fullWidth
              error={!!errors.cardHolder}
              helperText={errors.cardHolder?.message}
            />
          )}
        />

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <Controller
            name="expirationDate"
            control={control}
            rules={{
              required: 'La fecha de expiración es requerida',
              pattern: {
                value: /^(0[1-9]|1[0-2])\/([2-9]\d)$/,
                message: 'Formato inválido (MM/YY)',
              },
            }}
            render={({ field }) => (
              <TextField
                {...field}
                label="Fecha de Expiración"
                type="month"
                fullWidth
                error={!!errors.expirationDate}
                helperText={errors.expirationDate?.message}
                InputLabelProps={{ shrink: true }}
                onChange={e => {
                  // Convierte YYYY-MM a MM/YY
                  const value = e.target.value;
                  if (value) {
                    const [year, month] = value.split('-');
                    field.onChange(`${month}/${year.slice(-2)}`);
                  } else {
                    field.onChange('');
                  }
                }}
                value={(() => {
                  // Convierte MM/YY a YYYY-MM para el input
                  if (!field.value) return '';
                  const [mm, yy] = field.value.split('/');
                  return `20${yy}-${mm}`;
                })()}
              />
            )}
          />

          <Controller
            name="cvv"
            control={control}
            rules={{
              required: 'El CVV es requerido',
              pattern: {
                value: /^\d{3}$/,
                message: 'El CVV debe ser de 3 dígitos',
              },
            }}
            render={({ field }) => (
              <TextField
                {...field}
                label="CVV"
                fullWidth
                error={!!errors.cvv}
                helperText={errors.cvv?.message}
              />
            )}
          />
        </Stack>

        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <Controller
            name="creditLimit"
            control={control}
            rules={{
              required: 'El límite de crédito es requerido',
              min: {
                value: 0,
                message: 'El límite debe ser mayor a 0',
              },
            }}
            render={({ field }) => (
              <TextField
                {...field}
                label="Límite de Crédito"
                type="number"
                fullWidth
                error={!!errors.creditLimit}
                helperText={errors.creditLimit?.message}
              />
            )}
          />

          <Controller
            name="status"
            control={control}
            rules={{ required: 'El estado es requerido' }}
            render={({ field }) => (
              <FormControl fullWidth error={!!errors.status}>
                <InputLabel>Estado</InputLabel>
                <Select {...field} label="Estado">
                  <MenuItem value="ACTIVE">Activa</MenuItem>
                  <MenuItem value="BLOCKED">Bloqueada</MenuItem>
                  <MenuItem value="EXPIRED">Expirada</MenuItem>
                </Select>
              </FormControl>
            )}
          />
        </Stack>

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