import dayjs from 'dayjs/esm';
import { ISessionLink } from 'app/entities/session-link/session-link.model';
import { ISite } from 'app/entities/site/site.model';
import { ISession } from 'app/entities/session/session.model';
import { Attendance } from 'app/entities/enumerations/attendance.model';

export interface ISessionInstance {
  id: number;
  title?: string | null;
  sessionDate?: dayjs.Dayjs | null;
  startTime?: dayjs.Dayjs | null;
  duration?: number | null;
  info?: string | null;
  attendance?: keyof typeof Attendance | null;
  justifRef?: string | null;
  isActive?: boolean | null;
  links?: Pick<ISessionLink, 'id' | 'title'>[] | null;
  site16?: Pick<ISite, 'id' | 'nameAr'> | null;
  session1?: Pick<ISession, 'id' | 'title'> | null;
}

export type NewSessionInstance = Omit<ISessionInstance, 'id'> & { id: null };
