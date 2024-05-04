import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IQuizResult {
  id: number;
  result?: number | null;
  submittedAT?: dayjs.Dayjs | null;
  site8?: ISite | null;
  quiz?: IQuiz | null;
  userCustom2?: IUserCustom | null;
}

export type NewQuizResult = Omit<IQuizResult, 'id'> & { id: null };
