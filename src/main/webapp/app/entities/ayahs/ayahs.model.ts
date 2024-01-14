import dayjs from 'dayjs/esm';

export interface IAyahs {
  id: number;
  number?: number | null;
  textdesc?: string | null;
  numberInSurah?: number | null;
  page?: number | null;
  surahId?: number | null;
  hizbId?: number | null;
  juzId?: number | null;
  sajda?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewAyahs = Omit<IAyahs, 'id'> & { id: null };
