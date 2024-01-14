import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/payment/payment.model';
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
  payments?: IPayment[] | null;
  sponsor?: IUserCustom | null;
  project?: IProject | null;
  currency?: ICurrency | null;
}

export type NewSponsoring = Omit<ISponsoring, 'id'> & { id: null };
