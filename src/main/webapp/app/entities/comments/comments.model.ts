import dayjs from 'dayjs/esm';
import { ISession } from 'app/entities/session/session.model';
import { CommentType } from 'app/entities/enumerations/comment-type.model';

export interface IComments {
  id: number;
  pseudoName?: string | null;
  type?: keyof typeof CommentType | null;
  title?: string | null;
  message?: string | null;
  like?: boolean | null;
  disLike?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  sessions8s?: ISession[] | null;
}

export type NewComments = Omit<IComments, 'id'> & { id: null };
