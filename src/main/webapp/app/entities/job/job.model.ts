export interface IJob {
  id: number;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
}

export type NewJob = Omit<IJob, 'id'> & { id: null };
