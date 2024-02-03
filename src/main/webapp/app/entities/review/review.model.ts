import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IPart } from 'app/entities/part/part.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IReview {
  id: number;
  body?: string | null;
  rating?: number | null;
  reviewDate?: dayjs.Dayjs | null;
  site3?: Pick<ISite, 'id' | 'nameAr'> | null;
  part2?: Pick<IPart, 'id' | 'titleAr'> | null;
  userCustom3?: Pick<IUserCustom, 'id'> | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
