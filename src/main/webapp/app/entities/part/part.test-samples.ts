import { IPart, NewPart } from './part.model';

export const sampleWithRequiredData: IPart = {
  id: 14993,
  titleAr: 'sticky',
  titleLat: 'hefty huzzah purse',
};

export const sampleWithPartialData: IPart = {
  id: 4363,
  titleAr: 'winkle volumize glaze',
  titleLat: 'document brocolli',
  description: '../fake-data/blob/hipster.txt',
  duration: 5032,
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
};

export const sampleWithFullData: IPart = {
  id: 4755,
  titleAr: 'blessing sport',
  titleLat: 'whoa cloth voluminous',
  description: '../fake-data/blob/hipster.txt',
  duration: 7194,
  imageLink: '../fake-data/blob/hipster.png',
  imageLinkContentType: 'unknown',
  videoLink: 'throughout lest bicycle',
};

export const sampleWithNewData: NewPart = {
  titleAr: 'gift deliberately',
  titleLat: 'cleverly snake',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
