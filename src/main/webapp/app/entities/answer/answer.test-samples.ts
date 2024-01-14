import { IAnswer, NewAnswer } from './answer.model';

export const sampleWithRequiredData: IAnswer = {
  id: 31744,
  a1v: true,
  a2v: true,
  result: false,
};

export const sampleWithPartialData: IAnswer = {
  id: 14797,
  a1v: false,
  a2v: false,
  a3v: false,
  result: true,
};

export const sampleWithFullData: IAnswer = {
  id: 21671,
  a1v: true,
  a2v: false,
  a3v: true,
  a4v: true,
  result: false,
};

export const sampleWithNewData: NewAnswer = {
  a1v: true,
  a2v: true,
  result: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
