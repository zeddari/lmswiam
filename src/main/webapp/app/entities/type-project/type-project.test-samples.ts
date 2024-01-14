import { ITypeProject, NewTypeProject } from './type-project.model';

export const sampleWithRequiredData: ITypeProject = {
  id: 9931,
  nameAr: 'bitten after',
  nameLat: 'mud active cavernous',
};

export const sampleWithPartialData: ITypeProject = {
  id: 1259,
  nameAr: 'enchanted righteously',
  nameLat: 'plus exactly behind',
};

export const sampleWithFullData: ITypeProject = {
  id: 31979,
  nameAr: 'jealously appraise develop',
  nameLat: 'manufacturing far via',
};

export const sampleWithNewData: NewTypeProject = {
  nameAr: 'whoever failing that',
  nameLat: 'gah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
