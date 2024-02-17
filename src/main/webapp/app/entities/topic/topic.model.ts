export interface ITopic {
  id: number;
  titleAr?: string | null;
  titleLat?: string | null;
  description?: string | null;
  topic2?: Pick<ITopic, 'id' | 'titleAr'> | null;
}

export type NewTopic = Omit<ITopic, 'id'> & { id: null };
