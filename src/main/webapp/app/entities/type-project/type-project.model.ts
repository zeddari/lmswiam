import { IProject } from 'app/entities/project/project.model';

export interface ITypeProject {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
  projects?: IProject[] | null;
}

export type NewTypeProject = Omit<ITypeProject, 'id'> & { id: null };
