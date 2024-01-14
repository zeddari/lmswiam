import dayjs from 'dayjs/esm';

import { IDiploma, NewDiploma } from './diploma.model';

export const sampleWithRequiredData: IDiploma = {
  id: 9835,
  type: 'BACCALAUREATE',
  title: 'drummer which aw',
};

export const sampleWithPartialData: IDiploma = {
  id: 9922,
  type: 'SECONDARY',
  title: 'quarrelsomely eek spotless',
  subject: '../fake-data/blob/hipster.txt',
  detail: '../fake-data/blob/hipster.txt',
  grade: 'throughout for',
  attachment: '../fake-data/blob/hipster.png',
  attachmentContentType: 'unknown',
};

export const sampleWithFullData: IDiploma = {
  id: 19959,
  type: 'LICENCE',
  title: 'fiercely green prettify',
  subject: '../fake-data/blob/hipster.txt',
  detail: '../fake-data/blob/hipster.txt',
  supervisor: '../fake-data/blob/hipster.txt',
  grade: 'uncoil quiet',
  graduationDate: dayjs('2024-01-09'),
  school: '../fake-data/blob/hipster.txt',
  attachment: '../fake-data/blob/hipster.png',
  attachmentContentType: 'unknown',
};

export const sampleWithNewData: NewDiploma = {
  type: 'PRIMARY',
  title: 'nauseate sunroom',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
