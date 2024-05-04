import { ISite } from 'app/entities/site/site.model';
import { ISession } from 'app/entities/session/session.model';

export interface IClassroom {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  description?: string | null;
  site?: ISite | null;
  sessions6s?: ISession[] | null;
}

export type NewClassroom = Omit<IClassroom, 'id'> & { id: null };
