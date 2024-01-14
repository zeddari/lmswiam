import { ICertificate } from 'app/entities/certificate/certificate.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { ICourse } from 'app/entities/course/course.model';

export interface ITopic {
  id: number;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
  certificates?: ICertificate[] | null;
  quizzes?: IQuiz[] | null;
  topics?: ITopic[] | null;
  courses?: ICourse[] | null;
  topic2?: ITopic | null;
}

export type NewTopic = Omit<ITopic, 'id'> & { id: null };
