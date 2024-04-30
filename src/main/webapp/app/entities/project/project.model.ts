import dayjs from 'dayjs/esm';
import { ISite } from 'app/entities/site/site.model';
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
  site12?: Pick<ISite, 'id' | 'nameAr'> | null;
  typeProject?: Pick<ITypeProject, 'id' | 'nameAr'> | null;
}

export type NewProject = Omit<IProject, 'id'> & { id: null };
