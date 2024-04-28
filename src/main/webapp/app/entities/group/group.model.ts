import { ICertificate } from 'app/entities/certificate/certificate.model';
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
  certificates?: ICertificate[] | null;
  groups?: IGroup[] | null;
  elements?: IUserCustom[] | null;
  site11?: ISite | null;
  group1?: IGroup | null;
  quizzes?: IQuiz[] | null;
  sessions5s?: ISession[] | null;
}

export type NewGroup = Omit<IGroup, 'id'> & { id: null };
