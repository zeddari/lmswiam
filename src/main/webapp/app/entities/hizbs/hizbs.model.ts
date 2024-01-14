import dayjs from 'dayjs/esm';

export interface IHizbs {
  id: number;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewHizbs = Omit<IHizbs, 'id'> & { id: null };
