import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { ICourse } from 'app/entities/course/course.model';
import { EnrolementType } from 'app/entities/enumerations/enrolement-type.model';

export interface IEnrolement {
  id: number;
  ref?: string | null;
  enrolementType?: keyof typeof EnrolementType | null;
  enrolmentStartTime?: dayjs.Dayjs | null;
  enrolemntEndTime?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  activatedAt?: dayjs.Dayjs | null;
  site4?: Pick<ISite, 'id' | 'nameAr'> | null;
  userCustom4?: Pick<IUserCustom, 'id' | 'firstName'> | null;
  course?: Pick<ICourse, 'id' | 'titleAr'> | null;
}

export type NewEnrolement = Omit<IEnrolement, 'id'> & { id: null };
