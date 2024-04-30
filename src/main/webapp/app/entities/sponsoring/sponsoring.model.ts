import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { IProject } from 'app/entities/project/project.model';
import { ICurrency } from 'app/entities/currency/currency.model';

export interface ISponsoring {
  id: number;
  ref?: string | null;
  message?: string | null;
  amount?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  isAlways?: boolean | null;
  site10?: Pick<ISite, 'id' | 'nameAr'> | null;
  sponsor?: Pick<IUserCustom, 'id' | 'firstName'> | null;
  project?: Pick<IProject, 'id' | 'titleAr'> | null;
  currency?: Pick<ICurrency, 'id' | 'nameAr'> | null;
}

export type NewSponsoring = Omit<ISponsoring, 'id'> & { id: null };
