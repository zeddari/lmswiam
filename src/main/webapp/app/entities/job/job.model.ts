import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IJob {
  id: number;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
  userCustoms?: IUserCustom[] | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
