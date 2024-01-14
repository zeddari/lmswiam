import dayjs from 'dayjs/esm';

import { IQuiz, NewQuiz } from './quiz.model';

export const sampleWithRequiredData: IQuiz = {
  id: 11873,
  quizTitle: 'athwart inasmuch likely',
  deadline: dayjs('2024-01-08T20:59'),
  isActive: false,
};

export const sampleWithPartialData: IQuiz = {
  id: 19104,
  quizType: 'OPTIONAL',
  quizTitle: 'musician lamb deceivingly',
  quizDescription: '../fake-data/blob/hipster.txt',
  deadline: dayjs('2024-01-08T18:51'),
  isActive: false,
};

export const sampleWithFullData: IQuiz = {
  id: 30785,
  quizType: 'MANDATORY',
  quizTitle: 'though suddenly sift',
  quizDescription: '../fake-data/blob/hipster.txt',
  deadline: dayjs('2024-01-09T13:10'),
  isActive: true,
};

export const sampleWithNewData: NewQuiz = {
  quizTitle: 'attribute beneath grave',
  deadline: dayjs('2024-01-09T17:00'),
  isActive: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
