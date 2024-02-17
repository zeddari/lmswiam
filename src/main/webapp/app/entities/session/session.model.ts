import dayjs from 'dayjs/esm';
import { IClassroom } from 'app/entities/classroom/classroom.model';
import { IGroup } from 'app/entities/group/group.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { ISessionLink } from 'app/entities/session-link/session-link.model';
import { ISite } from 'app/entities/site/site.model';
import { SessionMode } from 'app/entities/enumerations/session-mode.model';
import { SessionType } from 'app/entities/enumerations/session-type.model';
import { SessionJoinMode } from 'app/entities/enumerations/session-join-mode.model';
import { TargetedGender } from 'app/entities/enumerations/targeted-gender.model';

export interface ISession {
  id: number;
  sessionMode?: keyof typeof SessionMode | null;
  sessionType?: keyof typeof SessionType | null;
  sessionJoinMode?: keyof typeof SessionJoinMode | null;
  title?: string | null;
  description?: string | null;
  periodStartDate?: dayjs.Dayjs | null;
  periodeEndDate?: dayjs.Dayjs | null;
  sessionStartTime?: dayjs.Dayjs | null;
  sessionEndTime?: dayjs.Dayjs | null;
  sessionSize?: number | null;
  targetedGender?: keyof typeof TargetedGender | null;
  price?: number | null;
  thumbnail?: string | null;
  thumbnailContentType?: string | null;
  monday?: boolean | null;
  tuesday?: boolean | null;
  wednesday?: boolean | null;
  thursday?: boolean | null;
  friday?: boolean | null;
  saturday?: boolean | null;
  sunday?: boolean | null;
  isPeriodic?: boolean | null;
  isMinorAllowed?: boolean | null;
  isActive?: boolean | null;
  classrooms?: Pick<IClassroom, 'id' | 'nameAr'>[] | null;
  groups?: Pick<IGroup, 'id' | 'nameAr'>[] | null;
  professors?: Pick<IUserCustom, 'id' | 'firstName'>[] | null;
  employees?: Pick<IUserCustom, 'id' | 'firstName'>[] | null;
  links?: Pick<ISessionLink, 'id' | 'title'>[] | null;
  site14?: Pick<ISite, 'id' | 'nameAr'> | null;
}

export type NewSession = Omit<ISession, 'id'> & { id: null };
