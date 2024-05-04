import dayjs from 'dayjs/esm';
import { IQuizResult } from 'app/entities/quiz-result/quiz-result.model';
import { IGroup } from 'app/entities/group/group.model';
import { IQuestion } from 'app/entities/question/question.model';
import { ISite } from 'app/entities/site/site.model';
import { ITopic } from 'app/entities/topic/topic.model';
import { ExamType } from 'app/entities/enumerations/exam-type.model';

export interface IQuiz {
  id: number;
  quizType?: keyof typeof ExamType | null;
  quizTitle?: string | null;
  quizDescription?: string | null;
  deadline?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  quizResults?: IQuizResult[] | null;
  groups?: IGroup[] | null;
  questions?: IQuestion[] | null;
  site7?: ISite | null;
  topic1?: ITopic | null;
}

export type NewQuiz = Omit<IQuiz, 'id'> & { id: null };
