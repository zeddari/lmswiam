import dayjs from 'dayjs/esm';

export interface IAyahEdition {
  id: number;
  ayahId?: number | null;
  editionId?: number | null;
  data?: string | null;
  isAudio?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewAyahEdition = Omit<IAyahEdition, 'id'> & { id: null };
