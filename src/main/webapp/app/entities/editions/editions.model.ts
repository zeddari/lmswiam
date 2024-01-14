import dayjs from 'dayjs/esm';

export interface IEditions {
  id: number;
  identifier?: string | null;
  language?: string | null;
  name?: string | null;
  englishName?: string | null;
  format?: string | null;
  type?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewEditions = Omit<IEditions, 'id'> & { id: null };
