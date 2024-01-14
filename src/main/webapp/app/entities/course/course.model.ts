import dayjs from 'dayjs/esm';
import { IPart } from 'app/entities/part/part.model';
import { IEnrolement } from 'app/entities/enrolement/enrolement.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { ITopic } from 'app/entities/topic/topic.model';
import { Level } from 'app/entities/enumerations/level.model';

export interface ICourse {
  id: number;
  level?: keyof typeof Level | null;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
  subTitles?: string | null;
  requirements?: string | null;
  options?: string | null;
  duration?: number | null;
  imageLink?: string | null;
  imageLinkContentType?: string | null;
  videoLink?: string | null;
  price?: number | null;
  isActive?: boolean | null;
  activateAt?: dayjs.Dayjs | null;
  isConfirmed?: boolean | null;
  confirmedAt?: dayjs.Dayjs | null;
  parts?: IPart[] | null;
  enrolements?: IEnrolement[] | null;
  professors?: IUserCustom[] | null;
  topic3?: ITopic | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
