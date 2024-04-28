import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
import { IUserCustom } from 'app/entities/user-custom/user-custom.model';
import { DiplomaType } from 'app/entities/enumerations/diploma-type.model';

export interface IDiploma {
  id: number;
  type?: keyof typeof DiplomaType | null;
  title?: string | null;
  subject?: string | null;
  detail?: string | null;
  supervisor?: string | null;
  grade?: string | null;
  graduationDate?: dayjs.Dayjs | null;
  school?: string | null;
  attachment?: string | null;
  attachmentContentType?: string | null;
  site20?: ISite | null;
  userCustom7s?: IUserCustom[] | null;
}

export type NewDiploma = Omit<IDiploma, 'id'> & { id: null };
