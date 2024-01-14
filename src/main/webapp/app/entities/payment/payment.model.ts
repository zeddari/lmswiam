import dayjs from 'dayjs/esm';
import { IEnrolement } from 'app/entities/enrolement/enrolement.model';
import { ISponsoring } from 'app/entities/sponsoring/sponsoring.model';
import { ISession } from 'app/entities/session/session.model';
import { ICurrency } from 'app/entities/currency/currency.model';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { PaymentType } from 'app/entities/enumerations/payment-type.model';

export interface IPayment {
  id: number;
  amount?: number | null;
  paymentMethod?: keyof typeof PaymentMode | null;
  paiedBy?: string | null;
  proof?: string | null;
  proofContentType?: string | null;
  paidAt?: dayjs.Dayjs | null;
  type?: keyof typeof PaymentType | null;
  validityStartTime?: dayjs.Dayjs | null;
  validityEndTime?: dayjs.Dayjs | null;
  details?: string | null;
  enrolment?: IEnrolement | null;
  sponsoring?: ISponsoring | null;
  session?: ISession | null;
  currency?: ICurrency | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
