export type ThemeMode = 'light' | 'dark';

export interface UserProfile {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: 'customer' | 'admin';
}

export interface Product {
  id: string;
  slug: string;
  name: string;
  price: number;
  rating: number;
  category: string;
  image: string;
  stock: number;
}

export interface CartItem extends Product {
  quantity: number;
}

export interface WishlistItem extends Product {}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}
