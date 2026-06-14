import { useQuery } from '@tanstack/react-query';
import { axiosClient } from '../api/axios';

export interface Category {
  id: string;
  name: string;
  slug: string;
  description?: string;
  imageUrl?: string;
  isActive: boolean;
}

export const categoryService = {
  getCategories: async (): Promise<Category[]> => {
    const { data } = await axiosClient.get<Category[]>('/v1/categories');
    return data;
  },

  createCategory: async (payload: Omit<Category, 'id' | 'isActive'>): Promise<Category> => {
    const { data } = await axiosClient.post<Category>('/v1/admin/categories', payload);
    return data;
  },

  updateCategory: async (id: string, payload: Partial<Category>): Promise<Category> => {
    const { data } = await axiosClient.put<Category>(`/v1/admin/categories/${id}`, payload);
    return data;
  },

  deleteCategory: async (id: string): Promise<void> => {
    await axiosClient.delete(`/v1/admin/categories/${id}`);
  },
};

export function useCategories() {
  return useQuery({
    queryKey: ['categories'],
    queryFn: categoryService.getCategories,
  });
}
