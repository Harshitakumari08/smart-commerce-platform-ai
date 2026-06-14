import { apiClient } from '../api/client';

export const authService = {
  login: (payload: { email: string; password: string }) => apiClient.post('/v1/auth/login', payload),
  register: (payload: any) => apiClient.post('/v1/auth/register', payload),
};
