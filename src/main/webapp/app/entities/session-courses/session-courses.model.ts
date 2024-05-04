import { ISessionInstance } from 'app/entities/session-instance/session-instance.model';

export interface ISessionCourses {
  id: number;
  title?: string | null;
  description?: string | null;
  resourceLink?: string | null;
  resourceFile?: string | null;
  resourceFileContentType?: string | null;
  sessionsInstance5s?: ISessionInstance[] | null;
}

export type NewSessionCourses = Omit<ISessionCourses, 'id'> & { id: null };
