import dayjs from 'dayjs/esm';
import { IPart } from 'app/entities/part/part.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IReview {
  id: number;
  body?: string | null;
  rating?: number | null;
  reviewDate?: dayjs.Dayjs | null;
  part2?: IPart | null;
  userCustom3?: IUserCustom | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
