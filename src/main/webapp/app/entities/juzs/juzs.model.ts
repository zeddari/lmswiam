import dayjs from 'dayjs/esm';

export interface IJuzs {
  id: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewJuzs = Omit<IJuzs, 'id'> & { id: null };
