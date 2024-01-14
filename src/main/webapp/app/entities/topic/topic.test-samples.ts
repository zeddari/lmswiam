import { ITopic, NewTopic } from './topic.model';

export const sampleWithRequiredData: ITopic = {
  id: 13445,
  titleAr: 'recipe now',
  titleLat: 'publicize boohoo helplessly',
};

export const sampleWithPartialData: ITopic = {
  id: 31416,
  titleAr: 'opposite amass pain',
  titleLat: 'pfft even',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: ITopic = {
  id: 19638,
  titleAr: 'championship ah',
  titleLat: 'drat versus',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewTopic = {
  titleAr: 'regarding ha',
  titleLat: 'though allude',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
