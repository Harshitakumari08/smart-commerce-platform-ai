import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { axiosClient } from '../api/axios';
import type { UserProfile } from '../types';

interface AuthContextValue {
  user: UserProfile | null;
  token: string | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (payload: any) => Promise<void>;
  logout: () => void;
  getCurrentUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<UserProfile | null>(null);
  const [token, setToken] = useState<string | null>(localStorage.getItem('accessToken'));
  const [loading, setLoading] = useState(Boolean(localStorage.getItem('accessToken')));

  const login = async (email: string, password: string) => {
    setLoading(true);
    try {
      const { data } = await axiosClient.post('/v1/auth/login', { email, password });
      localStorage.setItem('accessToken', data.accessToken);
      localStorage.setItem('refreshToken', data.refreshToken);
      setToken(data.accessToken);
      
      // Fetch user profile immediately after login
      const profileResponse = await axiosClient.get('/v1/auth/me');
      setUser(profileResponse.data as UserProfile);
    } finally {
      setLoading(false);
    }
  };

  const register = async (payload: any) => {
    setLoading(true);
    try {
      await axiosClient.post('/v1/auth/register', payload);
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setToken(null);
    setUser(null);
  };

  const getCurrentUser = async () => {
    setLoading(true);
    try {
      const { data } = await axiosClient.get('/v1/auth/me');
      setUser(data as UserProfile);
    } finally {
      setLoading(false);
    }
  };

  // Sync profile state on page load/mount if token is in local storage
  useEffect(() => {
    const initAuth = async () => {
      const storedToken = localStorage.getItem('accessToken');
      if (storedToken) {
        try {
          const { data } = await axiosClient.get('/v1/auth/me');
          setUser(data as UserProfile);
        } catch (error) {
          logout();
        } finally {
          setLoading(false);
        }
      } else {
        setLoading(false);
      }
    };
    initAuth();
  }, []);

  // Listen to forced logout events from the axios client
  useEffect(() => {
    const handleLogoutEvent = () => {
      logout();
    };
    window.addEventListener('auth-logout', handleLogoutEvent);
    return () => window.removeEventListener('auth-logout', handleLogoutEvent);
  }, []);

  const value = useMemo<AuthContextValue>(
    () => ({ user, token, isAuthenticated: Boolean(token), loading, login, register, logout, getCurrentUser }),
    [user, token, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
