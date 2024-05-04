import { ISessionCourses, NewSessionCourses } from './session-courses.model';

export const sampleWithRequiredData: ISessionCourses = {
  id: 19072,
  title: 'although supposing',
};

export const sampleWithPartialData: ISessionCourses = {
  id: 10394,
  title: 'till anti',
  resourceLink: 'puny gosh',
};

export const sampleWithFullData: ISessionCourses = {
  id: 1998,
  title: 'decimal humongous blazon',
  description: '../fake-data/blob/hipster.txt',
  resourceLink: 'yowza',
  resourceFile: '../fake-data/blob/hipster.png',
  resourceFileContentType: 'unknown',
};

export const sampleWithNewData: NewSessionCourses = {
  title: 'because conduct loyally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
