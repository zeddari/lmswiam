import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 28232,
  body: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2024-01-09T06:28'),
};

export const sampleWithPartialData: IReview = {
  id: 3135,
  body: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2024-01-09T01:29'),
};

export const sampleWithFullData: IReview = {
  id: 31459,
  body: '../fake-data/blob/hipster.txt',
  rating: 1,
  reviewDate: dayjs('2024-01-09T05:35'),
};

export const sampleWithNewData: NewReview = {
  body: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2024-01-09T05:46'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
