import dayjs from 'dayjs/esm';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';

export interface IQuizResult {
  id: number;
  result?: number | null;
  submittedAT?: dayjs.Dayjs | null;
  quiz?: IQuiz | null;
  userCustom2?: IUserCustom | null;
}

export type NewQuizResult = Omit<IQuizResult, 'id'> & { id: null };
