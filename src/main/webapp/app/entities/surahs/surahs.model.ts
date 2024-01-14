import dayjs from 'dayjs/esm';

export interface ISurahs {
  id: number;
  number?: number | null;
  nameAr?: string | null;
  nameEn?: string | null;
  nameEnTranslation?: string | null;
  type?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewSurahs = Omit<ISurahs, 'id'> & { id: null };
