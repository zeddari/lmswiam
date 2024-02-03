import { ISite } from 'app/entities/site/site.model';
import { IQuestion } from 'app/entities/question/question.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IAnswer {
  id: number;
  a1v?: boolean | null;
  a2v?: boolean | null;
  a3v?: boolean | null;
  a4v?: boolean | null;
  result?: boolean | null;
  site6?: Pick<ISite, 'id' | 'nameAr'> | null;
  question?: Pick<IQuestion, 'id'> | null;
  userCustom1?: Pick<IUserCustom, 'id'> | null;
}

export type NewAnswer = Omit<IAnswer, 'id'> & { id: null };
