import { IReview } from 'app/entities/review/review.model';
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
  parts?: IPart[] | null;
  reviews?: IReview[] | null;
  site2?: ISite | null;
  course?: ICourse | null;
  part1?: IPart | null;
}

export type NewPart = Omit<IPart, 'id'> & { id: null };
