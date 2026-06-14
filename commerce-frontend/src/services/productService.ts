import { useQuery } from '@tanstack/react-query';
import { axiosClient } from '../api/axios';

export interface Product {
  id: string;
  name: string;
  slug: string;
  shortDescription?: string;
  description?: string;
  sku: string;
  price: number;
  comparePrice?: number;
  stockQuantity: number;
  brand?: string;
  imageUrl?: string;
  thumbnailUrl?: string;
  rating: number;
  reviewCount: number;
  isActive: boolean;
  isFeatured: boolean;
  category: string;
  categoryId: string;
}

export interface ProductsParams {
  search?: string;
  category?: string;
  minPrice?: number;
  maxPrice?: number;
  sort?: string;
  page?: number;
  size?: number;
}

export interface PaginatedProducts {
  content: Product[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

export const productService = {
  getProducts: async (params?: ProductsParams): Promise<PaginatedProducts> => {
    const { data } = await axiosClient.get<PaginatedProducts>('/v1/products', { params });
    return data;
  },

  getProduct: async (slug: string): Promise<Product> => {
    const { data } = await axiosClient.get<Product>(`/v1/products/${slug}`);
    return data;
  },

  createProduct: async (payload: any): Promise<Product> => {
    const { data } = await axiosClient.post<Product>('/v1/admin/products', payload);
    return data;
  },

  updateProduct: async (id: string, payload: any): Promise<Product> => {
    const { data } = await axiosClient.put<Product>(`/v1/admin/products/${id}`, payload);
    return data;
  },

  deleteProduct: async (id: string): Promise<void> => {
    await axiosClient.delete(`/v1/admin/products/${id}`);
  },
};

export function useProducts(params?: ProductsParams) {
  return useQuery({
    queryKey: ['products', params],
    queryFn: () => productService.getProducts(params),
  });
}

export function useProduct(slug: string) {
  return useQuery({
    queryKey: ['product', slug],
    queryFn: () => productService.getProduct(slug),
    enabled: Boolean(slug),
  });
}
