export interface ITypeProject {
  id: number;
  nameAr?: string | null;
  nameLat?: string | null;
}

export type NewTypeProject = Omit<ITypeProject, 'id'> & { id: null };
