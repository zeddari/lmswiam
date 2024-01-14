import dayjs from 'dayjs/esm';

import { IQuizResult, NewQuizResult } from './quiz-result.model';

export const sampleWithRequiredData: IQuizResult = {
  id: 28407,
  result: 9344.47,
  submittedAT: dayjs('2024-01-09T13:41'),
};

export const sampleWithPartialData: IQuizResult = {
  id: 31240,
  result: 20104.96,
  submittedAT: dayjs('2024-01-09T08:53'),
};

export const sampleWithFullData: IQuizResult = {
  id: 5716,
  result: 4999.4,
  submittedAT: dayjs('2024-01-09T13:47'),
};

export const sampleWithNewData: NewQuizResult = {
  result: 31180.7,
  submittedAT: dayjs('2024-01-09T17:15'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
