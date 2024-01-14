import dayjs from 'dayjs/esm';
import { ISponsoring } from 'app/entities/sponsoring/sponsoring.model';
import { ITypeProject } from 'app/entities/type-project/type-project.model';

export interface IProject {
  id: number;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
  goals?: string | null;
  requirements?: string | null;
  imageLink?: string | null;
  imageLinkContentType?: string | null;
  videoLink?: string | null;
  budget?: number | null;
  isActive?: boolean | null;
  activateAt?: dayjs.Dayjs | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  sponsorings?: ISponsoring[] | null;
  typeProject?: ITypeProject | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
