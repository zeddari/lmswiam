import dayjs from 'dayjs/esm';
import { IProgression } from 'app/entities/progression/progression.model';
import { ISessionLink } from 'app/entities/session-link/session-link.model';
import { ISessionCourses } from 'app/entities/session-courses/session-courses.model';
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
  progressions?: IProgression[] | null;
  links?: ISessionLink[] | null;
  courses?: ISessionCourses[] | null;
  site16?: ISite | null;
  session1?: ISession | null;
}

export type NewSessionInstance = Omit<ISessionInstance, 'id'> & { id: null };
