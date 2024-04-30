import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { ISite } from 'app/entities/site/site.model';
import { IQuiz } from 'app/entities/quiz/quiz.model';
import { ISession } from 'app/entities/session/session.model';
import { GroupType } from 'app/entities/enumerations/group-type.model';

export interface IGroup {
  id: number;
  groupType?: keyof typeof GroupType | null;
  nameAr?: string | null;
  nameLat?: string | null;
  description?: string | null;
  elements?: Pick<IUserCustom, 'id' | 'firstName'>[] | null;
  site11?: Pick<ISite, 'id' | 'nameAr'> | null;
  group1?: Pick<IGroup, 'id' | 'nameAr'> | null;
  quizzes?: Pick<IQuiz, 'id' | 'quizTitle'>[] | null;
  sessions5s?: Pick<ISession, 'id' | 'title'>[] | null;
}

export type NewGroup = Omit<IGroup, 'id'> & { id: null };
