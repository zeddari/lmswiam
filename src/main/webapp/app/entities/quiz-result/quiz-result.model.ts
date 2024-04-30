import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IQuizResult {
  id: number;
  result?: number | null;
  submittedAT?: dayjs.Dayjs | null;
  site8?: Pick<ISite, 'id' | 'nameAr'> | null;
  quiz?: Pick<IQuiz, 'id'> | null;
  userCustom2?: Pick<IUserCustom, 'id' | 'firstName'> | null;
}

export type NewQuizResult = Omit<IQuizResult, 'id'> & { id: null };
