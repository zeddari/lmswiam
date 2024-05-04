import { ISite } from 'app/entities/site/site.model';
import { ISessionInstance } from 'app/entities/session-instance/session-instance.model';
import { ISession } from 'app/entities/session/session.model';
import { SessionProvider } from 'app/entities/enumerations/session-provider.model';

export interface ISessionLink {
  id: number;
  provider?: keyof typeof SessionProvider | null;
  title?: string | null;
  description?: string | null;
  link?: string | null;
  site15?: ISite | null;
  sessions4s?: ISessionInstance[] | null;
  sessions7s?: ISession[] | null;
}

export type NewSessionLink = Omit<ISessionLink, 'id'> & { id: null };
