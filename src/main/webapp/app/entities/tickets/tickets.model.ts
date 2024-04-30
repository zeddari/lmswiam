import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { TicketSubjects } from 'app/entities/enumerations/ticket-subjects.model';
import { TicketStatus } from 'app/entities/enumerations/ticket-status.model';

export interface ITickets {
  id: number;
  subject?: keyof typeof TicketSubjects | null;
  title?: string | null;
  reference?: string | null;
  description?: string | null;
  justifDoc?: string | null;
  justifDocContentType?: string | null;
  dateTicket?: dayjs.Dayjs | null;
  dateProcess?: dayjs.Dayjs | null;
  processed?: keyof typeof TicketStatus | null;
  from?: dayjs.Dayjs | null;
  toDate?: dayjs.Dayjs | null;
  decisionDetail?: string | null;
  site18?: Pick<ISite, 'id' | 'nameAr'> | null;
  userCustom5?: Pick<IUserCustom, 'id' | 'firstName'> | null;
}

export type NewTickets = Omit<ITickets, 'id'> & { id: null };
