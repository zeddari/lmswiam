import dayjs from 'dayjs/esm';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { ISite } from 'app/entities/site/site.model';
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
  professors?: Pick<IUserCustom, 'id' | 'firstName'>[] | null;
  site1?: Pick<ISite, 'id' | 'nameAr'> | null;
  topic3?: Pick<ITopic, 'id' | 'titleAr'> | null;
}

export type NewCourse = Omit<ICourse, 'id'> & { id: null };
