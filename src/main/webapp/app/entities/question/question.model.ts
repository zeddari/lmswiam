import { IAnswer } from 'app/entities/answer/answer.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';

export interface IQuestion {
  id: number;
  question?: string | null;
  details?: string | null;
  a1?: string | null;
  a1v?: boolean | null;
  a2?: string | null;
  a2v?: boolean | null;
  a3?: string | null;
  a3v?: boolean | null;
  a4?: string | null;
  a4v?: boolean | null;
  isActive?: boolean | null;
  answers?: IAnswer[] | null;
  quizzes?: IQuiz[] | null;
}

export type NewQuestion = Omit<IQuestion, 'id'> & { id: null };
