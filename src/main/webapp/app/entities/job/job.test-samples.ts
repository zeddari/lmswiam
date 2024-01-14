import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: 7238,
  titleAr: 'but fooey',
  titleLat: 'frantically ack unfortunately',
};

export const sampleWithPartialData: IJob = {
  id: 17431,
  titleAr: 'amid',
  titleLat: 'unless',
};

export const sampleWithFullData: IJob = {
  id: 584,
  titleAr: 'ferociously geez',
  titleLat: 'stimulation squander whereas',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewJob = {
  titleAr: 'burly finally anenst',
  titleLat: 'sub yearly zealous',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
