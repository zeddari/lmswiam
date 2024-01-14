import { IQuestion, NewQuestion } from './question.model';

export const sampleWithRequiredData: IQuestion = {
  id: 9428,
  question: '../fake-data/blob/hipster.txt',
  a1: '../fake-data/blob/hipster.txt',
  a1v: false,
  a2: '../fake-data/blob/hipster.txt',
  a2v: false,
  isActive: false,
};

export const sampleWithPartialData: IQuestion = {
  id: 15560,
  question: '../fake-data/blob/hipster.txt',
  a1: '../fake-data/blob/hipster.txt',
  a1v: true,
  a2: '../fake-data/blob/hipster.txt',
  a2v: true,
  a4: '../fake-data/blob/hipster.txt',
  isActive: false,
};

export const sampleWithFullData: IQuestion = {
  id: 25803,
  question: '../fake-data/blob/hipster.txt',
  details: '../fake-data/blob/hipster.txt',
  a1: '../fake-data/blob/hipster.txt',
  a1v: false,
  a2: '../fake-data/blob/hipster.txt',
  a2v: false,
  a3: '../fake-data/blob/hipster.txt',
  a3v: false,
  a4: '../fake-data/blob/hipster.txt',
  a4v: true,
  isActive: true,
};

export const sampleWithNewData: NewQuestion = {
  question: '../fake-data/blob/hipster.txt',
  a1: '../fake-data/blob/hipster.txt',
  a1v: false,
  a2: '../fake-data/blob/hipster.txt',
  a2v: true,
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
