import { IGroup, NewGroup } from './group.model';

export const sampleWithRequiredData: IGroup = {
  id: 27345,
  groupType: 'SUPERVISOR',
  nameAr: 'shabby ugh mother-in-law',
  nameLat: 'violently trusting',
};

export const sampleWithPartialData: IGroup = {
  id: 12854,
  groupType: 'EMPTY',
  nameAr: 'worried optimal instead',
  nameLat: 'canine lest',
};

export const sampleWithFullData: IGroup = {
  id: 17960,
  groupType: 'STUDENT',
  nameAr: 'wherever enact geranium',
  nameLat: 'round superficial',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewGroup = {
  groupType: 'EMPTY',
  nameAr: 'aw',
  nameLat: 'hardship',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
