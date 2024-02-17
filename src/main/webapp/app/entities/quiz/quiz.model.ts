import dayjs from 'dayjs/esm';
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
  groups?: Pick<IGroup, 'id' | 'nameAr'>[] | null;
  questions?: Pick<IQuestion, 'id' | 'question'>[] | null;
  site7?: Pick<ISite, 'id' | 'nameAr'> | null;
  topic1?: Pick<ITopic, 'id' | 'titleAr'> | null;
}

export type NewQuiz = Omit<IQuiz, 'id'> & { id: null };
