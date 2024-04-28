import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/payment/payment.model';
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
  payments?: IPayment[] | null;
  site4?: ISite | null;
  userCustom4?: IUserCustom | null;
  course?: ICourse | null;
}

export type NewEnrolement = Omit<IEnrolement, 'id'> & { id: null };
