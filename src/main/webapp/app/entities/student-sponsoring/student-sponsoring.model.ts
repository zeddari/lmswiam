import dayjs from 'dayjs/esm';

export interface IStudentSponsoring {
  id: number;
  ref?: string | null;
  message?: string | null;
  amount?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  isAlways?: boolean | null;
}

export type NewStudentSponsoring = Omit<IStudentSponsoring, 'id'> & { id: null };
