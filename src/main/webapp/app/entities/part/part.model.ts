import { ISite } from 'app/entities/site/site.model';
import { ICourse } from 'app/entities/course/course.model';

export interface IPart {
  id: number;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
  duration?: number | null;
  imageLink?: string | null;
  imageLinkContentType?: string | null;
  videoLink?: string | null;
  site2?: Pick<ISite, 'id' | 'nameAr'> | null;
  course?: Pick<ICourse, 'id' | 'titleAr'> | null;
  part1?: Pick<IPart, 'id' | 'titleAr'> | null;
}

export type NewPart = Omit<IPart, 'id'> & { id: null };
