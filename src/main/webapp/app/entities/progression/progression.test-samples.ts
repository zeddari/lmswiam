import { IProgression, NewProgression } from './progression.model';

export const sampleWithRequiredData: IProgression = {
  id: 3471,
  attendance: 'ABSENT',
  lateArrival: true,
  earlyDeparture: true,
  taskDone: true,
  tajweedScore: 2,
  hifdScore: 3,
  adaeScore: 4,
};

export const sampleWithPartialData: IProgression = {
  id: 21737,
  attendance: 'ABSENT_AUTHORIZED',
  lateArrival: true,
  earlyDeparture: true,
  progressionMode: 'SLOW',
  riwaya: 'READING_ABU_JAAFAR_FROM_DURRAH_ROAD',
  fromAyaVerset: '../fake-data/blob/hipster.txt',
  toAyaVerset: '../fake-data/blob/hipster.txt',
  taskDone: false,
  tajweedScore: 2,
  hifdScore: 4,
  adaeScore: 1,
};

export const sampleWithFullData: IProgression = {
  id: 22553,
  attendance: 'ABSENT_AUTHORIZED',
  justifRef: 'coaxingly verbally',
  lateArrival: true,
  earlyDeparture: true,
  progressionMode: 'INTERMEDIATE',
  examType: 'MANDATORY',
  riwaya: 'AL_DURIS_NARRATION_ON_THE_AUTHORITY_OF_ABU_AMR_VIA_AL_SHATIBIYYAH',
  fromSourate: 'FATIHA',
  toSourate: 'AL3IMRAN',
  fromAyaNum: 9669,
  toAyaNum: 28226,
  fromAyaVerset: '../fake-data/blob/hipster.txt',
  toAyaVerset: '../fake-data/blob/hipster.txt',
  tilawaType: 'HIFD',
  taskDone: false,
  tajweedScore: 3,
  hifdScore: 4,
  adaeScore: 2,
  observation: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewProgression = {
  attendance: 'ABSENT_AUTHORIZED',
  lateArrival: true,
  earlyDeparture: false,
  taskDone: false,
  tajweedScore: 5,
  hifdScore: 2,
  adaeScore: 3,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
