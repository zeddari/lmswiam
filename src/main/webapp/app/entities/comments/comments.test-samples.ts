import dayjs from 'dayjs/esm';

import { IComments, NewComments } from './comments.model';

export const sampleWithRequiredData: IComments = {
  id: 23968,
  pseudoName: 'forceful meanwhile',
  type: 'BAD_TEACHER',
  title: 'talkative coolly',
};

export const sampleWithPartialData: IComments = {
  id: 26863,
  pseudoName: 'gosh rehome',
  type: 'BAD_TEACHER',
  title: 'hybridization',
  message: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IComments = {
  id: 31157,
  pseudoName: 'calmly aw what',
  type: 'OTHERS',
  title: 'likewise freely',
  message: '../fake-data/blob/hipster.txt',
  like: false,
  disLike: true,
  createdAt: dayjs('2024-01-08T20:03'),
  updatedAt: dayjs('2024-01-08T23:04'),
};

export const sampleWithNewData: NewComments = {
  pseudoName: 'wetly',
  type: 'OTHERS',
  title: 'underneath',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
