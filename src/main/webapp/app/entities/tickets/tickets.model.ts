import dayjs from 'dayjs/esm';
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
  userCustom5?: IUserCustom | null;
}

export type NewTickets = Omit<ITickets, 'id'> & { id: null };
